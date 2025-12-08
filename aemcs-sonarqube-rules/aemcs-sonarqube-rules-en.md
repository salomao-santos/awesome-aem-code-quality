# Custom Code Quality Rules

**Last update:** 05 de novembro de 2025

**Applies to:** Experience Manager as a Cloud Service

**Created for:** Admin, Developer

## Overview

Learn about Cloud Manager's custom code quality rules, based on Adobe Experience Manager Engineering best practices, to ensure high-quality code through thorough testing.

Full SonarQube rules are not available for download due to Adobe proprietary information. You can download the complete list of rules *current* using the following links:

- [Current rules (CodeQuality-rules-latest-CS.xlsx)](/docs/experience-manager-cloud-service/assets/CodeQuality-rules-latest-CS.xlsx)
- [Updated rules for Cloud Manager 2025.2.0 (CodeQuality-rules-latest-CS-2024-12-0.xlsx)](/docs/experience-manager-cloud-service/assets/CodeQuality-rules-latest-CS-2024-12-0.xlsx)

> **IMPORTANT:** Starting Thursday, February 13, 2025 (Cloud Manager 2025.2.0), Cloud Manager Code Quality is using an updated SonarQube 9.9 version and an updated list of rules.

> **NOTE:** The code samples provided here are only for illustrative purposes. See the [SonarQube Concepts documentation](https://docs.sonarsource.com/sonarqube/latest/) to learn about SonarQube concepts and quality rules.

## Document Statistics

This document contains **172 rules** organized in the following categories:

- **Vulnerabilities**: 30 rules
- **Security Hotspots**: 6 rules (new in version 2024.12.0)
- **Bugs**: 46 rules
- **Code Smells**: 71 rules

### Main Categories

1. **SonarQube Rules**: Focused on Java coding practices, security, performance and maintainability
2. **OakPAL Rules**: Focused on AEM content structure, OSGi configurations and index definitions
3. **FindBugs Rules**: Detection of common bugs in Java code
4. **FindSecBugs Rules**: Focused on security vulnerabilities
5. **AEM Specific Rules**: Adobe Experience Manager specific best practices

### What's New in Version 2024.12.0

- ✅ 2 new rules added
- ⚠️ 9 rules removed
- 🔄 Key changes: squid: → java:
- 📊 Introduction of "Security Hotspots" as new rule type
- 🔀 Several rules merged and reclassified

---

## SonarQube Rules

### Do not use potentially dangerous functions

- **Key**: CQRules:CWE-676
- **Type**: Vulnerability
- **Severity**: Major
- **Since**: Versão 2018.4.0

Os métodos `Thread.stop()` e `Thread.interrupt()` podem produzir problemas difíceis de reproduzir e, às vezes, vulnerabilidades de segurança. Seu uso deve ser rigorosamente monitorado e validado. Em geral, a passagem de mensagens é uma maneira mais segura de atingir objetivos semelhantes.

#### Non-compliant code

```java
public class DontDoThis implements Runnable {
  private Thread thread;

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop() {
    thread.stop();  // INSEGURO!
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

### Não use strings de formato que possam ser controladas externamente

- **Key**: CQRules:CWE-134
- **Type**: Vulnerability
- **Severity**: Major
- **Since**: Versão 2018.4.0

Usar uma string de formato de uma fonte externa (como um parâmetro de solicitação ou conteúdo gerado pelo usuário) pode expor uma aplicação a ataques de negação de serviço. Existem circunstâncias em que uma string de formato pode ser controlada externamente, mas é permitida apenas de fontes confiáveis.

#### Non-compliant code

```java
protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
  String messageFormat = request.getParameter("messageFormat");
  request.getResource().getValueMap().put("some property", String.format(messageFormat, "some text"));
  response.sendStatus(HttpServletResponse.SC_OK);
}
```

---

### Requisições HTTP devem sempre ter timeouts de socket e conexão

- **Key**: CQRules:ConnectionTimeoutMechanism
- **Type**: Bug
- **Severity**: Critical
- **Since**: Versão 2018.6.0

Ao fazer requisições HTTP dentro de uma aplicação Experience Manager, é essencial configurar timeouts apropriados para evitar consumo desnecessário de threads. Por padrão, tanto o Java™ HTTP Client (java.net.HttpUrlConnection) quanto o cliente Apache HTTP Components amplamente usado não impõem timeouts, então eles devem ser configurados manualmente. Como melhor prática, os timeouts devem ser definidos para 60 segundos ou menos.

#### Non-compliant code

```java
@Reference
private HttpClientBuilderFactory httpClientBuilderFactory;

public void dontDoThis() {
  HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
  HttpClient httpClient = builder.build();

  // fazer algo com o cliente
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

  // fazer algo com o cliente
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

### Sempre feche objetos ResourceResolver

- **Key**: CQRules:CQBP-72
- **Type**: Code Smell
- **Severity**: Major
- **Since**: Versão 2018.4.0

Objetos ResourceResolver obtidos do `ResourceResolverFactory` consomem recursos do sistema. Embora existam medidas para recuperar esses recursos quando um `ResourceResolver` não está mais em uso, é mais eficiente fechar explicitamente quaisquer objetos `ResourceResolver` abertos chamando o método `close()`.

Um equívoco comum é que objetos `ResourceResolver` criados com uma sessão JCR existente não devem ser fechados explicitamente, ou que fechá-los afeta a sessão JCR. Esta informação está incorreta. Um `ResourceResolver` deve sempre ser fechado quando não for mais necessário. Como `ResourceResolver` implementa a interface `Closeable`, você também pode usar a sintaxe `try-with-resources` em vez de chamar `close()` diretamente.

#### Non-compliant code

```java
public void dontDoThis(Session session) throws Exception {
  ResourceResolver resolver = factory.getResourceResolver(Collections.singletonMap("user.jcr.session", (Object)session));
  // fazer algo com o resolver
}
```

#### Compliant code

```java
public void doThis(Session session) throws Exception {
  ResourceResolver resolver = null;
  try {
    resolver = factory.getResourceResolver(Collections.singletonMap("user.jcr.session", (Object)session));
    // fazer algo com o resolver
  } finally {
    if (resolver != null) {
      resolver.close();
    }
  }
}

public void orDoThis(Session session) throws Exception {
  try (ResourceResolver resolver = factory.getResourceResolver(Collections.singletonMap("user.jcr.session", (Object) session))){
    // fazer algo com o resolver
  }
}
```

---

### Não use caminhos de servlet Sling para registrar servlets

- **Key**: CQRules:CQBP-75
- **Type**: Code Smell
- **Severity**: Major
- **Since**: Versão 2018.4.0

Conforme descrito na [documentação do Sling](https://sling.apache.org/documentation/the-sling-engine/servlets.html), vincular servlets por caminhos é desencorajado. Servlets vinculados a caminhos não podem usar controles de acesso JCR padrão e, como resultado, requerem rigor de segurança adicional. Em vez de usar servlets vinculados a caminhos, é recomendado criar nós no repositório e registrar servlets por tipo de recurso.

#### Non-compliant code

```java
@Component(property = {
  "sling.servlet.paths=/apps/myco/endpoint"
})
public class DontDoThis extends SlingAllMethodsServlet {
 // implementação
}
```

---

### Exceções capturadas devem ser registradas ou lançadas, mas não ambos

- **Key**: CQRules:CQBP-44—CatchAndEitherLogOrThrow
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

Em geral, uma exceção deve ser registrada exatamente uma vez. Registrar exceções várias vezes pode causar confusão. A razão é que não fica claro quantas vezes uma exceção ocorreu. O padrão mais comum que leva a esse efeito é registrar e lançar uma exceção capturada.

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

### Evite ter uma instrução de log imediatamente seguida por uma instrução throw

- **Key**: CQRules:CQBP-44—ConsecutivelyLogAndThrow
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

Outro padrão comum a evitar é registrar uma mensagem e depois lançar imediatamente uma exceção. Esta prática geralmente indica que a mensagem de exceção acaba duplicada nos arquivos de log.

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

### Evite registrar em INFO ao manipular requisições GET ou HEAD

- **Key**: CQRules:CQBP-44—LogInfoInGetOrHeadRequests
- **Type**: Code Smell
- **Severity**: Minor

Em geral, o nível de log INFO deve ser usado para demarcar ações importantes e, por padrão, o Experience Manager está configurado para registrar no nível INFO ou acima. Os métodos GET e HEAD devem ser apenas operações somente leitura e, portanto, não constituem ações importantes. Registrar no nível INFO em resposta a requisições GET ou HEAD provavelmente criará ruído significativo no log, dificultando a identificação de informações úteis nos arquivos de log. Ao manipular requisições GET ou HEAD, registre nos níveis WARN ou ERROR se algo deu errado. Use os níveis DEBUG ou TRACE se informações detalhadas de solução de problemas forem necessárias.

> **NOTE:** Não se aplica ao registro do tipo `access.log` para cada requisição.

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

### Não use Exception.getMessage() como primeiro parâmetro de uma instrução de log

- **Key**: CQRules:CQBP-44—ExceptionGetMessageIsFirstLogParam
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

Como melhor prática, as mensagens de log devem fornecer informações contextuais sobre onde na aplicação ocorreu uma exceção. Embora o contexto também possa ser determinado usando stack traces, em geral a mensagem de log será mais fácil de ler e entender. Como resultado, ao registrar uma exceção, é uma má prática usar a mensagem da exceção como mensagem de log. A mensagem de exceção explica o que deu errado, enquanto a mensagem de log deve informar ao leitor o que a aplicação estava fazendo quando a exceção ocorreu. A mensagem de exceção ainda é registrada. Ao especificar sua própria mensagem, os logs ficam mais fáceis de entender.

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

### Registro em blocos catch deve estar no nível WARN ou ERROR

- **Key**: CQRules:CQBP-44—WrongLogLevelInCatchBlock
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

Como o nome sugere, exceções Java™ devem sempre ser usadas em circunstâncias excepcionais. Como resultado, quando uma exceção é capturada, é importante garantir que as mensagens de log sejam registradas no nível apropriado, seja WARN ou ERROR. Isso garante que essas mensagens apareçam corretamente nos logs.

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

### Não imprima stack traces no console

- **Key**: CQRules:CQBP-44—ExceptionPrintStackTrace
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

Como mencionado, o contexto é crítico ao entender mensagens de log. Usar `Exception.printStackTrace()` faz com que apenas o stack trace seja enviado para o fluxo de erro padrão, perdendo todo o contexto. Além disso, em uma aplicação multi-threaded como o Experience Manager, se várias exceções forem impressas usando este método em paralelo, seus stack traces podem se sobrepor, o que produz confusão significativa. As exceções devem ser registradas apenas através do framework de logging.

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

### Não envie saída para standard output ou standard error

- **Key**: CQRules:CQBP-44—LogLevelConsolePrinters
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

O registro no Experience Manager deve sempre ser feito através do framework de logging (SLF4J). Enviar saída diretamente para os fluxos de saída padrão ou erro padrão perde as informações estruturais e contextuais fornecidas pelo framework de logging. Às vezes, isso pode causar problemas de desempenho.

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

### Evite caminhos hardcoded de /apps e /libs

- **Key**: CQRules:CQBP-71
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2018.4.0

Caminhos começando com `/libs` e `/apps` geralmente não devem ser hardcoded. Esses caminhos são geralmente armazenados relativos ao caminho de busca do Sling, que por padrão é `/libs,/apps`. Usar o caminho absoluto pode introduzir defeitos sutis que só apareceriam mais tarde no ciclo de vida do projeto.

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

### Não use o Sling Scheduler

- **Key**: CQRules:AMSCORE-554
- **Type**: Code Smell/Compatibilidade com Cloud Service
- **Severity**: Minor
- **Since**: Versão 2020.5.0

Não use o Sling Scheduler para tarefas que requerem execução garantida. Sling Scheduled Jobs garantem execução e são mais adequados para ambientes clusterizados e não clusterizados.

Consulte [Apache Sling Eventing and Job Handling](https://sling.apache.org/documentation/bundles/apache-sling-eventing-and-job-handling.html) para saber mais sobre como Sling Jobs são manipulados em ambientes clusterizados.

---

### Não use APIs descontinuadas do Experience Manager

- **Key**: AMSCORE-553
- **Type**: Code Smell/Compatibilidade com Cloud Service
- **Severity**: Minor
- **Since**: Versão 2020.5.0

A superfície da API do Experience Manager está sob revisão constante para identificar APIs cujo uso é desencorajado e, portanto, consideradas descontinuadas.

Frequentemente, essas APIs são descontinuadas usando a anotação Java™ padrão `@Deprecated` e, como tal, identificadas por `squid:CallToDeprecatedMethod`.

No entanto, há casos em que uma API é descontinuada no contexto do Experience Manager, mas pode não ser descontinuada em outros contextos. Esta regra identifica essa segunda classe.

---

### Não use anotação @Inject com @Optional em Sling Models

- **Key**: InjectAnnotationWithOptionalInjectionCheck
- **Type**: Qualidade de software
- **Severity**: Minor
- **Since**: Versão 2023.11

O projeto Apache Sling desencoraja o uso da anotação `@Inject` no contexto de Sling Models, pois pode levar a mau desempenho quando combinada com `DefaultInjectionStrategy.OPTIONAL` (seja no nível de campo ou classe). Em vez disso, injeções mais específicas (como as anotações `@ValueMapValue` ou `@OsgiInjector`) devem ser usadas.

Consulte a [documentação do Apache Sling](https://sling.apache.org/documentation/bundles/models.html#discouraged-annotations-1) para mais informações sobre as anotações recomendadas e por que essa recomendação foi feita.

---

### Reutilize instâncias de HTTPClient

- **Key**: AEMSRE-870
- **Type**: Qualidade de software
- **Severity**: Minor
- **Since**: Versão 2023.11

Aplicações AEM frequentemente se comunicam com outras aplicações usando o protocolo HTTP, e o Apache HttpClient é uma biblioteca frequentemente usada para atingir esse fim. Mas a criação de tal objeto HttpClient vem com alguma sobrecarga, então esses objetos devem ser reutilizados o máximo possível.

Esta regra verifica se tal objeto HttpClient não é privado dentro de um método, mas global no nível da classe, para que possa ser reutilizado. Neste caso, o campo HttpClient deve ser definido no construtor da classe ou no método `activate()` (se esta classe for um componente/serviço OSGi).

Consulte o [Guia de Otimização](https://hc.apache.org/httpclient-legacy/performance.html) do HttpClient para algumas melhores práticas sobre o uso do HttpClient.

#### Non-compliant code

```java
public void doHttpCall() {
  HttpClient httpclient = HttpClients.createDefault();
  // fazer algo com o httpclient
}
```

#### Compliant code

```java
public class myClass {

  HttpClient httpclient;

  public void doHttpCall() {
    // fazer algo com o httpclient
  }

}
```

---

## Regras de Conteúdo OakPAL

> **NOTE:** OakPAL é um framework que valida pacotes de conteúdo usando um repositório Oak autônomo. Um Parceiro Experience Manager, que ganhou o prêmio 2019 Experience Manager Rockstar North America, o desenvolveu.


### Clientes não devem implementar ou estender APIs de Produto anotadas com @ProviderType

- **Key**: CQBP-84
- **Type**: Bug
- **Severity**: Critical
- **Since**: Versão 2018.7.0

A API do Experience Manager contém interfaces e classes Java™ que são destinadas apenas a serem usadas – mas não implementadas – por código personalizado. Por exemplo, apenas o Experience Manager deve implementar a interface `com.day.cq.wcm.api.Page`.

Quando novos métodos são adicionados a essas interfaces, esses métodos adicionais não impactam o código existente que usa essas interfaces. Como resultado, a adição de novos métodos a essas interfaces é considerada retrocompatível. No entanto, se o código personalizado implementar uma dessas interfaces, esse código personalizado introduziu um risco de retrocompatibilidade para o cliente.

Interfaces e classes – conforme implementadas pelo Experience Manager – são anotadas com `org.osgi.annotation.versioning.ProviderType` ou às vezes uma anotação legada similar `aQute.bnd.annotation.ProviderType`. Esta regra identifica casos em que o código personalizado implementa tal interface ou estende uma classe.

#### Non-compliant code

```java
import com.day.cq.wcm.api.Page;

public class DontDoThis implements Page {
// implementação aqui
}
```

---

### Índices Oak Lucene personalizados devem ter uma configuração Tika

- **Key**: IndexTikaNode
- **Type**: Bug
- **Severity**: Blocker
- **Since**: 2021.8.0

Vários índices Oak do Experience Manager prontos para uso incluem uma configuração Tika e personalizações desses índices devem incluir uma configuração Tika. Esta regra verifica personalizações dos índices `damAssetLucene`, `lucene` e `graphqlConfig` e levanta um problema se o nó `tika` estiver faltando ou se o nó `tika` estiver faltando um nó filho chamado `config.xml`.

#### Non-compliant code

```
+ oak:index
    + damAssetLucene-1-custom
      - async: [async]
      - evaluatePathRestrictions: true
      - includedPaths: /content/dam
      - tags: [visualSimilaritySearch]
      - type: lucene
```

#### Compliant code

```
+ oak:index
    + damAssetLucene-1-custom-2
      - async: [async]
      - evaluatePathRestrictions: true
      - includedPaths: /content/dam
      - tags: [visualSimilaritySearch]
      - type: lucene
      + tika
        + config.xml
```

---

### Índices Oak Lucene personalizados não devem ser síncronos

- **Key**: IndexAsyncProperty
- **Type**: Bug
- **Severity**: Blocker
- **Since**: 2021.8.0

Índices Oak do tipo `lucene` devem sempre ser indexados de forma assíncrona. Não fazer isso pode resultar em instabilidade do sistema.

#### Non-compliant code

```
+ oak:index
    + damAssetLucene-1-custom
      - evaluatePathRestrictions: true
      - includedPaths: /content/dam
      - type: lucene
      - tags: [visualSimilaritySearch]
      + tika
        + config.xml
```

#### Compliant code

```
+ oak:index
    + damAssetLucene-1-custom-2
      - async: [async]
      - evaluatePathRestrictions: true
      - includedPaths: /content/dam
      - tags: [visualSimilaritySearch]
      - type: lucene
      + tika
        + config.xml
```

---

### Índices Oak Lucene de DAM Asset personalizados devem estar estruturados adequadamente

- **Key**: IndexDamAssetLucene
- **Type**: Bug
- **Severity**: Blocker
- **Since**: 2021.6.0

Para que a pesquisa de ativos funcione corretamente no Experience Manager Assets, as personalizações do índice Oak `damAssetLucene` devem seguir um conjunto de diretrizes específicas para este índice. Esta regra verifica se a definição do índice deve ter uma propriedade de múltiplos valores chamada `tags`, que contém o valor `visualSimilaritySearch`.

#### Non-compliant code

```
+ oak:index
    + damAssetLucene-1-custom
      - async: [async, nrt]
      - evaluatePathRestrictions: true
      - includedPaths: /content/dam
      - type: lucene
      + tika
        + config.xml
```

#### Compliant code

```
+ oak:index
    + damAssetLucene-1-custom-2
      - async: [async, nrt]
      - evaluatePathRestrictions: true
      - includedPaths: /content/dam
      - tags: [visualSimilaritySearch]
      - type: lucene
      + tika
        + config.xml
```

---

### Pacotes de clientes não devem criar ou modificar nós em /libs

- **Key**: BannedPath
- **Type**: Bug
- **Severity**: Critical
- **Since**: Versão 2019.6.0

É uma prática recomendada de longa data que a árvore de conteúdo `/libs` no repositório de conteúdo do Experience Manager deve ser considerada somente leitura pelos clientes. Modificar nós e propriedades em `/libs` cria riscos significativos para atualizações maiores e menores. Use a Adobe, através de canais oficiais, para fazer modificações em `/libs`.

---

### Pacotes não devem conter configurações OSGi duplicadas

- **Key**: DuplicateOsgiConfigurations
- **Type**: Bug
- **Severity**: Major
- **Since**: Versão 2019.6.0

Um problema comum que ocorre em projetos complexos é onde o mesmo componente OSGi é configurado várias vezes. Este problema cria uma ambiguidade sobre qual configuração é aplicável. Esta regra é "consciente do modo de execução" pois identifica apenas problemas onde o mesmo componente é configurado várias vezes no mesmo modo de execução ou combinação de modos de execução.

> **NOTE:** Esta regra produz problemas onde a mesma configuração, no mesmo caminho, é definida em vários pacotes, incluindo casos onde o mesmo pacote é duplicado na lista geral de pacotes construídos.

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

### Pastas config e install devem conter apenas nós OSGi

- **Key**: ConfigAndInstallShouldOnlyContainOsgiNodes
- **Type**: Bug
- **Severity**: Major
- **Since**: Versão 2019.6.0

Por razões de segurança, caminhos contendo `/config/` e `/install/` são legíveis apenas por usuários administrativos no Experience Manager e devem ser usados apenas para configuração OSGi e bundles OSGi. Colocar outros tipos de conteúdo sob caminhos contendo esses segmentos faz com que o comportamento da aplicação difira entre usuários administrativos e não administrativos de forma não intencional.

Um problema comum é o uso de nós chamados `config` dentro de diálogos de componentes ou ao especificar a configuração do editor de rich text para edição inline. Para resolver este problema, o nó ofensivo deve ser renomeado para um nome compatível. Para a configuração do editor de rich text, use a propriedade `configPath` no nó `cq:inplaceEditing` para especificar o novo local.

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

### Pacotes não devem se sobrepor

- **Key**: PackageOverlaps
- **Type**: Bug
- **Severity**: Major
- **Since**: Versão 2019.6.0

Semelhante à regra de Configurações OSGi Duplicadas, esta situação é um problema comum em projetos complexos onde o mesmo caminho de nó é escrito por vários pacotes de conteúdo separados. Embora o uso de dependências de pacotes de conteúdo possa ser usado para garantir um resultado consistente, é melhor evitar sobreposições completamente.

---

### O modo de autoria padrão não deve ser Classic UI

- **Key**: ClassicUIAuthoringMode
- **Type**: Code Smell/Compatibilidade com Cloud Service
- **Severity**: Minor
- **Since**: Versão 2020.5.0

A configuração OSGi `com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl` define o modo de autoria padrão no Experience Manager. Como a Classic UI foi descontinuada desde o Experience Manager 6.4, um problema agora é levantado quando o modo de autoria padrão está configurado para Classic UI.

---

### Componentes com diálogos devem ter diálogos Touch UI

- **Key**: ComponentWithOnlyClassicUIDialog
- **Type**: Code Smell/Compatibilidade com Cloud Service
- **Severity**: Minor
- **Since**: Versão 2020.5.0

Componentes do Experience Manager que têm um diálogo Classic UI devem sempre ter um diálogo Touch UI correspondente. Ambos oferecem uma experiência de autoria ideal compatível com o modelo de implantação do Cloud Service, onde a Classic UI não é mais suportada. Esta regra verifica os seguintes cenários:

- Um componente com um diálogo Classic UI (ou seja, um nó filho `dialog`) deve ter um diálogo Touch UI correspondente (ou seja, um nó filho `cq:dialog`).
- Um componente com um diálogo de design Classic UI (ou seja, um nó `design_dialog`) deve ter um diálogo de design Touch UI correspondente (ou seja, um nó filho `cq:design_dialog`).
- Um componente com um diálogo Classic UI e um diálogo de design Classic UI deve ter tanto um diálogo Touch UI correspondente quanto um diálogo de design Touch UI correspondente.

---

### Pacotes não devem misturar conteúdo mutável e imutável

- **Key**: ImmutableMutableMixedPackage
- **Type**: Code Smell/Compatibilidade com Cloud Service
- **Severity**: Minor
- **Since**: Versão 2020.5.0

Para ser compatível com o modelo de implantação do Cloud Service, pacotes de conteúdo individuais devem conter conteúdo para as áreas imutáveis do repositório (`/apps` e `/libs`), ou a área mutável (tudo que não está em `/apps` ou `/libs`), mas não ambos. Por exemplo, um pacote que inclui tanto `/apps/myco/components/text` quanto `/etc/clientlibs/myco` não é compatível com o Cloud Service e causa um problema a ser relatado.

> **NOTE:** A regra "Pacotes de Clientes Não Devem Criar ou Modificar Nós em libs" sempre se aplica.

---

### Não use agentes de replicação reversa

- **Key**: ReverseReplication
- **Type**: Code Smell/Compatibilidade com Cloud Service
- **Severity**: Minor
- **Since**: Versão 2020.5.0

O suporte para replicação reversa não está disponível em implantações do Cloud Service. Clientes usando replicação reversa devem entrar em contato com a Adobe para soluções alternativas.

---

### Recursos em bibliotecas de cliente habilitadas para proxy devem estar em uma pasta chamada resources

- **Key**: ClientlibProxyResource
- **Type**: Bug
- **Severity**: Minor
- **Since**: Versão 2021.2.0

Bibliotecas de cliente do Experience Manager podem conter recursos estáticos como imagens e fontes. Ao usar bibliotecas de cliente com proxy, esses recursos estáticos devem estar contidos em uma pasta filho chamada `resources` para serem efetivamente referenciados nas instâncias de publicação.

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

### Uso de processos de workflow incompatíveis com Cloud Service

- **Key**: CloudServiceIncompatibleWorkflowProcess
- **Type**: Bug
- **Severity**: Major
- **Since**: Versão 2021.2.0

Com a mudança para micro-serviços de ativos para processamento de ativos no Adobe Experience Manager as a Cloud Service, vários processos de workflow usados em versões on-premise e AMS agora não são suportados. Muitos desses workflows também se tornaram desnecessários.

A ferramenta de migração no [repositório GitHub do Experience Manager as a Cloud Service Assets](https://github.com/adobe/aem-cloud-migration) pode ser usada para atualizar modelos de workflow durante a migração para o Experience Manager as a Cloud Service.

---

### O uso de templates estáticos é desencorajado em favor de templates editáveis

- **Key**: StaticTemplateUsage
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

Embora o uso de templates estáticos seja historicamente comum em projetos do Experience Manager, a Adobe recomenda templates editáveis porque fornecem mais flexibilidade e suportam recursos adicionais não presentes em templates estáticos.

A migração de templates estáticos para editáveis pode ser amplamente automatizada usando as [Ferramentas de Modernização do Experience Manager](https://opensource.adobe.com/aem-modernize-tools/).

---

### O uso de componentes foundation legados é desencorajado

- **Key**: LegacyFoundationComponentUsage
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

Os Componentes Foundation legados (ou seja, componentes em `/libs/foundation`) foram descontinuados para várias versões do Experience Manager em favor dos Core Components. O uso dos Componentes Foundation como base para componentes personalizados (seja por overlay ou herança) é desencorajado e deve ser convertido para os Core Components correspondentes.

As [Ferramentas de Modernização do Experience Manager](https://opensource.adobe.com/aem-modernize-tools/) podem facilitar essa conversão.

---

### Use apenas nomes e ordenação de modos de execução suportados

- **Key**: SupportedRunmode
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

O Experience Manager as a Cloud Service impõe uma política de nomenclatura estrita para nomes de modos de execução e uma ordenação estrita para esses modos de execução. A lista de modos de execução suportados está fundamentada na documentação e qualquer desvio desta lista é identificado como um problema.

---

### Nós de definição de índice de pesquisa personalizado devem ser filhos diretos de /oak:index

- **Key**: OakIndexLocation
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

O Experience Manager as a Cloud Service requer que definições de índice de pesquisa personalizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) sejam nós filhos diretos de `/oak:index`. Índices em outros locais devem ser movidos para serem compatíveis com o Experience Manager as a Cloud Service.

---

### Nós de definição de índice de pesquisa personalizado devem ter compatVersion de 2

- **Key**: IndexCompatVersion
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

O Experience Manager as a Cloud Service requer que definições de índice de pesquisa personalizadas (como nós do tipo `oak:QueryIndexDefinition`) devem ter a propriedade `compatVersion` definida como `2`. O Adobe Experience Manager as a Cloud Service não suporta nenhum outro valor.

---

### Nós descendentes de nós de definição de índice de pesquisa personalizado devem ser do tipo nt:unstructured

- **Key**: IndexDescendantNodeType
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

Problemas difíceis de solucionar podem ocorrer quando um nó de definição de índice de pesquisa personalizado tem nós filhos desordenados. Para evitar essa situação, é recomendado que todos os nós descendentes de um nó `oak:QueryIndexDefinition` sejam do tipo `nt:unstructured`.

---

### Nós de definição de índice de pesquisa personalizado devem conter um nó filho chamado indexRules que tenha filhos

- **Key**: IndexRulesNode
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

Um nó de definição de índice de pesquisa personalizado adequadamente definido deve conter um nó filho chamado `indexRules`, que por sua vez deve ter pelo menos um filho.

---

### Nós de definição de índice de pesquisa personalizado devem seguir convenções de nomenclatura

- **Key**: IndexName
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

O Experience Manager as a Cloud Service requer que definições de índice de pesquisa personalizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) devem ser nomeadas seguindo um padrão específico descrito na documentação.

---

### Nós de definição de índice de pesquisa personalizado devem usar o tipo de índice Lucene

- **Key**: IndexType
- **Type**: Bug
- **Severity**: Blocker
- **Since**: Versão 2021.2.0 (tipo e severidade alterados em 2021.8.0)

O Experience Manager as a Cloud Service requer que definições de índice de pesquisa personalizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) tenham uma propriedade `type` com o valor definido como `lucene`. A indexação usando tipos de índice legados deve ser atualizada antes da migração para o Experience Manager as a Cloud Service.

---

### Nós de definição de índice de pesquisa personalizado não devem conter uma propriedade chamada seed

- **Key**: IndexSeedProperty
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

O Experience Manager as a Cloud Service proíbe definições de índice de pesquisa personalizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) de conter uma propriedade chamada `seed`. A indexação usando esta propriedade deve ser atualizada antes da migração para o Experience Manager as a Cloud Service.

---

### Nós de definição de índice de pesquisa personalizado não devem conter uma propriedade chamada reindex

- **Key**: IndexReindexProperty
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2021.2.0

O Experience Manager as a Cloud Service proíbe definições de índice de pesquisa personalizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) de conter uma propriedade chamada `reindex`. A indexação usando esta propriedade deve ser atualizada antes da migração para o Experience Manager as a Cloud Service.

---

### Nós lucene de ativos DAM personalizados não devem especificar queryPaths

- **Key**: IndexDamAssetLucene
- **Type**: Bug
- **Severity**: Blocker
- **Since**: Versão 2022.1.0

#### Non-compliant code

```
+ oak:index
    + damAssetLucene-1-custom-1
      - async: [async, nrt]
      - evaluatePathRestrictions: true
      - includedPaths: [/content/dam]
      - queryPaths: [/content/dam]
      - type: lucene
      + tika
        + config.xml
```

#### Compliant code

```
+ oak:index
    + damAssetLucene-1-custom-2
      - async: [async, nrt]
      - evaluatePathRestrictions: true
      - includedPaths: [/content/dam]
      - tags: [visualSimilaritySearch]
      - type: lucene
      + tika
        + config.xml
```

---

### Se a definição de índice de pesquisa personalizado contém compatVersion, deve ser definido como 2

- **Key**: IndexCompatVersion
- **Type**: Code Smell
- **Severity**: Major
- **Since**: Versão 2022.1.0

---

### Nó de índice especificando includedPaths também deve especificar queryPaths com os mesmos valores

- **Key**: IndexIncludedPathsWithoutQueryPaths
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2023.1.0

Para índices personalizados, configure `includedPaths` e `queryPaths` com valores idênticos. Se um for especificado, o outro deve corresponder. No entanto, há um caso especial para índices de `damAssetLucene`, incluindo suas versões personalizadas. Para esses casos, forneça apenas `includedPaths`.

---

### Nó de índice especificando nodeScopeIndex em tipo de nó genérico também deve especificar includedPaths e queryPaths

- **Key**: IndexFulltextOnGenericType
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2023.1.0

Ao definir a propriedade `nodeScopeIndex` em um tipo de nó "genérico" como `nt:unstructured` ou `nt:base`, você também deve especificar as propriedades `includedPaths` e `queryPaths`. O tipo de nó `nt:base` pode ser considerado "genérico", porque todos os tipos de nó herdam dele. Portanto, definir um `nodeScopeIndex` em `nt:base` faz com que ele indexe todos os nós no repositório. Da mesma forma, `nt:unstructured` também é considerado "genérico", pois há muitos nós em repositórios que são desse tipo.

#### Non-compliant code

```
+ oak:index/acme.someIndex-custom-1
  - async: [async, nrt]
  - evaluatePathRestrictions: true
  - tags: [visualSimilaritySearch]
  - type: lucene
    + indexRules
      - jcr:primaryType: nt:unstructured
      + nt:base
        - jcr:primaryType: nt:unstructured
        + properties
          + acme.someIndex-custom-1
            - nodeScopeIndex: true
```

#### Compliant code

```
+ oak:index/acme.someIndex-custom-1
  - async: [async, nrt]
  - evaluatePathRestrictions: true
  - tags: [visualSimilaritySearch]
  - type: lucene
  - includedPaths: ["/content/dam/"]
  - queryPaths: ["/content/dam/"]
    + indexRules
      - jcr:primaryType: nt:unstructured
      + nt:base
        - jcr:primaryType: nt:unstructured
        + properties
          + acme.someIndex-custom-1
            - nodeScopeIndex: true
```

---

### A propriedade queryLimitReads do mecanismo de consulta não deve ser sobrescrita

- **Key**: OverrideOfQueryLimitReads
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2023.1.0

Sobrescrever o valor padrão pode levar a leituras de página lentas, particularmente quando mais conteúdo é adicionado.

---

### Múltiplas versões ativas do mesmo índice

- **Key**: IndexDetectMultipleActiveVersionsOfSameIndex
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2023.1.0

#### Non-compliant code

```
+ oak:index
  + damAssetLucene-1-custom-1
    ...
  + damAssetLucene-1-custom-2
    ...
  + damAssetLucene-1-custom-3
    ...
```

#### Compliant code

```
+ damAssetLucene-1-custom-3
    ...
```

---

### O nome de definições de índice totalmente personalizadas deve estar em conformidade com as diretrizes oficiais

- **Key**: IndexValidFullyCustomName
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2023.1.0

O padrão esperado para nomes de índice totalmente personalizados é: `[prefix].[indexName]-custom-[version]`.

---

### Mesma propriedade com valores analyzed diferentes na mesma definição de índice

#### Non-compliant code

```
+ indexRules
  + dam:Asset
    + properties
      + status
        - name: status
        - analyzed: true
  + dam:cfVariationNode
    + properties
      + status
        - name: status
```

#### Compliant code

Exemplo 1:

```
+ indexRules
  + dam:Asset
    + properties
      + status
        - name: status
        - analyzed: true
  + dam:cfVariationNode
    + properties
      + status
        - name: status
        - analyzed: true
```

Exemplo 2:

```
+ indexRules
  + dam:Asset
    + properties
      + status
        - name: status
  + dam:cfVariationNode
    + properties
      + status
        - name: status
        - analyzed: true
```

Se a propriedade analyzed não for explicitamente definida, seu valor padrão é false.

---

### Propriedade tags

- **Key**: IndexHasValidTagsProperty
- **Type**: Code Smell
- **Severity**: Minor
- **Since**: Versão 2023.1.0

Para índices específicos, certifique-se de manter a propriedade tags e seus valores current. Embora adicionar novos valores à propriedade tags seja permitido, excluir quaisquer valores existentes (ou a propriedade completamente) pode levar a resultados inesperados.

---

### Nós de definição de índice não devem ser implantados no pacote de conteúdo UI

- **Key**: IndexNotUnderUIContent
- **Type**: Improvement
- **Severity**: Minor
- **Since**: Versão 2024.6.0

O AEM Cloud Service proíbe definições de índice de pesquisa personalizadas (nós do tipo `oak:QueryIndexDefinition`) de serem implantadas no pacote de Conteúdo UI.

> **AVISO:** Você deve resolver este problema o mais rápido possível, pois pode causar falhas no pipeline a partir da versão de agosto de 2024 do Cloud Manager.

---

### Definição de índice de texto completo personalizado do tipo damAssetLucene deve ser corretamente prefixada com 'damAssetLucene'

- **Key**: CustomFulltextIndexesOfTheDamAssetCheck
- **Type**: Improvement
- **Severity**: Minor
- **Since**: Versão 2024.6.0

O AEM Cloud Service proíbe definições de índice de texto completo personalizadas do tipo `damAssetLucene` de serem prefixadas com qualquer coisa diferente de `damAssetLucene`.

> **AVISO:** Resolva este problema o mais rápido possível, pois pode causar falhas no pipeline a partir da versão de agosto de 2024 do Cloud Manager.

---

### Nós de definição de índice não devem conter propriedades com o mesmo nome

- **Key**: DuplicateNameProperty
- **Type**: Improvement
- **Severity**: Minor
- **Since**: Versão 2024.6.0

O AEM Cloud Service proíbe definições de índice de pesquisa personalizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) de conter propriedades com o mesmo nome.

> **AVISO:** Resolva este problema o mais rápido possível, pois pode causar falhas no pipeline a partir da versão de agosto de 2024 do Cloud Manager.

---

### A personalização de certas definições de índice prontas para uso é proibida

- **Key**: RestrictIndexCustomization
- **Type**: Improvement
- **Severity**: Minor
- **Since**: Versão 2024.6.0

O AEM Cloud Service proíbe modificações não autorizadas dos seguintes índices OOTB:

- `nodetypeLucene`
- `slingResourceResolver`
- `socialLucene`
- `appsLibsLucene`
- `authorizables`
- `pathReference`

> **AVISO:** Resolva este problema o mais rápido possível, pois pode causar falhas no pipeline a partir da versão de agosto de 2024 do Cloud Manager.

---

### A configuração dos tokenizers em analyzers deve ser criada com o nome 'tokenizer'

- **Key**: AnalyzerTokenizerConfigCheck
- **Type**: Improvement
- **Severity**: Minor
- **Since**: Versão 2024.6.0

O AEM Cloud Service proíbe a criação de tokenizers com nomes incorretos em analyzers. Os tokenizers devem sempre ser definidos como `tokenizer`.

> **AVISO:** Resolva este problema o mais rápido possível, pois pode causar falhas no pipeline a partir da versão de agosto de 2024 do Cloud Manager.

---

### A configuração de definições de indexação não deve conter espaços

- **Key**: PathSpacesCheck
- **Type**: Improvement
- **Severity**: Minor
- **Since**: Versão 2024.7.0

O AEM Cloud Service proíbe a criação de definições de indexação que contenham propriedades com espaços.

---

## Resumo

Este documento apresenta as rules personalizadas de qualidade de código do Cloud Manager para Adobe Experience Manager as a Cloud Service. As rules estão divididas em duas categorias principais:

1. **SonarQube Rules**: Focused on Java coding practices, security, performance and maintainability
2. **OakPAL Rules**: Focadas na estrutura de conteúdo, configurações OSGi e definições de índice

Todas as rules são baseadas nas melhores práticas de engenharia da Adobe e devem ser seguidas para garantir a compatibilidade com o AEM as a Cloud Service e manter a qualidade do código em alto nível.

Para mais informações, consulte:
- [Documentação oficial do Cloud Manager](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/using-cloud-manager/test-results/custom-code-quality-rules)
- [Documentação do SonarQube](https://docs.sonarsource.com/sonarqube/latest/)
- [Estrutura de Projeto AEM](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/developing/aem-project-content-package-structure)

---

**Documento gerado a partir da documentação oficial da Adobe Experience Manager**  
**Creation date:** Dezembro de 2025

---

## Regras Adicionais do SonarQube

### Credenciais não devem ser codificadas diretamente no código

- **Key**: squid:S2068
- **Type**: Vulnerability
- **Severity**: Blocker
- **Tags**: cert, cwe, owasp-a2, sans-top25-porous

Credenciais como senhas, tokens de API e chaves secretas nunca devem ser codificadas diretamente no código-fonte. Isso representa um risco de segurança significativo.

---

### "javax.crypto.NullCipher" não deve ser usado para nada além de testes

- **Key**: squid:S2258
- **Type**: Vulnerability
- **Severity**: Blocker
- **Tags**: cwe, owasp-a6, sans-top25-porous

O NullCipher não fornece criptografia real e deve ser usado apenas para testes.

---

### Nem DES nem DESede (3DES) devem ser usados

- **Key**: squid:S2278
- **Type**: Vulnerability
- **Severity**: Blocker
- **Tags**: cert, cwe, owasp-a6, sans-top25-porous

DES e 3DES são algoritmos de criptografia obsoletos e inseguros. Use algoritmos modernos como AES.

---

### Restrições de segurança devem ser definidas

- **Key**: squid:S3369
- **Type**: Vulnerability
- **Severity**: Blocker
- **Tags**: cwe, jee, owasp-a5, websphere

Aplicações JEE devem definir restrições de segurança apropriadas.

---

### Formulários de validação Struts devem ter nomes únicos

- **Key**: squid:S3374
- **Type**: Vulnerability
- **Severity**: Blocker
- **Tags**: cwe, struts

---

### Algoritmos de hash SHA-1 e Message-Digest não devem ser usados

- **Key**: squid:S2070
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a6, sans-top25-porous

SHA-1 é considerado criptograficamente quebrado. Use SHA-256 ou superior.

---

### HTTP referers não devem ser confiáveis

- **Key**: squid:S2089
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a2, sans-top25-porous

O cabeçalho HTTP Referer pode ser facilmente falsificado e não deve ser usado para decisões de segurança.

---

### Geradores de números pseudoaleatórios (PRNGs) não devem ser usados em contextos seguros

- **Key**: squid:S2245
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cert, cwe, owasp-a3

Use SecureRandom em vez de Random para contextos de segurança.

---

### "HttpServletRequest.getRequestedSessionId()" não deve ser usado

- **Key**: squid:S2254
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a2, sans-top25-porous

---

### Apenas algoritmos criptográficos padrão devem ser usados

- **Key**: squid:S2257
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a3, sans-top25-porous

Não implemente seus próprios algoritmos criptográficos. Use implementações padrão e testadas.

---

### Algoritmos criptográficos RSA devem sempre incorporar OAEP

- **Key**: squid:S2277
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a3, owasp-a6, sans-top25-porous

RSA sem OAEP (Optimal Asymmetric Encryption Padding) é vulnerável a ataques.

---

### Aplicações web não devem ter um método "main"

- **Key**: squid:S2653
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cert, cwe, jee

---

### Classes não devem ser carregadas dinamicamente

- **Key**: squid:S2658
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a1

Carregamento dinâmico de classes pode levar a vulnerabilidades de injeção de código.

---

### "File.createTempFile" não deve ser usado para criar um diretório

- **Key**: squid:S2976
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: owasp-a9

---

### Filtros definidos devem ser usados

- **Key**: squid:S3355
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: injection, owasp-a1

---

### Travessia de caminho absoluto em servlet

- **Key**: findbugs:PT_ABSOLUTE_PATH_TRAVERSAL
- **Type**: Vulnerability
- **Severity**: Major
- **Tags**: cwe

---

### Travessia de caminho relativo em servlet

- **Key**: findbugs:PT_RELATIVE_PATH_TRAVERSAL
- **Type**: Vulnerability
- **Severity**: Major
- **Tags**: cwe

---

### Potencial travessia de caminho (leitura de arquivo)

- **Key**: findsecbugs:PATH_TRAVERSAL_IN
- **Type**: Vulnerability
- **Severity**: Major
- **Tags**: cwe, owasp-a4, wasc

---

### Potencial travessia de caminho (escrita de arquivo)

- **Key**: findsecbugs:PATH_TRAVERSAL_OUT
- **Type**: Vulnerability
- **Severity**: Major
- **Tags**: cwe, owasp-a4, wasc

---

### Cookies devem ser "secure"

- **Key**: squid:S2092
- **Type**: Vulnerability
- **Severity**: Minor
- **Tags**: cwe, owasp-a2, owasp-a3

Cookies devem ter o atributo "secure" definido para garantir que sejam transmitidos apenas por HTTPS.

---

### Membros mutáveis não devem ser armazenados ou retornados diretamente

- **Key**: squid:S2384
- **Type**: Vulnerability
- **Severity**: Minor
- **Tags**: cert, cwe, unpredictable

---

### Campos mutáveis não devem ser "public static"

- **Key**: squid:S2386
- **Type**: Vulnerability
- **Severity**: Minor
- **Tags**: cert, cwe, unpredictable

---

### Valores de retorno não devem ser ignorados quando contêm o código de status da operação

- **Key**: squid:S899
- **Type**: Vulnerability
- **Severity**: Minor
- **Tags**: cert, cwe, error-handling, misra

---

### Nome de arquivo contaminado lido

- **Key**: findsecbugs:FILE_UPLOAD_FILENAME
- **Type**: Vulnerability
- **Severity**: Info
- **Tags**: cwe, owasp-a4, wasc

---

## Regras de Bug Adicionais

### Recursos devem ser fechados

- **Key**: squid:S2095
- **Type**: Bug
- **Severity**: Blocker
- **Tags**: cert, cwe, denial-of-service, leak

Recursos como streams, conexões e readers devem sempre ser fechados, preferencialmente usando try-with-resources.

---

### Double-checked locking não deve ser usado

- **Key**: squid:S2168
- **Type**: Bug
- **Severity**: Blocker
- **Tags**: cert, cwe, multi-threading

O padrão double-checked locking é problemático em Java e pode levar a comportamento imprevisível.

---

### "wait(...)" deve ser usado em vez de "Thread.sleep(...)" quando um lock é mantido

- **Key**: squid:S2276
- **Type**: Bug
- **Severity**: Blocker
- **Tags**: cert, multi-threading, performance

---

### Objeto não thread-safe usado como campo de Servlet/Filter

- **Key**: AEM Rules:AEM-3
- **Type**: Bug
- **Severity**: Critical
- **Tags**: aem

Servlets e Filters são singleton por padrão. Campos de instância devem ser thread-safe.

---

### Cast impossível

- **Key**: findbugs:BC_IMPOSSIBLE_CAST
- **Type**: Bug
- **Severity**: Critical
- **Tags**: correctness

---

### Downcast impossível

- **Key**: findbugs:BC_IMPOSSIBLE_DOWNCAST
- **Type**: Bug
- **Severity**: Critical
- **Tags**: correctness

---

### Locks devem ser liberados

- **Key**: squid:S2222
- **Type**: Bug
- **Severity**: Critical
- **Tags**: cwe, multi-threading

---

### Zero não deve ser um denominador possível

- **Key**: squid:S3518
- **Type**: Bug
- **Severity**: Critical
- **Tags**: cert, cwe, denial-of-service

Divisões por zero devem ser evitadas para prevenir exceções em tempo de execução.

---

### Método equals sempre retorna false

- **Key**: findbugs:EQ_ALWAYS_FALSE
- **Type**: Bug
- **Severity**: Major
- **Tags**: correctness

---

### Método equals sempre retorna true

- **Key**: findbugs:EQ_ALWAYS_TRUE
- **Type**: Bug
- **Severity**: Major
- **Tags**: correctness

---

### Loop infinito aparente

- **Key**: findbugs:IL_INFINITE_LOOP
- **Type**: Bug
- **Severity**: Major
- **Tags**: correctness

---

### Sincronização inconsistente

- **Key**: findbugs:IS2_INCONSISTENT_SYNC
- **Type**: Bug
- **Severity**: Major
- **Tags**: multi-threading

---

### Incremento em campo volátil não é atômico

- **Key**: findbugs:VO_VOLATILE_INCREMENT
- **Type**: Bug
- **Severity**: Major
- **Tags**: multi-threading

---

### Instruções de salto não devem ocorrer em blocos "finally"

- **Key**: squid:S1143
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe, error-handling

Return, break ou continue em blocos finally podem mascarar exceções.

---

### Blocos inúteis "if(true) {...}" e "if(false){...}" devem ser removidos

- **Key**: squid:S1145
- **Type**: Bug
- **Severity**: Major
- **Tags**: cwe, misra

---

### Thread.run() não deve ser chamado diretamente

- **Key**: squid:S1217
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe, multi-threading

Use thread.start() em vez de thread.run().

---

### Operadores "instanceof" que sempre retornam "true" ou "false" devem ser removidos

- **Key**: squid:S1850
- **Type**: Bug
- **Severity**: Major

---

### Classes não devem ser comparadas por nome

- **Key**: squid:S1872
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe

Use instanceof ou Class.isAssignableFrom() em vez de comparar nomes de classe.

---

### "InterruptedException" não deve ser ignorada

- **Key**: squid:S2142
- **Type**: Bug
- **Severity**: Major
- **Tags**: cwe, multi-threading

---

### Verificações de igualdade tolas não devem ser feitas

- **Key**: squid:S2159
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, unused

---

### Métodos "toString()" e "clone()" não devem retornar null

- **Key**: squid:S2225
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe

---

### Servlets não devem ter campos de instância mutáveis

- **Key**: squid:S2226
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, multi-threading, struts

---

### Ponteiros nulos não devem ser desreferenciados

- **Key**: squid:S2259
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe

---

### "wait", "notify" e "notifyAll" devem ser chamados apenas quando um lock é obviamente mantido em um objeto

- **Key**: squid:S2273
- **Type**: Bug
- **Severity**: Major
- **Tags**: multi-threading

---

### Objetos não serializáveis não devem ser armazenados em objetos "HttpSession"

- **Key**: squid:S2441
- **Type**: Bug
- **Severity**: Major
- **Tags**: cwe

---

### Blocos devem ser sincronizados em campos "private final"

- **Key**: squid:S2445
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe, multi-threading

---

### Blocos executados condicionalmente devem ser alcançáveis

- **Key**: squid:S2583
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe, misra, pitfall, unused

---

### Campos não thread-safe não devem ser estáticos

- **Key**: squid:S2885
- **Type**: Bug
- **Severity**: Major
- **Tags**: multi-threading

---

### Valor Optional deve ser acessado apenas após chamar isPresent()

- **Key**: squid:S3655
- **Type**: Bug
- **Severity**: Major
- **Tags**: cwe

---

### "equals(Object obj)" e "hashCode()" devem ser sobrescritos em pares

- **Key**: squid:S1206
- **Type**: Bug
- **Severity**: Minor
- **Tags**: cert, cwe

---

### Operandos matemáticos devem ser convertidos antes da atribuição

- **Key**: squid:S2184
- **Type**: Bug
- **Severity**: Minor
- **Tags**: cert, cwe, misra, overflow, sans-top25-risky

---

## Regras de Code Smell Adicionais

### Métodos de saída não devem ser chamados

- **Key**: squid:S1147
- **Type**: Code Smell
- **Severity**: Blocker
- **Tags**: cert, cwe, suspicious

Não use System.exit() ou Runtime.exit() em código de aplicação.

---

### Casos de switch devem terminar com uma instrução "break" incondicional

- **Key**: squid:S128
- **Type**: Code Smell
- **Severity**: Blocker
- **Tags**: cert, cwe, misra, suspicious

---

### Lógica de curto-circuito deve ser usada em contextos booleanos

- **Key**: squid:S2178
- **Type**: Code Smell
- **Severity**: Blocker
- **Tags**: cert

Use && e || em vez de & e | para operações booleanas.

---

### Nenhum método mutador invocado em ModifiableValueMap

- **Key**: AEM Rules:AEM-17
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: aem

---

### ResourceResolver deve ser fechado em bloco finally

- **Key**: AEM Rules:AEM-6
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: aem

---

### Session deve ser desconectada em bloco finally

- **Key**: AEM Rules:AEM-7
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: aem

---

### "Object.finalize()" deve permanecer protected (versus public) ao sobrescrever

- **Key**: squid:S1174
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: cert, cwe

---

### Campos em uma classe "Serializable" devem ser transient ou serializable

- **Key**: squid:S1948
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: cwe, serialization

---

### Operadores de igualdade não devem ser usados em condições de terminação de loop "for"

- **Key**: squid:S888
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: cert, cwe, misra, suspicious

Use < ou > em vez de == ou != em loops for.

---

### Instruções "switch" devem terminar com cláusulas "default"

- **Key**: squid:SwitchLastCaseIsDefaultCheck
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: cert, cwe, misra

---

### Não use métodos de acesso administrativo descontinuados

- **Key**: AEM Rules:AEM-11
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: aem

Evite usar métodos como ResourceResolverFactory.getAdministrativeResourceResolver().

---

### Rastreie usos de tags "FIXME"

- **Key**: squid:S1134
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cwe

---

### Throwable e Error não devem ser capturados

- **Key**: squid:S1181
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: bad-practice, cert, cwe, error-handling

---

### "NullPointerException" não deve ser capturada

- **Key**: squid:S1696
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, error-handling

---

### Dead stores devem ser removidos

- **Key**: squid:S1854
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, unused

Variáveis atribuídas mas nunca lidas devem ser removidas.

---

### "URL.hashCode" e "URL.equals" devem ser evitados

- **Key**: squid:S2112
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: performance

Esses métodos podem fazer chamadas de rede bloqueantes.

---

### Objetos "Lock" não devem ser "synchronized"

- **Key**: squid:S2442
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, clumsy, multi-threading

---

### Blocos multilinha devem ser envolvidos em chaves

- **Key**: squid:S2681
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe

---

### Dependência limitada deve ser colocada na precedência de operadores

- **Key**: squid:S864
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, misra

Use parênteses para deixar a precedência explícita.

---

### Use constante predefinida em anotação em vez de valor hardcoded

- **Key**: AEM Rules:AEM-1
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: aem

---

### Usar literal http hardcoded dificulta a mudança para https posteriormente

- **Key**: AEM Rules:AEM-14
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: aem

---

### Optional é definido como DefaultInjectionStrategy

- **Key**: AEM Rules:AEM-16
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: aem, sling-models

---

### Use constante predefinida em vez de valor hardcoded

- **Key**: AEM Rules:AEM-2
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: aem

---

### Classes que sobrescrevem "clone" devem ser "Cloneable" e chamar "super.clone()"

- **Key**: squid:S1182
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: cert, convention, cwe

---

### "==" e "!=" não devem ser usados quando "equals" é sobrescrito

- **Key**: squid:S1698
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: cert, cwe, suspicious

---

### "Exception" não deve ser capturada quando não é exigida pelos métodos chamados

- **Key**: squid:S2221
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: cwe, error-handling

---

### Uso da palavra-chave 'synchronized' deve ser evitado se possível

- **Key**: AEM Rules:AEM-15
- **Type**: Code Smell
- **Severity**: Info
- **Tags**: multi-threading, performance

---

### Rastreie usos de tags "TODO"

- **Key**: squid:S1135
- **Type**: Code Smell
- **Severity**: Info
- **Tags**: cwe

---

## Índice de Regras por Key

Para facilitar a consulta, aqui está um índice alfabético das principais chaves de rules:

### Regras AEM
- AEM Rules:AEM-1 a AEM-17
- CQRules:AMSCORE-553, AMSCORE-554
- CQRules:CQBP-44, CQBP-71, CQBP-72, CQBP-75, CQBP-84
- CQRules:CWE-134, CWE-676
- CQRules:ConnectionTimeoutMechanism

### OakPAL Rules
- BannedPath
- ClientlibProxyResource
- ClassicUIAuthoringMode
- CloudServiceIncompatibleWorkflowProcess
- ComponentWithOnlyClassicUIDialog
- ConfigAndInstallShouldOnlyContainOsgiNodes
- DuplicateOsgiConfigurations
- ImmutableMutableMixedPackage
- IndexAsyncProperty
- IndexCompatVersion
- IndexDamAssetLucene
- IndexDescendantNodeType
- IndexName
- IndexReindexProperty
- IndexRulesNode
- IndexSeedProperty
- IndexTikaNode
- IndexType
- LegacyFoundationComponentUsage
- OakIndexLocation
- PackageOverlaps
- ReverseReplication
- StaticTemplateUsage
- SupportedRunmode

### Regras SonarQube (squid)
- squid:S00112 a squid:S3655
- squid:ClassVariableVisibilityCheck
- squid:ObjectFinalizeCheck
- squid:SwitchLastCaseIsDefaultCheck

### FindBugs Rules
- findbugs:BC_IMPOSSIBLE_CAST
- findbugs:BC_IMPOSSIBLE_DOWNCAST
- findbugs:EQ_ALWAYS_FALSE
- findbugs:EQ_ALWAYS_TRUE
- findbugs:IL_INFINITE_LOOP
- findbugs:IS2_INCONSISTENT_SYNC
- findbugs:PT_ABSOLUTE_PATH_TRAVERSAL
- findbugs:PT_RELATIVE_PATH_TRAVERSAL
- findbugs:VO_VOLATILE_INCREMENT

### FindSecBugs Rules
- findsecbugs:FILE_UPLOAD_FILENAME
- findsecbugs:PATH_TRAVERSAL_IN
- findsecbugs:PATH_TRAVERSAL_OUT

---

**Last update do documento:** Dezembro de 2025  
**Total documented rules:** 100+

---

## Novas Regras (Versão 2024.12.0)

### Configuração de Proxy do HttpClient

- **Key**: CQRules:AEMSRE-889
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cqsoftwarequality
- **Since**: Versão 2024.12.0

A configuração de proxy do HttpClient deve ser feita corretamente para garantir que as requisições HTTP sejam roteadas adequadamente através de proxies corporativos quando necessário.

---

### Verificação de interrupção de thread

- **Key**: CQRules:GRANITE-54181
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: cqsecurity
- **Since**: Versão 2024.12.0

Threads devem verificar regularmente se foram interrompidas e responder adequadamente. Ignorar interrupções de thread pode levar a problemas de desligamento e vazamento de recursos.

#### Exemplo

```java
public void doWork() {
  while (!Thread.currentThread().isInterrupted()) {
    // fazer trabalho
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Restaurar o status de interrupção
      break;
    }
  }
}
```

---

## Security Hotspots (Pontos Sensíveis de Segurança)

A partir da versão 2024.12.0, algumas rules foram reclassificadas como "Security Hotspots" em vez de "Vulnerabilities". Security Hotspots são áreas do código que requerem revisão manual para determinar se representam um risco de segurança real.

### Senhas codificadas são sensíveis à segurança

- **Key**: java:S2068 (anteriormente squid:S2068)
- **Type**: Security Hotspot
- **Severity**: Blocker
- **Tags**: cert, cwe, owasp-a2, sans-top25-porous

Credenciais hardcoded devem ser revisadas para garantir que não representam um risco de segurança.

---

### Uso de geradores de números pseudoaleatórios (PRNGs) é sensível à segurança

- **Key**: java:S2245 (anteriormente squid:S2245)
- **Type**: Security Hotspot
- **Severity**: Critical
- **Tags**: cert, cwe, owasp-a3

O uso de PRNGs em contextos de segurança deve ser revisado. Use SecureRandom para operações criptográficas.

---

### Uso de algoritmos criptográficos não padrão é sensível à segurança

- **Key**: java:S2257 (anteriormente squid:S2257)
- **Type**: Security Hotspot
- **Severity**: Critical
- **Tags**: cwe, owasp-a3, sans-top25-porous

Algoritmos criptográficos personalizados devem ser revisados por especialistas em segurança.

---

### Uso de algoritmos de hash fracos é sensível à segurança

- **Key**: java:S4790 (anteriormente squid:S2070)
- **Type**: Security Hotspot
- **Severity**: Critical
- **Tags**: cwe, owasp-a3, owasp-a6, owasp-m5, sans-top25-porous, spring

SHA-1 e MD5 são considerados fracos. Revise o uso e considere migrar para SHA-256 ou superior.

---

### Mecanismos de binding SQL devem ser usados

- **Key**: java:S2077 (anteriormente squid:S2077)
- **Type**: Security Hotspot
- **Severity**: Major
- **Tags**: cert, cwe, hibernate, owasp-a1, sans-top25-insecure, sql

Consultas SQL devem usar prepared statements para prevenir injeção SQL.

---

### Criar cookies sem a flag "secure" é sensível à segurança

- **Key**: java:S2092 (anteriormente squid:S2092)
- **Type**: Security Hotspot
- **Severity**: Minor
- **Tags**: cwe, owasp-a3, privacy, sans-top25-porous, spring

Cookies devem ter a flag "secure" definida quando transmitidos por HTTPS.

---

## Regras Atualizadas (Mudanças de Key)

### Algoritmos de criptografia devem ser usados com modo e esquema de padding seguros

- **Key**: java:S5542 (anteriormente squid:S2277)
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous

RSA deve usar OAEP. AES deve usar modos seguros como GCM ou CBC com padding adequado.

---

### Algoritmos de cifra devem ser robustos

- **Key**: java:S5547 (substitui squid:S2258 e squid:S2278)
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous

Não use DES, 3DES ou NullCipher. Use AES-256 ou superior.

---

### Métodos inseguros de criação de arquivo temporário não devem ser usados

- **Key**: java:S5445 (anteriormente squid:S2976)
- **Type**: Vulnerability
- **Severity**: Critical
- **Tags**: cwe, owasp-a9

Use Files.createTempFile() em vez de File.createTempFile() seguido de delete() e mkdir().

---

### Exceções não devem ser lançadas de métodos servlet

- **Key**: java:S1989 (anteriormente squid:S1989)
- **Type**: Vulnerability
- **Severity**: Minor
- **Tags**: cert, cwe, error-handling, owasp-a3

---

### Campos de variável de classe não devem ter acessibilidade pública

- **Key**: java:S1104 (anteriormente squid:ClassVariableVisibilityCheck)
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: cwe

---

### Campos "public static" devem ser constantes

- **Key**: java:S1444 (anteriormente squid:S1444)
- **Type**: Code Smell (anteriormente Vulnerability)
- **Severity**: Minor
- **Tags**: cert, cwe

---

### Expressões booleanas não devem ser gratuitas

- **Key**: java:S2589 (anteriormente squid:S1850)
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, redundant, suspicious

Condições que sempre avaliam para true ou false devem ser removidas.

---

### Instruções "switch" devem ter cláusulas "default"

- **Key**: java:S131 (anteriormente squid:SwitchLastCaseIsDefaultCheck)
- **Type**: Code Smell
- **Severity**: Critical
- **Tags**: cert, cwe

---

### Atribuições não devem ser feitas de dentro de subexpressões

- **Key**: java:S1121 (anteriormente squid:AssignmentInSubExpressionCheck)
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, suspicious

---

### Exceções genéricas nunca devem ser lançadas

- **Key**: java:S112 (anteriormente squid:S00112)
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, error-handling

---

### Atribuições não utilizadas devem ser removidas

- **Key**: java:S1854 (anteriormente squid:S1854)
- **Type**: Code Smell
- **Severity**: Major
- **Tags**: cert, cwe, unused

---

### Código "@Deprecated" não deve ser usado

- **Key**: java:S1874 (anteriormente squid:CallToDeprecatedMethod)
- **Type**: Code Smell
- **Severity**: Minor
- **Tags**: cert, cwe, obsolete

---

### "super.finalize()" deve ser chamado no final de implementações de "Object.finalize()"

- **Key**: java:S1114 (anteriormente squid:ObjectFinalizeOverridenCallsSuperFinalizeCheck)
- **Type**: Bug
- **Severity**: Critical
- **Tags**: cert, cwe

---

### O método Object.finalize() não deve ser chamado

- **Key**: java:S1111 (anteriormente squid:ObjectFinalizeCheck)
- **Type**: Bug
- **Severity**: Major
- **Tags**: cert, cwe

---

## Changelog da Versão 2024.12.0

### Regras Adicionadas
- **CQRules:AEMSRE-889**: HttpClient Proxy Configuration
- **CQRules:GRANITE-54181**: Thread interruption check

### Regras Removidas
As seguintes rules foram removidas da versão 2024.12.0:
- squid:S3369 (Security constraints should be defined)
- squid:S3374 (Struts validation forms should have unique names)
- squid:S2089 (HTTP referers should not be relied on)
- squid:S2653 (Web applications should not have a "main" method)
- squid:S3355 (Defined filters should be used)
- CQRules:CQBP-84 (Product interfaces annotated with @ProviderType)
- squid:S2142 ("InterruptedException" should not be ignored)
- AEM Rules:AEM-17 (No mutator methods invoked on ModifiableValueMap)
- AEM Rules:AEM-14 (Using http literal hardcoded)

### Mudanças de Type
**6 Vulnerabilities movidas para Security Hotspots:**
- squid:S2068 → java:S2068
- squid:S2070 → java:S4790
- squid:S2077 → java:S2077
- squid:S2245 → java:S2245
- squid:S2257 → java:S2257
- squid:S2092 → java:S2092

**3 Vulnerabilities e 1 Bug movidos para Code Smells:**
- squid:S1444 → java:S1444
- squid:S2384 → java:S2384
- squid:S2386 → java:S2386
- squid:S1850 → java:S2589

### Regras Mescladas
**2 Vulnerabilities mescladas:**
- squid:S2258 e squid:S2278 → java:S5547

**2 Bugs mesclados:**
- squid:S1145 e squid:S2583 → java:S2583

### Mudanças de Repositório
Todas as rules "squid:" foram movidas para o repositório "java:" com novas chaves. Consulte a tabela de mapeamento acima para detalhes completos.

---

## Notas Importantes sobre a Versão 2024.12.0

1. **Security Hotspots**: Algumas rules agora são classificadas como "Security Hotspots" em vez de "Vulnerabilities". Isso significa que requerem revisão manual para determinar se representam um risco real.

2. **Mudanças de Key**: A maioria das rules "squid:" foi renomeada para "java:". Certifique-se de atualizar suas configurações de qualidade de código.

3. **Regras Removidas**: Algumas rules foram removidas. Revise seu código para garantir que não depende dessas rules específicas.

4. **Novas Regras**: Duas novas rules foram adicionadas focando em configuração de proxy e verificação de interrupção de thread.

5. **Compatibilidade**: Esta atualização entra em vigor a partir de 13 de fevereiro de 2025 (Cloud Manager 2025.2.0).

---

## Referências e Fontes

Este documento foi compilado a partir das seguintes fontes oficiais:

### Documentação Adobe Experience Manager

1. **Custom Code Quality Rules - AEM Cloud Service**
   - URL: https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/using-cloud-manager/test-results/custom-code-quality-rules
   - Descrição: Documentação oficial das rules personalizadas de qualidade de código do Cloud Manager
   - Acesso: Dezembro 2025

2. **Code Quality Testing - Cloud Manager**
   - URL: https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/using-cloud-manager/test-results/code-quality-testing
   - Descrição: Guia sobre testes de qualidade de código no Cloud Manager
   - Acesso: Dezembro 2025

### Arquivos de Regras

3. **CodeQuality Rules - Versão 2024.12.0 (Mais Recente)**
   - URL: https://experienceleague.adobe.com/docs/experience-manager-cloud-service/assets/CodeQuality-rules-latest-CS-2024-12-0.xlsx?lang=en
   - Descrição: Lista completa de rules atualizada para Cloud Manager 2025.2.0
   - Formato: Excel (.xlsx)
   - Versão: 2024.12.0
   - Data de Vigência: 13 de fevereiro de 2025

4. **CodeQuality Rules - Versão Anterior**
   - URL: https://experienceleague.adobe.com/docs/experience-manager-cloud-service/assets/CodeQuality-rules-latest-CS.xlsx?lang=en
   - Descrição: Lista de rules da versão anterior
   - Formato: Excel (.xlsx)

### Documentação SonarQube

5. **SonarQube Documentation**
   - URL: https://docs.sonarsource.com/sonarqube/latest/
   - Descrição: Documentação oficial do SonarQube sobre conceitos e rules de qualidade
   - Acesso: Dezembro 2025

### Documentação Complementar

6. **Apache Oak Documentation**
   - URL: https://jackrabbit.apache.org/oak/docs/query/lucene.html
   - Descrição: Documentação sobre índices Lucene no Oak

7. **Apache Sling Documentation**
   - URL: https://sling.apache.org/documentation/
   - Descrição: Documentação sobre Sling Servlets, Models e Event Handling

8. **AEM Modernization Tools**
   - URL: https://opensource.adobe.com/aem-modernize-tools/
   - Descrição: Ferramentas para modernização de componentes e templates AEM

9. **AEM Project Structure Guidelines**
   - URL: https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/developing/aem-project-content-package-structure
   - Descrição: Diretrizes sobre estrutura de projetos e pacotes de conteúdo

10. **Content Search and Indexing**
    - URL: https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing
    - Descrição: Documentação sobre pesquisa de conteúdo e indexação no AEM

### Metodologia de Compilação

Este documento foi criado através do seguinte processo:

1. **Extração de Conteúdo**: Utilizamos o MCP (Model Context Protocol) da Adobe para acessar a documentação oficial em tempo real
2. **Análise de Arquivos Excel**: Processamos os arquivos Excel oficiais contendo as listas completas de rules
3. **Comparação e Consolidação**: Comparamos múltiplas fontes para garantir completude e precisão
4. **Tradução**: Todo o conteúdo foi traduzido para português brasileiro mantendo a terminologia técnica apropriada
5. **Validação**: Verificamos duplicações e inconsistências para garantir a qualidade do documento final

### Notas sobre Atualização

- **Última Atualização da Documentação Original**: 05 de novembro de 2025
- **Data de Compilação deste Documento**: Dezembro 2025
- **Versão das Regras**: 2024.12.0 (vigente a partir de 13 de fevereiro de 2025)
- **Versão do SonarQube**: 9.9

### Aviso Legal

Este documento é uma compilação não oficial baseada em fontes públicas da Adobe. Para informações oficiais e atualizadas, sempre consulte a documentação oficial da Adobe Experience Manager em:
- https://experienceleague.adobe.com/

### Contribuições e Feedback

Este documento foi criado para fins educacionais e de referência. Para reportar erros ou sugerir melhorias, consulte a documentação oficial da Adobe ou entre em contato com o suporte da Adobe.

---

**Document compiled and translated by:** Kiro AI Assistant  
**Creation date:** Dezembro 2025  
**Document version:** 1.0  
**Total documented rules:** 172  
**Language:** Brazilian Portuguese (PT-BR)

---

*End of document*
