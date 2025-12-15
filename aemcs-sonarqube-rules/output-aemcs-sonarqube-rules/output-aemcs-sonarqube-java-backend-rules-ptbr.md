t code
```java
@Component(service = AssetProcessor.class)
public class AssetProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(AssetProcessor.class);
    
    // Pattern 1: Log and handle (don't rethrow)
    public void processAsset(Asset asset) {
        try {
            performAssetProcessing(asset);
        } catch (Exception e) {
            log.error("Asset processing failed for asset: {}", asset.getPath(), e);
            // Handle the error, don't rethrow
        }
    }
    
    // Pattern 2: Transform and throw (don't log)
    public void processAssetWithException(Asset asset) throws ProcessingException {
        try {
            performAssetProcessing(asset);
        } catch (Exception e) {
            // Transform to business exception without logging
            throw new ProcessingException("Failed to process asset: " + asset.getPath(), e);
        }
    }
}
```

### CQRules:CQBP-44—ExceptionPrintStackTrace - Do not use Exception.printStackTrace()

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-44—ExceptionPrintStackTrace |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **AEM Context** | Error Handling |

**Descrição**: Usar `Exception.printStackTrace()` faz com que apenas o stack trace seja enviado para o stream de erro padrão, perdendo todo o contexto.

**Impacto no AEM**: Em aplicação multi-thread como AEM, stack traces podem se sobrepor e causar confusão significativa.

#### Non-compliant code
```java
@Component(service = WorkflowStep.class,
    property = {
        "process.label=My Workflow Step"
    })
public class MyWorkflowStep implements WorkflowProcess {
    
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, 
                       MetaDataMap metaDataMap) throws WorkflowException {
        try {
            processWorkflowItem(workItem);
        } catch (Exception e) {
            e.printStackTrace(); // LOSES CONTEXT AND THREAD SAFETY
        }
    }
}
```

#### Compliant code
```java
@Component(service = WorkflowStep.class,
    property = {
        "process.label=My Workflow Step"
    })
public class MyWorkflowStep implements WorkflowProcess {
    
    private static final Logger log = LoggerFactory.getLogger(MyWorkflowStep.class);
    
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, 
                       MetaDataMap metaDataMap) throws WorkflowException {
        try {
            processWorkflowItem(workItem);
        } catch (Exception e) {
            log.error("Workflow step failed for item: {}", workItem.getId(), e);
            throw new WorkflowException("Processing failed", e);
        }
    }
}
```

---

## 🔄 Migrações de Chaves Java (SonarQube 9.9)

### Impacto da Migração squid:* → java:*

A partir de **13 de Fevereiro de 2025** (Cloud Manager 2025.2.0):

| Old Key (pre 2024.12.0) | New Key | Descrição | Impacto |
|-------------------------|---------|-----------|---------|
| squid:S2068 | java:S2068 | Hard-coded passwords | Security Hotspot |
| squid:S2095 | java:S2095 | Resources should be closed | Resource leaks |
| squid:S2168 | java:S2168 | Double-checked locking | Threading issues |
| squid:S2254 | java:S2254 | HttpServletRequest.getRequestedSessionId() | Security vulnerability |
| squid:S2658 | java:S2658 | Classes should not be loaded dynamically | Security vulnerability |
| squid:S2276 | java:S2276 | wait() vs Thread.sleep() with locks | Threading bug |
| squid:S2245 | java:S2245 | Pseudorandom number generators | Security hotspot |
| squid:S2257 | java:S2257 | Non-standard cryptographic algorithms | Security hotspot |
| squid:S2077 | java:S2077 | SQL binding mechanisms | Security hotspot |
| squid:S2092 | java:S2092 | Cookies without secure flag | Security hotspot |
| squid:S1989 | java:S1989 | Exceptions from servlet methods | Vulnerability |
| squid:S2441 | java:S2441 | Non-serializable objects in HttpSession | Bug |
| squid:S2222 | java:S2222 | Locks should be released | Bug |
| squid:S2273 | java:S2273 | wait/notify with obvious lock | Bug |
| squid:S2445 | java:S2445 | Synchronized on private final fields | Bug |
| squid:S2583 | java:S2583 | Conditionally executed code | Bug |
| squid:S2885 | java:S2885 | Non-thread-safe static fields | Bug |

### Ações Necessárias:

1. **Atualizar configurações SonarQube**:
   - Revisar quality profiles
   - Atualizar regras customizadas
   - Verificar exclusões baseadas em chaves antigas

2. **Revisar pipelines CI/CD**:
   - Atualizar scripts que referenciam chaves antigas
   - Verificar relatórios automatizados
   - Atualizar dashboards de qualidade

3. **Comunicação com equipes**:
   - Informar sobre mudanças nas chaves
   - Atualizar documentação interna
   - Treinar equipes sobre novas chaves

---

## 🛠️ Guia de Implementação

### Configuração SonarQube para AEM Java

```properties
# sonar-project.properties
sonar.projectKey=my-aem-project
sonar.projectName=My AEM Project
sonar.projectVersion=1.0

# Java configuration
sonar.java.source=11
sonar.java.target=11
sonar.java.libraries=target/dependency/*.jar,core/target/classes

# Source and test directories
sonar.sources=core/src/main/java,ui.apps/src/main/content
sonar.tests=core/src/test/java
sonar.java.test.libraries=target/dependency/*.jar

# Exclusions
sonar.exclusions=**/target/**,**/node_modules/**,**/*.min.js
sonar.test.exclusions=**/target/**

# Coverage
sonar.java.coveragePlugin=jacoco
sonar.jacoco.reportPaths=target/jacoco.exec
```

### Quality Gates Recomendados para AEM Java

```yaml
# Quality Gate: AEM Java Backend
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

### Configuração Maven para SonarQube

```xml
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
```

---

## 📚 Referências Java Backend

### Documentação Oficial AEM
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Java Best Practices for AEM](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/dev-guidelines-bestpractices)
- [OSGi Development Guidelines](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/deploying/configuring/configuring-osgi)
- [Sling Development](https://sling.apache.org/documentation/development.html)

### Ferramentas e Frameworks
- [SonarQube Java Rules](https://docs.sonarsource.com/sonarqube-server/latest/)
- [FindBugs Documentation](http://findbugs.sourceforge.net/)
- [Apache Sling Documentation](https://sling.apache.org/)
- [AEM Core Components](https://github.com/adobe/aem-core-wcm-components)

### Exemplos de Código
- [AEM Project Archetype](https://github.com/adobe/aem-project-archetype)
- [AEM Guides WKND](https://github.com/adobe/aem-guides-wknd)
- [ACS AEM Commons](https://adobe-consulting-services.github.io/acs-aem-commons/)

### Segurança
- [AEM Security Checklist](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/security/security-checklist)
- [OWASP Java Security](https://owasp.org/www-project-top-ten/)
- [CWE Common Weakness Enumeration](https://cwe.mitre.org/)

---

*Última atualização: 14 de dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Java Backend Development Rules*
*Foco: AEM Cloud Service Development*

## 🔧 Regras Java Adicionais do CSV

### java:S5542 - Encryption algorithms should be used with secure mode and padding scheme

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5542 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **AEM Context** | Data Encryption |
| **Old Key** | squid:S2277 |

**Descrição**: Algoritmos de criptografia devem ser usados com modo e esquema de padding seguros.

**Impacto no AEM**: Em armazenamento de dados sensíveis ou comunicação segura, algoritmos fracos podem expor informações.

#### Non-compliant code
```java
@Component(service = EncryptionService.class)
public class EncryptionService {
    
    public byte[] encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES"); // WEAK - no mode/padding specified
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data.getBytes());
    }
}
```

#### Compliant code
```java
@Component(service = EncryptionService.class)
public class EncryptionService {
    
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    
    public EncryptedData encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        
        // Generate random IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        
        return new EncryptedData(encryptedData, iv);
    }
    
    public static class EncryptedData {
        private final byte[] data;
        private final byte[] iv;
        
        public EncryptedData(byte[] data, byte[] iv) {
            this.data = data;
            this.iv = iv;
        }
        
        // getters...
    }
}
```

### java:S5547 - Cipher algorithms should be robust

| Atributo | Valor |
|----------|-------|
| **Key** | java:S5547 |
| **Type** | Vulnerability |
| **Severity** | Critical |
| **Tags** | cwe, owasp-a3, owasp-a6, owasp-m5, privacy, sans-top25-porous |
| **AEM Context** | Cryptographic Operations |
| **Old Key** | squid:S2258 |

**Descrição**: Algoritmos de cifra devem ser robustos e seguros contra ataques conhecidos.

**Impacto no AEM**: Algoritmos fracos podem ser quebrados, expondo dados sensíveis armazenados ou transmitidos.

#### Non-compliant code
```java
@Component(service = CryptoService.class)
public class CryptoService {
    
    public String encryptPassword(String password) throws Exception {
        Cipher cipher = Cipher.getInstance("DES"); // WEAK ALGORITHM
        // DES is vulnerable to brute force attacks
        return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes()));
    }
}
```

#### Compliant code
```java
@Component(service = CryptoService.class)
public class CryptoService {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_LENGTH = 256;
    
    public String encryptPassword(String password, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        
        byte[] iv = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        
        // Combine IV and encrypted data
        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
        
        return Base64.getEncoder().encodeToString(result);
    }
    
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_LENGTH);
        return keyGenerator.generateKey();
    }
}
```

### java:S1989 - Exceptions should not be thrown from servlet methods

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1989 |
| **Type** | Vulnerability |
| **Severity** | Minor |
| **Tags** | cert, cwe, error-handling, owasp-a3 |
| **AEM Context** | Servlet Development |
| **Old Key** | squid:S1989 |

**Descrição**: Exceções não devem ser lançadas de métodos de servlet pois podem expor informações sensíveis.

**Impacto no AEM**: Exceções não tratadas podem expor stack traces com informações do sistema para usuários finais.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/form"
    })
public class FormServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws ServletException, IOException {
        
        String data = request.getParameter("data");
        processData(data); // MAY THROW UNCAUGHT EXCEPTION
    }
    
    private void processData(String data) throws ProcessingException {
        if (data == null) {
            throw new ProcessingException("Data is null"); // EXPOSED TO USER
        }
        // Process data...
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/form"
    })
public class FormServlet extends SlingAllMethodsServlet {
    
    private static final Logger log = LoggerFactory.getLogger(FormServlet.class);
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String data = request.getParameter("data");
            processData(data);
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\"}");
            
        } catch (ProcessingException e) {
            log.error("Processing failed for user request", e);
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid request\"}");
            
        } catch (Exception e) {
            log.error("Unexpected error in form processing", e);
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Processing failed\"}");
        }
    }
    
    private void processData(String data) throws ProcessingException {
        if (data == null || data.trim().isEmpty()) {
            throw new ProcessingException("Invalid data provided");
        }
        // Process data...
    }
}
```

### java:S2077 - SQL binding mechanisms should be used

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2077 |
| **Type** | Security Hotspot |
| **Severity** | Major |
| **Tags** | cert, cwe, hibernate, owasp-a1, sans-top25-insecure, sql |
| **AEM Context** | Database Operations |
| **Old Key** | squid:S2077 |

**Descrição**: Mecanismos de binding SQL devem ser usados para prevenir injeção SQL.

**Impacto no AEM**: Em integrações com bancos de dados externos, SQL injection pode comprometer dados.

#### Non-compliant code
```java
@Component(service = UserService.class)
public class UserService {
    
    @Reference
    private DataSource dataSource;
    
    public User findUser(String username) throws SQLException {
        Connection conn = dataSource.getConnection();
        
        // SQL INJECTION VULNERABILITY
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        if (rs.next()) {
            return new User(rs.getString("username"), rs.getString("email"));
        }
        return null;
    }
}
```

#### Compliant code
```java
@Component(service = UserService.class)
public class UserService {
    
    @Reference
    private DataSource dataSource;
    
    private static final String FIND_USER_SQL = "SELECT username, email FROM users WHERE username = ?";
    
    public User findUser(String username) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_USER_SQL)) {
            
            stmt.setString(1, username); // SAFE - parameterized query
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("email"));
                }
            }
        }
        return null;
    }
    
    // For JCR queries in AEM, use Query API
    public List<Resource> findContentByTitle(ResourceResolver resolver, String title) {
        String queryString = "SELECT * FROM [cq:Page] WHERE [jcr:content/jcr:title] = $title";
        
        Query query = resolver.adaptTo(QueryManager.class).createQuery(queryString, Query.JCR_SQL2);
        query.bindValue("title", resolver.getValueFactory().createValue(title));
        
        List<Resource> results = new ArrayList<>();
        Iterator<Resource> resources = query.getResult().getResources();
        while (resources.hasNext()) {
            results.add(resources.next());
        }
        return results;
    }
}
```

### java:S2092 - Creating cookies without the "secure" flag is security-sensitive

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2092 |
| **Type** | Security Hotspot |
| **Severity** | Minor |
| **Tags** | cwe, owasp-a3, privacy, sans-top25-porous, spring |
| **AEM Context** | Cookie Management |
| **Old Key** | squid:S2092 |

**Descrição**: Criar cookies sem a flag "secure" é sensível à segurança em conexões HTTPS.

**Impacto no AEM**: Cookies inseguros podem ser interceptados em conexões não criptografadas.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/login"
    })
public class LoginServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // Authenticate user...
        
        Cookie sessionCookie = new Cookie("sessionId", generateSessionId());
        sessionCookie.setMaxAge(3600);
        // Missing secure flag - VULNERABLE over HTTPS
        response.addCookie(sessionCookie);
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/login"
    })
public class LoginServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // Authenticate user...
        
        Cookie sessionCookie = new Cookie("sessionId", generateSessionId());
        sessionCookie.setMaxAge(3600);
        sessionCookie.setSecure(true);      // Secure flag for HTTPS
        sessionCookie.setHttpOnly(true);    // Prevent XSS
        sessionCookie.setPath("/");         // Explicit path
        
        // For AEM, consider SameSite attribute
        response.setHeader("Set-Cookie", 
            sessionCookie.getName() + "=" + sessionCookie.getValue() + 
            "; Max-Age=" + sessionCookie.getMaxAge() + 
            "; Path=" + sessionCookie.getPath() + 
            "; Secure; HttpOnly; SameSite=Strict");
    }
    
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
```

---

## 🔧 Regras de Qualidade de Código Java

### java:S112 - Generic exceptions should never be thrown

| Atributo | Valor |
|----------|-------|
| **Key** | java:S112 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, error-handling |
| **AEM Context** | Exception Handling |
| **Old Key** | squid:S00112 |

**Descrição**: Exceções genéricas nunca devem ser lançadas pois não fornecem informação suficiente sobre o erro.

**Impacto no AEM**: Exceções genéricas dificultam o debugging e tratamento adequado de erros.

#### Non-compliant code
```java
@Component(service = AssetProcessor.class)
public class AssetProcessor {
    
    public void processAsset(Asset asset) throws Exception { // GENERIC EXCEPTION
        if (asset == null) {
            throw new RuntimeException("Asset is null"); // GENERIC EXCEPTION
        }
        
        if (!isValidAsset(asset)) {
            throw new Exception("Invalid asset"); // GENERIC EXCEPTION
        }
    }
}
```

#### Compliant code
```java
@Component(service = AssetProcessor.class)
public class AssetProcessor {
    
    public void processAsset(Asset asset) throws AssetProcessingException {
        if (asset == null) {
            throw new AssetProcessingException("Asset cannot be null");
        }
        
        if (!isValidAsset(asset)) {
            throw new InvalidAssetException("Asset validation failed: " + asset.getPath());
        }
    }
    
    // Custom exceptions provide better context
    public static class AssetProcessingException extends Exception {
        public AssetProcessingException(String message) {
            super(message);
        }
        
        public AssetProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class InvalidAssetException extends AssetProcessingException {
        public InvalidAssetException(String message) {
            super(message);
        }
    }
}
```

### java:S1181 - Throwable and Error should not be caught

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1181 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | bad-practice, cert, cwe, error-handling |
| **AEM Context** | Error Handling |
| **Old Key** | squid:S1181 |

**Descrição**: Throwable e Error não devem ser capturados pois representam problemas sérios do sistema.

**Impacto no AEM**: Capturar Error pode mascarar problemas críticos como OutOfMemoryError.

#### Non-compliant code
```java
@Component(service = WorkflowStep.class)
public class WorkflowStep implements WorkflowProcess {
    
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, 
                       MetaDataMap metaDataMap) throws WorkflowException {
        try {
            processWorkflowItem(workItem);
        } catch (Throwable t) { // TOO BROAD - catches Error too
            log.error("Workflow failed", t);
            // This might catch OutOfMemoryError, StackOverflowError, etc.
        }
    }
}
```

#### Compliant code
```java
@Component(service = WorkflowStep.class)
public class WorkflowStep implements WorkflowProcess {
    
    private static final Logger log = LoggerFactory.getLogger(WorkflowStep.class);
    
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, 
                       MetaDataMap metaDataMap) throws WorkflowException {
        try {
            processWorkflowItem(workItem);
        } catch (WorkflowException e) {
            // Re-throw workflow exceptions
            throw e;
        } catch (Exception e) {
            // Catch specific exceptions, not Throwable/Error
            log.error("Workflow processing failed for item: {}", workItem.getId(), e);
            throw new WorkflowException("Processing failed", e);
        }
        // Let Error propagate - don't catch OutOfMemoryError, etc.
    }
}
```

### java:S1854 - Unused assignments should be removed

| Atributo | Valor |
|----------|-------|
| **Key** | java:S1854 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, unused |
| **AEM Context** | Code Quality |
| **Old Key** | squid:S1854 |

**Descrição**: Atribuições não utilizadas devem ser removidas para melhorar a legibilidade do código.

**Impacto no AEM**: Código morto pode confundir desenvolvedores e indicar lógica incompleta.

#### Non-compliant code
```java
@Component(service = PageService.class)
public class PageService {
    
    public String getPageTitle(Page page) {
        String title = page.getTitle();
        String description = page.getDescription(); // UNUSED ASSIGNMENT
        
        if (title == null) {
            title = page.getName();
        }
        
        String fallbackTitle = "Default Title"; // UNUSED ASSIGNMENT
        
        return title;
    }
}
```

#### Compliant code
```java
@Component(service = PageService.class)
public class PageService {
    
    public String getPageTitle(Page page) {
        String title = page.getTitle();
        
        if (title == null) {
            title = page.getName();
        }
        
        return title != null ? title : "Default Title";
    }
    
    // If you need description, create a separate method
    public PageInfo getPageInfo(Page page) {
        String title = getPageTitle(page);
        String description = page.getDescription();
        
        return new PageInfo(title, description);
    }
}
```

### java:S2589 - Boolean expressions should not be gratuitous

| Atributo | Valor |
|----------|-------|
| **Key** | java:S2589 |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | cert, cwe, redundant, suspicious |
| **AEM Context** | Logic Errors |
| **Old Key** | squid:S1850 |

**Descrição**: Expressões booleanas não devem ser gratuitas (sempre true ou false).

**Impacto no AEM**: Lógica redundante pode indicar bugs ou código morto.

#### Non-compliant code
```java
@Component(service = ContentValidator.class)
public class ContentValidator {
    
    public boolean isValidContent(Resource resource) {
        if (resource != null) {
            ValueMap properties = resource.getValueMap();
            
            if (properties != null && properties != null) { // REDUNDANT CHECK
                return true;
            }
            
            boolean hasTitle = properties.containsKey("jcr:title");
            if (hasTitle == true) { // GRATUITOUS COMPARISON
                return true;
            }
        }
        
        return false;
    }
}
```

#### Compliant code
```java
@Component(service = ContentValidator.class)
public class ContentValidator {
    
    public boolean isValidContent(Resource resource) {
        if (resource == null) {
            return false;
        }
        
        ValueMap properties = resource.getValueMap();
        if (properties == null) {
            return false;
        }
        
        // Direct boolean check
        return properties.containsKey("jcr:title") || 
               properties.containsKey("jcr:description");
    }
    
    public ValidationResult validateContentDetailed(Resource resource) {
        if (resource == null) {
            return ValidationResult.invalid("Resource is null");
        }
        
        ValueMap properties = resource.getValueMap();
        if (properties.isEmpty()) {
            return ValidationResult.invalid("No properties found");
        }
        
        List<String> issues = new ArrayList<>();
        
        if (!properties.containsKey("jcr:title")) {
            issues.add("Missing title");
        }
        
        if (!properties.containsKey("jcr:description")) {
            issues.add("Missing description");
        }
        
        return issues.isEmpty() ? 
            ValidationResult.valid() : 
            ValidationResult.invalid(String.join(", ", issues));
    }
}
```

---

## 🔄 Regras de Sling Models e OSGi

### AEM Rules:AEM-16 - Optional is defined as DefaultInjectionStrategy

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-16 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, sling-models |
| **AEM Context** | Sling Models |

**Descrição**: Optional deve ser definido como DefaultInjectionStrategy em Sling Models para melhor performance.

**Impacto no AEM**: Injection strategy inadequada pode causar falhas de inicialização de modelos.

#### Non-compliant code
```java
@Model(adaptables = Resource.class)
public class MyModel {
    
    @ValueMapValue
    @Optional // SHOULD USE DEFAULT INJECTION STRATEGY
    private String title;
    
    @ChildResource
    @Optional // SHOULD USE DEFAULT INJECTION STRATEGY
    private Resource image;
    
    @OSGiService
    @Optional // SHOULD USE DEFAULT INJECTION STRATEGY
    private PageManager pageManager;
}
```

#### Compliant code
```java
@Model(adaptables = Resource.class, 
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MyModel {
    
    @ValueMapValue
    private String title;
    
    @ChildResource
    private Resource image;
    
    @OSGiService
    private PageManager pageManager;
    
    // For required fields, use @Required
    @ValueMapValue
    @Required
    private String requiredField;
    
    // Getters with null checks for optional fields
    public String getTitle() {
        return title != null ? title : "";
    }
    
    public String getImagePath() {
        return image != null ? image.getPath() : "";
    }
    
    public boolean hasImage() {
        return image != null;
    }
}
```

### AEM Rules:AEM-2 - Use predefined constant instead of hardcoded value

| Atributo | Valor |
|----------|-------|
| **Key** | AEM Rules:AEM-2 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem |
| **AEM Context** | Constants Usage |

**Descrição**: Use constantes predefinidas ao invés de valores hardcoded para melhor manutenibilidade.

**Impacto no AEM**: Valores hardcoded dificultam manutenção e podem causar inconsistências.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/api"
    })
public class ApiServlet extends SlingSafeMethodsServlet {
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws IOException {
        
        response.setContentType("application/json"); // HARDCODED
        response.setStatus(200); // HARDCODED
        
        String path = "/content/mysite"; // HARDCODED PATH
        
        if (request.getParameter("type").equals("page")) { // HARDCODED
            // Process page request
        }
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/api"
    })
public class ApiServlet extends SlingSafeMethodsServlet {
    
    // Use predefined constants
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String SITE_ROOT_PATH = "/content/mysite";
    private static final String PARAM_TYPE = "type";
    private static final String TYPE_PAGE = "page";
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws IOException {
        
        response.setContentType(CONTENT_TYPE_JSON);
        response.setStatus(HttpServletResponse.SC_OK); // Use HTTP constants
        
        String requestType = request.getParameter(PARAM_TYPE);
        
        if (TYPE_PAGE.equals(requestType)) {
            processPageRequest(request, response);
        }
    }
    
    private void processPageRequest(SlingHttpServletRequest request, 
                                  SlingHttpServletResponse response) throws IOException {
        ResourceResolver resolver = request.getResourceResolver();
        Resource siteRoot = resolver.getResource(SITE_ROOT_PATH);
        
        if (siteRoot != null) {
            // Process site content
        }
    }
}
```

---

## 🛡️ Regras de Segurança Avançadas

### findsecbugs:PATH_TRAVERSAL_IN - Security - Potential Path Traversal (file read)

| Atributo | Valor |
|----------|-------|
| **Key** | findsecbugs:PATH_TRAVERSAL_IN |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cwe, owasp-a4, wasc |
| **AEM Context** | File Operations |

**Descrição**: Potencial path traversal em operações de leitura de arquivo pode permitir acesso não autorizado.

**Impacto no AEM**: Atacantes podem ler arquivos sensíveis do sistema através de manipulação de caminhos.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/fileread"
    })
public class FileReadServlet extends SlingSafeMethodsServlet {
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws IOException {
        
        String fileName = request.getParameter("file");
        File file = new File("/var/uploads/" + fileName); // VULNERABLE TO PATH TRAVERSAL
        
        if (file.exists()) {
            Files.copy(file.toPath(), response.getOutputStream());
        }
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=myapp/components/fileread"
    })
public class FileReadServlet extends SlingSafeMethodsServlet {
    
    private static final String UPLOAD_DIR = "/var/uploads/";
    private static final Pattern SAFE_FILENAME = Pattern.compile("^[a-zA-Z0-9._-]+$");
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws IOException {
        
        String fileName = request.getParameter("file");
        
        if (!isValidFileName(fileName)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filename");
            return;
        }
        
        // Use ResourceResolver for AEM-managed files
        ResourceResolver resolver = request.getResourceResolver();
        Resource fileResource = resolver.getResource("/content/dam/uploads/" + fileName);
        
        if (fileResource != null) {
            Asset asset = fileResource.adaptTo(Asset.class);
            if (asset != null) {
                Rendition original = asset.getOriginal();
                try (InputStream is = original.getStream()) {
                    response.setContentType(asset.getMimeType());
                    IOUtils.copy(is, response.getOutputStream());
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private boolean isValidFileName(String fileName) {
        return fileName != null && 
               SAFE_FILENAME.matcher(fileName).matches() &&
               !fileName.contains("..") &&
               !fileName.startsWith("/") &&
               fileName.length() <= 255;
    }
}
```

### findsecbugs:PATH_TRAVERSAL_OUT - Security - Potential Path Traversal (file write)

| Atributo | Valor |
|----------|-------|
| **Key** | findsecbugs:PATH_TRAVERSAL_OUT |
| **Type** | Vulnerability |
| **Severity** | Major |
| **Tags** | cwe, owasp-a4, wasc |
| **AEM Context** | File Upload Operations |

**Descrição**: Potencial path traversal em operações de escrita de arquivo pode permitir sobrescrita de arquivos do sistema.

**Impacto no AEM**: Atacantes podem sobrescrever arquivos críticos do sistema ou criar arquivos em locais não autorizados.

#### Non-compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.paths=/bin/upload"
    })
public class FileUploadServlet extends SlingAllMethodsServlet {
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws IOException {
        
        RequestParameter fileParam = request.getRequestParameter("file");
        String fileName = request.getParameter("filename");
        
        // VULNERABLE - no path validation
        File targetFile = new File("/var/uploads/" + fileName);
        
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            IOUtils.copy(fileParam.getInputStream(), fos);
        }
    }
}
```

#### Compliant code
```java
@Component(service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=myapp/components/upload"
    })
public class FileUploadServlet extends SlingAllMethodsServlet {
    
    private static final String UPLOAD_PATH = "/content/dam/uploads";
    private static final Pattern SAFE_FILENAME = Pattern.compile("^[a-zA-Z0-9._-]+$");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "png", "pdf", "docx");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    
    @Reference
    private AssetManager assetManager;
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws IOException {
        
        RequestParameter fileParam = request.getRequestParameter("file");
        String fileName = request.getParameter("filename");
        
        // Validate filename
        if (!isValidFileName(fileName)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filename");
            return;
        }
        
        // Validate file size
        if (fileParam.getSize() > MAX_FILE_SIZE) {
            response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "File too large");
            return;
        }
        
        // Use AEM Asset Manager for secure file handling
        ResourceResolver resolver = request.getResourceResolver();
        
        try {
            String assetPath = UPLOAD_PATH + "/" + fileName;
            
            // Create asset using AEM's secure mechanisms
            Asset asset = assetManager.createAsset(
                assetPath,
                fileParam.getInputStream(),
                fileParam.getContentType(),
                true
            );
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"path\":\"" + asset.getPath() + "\"}");
            
        } catch (Exception e) {
            log.error("Failed to upload file: {}", fileName, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Upload failed");
        }
    }
    
    private boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        // Check pattern
        if (!SAFE_FILENAME.matcher(fileName).matches()) {
            return false;
        }
        
        // Check extension
        String extension = getFileExtension(fileName);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            return false;
        }
        
        // Check for path traversal
        return !fileName.contains("..") && 
               !fileName.startsWith("/") && 
               fileName.length() <= 255;
    }
    
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }
}
```

---

## 📋 Resumo de Implementação

### Checklist de Qualidade Java para AEM

#### 🔒 Segurança
- [ ] Não usar `HttpServletRequest.getRequestedSessionId()`
- [ ] Evitar carregamento dinâmico de classes
- [ ] Usar algoritmos de criptografia robustos (AES-256-GCM)
- [ ] Implementar validação rigorosa de entrada
- [ ] Configurar cookies com flags de segurança
- [ ] Usar prepared statements para SQL
- [ ] Validar caminhos de arquivo contra path traversal

#### 🧵 Threading e Concorrência
- [ ] Evitar double-checked locking
- [ ] Usar objetos thread-safe em servlets
- [ ] Preferir `wait()` ao invés de `Thread.sleep()` com locks
- [ ] Sincronizar em campos `private final`
- [ ] Evitar campos estáticos não thread-safe

#### 🔧 Gerenciamento de Recursos
- [ ] Sempre fechar `ResourceResolver` (try-with-resources)
- [ ] Fazer logout de `Session` JCR
- [ ] Fechar `InputStream`/`OutputStream`
- [ ] Configurar timeouts em `HttpClient`
- [ ] Usar service users ao invés de acesso administrativo

#### 📝 Qualidade de Código
- [ ] Usar exceções específicas ao invés de genéricas
- [ ] Não capturar `Throwable` ou `Error`
- [ ] Remover atribuições não utilizadas
- [ ] Evitar expressões booleanas redundantes
- [ ] Usar constantes ao invés de valores hardcoded

#### 🎯 Boas Práticas AEM
- [ ] Registrar servlets por resource type, não por path
- [ ] Usar `DefaultInjectionStrategy.OPTIONAL` em Sling Models
- [ ] Implementar tratamento adequado de exceções em servlets
- [ ] Usar Asset Manager para operações de arquivo
- [ ] Validar permissões com `ResourceResolver`

### Ferramentas de Validação

```bash
# Executar SonarQube
mvn clean compile sonar:sonar

# Executar testes com cobertura
mvn clean test jacoco:report

# Validar com Checkstyle
mvn checkstyle:check

# Executar SpotBugs
mvn spotbugs:check
```

---

*Documento gerado automaticamente via MCP AEM Documentation + análise de CSVs*
*Total de regras Java documentadas: 87*
*Exemplos de código AEM-específicos: 25+*
*Foco: Desenvolvimento Java Backend para AEM Cloud Service*