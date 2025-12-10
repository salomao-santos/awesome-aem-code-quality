# 🚨 PROMPT OBRIGATÓRIO: Gerar Documentação Completa de Regras SonarQube do AEM

## ⚠️ ATENÇÃO: USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION

**🔴 ESTE PROMPT REQUER O USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION**

**VOCÊ DEVE**:
1. ✅ Configurar o MCP AEM Documentation **ANTES** de qualquer outra ação
2. ✅ Ler a documentação oficial via MCP **POR PARTES** (documentos grandes > 1000 linhas)
3. ✅ **ESCREVER TODOS OS ITENS** encontrados na documentação oficial
4. ✅ Comparar com CSVs e **ADICIONAR REGRAS NOVAS** que não estão na documentação
5. ✅ **AGUARDAR 3 MINUTOS** antes de copiar para o arquivo steering

**🚫 NÃO É PERMITIDO**:
- ❌ Pular o uso do MCP
- ❌ Usar apenas os arquivos CSV
- ❌ Gerar documento sem dados da documentação oficial
- ❌ Ignorar regras ou itens da documentação
- ❌ Copiar para steering imediatamente (causa truncamento)

---

## 🎯 Objetivo

Gerar um documento técnico **COMPLETO** sobre as regras customizadas de qualidade de código do Cloud Manager para Adobe Experience Manager as a Cloud Service (AEMaaCS).

**PROCESSO OBRIGATÓRIO EM 3 FASES**:

### FASE 1: Leitura COMPLETA da Documentação Oficial via MCP
- Ler **TODAS** as páginas da documentação oficial
- **POR PARTES** usando `start_index` para documentos grandes
- **ESCREVER CADA REGRA** encontrada no documento final

### FASE 2: Comparação com CSVs
- Ler os arquivos CSV
- **IDENTIFICAR REGRAS NOVAS** que não estão na documentação oficial
- **ADICIONAR** essas regras ao documento final

### FASE 3: Criação do Steering (COM ESPERA)
- **AGUARDAR 3 MINUTOS** após gerar o documento principal
- Copiar conteúdo resumido para o steering
- Verificar que o arquivo steering está completo

---

## 📖 FASE 1: LEITURA COMPLETA DA DOCUMENTAÇÃO OFICIAL

### 🚨 INSTRUÇÃO CRÍTICA: LEITURA POR PARTES

**A documentação oficial tem mais de 1000 linhas. VOCÊ DEVE:**

1. **Primeira leitura** - Início do documento:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=0
)
```

2. **Segunda leitura** - Continuação:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=10000
)
```

3. **Terceira leitura** - Continuação:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=20000
)
```

4. **Continuar lendo** até receber mensagem de que o documento terminou ou conteúdo vazio.

### 📝 PARA CADA PARTE LIDA, VOCÊ DEVE:

1. **EXTRAIR TODAS AS REGRAS** encontradas naquela parte
2. **ESCREVER NO DOCUMENTO** imediatamente (usando fsWrite ou fsAppend)
3. **NÃO PULAR NENHUMA REGRA** - cada regra deve ser documentada
4. **INCLUIR EXEMPLOS DE CÓDIGO** compliant e non-compliant

### ✅ CHECKLIST DE LEITURA POR PARTES:

- [ ] Leitura 1 (start_index=0): Extrair e escrever regras
- [ ] Leitura 2 (start_index=10000): Extrair e escrever regras
- [ ] Leitura 3 (start_index=20000): Extrair e escrever regras
- [ ] Leitura 4 (start_index=30000): Extrair e escrever regras
- [ ] Leitura 5 (start_index=40000): Extrair e escrever regras
- [ ] Continuar até documento completo

### 🔍 BUSCAS COMPLEMENTARES OBRIGATÓRIAS:

Após ler a documentação principal, executar buscas adicionais:

```
mcp_aem_documentation_mcp_server_search_experience_league(
  query="custom code quality rules",
  content_types=["Documentation"],
  include_all_aem_products=true
)
```

```
mcp_aem_documentation_mcp_server_search_experience_league(
  query="sonarqube rules cloud manager",
  content_types=["Documentation"],
  include_all_aem_products=true
)
```

```
mcp_aem_documentation_mcp_server_search_experience_league(
  query="oakpal content rules aem",
  content_types=["Documentation"],
  include_all_aem_products=true
)
```

---

## 📊 FASE 2: COMPARAÇÃO COM CSVs

### Arquivos CSV a Analisar:
- `assets/CodeQuality-rules-latest-AMS-2024-12-0.csv` (versão mais recente)
- `assets/CodeQuality-rules-latest-AMS.csv` (versão anterior)

### PROCESSO DE COMPARAÇÃO:

1. **Ler CSV mais recente**
2. **Para cada regra no CSV**:
   - Verificar se já foi documentada na Fase 1
   - Se **NÃO** foi documentada → **ADICIONAR** ao documento
   - Se **JÁ** foi documentada → Verificar se há informações adicionais no CSV
3. **Identificar regras novas** entre versões CSV
4. **Documentar mudanças** (migrações de chaves, novas regras, removidas)

### FORMATO PARA REGRAS ADICIONAIS DO CSV:

```markdown
### [Rule Key] - [Nome da Regra]

| Atributo | Valor |
|----------|-------|
| **Key** | [rule_key] |
| **Type** | [Vulnerability/Bug/Code Smell/Security Hotspot] |
| **Severity** | [Blocker/Critical/Major/Minor/Info] |
| **Tags** | [tags do CSV] |
| **Old Key** | [se houver migração] |

**Descrição**: [descrição do CSV ou inferida]

**Fonte**: CSV (não encontrada na documentação oficial)
```

---

## ⏳ FASE 3: CRIAÇÃO DO STEERING (COM ESPERA OBRIGATÓRIA)

### 🚨 INSTRUÇÃO CRÍTICA: AGUARDAR ANTES DE COPIAR

**PROBLEMA**: O arquivo steering frequentemente fica truncado quando copiado imediatamente.

**SOLUÇÃO OBRIGATÓRIA**:

1. **APÓS gerar o documento principal** (`output-aemcs-sonarqube-rules-ptbr.md`)
2. **AGUARDAR 3 MINUTOS** (180 segundos)
3. **VERIFICAR** que o documento principal está completo
4. **ENTÃO** criar o arquivo steering

### PROCESSO DE CRIAÇÃO DO STEERING:

```bash
# PASSO 1: Verificar documento principal
wc -l output-aemcs-sonarqube-rules-ptbr.md
# Deve ter mais de 1000 linhas

# PASSO 2: Aguardar (OBRIGATÓRIO)
echo "Aguardando 3 minutos antes de criar steering..."
sleep 180

# PASSO 3: Criar steering com conteúdo resumido
```

### CONTEÚDO DO STEERING:

O arquivo `.kiro/steering/aemcs-sonarqube-rules.md` deve conter:

```markdown
---
inclusion: always
---

# AEM Cloud Service - Regras de Qualidade de Código SonarQube

## � ResuImo Estatístico
- **Total de Regras**: [número exato]
- **Vulnerabilities**: [X] regras
- **Security Hotspots**: [X] regras
- **Bugs**: [X] regras
- **Code Smells**: [X] regras

## 🔴 Regras Críticas (Blocker/Critical)

[LISTAR TODAS as regras Blocker e Critical com suas chaves]

## ☁️ Regras de Compatibilidade Cloud Service

[LISTAR regras específicas para Cloud Service]

## � MigFrações de Chaves (SonarQube 9.9)

[LISTAR migrações squid:* → java:*]

## 📚 Referência Completa

Documento completo: `output-aemcs-sonarqube-rules-ptbr.md`

Fontes:
- Documentação oficial Adobe Experience League (via MCP)
- CSV: CodeQuality-rules-latest-AMS-2024-12-0.csv
- CSV: CodeQuality-rules-latest-AMS.csv
```

### ✅ VERIFICAÇÃO DO STEERING:

Após criar o steering, **VERIFICAR**:

```bash
# Contar linhas do steering
wc -l .kiro/steering/aemcs-sonarqube-rules.md

# Deve ter pelo menos 100 linhas
# Se tiver menos de 50 linhas, o arquivo está TRUNCADO - RECRIAR
```

---

## 📝 ESTRUTURA DO DOCUMENTO PRINCIPAL

### Arquivo: `output-aemcs-sonarqube-rules-ptbr.md`

```markdown
# Custom Code Quality Rules - AEM Cloud Service

**Última atualização:** [Data atual]
**Fontes:** 
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas

| Tipo | Quantidade |
|------|------------|
| Total de Regras | XXX |
| Vulnerabilities | XX |
| Security Hotspots | XX |
| Bugs | XX |
| Code Smells | XX |

---

## 🔴 Regras de Vulnerabilidade (Vulnerability)

[TODAS as regras de vulnerabilidade]

---

## 🟠 Regras de Security Hotspot

[TODAS as regras de security hotspot]

---

## 🔵 Regras de Bug

[TODAS as regras de bug]

---

## 🟡 Regras de Code Smell

[TODAS as regras de code smell]

---

## 📦 Regras OakPAL

[TODAS as regras OakPAL]

---

## 🔧 Regras Dispatcher (DOT)

[TODAS as regras Dispatcher]

---

## 🔄 Changelog e Migrações

[Mudanças entre versões, migrações de chaves]

---

## 📚 Referências

[Links para documentação oficial]
```

---

## ✅ CHECKLIST DE EXECUÇÃO COMPLETA

### FASE 1 - Leitura MCP:
- [ ] Leitura parte 1 (start_index=0) executada
- [ ] Leitura parte 2 (start_index=10000) executada
- [ ] Leitura parte 3 (start_index=20000) executada
- [ ] Leitura parte 4 (start_index=30000) executada
- [ ] Leitura parte 5+ (continuar até fim) executada
- [ ] TODAS as regras da documentação oficial escritas no documento
- [ ] Buscas complementares executadas

### FASE 2 - Comparação CSV:
- [ ] CSV mais recente lido
- [ ] CSV anterior lido
- [ ] Regras novas identificadas
- [ ] Regras adicionais do CSV escritas no documento
- [ ] Migrações de chaves documentadas

### FASE 3 - Steering:
- [ ] Documento principal verificado (>1000 linhas)
- [ ] **AGUARDADO 3 MINUTOS**
- [ ] Steering criado
- [ ] Steering verificado (>100 linhas)
- [ ] Se truncado, RECRIADO

### VALIDAÇÃO FINAL:
- [ ] Documento principal completo
- [ ] Steering completo (não truncado)
- [ ] Todas as regras documentadas
- [ ] Exemplos de código incluídos
- [ ] **SINCRONIZAÇÃO**: Se `output-aemcs-sonarqube-rules-ptbr.md` foi modificado, steering foi atualizado

---

## � REGTRA DE SINCRONIZAÇÃO OBRIGATÓRIA

### 🚨 INSTRUÇÃO CRÍTICA: MANTER STEERING SINCRONIZADO

**TODA VEZ que o arquivo `output-aemcs-sonarqube-rules-ptbr.md` for modificado, você DEVE:**

1. **ATUALIZAR** o arquivo `.kiro/steering/aemcs-sonarqube-rules.md`
2. **AGUARDAR 3 MINUTOS** antes de atualizar (evitar truncamento)
3. **VERIFICAR** que o steering foi atualizado corretamente

### QUANDO ATUALIZAR O STEERING:

| Ação no Documento Principal | Ação no Steering |
|----------------------------|------------------|
| Nova regra adicionada | Atualizar contagem e lista de regras críticas |
| Regra removida | Atualizar contagem e remover da lista |
| Estatísticas alteradas | Atualizar resumo estatístico |
| Novas migrações | Atualizar seção de migrações |
| Qualquer modificação | **SEMPRE** atualizar steering |

### PROCESSO DE SINCRONIZAÇÃO:

```bash
# PASSO 1: Verificar se documento principal foi modificado
# (Se você acabou de modificar output-aemcs-sonarqube-rules-ptbr.md)

# PASSO 2: Aguardar 3 minutos
echo "Aguardando 3 minutos antes de atualizar steering..."
sleep 180

# PASSO 3: Atualizar steering com dados atualizados
# - Recalcular estatísticas
# - Atualizar lista de regras críticas
# - Atualizar migrações

# PASSO 4: Verificar steering
wc -l .kiro/steering/aemcs-sonarqube-rules.md
# Deve ter >100 linhas
```

### ⚠️ VALIDAÇÃO DE SINCRONIZAÇÃO:

**ANTES DE FINALIZAR, CONFIRMAR:**

- [ ] Documento principal foi modificado? → Steering foi atualizado?
- [ ] Estatísticas do steering batem com documento principal?
- [ ] Lista de regras críticas está atualizada?
- [ ] Steering não está truncado (>100 linhas)?

---

## 🚨 TRATAMENTO DE ERROS

### Se a leitura MCP retornar truncada:
1. **CONTINUAR** com próximo start_index
2. **NÃO PARAR** até ler documento completo

### Se o steering ficar truncado:
1. **DELETAR** arquivo truncado
2. **AGUARDAR** mais 2 minutos
3. **RECRIAR** o arquivo steering
4. **VERIFICAR** novamente

### Se CSV não puder ser lido:
1. **TENTAR** leitura alternativa
2. **DOCUMENTAR** que CSV não foi incluído
3. **CONTINUAR** com dados do MCP

---

## � MÉTRIrCAS DE SUCESSO

| Métrica | Mínimo Esperado |
|---------|-----------------|
| Regras documentadas | 150+ |
| Linhas documento principal | 1000+ |
| Linhas steering | 100+ |
| Exemplos de código | 20+ |
| Partes MCP lidas | 5+ |

---

## �  RESUMO EXECUTIVO

**AO FINALIZAR, REPORTAR:**

```
=== RELATÓRIO DE EXECUÇÃO ===

FASE 1 - MCP:
✅ Partes lidas: [X] partes
✅ Regras extraídas: [X] regras
✅ Exemplos obtidos: [X] exemplos

FASE 2 - CSV:
✅ Regras no CSV: [X] regras
✅ Regras novas (não no MCP): [X] regras
✅ Migrações identificadas: [X] migrações

FASE 3 - STEERING:
✅ Aguardado: [X] minutos
✅ Linhas steering: [X] linhas
✅ Status: [COMPLETO/TRUNCADO]

TOTAIS:
- Documento principal: [X] linhas
- Total de regras: [X] regras
- Fontes utilizadas: MCP + CSV1 + CSV2

STATUS FINAL: [SUCESSO/FALHA]
```

---

## 🔐 CONDIÇÃO FINAL DE VALIDAÇÃO

### 🚨 VALIDAÇÃO OBRIGATÓRIA ANTES DE CONCLUIR:

**O TRABALHO SÓ ESTÁ COMPLETO SE:**

1. ✅ **Documento principal existe** e tem >1000 linhas
2. ✅ **Steering existe** e tem >100 linhas
3. ✅ **Steering está SINCRONIZADO** com documento principal
4. ✅ **Estatísticas batem** entre documento e steering
5. ✅ **Nenhum arquivo truncado**

### COMANDO DE VALIDAÇÃO FINAL:

```bash
# Executar OBRIGATORIAMENTE antes de finalizar

echo "=== VALIDAÇÃO FINAL ==="

# 1. Verificar documento principal
DOC_LINES=$(wc -l < output-aemcs-sonarqube-rules-ptbr.md 2>/dev/null || echo "0")
echo "Documento principal: $DOC_LINES linhas"
[ "$DOC_LINES" -gt 1000 ] && echo "✅ OK" || echo "❌ FALHA: <1000 linhas"

# 2. Verificar steering
STEERING_LINES=$(wc -l < .kiro/steering/aemcs-sonarqube-rules.md 2>/dev/null || echo "0")
echo "Steering: $STEERING_LINES linhas"
[ "$STEERING_LINES" -gt 1000 ] && echo "✅ OK" || echo "❌ FALHA: <1000 linhas (TRUNCADO)"

# 3. Verificar sincronização
echo ""
echo "=== VERIFICAR SINCRONIZAÇÃO MANUAL ==="
echo "Comparar estatísticas do documento principal com steering"
echo "Se diferentes, ATUALIZAR steering antes de finalizar"
```

### ❌ NÃO FINALIZAR SE:

- Documento principal tem <1000 linhas
- Steering tem <100 linhas (truncado)
- Steering não foi atualizado após modificação do documento principal
- Estatísticas não batem entre documento e steering

### ✅ FINALIZAR APENAS SE:

- Todas as validações passaram
- Steering está sincronizado
- Nenhum arquivo truncado
- Relatório de execução gerado
