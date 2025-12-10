# Custom Code Quality Rules - AEM Cloud Service

**Last updated:** December 10, 2025  
**Sources:**
- Adobe Experience League (official documentation via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## ⚠️ SonarQube 9.9 Update (February 2025)

Starting **February 13, 2025** (Cloud Manager 2025.2.0), Cloud Manager Code Quality uses SonarQube 9.9 with updated rule list and migration from `squid:*` to `java:*` keys.

---

## 📊 Statistics

| Type | Count |
|------|-------|
| **Total Rules** | 127 |
| **Vulnerabilities** | 14 |
| **Security Hotspots** | 6 |
| **Bugs** | 32 |
| **Code Smells** | 75 |

### By Severity

| Severity | Count |
|----------|-------|
| **Blocker** | 8 |
| **Critical** | 22 |
| **Major** | 56 |
| **Minor** | 38 |
| **Info** | 3 |

---

## 🔴 Vulnerability Rules

### java:S2254 - HttpServletRequest.getRequestedSessionId() should not be used

| Attribute | Value |
|-----------|-------|
| **Key** | java:S2254 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a2, sans-top25-porous |
| **Old Key** | squid:S2254 |

**Description**: The `getRequestedSessionId()` method should not be used as it may expose sensitive session information.

---

### java:S2658 - Classes should not be loaded dynamically

| Attribute | Value |
|-----------|-------|
| **Key** | java:S2658 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a1 |
| **Old Key** | squid:S2658 |

**Description**: Loading classes dynamically can allow execution of malicious code.

---

### java:S5445 - Insecure temporary file creation methods should not be used

| Attribute | Value |
|-----------|-------|
| **Key** | java:S5445 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a9 |
| **Old Key** | squid:S2976 |

**Description**: Insecure temporary file creation methods should not be used.

---

### java:S5542 - Encryption algorithms should be used with secure mode and padding

| Attribute | Value |
|-----------|-------|
| **Key** | java:S5542 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **Old Key** | squid:S2277 |

**Description**: Encryption algorithms should use secure mode and proper padding.

---

### java:S5547 - Cipher algorithms should be robust

| Attribute | Value |
|-----------|-------|
| **Key** | java:S5547 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **Old Key** | squid:S2258 |

**Description**: Cipher algorithms should be robust (do not use DES, 3DES, etc.).

---

### CQRules:CWE-134 - Don't use format strings that may be externally-controlled

| Attribute | Value |
|-----------|-------|
| **Key** | CQRules:CWE-134 |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cqsecurity |
| **Since** | Version 2018.4.0 |

**Description**: Using a format string from an external source (such as a request parameter or user-generated content) can expose the application to denial of service attacks.

#### Non-compliant code

```java
protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
  String messageFormat = request.getParameter("messageFormat");
  request.getResource().getValueMap().put("some property", String.format(messageFormat, "some text"));
  response.sendStatus(HttpServletResponse.SC_OK);
}
```

---

### CQRules:CWE-676 - Use of Potentially Dangerous Function

| Attribute | Value |
|-----------|-------|
| **Key** | CQRules:CWE-676 |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cqsecurity |
| **Since** | Version 2018.4.0 |

**Description**: The `Thread.stop()` and `Thread.interrupt()` methods can produce hard-to-reproduce problems and sometimes security vulnerabilities.

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

---

## 🟠 Security Hotspot Rules

### java:S2068 - Hard-coded passwords are security-sensitive

| Attribute | Value |
|-----------|-------|
| **Key** | java:S2068 |
| **Type** | Security Hotspot |
| **Severity** | Blocker |
| **Tags** | cert, cwe, owasp-a2, sans-top25-porous |
| **Old Key** | squid:S2068 |

**Description**: Hard-coded passwords are security-sensitive and should be avoided.

---

### java:S2245 - Using pseudorandom number generators (PRNGs) is security-sensitive

| Attribute | Value |
|-----------|-------|
| **Key** | java:S2245 |
| **Type** | Security Hotspot |
| **Severity** | Critical |
| **Tags** | cert, cwe, owasp-a3 |
| **Old Key** | squid:S2245 |

**Description**: Using pseudorandom number generators (PRNGs) is security-sensitive in cryptographic contexts.

---

## 🔵 Bug Rules

### BannedPath - Customer packages should not install content under /libs

| Attribute | Value |
|-----------|-------|
| **Key** | BannedPath |
| **Type** | Bug |
| **Severity** | Blocker |
| **Since** | Version 2019.6.0 |

**Description**: The `/libs` content tree in the AEM repository should be considered read-only by customers. Modifying nodes and properties under `/libs` creates significant risk for major and minor upgrades.

---

### java:S2095 - Resources should be closed

| Attribute | Value |
|-----------|-------|
| **Key** | java:S2095 |
| **Type** | Bug |
| **Severity** | Blocker |
| **Tags** | cert, cwe, denial-of-service, leak |
| **Old Key** | squid:S2095 |

**Description**: Resources should be closed to avoid memory leaks.

---

### AEM Rules:AEM-6 - ResourceResolver should be closed in finally block

| Attribute | Value |
|-----------|-------|
| **Key** | AEM Rules:AEM-6 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | aem |

**Description**: ResourceResolver should be closed in finally block.

---

### AEM Rules:AEM-7 - Session should be logged out in finally block

| Attribute | Value |
|-----------|-------|
| **Key** | AEM Rules:AEM-7 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | aem |

**Description**: Session should be logged out in finally block.

---

### CQRules:ConnectionTimeoutMechanism - HTTP requests should always have socket and connect timeouts

| Attribute | Value |
|-----------|-------|
| **Key** | CQRules:ConnectionTimeoutMechanism |
| **Type** | Bug |
| **Severity** | Critical |
| **Since** | Version 2018.6.0 |

**Description**: When executing HTTP requests from within an AEM application, it is critical that proper timeouts are configured to avoid unnecessary thread consumption. As a best practice, these timeouts should not exceed 60 seconds.

#### Non-compliant code

```java
@Reference
private HttpClientBuilderFactory httpClientBuilderFactory;

public void dontDoThis() {
  HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
  HttpClient httpClient = builder.build();
  // do something with the client
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
```

---

## 🟡 Code Smell Rules

### java:S1147 - Exit methods should not be called

| Attribute | Value |
|-----------|-------|
| **Key** | java:S1147 |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | cert, cwe, suspicious |
| **Old Key** | squid:S1147 |

**Description**: Exit methods like `System.exit()` should not be called.

---

### CQRules:CQBP-72 - close() method is not called on ResourceResolver object

| Attribute | Value |
|-----------|-------|
| **Key** | CQRules:CQBP-72 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Description**: `ResourceResolver` objects obtained from the `ResourceResolverFactory` consume system resources. It is more efficient to explicitly close any open `ResourceResolver` object by calling the `close()` method.

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

---

## ☁️ Cloud Service Compatibility Rules

### CloudServiceIncompatibleWorkflowProcess - Usage of Cloud Service Incompatible Workflow Processes

| Attribute | Value |
|-----------|-------|
| **Key** | CloudServiceIncompatibleWorkflowProcess |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Description**: With the move to Asset micro-services for asset processing in AEM Cloud Service, several workflow processes that were used in on-premise and AMS versions of AEM have become unsupported or unnecessary.

---

### CQRules:AMSCORE-553 - AEM Deprecated APIs Should Not Be Used

| Attribute | Value |
|-----------|-------|
| **Key** | CQRules:AMSCORE-553 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqcompatibility |
| **Since** | Version 2020.5.0 |

**Description**: AEM's API surface is under constant review to identify APIs whose usage is discouraged and thus considered deprecated.

---

## 🔧 Dispatcher Rules (DOT - Dispatcher Optimization Tool)

### DOTRules:Disp-2---statfileslevel

| Attribute | Value |
|-----------|-------|
| **Key** | DOTRules:Disp-2---statfileslevel |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Description**: The statfileslevel property of the Dispatcher publish farm cache should be >= 2.

---

### DOTRules:Disp-4---default-filter-deny-rules

| Attribute | Value |
|-----------|-------|
| **Key** | DOTRules:Disp-4---default-filter-deny-rules |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Description**: The Dispatcher publish farm filters should contain the default `deny` rules from the 6.x.x version of the AEM archetype.

---

### DOTRules:Disp-5---serveStaleOnError

| Attribute | Value |
|-----------|-------|
| **Key** | DOTRules:Disp-5---serveStaleOnError |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Description**: The Dispatcher publish farm cache should have serveStaleOnError enabled.

---

## 🔄 Key Migrations (SonarQube 9.9)

Starting **February 13, 2025** (Cloud Manager 2025.2.0), the following keys were migrated from `squid:*` to `java:*`:

| Old Key (pre 2024.12.0) | New Key |
|-------------------------|---------|
| squid:S2068 | java:S2068 |
| squid:S2095 | java:S2095 |
| squid:S2168 | java:S2168 |
| squid:S2276 | java:S2276 |
| squid:S1147 | java:S1147 |
| squid:S128 | java:S128 |
| squid:S2178 | java:S2178 |
| squid:S2254 | java:S2254 |
| squid:S2658 | java:S2658 |
| squid:S2976 | java:S5445 |
| squid:S2277 | java:S5542 |
| squid:S2258 | java:S5547 |

---

## 📚 References

### Official Documentation

- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Code Quality Testing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/code-quality-testing)
- [Content Search and Indexing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing)

### Tools

- [SonarQube Documentation](https://docs.sonarsource.com/sonarqube-server/latest/)
- [Dispatcher Optimization Tool (DOT)](https://github.com/adobe/aem-dispatcher-optimizer-tool/blob/main/docs/Rules.md)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)

### CSV Reference Files

- `CodeQuality-rules-latest-AMS-2024-12-0.csv` - Latest version (SonarQube 9.9)
- `CodeQuality-rules-latest-AMS.csv` - Previous version

---

*Last updated: December 10, 2025*
*Generated via MCP AEM Documentation + CSV analysis*