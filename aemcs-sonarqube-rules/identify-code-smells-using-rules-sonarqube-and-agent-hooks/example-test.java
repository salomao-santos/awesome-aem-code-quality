package com.example.test;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.jcr.Session;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.security.MessageDigest;

/**
 * ⚠️ ARQUIVO DE TESTE - CONTÉM VIOLAÇÕES INTENCIONAIS ⚠️
 * 
 * Este arquivo contém múltiplas violações das regras SonarQube AEM
 * para testar o sistema de validação automática via Agent Hook.
 * 
 * INSTRUÇÕES:
 * 1. Configure o MCP AEM Documentation
 * 2. Configure o Agent Hook para validação
 * 3. Salve este arquivo para disparar a análise automática
 * 4. Observe as violações identificadas pelo hook
 */
@Component(property = {
    "sling.servlet.paths=/apps/myco/endpoint"  // VIOLAÇÃO: CQRules:CQBP-75 - Não use servlet paths
})
public class ExampleWithViolations extends SlingAllMethodsServlet {

    private static final Logger logger = LoggerFactory.getLogger(ExampleWithViolations.class);

    // VIOLAÇÃO: java:S1444 - Campo público estático não constante
    public static String publicField = "bad practice";
    
    // VIOLAÇÃO: java:S2386 - Campo público estático mutável
    public static String[] publicArray = {"item1", "item2"};
    
    private ResourceResolverFactory factory;
    private String password = "hardcoded123";  // VIOLAÇÃO: java:S2068 - Senha hardcoded
    
    // VIOLAÇÃO 1: ResourceResolver não fechado (CQRules:CQBP-72)
    public void badResourceResolverUsage() throws Exception {
        ResourceResolver resolver = factory.getResourceResolver(null);
        // fazer algo com o resolver
        // PROBLEMA: resolver nunca é fechado!
    }

    // VIOLAÇÃO 2: Requisição HTTP sem timeout (CQRules:ConnectionTimeoutMechanism)
    public void badHttpRequest() throws Exception {
        URL url = new URL("http://www.example.com");
        URLConnection urlConnection = url.openConnection();
        // PROBLEMA: sem timeout configurado!

        BufferedReader in = new BufferedReader(new InputStreamReader(
            urlConnection.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);  // VIOLAÇÃO: CQRules:CQBP-44 - Não use System.out
        }

        in.close();
    }

    // VIOLAÇÃO 3: Exception.printStackTrace() (CQRules:CQBP-44)
    public void badExceptionHandling() {
        try {
            someRiskyOperation();
        } catch (Exception e) {
            e.printStackTrace();  // PROBLEMA: use logger em vez disso!
            throw e;  // VIOLAÇÃO: CQRules:CQBP-44 - Log e throw juntos
        }
    }

    // VIOLAÇÃO 4: Caminho hardcoded (CQRules:CQBP-71)
    public boolean badPathUsage(Resource resource) {
        return resource.isResourceType("/libs/foundation/components/text");
        // PROBLEMA: caminho hardcoded de /libs
    }

    // VIOLAÇÃO 5: Thread.stop() - função perigosa (CQRules:CWE-676)
    public class BadThreadUsage implements Runnable {
        private Thread thread;

        public void start() {
            thread = new Thread(this);
            thread.start();
        }

        public void stop() {
            thread.stop();  // PROBLEMA: método perigoso e deprecated!
        }

        public void run() {
            while (true) {
                // fazer algo
            }
        }
    }

    // VIOLAÇÃO 6: Servlet com campos mutáveis (java:S2226)
    private StringBuilder buffer = new StringBuilder();  // PROBLEMA: campo mutável em servlet

    // VIOLAÇÃO 7: Objeto não-serializável em sessão (java:S2441)
    public void badSessionUsage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("nonSerializable", new Object());  // PROBLEMA: não-serializável
    }

    // VIOLAÇÃO 8: Algoritmo de hash fraco (java:S4790)
    public String weakHash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");  // PROBLEMA: MD5 é fraco
        return new String(md.digest(input.getBytes()));
    }

    // VIOLAÇÃO 9: Random não-seguro para criptografia (java:S2245)
    public String generateToken() {
        Random random = new Random();  // PROBLEMA: não use Random para segurança
        return String.valueOf(random.nextLong());
    }

    // VIOLAÇÃO 10: Recurso não fechado (java:S2095)
    public String readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        // PROBLEMA: FileInputStream nunca é fechado!
        return "content";
    }

    // VIOLAÇÃO 11: Switch sem default (java:S131)
    public String processType(int type) {
        switch (type) {
            case 1:
                return "type1";
            case 2:
                return "type2";
            // PROBLEMA: falta case default
        }
        return null;
    }

    // VIOLAÇÃO 12: Comparação de classes por nome (java:S1872)
    public boolean isPageType(Object obj) {
        return obj.getClass().getName().equals("com.day.cq.wcm.api.Page");
        // PROBLEMA: use instanceof em vez disso
    }

    // VIOLAÇÃO 13: Método finalize() público (java:S1174)
    public void finalize() throws Throwable {  // PROBLEMA: deve ser protected
        super.finalize();
    }

    // VIOLAÇÃO 14: Catch de Exception genérica (java:S112)
    public void badMethodSignature() throws Exception {  // PROBLEMA: muito genérico
        // implementação
    }

    // VIOLAÇÃO 15: Log no nível INFO em GET (CQRules:CQBP-44)
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        logger.info("Processing GET request");  // PROBLEMA: use DEBUG em GET
    }

    // VIOLAÇÃO 16: Uso de API deprecada AEM (CQRules:AMSCORE-553)
    public void useDeprecatedAPI(Session session) {
        // Exemplo de uso de API deprecada - seria detectado pelo SonarQube
        // session.getRepository().login();  // Método deprecado
    }

    // VIOLAÇÃO 17: Divisão por zero possível (java:S3518)
    public int divide(int a, int b) {
        return a / b;  // PROBLEMA: b pode ser zero
    }

    // VIOLAÇÃO 18: Equals sempre retorna false (simulação)
    @Override
    public boolean equals(Object obj) {
        return false;  // PROBLEMA: sempre false
    }

    // VIOLAÇÃO 19: TODO não resolvido (java:S1135)
    public void methodWithTodo() {
        // TODO: implementar esta funcionalidade
        // PROBLEMA: TODO não resolvido
    }

    // VIOLAÇÃO 20: System.exit() (java:S1147)
    public void badExit() {
        if (someCondition()) {
            System.exit(1);  // PROBLEMA: não use System.exit()
        }
    }

    private void someRiskyOperation() throws Exception {
        // simulação de operação arriscada
        throw new RuntimeException("Erro simulado");
    }

    private boolean someCondition() {
        return true;
    }

    // Método para testar o hook - adicione comentários aqui para disparar nova análise
    // TESTE DO HOOK: Salve este arquivo para ver a análise automática
}
