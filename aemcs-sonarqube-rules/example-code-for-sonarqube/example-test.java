package com.example.test;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Exemplo de código com VÁRIAS violações do SonarQube
 * Use este arquivo para testar o hook de validação
 */
public class ExampleWithViolations {

    // Teste do hook SonarQube

    private ResourceResolverFactory factory;

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
            System.out.println(inputLine);  // VIOLAÇÃO 3: Não use System.out (CQRules:CQBP-44)
        }

        in.close();
    }

    // VIOLAÇÃO 4: Exception.printStackTrace() (CQRules:CQBP-44)
    public void badExceptionHandling() {
        try {
            someRiskyOperation();
        } catch (Exception e) {
            e.printStackTrace();  // PROBLEMA: use logger em vez disso!
        }
    }

    // VIOLAÇÃO 5: Caminho hardcoded (CQRules:CQBP-71)
    public boolean badPathUsage(Resource resource) {
        return resource.isResourceType("/libs/foundation/components/text");
        // PROBLEMA: caminho hardcoded de /libs
    }

    // VIOLAÇÃO 6: Thread.stop() - função perigosa (CQRules:CWE-676)
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

    private void someRiskyOperation() throws Exception {
        // simulação
    }
    // Teste do hook SonarQube
}
