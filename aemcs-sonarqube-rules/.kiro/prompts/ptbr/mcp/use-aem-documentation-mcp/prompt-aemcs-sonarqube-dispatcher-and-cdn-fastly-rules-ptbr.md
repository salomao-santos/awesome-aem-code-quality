# 🚨 PROMPT: Gerar Regras Dispatcher & CDN Fastly - AEM SonarQube

## ⚠️ ATENÇÃO: USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION

**🔴 ESTE PROMPT REQUER O USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION**

**VOCÊ DEVE**:
1. ✅ Configurar o MCP AEM Documentation **ANTES** de qualquer outra ação
2. ✅ Ler a documentação oficial via MCP **POR PARTES** (documentos grandes > 1000 linhas)
3. ✅ **FILTRAR APENAS REGRAS DISPATCHER** durante a leitura
4. ✅ Comparar com CSVs e **ADICIONAR REGRAS DISPATCHER NOVAS**
5. ✅ **GERAR APENAS 1 ARQUIVO**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md`

**🚫 NÃO É PERMITIDO**:
- ❌ Pular o uso do MCP
- ❌ Usar apenas os arquivos CSV
- ❌ Incluir regras que não são Dispatcher/Infrastructure
- ❌ Gerar múltiplos arquivos

---

## 🎯 Objetivo

Gerar **1 documento técnico específico** sobre as regras Dispatcher, CDN Fastly e infraestrutura de qualidade de código do Cloud Manager para Adobe Experience Manager as a Cloud Service (AEMaaCS).

## 📁 ARQUIVO DE OUTPUT

**Arquivo**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md`
- **Regras**: `DOTRules:*` (Dispatcher Optimization Tool), CDN Fastly
- **Foco**: Configuração Dispatcher, Apache, CDN Fastly, cache, filtros, farms, sintaxe
- **Tecnologias**: `.any`, `.rules`, `.vhost`, `.conf`, `.farm`, CDN configs
- **Mínimo**: 200 linhas

---

## 🔍 CRITÉRIOS DE FILTRO DISPATCHER

### ✅ **INCLUIR APENAS SE**:

#### 📂 **Prefixos Dispatcher Exatos**:
- `DOTRules:Disp-*` (regras Dispatcher)
- `DOTRules:Httpd-*` (regras Apache)
- `DOTRules:Syntax*` (regras de sintaxe)
- `DOTRules:Disp-S*` (regras de sintaxe Dispatcher)

#### 🔑 **Palavras-chave Dispatcher & CDN**:
- dispatcher, cache, filter, farm, apache
- httpd, vhost, configuration, mod_dispatcher
- ignoreUrlParams, statfileslevel, gracePeriod
- serveStaleOnError, allowedClients, renders
- fastly, cdn, edge, purge, vcl

#### 💻 **Tecnologias Dispatcher & CDN**:
- .any (arquivos de configuração Dispatcher)
- .rules (regras de filtro)
- .vhost (virtual hosts Apache)
- .conf (configuração Apache)
- .farm (definições de farm)
- .vcl (Fastly VCL configurations)
- CDN configurations (Fastly)

### ❌ **EXCLUIR SEMPRE**:
- Regras Java (`java:S*`, `AEM Rules:*`, `CQRules:*`)
- Regras de Content (`BannedPath`, `PackageOverlaps`)
- Regras de UI/Frontend (`ClassicUI*`, `Component*`)

---

## 📖 FASE 1: LEITURA MCP COM FILTRO DISPATCHER

### 🚨 INSTRUÇÃO CRÍTICA: LEITURA POR PARTES + FILTRO

**A documentação oficial tem mais de 1000 linhas. VOCÊ DEVE:**

1. **Primeira leitura** - Início do documento:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=0
)
```

2. **Para cada parte lida**:
   - **FILTRAR** apenas regras Dispatcher (prefixos + palavras-chave)
   - **ESCREVER** imediatamente no arquivo Dispatcher
   - **IGNORAR** regras de outras categorias

3. **Continuar lendo** até documento completo

### 📝 PROCESSO DE FILTRO POR PARTE:

```markdown
# Para cada parte lida do MCP:

1. IDENTIFICAR regras Dispatcher:
   - Prefixo DOTRules:Disp-* → INCLUIR
   - Prefixo DOTRules:Httpd-* → INCLUIR  
   - Prefixo DOTRules:Syntax* → INCLUIR
   - Palavra-chave "dispatcher" → INCLUIR
   - Prefixo java:S* → EXCLUIR
   - Prefixo BannedPath → EXCLUIR

2. ESCREVER no arquivo Dispatcher:
   - Apenas regras identificadas como Dispatcher
   - Com exemplos de configuração .any/.conf
   - Com descrições detalhadas

3. CONTINUAR para próxima parte
```

### ✅ CHECKLIST DE LEITURA COM FILTRO:

- [ ] Leitura 1 (start_index=0): Filtrar e escrever regras Dispatcher
- [ ] Leitura 2 (start_index=10000): Filtrar e escrever regras Dispatcher
- [ ] Leitura 3 (start_index=20000): Filtrar e escrever regras Dispatcher
- [ ] Leitura 4 (start_index=30000): Filtrar e escrever regras Dispatcher
- [ ] Leitura 5+ (continuar até fim): Filtrar e escrever regras Dispatcher

---

## 📊 FASE 2: COMPARAÇÃO CSV COM FILTRO DISPATCHER

### Arquivos CSV a Analisar:
- `assets/CodeQuality-rules-latest-AMS-2024-12-0.csv` (versão mais recente)
- `assets/CodeQuality-rules-latest-AMS.csv` (versão anterior)

### PROCESSO DE COMPARAÇÃO COM FILTRO:

1. **Ler CSV mais recente**
2. **Para cada regra no CSV**:
   - **APLICAR FILTRO DISPATCHER** (prefixos + palavras-chave)
   - Se é regra Dispatcher E não foi documentada → **ADICIONAR**
   - Se não é regra Dispatcher → **IGNORAR**
3. **Identificar novas regras Dispatcher**
4. **Documentar apenas mudanças Dispatcher**

### FORMATO PARA REGRAS DISPATCHER ADICIONAIS DO CSV:

```markdown
### [Rule Key] - [Nome da Regra]

| Atributo | Valor |
|----------|-------|
| **Key** | [rule_key] |
| **Type** | [Code Smell] |
| **Severity** | [Major/Minor] |
| **Tags** | beta, dispatcher |

**Descrição**: [descrição do CSV ou inferida]

**Categoria**: Dispatcher & Infrastructure
**Fonte**: CSV (não encontrada na documentação oficial)

#### Exemplo de Configuração (se aplicável):
```apache
# Non-compliant configuration
[exemplo se disponível]

# Compliant configuration  
[exemplo se disponível]
```
```

---

## 📝 ESTRUTURA DO ARQUIVO DISPATCHER

### Arquivo: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md`

```markdown
# Regras Dispatcher, CDN Fastly e Infraestrutura - AEM Cloud Service

**Última atualização:** [Data atual]
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
| **Total Regras Dispatcher & CDN** | XXX |
| **Regras Dispatcher** | XX |
| **Regras Apache** | XX |
| **Regras CDN Fastly** | XX |
| **Regras Sintaxe** | XX |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Major** | XX |
| **Minor** | XX |

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
}
```

### DOTRules:Disp-2---statfileslevel

[Detalhes da regra com exemplos]

[... todas as regras Dispatcher ...]

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

#### Configuração Non-compliant:
```apache
<Directory />
  Require all granted
</Directory>
```

#### Configuração Compliant:
```apache
<Directory />
  Require all denied
</Directory>

<Directory "/var/www/html">
  Require all granted
</Directory>
```

[... todas as regras Apache ...]

---

## 🚀 Regras CDN Fastly

### Configurações CDN Fastly - AEM Cloud Service

| Atributo | Valor |
|----------|-------|
| **Categoria** | CDN Fastly |
| **Tecnologia** | VCL, Edge Computing |
| **Foco** | Cache, Purge, Headers, Performance |

**Descrição**: Regras e configurações específicas para CDN Fastly no AEM Cloud Service, incluindo cache policies, purge strategies, e otimizações de performance.

#### Configuração CDN Fastly:
```vcl
# Exemplo de configuração VCL para Fastly
sub vcl_recv {
  # Cache control headers
  if (req.url ~ "^/content/dam/.*\.(jpg|jpeg|png|gif|webp)$") {
    set req.http.Cache-Control = "public, max-age=31536000";
  }
}

sub vcl_deliver {
  # Add cache headers
  set resp.http.X-Cache-Status = "HIT";
}
```

[... outras regras CDN Fastly ...]

---

## ⚙️ Regras de Sintaxe (DOTRules:Syntax*, DOTRules:Disp-S*)

### DOTRules:Disp-S1---brace-missing

[Detalhes da regra com exemplos]

### DOTRules:Syntax0---syntax-violation

[Detalhes da regra com exemplos]

[... todas as regras de sintaxe ...]

---

## 📚 Referências Dispatcher, CDN Fastly & Infrastructure

### Documentação Oficial
- [Dispatcher Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-dispatcher/using/configuring/dispatcher-configuration)
- [CDN in AEM as a Cloud Service](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn)
- [Fastly CDN Configuration](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-credentials-authentication)
- [Dispatcher Optimization Tool](https://github.com/adobe/aem-dispatcher-optimizer-tool)
- [Apache HTTP Server Documentation](https://httpd.apache.org/docs/)

### Ferramentas
- [Dispatcher Optimization Tool (DOT)](https://github.com/adobe/aem-dispatcher-optimizer-tool/blob/main/docs/Rules.md)
- [AEM Dispatcher Converter](https://github.com/adobe/aem-cloud-service-source-migration/tree/master/packages/dispatcher-converter)
- [Fastly VCL Documentation](https://docs.fastly.com/en/guides/guide-to-vcl)
- [AEM CDN Cache Purging](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/caching)

---

*Última atualização: [Data atual]*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Dispatcher, CDN Fastly & Infrastructure Rules*
```

---

## ✅ CHECKLIST DE EXECUÇÃO DISPATCHER

### FASE 1 - Leitura MCP com Filtro Dispatcher:
- [ ] Leitura parte 1: Filtrar e escrever regras Dispatcher
- [ ] Leitura parte 2: Filtrar e escrever regras Dispatcher  
- [ ] Leitura parte 3: Filtrar e escrever regras Dispatcher
- [ ] Leitura parte 4: Filtrar e escrever regras Dispatcher
- [ ] Leitura parte 5+: Continuar até fim
- [ ] TODAS as regras Dispatcher da documentação escritas
- [ ] Buscas complementares executadas

### FASE 2 - Comparação CSV com Filtro Dispatcher:
- [ ] CSV mais recente lido e filtrado para Dispatcher
- [ ] CSV anterior lido e filtrado para Dispatcher
- [ ] Regras Dispatcher novas identificadas
- [ ] Regras Dispatcher adicionais escritas no arquivo
- [ ] Mudanças Dispatcher documentadas

### VALIDAÇÃO FINAL:
- [ ] Arquivo Dispatcher criado: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md`
- [ ] Arquivo tem >250 linhas
- [ ] Apenas regras Dispatcher & CDN incluídas
- [ ] Estatísticas Dispatcher & CDN corretas
- [ ] Exemplos de configuração incluídos

---

## 📈 MÉTRICAS DE SUCESSO DISPATCHER

| Métrica Dispatcher | Mínimo Esperado |
|--------------------|-----------------|
| **Regras Dispatcher & CDN documentadas** | 15-30 regras |
| **Linhas arquivo Dispatcher & CDN** | 250+ |
| **Regras Disp-*** | 8-12 regras |
| **Regras Httpd-*** | 2-5 regras |
| **Regras CDN Fastly** | 3-8 regras |
| **Regras Sintaxe** | 5-8 regras |
| **Exemplos de configuração** | 10+ |

---

## 🔐 VALIDAÇÃO FINAL DISPATCHER

### 🚨 VALIDAÇÃO OBRIGATÓRIA:

```bash
# Executar antes de finalizar

echo "=== VALIDAÇÃO ARQUIVO DISPATCHER ==="

# 1. Verificar arquivo existe
if [ -f "output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md" ]; then
    echo "✅ Arquivo Dispatcher existe"
else
    echo "❌ FALHA: Arquivo Dispatcher não existe"
    exit 1
fi

# 2. Verificar tamanho mínimo
DISPATCHER_LINES=$(wc -l < output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md)
echo "Arquivo Dispatcher & CDN: $DISPATCHER_LINES linhas"
[ "$DISPATCHER_LINES" -gt 250 ] && echo "✅ Tamanho OK" || echo "❌ FALHA: <250 linhas"

# 3. Verificar conteúdo Dispatcher
echo "=== VERIFICAÇÃO DE CONTEÚDO ==="
grep -c "DOTRules:Disp-" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md && echo "✅ Regras Disp-* encontradas"
grep -c "DOTRules:Httpd-" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md && echo "✅ Regras Httpd-* encontradas"
grep -c "DOTRules:Syntax" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md && echo "✅ Regras Syntax* encontradas"

# 4. Verificar que NÃO tem regras de outras categorias
echo "=== VERIFICAÇÃO DE FILTRO ==="
if grep -q "java:S" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md; then
    echo "❌ FALHA: Contém regras Java (java:S*)"
else
    echo "✅ Filtro OK: Sem regras Java"
fi

if grep -q "BannedPath" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md; then
    echo "❌ FALHA: Contém regras Content (BannedPath)"
else
    echo "✅ Filtro OK: Sem regras Content"
fi

if grep -q "ClassicUI" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md; then
    echo "❌ FALHA: Contém regras Frontend (ClassicUI*)"
else
    echo "✅ Filtro OK: Sem regras Frontend"
fi
```

### ✅ CRITÉRIOS DE SUCESSO:

- ✅ Arquivo `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md` existe
- ✅ Arquivo tem >250 linhas
- ✅ Contém apenas regras Dispatcher (DOTRules:*)
- ✅ NÃO contém regras de outras categorias
- ✅ Estatísticas Dispatcher corretas
- ✅ Exemplos de configuração incluídos

### ❌ FALHA SE:

- ❌ Arquivo não existe ou tem <200 linhas
- ❌ Contém regras não-Dispatcher (java:S*, BannedPath, etc.)
- ❌ Faltam regras Dispatcher importantes
- ❌ Estatísticas incorretas

---

## 📊 RESUMO EXECUTIVO DISPATCHER

**AO FINALIZAR, REPORTAR:**

```
=== RELATÓRIO DISPATCHER & INFRASTRUCTURE RULES ===

FASE 1 - MCP com Filtro Dispatcher:
✅ Partes lidas: [X] partes
✅ Regras Dispatcher extraídas: [X] regras
✅ Regras não-Dispatcher ignoradas: [X] regras
✅ Exemplos configuração obtidos: [X] exemplos

FASE 2 - CSV com Filtro Dispatcher:
✅ Regras Dispatcher no CSV: [X] regras
✅ Regras Dispatcher novas (não no MCP): [X] regras
✅ Mudanças Dispatcher identificadas: [X] mudanças

RESULTADO FINAL:
✅ Arquivo: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md
✅ Linhas: [X] linhas
✅ Regras Dispatcher: [X] regras
✅ Regras Disp-*: [X] regras
✅ Regras Httpd-*: [X] regras  
✅ Regras Sintaxe: [X] regras

FILTRO APLICADO:
✅ Incluídas: DOTRules:Disp-*, DOTRules:Httpd-*, DOTRules:Syntax*
✅ Excluídas: java:S*, BannedPath, ClassicUI*, AEM Rules:*

STATUS: [SUCESSO/FALHA]
```

---

## 🔍 VALIDAÇÃO PÓS-GERAÇÃO

### 🚨 VALIDAÇÃO OBRIGATÓRIA APÓS GERAR O ARQUIVO

**APÓS GERAR O ARQUIVO DE OUTPUT, VOCÊ DEVE EXECUTAR ESTA VALIDAÇÃO:**

```bash
echo "=== VALIDAÇÃO FINAL DO ARQUIVO GERADO ==="

# 1. Verificar se arquivo foi criado
FILE_PATH="output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md"
if [ -f "$FILE_PATH" ]; then
    echo "✅ Arquivo criado com sucesso: $FILE_PATH"
else
    echo "❌ ERRO CRÍTICO: Arquivo não foi criado!"
    exit 1
fi

# 2. Verificar tamanho do arquivo
LINES=$(wc -l < "$FILE_PATH")
echo "📊 Arquivo contém: $LINES linhas"
if [ "$LINES" -gt 250 ]; then
    echo "✅ Tamanho adequado (>250 linhas)"
else
    echo "❌ ERRO: Arquivo muito pequeno (<250 linhas)"
fi

# 3. Verificar conteúdo específico Dispatcher
echo "=== VERIFICAÇÃO DE CONTEÚDO DISPATCHER ==="
DISP_RULES=$(grep -c "DOTRules:Disp-" "$FILE_PATH" 2>/dev/null || echo "0")
HTTPD_RULES=$(grep -c "DOTRules:Httpd-" "$FILE_PATH" 2>/dev/null || echo "0")
SYNTAX_RULES=$(grep -c "DOTRules:Syntax" "$FILE_PATH" 2>/dev/null || echo "0")

echo "📋 Regras Dispatcher (DOTRules:Disp-*): $DISP_RULES"
echo "📋 Regras Apache (DOTRules:Httpd-*): $HTTPD_RULES"
echo "📋 Regras Sintaxe (DOTRules:Syntax*): $SYNTAX_RULES"

# 4. Verificar filtro aplicado corretamente
echo "=== VERIFICAÇÃO DE FILTRO ==="
JAVA_RULES=$(grep -c "java:S" "$FILE_PATH" 2>/dev/null || echo "0")
BANNED_RULES=$(grep -c "BannedPath" "$FILE_PATH" 2>/dev/null || echo "0")
UI_RULES=$(grep -c "ClassicUI" "$FILE_PATH" 2>/dev/null || echo "0")

if [ "$JAVA_RULES" -eq 0 ] && [ "$BANNED_RULES" -eq 0 ] && [ "$UI_RULES" -eq 0 ]; then
    echo "✅ Filtro aplicado corretamente - apenas regras Dispatcher"
else
    echo "❌ ERRO: Filtro não aplicado - contém regras não-Dispatcher"
    echo "   - Regras Java (java:S*): $JAVA_RULES"
    echo "   - Regras Content (BannedPath): $BANNED_RULES"
    echo "   - Regras UI (ClassicUI*): $UI_RULES"
fi

# 5. Verificar estrutura do documento
echo "=== VERIFICAÇÃO DE ESTRUTURA ==="
if grep -q "# Regras Dispatcher, CDN Fastly e Infraestrutura" "$FILE_PATH"; then
    echo "✅ Título principal encontrado"
else
    echo "❌ ERRO: Título principal não encontrado"
fi

if grep -q "## 📊 Estatísticas Dispatcher" "$FILE_PATH"; then
    echo "✅ Seção de estatísticas encontrada"
else
    echo "❌ ERRO: Seção de estatísticas não encontrada"
fi

# 6. Verificar exemplos de configuração
EXAMPLES=$(grep -c "```apache\|```vcl\|```conf" "$FILE_PATH" 2>/dev/null || echo "0")
echo "📝 Exemplos de configuração encontrados: $EXAMPLES"
if [ "$EXAMPLES" -gt 5 ]; then
    echo "✅ Exemplos suficientes de configuração"
else
    echo "⚠️  AVISO: Poucos exemplos de configuração ($EXAMPLES)"
fi

echo "=== VALIDAÇÃO CONCLUÍDA ==="
```

### ✅ CRITÉRIOS DE APROVAÇÃO:

- ✅ Arquivo existe e tem >250 linhas
- ✅ Contém regras Dispatcher (DOTRules:Disp-*, DOTRules:Httpd-*, DOTRules:Syntax*)
- ✅ NÃO contém regras de outras categorias (java:S*, BannedPath, ClassicUI*)
- ✅ Estrutura do documento está completa
- ✅ Contém exemplos de configuração (.any, .conf, .vcl)

---

## 📁 CÓPIA PARA STEERING

### 🚨 APÓS VALIDAÇÃO APROVADA, COPIAR ARQUIVO PARA STEERING no Workspace

**EXECUTAR APÓS VALIDAÇÃO BEM-SUCEDIDA:**

```bash
# Criar diretório steering se não existir
mkdir -p [projeto]/.kiro/steering

# Copiar arquivo para steering
cp "output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md" "[projeto]/.kiro/steering/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md"

echo "✅ Arquivo copiado para steering com sucesso!"
echo "📁 Localização: [projeto].kiro/steering/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md"
```

### ✅ CHECKLIST FINAL:

- [ ] **Arquivo gerado**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md`
- [ ] **Validação executada**: Arquivo aprovado em todos os critérios
- [ ] **Arquivo copiado para steering**: `.kiro/steering/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md`

---

## 📊 RESUMO EXECUTIVO DISPATCHER

**AO FINALIZAR, REPORTAR:**

```
=== RELATÓRIO DISPATCHER & INFRASTRUCTURE RULES ===

FASE 1 - MCP com Filtro Dispatcher:
✅ Partes lidas: [X] partes
✅ Regras Dispatcher extraídas: [X] regras
✅ Regras não-Dispatcher ignoradas: [X] regras
✅ Exemplos configuração obtidos: [X] exemplos

FASE 2 - CSV com Filtro Dispatcher:
✅ Regras Dispatcher no CSV: [X] regras
✅ Regras Dispatcher novas (não no MCP): [X] regras
✅ Mudanças Dispatcher identificadas: [X] mudanças

RESULTADO FINAL:
✅ Arquivo: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md
✅ Linhas: [X] linhas
✅ Regras Dispatcher: [X] regras
✅ Regras Disp-*: [X] regras
✅ Regras Httpd-*: [X] regras  
✅ Regras Sintaxe: [X] regras

FILTRO APLICADO:
✅ Incluídas: DOTRules:Disp-*, DOTRules:Httpd-*, DOTRules:Syntax*
✅ Excluídas: java:S*, BannedPath, ClassicUI*, AEM Rules:*

VALIDAÇÃO:
✅ Arquivo validado e aprovado
✅ Arquivo copiado para steering: .kiro/steering/output-aemcs-sonarqube-dispatcher-and-cdn-fastly-rules-ptbr.md

STATUS: [SUCESSO/FALHA]
```