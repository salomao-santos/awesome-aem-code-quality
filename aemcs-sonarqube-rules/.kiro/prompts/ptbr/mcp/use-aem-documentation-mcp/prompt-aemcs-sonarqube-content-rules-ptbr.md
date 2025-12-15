# 🚨 PROMPT: Gerar Regras Content & Package - AEM SonarQube

## ⚠️ ATENÇÃO: USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION

**🔴 ESTE PROMPT REQUER O USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION**

**VOCÊ DEVE**:
1. ✅ Configurar o MCP AEM Documentation **ANTES** de qualquer outra ação
2. ✅ Ler a documentação oficial via MCP **POR PARTES** (documentos grandes > 1000 linhas)
3. ✅ **FILTRAR APENAS REGRAS CONTENT/PACKAGE** durante a leitura
4. ✅ Comparar com CSVs e **ADICIONAR REGRAS CONTENT NOVAS**
5. ✅ **GERAR APENAS 1 ARQUIVO**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md`

**🚫 NÃO É PERMITIDO**:
- ❌ Pular o uso do MCP
- ❌ Usar apenas os arquivos CSV
- ❌ Incluir regras que não são Content/Package
- ❌ Gerar múltiplos arquivos

---

## 🎯 Objetivo

Gerar **1 documento técnico específico** sobre as regras de conteúdo e pacotes de qualidade de código do Cloud Manager para Adobe Experience Manager as a Cloud Service (AEMaaCS).

## 📁 ARQUIVO DE OUTPUT

**Arquivo**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md`
- **Regras**: `BannedPath`, `PackageOverlaps`, `ConfigAndInstallShouldOnlyContainOsgiNodes`, `DuplicateOsgiConfigurations`, Oak Index rules, OakPAL rules
- **Foco**: Estrutura de conteúdo, pacotes FileVault, índices Oak, componentes AEM, JCR
- **Tecnologias**: `.xml`, `.json`, `.content.xml`, estrutura JCR, pacotes
- **Mínimo**: 300 linhas

---

## 🔍 CRITÉRIOS DE FILTRO CONTENT & PACKAGE

### ✅ **INCLUIR APENAS SE**:

#### 📂 **Prefixos Content Exatos**:
- `BannedPath` (modificações em /libs)
- `PackageOverlaps` (sobreposição de pacotes)
- `ConfigAndInstallShouldOnlyContainOsgiNodes` (estrutura OSGi)
- `DuplicateOsgiConfigurations` (configurações duplicadas)
- `OakIndex*` (todas as regras Oak Index)
- `Index*` (regras de índices)
- `CQBP-84` (regras OakPAL)
- `Custom*` (índices customizados)
- `Duplicate*` (duplicações)
- `RestrictIndex*` (restrições de índice)
- `FilterXmlModeAnalysis` (análise filter.xml)

#### 🔑 **Palavras-chave Content**:
- /libs, /apps, package, content, oak, index
- JCR, node, repository, vault, filter
- OSGi, configuration, bundle, install
- template, component, dialog (estrutura)

#### 💻 **Tecnologias Content**:
- .xml, .content.xml, estrutura JCR
- Pacotes FileVault, filter.xml
- Configurações OSGi, bundles

### ❌ **EXCLUIR SEMPRE**:
- Regras Java (`java:S*`, `AEM Rules:*` com código Java)
- Regras de Dispatcher (`DOTRules:*`)
- Regras de UI/Frontend (`ClassicUI*` se não estrutural)

---

## 📖 FASE 1: LEITURA MCP COM FILTRO CONTENT

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
   - **FILTRAR** apenas regras Content/Package (prefixos + palavras-chave)
   - **ESCREVER** imediatamente no arquivo Content
   - **IGNORAR** regras de outras categorias

3. **Continuar lendo** até documento completo

### 📝 PROCESSO DE FILTRO POR PARTE:

```markdown
# Para cada parte lida do MCP:

1. IDENTIFICAR regras Content/Package:
   - Prefixo BannedPath → INCLUIR
   - Prefixo PackageOverlaps → INCLUIR  
   - Prefixo OakIndex* → INCLUIR
   - Prefixo CQBP-84 → INCLUIR
   - Prefixo java:S* → EXCLUIR
   - Prefixo DOTRules:* → EXCLUIR

2. ESCREVER no arquivo Content:
   - Apenas regras identificadas como Content/Package
   - Com exemplos de estrutura XML/JCR
   - Com descrições detalhadas

3. CONTINUAR para próxima parte
```

### ✅ CHECKLIST DE LEITURA COM FILTRO:

- [ ] Leitura 1 (start_index=0): Filtrar e escrever regras Content
- [ ] Leitura 2 (start_index=10000): Filtrar e escrever regras Content
- [ ] Leitura 3 (start_index=20000): Filtrar e escrever regras Content
- [ ] Leitura 4 (start_index=30000): Filtrar e escrever regras Content
- [ ] Leitura 5+ (continuar até fim): Filtrar e escrever regras Content

---

## 📊 FASE 2: COMPARAÇÃO CSV COM FILTRO CONTENT

### Arquivos CSV a Analisar:
- `assets/CodeQuality-rules-latest-AMS-2024-12-0.csv` (versão mais recente)
- `assets/CodeQuality-rules-latest-AMS.csv` (versão anterior)

### PROCESSO DE COMPARAÇÃO COM FILTRO:

1. **Ler CSV mais recente**
2. **Para cada regra no CSV**:
   - **APLICAR FILTRO CONTENT** (prefixos + palavras-chave)
   - Se é regra Content E não foi documentada → **ADICIONAR**
   - Se não é regra Content → **IGNORAR**
3. **Identificar novas regras Content**
4. **Documentar apenas mudanças Content**

### FORMATO PARA REGRAS CONTENT ADICIONAIS DO CSV:

```markdown
### [Rule Key] - [Nome da Regra]

| Atributo | Valor |
|----------|-------|
| **Key** | [rule_key] |
| **Type** | [Bug/Code Smell/Improvement] |
| **Severity** | [Blocker/Critical/Major/Minor/Info] |
| **Tags** | [tags do CSV] |
| **Since** | [versão se disponível] |

**Descrição**: [descrição do CSV ou inferida]

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Exemplo de Estrutura (se aplicável):
```xml
<!-- Non-compliant structure -->
[exemplo se disponível]

<!-- Compliant structure -->  
[exemplo se disponível]
```
```

---

## 📝 ESTRUTURA DO ARQUIVO CONTENT

### Arquivo: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md`

```markdown
# Regras de Conteúdo e Pacotes - AEM Cloud Service

**Última atualização:** [Data atual]
**Categoria:** Content & Package Rules
**Tecnologias:** `.xml`, `.json`, `.content.xml`, estrutura JCR
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Content & Package

| Tipo | Quantidade |
|------|------------|
| **Total Regras Content** | XXX |
| **Regras de Pacote** | XX |
| **Regras Oak Index** | XX |
| **Regras OakPAL** | XX |
| **Regras JCR** | XX |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | XX |
| **Critical** | XX |
| **Major** | XX |
| **Minor** | XX |
| **Info** | XX |

---

## 🚫 Regras de Estrutura de Pacotes

### BannedPath - Customer packages should not install content under /libs

| Atributo | Valor |
|----------|-------|
| **Key** | BannedPath |
| **Type** | Bug |
| **Severity** | Blocker |
| **Since** | Version 2019.6.0 |

**Descrição**: A árvore de conteúdo `/libs` no repositório AEM deve ser considerada somente leitura pelos clientes. Modificar nós e propriedades sob `/libs` cria risco significativo para atualizações maiores e menores.

#### Estrutura Non-compliant:
```xml
+ libs
  + foundation
    + components
      + myCustomComponent
```

#### Estrutura Compliant:
```xml
+ apps
  + myproject
    + components
      + myCustomComponent
```

### PackageOverlaps - Customer packages should not overlap

[Detalhes da regra com exemplos]

[... todas as regras de pacote ...]

---

## 🌳 Regras Oak Index

### OakIndexLocation - Custom Search Index Definition Nodes Must Be Direct Children of /oak:index

[Detalhes da regra com exemplos]

### IndexCompatVersion - Custom Search Index Definition Nodes Must Have a compatVersion of 2

[Detalhes da regra com exemplos]

[... todas as regras Oak Index ...]

---

## 🔍 Regras OakPAL

### CQBP-84 - Product APIs annotated with @ProviderType should not be implemented by customers

[Detalhes da regra com exemplos]

[... todas as regras OakPAL ...]

---

## ⚙️ Regras de Configuração OSGi

### ConfigAndInstallShouldOnlyContainOsgiNodes - Paths with /config/ and /install/ should only be used for OSGi

[Detalhes da regra com exemplos]

### DuplicateOsgiConfigurations - Customer packages should not contain overlapping OSGi configurations

[Detalhes da regra com exemplos]

[... todas as regras OSGi ...]

---

## 📦 Regras de Filter.xml

### FilterXmlModeAnalysis - Should not contain mode replacement for forbidden paths

[Detalhes da regra com exemplos]

[... todas as regras Filter.xml ...]

---

## 📚 Referências Content & Package

### Documentação Oficial
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Content Search and Indexing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing)
- [Package Manager](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/sites/administering/contentmanagement/package-manager)

### Ferramentas
- [OakPAL Documentation](https://github.com/adamcin/oakpal)
- [FileVault Documentation](https://jackrabbit.apache.org/filevault/)

---

*Última atualização: [Data atual]*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Content & Package Rules*
```

---

## ✅ CHECKLIST DE EXECUÇÃO CONTENT

### FASE 1 - Leitura MCP com Filtro Content:
- [ ] Leitura parte 1: Filtrar e escrever regras Content
- [ ] Leitura parte 2: Filtrar e escrever regras Content  
- [ ] Leitura parte 3: Filtrar e escrever regras Content
- [ ] Leitura parte 4: Filtrar e escrever regras Content
- [ ] Leitura parte 5+: Continuar até fim
- [ ] TODAS as regras Content da documentação escritas
- [ ] Buscas complementares executadas

### FASE 2 - Comparação CSV com Filtro Content:
- [ ] CSV mais recente lido e filtrado para Content
- [ ] CSV anterior lido e filtrado para Content
- [ ] Regras Content novas identificadas
- [ ] Regras Content adicionais escritas no arquivo
- [ ] Mudanças Content documentadas

### VALIDAÇÃO FINAL COMPLETA:
- [ ] Arquivo Content criado: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md`
- [ ] Arquivo tem >300 linhas
- [ ] Apenas regras Content/Package incluídas
- [ ] Estatísticas Content corretas
- [ ] Exemplos de estrutura incluídos
- [ ] **TODAS as validações bash executadas com sucesso**
- [ ] **Arquivo copiado para steering: `[projeto].kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md`**
- [ ] **Integridade da cópia verificada (MD5 idêntico)**
- [ ] **Arquivo steering legível e acessível**

---

## 📈 MÉTRICAS DE SUCESSO CONTENT

| Métrica Content | Mínimo Esperado |
|-----------------|-----------------|
| **Regras Content documentadas** | 25-35 regras |
| **Linhas arquivo Content** | 300+ |
| **Regras de Pacote** | 5-10 regras |
| **Regras Oak Index** | 10-15 regras |
| **Regras OakPAL** | 5-10 regras |
| **Exemplos de estrutura** | 10+ |

---

## 🔐 VALIDAÇÃO FINAL CONTENT

### 🚨 VALIDAÇÃO OBRIGATÓRIA:

**VOCÊ DEVE EXECUTAR TODAS AS VALIDAÇÕES ABAIXO APÓS GERAR O ARQUIVO COMPLETO:**

```bash
# FASE 1: VALIDAÇÃO BÁSICA DO ARQUIVO
echo "=== VALIDAÇÃO ARQUIVO CONTENT ==="

# 1. Verificar arquivo existe
if [ -f "output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md" ]; then
    echo "✅ Arquivo Content existe"
else
    echo "❌ FALHA: Arquivo Content não existe"
    exit 1
fi

# 2. Verificar tamanho mínimo
CONTENT_LINES=$(wc -l < output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md)
echo "Arquivo Content: $CONTENT_LINES linhas"
if [ "$CONTENT_LINES" -gt 300 ]; then
    echo "✅ Tamanho OK ($CONTENT_LINES linhas)"
else
    echo "❌ FALHA: Arquivo muito pequeno ($CONTENT_LINES linhas < 300 mínimo)"
    exit 1
fi

# 3. Verificar conteúdo Content obrigatório
echo "=== VERIFICAÇÃO DE CONTEÚDO OBRIGATÓRIO ==="
BANNED_COUNT=$(grep -c "BannedPath" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
PACKAGE_COUNT=$(grep -c "PackageOverlaps" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
OAK_COUNT=$(grep -c "OakIndex\|Index.*" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
OAKPAL_COUNT=$(grep -c "CQBP-84\|OakPAL" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
OSGI_COUNT=$(grep -c "ConfigAndInstallShouldOnlyContainOsgiNodes\|DuplicateOsgiConfigurations" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")

echo "Regras BannedPath: $BANNED_COUNT"
echo "Regras PackageOverlaps: $PACKAGE_COUNT"
echo "Regras Oak Index: $OAK_COUNT"
echo "Regras OakPAL: $OAKPAL_COUNT"
echo "Regras OSGi: $OSGI_COUNT"

# Validar mínimos obrigatórios
VALIDATION_FAILED=0
[ "$BANNED_COUNT" -eq 0 ] && echo "❌ FALHA: Nenhuma regra BannedPath encontrada" && VALIDATION_FAILED=1
[ "$PACKAGE_COUNT" -eq 0 ] && echo "❌ FALHA: Nenhuma regra PackageOverlaps encontrada" && VALIDATION_FAILED=1
[ "$OAK_COUNT" -eq 0 ] && echo "❌ FALHA: Nenhuma regra Oak Index encontrada" && VALIDATION_FAILED=1

if [ "$VALIDATION_FAILED" -eq 0 ]; then
    echo "✅ Conteúdo obrigatório presente"
else
    echo "❌ FALHA: Conteúdo obrigatório ausente"
    exit 1
fi

# 4. Verificar que NÃO tem regras de outras categorias
echo "=== VERIFICAÇÃO DE FILTRO ==="
JAVA_RULES=$(grep -c "java:S" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
DISPATCHER_RULES=$(grep -c "DOTRules:" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
AEM_JAVA_RULES=$(grep -c "AEM Rules:.*java\|AEM Rules:.*servlet" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")

if [ "$JAVA_RULES" -gt 0 ]; then
    echo "❌ FALHA: Contém $JAVA_RULES regras Java (java:S*) - DEVE SER REMOVIDO"
    exit 1
else
    echo "✅ Filtro OK: Sem regras Java"
fi

if [ "$DISPATCHER_RULES" -gt 0 ]; then
    echo "❌ FALHA: Contém $DISPATCHER_RULES regras Dispatcher (DOTRules:*) - DEVE SER REMOVIDO"
    exit 1
else
    echo "✅ Filtro OK: Sem regras Dispatcher"
fi

if [ "$AEM_JAVA_RULES" -gt 0 ]; then
    echo "❌ FALHA: Contém $AEM_JAVA_RULES regras AEM Java - DEVE SER REMOVIDO"
    exit 1
else
    echo "✅ Filtro OK: Sem regras AEM Java"
fi

# 5. Verificar estrutura do documento
echo "=== VERIFICAÇÃO DE ESTRUTURA ==="
STATS_COUNT=$(grep -c "## 📊 Estatísticas Content" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
EXAMPLES_COUNT=$(grep -c "```xml\|```json" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")
REFERENCES_COUNT=$(grep -c "## 📚 Referências" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md || echo "0")

[ "$STATS_COUNT" -eq 0 ] && echo "❌ FALHA: Seção de estatísticas ausente" && exit 1
[ "$EXAMPLES_COUNT" -lt 5 ] && echo "❌ FALHA: Poucos exemplos de código ($EXAMPLES_COUNT < 5)" && exit 1
[ "$REFERENCES_COUNT" -eq 0 ] && echo "❌ FALHA: Seção de referências ausente" && exit 1

echo "✅ Estrutura do documento OK"
echo "✅ Exemplos de código: $EXAMPLES_COUNT"

echo ""
echo "🎉 VALIDAÇÃO COMPLETA - ARQUIVO APROVADO!"
```

### 📋 FASE 2: CÓPIA PARA STEERING

**APÓS VALIDAÇÃO APROVADA, EXECUTAR:**

```bash
# FASE 2: CÓPIA PARA PASTA STEERING
echo "=== COPIANDO ARQUIVO PARA STEERING ==="

# 1. Criar diretório steering se não existir
mkdir -p .kiro/steering

# 2. Copiar arquivo validado para steering
cp output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md

# 3. Verificar cópia
if [ -f ".kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md" ]; then
    STEERING_LINES=$(wc -l < .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md)
    echo "✅ Arquivo copiado para steering: $STEERING_LINES linhas"
    
    # Verificar se as linhas são iguais
    if [ "$CONTENT_LINES" -eq "$STEERING_LINES" ]; then
        echo "✅ Cópia íntegra confirmada"
    else
        echo "❌ FALHA: Cópia incompleta ($CONTENT_LINES != $STEERING_LINES)"
        exit 1
    fi
else
    echo "❌ FALHA: Arquivo não foi copiado para steering"
    exit 1
fi

echo ""
echo "🎯 PROCESSO COMPLETO!"
echo "📁 Arquivo original: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md"
echo "📁 Arquivo steering: .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md"
echo "📊 Total de linhas: $STEERING_LINES"
```

### 🔄 FASE 3: VALIDAÇÃO FINAL STEERING

**EXECUTAR VALIDAÇÃO FINAL DO ARQUIVO STEERING:**

```bash
# FASE 3: VALIDAÇÃO FINAL DO STEERING
echo "=== VALIDAÇÃO FINAL STEERING ==="

# 1. Verificar arquivo steering existe e tem conteúdo
if [ -f ".kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md" ]; then
    STEERING_SIZE=$(stat -c%s .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md)
    echo "✅ Arquivo steering existe ($STEERING_SIZE bytes)"
else
    echo "❌ FALHA: Arquivo steering não existe"
    exit 1
fi

# 2. Verificar que steering tem mesmo conteúdo que original
ORIGINAL_MD5=$(md5sum output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md | cut -d' ' -f1)
STEERING_MD5=$(md5sum .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md | cut -d' ' -f1)

if [ "$ORIGINAL_MD5" = "$STEERING_MD5" ]; then
    echo "✅ Arquivo steering idêntico ao original"
else
    echo "❌ FALHA: Arquivo steering diferente do original"
    echo "Original MD5: $ORIGINAL_MD5"
    echo "Steering MD5: $STEERING_MD5"
    exit 1
fi

# 3. Verificar permissões de leitura
if [ -r ".kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md" ]; then
    echo "✅ Arquivo steering legível"
else
    echo "❌ FALHA: Arquivo steering não legível"
    exit 1
fi

echo ""
echo "🏆 SUCESSO TOTAL!"
echo "✅ Arquivo Content gerado e validado"
echo "✅ Arquivo copiado para steering"
echo "✅ Validação final aprovada"
echo ""
echo "📋 RESUMO FINAL:"
echo "   Original: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md"
echo "   Steering: .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md"
echo "   Status: PRONTO PARA USO"
```

### ✅ CRITÉRIOS DE SUCESSO COMPLETO:

**FASE 1 - Geração do Arquivo:**
- ✅ Arquivo `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md` existe
- ✅ Arquivo tem >300 linhas
- ✅ Contém apenas regras Content/Package
- ✅ NÃO contém regras de outras categorias
- ✅ Estatísticas Content corretas
- ✅ Exemplos de estrutura incluídos

**FASE 2 - Validação Obrigatória:**
- ✅ Todas as validações bash executadas com sucesso
- ✅ Conteúdo obrigatório presente (BannedPath, PackageOverlaps, Oak Index)
- ✅ Filtro aplicado corretamente (sem Java/Dispatcher)
- ✅ Estrutura do documento completa
- ✅ Mínimo de 5 exemplos de código

**FASE 3 - Cópia para Steering:**
- ✅ Diretório `.kiro/steering` criado
- ✅ Arquivo copiado para `.kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md`
- ✅ Cópia íntegra confirmada (mesmo número de linhas)
- ✅ MD5 idêntico entre original e steering
- ✅ Arquivo steering legível

### ❌ FALHA SE:

**FASE 1 - Geração:**
- ❌ Arquivo não existe ou tem <300 linhas
- ❌ Contém regras não-Content (java:S*, DOTRules:*, AEM Rules:*java)
- ❌ Faltam regras Content importantes (BannedPath, PackageOverlaps, Oak Index)
- ❌ Estatísticas incorretas ou estrutura incompleta

**FASE 2 - Validação:**
- ❌ Qualquer validação bash falha
- ❌ Conteúdo obrigatório ausente
- ❌ Filtro não aplicado corretamente
- ❌ Menos de 5 exemplos de código

**FASE 3 - Steering:**
- ❌ Falha na criação do diretório steering
- ❌ Falha na cópia do arquivo
- ❌ Cópia incompleta ou corrompida
- ❌ MD5 diferente entre arquivos

---

## 📊 RESUMO EXECUTIVO CONTENT

**AO FINALIZAR TODAS AS FASES, REPORTAR:**

```
=== RELATÓRIO COMPLETO CONTENT & PACKAGE RULES ===

FASE 1 - MCP com Filtro Content:
✅ Partes lidas: [X] partes
✅ Regras Content extraídas: [X] regras
✅ Regras não-Content ignoradas: [X] regras
✅ Exemplos estrutura obtidos: [X] exemplos

FASE 2 - CSV com Filtro Content:
✅ Regras Content no CSV: [X] regras
✅ Regras Content novas (não no MCP): [X] regras
✅ Mudanças Content identificadas: [X] mudanças

FASE 3 - Geração do Arquivo:
✅ Arquivo: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md
✅ Linhas: [X] linhas
✅ Regras Content: [X] regras
✅ Regras de Pacote: [X] regras
✅ Regras Oak Index: [X] regras  
✅ Regras OakPAL: [X] regras
✅ Regras OSGi: [X] regras

FASE 4 - Validação Obrigatória:
✅ Validação básica: APROVADA
✅ Conteúdo obrigatório: PRESENTE
✅ Filtro aplicado: CORRETO
✅ Estrutura documento: COMPLETA
✅ Exemplos código: [X] exemplos (≥5)

FASE 5 - Cópia para Steering:
✅ Diretório steering: CRIADO
✅ Arquivo copiado: .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md
✅ Integridade: VERIFICADA
✅ MD5 original: [hash]
✅ MD5 steering: [hash] (IDÊNTICO)

FILTRO APLICADO:
✅ Incluídas: BannedPath, PackageOverlaps, Oak*, CQBP-84, OSGi*
✅ Excluídas: java:S*, DOTRules:*, AEM Rules:* (Java)

ARQUIVOS FINAIS:
📁 Original: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-content-rules-ptbr.md
📁 Steering: .kiro/steering/output-aemcs-sonarqube-content-rules-ptbr.md

STATUS FINAL: [SUCESSO COMPLETO/FALHA]
```

### 🎯 CHECKLIST FINAL OBRIGATÓRIO:

**ANTES DE REPORTAR SUCESSO, CONFIRMAR:**

- [ ] ✅ Arquivo original gerado e validado
- [ ] ✅ Todas as validações bash executadas com sucesso
- [ ] ✅ Arquivo copiado para steering com integridade
- [ ] ✅ MD5 idêntico entre original e steering
- [ ] ✅ Arquivo steering legível e acessível
- [ ] ✅ Relatório executivo completo fornecido

**SE QUALQUER ITEM FALHAR:**
- ❌ NÃO reportar sucesso
- ❌ Identificar e corrigir o problema
- ❌ Re-executar validações
- ❌ Só reportar sucesso após TODOS os itens ✅