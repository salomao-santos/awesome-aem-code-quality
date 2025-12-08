package com.example.test;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Exemplo de código CORRETO seguindo as regras do SonarQube
 * Compare com example-test.java para ver as diferenças
 */
public class ExampleCompliant {

    private static final Logger logger = LoggerFactory.getLogger(ExampleCompliant.class);
    private ResourceResolverFactory factory;

    // ✅ CORRETO: ResourceResolver fechado com try-with-resources
    public void goodResourceResolverUsage() throws Exception {
        try (ResourceResolver resolver = factory.getResourceResolver(null)) {
            // fazer algo com o resolver
            // será fechado automaticamente
        }
    }

    // ✅ CORRETO: Requisição HTTP com timeouts configurados
    public void goodHttpRequest() throws Exception {
        URL url = new URL("http://www.example.com");
        URLConnection urlConnection = url.openConnection();
        
        // Configurar timeouts
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()))) {
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                logger.debug("Response line: {}", inputLine);  // ✅ Usa logger
            }
        }
    }

    // ✅ CORRETO: Tratamento de exceção com logging apropriado
    public void goodExceptionHandling() {
        try {
            someRiskyOperation();
        } catch (Exception e) {
            logger.error("Failed to execute risky operation", e);  // ✅ Usa logger com contexto
        }
    }

    // ✅ CORRETO: Caminho relativo sem hardcode
    public boolean goodPathUsage(Resource resource) {
        return resource.isResourceType("foundation/components/text");
        // ✅ Caminho relativo, sem /libs hardcoded
    }

    // ✅ CORRETO: Thread com flag de controle em vez de stop()
    public class GoodThreadUsage implements Runnable {
        private Thread thread;
        private volatile boolean keepRunning = true;  // ✅ Flag de controle

        public void start() {
            thread = new Thread(this);
            thread.start();
        }

        public void stop() {
            keepRunning = false;  // ✅ Sinaliza para parar de forma segura
        }

        @Override
        public void run() {
            while (keepRunning) {  // ✅ Verifica flag
                try {
                    doWork();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Thread interrupted", e);
                    break;
                }
            }
        }

        private void doWork() throws InterruptedException {
            // fazer algo
            Thread.sleep(100);
        }
    }

    // ✅ CORRETO: HttpClient com timeouts configurados
    public void goodHttpClientUsage() {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .build();

        HttpClientBuilder builder = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig);

        // usar o cliente
    }

    // ✅ CORRETO: Exceção lançada sem log duplicado
    public void goodExceptionThrow() throws CustomException {
        try {
            someRiskyOperation();
        } catch (Exception e) {
            // ✅ Apenas lança, sem logar (será logado em nível superior)
            throw new CustomException("Operation failed", e);
        }
    }

    // ✅ CORRETO: Log em nível apropriado para GET requests
    public void goodGetRequestLogging(HttpServletRequest request) {
        logger.debug("Handling GET request from {}", request.getRemoteAddr());
        // ✅ DEBUG para operações de leitura, não INFO
    }

    // ✅ CORRETO: Mensagem de log contextual
    public void goodLogMessage() {
        try {
            processUserData();
        } catch (Exception e) {
            // ✅ Mensagem contextual, não apenas e.getMessage()
            logger.error("Failed to process user data", e);
        }
    }

    // ✅ CORRETO: Log em nível apropriado no catch
    public void goodCatchLogging() {
        try {
            criticalOperation();
        } catch (Exception e) {
            // ✅ ERROR level para exceções
            logger.error("Critical operation failed", e);
        }
    }

    private void someRiskyOperation() throws Exception {
        // simulação
    }

    private void processUserData() throws Exception {
        // simulação
    }

    private void criticalOperation() throws Exception {
        // simulação
    }

    // Classe de exceção customizada
    private static class CustomException extends Exception {
        public CustomException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
