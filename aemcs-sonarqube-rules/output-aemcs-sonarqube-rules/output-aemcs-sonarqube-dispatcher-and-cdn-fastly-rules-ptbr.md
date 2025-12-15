 2025
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
| **Total Regras Dispatcher & CDN** | 21 |
| **Regras Dispatcher (DOTRules:Disp-*)** | 8 |
| **Regras Apache (DOTRules:Httpd-*)** | 1 |
| **Regras Sintaxe (DOTRules:Disp-S*, DOTRules:Syntax*)** | 8 |
| **Regras CDN Fastly** | 4 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Major** | 21 |
| **Minor** | 0 |

---

## 🔧 Regras Dispatcher (DOTRules:Disp-*)

### DOTRules:Disp-1---ignoreUrlParams-allow-list - Cache deve usar allowlist para ignoreUrlParams

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-1---ignoreUrlParams-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: O cache do farm de publicação do Dispatcher deve ter suas regras `ignoreUrlParams` configuradas de forma allowlist. Esta abordagem é mais segura pois permite apenas parâmetros específicos conhecidos, em vez de negar parâmetros específicos e permitir todos os outros.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

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

### DOTRules:Disp-2---statfileslevel - Propriedade statfileslevel deve ser >= 2

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-2---statfileslevel |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: A propriedade `statfileslevel` do cache do farm de publicação do Dispatcher deve ser >= 2. Isso garante invalidação de cache mais eficiente e granular, especialmente importante para sites com estruturas de conteúdo profundas.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /statfileslevel "1"
  /docroot "/var/www/html"
}
```

#### Configuração Compliant:
```apache
/cache {
  /statfileslevel "2"
  /docroot "/var/www/html"
}
```

### DOTRules:Disp-3---gracePeriod - Propriedade gracePeriod deve ser >= 2

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-3---gracePeriod |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: A propriedade `gracePeriod` do farm de publicação do Dispatcher deve ser >= 2. Isso permite um período de carência adequado para revalidação de cache, melhorando a performance e reduzindo a carga no servidor de publicação.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

#### Configuração Non-compliant:
```apache
/farm {
  /gracePeriod "1"
  /cache { ... }
}
```

#### Configuração Compliant:
```apache
/farm {
  /gracePeriod "2"
  /cache { ... }
}
```

### DOTRules:Disp-4---default-filter-deny-rules - Filtros devem conter regras deny padrão

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-4---default-filter-deny-rules |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem conter as regras `deny` padrão da versão 6.x.x do arquétipo AEM. Essas regras são essenciais para segurança, bloqueando acesso a caminhos sensíveis e arquivos de sistema.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

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
  /0002 { /type "allow" /glob "/etc/clientlibs/*" }
  
  # Allow access to content
  /0003 { /type "allow" /glob "/content/*" }
  
  # Deny access to system paths
  /0004 { /type "deny" /glob "/system/*" }
  /0005 { /type "deny" /glob "/apps/*" }
  /0006 { /type "deny" /glob "/libs/*" }
}
```

### DOTRules:Disp-5---serveStaleOnError - Cache deve ter serveStaleOnError habilitado

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-5---serveStaleOnError |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: O cache do farm de publicação do Dispatcher deve ter `serveStaleOnError` habilitado. Isso permite servir conteúdo em cache mesmo quando o servidor de origem está indisponível, melhorando a disponibilidade do site.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

#### Configuração Non-compliant:
```apache
/cache {
  /serveStaleOnError "0"
  /docroot "/var/www/html"
}
```

#### Configuração Compliant:
```apache
/cache {
  /serveStaleOnError "1"
  /docroot "/var/www/html"
}
```

### DOTRules:Disp-6---suffix-allow-list - Filtros devem especificar padrões de sufixo Sling em allowlist

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-6---suffix-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem especificar os padrões de sufixo Sling permitidos de forma allowlist. Isso previne ataques através de sufixos maliciosos e melhora a segurança.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

#### Configuração Non-compliant:
```apache
/filter {
  /0001 { /type "allow" /glob "* *.html*" }
}
```

#### Configuração Compliant:
```apache
/filter {
  # Allow specific suffixes only
  /0001 { /type "allow" /glob "*.html" }
  /0002 { /type "allow" /glob "*.json" }
  /0003 { /type "allow" /glob "*.xml" }
  /0004 { /type "deny" /glob "* *.*" }
}
```

### DOTRules:Disp-7---selector-allow-list - Filtros devem especificar seletores Sling em allowlist

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-7---selector-allow-list |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Os filtros do farm de publicação do Dispatcher devem especificar os seletores Sling permitidos de forma allowlist. Isso previne acesso não autorizado através de seletores maliciosos e melhora a segurança.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

#### Configuração Non-compliant:
```apache
/filter {
  /0001 { /type "allow" /glob "*.*.*" }
}
```

#### Configuração Compliant:
```apache
/filter {
  # Allow specific selectors only
  /0001 { /type "allow" /glob "*.feed.xml" }
  /0002 { /type "allow" /glob "*.rss.xml" }
  /0003 { /type "allow" /glob "*.sitemap.xml" }
  /0004 { /type "deny" /glob "*.*.*" }
}
```

### DOTRules:Disp-8---unique-farm-name - Cada farm Dispatcher deve ter nome único

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-8---unique-farm-name |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Cada farm do Dispatcher deve ter um nome único. Nomes duplicados podem causar conflitos de configuração e comportamento imprevisível do Dispatcher.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

#### Configuração Non-compliant:
```apache
/farms {
  /publish {
    /clientheaders { ... }
  }
  /publish {  # DUPLICATE NAME!
    /clientheaders { ... }
  }
}
```

#### Configuração Compliant:
```apache
/farms {
  /publish {
    /clientheaders { ... }
  }
  /author {
    /clientheaders { ... }
  }
}
```

---

## 🌐 Regras Apache (DOTRules:Httpd-*)

### DOTRules:Httpd-1---require-all-granted - Não usar 'Require all granted' em diretório raiz

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Httpd-1---require-all-granted |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: A diretiva 'Require all granted' não deve ser usada em uma seção Directory do VirtualHost com um caminho de diretório raiz. Isso representa um risco de segurança significativo, permitindo acesso irrestrito ao sistema de arquivos.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: Adobe Experience League (documentação oficial) + CSV

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

---

## ⚙️ Regras de Sintaxe Dispatcher (DOTRules:Disp-S*, DOTRules:Syntax*)

### DOTRules:Disp-S1---brace-missing - Seções devem começar com '{'

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S1---brace-missing |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Cada seção deve começar com um caractere '{'. Erro de sintaxe básico que impede o Dispatcher de funcionar corretamente.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

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

### DOTRules:Disp-S2---token-unexpected - Token inesperado no nível superior

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S2---token-unexpected |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Ignorando token desconhecido no nível superior. Indica presença de configuração inválida ou mal posicionada.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

### DOTRules:Disp-S3---quote-unmatched - Aspas não fechadas

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S3---quote-unmatched |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Aspas não fechadas encontradas. Erro de sintaxe que pode causar interpretação incorreta da configuração.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```apache
/docroot "/var/www/html
```

#### Configuração Compliant:
```apache
/docroot "/var/www/html"
```

### DOTRules:Disp-S4---brace-unclosed - Chave não fechada

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S4---brace-unclosed |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Chave não fechada encontrada. Erro de sintaxe que impede o parsing correto da configuração.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```apache
/cache {
  /docroot "/var/www/html"
  /rules {
    /0001 { /glob "*" /type "allow" }
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

### DOTRules:Disp-S5---mandatory-missing - Propriedade obrigatória ausente

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S5---mandatory-missing |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Seção está faltando valor obrigatório. Configurações incompletas podem causar falhas no Dispatcher.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```apache
/farm {
  /cache {
    # Missing mandatory /docroot property
  }
}
```

#### Configuração Compliant:
```apache
/farm {
  /cache {
    /docroot "/var/www/html"
  }
}
```

### DOTRules:Disp-S6---property-deprecated - Propriedade depreciada

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S6---property-deprecated |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Propriedade está depreciada. Uso de configurações obsoletas pode causar problemas de compatibilidade.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

### DOTRules:Disp-S7---no-dispatcher-config - Arquivo de configuração Dispatcher não encontrado

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Disp-S7---no-dispatcher-config |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Não foi possível encontrar arquivo de configuração do Dispatcher. O Dispatcher requer um arquivo de configuração válido para funcionar.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

### DOTRules:Syntax0---syntax-violation - Problema de sintaxe encontrado

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Syntax0---syntax-violation |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Problema de sintaxe encontrado. Erros de sintaxe gerais que impedem o funcionamento correto do Dispatcher.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

---

## 🚀 Regras CDN Fastly

### Configurações CDN Fastly - AEM Cloud Service

| Atributo | Valor |
|----------|-------|
| **Categoria** | CDN Fastly |
| **Tecnologia** | VCL, Edge Computing |
| **Foco** | Cache, Purge, Headers, Performance |

**Descrição**: AEM Cloud Service utiliza Fastly como CDN padrão, fornecendo cache de borda, otimização de performance e proteção DDoS. As configurações CDN são gerenciadas através de arquivos VCL e políticas de cache.

#### Configuração CDN Fastly - Cache Headers:
```vcl
# Configuração VCL para headers de cache
sub vcl_recv {
  # Cache control para assets estáticos
  if (req.url ~ "^/content/dam/.*\.(jpg|jpeg|png|gif|webp|svg)$") {
    set req.http.Cache-Control = "public, max-age=31536000";
  }
  
  # Cache control para CSS/JS
  if (req.url ~ "^/etc/clientlibs/.*\.(css|js)$") {
    set req.http.Cache-Control = "public, max-age=86400";
  }
}

sub vcl_deliver {
  # Adicionar headers de cache status
  set resp.http.X-Cache-Status = "HIT";
  set resp.http.X-Cache-Age = resp.http.Age;
}
```

### Configuração CDN Fastly - Purge Strategy

**Descrição**: Estratégias de purge para invalidação de cache no Fastly CDN, essenciais para manter conteúdo atualizado.

#### Configuração de Purge:
```vcl
# Configuração de purge por tags
sub vcl_recv {
  if (req.method == "PURGE") {
    if (!client.ip ~ purge_acl) {
      return (synth(405, "Method not allowed"));
    }
    return (purge);
  }
}

# Configuração de surrogate keys para purge seletivo
sub vcl_deliver {
  set resp.http.Surrogate-Key = "content-" + req.url;
}
```

### Configuração CDN Fastly - Security Headers

**Descrição**: Configuração de headers de segurança através do Fastly CDN para proteção adicional.

#### Configuração de Security Headers:
```vcl
sub vcl_deliver {
  # Security headers
  set resp.http.X-Frame-Options = "SAMEORIGIN";
  set resp.http.X-Content-Type-Options = "nosniff";
  set resp.http.X-XSS-Protection = "1; mode=block";
  set resp.http.Strict-Transport-Security = "max-age=31536000; includeSubDomains";
  
  # Remove server information
  unset resp.http.Server;
  unset resp.http.X-Powered-By;
}
```

### Configuração CDN Fastly - Performance Optimization

**Descrição**: Otimizações de performance através do Fastly CDN, incluindo compressão e otimização de imagens.

#### Configuração de Performance:
```vcl
sub vcl_recv {
  # Enable compression for text content
  if (req.http.Accept-Encoding ~ "gzip") {
    set req.http.Accept-Encoding = "gzip";
  }
  
  # Image optimization
  if (req.url ~ "\.(jpg|jpeg|png|webp)$") {
    set req.http.X-Image-Optimize = "true";
  }
}

sub vcl_backend_response {
  # Set cache TTL based on content type
  if (beresp.http.Content-Type ~ "^(text|application)/(css|javascript|json)") {
    set beresp.ttl = 1d;
  }
  
  if (beresp.http.Content-Type ~ "^image/") {
    set beresp.ttl = 7d;
  }
}
```

---

## 📚 Referências Dispatcher, CDN Fastly & Infrastructure

### Documentação Oficial
- [Dispatcher Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-dispatcher/using/configuring/dispatcher-configuration)
- [CDN in AEM as a Cloud Service](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn)
- [Fastly CDN Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-credentials-authentication)
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Apache HTTP Server Documentation](https://httpd.apache.org/docs/)

### Ferramentas
- [Dispatcher Optimization Tool (DOT)](https://github.com/adobe/aem-dispatcher-optimizer-tool/blob/main/docs/Rules.md)
- [AEM Dispatcher Converter](https://github.com/adobe/aem-cloud-service-source-migration/tree/master/packages/dispatcher-converter)
- [Fastly VCL Documentation](https://docs.fastly.com/en/guides/guide-to-vcl)
- [AEM CDN Cache Purging](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/caching)

### Arquivos de Regras
- CodeQuality-rules-latest-AMS-2024-12-0.csv (versão mais recente)
- CodeQuality-rules-latest-AMS.csv (versão anterior)

---

*Última atualização: 14 de dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Dispatcher, CDN Fastly & Infrastructure Rules*

### DOTRules:Httpd-S1---include-failed - Arquivo de include não encontrado

| Atributo | Valor |
|----------|-------|
| **Key** | DOTRules:Httpd-S1---include-failed |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, dispatcher |
| **Since** | Version 2024.12.0 |

**Descrição**: Diretiva Include deve incluir arquivos existentes. Verificar caminho ou usar IncludeOptional.

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```apache
# Arquivo não existe
Include /etc/httpd/conf.d/nonexistent.conf
```

#### Configuração Compliant:
```apache
# Usar IncludeOptional para arquivos opcionais
IncludeOptional /etc/httpd/conf.d/optional.conf

# Ou garantir que arquivo existe
Include /etc/httpd/conf.d/existing.conf
```

---

## 🔒 Regras de Segurança Dispatcher

### Configurações de Segurança - Filtros de Proteção

| Atributo | Valor |
|----------|-------|
| **Categoria** | Security & Protection |
| **Tecnologia** | Dispatcher Filters |
| **Foco** | Path Traversal, XSS, CSRF Protection |

**Descrição**: Implementar filtros de segurança robustos para proteger contra ataques comuns como path traversal, XSS e CSRF.

#### Configuração de Filtros de Segurança:
```apache
/filter {
  # Negar tudo por padrão
  /0001 { /type "deny" /glob "*" }
  
  # Permitir apenas extensões seguras
  /0010 { /type "allow" /glob "*.html" }
  /0011 { /type "allow" /glob "*.json" }
  /0012 { /type "allow" /glob "*.xml" }
  /0013 { /type "allow" /glob "*.css" }
  /0014 { /type "allow" /glob "*.js" }
  /0015 { /type "allow" /glob "*.png" }
  /0016 { /type "allow" /glob "*.jpg" }
  /0017 { /type "allow" /glob "*.gif" }
  /0018 { /type "allow" /glob "*.svg" }
  /0019 { /type "allow" /glob "*.woff*" }
  
  # Bloquear path traversal
  /0020 { /type "deny" /glob "*../*" }
  /0021 { /type "deny" /glob "*/.*" }
  /0022 { /type "deny" /glob "*/..*" }
  
  # Bloquear acesso a arquivos de sistema
  /0030 { /type "deny" /glob "*/bin/*" }
  /0031 { /type "deny" /glob "*/etc/*" }
  /0032 { /type "deny" /glob "*/home/*" }
  /0033 { /type "deny" /glob "*/tmp/*" }
  /0034 { /type "deny" /glob "*/var/*" }
  /0035 { /type "deny" /glob "*/proc/*" }
  
  # Bloquear acesso a caminhos AEM sensíveis
  /0040 { /type "deny" /glob "/system/*" }
  /0041 { /type "deny" /glob "/apps/*" }
  /0042 { /type "deny" /glob "/libs/*" }
  /0043 { /type "deny" /glob "/bin/*" }
  /0044 { /type "deny" /glob "/services/*" }
  /0045 { /type "deny" /glob "/crx/*" }
  
  # Permitir conteúdo público
  /0050 { /type "allow" /glob "/content/*" }
  /0051 { /type "allow" /glob "/etc/clientlibs/*" }
  /0052 { /type "allow" /glob "/etc/designs/*" }
}
```

### Configurações de Headers de Segurança

| Atributo | Valor |
|----------|-------|
| **Categoria** | Security Headers |
| **Tecnologia** | Apache HTTP Server |
| **Foco** | XSS, Clickjacking, MIME Sniffing Protection |

**Descrição**: Configurar headers de segurança essenciais no Apache para proteção adicional.

#### Configuração de Security Headers:
```apache
<VirtualHost *:80>
  # Headers de segurança básicos
  Header always set X-Frame-Options "SAMEORIGIN"
  Header always set X-Content-Type-Options "nosniff"
  Header always set X-XSS-Protection "1; mode=block"
  Header always set Referrer-Policy "strict-origin-when-cross-origin"
  
  # Content Security Policy
  Header always set Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; font-src 'self' data:; connect-src 'self'"
  
  # HSTS (apenas para HTTPS)
  # Header always set Strict-Transport-Security "max-age=31536000; includeSubDomains"
  
  # Remover headers que expõem informações
  Header unset Server
  Header unset X-Powered-By
  ServerTokens Prod
</VirtualHost>
```

---

## ⚡ Regras de Performance Dispatcher

### Configurações de Cache Avançadas

| Atributo | Valor |
|----------|-------|
| **Categoria** | Cache Performance |
| **Tecnologia** | Dispatcher Cache |
| **Foco** | TTL, Invalidation, Cache Efficiency |

**Descrição**: Configurações avançadas de cache para maximizar performance e eficiência do Dispatcher.

#### Configuração de Cache Otimizada:
```apache
/cache {
  /docroot "/var/www/html"
  /statfileslevel "2"
  /serveStaleOnError "1"
  /allowAuthorized "0"
  /enableTTL "1"
  
  # Regras de cache por tipo de conteúdo
  /rules {
    # Cache páginas HTML por 1 hora
    /0001 {
      /glob "*.html"
      /type "allow"
      /headers "Cache-Control: max-age=3600"
    }
    
    # Cache assets estáticos por 1 ano
    /0002 {
      /glob "*.{css,js,png,jpg,gif,svg,woff,woff2}"
      /type "allow"
      /headers "Cache-Control: max-age=31536000, immutable"
    }
    
    # Não fazer cache de conteúdo dinâmico
    /0003 {
      /glob "*.json"
      /type "deny"
    }
  }
  
  # Configuração de invalidação
  /invalidate {
    /0001 { /glob "*" /type "deny" }
    /0002 { /glob "*.html" /type "allow" }
    /0003 { /glob "/etc/clientlibs/*" /type "allow" }
  }
  
  # Headers permitidos para cache
  /allowedClients {
    /0001 { /glob "*" /type "deny" }
    /0002 { /glob "127.0.0.1" /type "allow" }
    /0003 { /glob "localhost" /type "allow" }
  }
}
```

### Configurações de Compressão

| Atributo | Valor |
|----------|-------|
| **Categoria** | Compression & Bandwidth |
| **Tecnologia** | Apache mod_deflate |
| **Foco** | Bandwidth Optimization, Loading Speed |

**Descrição**: Configurar compressão adequada para reduzir uso de bandwidth e melhorar velocidade de carregamento.

#### Configuração de Compressão:
```apache
# Habilitar mod_deflate
LoadModule deflate_module modules/mod_deflate.so

<Location />
  # Comprimir tipos de arquivo texto
  SetOutputFilter DEFLATE
  SetEnvIfNoCase Request_URI \
    \.(?:gif|jpe?g|png|ico|zip|gz|bz2|rar|7z)$ no-gzip dont-vary
  SetEnvIfNoCase Request_URI \
    \.(?:exe|t?gz|zip|bz2|sit|rar|7z)$ no-gzip dont-vary
  
  # Comprimir apenas arquivos maiores que 1KB
  DeflateFilterNote Input instream
  DeflateFilterNote Output outstream
  DeflateFilterNote Ratio ratio
  
  # Configurar nível de compressão
  DeflateCompressionLevel 6
  
  # Tipos MIME para compressão
  AddOutputFilterByType DEFLATE text/plain
  AddOutputFilterByType DEFLATE text/html
  AddOutputFilterByType DEFLATE text/xml
  AddOutputFilterByType DEFLATE text/css
  AddOutputFilterByType DEFLATE text/javascript
  AddOutputFilterByType DEFLATE application/xml
  AddOutputFilterByType DEFLATE application/xhtml+xml
  AddOutputFilterByType DEFLATE application/rss+xml
  AddOutputFilterByType DEFLATE application/javascript
  AddOutputFilterByType DEFLATE application/x-javascript
  AddOutputFilterByType DEFLATE application/json
</Location>
```

---

## 🌍 Regras CDN Fastly Avançadas

### Configuração de Edge Side Includes (ESI)

| Atributo | Valor |
|----------|-------|
| **Categoria** | CDN Advanced Features |
| **Tecnologia** | Fastly ESI, VCL |
| **Foco** | Dynamic Content, Personalization |

**Descrição**: Configurar Edge Side Includes para conteúdo dinâmico e personalização no edge.

#### Configuração ESI:
```vcl
sub vcl_recv {
  # Habilitar ESI para páginas específicas
  if (req.url ~ "^/content/.*\.html$") {
    set req.http.X-ESI = "1";
  }
  
  # Configurar headers para ESI
  if (req.http.X-ESI) {
    set req.http.Surrogate-Capability = "abc=ESI/1.0";
  }
}

sub vcl_backend_response {
  # Processar ESI se habilitado
  if (beresp.http.Surrogate-Control ~ "ESI/1.0") {
    set beresp.do_esi = true;
  }
  
  # Configurar TTL para conteúdo ESI
  if (beresp.http.Content-Type ~ "text/html") {
    set beresp.ttl = 300s;  # 5 minutos
    set beresp.grace = 1h;
  }
}

sub vcl_deliver {
  # Remover headers ESI do cliente
  unset resp.http.Surrogate-Control;
  unset resp.http.X-ESI-Parser;
}
```

### Configuração de Geolocation e A/B Testing

| Atributo | Valor |
|----------|-------|
| **Categoria** | CDN Personalization |
| **Tecnologia** | Fastly Geolocation, VCL |
| **Foco** | Geographic Targeting, Testing |

**Descrição**: Implementar geolocation e A/B testing usando recursos do Fastly CDN.

#### Configuração de Geolocation:
```vcl
sub vcl_recv {
  # Adicionar informações de geolocalização
  set req.http.X-Country-Code = geoip.country_code;
  set req.http.X-Region = geoip.region;
  set req.http.X-City = geoip.city.utf8;
  
  # Roteamento baseado em localização
  if (geoip.country_code == "BR") {
    set req.backend = brazil_backend;
  } elsif (geoip.country_code ~ "^(US|CA)$") {
    set req.backend = americas_backend;
  } else {
    set req.backend = global_backend;
  }
  
  # A/B Testing baseado em hash do IP
  declare local var.ab_test STRING;
  set var.ab_test = digest.hash_sha256(client.ip + "salt");
  if (std.atoi(substr(var.ab_test, 0, 1)) < 5) {
    set req.http.X-AB-Test = "A";
  } else {
    set req.http.X-AB-Test = "B";
  }
}

sub vcl_backend_request {
  # Passar headers de geolocalização para o backend
  set bereq.http.X-Country-Code = req.http.X-Country-Code;
  set bereq.http.X-AB-Test = req.http.X-AB-Test;
}

sub vcl_deliver {
  # Adicionar headers informativos (apenas para debug)
  if (req.http.X-Debug == "1") {
    set resp.http.X-Country = req.http.X-Country-Code;
    set resp.http.X-AB-Test = req.http.X-AB-Test;
  }
}
```

---

## 🔧 Regras de Monitoramento e Logging

### Configuração de Logging Avançado

| Atributo | Valor |
|----------|-------|
| **Categoria** | Monitoring & Observability |
| **Tecnologia** | Apache Logging, Dispatcher |
| **Foco** | Performance Monitoring, Debugging |

**Descrição**: Configurar logging detalhado para monitoramento de performance e debugging.

#### Configuração de Logging:
```apache
# Log format customizado para performance
LogFormat "%h %l %u %t \"%r\" %>s %O \"%{Referer}i\" \"%{User-Agent}i\" %D %{X-Cache-Status}o %{X-Backend}o" combined_perf

# Logs separados por tipo
CustomLog logs/access.log combined_perf
CustomLog logs/cache.log "%t [%{X-Cache-Status}o] %U %q (%{Content-Length}o bytes) %D ms"
CustomLog logs/errors.log "%t [ERROR] %{error-notes}n"

# Log condicional para debugging
SetEnvIf Request_URI "\.(?:css|js|png|jpg|gif|ico|woff)$" static_asset
CustomLog logs/static.log combined_perf env=static_asset

# Dispatcher logging
DispatcherLog logs/dispatcher.log
DispatcherLogLevel 3

# Rotação de logs
<IfModule mod_log_rotate.c>
  RotateLogs On
  RotateLogsLocalTime On
  RotateInterval 86400
  RotateSize 100M
</IfModule>
```

### Health Checks e Monitoring

| Atributo | Valor |
|----------|-------|
| **Categoria** | Health Monitoring |
| **Tecnologia** | Apache, Dispatcher |
| **Foco** | Availability, Performance Metrics |

**Descrição**: Implementar health checks e endpoints de monitoramento para observabilidade.

#### Configuração de Health Checks:
```apache
# Endpoint de health check
<Location "/health">
  SetHandler server-status
  Require local
  Require ip 10.0.0.0/8
  Require ip 172.16.0.0/12
  Require ip 192.168.0.0/16
</Location>

# Status detalhado do servidor
<Location "/server-status">
  SetHandler server-status
  ExtendedStatus On
  Require local
</Location>

# Informações do servidor
<Location "/server-info">
  SetHandler server-info
  Require local
</Location>

# Métricas customizadas
<Location "/metrics">
  SetHandler application/json
  Header set Content-Type "application/json"
  
  # Script customizado para métricas
  RewriteEngine On
  RewriteRule ^/metrics$ /bin/metrics.sh [L,E=no-gzip:1]
</Location>
```

---

## 📋 Checklist de Implementação Dispatcher

### ✅ Checklist de Segurança

- [ ] Filtros configurados com deny-first approach
- [ ] Path traversal bloqueado (../, ./, etc.)
- [ ] Acesso a caminhos sensíveis negado (/system, /apps, /libs)
- [ ] Headers de segurança configurados (X-Frame-Options, CSP, etc.)
- [ ] Informações do servidor removidas (Server, X-Powered-By)
- [ ] HTTPS configurado com HSTS
- [ ] Certificados SSL válidos e atualizados

### ✅ Checklist de Performance

- [ ] Cache configurado com statfileslevel >= 2
- [ ] serveStaleOnError habilitado
- [ ] gracePeriod >= 2 configurado
- [ ] ignoreUrlParams em allowlist
- [ ] Compressão habilitada para conteúdo texto
- [ ] TTL apropriado para diferentes tipos de conteúdo
- [ ] Invalidação de cache configurada corretamente

### ✅ Checklist de Monitoramento

- [ ] Logging detalhado configurado
- [ ] Health checks implementados
- [ ] Métricas de performance coletadas
- [ ] Alertas configurados para falhas
- [ ] Rotação de logs implementada
- [ ] Dashboards de monitoramento criados

---

## 🚨 Troubleshooting Comum

### Problemas de Cache

**Sintoma**: Conteúdo não sendo cached
**Soluções**:
1. Verificar configuração de /cache/rules
2. Confirmar headers Cache-Control
3. Validar filtros de cache
4. Checar permissões do diretório docroot

**Sintoma**: Cache não invalidando
**Soluções**:
1. Verificar configuração /invalidate
2. Confirmar agentes de replicação
3. Validar statfileslevel
4. Checar logs de invalidação

### Problemas de Segurança

**Sintoma**: Acesso negado a recursos válidos
**Soluções**:
1. Revisar ordem dos filtros
2. Verificar padrões glob
3. Confirmar allowlist vs denylist
4. Testar com logs detalhados

### Problemas de Performance

**Sintoma**: Lentidão no carregamento
**Soluções**:
1. Verificar compressão
2. Otimizar TTL de cache
3. Revisar configuração de backends
4. Analisar logs de performance

---

*Última atualização: 14 de dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Dispatcher, CDN Fastly & Infrastructure Rules*