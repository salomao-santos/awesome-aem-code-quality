# 🚨 PROMPT: Gerar Regras Frontend & Template - AEM SonarQube

## ⚠️ ATENÇÃO: USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION

**🔴 ESTE PROMPT REQUER O USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION**

**VOCÊ DEVE**:
1. ✅ Configurar o MCP AEM Documentation **ANTES** de qualquer outra ação
2. ✅ Ler a documentação oficial via MCP **POR PARTES** (documentos grandes > 1000 linhas)
3. ✅ **FILTRAR APENAS REGRAS FRONTEND/TEMPLATE** durante a leitura
4. ✅ Comparar com CSVs e **ADICIONAR REGRAS FRONTEND NOVAS**
5. ✅ **GERAR APENAS 1 ARQUIVO**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`

**🚫 NÃO É PERMITIDO**:
- ❌ Pular o uso do MCP
- ❌ Usar apenas os arquivos CSV
- ❌ Incluir regras que não são Frontend/Template
- ❌ Gerar múltiplos arquivos

---

## 🎯 Objetivo

Gerar **1 documento técnico específico** sobre as regras frontend e templates de qualidade de código do Cloud Manager para Adobe Experience Manager as a Cloud Service (AEMaaCS).

## 📁 ARQUIVO DE OUTPUT

**Arquivo**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`
- **Regras**: `ClassicUI*`, `Component*`, `Static*`, `Legacy*`, `Clientlib*`, Cloud Service compatibility para UI
- **Foco**: Templates, componentes UI, JavaScript, CSS, Touch UI vs Classic UI, clientlibs
- **Tecnologias**: `.js`, `.ts`, `.jsx`, `.tsx`, `.html`, `.jsp`, `.jspx`, `.htl`
- **Mínimo**: 200 linhas

---

## 🔍 CRITÉRIOS DE FILTRO FRONTEND & TEMPLATE

### ✅ **INCLUIR APENAS SE**:

#### 📂 **Prefixos Frontend Exatos**:
- `ClassicUIAuthoringMode` (modo de autoria Classic UI)
- `ComponentWithOnlyClassicUIDialog` (componentes com dialog Classic UI)
- `StaticTemplateUsage` (uso de templates estáticos)
- `LegacyFoundationComponentUsage` (componentes Foundation legados)
- `ClientlibProxyResource` (recursos clientlib proxy)
- `CloudServiceIncompatibleWorkflowProcess` (se relacionado a UI)

#### 🔑 **Palavras-chave Frontend**:
- UI, template, component, clientlib, dialog
- classic, touch, foundation, authoring, editor
- JavaScript, CSS, HTL, JSP, frontend
- workflow (se relacionado a UI), asset (se UI)

#### 💻 **Tecnologias Frontend**:
- .js, .ts, .jsx, .tsx (JavaScript/TypeScript)
- .html, .jsp, .jspx, .htl (templates)
- .css, .scss, .less (estilos)
- Componentes AEM, dialogs, clientlibs

### ❌ **EXCLUIR SEMPRE**:
- Regras Java puras (`java:S*`, `AEM Rules:*` com código Java)
- Regras de Dispatcher (`DOTRules:*`)
- Regras de Content estrutural (`BannedPath`, `PackageOverlaps`)
- Regras Oak Index (`OakIndex*`)

---

## 📖 FASE 1: LEITURA MCP COM FILTRO FRONTEND

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
   - **FILTRAR** apenas regras Frontend/Template (prefixos + palavras-chave)
   - **ESCREVER** imediatamente no arquivo Frontend
   - **IGNORAR** regras de outras categorias

3. **Continuar lendo** até documento completo

### 📝 PROCESSO DE FILTRO POR PARTE:

```markdown
# Para cada parte lida do MCP:

1. IDENTIFICAR regras Frontend/Template:
   - Prefixo ClassicUI* → INCLUIR
   - Prefixo Component* → INCLUIR  
   - Prefixo Static* → INCLUIR
   - Prefixo Legacy* → INCLUIR
   - Prefixo Clientlib* → INCLUIR
   - Palavra-chave "UI", "template" → INCLUIR
   - Prefixo java:S* → EXCLUIR
   - Prefixo DOTRules:* → EXCLUIR

2. ESCREVER no arquivo Frontend:
   - Apenas regras identificadas como Frontend/Template
   - Com exemplos de código HTML/JS/HTL
   - Com descrições detalhadas

3. CONTINUAR para próxima parte
```

### ✅ CHECKLIST DE LEITURA COM FILTRO:

- [ ] Leitura 1 (start_index=0): Filtrar e escrever regras Frontend
- [ ] Leitura 2 (start_index=10000): Filtrar e escrever regras Frontend
- [ ] Leitura 3 (start_index=20000): Filtrar e escrever regras Frontend
- [ ] Leitura 4 (start_index=30000): Filtrar e escrever regras Frontend
- [ ] Leitura 5+ (continuar até fim): Filtrar e escrever regras Frontend

---

## 📊 FASE 2: COMPARAÇÃO CSV COM FILTRO FRONTEND

### Arquivos CSV a Analisar:
- `assets/CodeQuality-rules-latest-AMS-2024-12-0.csv` (versão mais recente)
- `assets/CodeQuality-rules-latest-AMS.csv` (versão anterior)

### PROCESSO DE COMPARAÇÃO COM FILTRO:

1. **Ler CSV mais recente**
2. **Para cada regra no CSV**:
   - **APLICAR FILTRO FRONTEND** (prefixos + palavras-chave)
   - Se é regra Frontend E não foi documentada → **ADICIONAR**
   - Se não é regra Frontend → **IGNORAR**
3. **Identificar novas regras Frontend**
4. **Documentar apenas mudanças Frontend**

### FORMATO PARA REGRAS FRONTEND ADICIONAIS DO CSV:

```markdown
### [Rule Key] - [Nome da Regra]

| Atributo | Valor |
|----------|-------|
| **Key** | [rule_key] |
| **Type** | [Code Smell/Bug] |
| **Severity** | [Blocker/Major/Minor] |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | [versão se disponível] |

**Descrição**: [descrição do CSV ou inferida]

**Categoria**: Frontend & Template
**Fonte**: CSV (não encontrada na documentação oficial)

#### Exemplo de Código (se aplicável):
```html
<!-- Non-compliant template -->
[exemplo se disponível]

<!-- Compliant template -->  
[exemplo se disponível]
```
```

---

## 📝 ESTRUTURA DO ARQUIVO FRONTEND

### Arquivo: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`

```markdown
# Regras Frontend e Templates - AEM Cloud Service

**Última atualização:** [Data atual]
**Categoria:** Frontend & Template Rules
**Tecnologias:** `.js`, `.ts`, `.jsx`, `.tsx`, `.html`, `.jsp`, `.jspx`, `.htl`
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Frontend & Template

| Tipo | Quantidade |
|------|------------|
| **Total Regras Frontend** | XXX |
| **Regras UI** | XX |
| **Regras Template** | XX |
| **Regras Clientlib** | XX |
| **Regras Compatibilidade** | XX |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | XX |
| **Major** | XX |
| **Minor** | XX |

---

## 🎨 Regras de Interface (UI)

### ClassicUIAuthoringMode - Default Authoring Mode Should Not Be Classic UI

| Atributo | Valor |
|----------|-------|
| **Key** | ClassicUIAuthoringMode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: A configuração OSGi `com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl` define o modo de autoria padrão no AEM. Como a Classic UI foi deprecada desde o AEM 6.4, um problema é levantado quando o modo de autoria padrão é configurado para Classic UI.

#### Configuração Non-compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="sling:OsgiConfig"
    AuthoringUIModeService.default.authoring.ui.mode="CLASSIC"/>
```

#### Configuração Compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="sling:OsgiConfig"
    AuthoringUIModeService.default.authoring.ui.mode="TOUCH"/>
```

### ComponentWithOnlyClassicUIDialog - Components Should Have Touch UI Dialogs

[Detalhes da regra com exemplos]

[... todas as regras de UI ...]

---

## 📄 Regras de Templates

### StaticTemplateUsage - Usage of Static Templates is Discouraged

| Atributo | Valor |
|----------|-------|
| **Key** | StaticTemplateUsage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Embora o uso de templates estáticos tenha sido historicamente comum em projetos AEM, templates editáveis são altamente recomendados pois fornecem mais flexibilidade e suportam recursos adicionais não presentes em templates estáticos.

#### Template Estático Non-compliant:
```html
<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<html>
<head>
    <title>Static Template</title>
</head>
<body>
    <div>Static content</div>
</body>
</html>
```

#### Template Editável Compliant:
```html
<template data-sly-template.page="${@ wcmmode}">
    <div class="page" data-sly-use.page="com.example.PageModel">
        <div data-sly-resource="${'content' @ resourceType='wcm/foundation/components/parsys'}"></div>
    </div>
</template>
```

### LegacyFoundationComponentUsage - Usage of Legacy Foundation Components is Discouraged

[Detalhes da regra com exemplos]

[... todas as regras de templates ...]

---

## 📚 Regras Clientlib

### ClientlibProxyResource - Resources in Proxy-Enabled Client Libraries should be in resources folder

| Atributo | Valor |
|----------|-------|
| **Key** | ClientlibProxyResource |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | aem |
| **Since** | Version 2021.2.0 |

**Descrição**: Bibliotecas de cliente AEM podem conter recursos estáticos como imagens e fontes. Ao usar bibliotecas de cliente com proxy, esses recursos estáticos devem estar em uma pasta filha chamada `resources`.

#### Estrutura Non-compliant:
```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + images
        + myimage.jpg
```

#### Estrutura Compliant:
```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + resources
        + myimage.jpg
```

[... todas as regras clientlib ...]

---

## ☁️ Regras Compatibilidade Cloud Service UI

### CloudServiceIncompatibleWorkflowProcess - Usage of Cloud Service Incompatible Workflow Processes

[Detalhes da regra se relacionada a UI]

[... todas as regras de compatibilidade UI ...]

---

## 📚 Referências Frontend & Template

### Documentação Oficial
- [Page Templates - Editable](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)
- [Using Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)
- [HTL Specification](https://experienceleague.adobe.com/en/docs/experience-manager-htl/content/specification)

### Ferramentas
- [AEM Core Components](https://github.com/adobe/aem-core-wcm-components)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)

---

*Última atualização: [Data atual]*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Frontend & Template Rules*
```

---

## ✅ CHECKLIST DE EXECUÇÃO FRONTEND

### FASE 1 - Leitura MCP com Filtro Frontend:
- [ ] Leitura parte 1: Filtrar e escrever regras Frontend
- [ ] Leitura parte 2: Filtrar e escrever regras Frontend  
- [ ] Leitura parte 3: Filtrar e escrever regras Frontend
- [ ] Leitura parte 4: Filtrar e escrever regras Frontend
- [ ] Leitura parte 5+: Continuar até fim
- [ ] TODAS as regras Frontend da documentação escritas
- [ ] Buscas complementares executadas

### FASE 2 - Comparação CSV com Filtro Frontend:
- [ ] CSV mais recente lido e filtrado para Frontend
- [ ] CSV anterior lido e filtrado para Frontend
- [ ] Regras Frontend novas identificadas
- [ ] Regras Frontend adicionais escritas no arquivo
- [ ] Mudanças Frontend documentadas

### VALIDAÇÃO FINAL:
- [ ] Arquivo Frontend criado: `aemcs-sonarqube-frontend-rules-ptbr.md`
- [ ] Arquivo tem >200 linhas
- [ ] Apenas regras Frontend/Template incluídas
- [ ] Estatísticas Frontend corretas
- [ ] Exemplos de código incluídos

---

## 📈 MÉTRICAS DE SUCESSO FRONTEND

| Métrica Frontend | Mínimo Esperado |
|------------------|-----------------|
| **Regras Frontend documentadas** | 10-20 regras |
| **Linhas arquivo Frontend** | 200+ |
| **Regras UI** | 3-5 regras |
| **Regras Template** | 3-5 regras |
| **Regras Clientlib** | 2-3 regras |
| **Regras Compatibilidade** | 2-7 regras |
| **Exemplos de código** | 5+ |

---

## 🔐 VALIDAÇÃO FINAL FRONTEND

### 🚨 VALIDAÇÃO OBRIGATÓRIA:

```bash
# Executar antes de finalizar

echo "=== VALIDAÇÃO ARQUIVO FRONTEND ==="

# 1. Verificar arquivo existe
if [ -f "output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md" ]; then
    echo "✅ Arquivo Frontend existe"
else
    echo "❌ FALHA: Arquivo Frontend não existe"
    exit 1
fi

# 2. Verificar tamanho mínimo
FRONTEND_LINES=$(wc -l < output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md)
echo "Arquivo Frontend: $FRONTEND_LINES linhas"
[ "$FRONTEND_LINES" -gt 200 ] && echo "✅ Tamanho OK" || echo "❌ FALHA: <200 linhas"

# 3. Verificar conteúdo Frontend
echo "=== VERIFICAÇÃO DE CONTEÚDO ==="
grep -c "ClassicUI" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md && echo "✅ Regras ClassicUI* encontradas"
grep -c "Component" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md && echo "✅ Regras Component* encontradas"
grep -c "Static" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md && echo "✅ Regras Static* encontradas"
grep -c "Legacy" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md && echo "✅ Regras Legacy* encontradas"
grep -c "Clientlib" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md && echo "✅ Regras Clientlib* encontradas"

# 4. Verificar que NÃO tem regras de outras categorias
echo "=== VERIFICAÇÃO DE FILTRO ==="
if grep -q "java:S" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md; then
    echo "❌ FALHA: Contém regras Java (java:S*)"
else
    echo "✅ Filtro OK: Sem regras Java"
fi

if grep -q "DOTRules:" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md; then
    echo "❌ FALHA: Contém regras Dispatcher (DOTRules:*)"
else
    echo "✅ Filtro OK: Sem regras Dispatcher"
fi

if grep -q "BannedPath" output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md; then
    echo "❌ FALHA: Contém regras Content (BannedPath)"
else
    echo "✅ Filtro OK: Sem regras Content"
fi
```

### ✅ CRITÉRIOS DE SUCESSO:

- ✅ Arquivo `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md` existe
- ✅ Arquivo tem >200 linhas
- ✅ Contém apenas regras Frontend/Template
- ✅ NÃO contém regras de outras categorias
- ✅ Estatísticas Frontend corretas
- ✅ Exemplos de código incluídos

### ❌ FALHA SE:

- ❌ Arquivo não existe ou tem <200 linhas
- ❌ Contém regras não-Frontend (java:S*, DOTRules:*, BannedPath)
- ❌ Faltam regras Frontend importantes
- ❌ Estatísticas incorretas

---

## 📊 RESUMO EXECUTIVO FRONTEND

**AO FINALIZAR, REPORTAR:**

```
=== RELATÓRIO FRONTEND & TEMPLATE RULES ===

FASE 1 - MCP com Filtro Frontend:
✅ Partes lidas: [X] partes
✅ Regras Frontend extraídas: [X] regras
✅ Regras não-Frontend ignoradas: [X] regras
✅ Exemplos código obtidos: [X] exemplos

FASE 2 - CSV com Filtro Frontend:
✅ Regras Frontend no CSV: [X] regras
✅ Regras Frontend novas (não no MCP): [X] regras
✅ Mudanças Frontend identificadas: [X] mudanças

RESULTADO FINAL:
✅ Arquivo: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md
✅ Linhas: [X] linhas
✅ Regras Frontend: [X] regras
✅ Regras UI: [X] regras
✅ Regras Template: [X] regras  
✅ Regras Clientlib: [X] regras
✅ Regras Compatibilidade: [X] regras

FILTRO APLICADO:
✅ Incluídas: ClassicUI*, Component*, Static*, Legacy*, Clientlib*
✅ Excluídas: java:S*, DOTRules:*, BannedPath, OakIndex*

STATUS: [SUCESSO/FALHA]
```