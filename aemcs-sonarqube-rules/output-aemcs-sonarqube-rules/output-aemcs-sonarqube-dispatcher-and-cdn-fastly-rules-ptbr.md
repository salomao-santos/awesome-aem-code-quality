# Regras Dispatcher, CDN Fastly e Infraestrutura - AEM Cloud Service

**Última atualização:** 14 de Dezembro de 2025  
**Categoria:** Dispatcher, CDN Fastly & Infrastructure Rules  
**Tecnologias:** `.any`, `.rules`, `.vhost`, `.conf`, `.farm`, `.vcl`, CDN configs  
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Dispatcher, CDN Fastly & Infrastructure

| Tipo | Quantidade |
|------|------------|
| **Total Regras Dispatcher & CDN** | 18 |
| **Regras Dispatcher** | 8 |
| **Regras Apache** | 2 |
| **Regras Sintaxe** | 8 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Major** | 18 |

---

## 🔧 Regras Dispatcher (DOTRules:Disp-*)

### DOTRules:Disp-1---ignoreUrlParams-allow-list

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-1---ignoreUrlParams-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: O cache do farm de publicação do Dispatcher deve ter suas regras ignoreUrlParams configuradas de forma allowlist.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/ignoreUrlParams {
  /0001 { /glob "*" /type "deny" }
  /0002 { /glob "q" /type "allow" }
}
```

#### Configuração Compliant:
```apache
/ignoreUrlParams {
  /0001 { /glob "*" /type "allow" }
  /0002 { /glob "utm_*" /type "deny" }
  /0003 { /glob "gclid" /type "deny" }
  /0004 { /glob "fbclid" /type "deny" }
}
```

---

### DOTRules:Disp-2---statfileslevel

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-2---statfileslevel |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A propriedade statfileslevel do cache do farm de publicação do Dispatcher deve ser >= 2.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /statfileslevel "1"
}
```

#### Configuração Compliant:
```apache
/cache {
  /statfileslevel "2"
}
```

**Explicação**: Um nível de statfiles de 2 ou superior permite invalidação mais granular do cache, melhorando a performance e reduzindo invalidações desnecessárias.

---

### DOTRules:Disp-3---gracePeriod

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-3---gracePeriod |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A propriedade gracePeriod do farm de publicação do Dispatcher deve ser >= 2.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /gracePeriod "1"
}
```

#### Configuração Compliant:
```apache
/cache {
  /gracePeriod "2"
}
```

**Explicação**: O período de graça permite que o Dispatcher sirva conteúdo em cache mesmo quando está sendo atualizado, melhorando a experiência do usuário.

---

### DOTRules:Disp-4---default-filter-deny-rules

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-4---default-filter-deny-rules |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem conter as regras `deny` padrão da versão 6.x.x do archetype AEM.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/filter {
  /0001 { /type "allow" /glob "*" }
}
```

#### Configuração Compliant:
```apache
/filter {
  # Deny everything first, then allow specific patterns
  /0001 { /type "deny" /glob "*" }
  
  # Allow access to client libraries
  /0002 { /type "allow" /extension '(css|gif|ico|js|png|swf|jpe?g)' }
  
  # Allow access to manifest files
  /0003 { /type "allow" /path "/etc.clientlibs/*" /extension '(json|txt|html|js|css)' }
  
  # Allow access to content
  /0004 { /type "allow" /path "/content/*" }
  
  # Deny access to sensitive paths
  /0005 { /type "deny" /path "/etc/*" }
  /0006 { /type "deny" /path "/system/*" }
  /0007 { /type "deny" /path "/tmp/*" }
}
```

---

### DOTRules:Disp-5---serveStaleOnError

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-5---serveStaleOnError |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: O cache do farm de publicação do Dispatcher deve ter serveStaleOnError habilitado.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /serveStaleOnError "0"
}
```

#### Configuração Compliant:
```apache
/cache {
  /serveStaleOnError "1"
}
```

**Explicação**: Quando habilitado, o Dispatcher serve conteúdo em cache mesmo quando o backend está indisponível, melhorando a disponibilidade do site.

---

### DOTRules:Disp-6---suffix-allow-list

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-6---suffix-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem especificar os padrões de sufixo Sling permitidos de forma allowlist.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/filter {
  /0001 { /type "allow" /suffix "*" }
}
```

#### Configuração Compliant:
```apache
/filter {
  # Allow specific suffixes only
  /0001 { /type "allow" /suffix "/jcr:content" }
  /0002 { /type "allow" /suffix "/jcr:content/*" }
  /0003 { /type "deny" /suffix "*" }
}
```

---

### DOTRules:Disp-7---selector-allow-list

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-7---selector-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem especificar os seletores Sling permitidos de forma allowlist.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/filter {
  /0001 { /type "allow" /selectors "*" }
}
```

#### Configuração Compliant:
```apache
/filter {
  # Allow specific selectors only
  /0001 { /type "allow" /selectors "(feed|rss|pages|languages|blueprint|infinity|tidy|sysview|docview|query|[0-9-]+|jcr:content)" }
  /0002 { /type "deny" /selectors "*" }
}
```

---

### DOTRules:Disp-8---unique-farm-name

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-8---unique-farm-name |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Cada farm do Dispatcher deve ter um nome único.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/farms {
  /publish {
    /clientheaders {
      "*"
    }
  }
  /publish {
    /clientheaders {
      "*"
    }
  }
}
```

#### Configuração Compliant:
```apache
/farms {
  /publish {
    /clientheaders {
      "*"
    }
  }
  /author {
    /clientheaders {
      "*"
    }
  }
}
```

---

## 🌐 Regras Apache (DOTRules:Httpd-*)

### DOTRules:Httpd-1---require-all-granted

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Httpd-1---require-all-granted |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A diretiva 'Require all granted' não deve ser usada em uma seção Directory do VirtualHost com um caminho de diretório raiz.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
<VirtualHost *:80>
  <Directory />
    Require all granted
  </Directory>
</VirtualHost>
```

#### Configuração Compliant:
```apache
<VirtualHost *:80>
  <Directory />
    Require all denied
  </Directory>
  
  <Directory "/var/www/html">
    Require all granted
  </Directory>
</VirtualHost>
```

**Explicação**: Usar 'Require all granted' no diretório raiz cria um risco de segurança, permitindo acesso a todos os arquivos do sistema.

---

### DOTRules:Httpd-S1---include-failed

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Httpd-S1---include-failed |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: A diretiva Include deve incluir arquivos existentes. Verifique o caminho ou use IncludeOptional.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
Include conf/non-existent-file.conf
```

#### Configuração Compliant:
```apache
# Use IncludeOptional para arquivos que podem não existir
IncludeOptional conf/optional-file.conf

# Ou certifique-se de que o arquivo existe
Include conf/existing-file.conf
```

---

## ⚙️ Regras de Sintaxe (DOTRules:Disp-S*, DOTRules:Syntax*)

### DOTRules:Disp-S1---brace-missing

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S1---brace-missing |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Cada seção deve começar com um caractere '{'.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/cache
  /docroot "/var/www/html"
}
```

#### Configuração Compliant:
```apache
/cache {
  /docroot "/var/www/html"
}
```

---

### DOTRules:Disp-S2---token-unexpected

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S2---token-unexpected |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Pulando token de nível superior desconhecido.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/farms {
  /publish {
    /invalidtoken "value"
  }
}
```

#### Configuração Compliant:
```apache
/farms {
  /publish {
    /clientheaders {
      "*"
    }
  }
}
```

---

### DOTRules:Disp-S3---quote-unmatched

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S3---quote-unmatched |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Aspas não correspondentes encontradas.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/docroot "/var/www/html
```

#### Configuração Compliant:
```apache
/docroot "/var/www/html"
```

---

### DOTRules:Disp-S4---brace-unclosed

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S4---brace-unclosed |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Chave não fechada encontrada.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /docroot "/var/www/html"
  /rules {
    /0001 { /glob "*" /type "allow"
  }
```

#### Configuração Compliant:
```apache
/cache {
  /docroot "/var/www/html"
  /rules {
    /0001 { /glob "*" /type "allow" }
  }
}
```

---

### DOTRules:Disp-S5---mandatory-missing

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S5---mandatory-missing |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Seção está faltando valor obrigatório.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/farms {
  /publish {
    # Missing mandatory /renders section
  }
}
```

#### Configuração Compliant:
```apache
/farms {
  /publish {
    /renders {
      /rend01 {
        /hostname "localhost"
        /port "4503"
      }
    }
  }
}
```

---

### DOTRules:Disp-S6---property-deprecated

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S6---property-deprecated |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Propriedade está deprecada.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /allowAuthorized "1"  # Deprecated property
}
```

#### Configuração Compliant:
```apache
/cache {
  /allowedClients {
    /0001 { /glob "*" /type "deny" }
    /0002 { /glob "127.0.0.1" /type "allow" }
  }
}
```

---

### DOTRules:Disp-S7---no-dispatcher-config

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S7---no-dispatcher-config |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Não foi possível encontrar arquivo de configuração do Dispatcher.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

**Solução**: Certifique-se de que o arquivo `dispatcher.any` existe no caminho correto e está acessível.

---

### DOTRules:Syntax0---syntax-violation

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Syntax0---syntax-violation |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |

**Descrição**: Problema de sintaxe encontrado.

**Categoria**: Dispatcher & Infrastructure  
**Fonte**: Documentação oficial Adobe Experience League + CSV

**Solução**: Verifique a sintaxe do arquivo de configuração do Dispatcher usando ferramentas de validação ou o Dispatcher Optimization Tool.

---

## 🚀 Regras CDN Fastly

### Configurações CDN Fastly - AEM Cloud Service

| Atributo | Valor |
|----------|-------|
| **Categoria** | CDN Fastly |
| **Tecnologia** | VCL, Edge Computing |
| **Foco** | Cache, Purge, Headers, Performance |

**Descrição**: Regras e configurações específicas para CDN Fastly no AEM Cloud Service, incluindo cache policies, purge strategies, e otimizações de performance.

#### Configuração CDN Fastly - Cache Control:
```vcl
# Exemplo de configuração VCL para Fastly
sub vcl_recv {
  # Cache control para assets estáticos
  if (req.url ~ "^/content/dam/.*\.(jpg|jpeg|png|gif|webp|svg)$") {
    set req.http.Cache-Control = "public, max-age=31536000";
  }
  
  # Cache control para CSS e JS
  if (req.url ~ "^/etc\.clientlibs/.*\.(css|js)$") {
    set req.http.Cache-Control = "public, max-age=31536000";
  }
  
  # Bypass cache para conteúdo dinâmico
  if (req.url ~ "^/content/.*/jcr:content\..*$") {
    set req.http.Cache-Control = "no-cache";
  }
}

sub vcl_deliver {
  # Adicionar headers de cache status
  if (obj.hits > 0) {
    set resp.http.X-Cache-Status = "HIT";
  } else {
    set resp.http.X-Cache-Status = "MISS";
  }
  
  # Adicionar headers de performance
  set resp.http.X-Served-By = "Fastly";
  set resp.http.X-Cache-Hits = obj.hits;
}
```

#### Configuração CDN Fastly - Purge Strategy:
```vcl
# Configuração de purge para AEM Cloud Service
sub vcl_recv {
  # Permitir purge apenas de IPs autorizados
  if (req.method == "PURGE") {
    if (!client.ip ~ purge_acl) {
      return (synth(405, "Method not allowed"));
    }
    return (purge);
  }
}

# ACL para IPs autorizados para purge
acl purge_acl {
  "192.168.1.0"/24;  # AEM Cloud Service IPs
  "10.0.0.0"/8;      # Internal network
}
```

#### Configuração CDN Fastly - Security Headers:
```vcl
sub vcl_deliver {
  # Security headers
  set resp.http.X-Frame-Options = "SAMEORIGIN";
  set resp.http.X-Content-Type-Options = "nosniff";
  set resp.http.X-XSS-Protection = "1; mode=block";
  set resp.http.Strict-Transport-Security = "max-age=31536000; includeSubDomains";
  
  # Remove headers que expõem informações do servidor
  unset resp.http.Server;
  unset resp.http.X-Powered-By;
  unset resp.http.X-Varnish;
}
```

#### Configuração CDN Fastly - Compression:
```vcl
sub vcl_recv {
  # Habilitar compressão para tipos de conteúdo específicos
  if (req.http.Accept-Encoding ~ "gzip") {
    if (req.url ~ "\.(css|js|html|xml|json)$") {
      set req.http.Accept-Encoding = "gzip";
    }
  }
}
```

---

## 📚 Referências Dispatcher, CDN Fastly & Infrastructure

### Documentação Oficial
- [Dispatcher Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-dispatcher/using/configuring/dispatcher-configuration)
- [CDN in AEM as a Cloud Service](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn)
- [Fastly CDN Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-credentials-authentication)
- [Dispatcher Optimization Tool](https://github.com/adobe/aem-dispatcher-optimizer-tool)
- [Apache HTTP Server Documentation](https://httpd.apache.org/docs/)
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)

### Ferramentas
- [Dispatcher Optimization Tool (DOT)](https://github.com/adobe/aem-dispatcher-optimizer-tool/blob/main/docs/Rules.md)
- [AEM Dispatcher Converter](https://github.com/adobe/aem-cloud-service-source-migration/tree/master/packages/dispatcher-converter)
- [Fastly VCL Documentation](https://docs.fastly.com/en/guides/guide-to-vcl)
- [AEM CDN Cache Purging](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/caching)

### Configuração e Otimização
- [Dispatcher Performance Tuning](https://experienceleague.adobe.com/en/docs/experience-manager-dispatcher/using/configuring/dispatcher-configuration#performance-tuning)
- [CDN Performance Best Practices](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-performance)
- [Security Configuration for Dispatcher](https://experienceleague.adobe.com/en/docs/experience-manager-dispatcher/using/configuring/security-checklist)

### Arquivos CSV de Referência
- `CodeQuality-rules-latest-AMS-2024-12-0.csv` - Versão mais recente (SonarQube 9.9)
- `CodeQuality-rules-latest-AMS.csv` - Versão anterior

---

*Última atualização: 14 de Dezembro de 2025*  
*Gerado via MCP AEM Documentation + análise de CSVs*  
*Categoria: Dispatcher, CDN Fastly & Infrastructure Rules*