# Regras Dispatcher, CDN Fastly e Infraestrutura - AEM Cloud Service

**Última atualização:** 14 de dezembro de 2025
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
| **Total Regras Dispatcher & CDN** | 16 |
| **Regras Dispatcher (DOTRules:Disp-*)** | 8 |
| **Regras Apache (DOTRules:Httpd-*)** | 2 |
| **Regras Sintaxe (DOTRules:Disp-S*, DOTRules:Syntax*)** | 6 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Major** | 16 |
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

## 📋 Checklist de Implementação Dispatcher

### ✅ Checklist de Regras SonarQube

- [ ] **DOTRules:Disp-1**: ignoreUrlParams configurado em allowlist
- [ ] **DOTRules:Disp-2**: statfileslevel >= 2
- [ ] **DOTRules:Disp-3**: gracePeriod >= 2
- [ ] **DOTRules:Disp-4**: Filtros com regras deny padrão
- [ ] **DOTRules:Disp-5**: serveStaleOnError habilitado
- [ ] **DOTRules:Disp-6**: Sufixos Sling em allowlist
- [ ] **DOTRules:Disp-7**: Seletores Sling em allowlist
- [ ] **DOTRules:Disp-8**: Nomes de farm únicos
- [ ] **DOTRules:Httpd-1**: Não usar 'Require all granted' em raiz
- [ ] **DOTRules:Httpd-S1**: Arquivos de include existem
- [ ] **DOTRules:Disp-S1-S7**: Sintaxe correta em configurações
- [ ] **DOTRules:Syntax0**: Sem violações de sintaxe

### ✅ Checklist de Validação

- [ ] Configurações testadas em ambiente de desenvolvimento
- [ ] Logs do Dispatcher verificados sem erros
- [ ] Performance de cache validada
- [ ] Segurança testada com ferramentas de scan
- [ ] Documentação atualizada

---

## 🚨 Troubleshooting Comum

### Problemas de Cache (DOTRules:Disp-1, Disp-2, Disp-5)

**Sintoma**: Conteúdo não sendo cached adequadamente
**Soluções**:
1. Verificar ignoreUrlParams em allowlist
2. Confirmar statfileslevel >= 2
3. Validar serveStaleOnError habilitado
4. Checar logs de invalidação

### Problemas de Segurança (DOTRules:Disp-4, Disp-6, Disp-7, Httpd-1)

**Sintoma**: Acesso não autorizado ou exposição de dados
**Soluções**:
1. Revisar filtros deny-first
2. Verificar allowlist de sufixos e seletores
3. Confirmar Directory permissions
4. Testar com ferramentas de segurança

### Problemas de Sintaxe (DOTRules:Disp-S*, Syntax0)

**Sintoma**: Dispatcher não inicia ou comportamento inesperado
**Soluções**:
1. Validar sintaxe com dispatcher -t
2. Verificar chaves e aspas balanceadas
3. Confirmar propriedades obrigatórias
4. Remover propriedades depreciadas

---

## 📚 Referências Dispatcher, CDN Fastly & Infrastructure

### Documentação Oficial
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Dispatcher Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-dispatcher/using/configuring/dispatcher-configuration)
- [CDN in AEM as a Cloud Service](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn)
- [Fastly CDN Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-credentials-authentication)

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
*Foco: Regras SonarQube específicas para Dispatcher e CDN*