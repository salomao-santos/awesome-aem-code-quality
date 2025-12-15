# Regras Java Backend e SonarQube para AEM Cloud Service

**Última atualização:** 14 de Dezembro de 2025
**Categoria:** Java Backend Development Rules
**Tecnologias:** `.java`, `.class`, `.jar`, OSGi, Sling, JCR
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Java Backend

| Tipo | Quantidade |
|------|------------|
| **Total Regras Java** | 89 |
| **Vulnerabilities** | 13 |
| **Security Hotspots** | 6 |
| **Bugs** | 32 |
| **Code Smells** | 38 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | 8 |
| **Critical** | 15 |
| **Major** | 41 |
| **Minor** | 23 |
| **Info** | 2 |

### Por Framework/Tecnologia

| Framework | Regras |
|-----------|--------|
| **Java Core** | 45 |
| **AEM/Sling** | 18 |
| **OSGi** | 8 |
| **JCR/Repository** | 6 |
| **Security** | 19 |
| **Threading** | 12 |

---

## 🔴 Vulnerabilidades Java (Severity: Critical/Major)
### java:S2254 - HttpServletRequest.getRequestedSessionId() should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2254 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a2, sans-top25-porous |
| **AEM Context** | Servlet Development |
| **Old Key** | squid:S2254 |

**Descrição**: O método `getRequestedSessionId()` não deve ser usado pois pode expor informações sensíveis de sessão.

**Impacto no AEM**: Em servlets AEM, isso pode expor IDs de sessão em logs ou respostas, criando vulnerabilidades de segurança.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/servlet"
    })
public class MyServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        String sessionId = request.getRequestedSessionId(); // VULNERABLE
        response.getWriter().write("Session: " + sessionId);
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/servlet"
    })
public class MyServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Use session attributes instead of exposing session ID
            String userId = (String) session.getAttribute("userId");
            response.getWriter().write("User: " + userId);
        }
    }
}
```

---
### java:S2658 - Classes should not be loaded dynamically

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2658 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a1 |
| **AEM Context** | OSGi Bundle Development |
| **Old Key** | squid:S2658 |

**Descrição**: Carregar classes dinamicamente pode permitir execução de código malicioso.

**Impacto no AEM**: Em ambientes OSGi, o carregamento dinâmico de classes pode comprometer a segurança do container.

#### Non-compliant code
```java
@Component(service = MyService.class)
public class DynamicClassLoader {
    
    public void loadClass(String className) throws Exception {
        // VULNERABLE - Dynamic class loading
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
    }
}
```

#### Compliant code
```java
@Component(service = MyService.class)
public class SafeClassLoader {
    
    private static final Set<String> ALLOWED_CLASSES = Set.of(
        "com.mycompany.safe.Class1",
        "com.mycompany.safe.Class2"
    );
    
    public void loadClass(String className) throws Exception {
        if (!ALLOWED_CLASSES.contains(className)) {
            throw new SecurityException("Class not allowed: " + className);
        }
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
    }
}
```

---
### java:S5445 - Insecure temporary file creation methods should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5445 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a9 |
| **AEM Context** | File Processing |
| **Old Key** | squid:S2976 |

**Descrição**: Métodos inseguros de criação de arquivos temporários não devem ser usados.

**Impacto no AEM**: Em processamento de assets ou workflows, arquivos temporários inseguros podem ser explorados.

#### Non-compliant code
```java
@Component(service = FileProcessor.class)
public class FileProcessor {
    
    public void processFile(InputStream input) throws IOException {
        // VULNERABLE - Insecure temp file creation
        File tempFile = File.createTempFile("temp", ".tmp");
        // Process file...
    }
}
```

#### Compliant code
```java
@Component(service = FileProcessor.class)
public class FileProcessor {
    
    public void processFile(InputStream input) throws IOException {
        // SECURE - Proper temp file creation with restricted permissions
        Path tempDir = Files.createTempDirectory("secure-temp");
        Path tempFile = Files.createTempFile(tempDir, "temp", ".tmp");
        
        // Set restrictive permissions
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        Files.setPosixFilePermissions(tempFile, perms);
        
        try {
            // Process file...
        } finally {
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(tempDir);
        }
    }
}
```

---
### java:S5542 - Encryption algorithms should be used with secure mode and padding

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5542 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **AEM Context** | Security, Data Protection |
| **Old Key** | squid:S2277 |

**Descrição**: Algoritmos de criptografia devem usar modo seguro e padding adequado.

**Impacto no AEM**: Dados sensíveis em repositório JCR ou configurações OSGi podem ser comprometidos.

#### Non-compliant code
```java
@Component(service = EncryptionService.class)
public class EncryptionService {
    
    public byte[] encrypt(String data) throws Exception {
        // VULNERABLE - Insecure cipher configuration
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec("mykey".getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes());
    }
}
```

#### Compliant code
```java
@Component(service = EncryptionService.class)
public class EncryptionService {
    
    public byte[] encrypt(String data) throws Exception {
        // SECURE - Proper cipher configuration
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        
        // Generate secure key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();
        
        // Generate IV
        byte[] iv = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
```

---
### CQRules:CWE-134 - Don't use format strings that may be externally-controlled

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CWE-134 |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cqsecurity |
| **AEM Context** | Servlet Development |
| **Since** | Version 2018.4.0 |

**Descrição**: Usar uma string de formato de uma fonte externa pode expor a aplicação a ataques de negação de serviço.

**Impacto no AEM**: Parâmetros de requisição ou conteúdo gerado pelo usuário podem ser explorados em servlets AEM.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/formatter"
    })
public class FormatterServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        String messageFormat = request.getParameter("messageFormat");
        // VULNERABLE - External format string
        String result = String.format(messageFormat, "some text");
        request.getResource().getValueMap().put("formatted", result);
        response.sendStatus(HttpServletResponse.SC_OK);
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/formatter"
    })
public class FormatterServlet extends SlingAllMethodsServlet {
    
    private static final Map<String, String> ALLOWED_FORMATS = Map.of(
        "simple", "Message: %s",
        "detailed", "Detailed message: %s at %s"
    );
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        String formatType = request.getParameter("formatType");
        String messageFormat = ALLOWED_FORMATS.get(formatType);
        
        if (messageFormat != null) {
            String result = String.format(messageFormat, "some text", new Date());
            request.getResource().getValueMap().put("formatted", result);
        }
        response.sendStatus(HttpServletResponse.SC_OK);
    }
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
| **AEM Context** | Threading, OSGi Services |
| **Since** | Version 2018.4.0 |

**Descrição**: Os métodos `Thread.stop()` e `Thread.interrupt()` podem produzir problemas difíceis de reproduzir e vulnerabilidades de segurança.

**Impacto no AEM**: Em serviços OSGi e workflows, uso inadequado de threading pode causar instabilidade.

#### Non-compliant code
```java
@Component(service = BackgroundProcessor.class)
public class BackgroundProcessor implements Runnable {
    private Thread thread;

    @Activate
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Deactivate
    public void stop() {
        thread.stop();  // UNSAFE!
    }

    public void run() {
        while (true) {
            processWorkflowItems();
        }
    }
}
```

#### Compliant code
```java
@Component(service = BackgroundProcessor.class)
public class BackgroundProcessor implements Runnable {
    private Thread thread;
    private volatile boolean keepRunning = true;

    @Activate
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Deactivate
    public void stop() {
        keepRunning = false;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join(5000); // Wait up to 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void run() {
        while (keepRunning && !Thread.currentThread().isInterrupted()) {
            try {
                processWorkflowItems();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
```

---
## 🟠 Security Hotspots Java

### java:S2068 - Hard-coded passwords are security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2068 |
| **Type** | Security Hotspot |
| **Severity** | Blocker |
| **Tags** | cert, cwe, owasp-a2, sans-top25-porous |
| **AEM Context** | OSGi Configuration |
| **Old Key** | squid:S2068 |

**Descrição**: Senhas hard-coded são sensíveis à segurança e devem ser evitadas.

**Impacto no AEM**: Configurações OSGi com senhas hardcoded podem comprometer a segurança do sistema.

#### Non-compliant code
```java
@Component(service = DatabaseService.class)
@Designate(ocd = DatabaseService.Config.class)
public class DatabaseService {
    
    @ObjectClassDefinition(name = "Database Service Configuration")
    public @interface Config {
        String username() default "admin";
        String password() default "admin123"; // VULNERABLE - Hard-coded password
    }
    
    @Activate
    protected void activate(Config config) {
        String password = config.password(); // Hard-coded password used
        // Connect to database...
    }
}
```

#### Compliant code
```java
@Component(service = DatabaseService.class)
@Designate(ocd = DatabaseService.Config.class)
public class DatabaseService {
    
    @ObjectClassDefinition(name = "Database Service Configuration")
    public @interface Config {
        String username();
        @AttributeDefinition(type = AttributeType.PASSWORD)
        String password(); // Password field without default
    }
    
    @Activate
    protected void activate(Config config) {
        String password = config.password(); // Password from configuration
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must be configured");
        }
        // Connect to database...
    }
}
```

---
### java:S2245 - Using pseudorandom number generators (PRNGs) is security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2245 |
| **Type** | Security Hotspot |
| **Severity** | Critical |
| **Tags** | cert, cwe, owasp-a3 |
| **AEM Context** | Security, Token Generation |
| **Old Key** | squid:S2245 |

**Descrição**: Usar geradores de números pseudoaleatórios (PRNGs) é sensível à segurança em contextos criptográficos.

**Impacto no AEM**: Geração de tokens de segurança ou chaves de sessão pode ser comprometida.

#### Non-compliant code
```java
@Component(service = TokenService.class)
public class TokenService {
    
    private final Random random = new Random(); // VULNERABLE - Weak PRNG
    
    public String generateToken() {
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes); // Predictable randomness
        return Base64.getEncoder().encodeToString(tokenBytes);
    }
}
```

#### Compliant code
```java
@Component(service = TokenService.class)
public class TokenService {
    
    private final SecureRandom secureRandom;
    
    public TokenService() throws NoSuchAlgorithmException {
        this.secureRandom = SecureRandom.getInstanceStrong(); // Cryptographically secure
    }
    
    public String generateToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes); // Cryptographically secure randomness
        return Base64.getEncoder().encodeToString(tokenBytes);
    }
}
```

---
## 🔵 Bugs Java - Gerenciamento de Recursos

### java:S2095 - Resources should be closed

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2095 |
| **Type** | Bug |
| **Severity** | Blocker |
| **Tags** | cert, cwe, denial-of-service, leak |
| **AEM Context** | ResourceResolver, Session, InputStream |
| **Old Key** | squid:S2095 |

**Descrição**: Recursos devem ser fechados para evitar vazamentos de memória.

**Impacto no AEM**: ResourceResolver, Session JCR e streams não fechados podem esgotar recursos do sistema.

#### Non-compliant code
```java
@Component(service = ContentService.class)
public class ContentService {
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    public void processContent() throws LoginException {
        // RESOURCE LEAK - ResourceResolver not closed
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null);
        Resource resource = resolver.getResource("/content/mysite");
        // Process resource...
        // Missing resolver.close()
    }
    
    public void readFile(String path) throws IOException {
        // RESOURCE LEAK - InputStream not closed
        InputStream input = new FileInputStream(path);
        byte[] data = input.readAllBytes();
        // Process data...
        // Missing input.close()
    }
}
```

#### Compliant code
```java
@Component(service = ContentService.class)
public class ContentService {
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    public void processContent() throws LoginException {
        // PROPER - Using try-with-resources
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null)) {
            Resource resource = resolver.getResource("/content/mysite");
            // Process resource...
        } // ResourceResolver automatically closed
    }
    
    public void readFile(String path) throws IOException {
        // PROPER - Using try-with-resources
        try (InputStream input = new FileInputStream(path)) {
            byte[] data = input.readAllBytes();
            // Process data...
        } // InputStream automatically closed
    }
}
```

---
### AEM Rules:AEM-6 - ResourceResolver should be closed in finally block

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-6 |
| **Type** | Code Smell |
| **Severity** | Critical |
| **Tags** | aem |
| **AEM Context** | Resource Management |

**Descrição**: ResourceResolver deve ser fechado em bloco finally para garantir liberação de recursos.

**Impacto no AEM**: Vazamentos de ResourceResolver podem causar esgotamento de recursos e degradação de performance.

#### Padrões recomendados para AEM:

```java
@Component(service = MyService.class)
public class MyService {
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    // Padrão 1: Try-with-resources (Recomendado)
    public void method1() throws LoginException {
        Map<String, Object> authInfo = Collections.singletonMap(
            ResourceResolverFactory.SUBSERVICE, "myservice");
            
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfo)) {
            // Use resolver
            Resource resource = resolver.getResource("/content/mysite");
            // Process resource...
        } catch (LoginException e) {
            log.error("Login failed", e);
            throw e;
        }
    }
    
    // Padrão 2: Finally block (Alternativo)
    public void method2() throws LoginException {
        ResourceResolver resolver = null;
        try {
            Map<String, Object> authInfo = Collections.singletonMap(
                ResourceResolverFactory.SUBSERVICE, "myservice");
            resolver = resolverFactory.getServiceResourceResolver(authInfo);
            
            // Use resolver
            Resource resource = resolver.getResource("/content/mysite");
            // Process resource...
        } catch (LoginException e) {
            log.error("Login failed", e);
            throw e;
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }
}
```

---
### CQRules:ConnectionTimeoutMechanism - HTTP requests should always have socket and connect timeouts

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:ConnectionTimeoutMechanism |
| **Type** | Bug |
| **Severity** | Critical |
| **AEM Context** | HTTP Client, External Integrations |
| **Since** | Version 2018.6.0 |

**Descrição**: Ao executar requisições HTTP de dentro de uma aplicação AEM, é crítico que timeouts adequados sejam configurados para evitar consumo desnecessário de threads.

**Impacto no AEM**: Requisições HTTP sem timeout podem bloquear threads indefinidamente, causando degradação de performance.

#### Non-compliant code
```java
@Component(service = ExternalService.class)
public class ExternalService {
    
    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    public void callExternalAPI() throws IOException {
        // PROBLEMA - Sem timeouts configurados
        HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
        HttpClient httpClient = builder.build();
        
        HttpGet request = new HttpGet("https://api.external.com/data");
        HttpResponse response = httpClient.execute(request);
        // Process response...
    }

    public void callWithURLConnection() throws IOException {
        // PROBLEMA - Sem timeouts configurados
        URL url = new URL("https://api.external.com/data");
        URLConnection urlConnection = url.openConnection();
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
            urlConnection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                log.info(inputLine);
            }
        }
    }
}
```

#### Compliant code
```java
@Component(service = ExternalService.class)
public class ExternalService {
    
    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    public void callExternalAPI() throws IOException {
        // CORRETO - Com timeouts configurados
        HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)      // 5 seconds connect timeout
            .setSocketTimeout(30000)      // 30 seconds socket timeout
            .setConnectionRequestTimeout(5000) // 5 seconds connection request timeout
            .build();
        builder.setDefaultRequestConfig(requestConfig);
        
        HttpClient httpClient = builder.build();
        HttpGet request = new HttpGet("https://api.external.com/data");
        
        try {
            HttpResponse response = httpClient.execute(request);
            // Process response...
        } catch (SocketTimeoutException e) {
            log.warn("Request timed out", e);
            throw e;
        }
    }

    public void callWithURLConnection() throws IOException {
        // CORRETO - Com timeouts configurados
        URL url = new URL("https://api.external.com/data");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(5000);  // 5 seconds
        urlConnection.setReadTimeout(30000);    // 30 seconds
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
            urlConnection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                log.info(inputLine);
            }
        } catch (SocketTimeoutException e) {
            log.warn("Request timed out", e);
            throw e;
        }
    }
}
```

---
## 🧵 Regras de Threading e Concorrência

### java:S2168 - Double-checked locking should not be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2168 |
| **Type** | Bug |
| **Severity** | Blocker |
| **Tags** | cert, cwe, multi-threading |
| **AEM Context** | OSGi Services, Singletons |
| **Old Key** | squid:S2168 |

**Descrição**: Double-checked locking não deve ser usado pois pode causar problemas de concorrência.

**Impacto no AEM**: Em serviços OSGi, padrões de inicialização lazy podem falhar em ambientes multi-thread.

#### Non-compliant code
```java
@Component(service = CacheService.class)
public class CacheService {
    
    private volatile Map<String, Object> cache;
    
    public Map<String, Object> getCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) { // PROBLEMA - Double-checked locking
                    cache = new ConcurrentHashMap<>();
                }
            }
        }
        return cache;
    }
}
```

#### Compliant code
```java
@Component(service = CacheService.class)
public class CacheService {
    
    // Opção 1: Inicialização no activate
    private Map<String, Object> cache;
    
    @Activate
    protected void activate() {
        this.cache = new ConcurrentHashMap<>();
    }
    
    public Map<String, Object> getCache() {
        return cache;
    }
    
    // Opção 2: Lazy initialization holder pattern
    private static class CacheHolder {
        private static final Map<String, Object> INSTANCE = new ConcurrentHashMap<>();
    }
    
    public Map<String, Object> getCacheLazy() {
        return CacheHolder.INSTANCE;
    }
}
```

---
### AEM Rules:AEM-3 - Non-thread safe object used as a field of Servlet/Filter

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-3 |
| **Type** | Bug |
| **Severity** | Critical |
| **Tags** | aem |
| **AEM Context** | Servlet Development |

**Descrição**: Objetos não thread-safe não devem ser usados como campos de Servlet/Filter.

**Impacto no AEM**: Servlets são singleton por padrão, campos não thread-safe podem causar corrupção de dados.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/data"
    })
public class DataServlet extends SlingSafeMethodsServlet {
    
    // PROBLEMA - SimpleDateFormat não é thread-safe
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // PROBLEMA - StringBuilder não é thread-safe
    private final StringBuilder buffer = new StringBuilder();
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        String formattedDate = dateFormat.format(new Date()); // Race condition
        buffer.append("Request at: ").append(formattedDate); // Race condition
        
        response.getWriter().write(buffer.toString());
        buffer.setLength(0); // Race condition
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/data"
    })
public class DataServlet extends SlingSafeMethodsServlet {
    
    // CORRETO - DateTimeFormatter é thread-safe
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // CORRETO - Variáveis locais são thread-safe
        String formattedDate = DATE_FORMATTER.format(LocalDate.now());
        StringBuilder buffer = new StringBuilder();
        
        buffer.append("Request at: ").append(formattedDate);
        response.getWriter().write(buffer.toString());
    }
}
```

### Alternativa com ThreadLocal (para casos específicos):

```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/legacy"
    })
public class LegacyServlet extends SlingSafeMethodsServlet {
    
    // ALTERNATIVA - ThreadLocal para objetos não thread-safe
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        String formattedDate = DATE_FORMAT.get().format(new Date());
        response.getWriter().write("Request at: " + formattedDate);
    }
}
```

---
## 🟡 Code Smells Java - Boas Práticas AEM

### CQRules:CQBP-72 - close() method is not called on ResourceResolver object

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-72 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cqsoftwarequality |
| **AEM Context** | Resource Management |
| **Since** | Version 2018.4.0 |

**Descrição**: Objetos `ResourceResolver` obtidos do `ResourceResolverFactory` consomem recursos do sistema. É mais eficiente fechar explicitamente qualquer objeto `ResourceResolver` aberto chamando o método `close()`.

**Impacto no AEM**: ResourceResolvers não fechados podem causar vazamentos de recursos e degradação de performance.

#### Padrões Anti-Pattern Comuns:

```java
@Component(service = ContentProcessor.class)
public class ContentProcessor {
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    // ANTI-PATTERN 1 - Não fechar ResourceResolver
    public void processContent1() throws LoginException {
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null);
        Resource resource = resolver.getResource("/content/mysite");
        // Process resource...
        // PROBLEMA: ResourceResolver não é fechado
    }
    
    // ANTI-PATTERN 2 - Fechar apenas em caso de sucesso
    public void processContent2() throws LoginException {
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null);
        try {
            Resource resource = resolver.getResource("/content/mysite");
            if (resource != null) {
                // Process resource...
                resolver.close(); // PROBLEMA: Só fecha em caso de sucesso
            }
        } catch (Exception e) {
            // PROBLEMA: ResourceResolver não é fechado em caso de exceção
            throw e;
        }
    }
}
```

#### Padrões Recomendados:

```java
@Component(service = ContentProcessor.class)
public class ContentProcessor {
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    // PADRÃO 1: Try-with-resources (Mais Recomendado)
    public void processContent1() throws LoginException {
        Map<String, Object> authInfo = Collections.singletonMap(
            ResourceResolverFactory.SUBSERVICE, "content-processor");
            
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfo)) {
            Resource resource = resolver.getResource("/content/mysite");
            if (resource != null) {
                processResource(resource);
            }
        } // ResourceResolver automaticamente fechado
    }
    
    // PADRÃO 2: Finally block (Alternativo)
    public void processContent2() throws LoginException {
        ResourceResolver resolver = null;
        try {
            Map<String, Object> authInfo = Collections.singletonMap(
                ResourceResolverFactory.SUBSERVICE, "content-processor");
            resolver = resolverFactory.getServiceResourceResolver(authInfo);
            
            Resource resource = resolver.getResource("/content/mysite");
            if (resource != null) {
                processResource(resource);
            }
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }
    
    // PADRÃO 3: Método utilitário para reutilização
    public void processContent3() throws LoginException {
        executeWithResolver("content-processor", resolver -> {
            Resource resource = resolver.getResource("/content/mysite");
            if (resource != null) {
                processResource(resource);
            }
        });
    }
    
    private void executeWithResolver(String subservice, 
                                   Consumer<ResourceResolver> operation) throws LoginException {
        Map<String, Object> authInfo = Collections.singletonMap(
            ResourceResolverFactory.SUBSERVICE, subservice);
            
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfo)) {
            operation.accept(resolver);
        }
    }
    
    private void processResource(Resource resource) {
        // Process resource logic...
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
| **AEM Context** | Servlet Registration |
| **Since** | Version 2018.4.0 |

**Descrição**: Vincular servlets por caminhos é desencorajado. Servlets vinculados por caminho não podem usar controles de acesso JCR padrão e requerem rigor de segurança adicional.

**Impacto no AEM**: Servlets registrados por path bypass o sistema de segurança do Sling e podem expor endpoints inseguros.

#### Non-compliant code
```java
// PROBLEMA - Registro por path
@Component(service = Servlet.class,
    property = {
        "sling.servlet.paths=/apps/myco/endpoint",  // INSEGURO
        "sling.servlet.methods=GET"
    })
public class PathBoundServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // Este servlet é acessível diretamente via path
        // Não há controle de acesso JCR automático
        response.getWriter().write("Data: " + getSensitiveData());
    }
    
    private String getSensitiveData() {
        return "sensitive information";
    }
}
```

#### Compliant code
```java
// CORRETO - Registro por resource type
@Component(service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=myco/components/api",  // SEGURO
        "sling.servlet.methods=GET",
        "sling.servlet.selectors=data"
    })
public class ResourceTypeBoundServlet extends SlingSafeMethodsServlet {
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // Este servlet herda as permissões JCR do resource
        Resource resource = request.getResource();
        
        // Verificação adicional de segurança
        if (!hasPermission(resource, request.getResourceResolver())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        response.getWriter().write("Data: " + getSensitiveData());
    }
    
    private boolean hasPermission(Resource resource, ResourceResolver resolver) {
        // Implementar lógica de verificação de permissão
        return resolver.hasChildren(resource); // Exemplo simplificado
    }
    
    private String getSensitiveData() {
        return "sensitive information";
    }
}
```

#### Estrutura de Conteúdo Recomendada:

```
/content/mysite/api
  - sling:resourceType = "myco/components/api"
  - jcr:primaryType = "nt:unstructured"
  
/apps/myco/components/api
  - jcr:primaryType = "nt:folder"
  
// Servlet será acessível via:
// GET /content/mysite/api.data.json
// Com controle de acesso JCR aplicado automaticamente
```

#### Configuração de Segurança Adicional:

```java
// Para casos onde path-bound servlet é necessário
@Component(service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/myco/secure-endpoint",
        "sling.servlet.methods=POST"
    })
public class SecurePathBoundServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // OBRIGATÓRIO - Implementar verificação de segurança manual
        if (!isAuthorized(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // OBRIGATÓRIO - Validar CSRF token
        if (!isValidCSRFToken(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        // Process request...
    }
    
    private boolean isAuthorized(SlingHttpServletRequest request) {
        // Implementar verificação de autorização
        ResourceResolver resolver = request.getResourceResolver();
        return !"anonymous".equals(resolver.getUserID());
    }
    
    private boolean isValidCSRFToken(SlingHttpServletRequest request) {
        // Implementar verificação de CSRF token
        String token = request.getParameter("csrf-token");
        return token != null && validateToken(token);
    }
    
    private boolean validateToken(String token) {
        // Implementar validação de token
        return true; // Placeholder
    }
}
```

---
### java:S1147 - Exit methods should not be called

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1147 |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | cert, cwe, suspicious |
| **AEM Context** | OSGi Services, Application Lifecycle |
| **Old Key** | squid:S1147 |

**Descrição**: Métodos de saída como `System.exit()` não devem ser chamados.

**Impacto no AEM**: Chamar `System.exit()` pode derrubar toda a instância AEM, afetando outros serviços.

#### Non-compliant code
```java
@Component(service = DataProcessor.class)
public class DataProcessor {
    
    public void processData(String data) {
        try {
            if (data == null || data.isEmpty()) {
                log.error("Invalid data provided");
                System.exit(1); // PROBLEMA - Derruba toda a JVM
            }
            
            // Process data...
            if (processingFailed()) {
                System.exit(-1); // PROBLEMA - Derruba toda a JVM
            }
        } catch (Exception e) {
            log.error("Processing failed", e);
            System.exit(1); // PROBLEMA - Derruba toda a JVM
        }
    }
}
```

#### Compliant code
```java
@Component(service = DataProcessor.class)
public class DataProcessor {
    
    public void processData(String data) throws DataProcessingException {
        try {
            if (data == null || data.isEmpty()) {
                log.error("Invalid data provided");
                throw new IllegalArgumentException("Data cannot be null or empty");
            }
            
            // Process data...
            if (processingFailed()) {
                throw new DataProcessingException("Processing failed");
            }
        } catch (Exception e) {
            log.error("Processing failed", e);
            throw new DataProcessingException("Failed to process data", e);
        }
    }
    
    // Para casos onde o serviço precisa ser desabilitado
    @Reference
    private ComponentContext componentContext;
    
    public void handleCriticalError() {
        try {
            // Attempt recovery...
        } catch (Exception e) {
            log.error("Critical error, disabling service", e);
            // CORRETO - Desabilita apenas este componente
            componentContext.disableComponent(DataProcessor.class.getName());
        }
    }
}

// Exceção customizada para melhor tratamento de erros
public class DataProcessingException extends Exception {
    public DataProcessingException(String message) {
        super(message);
    }
    
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---
## 🔒 Regras de Logging e Tratamento de Exceções

### CQRules:CQBP-44---CatchAndEitherLogOrThrow - Catch and either log or throw

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44---CatchAndEitherLogOrThrow |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **AEM Context** | Exception Handling |
| **Since** | Version 2018.4.0 |

**Descrição**: Em geral, uma exceção deve ser logada exatamente uma vez. Logar exceções múltiplas vezes causa confusão.

**Impacto no AEM**: Logs duplicados dificultam a análise de problemas e podem mascarar a origem real dos erros.

#### Non-compliant code
```java
@Component(service = WorkflowService.class)
public class WorkflowService {
    
    public void executeWorkflow(String workflowId) throws WorkflowException {
        try {
            processWorkflowStep(workflowId);
        } catch (Exception e) {
            log.error("Workflow execution failed", e); // Log da exceção
            throw e; // PROBLEMA - Re-throw da mesma exceção (será logada novamente)
        }
    }
    
    private void processWorkflowStep(String workflowId) throws Exception {
        // Workflow processing logic...
        throw new RuntimeException("Simulated error");
    }
}
```

#### Compliant code
```java
@Component(service = WorkflowService.class)
public class WorkflowService {
    
    // OPÇÃO 1: Log e handle (não re-throw)
    public void executeWorkflow1(String workflowId) {
        try {
            processWorkflowStep(workflowId);
        } catch (Exception e) {
            log.error("Workflow execution failed for ID: {}", workflowId, e);
            // Handle the error appropriately
            notifyWorkflowFailure(workflowId, e.getMessage());
        }
    }
    
    // OPÇÃO 2: Transform e throw (sem log duplicado)
    public void executeWorkflow2(String workflowId) throws WorkflowException {
        try {
            processWorkflowStep(workflowId);
        } catch (Exception e) {
            // Transform para exceção específica do domínio (sem log aqui)
            throw new WorkflowException("Failed to execute workflow: " + workflowId, e);
        }
    }
    
    // OPÇÃO 3: Log apenas em nível específico da aplicação
    public void executeWorkflow3(String workflowId) throws WorkflowException {
        try {
            processWorkflowStep(workflowId);
        } catch (Exception e) {
            // Log apenas para debug/trace, não error
            log.debug("Workflow step failed, will be retried", e);
            throw new WorkflowException("Workflow execution failed", e);
        }
    }
    
    private void processWorkflowStep(String workflowId) throws Exception {
        // Workflow processing logic...
        if (workflowId == null) {
            throw new IllegalArgumentException("Workflow ID cannot be null");
        }
    }
    
    private void notifyWorkflowFailure(String workflowId, String errorMessage) {
        // Notify administrators or trigger recovery process
        log.info("Workflow failure notification sent for ID: {}", workflowId);
    }
}

// Exceção específica do domínio
public class WorkflowException extends Exception {
    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
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
| **AEM Context** | Logging Framework |
| **Since** | Version 2018.4.0 |

**Descrição**: Contexto é crítico ao entender mensagens de log. Usar `Exception.printStackTrace()` faz com que apenas o stack trace seja enviado para o stream de erro padrão, perdendo todo o contexto.

**Impacto no AEM**: Em aplicações multi-threaded como AEM, stack traces podem se sobrepor e causar confusão significativa.

#### Non-compliant code
```java
@Component(service = AssetProcessor.class)
public class AssetProcessor {
    
    public void processAsset(Resource assetResource) {
        try {
            validateAsset(assetResource);
            transformAsset(assetResource);
        } catch (Exception e) {
            e.printStackTrace(); // PROBLEMA - Perde contexto e pode sobrepor com outros threads
        }
    }
    
    public void batchProcessAssets(List<Resource> assets) {
        for (Resource asset : assets) {
            try {
                processAsset(asset);
            } catch (Exception e) {
                System.err.println("Error processing asset"); // PROBLEMA - Sem contexto
                e.printStackTrace(); // PROBLEMA - Stack trace sem contexto
            }
        }
    }
}
```

#### Compliant code
```java
@Component(service = AssetProcessor.class)
public class AssetProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(AssetProcessor.class);
    
    public void processAsset(Resource assetResource) {
        try {
            validateAsset(assetResource);
            transformAsset(assetResource);
        } catch (Exception e) {
            // CORRETO - Log com contexto através do framework de logging
            log.error("Failed to process asset at path: {}", 
                     assetResource.getPath(), e);
        }
    }
    
    public void batchProcessAssets(List<Resource> assets) {
        log.info("Starting batch processing of {} assets", assets.size());
        
        int processed = 0;
        int failed = 0;
        
        for (Resource asset : assets) {
            try {
                processAsset(asset);
                processed++;
                log.debug("Successfully processed asset: {}", asset.getPath());
            } catch (Exception e) {
                failed++;
                // CORRETO - Log estruturado com contexto completo
                log.error("Failed to process asset {} (batch position: {}). " +
                         "Processed: {}, Failed: {}", 
                         asset.getPath(), processed + failed, processed, failed, e);
            }
        }
        
        log.info("Batch processing completed. Processed: {}, Failed: {}", 
                processed, failed);
    }
    
    // Exemplo de logging estruturado para diferentes níveis
    public void processAssetWithDetailedLogging(Resource assetResource) {
        String assetPath = assetResource.getPath();
        log.info("Starting asset processing for: {}", assetPath);
        
        try {
            log.debug("Validating asset: {}", assetPath);
            validateAsset(assetResource);
            
            log.debug("Transforming asset: {}", assetPath);
            transformAsset(assetResource);
            
            log.info("Successfully processed asset: {}", assetPath);
            
        } catch (ValidationException e) {
            log.warn("Asset validation failed for: {}. Reason: {}", 
                    assetPath, e.getMessage(), e);
        } catch (TransformationException e) {
            log.error("Asset transformation failed for: {}. " +
                     "This may require manual intervention.", assetPath, e);
        } catch (Exception e) {
            log.error("Unexpected error processing asset: {}. " +
                     "Please check system configuration.", assetPath, e);
        }
    }
    
    private void validateAsset(Resource assetResource) throws ValidationException {
        // Validation logic...
    }
    
    private void transformAsset(Resource assetResource) throws TransformationException {
        // Transformation logic...
    }
}

// Exceções específicas para melhor categorização
class ValidationException extends Exception {
    public ValidationException(String message) { super(message); }
    public ValidationException(String message, Throwable cause) { super(message, cause); }
}

class TransformationException extends Exception {
    public TransformationException(String message) { super(message); }
    public TransformationException(String message, Throwable cause) { super(message, cause); }
}
```

---
## 🔄 Migrações de Chaves Java (SonarQube 9.9)

### Impacto da Migração squid:* → java:*

A partir de **13 de Fevereiro de 2025** (Cloud Manager 2025.2.0), o Cloud Manager Code Quality utiliza SonarQube 9.9 com lista atualizada de regras e migração de chaves `squid:*` para `java:*`.

| Old Key (pre 2024.12.0) | New Key | Categoria | Impacto |
|-------------------------|---------|-----------|---------|
| squid:S2068 | java:S2068 | Security Hotspot | Senhas hardcoded |
| squid:S2095 | java:S2095 | Bug | Fechamento de recursos |
| squid:S2168 | java:S2168 | Bug | Double-checked locking |
| squid:S2276 | java:S2276 | Bug | Thread.sleep vs wait |
| squid:S1147 | java:S1147 | Code Smell | System.exit() |
| squid:S128 | java:S128 | Code Smell | Switch cases |
| squid:S2178 | java:S2178 | Code Smell | Short-circuit logic |
| squid:S2254 | java:S2254 | Vulnerability | Session ID exposure |
| squid:S2658 | java:S2658 | Vulnerability | Dynamic class loading |
| squid:S2976 | java:S5445 | Vulnerability | Temp file creation |
| squid:S2277 | java:S5542 | Vulnerability | Encryption algorithms |
| squid:S2258 | java:S5547 | Vulnerability | Cipher algorithms |
| squid:S2070 | java:S4790 | Security Hotspot | Weak hashing |
| squid:S2245 | java:S2245 | Security Hotspot | PRNG usage |
| squid:S2257 | java:S2257 | Security Hotspot | Non-standard crypto |
| squid:S2077 | java:S2077 | Security Hotspot | SQL injection |
| squid:S2092 | java:S2092 | Security Hotspot | Cookie security |

### Ações Necessárias para Migração:

#### 1. Atualização de Configurações SonarQube

```xml
<!-- sonar-project.properties - ANTES -->
sonar.issue.ignore.multicriteria=e1,e2,e3
sonar.issue.ignore.multicriteria.e1.ruleKey=squid:S2095
sonar.issue.ignore.multicriteria.e2.ruleKey=squid:S2068
sonar.issue.ignore.multicriteria.e3.ruleKey=squid:S1147

<!-- sonar-project.properties - DEPOIS -->
sonar.issue.ignore.multicriteria=e1,e2,e3
sonar.issue.ignore.multicriteria.e1.ruleKey=java:S2095
sonar.issue.ignore.multicriteria.e2.ruleKey=java:S2068
sonar.issue.ignore.multicriteria.e3.ruleKey=java:S1147
```

#### 2. Atualização de Quality Gates

```json
// Quality Gate - ANTES
{
  "conditions": [
    {
      "metric": "squid:S2095",
      "operator": "GT",
      "threshold": "0"
    }
  ]
}

// Quality Gate - DEPOIS
{
  "conditions": [
    {
      "metric": "java:S2095", 
      "operator": "GT",
      "threshold": "0"
    }
  ]
}
```

#### 3. Atualização de Scripts de CI/CD

```bash
#!/bin/bash
# ANTES - Verificação de regras antigas
if sonar-scanner | grep -q "squid:S2095"; then
    echo "Resource leak detected"
    exit 1
fi

# DEPOIS - Verificação de regras novas
if sonar-scanner | grep -q "java:S2095"; then
    echo "Resource leak detected"
    exit 1
fi
```

#### 4. Atualização de Documentação de Equipe

```markdown
# Guia de Desenvolvimento - ATUALIZADO

## Regras Críticas Java (SonarQube 9.9+)

### Gerenciamento de Recursos
- **java:S2095** (antes squid:S2095): Resources should be closed
- **AEM Rules:AEM-6**: ResourceResolver should be closed in finally block

### Segurança
- **java:S2068** (antes squid:S2068): Hard-coded passwords
- **java:S2254** (antes squid:S2254): HttpServletRequest.getRequestedSessionId()

### Threading
- **java:S2168** (antes squid:S2168): Double-checked locking
- **java:S2276** (antes squid:S2276): wait() vs Thread.sleep()
```

---
## 🛠️ Guia de Implementação para AEM

### Configuração SonarQube para Projetos AEM

#### sonar-project.properties
```properties
# Configuração básica para projetos AEM
sonar.projectKey=com.mycompany:aem-project
sonar.projectName=AEM Project
sonar.projectVersion=1.0.0

# Configurações Java
sonar.java.source=11
sonar.java.target=11
sonar.java.libraries=target/dependency/*.jar,core/target/classes

# Exclusões padrão AEM
sonar.exclusions=**/target/**,**/node_modules/**,**/clientlibs-site/**,**/resources/**

# Inclusões específicas para Java backend
sonar.sources=core/src/main/java,it.tests/src/main/java
sonar.tests=core/src/test/java,it.tests/src/test/java

# Configurações específicas para regras Java
sonar.java.coveragePlugin=jacoco
sonar.jacoco.reportPaths=target/site/jacoco/jacoco.exec
```

#### Quality Gates Recomendados para AEM
```json
{
  "name": "AEM Java Backend Quality Gate",
  "conditions": [
    {
      "metric": "bugs",
      "operator": "GT", 
      "threshold": "0"
    },
    {
      "metric": "vulnerabilities",
      "operator": "GT",
      "threshold": "0"
    },
    {
      "metric": "security_hotspots_reviewed",
      "operator": "LT",
      "threshold": "100"
    },
    {
      "metric": "code_smells",
      "operator": "GT",
      "threshold": "50"
    },
    {
      "metric": "coverage",
      "operator": "LT",
      "threshold": "80"
    },
    {
      "metric": "duplicated_lines_density",
      "operator": "GT",
      "threshold": "3"
    }
  ]
}
```

### Configuração Maven para Análise SonarQube

#### pom.xml - Configuração Parent
```xml
<properties>
    <sonar.host.url>https://sonarqube.company.com</sonar.host.url>
    <sonar.organization>mycompany</sonar.organization>
    <sonar.projectKey>com.mycompany:aem-project</sonar.projectKey>
    
    <!-- Configurações específicas para regras Java AEM -->
    <sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
    <sonar.dynamicAnalysis>reuseReports</sonar.dynamicAnalysis>
    <sonar.jacoco.reportPath>${project.basedir}/../target/jacoco.exec</sonar.jacoco.reportPath>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>org.sonarsource.scanner.maven</groupId>
            <artifactId>sonar-maven-plugin</artifactId>
            <version>3.9.1.2184</version>
        </plugin>
        
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.7</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Pipeline CI/CD com Verificação de Qualidade

#### .github/workflows/quality-check.yml
```yaml
name: AEM Java Quality Check

on:
  pull_request:
    branches: [ main, develop ]
  push:
    branches: [ main ]

jobs:
  sonarqube-analysis:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
      with:
        fetch-depth: 0
        
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
        
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        
    - name: Run tests with coverage
      run: mvn clean verify jacoco:report
      
    - name: SonarQube Analysis
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
      run: |
        mvn sonar:sonar \
          -Dsonar.projectKey=aem-project \
          -Dsonar.organization=mycompany \
          -Dsonar.host.url=https://sonarcloud.io \
          -Dsonar.login=$SONAR_TOKEN
          
    - name: Quality Gate Check
      uses: sonarqube-quality-gate-action@master
      env:
        SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

### Configuração de Regras Customizadas

#### custom-rules.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<profile>
    <name>AEM Java Backend Rules</name>
    <language>java</language>
    
    <!-- Regras críticas para AEM -->
    <rules>
        <!-- Resource Management -->
        <rule>
            <repositoryKey>java</repositoryKey>
            <key>S2095</key>
            <priority>BLOCKER</priority>
        </rule>
        
        <!-- AEM Specific Rules -->
        <rule>
            <repositoryKey>AEM Rules</repositoryKey>
            <key>AEM-6</key>
            <priority>CRITICAL</priority>
        </rule>
        
        <rule>
            <repositoryKey>CQRules</repositoryKey>
            <key>CQBP-72</key>
            <priority>MAJOR</priority>
        </rule>
        
        <!-- Security Rules -->
        <rule>
            <repositoryKey>java</repositoryKey>
            <key>S2068</key>
            <priority>BLOCKER</priority>
        </rule>
        
        <!-- Threading Rules -->
        <rule>
            <repositoryKey>java</repositoryKey>
            <key>S2168</key>
            <priority>BLOCKER</priority>
        </rule>
        
        <rule>
            <repositoryKey>AEM Rules</repositoryKey>
            <key>AEM-3</key>
            <priority>CRITICAL</priority>
        </rule>
    </rules>
</profile>
```

---