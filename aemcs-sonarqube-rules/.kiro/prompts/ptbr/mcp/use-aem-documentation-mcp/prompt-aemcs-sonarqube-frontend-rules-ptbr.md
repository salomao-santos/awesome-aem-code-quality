# 🚨 PROMPT: Gerar Regras Frontend - AEM SonarQube

## ⚠️ ATENÇÃO: USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION

**🔴 ESTE PROMPT REQUER O USO OBRIGATÓRIO DO MCP AEM DOCUMENTATION**

**VOCÊ DEVE**:
1. ✅ Configurar o MCP AEM Documentation **ANTES** de qualquer outra ação
2. ✅ Ler a documentação oficial via MCP **POR PARTES** (documentos grandes > 1000 linhas)
3. ✅ **FILTRAR APENAS REGRAS FRONTEND** durante a leitura
4. ✅ Comparar com CSVs e **ADICIONAR REGRAS FRONTEND NOVAS**
5. ✅ **GERAR APENAS 1 ARQUIVO**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`
6. ✅ **VALIDAR ARQUIVO GERADO** com critérios específicos
7. ✅ **COPIAR PARA STEERING**: `.kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md`

**🚫 NÃO É PERMITIDO**:
- ❌ Pular o uso do MCP
- ❌ Usar apenas os arquivos CSV
- ❌ Incluir regras que não são Frontend/UI
- ❌ Gerar múltiplos arquivos
- ❌ Finalizar sem validação e cópia para steering

---

## 🎯 Objetivo

Gerar **1 documento técnico específico** sobre as regras Frontend, UI/UX e componentes de qualidade de código do Cloud Manager para Adobe Experience Manager as a Cloud Service (AEMaaCS).

## 📁 ARQUIVO DE OUTPUT

**Arquivo**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`
- **Regras**: Frontend, UI, UX, Componentes, Client-side
- **Foco**: HTML, CSS, JavaScript, HTL/Sightly, Client Libraries, Componentes AEM
- **Tecnologias**: `.html`, `.css`, `.js`, `.htl`, `.jsp`, clientlibs, componentes
- **Mínimo**: 200 linhas

---

## 🔍 CRITÉRIOS DE FILTRO FRONTEND

### ✅ **INCLUIR APENAS SE**:

#### 📂 **Prefixos Frontend Exatos**:
- `ClassicUI:*` (regras Classic UI)
- `Component:*` (regras de componentes)
- `ClientLibs:*` (regras Client Libraries)
- `HTL:*` (regras HTL/Sightly)
- `UI:*` (regras de interface)
- `Frontend:*` (regras frontend)

#### 🔑 **Palavras-chave Frontend & UI**:
- html, css, javascript, js, clientlibs
- htl, sightly, jsp, components, ui
- frontend, client-side, browser, dom
- responsive, accessibility, wcag, aria
- touch-ui, coral, granite, foundation

#### 💻 **Tecnologias Frontend**:
- .html (páginas HTML)
- .css (estilos CSS)
- .js (JavaScript)
- .htl (HTL/Sightly templates)
- .jsp (JSP pages)
- clientlibs (Client Libraries)
- components (Componentes AEM)

### ❌ **EXCLUIR SEMPRE**:
- Regras Java (`java:S*`, `AEM Rules:*`, `CQRules:*`)
- Regras Dispatcher (`DOTRules:Disp-*`, `DOTRules:Httpd-*`)
- Regras de Content (`BannedPath`, `PackageOverlaps`)
- Regras de Infrastructure/Backend

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
   - **FILTRAR** apenas regras Frontend (prefixos + palavras-chave)
   - **ESCREVER** imediatamente no arquivo Frontend
   - **IGNORAR** regras de outras categorias

3. **Continuar lendo** até documento completo

### 📝 PROCESSO DE FILTRO POR PARTE:

```markdown
# Para cada parte lida do MCP:

1. IDENTIFICAR regras Frontend:
   - Prefixo ClassicUI:* → INCLUIR
   - Prefixo Component:* → INCLUIR  
   - Prefixo ClientLibs:* → INCLUIR
   - Prefixo HTL:* → INCLUIR
   - Palavra-chave "html", "css", "javascript" → INCLUIR
   - Prefixo java:S* → EXCLUIR
   - Prefixo DOTRules:Disp-* → EXCLUIR

2. ESCREVER no arquivo Frontend:
   - Apenas regras identificadas como Frontend
   - Com exemplos de código HTML/CSS/JS/HTL
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
| **Type** | [Code Smell] |
| **Severity** | [Major/Minor] |
| **Tags** | beta, frontend, ui |

**Descrição**: [descrição do CSV ou inferida]

**Categoria**: Frontend & UI
**Fonte**: CSV (não encontrada na documentação oficial)

#### Exemplo de Código (se aplicável):
```html
<!-- Non-compliant code -->
[exemplo se disponível]

<!-- Compliant code -->  
[exemplo se disponível]
```
```

---

## 📝 ESTRUTURA DO ARQUIVO FRONTEND

### Arquivo: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`

```markdown
# Regras Frontend, UI/UX e Componentes - AEM Cloud Service

**Última atualização:** [Data atual]
**Categoria:** Frontend, UI/UX & Components Rules
**Tecnologias:** `.html`, `.css`, `.js`, `.htl`, `.jsp`, clientlibs, componentes
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Frontend, UI/UX & Components

| Tipo | Quantidade |
|------|------------|
| **Total Regras Frontend & UI** | XXX |
| **Regras Classic UI** | XX |
| **Regras Componentes** | XX |
| **Regras Client Libraries** | XX |
| **Regras HTL/Sightly** | XX |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Major** | XX |
| **Minor** | XX |

---

## 🎨 Regras Classic UI (ClassicUI:*)

### ClassicUI:AvoidClassicUI

| Atributo | Valor |
|----------|-------|
| **Key** | ClassicUI:AvoidClassicUI |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, ui, classic-ui |

**Descrição**: Evitar o uso da Classic UI do AEM, pois foi descontinuada em favor da Touch UI.

#### Código Non-compliant:
```html
<!-- Classic UI components -->
<div class="cq-editbar">
  <div class="cq-editbar-cell cq-editbar-cell-left">
    <a href="#" class="cq-editbar-button">Edit</a>
  </div>
</div>
```

#### Código Compliant:
```html
<!-- Touch UI components -->
<div data-sly-resource="${resource @ resourceType='foundation/components/parsys'}">
</div>
```

### ClassicUI:AvoidExtJSWidgets

[Detalhes da regra com exemplos]

[... todas as regras Classic UI ...]

---

## 🧩 Regras de Componentes (Component:*)

### Component:AvoidHardcodedPaths

| Atributo | Valor |
|----------|-------|
| **Key** | Component:AvoidHardcodedPaths |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, components, paths |

**Descrição**: Evitar caminhos hardcoded em componentes AEM para garantir portabilidade entre ambientes.

#### Código Non-compliant:
```htl
<div data-sly-use.model="com.example.MyModel">
  <img src="/content/dam/mysite/images/logo.png" alt="Logo">
</div>
```

#### Código Compliant:
```htl
<div data-sly-use.model="com.example.MyModel">
  <img src="${model.logoPath}" alt="Logo">
</div>
```

[... todas as regras de componentes ...]

---

## 📚 Regras Client Libraries (ClientLibs:*)

### ClientLibs:AvoidEmbedding

| Atributo | Valor |
|----------|-------|
| **Key** | ClientLibs:AvoidEmbedding |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, clientlibs, performance |

**Descrição**: Evitar embedding excessivo de client libraries para melhorar performance.

#### Configuração Non-compliant:
```xml
<!-- .content.xml -->
<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0"
    jcr:primaryType="cq:ClientLibraryFolder"
    categories="[mysite.all]"
    embed="[mysite.base,mysite.components,mysite.utils,mysite.vendor]"/>
```

#### Configuração Compliant:
```xml
<!-- .content.xml -->
<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0"
    jcr:primaryType="cq:ClientLibraryFolder"
    categories="[mysite.all]"
    dependencies="[mysite.base,mysite.components]"/>
```

[... todas as regras Client Libraries ...]

---

## 🔧 Regras HTL/Sightly (HTL:*)

### HTL:AvoidScriptlets

| Atributo | Valor |
|----------|-------|
| **Key** | HTL:AvoidScriptlets |
| **Type** | Code Smell |
| **Severity** | Major |
| **Tags** | beta, htl, sightly |

**Descrição**: Evitar uso de scriptlets Java em templates HTL, preferindo Use-API ou Sling Models.

#### Código Non-compliant:
```htl
<%
  String title = properties.get("jcr:title", "");
  if (title.isEmpty()) {
    title = "Default Title";
  }
%>
<h1><%= title %></h1>
```

#### Código Compliant:
```htl
<div data-sly-use.model="com.example.TitleModel">
  <h1>${model.title || 'Default Title'}</h1>
</div>
```

[... todas as regras HTL ...]

---

## 🎯 Regras de Acessibilidade & UX

### Accessibility:RequireAltText

| Atributo | Valor |
|----------|-------|
| **Categoria** | Accessibility & UX |
| **Tecnologia** | HTML, WCAG |
| **Foco** | Acessibilidade, SEO, UX |

**Descrição**: Garantir que todas as imagens tenham texto alternativo apropriado para acessibilidade.

#### Código Non-compliant:
```html
<img src="/content/dam/mysite/hero.jpg">
<img src="/content/dam/mysite/icon.png" alt="">
```

#### Código Compliant:
```html
<img src="/content/dam/mysite/hero.jpg" alt="Hero image showing our main product">
<img src="/content/dam/mysite/icon.png" alt="Company logo" role="img">
```

[... outras regras de acessibilidade ...]

---

## 📱 Regras Responsive & Performance

### Performance:OptimizeImages

| Atributo | Valor |
|----------|-------|
| **Categoria** | Performance & Responsive |
| **Tecnologia** | HTML, CSS, Images |
| **Foco** | Performance, Mobile, Core Web Vitals |

**Descrição**: Otimizar imagens para diferentes dispositivos e resoluções usando AEM Dynamic Media.

#### Código Non-compliant:
```html
<img src="/content/dam/mysite/large-image.jpg" width="100%">
```

#### Código Compliant:
```html
<picture>
  <source media="(max-width: 768px)" 
          srcset="/content/dam/mysite/large-image.jpg?width=768&quality=85">
  <source media="(max-width: 1200px)" 
          srcset="/content/dam/mysite/large-image.jpg?width=1200&quality=85">
  <img src="/content/dam/mysite/large-image.jpg?width=1920&quality=85" 
       alt="Descriptive text" loading="lazy">
</picture>
```

[... outras regras de performance ...]

---

## 📚 Referências Frontend, UI/UX & Components

### Documentação Oficial
- [HTL/Sightly Specification](https://experienceleague.adobe.com/en/docs/experience-manager-htl/content/specification)
- [AEM Core Components](https://experienceleague.adobe.com/en/docs/experience-manager-core-components/using/introduction)
- [Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)
- [Touch UI Development](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/touch-ui-concepts)
- [AEM Responsive Design](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/sites/developing/responsive)

### Ferramentas Frontend
- [AEM Developer Tools](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/tools/developer-mode)
- [HTL REPL](https://github.com/adobe/htl-repl)
- [AEM Brackets Extension](https://github.com/adobe/brackets-aem)
- [Core Components Library](https://www.aemcomponents.dev/)

### Padrões & Guidelines
- [AEM Style System](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/sites/authoring/siteandpage/style-system)
- [Editable Templates](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)
- [WCAG 2.1 Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)
- [AEM Accessibility Checklist](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/compliance/accessibility/quick-guide-wcag)

---

*Última atualização: [Data atual]*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Frontend, UI/UX & Components Rules*
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

### FASE 3 - Validação e Cópia para Steering:
- [ ] Arquivo Frontend criado e validado
- [ ] Arquivo copiado para `.kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md`

---

## 🔍 VALIDAÇÃO PÓS-GERAÇÃO

### 🚨 VALIDAÇÃO OBRIGATÓRIA APÓS GERAR O ARQUIVO

**APÓS GERAR O ARQUIVO DE OUTPUT, VOCÊ DEVE EXECUTAR ESTA VALIDAÇÃO:**

```bash
echo "=== VALIDAÇÃO FINAL DO ARQUIVO FRONTEND GERADO ==="

# 1. Verificar se arquivo foi criado
FILE_PATH="output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md"
if [ -f "$FILE_PATH" ]; then
    echo "✅ Arquivo criado com sucesso: $FILE_PATH"
else
    echo "❌ ERRO CRÍTICO: Arquivo não foi criado!"
    exit 1
fi

# 2. Verificar tamanho do arquivo
LINES=$(wc -l < "$FILE_PATH")
echo "📊 Arquivo contém: $LINES linhas"
if [ "$LINES" -gt 200 ]; then
    echo "✅ Tamanho adequado (>200 linhas)"
else
    echo "❌ ERRO: Arquivo muito pequeno (<200 linhas)"
fi

# 3. Verificar conteúdo específico Frontend
echo "=== VERIFICAÇÃO DE CONTEÚDO FRONTEND ==="
CLASSIC_RULES=$(grep -c "ClassicUI:" "$FILE_PATH" 2>/dev/null || echo "0")
COMPONENT_RULES=$(grep -c "Component:" "$FILE_PATH" 2>/dev/null || echo "0")
CLIENTLIB_RULES=$(grep -c "ClientLibs:" "$FILE_PATH" 2>/dev/null || echo "0")
HTL_RULES=$(grep -c "HTL:" "$FILE_PATH" 2>/dev/null || echo "0")

echo "📋 Regras Classic UI (ClassicUI:*): $CLASSIC_RULES"
echo "📋 Regras Componentes (Component:*): $COMPONENT_RULES"
echo "📋 Regras Client Libraries (ClientLibs:*): $CLIENTLIB_RULES"
echo "📋 Regras HTL (HTL:*): $HTL_RULES"

# 4. Verificar filtro aplicado corretamente
echo "=== VERIFICAÇÃO DE FILTRO ==="
JAVA_RULES=$(grep -c "java:S" "$FILE_PATH" 2>/dev/null || echo "0")
DISP_RULES=$(grep -c "DOTRules:Disp-" "$FILE_PATH" 2>/dev/null || echo "0")
BANNED_RULES=$(grep -c "BannedPath" "$FILE_PATH" 2>/dev/null || echo "0")

if [ "$JAVA_RULES" -eq 0 ] && [ "$DISP_RULES" -eq 0 ] && [ "$BANNED_RULES" -eq 0 ]; then
    echo "✅ Filtro aplicado corretamente - apenas regras Frontend"
else
    echo "❌ ERRO: Filtro não aplicado - contém regras não-Frontend"
    echo "   - Regras Java (java:S*): $JAVA_RULES"
    echo "   - Regras Dispatcher (DOTRules:Disp-*): $DISP_RULES"
    echo "   - Regras Content (BannedPath): $BANNED_RULES"
fi

# 5. Verificar estrutura do documento
echo "=== VERIFICAÇÃO DE ESTRUTURA ==="
if grep -q "# Regras Frontend, UI/UX e Componentes" "$FILE_PATH"; then
    echo "✅ Título principal encontrado"
else
    echo "❌ ERRO: Título principal não encontrado"
fi

if grep -q "## 📊 Estatísticas Frontend" "$FILE_PATH"; then
    echo "✅ Seção de estatísticas encontrada"
else
    echo "❌ ERRO: Seção de estatísticas não encontrada"
fi

# 6. Verificar exemplos de código
EXAMPLES=$(grep -c "```html\|```css\|```javascript\|```htl" "$FILE_PATH" 2>/dev/null || echo "0")
echo "📝 Exemplos de código encontrados: $EXAMPLES"
if [ "$EXAMPLES" -gt 5 ]; then
    echo "✅ Exemplos suficientes de código"
else
    echo "⚠️  AVISO: Poucos exemplos de código ($EXAMPLES)"
fi

echo "=== VALIDAÇÃO CONCLUÍDA ==="
```

---

## 📁 CÓPIA PARA STEERING

### 🚨 APÓS VALIDAÇÃO APROVADA, COPIAR ARQUIVO PARA STEERING

**EXECUTAR APÓS VALIDAÇÃO BEM-SUCEDIDA:**

```bash
echo "=== INICIANDO CÓPIA PARA STEERING ==="

# 1. Verificar se arquivo original existe e foi validado
FILE_PATH="output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md"
if [ ! -f "$FILE_PATH" ]; then
    echo "❌ ERRO: Arquivo original não encontrado para cópia!"
    exit 1
fi

# 2. Criar diretório steering se não existir
STEERING_DIR=".kiro/steering"
if [ ! -d "$STEERING_DIR" ]; then
    echo "📁 Criando diretório steering: $STEERING_DIR"
    mkdir -p "$STEERING_DIR"
else
    echo "📁 Diretório steering já existe: $STEERING_DIR"
fi

# 3. Definir caminho de destino
STEERING_FILE="$STEERING_DIR/output-aemcs-sonarqube-frontend-rules-ptbr.md"

# 4. Copiar arquivo para steering
echo "📋 Copiando arquivo para steering..."
cp "$FILE_PATH" "$STEERING_FILE"

# 5. Verificar se cópia foi bem-sucedida
if [ -f "$STEERING_FILE" ]; then
    echo "✅ Arquivo copiado com sucesso para steering!"
    echo "📁 Localização: $STEERING_FILE"
    
    # Verificar tamanho do arquivo copiado
    STEERING_LINES=$(wc -l < "$STEERING_FILE")
    echo "📊 Arquivo steering contém: $STEERING_LINES linhas"
    
    # Verificar se o conteúdo é idêntico
    if cmp -s "$FILE_PATH" "$STEERING_FILE"; then
        echo "✅ Conteúdo idêntico confirmado"
    else
        echo "⚠️  AVISO: Diferença detectada entre arquivos"
    fi
else
    echo "❌ ERRO: Falha ao copiar arquivo para steering!"
    exit 1
fi

echo "=== CÓPIA PARA STEERING CONCLUÍDA ==="
```

### ✅ CHECKLIST FINAL COMPLETO:

- [ ] **Arquivo gerado**: `output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md`
- [ ] **Validação executada**: Arquivo aprovado em todos os critérios
- [ ] **Diretório steering criado**: `.kiro/steering/` (se não existia)
- [ ] **Arquivo copiado para steering**: `.kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md`
- [ ] **Cópia verificada**: Conteúdo idêntico confirmado

---

## 📈 MÉTRICAS DE SUCESSO FRONTEND

| Métrica Frontend | Mínimo Esperado |
|------------------|-----------------|
| **Regras Frontend & UI documentadas** | 15-25 regras |
| **Linhas arquivo Frontend & UI** | 200+ |
| **Regras ClassicUI*** | 3-8 regras |
| **Regras Component*** | 5-10 regras |
| **Regras ClientLibs*** | 2-5 regras |
| **Regras HTL*** | 3-8 regras |
| **Exemplos de código** | 10+ |

---

## 📊 RESUMO EXECUTIVO FRONTEND

**AO FINALIZAR, REPORTAR:**

```
=== RELATÓRIO FRONTEND & UI/UX RULES ===

FASE 1 - MCP com Filtro Frontend:
✅ Partes lidas: [X] partes
✅ Regras Frontend extraídas: [X] regras
✅ Regras não-Frontend ignoradas: [X] regras
✅ Exemplos código obtidos: [X] exemplos

FASE 2 - CSV com Filtro Frontend:
✅ Regras Frontend no CSV: [X] regras
✅ Regras Frontend novas (não no MCP): [X] regras
✅ Mudanças Frontend identificadas: [X] mudanças

FASE 3 - Validação e Cópia:
✅ Arquivo validado: output-aemcs-sonarqube-rules/output-aemcs-sonarqube-frontend-rules-ptbr.md
✅ Linhas: [X] linhas
✅ Arquivo copiado para: .kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md

RESULTADO FINAL:
✅ Regras Frontend: [X] regras
✅ Regras ClassicUI:*: [X] regras
✅ Regras Component:*: [X] regras  
✅ Regras ClientLibs:*: [X] regras
✅ Regras HTL:*: [X] regras

FILTRO APLICADO:
✅ Incluídas: ClassicUI:*, Component:*, ClientLibs:*, HTL:*, UI:*
✅ Excluídas: java:S*, DOTRules:*, BannedPath, AEM Rules:*

STEERING:
✅ Arquivo disponível em: .kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md
✅ Pronto para uso como contexto em futuras sessões

STATUS: [SUCESSO/FALHA]
```

---

## 🎯 INSTRUÇÕES FINAIS

### 🚨 SEQUÊNCIA OBRIGATÓRIA DE EXECUÇÃO:

1. **CONFIGURAR MCP** - Ativar AEM Documentation MCP
2. **EXECUTAR FASE 1** - Leitura MCP com filtro Frontend
3. **EXECUTAR FASE 2** - Comparação CSV com filtro Frontend  
4. **EXECUTAR VALIDAÇÃO** - Validar arquivo gerado
5. **EXECUTAR CÓPIA** - Copiar para `.kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md`
6. **REPORTAR RESULTADO** - Resumo executivo final

### ✅ CRITÉRIOS DE SUCESSO FINAL:

- ✅ Arquivo original existe e tem >200 linhas
- ✅ Contém apenas regras Frontend (ClassicUI:*, Component:*, ClientLibs:*, HTL:*)
- ✅ NÃO contém regras de outras categorias
- ✅ Arquivo copiado para steering com sucesso
- ✅ Steering file disponível para uso futuro

**🎯 OBJETIVO FINAL: Arquivo `.kiro/steering/output-aemcs-sonarqube-frontend-rules-ptbr.md` criado e pronto para uso como contexto em futuras sessões do Kiro.**