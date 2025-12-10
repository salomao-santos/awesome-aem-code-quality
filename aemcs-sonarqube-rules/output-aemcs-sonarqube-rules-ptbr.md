# Custom Code Quality Rules - AEM Cloud Service

**Última atualização:** 10 de Dezembro de 2025  
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## ⚠️ Atualização SonarQube 9.9 (Fevereiro 2025)

A partir de **13 de Fevereiro de 2025** (Cloud Manager 2025.2.0), o Cloud Manager Code Quality utiliza SonarQube 9.9 com lista atualizada de regras e migração de chaves `squid:*` para `java:*`.

---

## 📊 Estatísticas

| Tipo | Quantidade |
|------|------------|
| **Total de Regras** | 127 |
| **Vulnerabilities** | 14 |
| **Security Hotspots** | 6 |
| **Bugs** | 32 |
| **Code Smells** | 75 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | 8 |
| **Critical** | 22 |
| **Major** | 56 |
| **Minor** | 38 |
| **Info** | 3 |

---

## 🔴 Regras de Vulnerabilidade (Vulnerability)

### java:S2254 - HttpServletRequest.getRequestedSessionId() should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2254 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a2, sans-top25-porous |
| **Old Key** | squid:S2254 |

**Descrição**: O método `getRequestedSessionId()` não deve ser usado pois pode expor informações sensíveis de sessão.

---

### java:S2658 - Classes should not be loaded dynamically

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2658 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a1 |
| **Old Key** | squid:S2658 |

**Descrição**: Carregar classes dinamicamente pode permitir execução de código malicioso.

---

### java:S5445 - Insecure temporary file creation methods should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5445 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a9 |
| **Old Key** | squid:S2976 |

**Descrição**: Métodos inseguros de criação de arquivos temporários não devem ser usados.

---

### java:S5542 - Encryption algorithms should be used with secure mode and padding

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5542 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **Old Key** | squid:S2277 |

**Descrição**: Algoritmos de criptografia devem usar modo seguro e padding adequado.

---

### java:S5547 - Cipher algorithms should be robust

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5547 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **Old Key** | squid:S2258 |

**Descrição**: Algoritmos de cifra devem ser robustos (não usar DES, 3DES, etc.).

---

### CQRules:CWE-134 - Don't use format strings that may be externally-controlled

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CWE-134 |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cqsecurity |
| **Since** | Version 2018.4.0 |

**Descrição**: Usar uma string de formato de uma fonte externa (como um parâmetro de requisição ou conteúdo gerado pelo usuário) pode expor a aplicação a ataques de negação de serviço.

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

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CWE-676 |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cqsecurity |
| **Since** | Version 2018.4.0 |

**Descrição**: Os métodos `Thread.stop()` e `Thread.interrupt()` podem produzir problemas difíceis de reproduzir e, às vezes, vulnerabilidades de segurança.

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

### findbugs:PT_ABSOLUTE_PATH_TRAVERSAL - Absolute path traversal in servlet

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:PT_ABSOLUTE_PATH_TRAVERSAL |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cwe |

---

### findbugs:PT_RELATIVE_PATH_TRAVERSAL - Relative path traversal in servlet

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:PT_RELATIVE_PATH_TRAVERSAL |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cwe |

---

### findsecbugs:PATH_TRAVERSAL_IN - Potential Path Traversal (file read)

| Atributo | Valor |
|----------|-------|
| **Key** | findsecbugs:PATH_TRAVERSAL_IN |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cwe, owasp-a4, wasc |

---

### findsecbugs:PATH_TRAVERSAL_OUT - Potential Path Traversal (file write)

| Atributo | Valor |
|----------|-------|
| **Key** | findsecbugs:PATH_TRAVERSAL_OUT |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cwe, owasp-a4, wasc |

---

### java:S1989 - Exceptions should not be thrown from servlet methods

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1989 |
| **Type** | Vulnerability |
| **Severity** | Minor |
| **Tags** | cert, cwe, error-handling, owasp-a3 |
| **Old Key** | squid:S1989 |

---

### findsecbugs:FILE_UPLOAD_FILENAME - Tainted filename read

| Atributo | Valor |
|----------|-------|
| **Key** | findsecbugs:FILE_UPLOAD_FILENAME |
| **Type** | Vulnerability |
| **Severity** | Info |
| **Tags** | cwe, owasp-a4, wasc |

---

## 🟠 Regras de Security Hotspot

### java:S2068 - Hard-coded passwords are security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2068 |
| **Type** | Security Hotspot |
| **Severity** | Blocker |
| **Tags** | cert, cwe, owasp-a2, sans-top25-porous |
| **Old Key** | squid:S2068 |

**Descrição**: Senhas hard-coded são sensíveis à segurança e devem ser evitadas.

---

### java:S2245 - Using pseudorandom number generators (PRNGs) is security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2245 |
| **Type** | Security Hotspot |
| **Severity** | Critical |
| **Tags** | cert, cwe, owasp-a3 |
| **Old Key** | squid:S2245 |

**Descrição**: Usar geradores de números pseudoaleatórios (PRNGs) é sensível à segurança em contextos criptográficos.

---

### java:S2257 - Using non-standard cryptographic algorithms is security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2257 |
| **Type** | Security Hotspot |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, sans-top25-porous |
| **Old Key** | squid:S2257 |

**Descrição**: Usar algoritmos criptográficos não-padrão é sensível à segurança.

---

### java:S4790 - Using weak hashing algorithms is security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S4790 |
| **Type** | Security Hotspot |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, sans-top25-porous, spring |
| **Old Key** | squid:S2070 |

**Descrição**: Usar algoritmos de hash fracos (SHA-1, MD5) é sensível à segurança.

---

### java:S2077 - SQL binding mechanisms should be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2077 |
| **Type** | Security Hotspot |
| **Severity** | Major |
| **Tags** | cert, cwe, hibernate, owasp-a1, sans-top25-insecure, sql |
| **Old Key** | squid:S2077 |

**Descrição**: Mecanismos de binding SQL devem ser usados para prevenir SQL injection.

---

### java:S2092 - Creating cookies without the "secure" flag is security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2092 |
| **Type** | Security Hotspot |
| **Severity** | Minor |
| **Tags** | cwe, owasp-a3, privacy, sans-top25-porous, spring |
| **Old Key** | squid:S2092 |

**Descrição**: Criar cookies sem a flag "secure" é sensível à segurança.

---

## 🔵 Regras de Bug

### BannedPath - Customer packages should not install content under /libs

| Atributo | Valor |
|----------|-------|
| **Key** | BannedPath |
| **Type** | Bug |
| **Severity** | Blocker |
| **Since** | Version 2019.6.0 |

**Descrição**: A árvore de conteúdo `/libs` no repositório AEM deve ser considerada somente leitura pelos clientes. Modificar nós e propriedades sob `/libs` cria risco significativo para atualizações maiores e menores.

---

### java:S2095 - Resources should be closed

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2095 |
| **Type** | Bug |
| **Severity** | Blocker |
| **Tags** | cert, cwe, denial-of-service, leak |
| **Old Key** | squid:S2095 |

**Descrição**: Recursos devem ser fechados para evitar vazamentos de memória.

---

### java:S2168 - Double-checked locking should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2168 |
| **Type** | Bug |
| **Severity** | Blocker |
| **Tags** | cert, cwe, multi-threading |
| **Old Key** | squid:S2168 |

**Descrição**: Double-checked locking não deve ser usado pois pode causar problemas de concorrência.

---

### java:S2276 - wait() should be used instead of Thread.sleep() when lock is held

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2276 |
| **Type** | Bug |
| **Severity** | Blocker |
| **Tags** | cert, multi-threading, performance |
| **Old Key** | squid:S2276 |

**Descrição**: `wait()` deve ser usado em vez de `Thread.sleep()` quando um lock é mantido.

---

### AEM Rules:AEM-3 - Non-thread safe object used as a field of Servlet/Filter

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-3 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | aem |

**Descrição**: Objetos não thread-safe não devem ser usados como campos de Servlet/Filter.

---

### CQRules:ConnectionTimeoutMechanism - HTTP requests should always have socket and connect timeouts

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:ConnectionTimeoutMechanism |
| **Type** | Bug |
| **Severity** | Critical |
| **Since** | Version 2018.6.0 |

**Descrição**: Ao executar requisições HTTP de dentro de uma aplicação AEM, é crítico que timeouts adequados sejam configurados para evitar consumo desnecessário de threads. Como melhor prática, esses timeouts não devem exceder 60 segundos.

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

---

### findbugs:BC_IMPOSSIBLE_CAST - Impossible cast

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:BC_IMPOSSIBLE_CAST |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | correctness |

---

### findbugs:BC_IMPOSSIBLE_DOWNCAST - Impossible downcast

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:BC_IMPOSSIBLE_DOWNCAST |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | correctness |

---

### java:S1114 - super.finalize() should be called at the end of Object.finalize()

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1114 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | cert, cwe |
| **Old Key** | squid:ObjectFinalizeOverridenCallsSuperFinalizeCheck |

---

### java:S1143 - Jump statements should not occur in finally blocks

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1143 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | cert, cwe, error-handling |
| **Old Key** | squid:S1143 |

---

### java:S2222 - Locks should be released on all paths

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2222 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | cwe, multi-threading |
| **Old Key** | squid:S2222 |

---

### java:S2441 - Non-serializable objects should not be stored in HttpSession

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2441 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | cert, cwe |
| **Old Key** | squid:S2441 |

---

### java:S3518 - Zero should not be a possible denominator

| Atributo | Valor |
|----------|-------|
| **Key** | java:S3518 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | cert, cwe, denial-of-service |
| **Old Key** | squid:S3518 |

---

### ConfigAndInstallShouldOnlyContainOsgiNodes - Paths with /config/ and /install/ should only be used for OSGi

| Atributo | Valor |
|----------|-------|
| **Key** | ConfigAndInstallShouldOnlyContainOsgiNodes |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Por razões de segurança, caminhos contendo `/config/` e `/install/` são legíveis apenas por usuários administrativos no AEM e devem ser usados apenas para configuração OSGi e bundles OSGi.

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

---

### DuplicateOsgiConfigurations - Customer packages should not contain overlapping OSGi configurations

| Atributo | Valor |
|----------|-------|
| **Key** | DuplicateOsgiConfigurations |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Um problema comum em projetos complexos é quando o mesmo componente OSGi é configurado múltiplas vezes. Esta regra é "runmode-aware" e identifica apenas problemas onde o mesmo componente é configurado múltiplas vezes no mesmo run mode.

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

---

### findbugs:EQ_ALWAYS_FALSE - equals method always returns false

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:EQ_ALWAYS_FALSE |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | correctness |

---

### findbugs:EQ_ALWAYS_TRUE - equals method always returns true

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:EQ_ALWAYS_TRUE |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | correctness |

---

### findbugs:IL_INFINITE_LOOP - An apparent infinite loop

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:IL_INFINITE_LOOP |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | correctness |

---

### findbugs:IS2_INCONSISTENT_SYNC - Inconsistent synchronization

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:IS2_INCONSISTENT_SYNC |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | multi-threading |

---

### findbugs:VO_VOLATILE_INCREMENT - An increment to a volatile field isn't atomic

| Atributo | Valor |
|----------|-------|
| **Key** | findbugs:VO_VOLATILE_INCREMENT |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | multi-threading |

---

### java:S1111 - The Object.finalize() method should not be called

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1111 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe |
| **Old Key** | squid:ObjectFinalizeCheck |

---

### java:S1217 - Thread.run() should not be called directly

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1217 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe, multi-threading |
| **Old Key** | squid:S1217 |

---

### java:S1872 - Classes should not be compared by name

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1872 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe |
| **Old Key** | squid:S1872 |

---

### java:S2159 - Silly equality checks should not be made

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2159 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, unused |
| **Old Key** | squid:S2159 |

---

### java:S2225 - toString() and clone() methods should not return null

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2225 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe |
| **Old Key** | squid:S2225 |

---

### java:S2226 - Servlets should not have mutable instance fields

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2226 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, multi-threading, struts |
| **Old Key** | squid:S2226 |

---

### java:S2259 - Null pointers should not be dereferenced

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2259 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe |
| **Old Key** | squid:S2259 |

---

### java:S2273 - wait, notify and notifyAll should only be called when a lock is held

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2273 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | multi-threading |
| **Old Key** | squid:S2273 |

---

### java:S2445 - Blocks should be synchronized on private final fields

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2445 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe, multi-threading |
| **Old Key** | squid:S2445 |

---

### java:S2583 - Conditionally executed code should be reachable

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2583 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cert, cwe, pitfall, suspicious, unused |
| **Old Key** | squid:S2583, squid:S1145 |

---

### java:S2885 - Non-thread-safe fields should not be static

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2885 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | multi-threading |
| **Old Key** | squid:S2885 |

---

### java:S3655 - Optional value should only be accessed after calling isPresent()

| Atributo | Valor |
|----------|-------|
| **Key** | java:S3655 |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | cwe |
| **Old Key** | squid:S3655 |

---

### PackageOverlaps - Customer packages should not overlap

| Atributo | Valor |
|----------|-------|
| **Key** | PackageOverlaps |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Similar à regra de configurações OSGi duplicadas, este é um problema comum em projetos complexos onde o mesmo caminho de nó é escrito por múltiplos pacotes de conteúdo separados.

---

### ClientlibProxyResource - Resources in Proxy-Enabled Client Libraries should be in resources folder

| Atributo | Valor |
|----------|-------|
| **Key** | ClientlibProxyResource |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | aem |
| **Since** | Version 2021.2.0 |

**Descrição**: Bibliotecas de cliente AEM podem conter recursos estáticos como imagens e fontes. Ao usar bibliotecas de cliente com proxy, esses recursos estáticos devem estar em uma pasta filha chamada `resources`.

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

---

### java:S1206 - equals(Object obj) and hashCode() should be overridden in pairs

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1206 |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | cert, cwe |
| **Old Key** | squid:S1206 |

---

### java:S2184 - Math operands should be cast before assignment

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2184 |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | aem |
| **Old Key** | squid:S2184 |

---

### java:S899 - Return values should not be ignored when they contain the operation status code

| Atributo | Valor |
|----------|-------|
| **Key** | java:S899 |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | cert, cwe, error-handling |
| **Old Key** | squid:S899 |

---

## 🟡 Regras de Code Smell

### java:S1147 - Exit methods should not be called

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1147 |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | cert, cwe, suspicious |
| **Old Key** | squid:S1147 |

**Descrição**: Métodos de saída como `System.exit()` não devem ser chamados.

---

### java:S128 - Switch cases should end with an unconditional break statement

| Atributo | Valor |
|----------|-------|
| **Key** | java:S128 |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | cert, cwe, suspicious |
| **Old Key** | squid:S128 |

**Descrição**: Casos de switch devem terminar com uma instrução break incondicional.

---

### java:S2178 - Short-circuit logic should be used in boolean contexts

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2178 |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | cert |
| **Old Key** | squid:S2178 |

**Descrição**: Lógica de curto-circuito deve ser usada em contextos booleanos.

---

### AEM Rules:AEM-6 - ResourceResolver should be closed in finally block

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-6 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | aem |

**Descrição**: ResourceResolver deve ser fechado em bloco finally.

---

### AEM Rules:AEM-7 - Session should be logged out in finally block

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-7 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | aem |

**Descrição**: Session deve fazer logout em bloco finally.

---

### java:S1174 - Object.finalize() should remain protected when overriding

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1174 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | cert, cwe |
| **Old Key** | squid:S1174 |

---

### java:S131 - switch statements should have default clauses

| Atributo | Valor |
|----------|-------|
| **Key** | java:S131 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | cert, cwe |
| **Old Key** | squid:SwitchLastCaseIsDefaultCheck |

---

### java:S1948 - Fields in a Serializable class should either be transient or serializable

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1948 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | cwe, serialization |
| **Old Key** | squid:S1948 |

---

### java:S888 - Equality operators should not be used in for loop termination conditions

| Atributo | Valor |
|----------|-------|
| **Key** | java:S888 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | cert, cwe, suspicious |
| **Old Key** | squid:S888 |

---

### AEM Rules:AEM-11 - Do not use deprecated administrative access methods

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-11 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | aem |

**Descrição**: Não use métodos de acesso administrativo deprecados.

---

### CQRules:AEMSRE-889 - HttpClient Proxy Configuration

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:AEMSRE-889 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cqsoftwarequality |

---

### CQRules:CQBP-72 - close() method is not called on ResourceResolver object

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-72 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Objetos `ResourceResolver` obtidos do `ResourceResolverFactory` consomem recursos do sistema. É mais eficiente fechar explicitamente qualquer objeto `ResourceResolver` aberto chamando o método `close()`.

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

### CQRules:CQBP-75 - Do not use Sling servlet paths to register servlet

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-75 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Vincular servlets por caminhos é desencorajado. Servlets vinculados por caminho não podem usar controles de acesso JCR padrão e, como resultado, requerem rigor de segurança adicional.

#### Non-compliant code

```java
@Component(property = {
  "sling.servlet.paths=/apps/myco/endpoint"
})
public class DontDoThis extends SlingAllMethodsServlet {
 // implementation
}
```

---

### java:S112 - Generic exceptions should never be thrown

| Atributo | Valor |
|----------|-------|
| **Key** | java:S112 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, error-handling |
| **Old Key** | squid:S00112 |

---

### java:S1121 - Assignments should not be made from within sub-expressions

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1121 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, suspicious |
| **Old Key** | squid:AssignmentInSubExpressionCheck |

---

### java:S1134 - Track uses of FIXME tags

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1134 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cwe |
| **Old Key** | squid:S1134 |

---

### java:S1181 - Throwable and Error should not be caught

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1181 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | bad-practice, cert, cwe, error-handling |
| **Old Key** | squid:S1181 |

---

### java:S1696 - NullPointerException should not be caught

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1696 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, error-handling |
| **Old Key** | squid:S1696 |

---

### java:S1854 - Unused assignments should be removed

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1854 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, unused |
| **Old Key** | squid:S1854 |

---

### java:S2112 - URL.hashCode and URL.equals should be avoided

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2112 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | performance |
| **Old Key** | squid:S2112 |

---

### java:S2442 - Lock objects should not be synchronized

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2442 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, clumsy, multi-threading |
| **Old Key** | squid:S2442 |

---

### java:S2589 - Boolean expressions should not be gratuitous

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2589 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, redundant, suspicious |
| **Old Key** | squid:S1850 |

---

### java:S2681 - Multiline blocks should be enclosed in curly braces

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2681 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe |
| **Old Key** | squid:S2681 |

---

### java:S864 - Limited dependence should be placed on operator precedence

| Atributo | Valor |
|----------|-------|
| **Key** | java:S864 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe |
| **Old Key** | squid:S864 |

---

### AEM Rules:AEM-1 - Use predefined constant in annotation instead of hardcoded value

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-1 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem |

---

### AEM Rules:AEM-2 - Use predefined constant instead of hardcoded value

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-2 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem |

---

### AEM Rules:AEM-16 - Optional is defined as DefaultInjectionStrategy

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-16 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, sling-models |

---

### CQRules:CQBP-44---CatchAndEitherLogOrThrow - Catch and either log or throw

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---CatchAndEitherLogOrThrow |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Em geral, uma exceção deve ser logada exatamente uma vez. Logar exceções múltiplas vezes causa confusão.

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

---

### CQRules:CQBP-44---ConsecutivelyLogAndThrow - Avoid log statement immediately followed by throw

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---ConsecutivelyLogAndThrow |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Outro padrão comum a evitar é logar uma mensagem e então imediatamente lançar uma exceção.

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

---

### CQRules:CQBP-44---ExceptionGetMessageIsFirstLogParam - Do not use Exception.getMessage() as first log param

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---ExceptionGetMessageIsFirstLogParam |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Como melhor prática, mensagens de log devem fornecer informações contextuais sobre onde na aplicação uma exceção ocorreu.

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

---

### CQRules:CQBP-44---ExceptionPrintStackTrace - Do not use Exception.printStackTrace()

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---ExceptionPrintStackTrace |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Contexto é crítico ao entender mensagens de log. Usar `Exception.printStackTrace()` faz com que apenas o stack trace seja enviado para o stream de erro padrão, perdendo todo o contexto.

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

---

### CQRules:CQBP-44---LogInfoInGetOrHeadRequests - Do not log with INFO level in GET or HEAD handlers

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---LogInfoInGetOrHeadRequests |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |

**Descrição**: Em geral, o nível de log INFO deve ser usado para demarcar ações importantes. Métodos GET e HEAD devem ser apenas operações de leitura e não constituem ações importantes.

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

---

### CQRules:CQBP-44---LowLevelConsolePrinters - Do not use low level console printers

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---LowLevelConsolePrinters |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Logging no AEM deve sempre ser feito através do framework de logging, SLF4J. Saída direta para standard output ou standard error perde informações estruturais e contextuais.

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

---

### CQRules:CQBP-44---WrongLogLevelInCatchBlock - In catch blocks, you should only log WARN or ERROR

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---WrongLogLevelInCatchBlock |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Quando uma exceção é capturada, é importante garantir que mensagens de log sejam logadas no nível apropriado: WARN ou ERROR.

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

---

### CQRules:CQBP-71 - Do not hardcode paths using String literals

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-71 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Caminhos começando com `/libs` e `/apps` geralmente não devem ser hardcoded. Esses caminhos são tipicamente armazenados relativos ao caminho de busca do Sling.

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

---

### CQRules:GRANITE-54181 - Thread interruption check

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:GRANITE-54181 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsecurity |

---

### java:S1104 - Class variable fields should not have public accessibility

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1104 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cwe |
| **Old Key** | squid:ClassVariableVisibilityCheck |

---

### java:S1182 - Classes that override clone should be Cloneable and call super.clone()

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1182 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cert, convention, cwe |
| **Old Key** | squid:S1182 |

---

### java:S1444 - public static fields should be constant

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1444 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cert, cwe |
| **Old Key** | squid:S1444 |

---

### java:S1698 - == and != should not be used when equals is overridden

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1698 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cert, cwe, suspicious |
| **Old Key** | squid:S1698 |

---

### java:S1874 - @Deprecated code should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1874 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cert, cwe, obsolete |
| **Old Key** | squid:CallToDeprecatedMethod |

---

### java:S2221 - Exception should not be caught when not required by called methods

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2221 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cwe, error-handling |
| **Old Key** | squid:S2221 |

---

### java:S2384 - Mutable members should not be stored or returned directly

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2384 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cert, cwe, unpredictable |
| **Old Key** | squid:S2384 |

---

### java:S2386 - Mutable fields should not be public static

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2386 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cert, cwe, unpredictable |
| **Old Key** | squid:S2386 |

---

### AEM Rules:AEM-15 - Usage of synchronized keyword should be avoided if possible

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-15 |
| **Type** | Code Smell |
| **Severity** | Info |
| **Tags** | multi-threading, performance |

---

### java:S1135 - Track uses of TODO tags

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1135 |
| **Type** | Code Smell |
| **Severity** | Info |
| **Tags** | cwe |
| **Old Key** | squid:S1135 |

---

## ☁️ Regras de Compatibilidade Cloud Service

### ClassicUIAuthoringMode - Default Authoring Mode Should Not Be Classic UI

| Atributo | Valor |
|----------|-------|
| **Key** | ClassicUIAuthoringMode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: A configuração OSGi `com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl` define o modo de autoria padrão no AEM. Como a Classic UI foi deprecada desde o AEM 6.4, um problema é levantado quando o modo de autoria padrão é configurado para Classic UI.

---

### ComponentWithOnlyClassicUIDialog - Components Should Have Touch UI Dialogs

| Atributo | Valor |
|----------|-------|
| **Key** | ComponentWithOnlyClassicUIDialog |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: Componentes AEM com um diálogo Classic UI também devem ter um diálogo Touch UI para autoria otimizada e compatibilidade com o modelo de deployment do Cloud Service, que não suporta Classic UI.

---

### CloudServiceIncompatibleWorkflowProcess - Usage of Cloud Service Incompatible Workflow Processes

| Atributo | Valor |
|----------|-------|
| **Key** | CloudServiceIncompatibleWorkflowProcess |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Com a mudança para Asset micro-services para processamento de assets no AEM Cloud Service, vários processos de workflow que eram usados em versões on-premise e AMS do AEM tornaram-se não suportados ou desnecessários.

---

### ImmutableMutableMixedPackage - Packages Should Not Mix Mutable and Immutable Content

| Atributo | Valor |
|----------|-------|
| **Key** | ImmutableMutableMixedPackage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Pacotes não devem misturar conteúdo mutável e imutável.

---

### ReverseReplication - Reverse Replication Agents Should Not Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | ReverseReplication |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: Suporte para replicação reversa não está disponível em deployments do Cloud Service.

---

### StaticTemplateUsage - Usage of Static Templates is Discouraged

| Atributo | Valor |
|----------|-------|
| **Key** | StaticTemplateUsage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Embora o uso de templates estáticos tenha sido historicamente comum em projetos AEM, templates editáveis são altamente recomendados pois fornecem mais flexibilidade e suportam recursos adicionais não presentes em templates estáticos.

---

### LegacyFoundationComponentUsage - Usage of Legacy Foundation Components is Discouraged

| Atributo | Valor |
|----------|-------|
| **Key** | LegacyFoundationComponentUsage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Os Foundation Components legados (componentes sob `/libs/foundation`) foram deprecados por várias releases do AEM em favor dos Core Components.

---

### SupportedRunmode - Only Supported Runmode Names Should Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | SupportedRunmode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Apenas nomes de runmode suportados devem ser usados.

---

### CQRules:AMSCORE-553 - AEM Deprecated APIs Should Not Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:AMSCORE-553 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqcompatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: A superfície da API do AEM está sob revisão constante para identificar APIs cujo uso é desencorajado e, portanto, considerado deprecado.

---

### CQRules:AMSCORE-554 - Sling Scheduler Should Not Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:AMSCORE-554 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqcompatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: Não use Sling Scheduler para tarefas que requerem execução garantida. Sling Scheduled Jobs garantem execução e são mais adequados para ambientes clusterizados e não-clusterizados.

---

## 📦 Regras OakPAL

### CQBP-84 - Product APIs annotated with @ProviderType should not be implemented by customers

| Atributo | Valor |
|----------|-------|
| **Key** | CQBP-84 |
| **Type** | Bug |
| **Severity** | Critical |
| **Since** | Version 2018.7.0 |

**Descrição**: A API do AEM contém interfaces e classes Java que são destinadas apenas a serem usadas, mas não implementadas, por código customizado. AEM anota interfaces e classes destinadas apenas para sua implementação com `org.osgi.annotation.versioning.ProviderType`.

#### Non-compliant Code

```java
import com.day.cq.wcm.api.Page;

public class DontDoThis implements Page {
// implementation here
}
```

---

## 🔍 Regras de Oak Index (Cloud Service)

### OakIndexLocation - Custom Search Index Definition Nodes Must Be Direct Children of /oak:index

| Atributo | Valor |
|----------|-------|
| **Key** | OakIndexLocation |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) sejam nós filhos diretos de `/oak:index`.

---

### IndexCompatVersion - Custom Search Index Definition Nodes Must Have a compatVersion of 2

| Atributo | Valor |
|----------|-------|
| **Key** | IndexCompatVersion |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas tenham a propriedade `compatVersion` definida como `2`.

---

### IndexDescendantNodeType - Descendent Nodes Must Be Of Type nt:unstructured

| Atributo | Valor |
|----------|-------|
| **Key** | IndexDescendantNodeType |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Problemas difíceis de solucionar podem ocorrer quando um nó de definição de índice de busca customizado tem nós filhos não ordenados. Adobe recomenda que todos os nós descendentes de um nó `oak:QueryIndexDefinition` sejam do tipo `nt:unstructured`.

---

### IndexRulesNode - Custom Search Index Definition Nodes Must Contain indexRules Child Node

| Atributo | Valor |
|----------|-------|
| **Key** | IndexRulesNode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Um nó de definição de índice de busca customizado propriamente definido deve incluir um nó filho chamado `indexRules` e este nó deve ter pelo menos um filho.

---

### IndexName - Custom Search Index Definition Nodes Must Follow Naming Conventions

| Atributo | Valor |
|----------|-------|
| **Key** | IndexName |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas sejam nomeadas seguindo um padrão específico.

---

### IndexType - Custom Search Index Definition Nodes Must Use the Index Type lucene

| Atributo | Valor |
|----------|-------|
| **Key** | IndexType |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas tenham uma propriedade `type` com o valor definido como `lucene`.

---

### IndexSeedProperty - Custom Search Index Definition Nodes Must Not Contain seed Property

| Atributo | Valor |
|----------|-------|
| **Key** | IndexSeedProperty |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas de conter uma propriedade chamada `seed`.

---

### IndexReindexProperty - Custom Search Index Definition Nodes Must Not Contain reindex Property

| Atributo | Valor |
|----------|-------|
| **Key** | IndexReindexProperty |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas de conter uma propriedade chamada `reindex`.

---

### IndexNotUnderUIContent - Index definition nodes must not be deployed in UI content package

| Atributo | Valor |
|----------|-------|
| **Key** | IndexNotUnderUIContent |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas de serem deployadas no pacote UI Content.

---

### CustomFulltextIndexesOfTheDamAssetCheck - damAssetLucene prefix required

| Atributo | Valor |
|----------|-------|
| **Key** | CustomFulltextIndexesOfTheDamAssetCheck |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice full-text customizadas do tipo `damAssetLucene` de serem prefixadas com qualquer coisa diferente de `damAssetLucene`.

---

### DuplicateNameProperty - No duplicate property names

| Atributo | Valor |
|----------|-------|
| **Key** | DuplicateNameProperty |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas de conter propriedades com o mesmo nome.

---

### RestrictIndexCustomization - OOTB indexes cannot be customized

| Atributo | Valor |
|----------|-------|
| **Key** | RestrictIndexCustomization |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe modificações não autorizadas dos seguintes índices OOTB: `nodetypeLucene`, `slingResourceResolver`, `socialLucene`, `appsLibsLucene`, `authorizables`, `pathReference`.

---

### AnalyzerTokenizerConfigCheck - Configuration of tokenizers should use name tokenizer

| Atributo | Valor |
|----------|-------|
| **Key** | AnalyzerTokenizerConfigCheck |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe a criação de tokenizers com nomes incorretos em analyzers. Tokenizers devem sempre ser definidos como `tokenizer`.

---

### PathSpacesCheck - Configuration of indexing definitions should not contain spaces

| Atributo | Valor |
|----------|-------|
| **Key** | PathSpacesCheck |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2024.7.0 |

**Descrição**: AEM Cloud Service proíbe a criação de definições de indexação que contenham propriedades com espaços.

---

### HayStackPropertyCheck - Configuration should not contain haystack0 property

| Atributo | Valor |
|----------|-------|
| **Key** | HayStackPropertyCheck |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2024.12.0 |

**Descrição**: AEM Cloud Service proíbe a criação de definições de indexação que contenham propriedades haystack.

---

### IndexUnsupportedAsyncPropertiesCheck - Should not contain async-previous property

| Atributo | Valor |
|----------|-------|
| **Key** | IndexUnsupportedAsyncPropertiesCheck |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2025.3.0 |

**Descrição**: AEM Cloud Service proíbe a criação de definições de indexação com propriedades async não suportadas.

---

### SameTagInMultipleIndexes - Should not have same tag in multiple indexes

| Atributo | Valor |
|----------|-------|
| **Key** | SameTagInMultipleIndexes |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2025.3.0 |

**Descrição**: AEM Cloud Service proíbe a criação de definições de indexação que contenham a mesma tag em múltiplos índices.

---

### FilterXmlModeAnalysis - Should not contain mode replacement for forbidden paths

| Atributo | Valor |
|----------|-------|
| **Key** | FilterXmlModeAnalysis |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2025.4.0 |

**Descrição**: O uso do modo "replacement" no file vault não é permitido para caminhos abaixo de `/content`; não deve ser usado para caminhos abaixo de `/etc` e `/var`.

---

## 🔧 Regras Dispatcher (DOT - Dispatcher Optimization Tool)

### DOTRules:Disp-1---ignoreUrlParams-allow-list

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-1---ignoreUrlParams-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: O cache do farm de publicação do Dispatcher deve ter suas regras ignoreUrlParams configuradas de forma allowlist.

---

### DOTRules:Disp-2---statfileslevel

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-2---statfileslevel |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A propriedade statfileslevel do cache do farm de publicação do Dispatcher deve ser >= 2.

---

### DOTRules:Disp-3---gracePeriod

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-3---gracePeriod |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A propriedade gracePeriod do farm de publicação do Dispatcher deve ser >= 2.

---

### DOTRules:Disp-4---default-filter-deny-rules

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-4---default-filter-deny-rules |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem conter as regras `deny` padrão da versão 6.x.x do archetype AEM.

---

### DOTRules:Disp-5---serveStaleOnError

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-5---serveStaleOnError |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: O cache do farm de publicação do Dispatcher deve ter serveStaleOnError habilitado.

---

### DOTRules:Disp-6---suffix-allow-list

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-6---suffix-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem especificar os padrões de sufixo Sling permitidos de forma allowlist.

---

### DOTRules:Disp-7---selector-allow-list

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-7---selector-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem especificar os seletores Sling permitidos de forma allowlist.

---

### DOTRules:Disp-8---unique-farm-name

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-8---unique-farm-name |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Cada farm do Dispatcher deve ter um nome único.

---

### DOTRules:Disp-S1---brace-missing

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S1---brace-missing |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Cada seção deve começar com um caractere '{'.

---

### DOTRules:Disp-S2---token-unexpected

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S2---token-unexpected |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Pulando token de nível superior desconhecido.

---

### DOTRules:Disp-S3---quote-unmatched

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S3---quote-unmatched |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Aspas não correspondentes encontradas.

---

### DOTRules:Disp-S4---brace-unclosed

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S4---brace-unclosed |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Chave não fechada encontrada.

---

### DOTRules:Disp-S5---mandatory-missing

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S5---mandatory-missing |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Seção está faltando valor obrigatório.

---

### DOTRules:Disp-S6---property-deprecated

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S6---property-deprecated |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Propriedade está deprecada.

---

### DOTRules:Disp-S7---no-dispatcher-config

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S7---no-dispatcher-config |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Não foi possível encontrar arquivo de configuração do Dispatcher.

---

### DOTRules:Httpd-1---require-all-granted

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Httpd-1---require-all-granted |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A diretiva 'Require all granted' não deve ser usada em uma seção Directory do VirtualHost com um caminho de diretório raiz.

---

### DOTRules:Httpd-S1---include-failed

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Httpd-S1---include-failed |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A diretiva Include deve incluir arquivos existentes. Verifique o caminho ou use IncludeOptional.

---

### DOTRules:Syntax0---syntax-violation

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Syntax0---syntax-violation |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Problema de sintaxe encontrado.

---

## 🔄 Changelog e Migrações de Chaves (SonarQube 9.9)

### Migrações de Chaves Completas

A partir de **13 de Fevereiro de 2025** (Cloud Manager 2025.2.0), as seguintes chaves foram migradas de `squid:*` para `java:*`:

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
| squid:S2070 | java:S4790 |
| squid:S2245 | java:S2245 |
| squid:S2257 | java:S2257 |
| squid:S2077 | java:S2077 |
| squid:S2092 | java:S2092 |
| squid:ObjectFinalizeOverridenCallsSuperFinalizeCheck | java:S1114 |
| squid:S1143 | java:S1143 |
| squid:S2222 | java:S2222 |
| squid:S2441 | java:S2441 |
| squid:S3518 | java:S3518 |
| squid:ObjectFinalizeCheck | java:S1111 |
| squid:S1217 | java:S1217 |
| squid:S1872 | java:S1872 |
| squid:S2159 | java:S2159 |
| squid:S2225 | java:S2225 |
| squid:S2226 | java:S2226 |
| squid:S2259 | java:S2259 |
| squid:S2273 | java:S2273 |
| squid:S2445 | java:S2445 |
| squid:S2583 | java:S2583 |
| squid:S1145 | java:S2583 |
| squid:S2885 | java:S2885 |
| squid:S3655 | java:S3655 |
| squid:S1206 | java:S1206 |
| squid:S2184 | java:S2184 |
| squid:S899 | java:S899 |
| squid:S1174 | java:S1174 |
| squid:SwitchLastCaseIsDefaultCheck | java:S131 |
| squid:S1948 | java:S1948 |
| squid:S888 | java:S888 |
| squid:S00112 | java:S112 |
| squid:AssignmentInSubExpressionCheck | java:S1121 |
| squid:S1134 | java:S1134 |
| squid:S1181 | java:S1181 |
| squid:S1696 | java:S1696 |
| squid:S1854 | java:S1854 |
| squid:S2112 | java:S2112 |
| squid:S2442 | java:S2442 |
| squid:S1850 | java:S2589 |
| squid:S2681 | java:S2681 |
| squid:S864 | java:S864 |
| squid:ClassVariableVisibilityCheck | java:S1104 |
| squid:S1182 | java:S1182 |
| squid:S1444 | java:S1444 |
| squid:S1698 | java:S1698 |
| squid:CallToDeprecatedMethod | java:S1874 |
| squid:S2221 | java:S2221 |
| squid:S2384 | java:S2384 |
| squid:S2386 | java:S2386 |
| squid:S1135 | java:S1135 |
| squid:S1989 | java:S1989 |

### Regras Removidas na Versão 2024.12.0

As seguintes regras foram removidas ou consolidadas:

| Regra Removida | Motivo |
|----------------|--------|
| squid:S2258 | Consolidada em java:S5547 |
| squid:S2278 | Consolidada em java:S5547 |
| squid:S3369 | Removida |
| squid:S3374 | Removida |
| squid:S2089 | Removida |
| squid:S2653 | Removida |
| squid:S3355 | Removida |
| squid:S2142 | Removida |
| AEM Rules:AEM-17 | Removida |
| AEM Rules:AEM-14 | Removida |

---

## 📚 Referências

### Documentação Oficial

- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Code Quality Testing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/code-quality-testing)
- [Content Search and Indexing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing)
- [Page Templates - Editable](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)
- [Using Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)

### Ferramentas

- [SonarQube Documentation](https://docs.sonarsource.com/sonarqube-server/latest/)
- [Dispatcher Optimization Tool (DOT)](https://github.com/adobe/aem-dispatcher-optimizer-tool/blob/main/docs/Rules.md)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)
- [AEM Assets Cloud Migration Tool](https://github.com/adobe/aem-cloud-migration)

### Arquivos CSV de Referência

- `CodeQuality-rules-latest-AMS-2024-12-0.csv` - Versão mais recente (SonarQube 9.9)
- `CodeQuality-rules-latest-AMS.csv` - Versão anterior

---

*Última atualização: 10 de Dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
