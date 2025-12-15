# Custom Code Quality Rules - AEM Cloud Service

**Last updated:** December 14, 2025
**Sources:** 
- Adobe Experience League (official documentation via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Statistics

| Type | Count |
|------|-------|
| Total Rules | 50+ |
| Vulnerabilities | 3 |
| Security Hotspots | 0 |
| Bugs | 8 |
| Code Smells | 39+ |

---

## 🔴 Vulnerability Rules

### Do Not Use Potentially Dangerous Functions

* **Key**: CQRules:CWE-676
* **Type**: Vulnerability
* **Severity**: Major
* **Since**: Version 2018.4.0

The methods `Thread.stop()` and `Thread.interrupt()` can produce hard-to-reproduce issues and, sometimes, security vulnerabilities. Their usage should be tightly monitored and validated. In general, message passing is a safer way to accomplish similar goals.

#### Non-compliant code

```java
public class DontDoThis implements Runnable {
  private Thread thread;

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop() {
    thread.stop();  // UNSAFE!
  }

  public void run() {
    while (true) {
        somethingWhichTakesAWhileToDo();
    }
  }
}
```

#### Compliant code

```java
public class DoThis implements Runnable {
  private Thread thread;
  private boolean keepGoing = true;

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop() {
    keepGoing = false;
  }

  public void run() {
    while (this.keepGoing) {
        somethingWhichTakesAWhileToDo();
    }
  }
}
```

### Do not use format strings that may be externally controlled

* **Key**: CQRules:CWE-134
* **Type**: Vulnerability
* **Severity**: Major
* **Since**: Version 2018.4.0

Using a format string from an external source (such as a request parameter or user-generated content) can expose an application to denial of service attacks. There are circumstances where a format string may be externally controlled, but is only allowed from trusted sources.

#### Non-compliant code

```java
protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
  String messageFormat = request.getParameter("messageFormat");
  request.getResource().getValueMap().put("some property", String.format(messageFormat, "some text"));
  response.sendStatus(HttpServletResponse.SC_OK);
}
```

---

## 🔵 Bug Rules

### HTTP requests should always have socket and connect timeouts

* **Key**: CQRules:ConnectionTimeoutMechanism
* **Type**: Bug
* **Severity**: Critical
* **Since**: Version 2018.6.0

When executing HTTP requests from inside an AEM application, it is critical that proper timeouts are configured to avoid unnecessary thread consumption. Unfortunately, both Java™'s default HTTP Client, `java.net.HttpUrlConnection`, and the widely used Apache HTTP Components client do not have a default timeout. Therefore, timeouts must be explicitly configured. As a best practice, these timeouts should be no more than 60 seconds.

#### Non-compliant code

```java
@Reference
private HttpClientBuilderFactory httpClientBuilderFactory;

public void dontDoThis() {
  HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
  HttpClient httpClient = builder.build();

  // do something with the client
}

public void dontDoThisEither() {
  URL url = new URL("http://www.google.com");
  URLConnection urlConnection = url.openConnection();

  BufferedReader in = new BufferedReader(new InputStreamReader(
    urlConnection.getInputStream()));

  String inputLine;
  while ((inputLine = in.readLine()) != null) {
    logger.info(inputLine);
  }

  in.close();
}
```

#### Compliant code

```java
@Reference
private HttpClientBuilderFactory httpClientBuilderFactory;

public void doThis() {
  HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
  RequestConfig requestConfig = RequestConfig.custom()
    .setConnectTimeout(5000)
    .setSocketTimeout(5000)
    .build();
  builder.setDefaultRequestConfig(requestConfig);

  HttpClient httpClient = builder.build();

  // do something with the client
}

public void orDoThis() {
  URL url = new URL("http://www.google.com");
  URLConnection urlConnection = url.openConnection();
  urlConnection.setConnectTimeout(5000);
  urlConnection.setReadTimeout(5000);

  BufferedReader in = new BufferedReader(new InputStreamReader(
    urlConnection.getInputStream()));

  String inputLine;
  while ((inputLine = in.readLine()) != null) {
    logger.info(inputLine);
  }

  in.close();
}
```

### Customer packages should not create or edit nodes under `/libs`

* **Key**: BannedPath
* **Type**: Bug
* **Severity**: Blocker
* **Since**: Version 2019.6.0

It has been a long-standing best practice that the `/libs` content tree in the AEM content repository should be considered read-only by customers. Modifying nodes and properties under `/libs` creates significant risk for major and minor updates. Edits to `/libs` are only made by Adobe through official channels.

### Packages should not contain duplicate OSGi configurations

* **Key**: DuplicateOsgiConfigurations
* **Type**: Bug
* **Severity**: Major
* **Since**: Version 2019.6.0

A common problem that occurs in complex projects is where the same OSGi component is configured multiple times. This issue creates an ambiguity about which configuration is operable. This rule is "runmode-aware" in that it only identifies issues where the same component is configured multiple times in the same run mode or combination of run modes.

#### Non-compliant code

```
+ apps
  + projectA
    + config
      + com.day.cq.commons.impl.ExternalizerImpl
  + projectB
    + config
      + com.day.cq.commons.impl.ExternalizerImpl
```

#### Compliant code

```
+ apps
  + shared-config
    + config
      + com.day.cq.commons.impl.ExternalizerImpl
```

### Config and install folders should only contain OSGi nodes

* **Key**: ConfigAndInstallShouldOnlyContainOsgiNodes
* **Type**: Bug
* **Severity**: Major
* **Since**: Version 2019.6.0

For security reasons, paths containing `/config/` and `/install/` are only readable by administrative users in AEM and should be used only for OSGi configuration and OSGi bundles. Placing other types of content under paths that contain these segments results in application behavior that unintentionally varies between administrative and non-administrative users.

#### Non-compliant code

```
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    + config [nt:unstructured]
      + rtePlugins [nt:unstructured]
```

#### Compliant code

```
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    ./configPath = inplaceEditingConfig (String)
    + inplaceEditingConfig [nt:unstructured]
      + rtePlugins [nt:unstructured]
```

### Packages should not overlap

* **Key**: PackageOverlaps
* **Type**: Bug
* **Severity**: Major
* **Since**: Version 2019.6.0

Similar to the Packages Should Not Contain Duplicate OSGi Configurations rule, this issue is a common problem on complex projects where the same node path is written to by multiple separate content packages. While using content package dependencies can be used to ensure a consistent result, it is better to avoid overlaps entirely.

### Resources contained in proxy-enabled client libraries should be in a folder named resources

* **Key**: ClientlibProxyResource
* **Type**: Bug
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM client libraries may contain static resources like images and fonts. As described in the Using Client-Side Libraries documentation, when using proxied client libraries these static resources must be contained in a child folder named `resources` to be effectively referenced on the publish instances.

#### Non-compliant code

```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + images
        + myimage.jpg
```

#### Compliant code

```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + resources
        + myimage.jpg
```

### Customers should not implement or extend product APIs annotated with @ProviderType

* **Key**: CQBP-84
* **Type**: Bug
* **Severity**: Critical
* **Since**: Version 2018.7.0

The AEM API contains Java™ interfaces and classes that are only meant to be used, but not implemented, by custom code. For example, only AEM implements the interface `com.day.cq.wcm.api.Page`.

Adding new methods to these interfaces does not affect existing code, making the addition of new methods backwards-compatible. However, if custom code implements one of these interfaces, that custom code has introduced a backwards-compatibility risk for the customer.

#### Non-compliant Code

```java
import com.day.cq.wcm.api.Page;

public class DontDoThis implements Page {
// implementation here
}
```

---

## 🟡 Code Smell Rules

### The objects `ResourceResolver` should always be closed

* **Key**: CQRules:CQBP-72
* **Type**: Code Smell
* **Severity**: Major
* **Since**: Version 2018.4.0

`ResourceResolver` Objects obtained from the `ResourceResolverFactory` consume system resources. Although there are measures in place to reclaim these resources when a `ResourceResolver` is no longer in use, it is more efficient to close any opened `ResourceResolver` objects explicitly by calling the `close()` method.

#### Non-compliant code

```java
public void dontDoThis(Session session) throws Exception {
  ResourceResolver resolver = factory.getResourceResolver(Collections.singletonMap("user.jcr.session", (Object)session));
  // do some stuff with the resolver
}
```

#### Compliant code

```java
public void doThis(Session session) throws Exception {
  ResourceResolver resolver = null;
  try {
    resolver = factory.getResourceResolver(Collections.singletonMap("user.jcr.session", (Object)session));
    // do something with the resolver
  } finally {
    if (resolver != null) {
      resolver.close();
    }
  }
}

public void orDoThis(Session session) throws Exception {
  try (ResourceResolver resolver = factory.getResourceResolver(Collections.singletonMap("user.jcr.session", (Object) session))){
    // do something with the resolver
  }
}
```

### Do not use sling servlet paths to register servlets

* **Key**: CQRules:CQBP-75
* **Type**: Code Smell
* **Severity**: Major
* **Since**: Version 2018.4.0

As described in Sling documentation, binding servlets by paths are discouraged. Path-bound servlets cannot use standard JCR access controls and, as a result, require additional security rigor. Rather than using path-bound servlets, it is recommended to create nodes in the repository and register servlets by resource type.

#### Non-compliant code

```java
@Component(property = {
  "sling.servlet.paths=/apps/myco/endpoint"
})
public class DontDoThis extends SlingAllMethodsServlet {
 // implementation
}
```

### Caught exceptions should be logged or thrown, not both

* **Key**: CQRules:CQBP-44—CatchAndEitherLogOrThrow
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

In general, an exception should be logged exactly one time. Logging exceptions multiple times can cause confusion because it is unclear how many times an exception occurred. The most common pattern that leads to this issue is logging and throwing a caught exception.

#### Non-compliant code

```java
public void dontDoThis() throws Exception {
  try {
    someOperation();
  } catch (Exception e) {
    logger.error("something went wrong", e);
    throw e;
  }
}
```

#### Compliant code

```java
public void doThis() {
  try {
    someOperation();
  } catch (Exception e) {
    logger.error("something went wrong", e);
  }
}

public void orDoThis() throws MyCustomException {
  try {
    someOperation();
  } catch (Exception e) {
    throw new MyCustomException(e);
  }
}
```
### Avoid log statements immediately followed by throw statements

* **Key**: CQRules:CQBP-44—ConsecutivelyLogAndThrow
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

Another common pattern to avoid is to log a message and then immediately throw an exception. This issue generally indicates that the exception message ends up duplicated in log files.

#### Non-compliant code

```java
public void dontDoThis() throws Exception {
  logger.error("something went wrong");
  throw new RuntimeException("something went wrong");
}
```

#### Compliant code

```java
public void doThis() throws Exception {
  throw new RuntimeException("something went wrong");
}
```

### Avoid logging at INFO when handling GET or HEAD requests

* **Key**: CQRules:CQBP-44—LogInfoInGetOrHeadRequests
* **Type**: Code Smell
* **Severity**: Minor

In general, the INFO log level should be used to demarcate important actions and, by default, AEM is configured to log at the INFO level or above. GET and HEAD methods should only ever be read-only operations and thus do not constitute important actions. Logging at the INFO level in response to GET or HEAD requests is likely to create significant log noise, making it harder to identify useful information in log files.

#### Non-compliant code

```java
public void doGet() throws Exception {
  logger.info("handling a request from the user");
}
```

#### Compliant code

```java
public void doGet() throws Exception {
  logger.debug("handling a request from the user.");
}
```

### Do not use `Exception.getMessage()` as the first parameter of a logging statement

* **Key**: CQRules:CQBP-44—ExceptionGetMessageIsFirstLogParam
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

As a best practice, log messages should provide contextual information about where in the application an exception has occurred. While context can also be determined by using stack traces, in general the log message is going to be easier to read and understand. As a result, when logging an exception, it is a bad practice to use the exception's message as the log message.

#### Non-compliant code

```java
public void dontDoThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    logger.error(e.getMessage(), e);
  }
}
```

#### Compliant code

```java
public void doThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    logger.error("Unable to do something", e);
  }
}
```

### Logging in catch blocks should be at the WARN or ERROR level

* **Key**: CQRules:CQBP-44—WrongLogLevelInCatchBlock
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

As the name suggests, Java™ exceptions should always be used in exceptional circumstances. As a result, when an exception is caught, it is important to ensure that log messages are logged at the appropriate level: either WARN or ERROR.

#### Non-compliant code

```java
public void dontDoThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    logger.debug(e.getMessage(), e);
  }
}
```

#### Compliant code

```java
public void doThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    logger.error("Unable to do something", e);
  }
}
```

### Do not print stack traces to the console

* **Key**: CQRules:CQBP-44—ExceptionPrintStackTrace
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

Context is critical when understanding log messages. Using `Exception.printStackTrace()` causes only the stack trace to be output to the standard error stream, losing all context. Further, in a multi-threaded application like AEM, if multiple exceptions are printed using this method in parallel, their stack traces may overlap which produces significant confusion.

#### Non-compliant code

```java
public void dontDoThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    e.printStackTrace();
  }
}
```

#### Compliant code

```java
public void doThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    logger.error("Unable to do something", e);
  }
}
```

### Do not output to standard output or standard error

* **Key**: CQRules:CQBP-44—LogLevelConsolePrinters
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

Logging in AEM should always be done through the logging framework, SLF4J. Outputting directly to the standard output or standard error streams loses the structural and contextual information provided by the logging framework and may, sometimes, cause performance issues.

#### Non-compliant code

```java
public void dontDoThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    System.err.println("Unable to do something");
  }
}
```

#### Compliant code

```java
public void doThis() {
  try {
    someMethodThrowingAnException();
  } catch (Exception e) {
    logger.error("Unable to do something", e);
  }
}
```

### Avoid hardcoded `/apps` and `/libs` paths

* **Key**: CQRules:CQBP-71
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2018.4.0

Paths starting with `/libs` and `/apps` should generally not be hardcoded. These paths are typically stored relative to the Sling search path, which defaults to `/libs,/apps`. Using the absolute path may introduce subtle defects that would only appear later in the project lifecycle.

#### Non-compliant code

```java
public boolean dontDoThis(Resource resource) {
  return resource.isResourceType("/libs/foundation/components/text");
}
```

#### Compliant code

```java
public void doThis(Resource resource) {
  return resource.isResourceType("foundation/components/text");
}
```

### Sling scheduler should not be used

* **Key**: CQRules:AMSCORE-554
* **Type**: Code Smell / Cloud Service Compatibility
* **Severity**: Minor
* **Since**: Version 2020.5.0

Do not use Sling Scheduler for tasks that require a guaranteed execution. Sling Scheduled Jobs guarantee execution and better suited for both clustered and non-clustered environments.

### AEM deprecated APIs should not be used

* **Key**: AMSCORE-553
* **Type**: Code Smell / Cloud Service Compatibility
* **Severity**: Minor
* **Since**: Version 2020.5.0

The AEM API surface is under constant revision to identify APIs for which usage is discouraged and thus considered deprecated. Often, these APIs are deprecated using the standard Java™ *@Deprecated* annotation and, as such, as identified by `squid:CallToDeprecatedMethod`. However, there are cases where an API is deprecated in the context of AEM but may not be deprecated in other contexts.

### The default authoring mode should not be Classic UI

* **Key**: ClassicUIAuthoringMode
* **Type**: Code Smell / Cloud Service Compatibility
* **Severity**: Minor
* **Since**: Version 2020.5.0

The OSGi configuration `com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl` defines the default authoring mode within AEM. Because the Classic UI has been deprecated since AEM 6.4, an issue is now raised when the default authoring mode is configured to Classic UI.

### Components with dialog boxes should have touch UI dialog boxes

* **Key**: ComponentWithOnlyClassicUIDialog
* **Type**: Code Smell / Cloud Service Compatibility
* **Severity**: Minor
* **Since**: Version 2020.5.0

AEM Components with a Classic UI dialog should also have a Touch UI dialog for optimal authoring and compatibility with the Cloud Service deployment model, which does not support Classic UI. This rule verifies the following scenarios:

* A component with a Classic UI dialog (that is, a `dialog` child node) must have a corresponding Touch UI dialog (that is, a `cq:dialog` child node).
* A component with a Classic UI design dialog (that is, a `design_dialog` node) must have a corresponding Touch UI design dialog (that is, a `cq:design_dialog` child node).
* A component with both a Classic UI dialog and a Classic UI design dialog must have both a corresponding Touch UI dialog and a corresponding Touch UI design dialog.

### Reverse replication agents should not be used

* **Key**: ReverseReplication
* **Type**: Code Smell / Cloud Service Compatibility
* **Severity**: Minor
* **Since**: Version 2020.5.0

Support for reverse replication is not available in Cloud Service deployments, as described in Release Notes: Removal of Replication Agents. Customers using reverse replication should contact Adobe for alternative solutions.

### Usage of Cloud Service incompatible workflow processes

* **Key**: CloudServiceIncompatibleWorkflowProcess
* **Type**: Code Smell
* **Severity**: Blocker
* **Since**: Version 2021.2.0

With the move to Asset micro-services for asset processing on AEM Cloud Service, several workflow processes that were used in on-premise and AMS versions of AEM have become either unsupported or unnecessary.

### Usage of static templates is discouraged in favor of editable templates

* **Key**: StaticTemplateUsage
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

While the use of static templates has historically been common in AEM Projects, editable templates are highly recommended as they provide the most flexibility and support additional features not present in static templates.

### Usage of legacy foundation components is discouraged

* **Key**: LegacyFoundationComponentUsage
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

The legacy Foundation Components (that is, components under `/libs/foundation`) have been deprecated for several AEM releases in favor of the Core Components. Usage of the legacy Foundation Components as the basis for custom components, whether by overlay or inheritance, is discouraged and should be converted to the corresponding core component.

---

## 📦 OakPAL Rules

### Custom search index definition nodes must be direct children of `/oak:index`

* **Key**: OakIndexLocation
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM Cloud Service requires that custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) be direct child nodes of `/oak:index`. Indexes in other locations must be moved to be compatible with AEM Cloud Service.

### Custom search index definition nodes must have a compatVersion of 2

* **Key**: IndexCompatVersion
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM Cloud Service requires that custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) must have the `compatVersion` property set to `2`. AEM Cloud Service does not support any other value.

### Descendent nodes of custom search index definition nodes must be of type `nt:unstructured`

* **Key**: IndexDescendantNodeType
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

Hard-to-troubleshoot issues can occur when a custom search index definition node has unordered child nodes. To avoid such nodes, Adobe recommends that all descendent nodes of an `oak:QueryIndexDefinition` node be of type `nt:unstructured`.

### Custom search index definition nodes must contain a child node named `indexRules` that has children

* **Key**: IndexRulesNode
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

A properly defined custom search index definition node must include a child node named `indexRules` and this node must have at least one child.

### Custom search index definition nodes must follow naming conventions

* **Key**: IndexName
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM Cloud Service requires that custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) must be named following a specific pattern described on Content Search and Indexing.

### Custom search index definition nodes must use the index type lucene

* **Key**: IndexType
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM Cloud Service requires that custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) have a `type` property with the value set to `lucene`. Indexing using legacy index types must be updated before migration to AEM Cloud Service.

### Custom search index definition nodes must not contain a property named `seed`

* **Key**: IndexSeedProperty
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM Cloud Service prohibits custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) from containing a property named `seed`. Indexing using this property must be updated before migration to AEM Cloud Service.

### Custom search index definition nodes must not contain a property named `reindex`

* **Key**: IndexReindexProperty
* **Type**: Code Smell
* **Severity**: Minor
* **Since**: Version 2021.2.0

AEM Cloud Service prohibits custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) from containing a property named `reindex`. Indexing using this property must be updated before migration to AEM Cloud Service.

### Index definition nodes must not be deployed in UI content package

* **Key**: IndexNotUnderUIContent
* **Type**: Improvement
* **Severity**: Major
* **Since**: Version 2024.6.0

AEM Cloud Service prohibits custom search index definitions (nodes of type `oak:QueryIndexDefinition`) from being deployed in the UI Content package.

### Custom full-text index definition of type `damAssetLucene` must be correctly prefixed with `damAssetLucene`

* **Key**: CustomFulltextIndexesOfTheDamAssetCheck
* **Type**: Improvement
* **Severity**: Major
* **Since**: Version 2024.6.0

AEM Cloud Service prohibits custom full-text index definitions of type `damAssetLucene` from being prefixed with anything other than `damAssetLucene`.

### Index definition nodes must not contain properties with the same name

* **Key**: DuplicateNameProperty
* **Type**: Improvement
* **Severity**: Major
* **Since**: Version 2024.6.0

AEM Cloud Service prohibits custom search index definitions (that is, nodes of type `oak:QueryIndexDefinition`) from containing properties with the same name.

### Customizing of certain out-of-the-box index definitions is prohibited

* **Key**: RestrictIndexCustomization
* **Type**: Improvement
* **Severity**: Major
* **Since**: Version 2024.6.0

AEM Cloud Service prohibits unauthorized modifications of the following OOTB indexes:

* `nodetypeLucene`
* `slingResourceResolver`
* `socialLucene`
* `appsLibsLucene`
* `authorizables`
* `pathReference`

### Configuration of the tokenizers in analyzers should be created with the name `tokenizer`

* **Key**: AnalyzerTokenizerConfigCheck
* **Type**: Improvement
* **Severity**: Minor
* **Since**: Version 2024.6.0

AEM Cloud Service prohibits the creation of tokenizers with incorrect names in analyzers. Tokenizers should always be defined as `tokenizer`.

### Configuration of indexing definitions should not contain spaces

* **Key**: PathSpacesCheck
* **Type**: Improvement
* **Severity**: Minor
* **Since**: Version 2024.7.0

AEM Cloud Service prohibits the creation of indexing definitions that contain properties with spaces.

### Configuration of indexing definitions should not contain haystack0 property

* **Key**: HayStackPropertyCheck
* **Type**: Improvement
* **Severity**: Minor
* **Since**: Version 2024.12.0

AEM Cloud Service prohibits the creation of indexing definitions that contain haystack properties.

### Configuration of indexing definitions should not contain the property: async-previous

* **Key**: IndexUnsupportedAsyncPropertiesCheck
* **Type**: Improvement
* **Severity**: Minor
* **Since**: Version 2025.3.0

AEM Cloud Service prohibits the creation of indexing definitions with unsupported async properties.

### Configuration of indexing definitions should not have the same tag in multiple indexes

* **Key**: SameTagInMultipleIndexes
* **Type**: Improvement
* **Severity**: Minor
* **Since**: Version 2025.3.0

AEM Cloud Service prohibits the creation of indexing definitions that contain the same tag in multiple indexes.

### Configuration of indexing definitions should not contain mode replacement for forbidden paths

* **Key**: FilterXmlModeAnalysis
* **Type**: Improvement
* **Severity**: Major
* **Since**: Version 2025.4.0

The use of the "replacement" mode in file vault is not allowed for paths below `/content`; it should not be used for paths below `/etc` and `/var`. The "replace" mode overwrites existing repository content with content that comes from the package.

---

## 🔧 Dispatcher Optimization Tool Rules

The following section lists the Dispatcher Optimization Tool (DOT) checks executed by Cloud Manager:

### Dispatcher Configuration Rules

* **Dispatcher configuration unexpected tokens**
* **Dispatcher configuration unmatched quote**
* **Dispatcher configuration missing brace**
* **Dispatcher configuration extra brace**
* **Dispatcher configuration missing mandatory Property**
* **Dispatcher configuration deprecated property**
* **Dispatcher configuration not found**
* **Httpd configuration includes file not found**
* **Dispatcher configuration general**

### Dispatcher Farm Configuration Rules

* **The Dispatcher publish farm cache should have `serveStaleOnError` enabled**
* **The Dispatcher publish farm filters should contain the default deny rules from the 6.x.x version of the AEM archetype**
* **The Dispatcher publish farm cache `statfileslevel` property should be >= 2**
* **The Dispatcher publish farm `gracePeriod` property should be >= 2**
* **Each Dispatcher farm should have a unique name**
* **The Dispatcher publish farm cache should have its `ignoreUrlParams` rules configured in an allowlist manner**
* **The Dispatcher publish farm filters should specify the allowed Sling selectors in an allowlist manner**
* **The Dispatcher publish farm filters should specify the allowed Sling suffix patterns in an allowlist manner**
* **Do not use the 'Require all granted' directive in a VirtualHost Directory section with a root directory-path**

---

## 🔄 Important Updates

### SonarQube 9.9 Migration (February 13, 2025)

Starting Thursday, February 13, 2025 (Cloud Manager 2025.2.0), Cloud Manager Code Quality is using an updated SonarQube 9.9 version and an updated list of rules.

**Key Changes:**
- Updated SonarQube version to 9.9
- New rule set available for download
- Potential rule key migrations from `squid:*` to `java:*` format

---

## 📚 References

### Official Documentation
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Code Quality Testing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/code-quality-testing)
- [SonarQube Concepts](https://docs.sonarsource.com/sonarqube-server/latest/)
- [Content Search and Indexing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing)
- [Using Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)

### Tools and Resources
- [Dispatcher Optimization Tool (DOT)](https://github.com/adobe/aem-dispatcher-optimizer-tool/blob/main/docs/Rules.md)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)
- [AEM Core Components](https://experienceleague.adobe.com/en/docs/experience-manager-core-components/using/introduction)
- [OakPAL Framework](https://github.com/adamcin/oakpal)

### Downloads
- [Latest Rules (2024.12.0)](https://experienceleague.adobe.com/docs/experience-manager-cloud-manager/assets/CodeQuality-rules-latest-AMS-2024-12-0.xlsx)
- [Previous Rules](https://experienceleague.adobe.com/docs/experience-manager-cloud-manager/assets/CodeQuality-rules-latest-AMS.xlsx)

---

*Generated via MCP AEM Documentation + CSV analysis*
*Last updated: December 14, 2025*
*Total documented rules: 50+*
## 🔄 Additional Rules from CSV Analysis

The following rules were identified in the CSV files but not detailed in the official documentation:

### Security Vulnerabilities (Additional)

#### HttpServletRequest.getRequestedSessionId() should not be used

* **Key**: java:S2254
* **Type**: Vulnerability
* **Severity**: Critical
* **Tags**: cwe, owasp-a2, sans-top25-porous
* **Old Key**: squid:S2254

**Description**: Using `HttpServletRequest.getRequestedSessionId()` can expose applications to session fixation attacks.

#### Classes should not be loaded dynamically

* **Key**: java:S2658
* **Type**: Vulnerability
* **Severity**: Critical
* **Tags**: cwe, owasp-a1
* **Old Key**: squid:S2658

**Description**: Dynamic class loading can introduce security vulnerabilities and should be avoided.

#### Insecure temporary file creation methods should not be used

* **Key**: java:S5445
* **Type**: Vulnerability
* **Severity**: Critical
* **Tags**: cwe, owasp-a9
* **Old Key**: squid:S2976

**Description**: Using insecure methods to create temporary files can lead to security vulnerabilities.

#### Encryption algorithms should be used with secure mode and padding scheme

* **Key**: java:S5542
* **Type**: Vulnerability
* **Severity**: Critical
* **Tags**: cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous
* **Old Key**: squid:S2277

**Description**: Encryption algorithms must use secure modes and padding schemes to prevent attacks.

#### Cipher algorithms should be robust

* **Key**: java:S5547
* **Type**: Vulnerability
* **Severity**: Critical
* **Tags**: cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous
* **Old Key**: squid:S2258

**Description**: Cipher algorithms must be robust against known cryptographic attacks.

### Security Hotspots (Additional)

#### Hard-coded passwords are security-sensitive

* **Key**: java:S2068
* **Type**: Security Hotspot
* **Severity**: Blocker
* **Tags**: cert, cwe, owasp-a2, sans-top25-porous
* **Old Key**: squid:S2068

**Description**: Hard-coded passwords in source code pose significant security risks.

#### Using pseudorandom number generators (PRNGs) is security-sensitive

* **Key**: java:S2245
* **Type**: Security Hotspot
* **Severity**: Critical
* **Tags**: cert, cwe, owasp-a3
* **Old Key**: squid:S2245

**Description**: PRNGs should not be used in security-sensitive contexts without proper seeding.

#### Using non-standard cryptographic algorithms is security-sensitive

* **Key**: java:S2257
* **Type**: Security Hotspot
* **Severity**: Critical
* **Tags**: cwe, owasp-a3, sans-top25-porous
* **Old Key**: squid:S2257

**Description**: Only standard, well-tested cryptographic algorithms should be used.

#### Using weak hashing algorithms is security-sensitive

* **Key**: java:S4790
* **Type**: Security Hotspot
* **Severity**: Critical
* **Tags**: cwe, owasp-a3, owasp-a6, owasp-m5, sans-top25-porous, spring
* **Old Key**: squid:S2070

**Description**: Weak hashing algorithms like MD5 and SHA-1 should not be used for security purposes.

### Bug Rules (Additional)

#### Resources should be closed

* **Key**: java:S2095
* **Type**: Bug
* **Severity**: Blocker
* **Tags**: cert, cwe, denial-of-service, leak
* **Old Key**: squid:S2095

**Description**: Resources like streams, connections, and files must be properly closed to prevent resource leaks.

#### Double-checked locking should not be used

* **Key**: java:S2168
* **Type**: Bug
* **Severity**: Blocker
* **Tags**: cert, cwe, multi-threading
* **Old Key**: squid:S2168

**Description**: Double-checked locking pattern is broken in Java and should not be used.

#### "wait(...)" should be used instead of "Thread.sleep(...)" when a lock is held

* **Key**: java:S2276
* **Type**: Bug
* **Severity**: Blocker
* **Tags**: cert, multi-threading, performance
* **Old Key**: squid:S2276

**Description**: When holding a lock, use `wait()` instead of `Thread.sleep()` to avoid blocking other threads unnecessarily.

### AEM-Specific Rules (Additional)

#### Non-thread safe object used as a field of Servlet/Filter

* **Key**: AEM Rules:AEM-3
* **Type**: Bug
* **Severity**: Critical
* **Tags**: aem

**Description**: Servlets and filters should not have non-thread-safe objects as instance fields.

#### ResourceResolver should be closed in finally block

* **Key**: AEM Rules:AEM-6
* **Type**: Code Smell
* **Severity**: Critical
* **Tags**: aem

**Description**: ResourceResolver objects must be closed in finally blocks to prevent resource leaks.

#### Session should be logged out in finally block

* **Key**: AEM Rules:AEM-7
* **Type**: Code Smell
* **Severity**: Critical
* **Tags**: aem

**Description**: JCR Session objects must be logged out in finally blocks to prevent resource leaks.

#### Do not use deprecated administrative access methods

* **Key**: AEM Rules:AEM-11
* **Type**: Code Smell
* **Severity**: Major
* **Tags**: aem

**Description**: Deprecated administrative access methods should not be used in AEM applications.

#### Use predefined constant in annotation instead of hardcoded value

* **Key**: AEM Rules:AEM-1
* **Type**: Code Smell
* **Severity**: Minor
* **Tags**: aem

**Description**: Use predefined constants in annotations instead of hardcoded values for better maintainability.

#### Optional is defined as DefaultInjectionStrategy

* **Key**: AEM Rules:AEM-16
* **Type**: Code Smell
* **Severity**: Minor
* **Tags**: aem, sling-models

**Description**: In Sling Models, use DefaultInjectionStrategy.OPTIONAL instead of individual @Optional annotations.

#### Use predefined constant instead of hardcoded value

* **Key**: AEM Rules:AEM-2
* **Type**: Code Smell
* **Severity**: Minor
* **Tags**: aem

**Description**: Use predefined constants instead of hardcoded values for better maintainability.

#### Usage of 'synchronized' keyword should be avoided if possible

* **Key**: AEM Rules:AEM-15
* **Type**: Code Smell
* **Severity**: Info
* **Tags**: multi-threading, performance

**Description**: The synchronized keyword should be avoided when possible for better performance.

### Additional Code Quality Rules

#### Generic exceptions should never be thrown

* **Key**: java:S112
* **Type**: Code Smell
* **Severity**: Major
* **Tags**: cert, cwe, error-handling
* **Old Key**: squid:S00112

**Description**: Generic exceptions like Exception, RuntimeException, or Throwable should not be thrown directly.

#### Throwable and Error should not be caught

* **Key**: java:S1181
* **Type**: Code Smell
* **Severity**: Major
* **Tags**: bad-practice, cert, cwe, error-handling
* **Old Key**: squid:S1181

**Description**: Catching Throwable or Error can mask serious system problems and should be avoided.

#### Unused assignments should be removed

* **Key**: java:S1854
* **Type**: Code Smell
* **Severity**: Major
* **Tags**: cert, cwe, unused
* **Old Key**: squid:S1854

**Description**: Assignments to variables that are never read should be removed as dead code.

#### Boolean expressions should not be gratuitous

* **Key**: java:S2589
* **Type**: Code Smell
* **Severity**: Major
* **Tags**: cert, cwe, redundant, suspicious
* **Old Key**: squid:S1850

**Description**: Boolean expressions that are always true or false should be simplified or removed.

### Cloud Service Compatibility Rules (Additional)

#### Packages Should Not Mix Mutable and Immutable Content

* **Key**: ImmutableMutableMixedPackage
* **Type**: Code Smell
* **Severity**: Minor
* **Tags**: aem, cloud-service-compatibility

**Description**: Content packages should not mix mutable and immutable content for Cloud Service compatibility.

#### Only Supported Runmode Names and Ordering Should Be Used

* **Key**: SupportedRunmode
* **Type**: Code Smell
* **Severity**: Minor
* **Tags**: aem, cloud-service-compatibility

**Description**: Only supported runmode names and ordering should be used in AEM Cloud Service.

---

## 🔄 Key Migrations (SonarQube 9.9)

### Important Rule Key Changes (February 13, 2025)

Starting with Cloud Manager 2025.2.0, several rule keys have been migrated from the `squid:*` format to the `java:*` format:

| Old Key (pre 2024.12.0) | New Key | Description | Impact |
|-------------------------|---------|-------------|---------|
| squid:S2068 | java:S2068 | Hard-coded passwords | Security Hotspot |
| squid:S2095 | java:S2095 | Resources should be closed | Resource leaks |
| squid:S2168 | java:S2168 | Double-checked locking | Threading issues |
| squid:S2254 | java:S2254 | HttpServletRequest.getRequestedSessionId() | Security vulnerability |
| squid:S2658 | java:S2658 | Classes should not be loaded dynamically | Security vulnerability |
| squid:S2276 | java:S2276 | wait() vs Thread.sleep() with locks | Threading bug |
| squid:S2245 | java:S2245 | Pseudorandom number generators | Security hotspot |
| squid:S2257 | java:S2257 | Non-standard cryptographic algorithms | Security hotspot |
| squid:S2277 | java:S5542 | Encryption algorithms secure mode | Security vulnerability |
| squid:S2258 | java:S5547 | Cipher algorithms should be robust | Security vulnerability |
| squid:S2070 | java:S4790 | Weak hashing algorithms | Security hotspot |
| squid:S2077 | java:S2077 | SQL binding mechanisms | Security hotspot |
| squid:S2092 | java:S2092 | Cookies without secure flag | Security hotspot |
| squid:S2976 | java:S5445 | Insecure temporary file creation | Security vulnerability |
| squid:S1989 | java:S1989 | Exceptions from servlet methods | Vulnerability |
| squid:S2441 | java:S2441 | Non-serializable objects in HttpSession | Bug |
| squid:S2222 | java:S2222 | Locks should be released | Bug |
| squid:S2273 | java:S2273 | wait/notify with obvious lock | Bug |
| squid:S2445 | java:S2445 | Synchronized on private final fields | Bug |
| squid:S2583 | java:S2583 | Conditionally executed code | Bug |
| squid:S2885 | java:S2885 | Non-thread-safe static fields | Bug |

### Actions Required for Migration

1. **Update SonarQube configurations** to use new rule keys
2. **Review quality profiles** and update custom rules
3. **Update CI/CD pipelines** that reference old rule keys
4. **Update documentation** and team guidelines
5. **Verify exclusions** are updated to use new keys

---

## 📊 Updated Statistics

| Type | Count |
|------|-------|
| **Total Rules** | 140+ |
| **Vulnerabilities** | 13 |
| **Security Hotspots** | 5 |
| **Bugs** | 25 |
| **Code Smells** | 97+ |
| **Dispatcher Rules** | 16 |
| **AEM-Specific Rules** | 20+ |
| **Cloud Service Compatibility** | 15+ |

### By Severity

| Severity | Count |
|----------|-------|
| **Blocker** | 8 |
| **Critical** | 15 |
| **Major** | 45+ |
| **Minor** | 70+ |
| **Info** | 2 |

---

## 🛠️ Implementation Guidelines

### Priority Implementation Order

1. **Blocker and Critical Issues** - Address immediately
2. **Security Vulnerabilities and Hotspots** - High priority
3. **Cloud Service Compatibility Issues** - Required for AEM CS migration
4. **Resource Management Issues** - Prevent memory leaks
5. **Threading Issues** - Ensure application stability
6. **Code Quality Issues** - Improve maintainability

### Quality Gates Recommendations

```yaml
# Recommended Quality Gate for AEM Projects
conditions:
  - metric: bugs
    operator: GT
    threshold: 0
    
  - metric: vulnerabilities  
    operator: GT
    threshold: 0
    
  - metric: security_hotspots_reviewed
    operator: LT
    threshold: 100
    
  - metric: code_smells
    operator: GT
    threshold: 50
    
  - metric: coverage
    operator: LT
    threshold: 80
    
  - metric: duplicated_lines_density
    operator: GT
    threshold: 3
```

---

*Complete documentation generated via MCP AEM Documentation + comprehensive CSV analysis*
*Sources: Official Adobe Experience League documentation + CodeQuality-rules-latest-AMS-2024-12-0.csv + CodeQuality-rules-latest-AMS.csv*
*Total rules documented: 140+*
*Last updated: December 14, 2025*