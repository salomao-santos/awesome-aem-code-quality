# Regras Frontend, UI/UX e Componentes - AEM Cloud Service

**Última atualização:** 14 de dezembro de 2025
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
| **Total Regras Frontend & UI** | 6 |
| **Regras Classic UI** | 2 |
| **Regras Componentes** | 1 |
| **Regras Client Libraries** | 1 |
| **Regras HTL/Sightly** | 0 |
| **Regras UI/UX** | 2 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Major** | 1 |
| **Minor** | 5 |

---

## 🎨 Regras Classic UI

### ClassicUIAuthoringMode - Default Authoring Mode Should Not Be Classic UI

| Atributo | Valor |
|----------|-------|
| **Key** | ClassicUIAuthoringMode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: O modo de autoria padrão não deve ser configurado para Classic UI, pois foi descontinuada desde o AEM 6.4 em favor da Touch UI.

**Categoria**: Classic UI & Cloud Service Compatibility
**Fonte**: Documentação oficial + CSV

#### Configuração Non-compliant:
```xml
<!-- OSGi Configuration -->
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" 
          xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="sling:OsgiConfig"
    mode="classic"/>
```

#### Configuração Compliant:
```xml
<!-- OSGi Configuration -->
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" 
          xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="sling:OsgiConfig"
    mode="touch"/>
```

### ComponentWithOnlyClassicUIDialog - Components With Dialogs Should Have Touch UI Dialogs

| Atributo | Valor |
|----------|-------|
| **Key** | ComponentWithOnlyClassicUIDialog |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Componentes AEM com diálogo Classic UI devem também ter um diálogo Touch UI para compatibilidade com o Cloud Service, que não suporta Classic UI.

**Categoria**: Classic UI & Cloud Service Compatibility
**Fonte**: Documentação oficial + CSV

#### Estrutura Non-compliant:
```
+ mycomponent
  + dialog [cq:Dialog]
    + items [cq:Widget]
      + tabs [cq:TabPanel]
        + items [cq:WidgetCollection]
          + tab1 [cq:Panel]
```

#### Estrutura Compliant:
```
+ mycomponent
  + dialog [cq:Dialog]
    + items [cq:Widget]
      + tabs [cq:TabPanel]
  + cq:dialog [nt:unstructured]
    + content [granite/ui/components/coral/foundation/container]
      + items [nt:unstructured]
        + tabs [granite/ui/components/coral/foundation/tabs]
```

---

## 🧩 Regras de Componentes

### LegacyFoundationComponentUsage - Usage of Legacy Foundation Components is Discouraged

| Atributo | Valor |
|----------|-------|
| **Key** | LegacyFoundationComponentUsage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: O uso de Foundation Components legados (componentes sob `/libs/foundation`) foi descontinuado em favor dos Core Components.

**Categoria**: Components & Cloud Service Compatibility
**Fonte**: Documentação oficial + CSV

#### Código Non-compliant:
```htl
<!-- Usando Foundation Component -->
<div data-sly-resource="${'text' @ resourceType='/libs/foundation/components/text'}">
</div>

<!-- Herança de Foundation Component -->
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0"
    jcr:primaryType="cq:Component"
    sling:resourceSuperType="foundation/components/text"/>
```

#### Código Compliant:
```htl
<!-- Usando Core Component -->
<div data-sly-resource="${'text' @ resourceType='core/wcm/components/text/v2/text'}">
</div>

<!-- Herança de Core Component -->
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0"
    jcr:primaryType="cq:Component"
    sling:resourceSuperType="core/wcm/components/text/v2/text"/>
```

---

## 📚 Regras Client Libraries

### ClientlibProxyResource - Resources Contained in Proxy-Enabled Client Libraries Should Be in a folder named resources

| Atributo | Valor |
|----------|-------|
| **Key** | ClientlibProxyResource |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | aem |

**Descrição**: Quando usando client libraries com proxy habilitado, recursos estáticos como imagens e fontes devem estar contidos em uma pasta filha chamada `resources` para serem referenciados efetivamente nas instâncias de publicação.

**Categoria**: Client Libraries & Performance
**Fonte**: Documentação oficial + CSV

#### Estrutura Non-compliant:
```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + images
        + myimage.jpg
      + fonts
        + myfont.woff
```

#### Estrutura Compliant:
```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + resources
        + images
          + myimage.jpg
        + fonts
          + myfont.woff
```

#### Referência CSS Compliant:
```css
/* CSS referenciando recursos */
.my-component {
    background-image: url('resources/images/myimage.jpg');
    font-family: 'MyFont', sans-serif;
}

@font-face {
    font-family: 'MyFont';
    src: url('resources/fonts/myfont.woff') format('woff');
}
```

---

## 🎯 Regras de Acessibilidade & UX

### Accessibility Best Practices - Texto Alternativo para Imagens

| Atributo | Valor |
|----------|-------|
| **Categoria** | Accessibility & UX |
| **Tecnologia** | HTML, WCAG |
| **Foco** | Acessibilidade, SEO, UX |

**Descrição**: Garantir que todas as imagens tenham texto alternativo apropriado para acessibilidade e conformidade com WCAG.

#### Código Non-compliant:
```html
<!-- Imagem sem alt text -->
<img src="/content/dam/mysite/hero.jpg">

<!-- Alt text vazio inadequado -->
<img src="/content/dam/mysite/logo.png" alt="">

<!-- Alt text genérico -->
<img src="/content/dam/mysite/product.jpg" alt="image">
```

#### Código Compliant:
```html
<!-- Alt text descritivo -->
<img src="/content/dam/mysite/hero.jpg" 
     alt="Equipe trabalhando em escritório moderno com laptops">

<!-- Imagem decorativa -->
<img src="/content/dam/mysite/decoration.png" 
     alt="" role="presentation">

<!-- Logo com contexto -->
<img src="/content/dam/mysite/logo.png" 
     alt="Logo da Empresa XYZ - Página inicial">
```

### Responsive Design Best Practices - Otimização para Dispositivos Móveis

| Atributo | Valor |
|----------|-------|
| **Categoria** | Responsive & Performance |
| **Tecnologia** | HTML, CSS, Images |
| **Foco** | Performance, Mobile, Core Web Vitals |

**Descrição**: Otimizar imagens e layout para diferentes dispositivos e resoluções usando AEM Dynamic Media e técnicas responsivas.

#### Código Non-compliant:
```html
<!-- Imagem fixa sem otimização -->
<img src="/content/dam/mysite/large-image.jpg" width="100%">

<!-- CSS não responsivo -->
<style>
.container {
    width: 1200px;
    margin: 0 auto;
}
</style>
```

#### Código Compliant:
```html
<!-- Imagens responsivas com srcset -->
<picture>
  <source media="(max-width: 768px)" 
          srcset="/content/dam/mysite/image.jpg?width=768&quality=85">
  <source media="(max-width: 1200px)" 
          srcset="/content/dam/mysite/image.jpg?width=1200&quality=85">
  <img src="/content/dam/mysite/image.jpg?width=1920&quality=85" 
       alt="Descrição da imagem" 
       loading="lazy">
</picture>

<!-- CSS responsivo -->
<style>
.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
}

@media (max-width: 768px) {
    .container {
        padding: 0 0.5rem;
    }
}
</style>
```

---

## 🔧 Regras HTL/Sightly

### HTL Best Practices - Evitar Scriptlets Java

| Atributo | Valor |
|----------|-------|
| **Categoria** | HTL/Sightly Best Practices |
| **Tecnologia** | HTL, Sling Models |
| **Foco** | Separação de responsabilidades, Manutenibilidade |

**Descrição**: Evitar uso de scriptlets Java em templates HTL, preferindo Use-API ou Sling Models para lógica de negócio.

#### Código Non-compliant:
```htl
<!-- Scriptlet Java em HTL -->
<%
  String title = properties.get("jcr:title", "");
  if (title.isEmpty()) {
    title = "Título Padrão";
  }
%>
<h1><%= title %></h1>

<!-- Lógica complexa no template -->
<div data-sly-test="${resource.resourceType == 'mysite/components/text'}">
  <p data-sly-test="${properties.text && properties.text.length > 100}">
    ${properties.text @ context='html'}
  </p>
</div>
```

#### Código Compliant:
```htl
<!-- Usando Sling Model -->
<div data-sly-use.model="com.mysite.models.TitleModel">
  <h1>${model.title || 'Título Padrão'}</h1>
</div>

<!-- Lógica no Sling Model -->
<div data-sly-use.textModel="com.mysite.models.TextModel">
  <p data-sly-test="${textModel.shouldDisplay}">
    ${textModel.formattedText @ context='html'}
  </p>
</div>
```

#### Sling Model Correspondente:
```java
@Model(adaptables = Resource.class)
public class TitleModel {
    
    @ValueMapValue
    @Default(values = "Título Padrão")
    private String title;
    
    public String getTitle() {
        return StringUtils.isNotBlank(title) ? title : "Título Padrão";
    }
}
```

---

## 📱 Regras Performance Frontend

### Performance Optimization - Lazy Loading e Otimização de Recursos

| Atributo | Valor |
|----------|-------|
| **Categoria** | Performance & Loading |
| **Tecnologia** | HTML, JavaScript, CSS |
| **Foco** | Core Web Vitals, Loading Performance |

**Descrição**: Implementar lazy loading e otimização de recursos para melhorar performance e Core Web Vitals.

#### Código Non-compliant:
```html
<!-- Carregamento síncrono de recursos -->
<script src="/etc/clientlibs/mysite/js/heavy-library.js"></script>
<link rel="stylesheet" href="/etc/clientlibs/mysite/css/all-styles.css">

<!-- Imagens sem lazy loading -->
<img src="/content/dam/mysite/large-image.jpg">
```

#### Código Compliant:
```html
<!-- Carregamento assíncrono -->
<script src="/etc/clientlibs/mysite/js/critical.js"></script>
<script async src="/etc/clientlibs/mysite/js/non-critical.js"></script>

<!-- CSS crítico inline, não-crítico assíncrono -->
<style>
/* CSS crítico inline */
.above-fold { display: block; }
</style>
<link rel="preload" href="/etc/clientlibs/mysite/css/non-critical.css" 
      as="style" onload="this.onload=null;this.rel='stylesheet'">

<!-- Lazy loading de imagens -->
<img src="/content/dam/mysite/placeholder.jpg" 
     data-src="/content/dam/mysite/large-image.jpg"
     loading="lazy" 
     alt="Descrição da imagem">
```

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
- [Core Components Library](https://www.aemcomponents.dev/)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)

### Padrões & Guidelines
- [AEM Style System](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/sites/authoring/siteandpage/style-system)
- [Editable Templates](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)
- [WCAG 2.1 Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)
- [AEM Accessibility Checklist](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/compliance/accessibility/quick-guide-wcag)

### Migração e Modernização
- [Classic UI to Touch UI Migration](https://opensource.adobe.com/aem-modernize-tools/)
- [Foundation Components to Core Components](https://experienceleague.adobe.com/en/docs/experience-manager-core-components/using/developing/archetype/using.html)
- [Static to Editable Templates](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)

---

*Última atualização: 14 de dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Frontend, UI/UX & Components Rules*id-item {
    flex: 1;
    margin: 20px;
}

@media (max-width: 768px) {
    .grid {
        flex-direction: column;
    }
    .grid-item {
        margin: 10px 0;
    }
}
```

#### CSS Compliant:
```css
/* Mobile-first approach */
.container {
    width: 100%;
    padding: 1rem;
    margin: 0 auto;
}

@media (min-width: 768px) {
    .container {
        max-width: 1200px;
        padding: 2rem;
    }
}

.grid {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.grid-item {
    flex: 1;
}

@media (min-width: 768px) {
    .grid {
        flex-direction: row;
        gap: 2rem;
    }
}
```

### Touch-Friendly Interfaces

| Atributo | Valor |
|----------|-------|
| **Categoria** | Touch UX |
| **Tecnologia** | CSS, HTML |
| **Foco** | Touch targets, Mobile usability |

**Descrição**: Criar interfaces amigáveis para touch com targets adequados e feedback visual.

#### CSS Non-compliant:
```css
/* Targets muito pequenos */
.button {
    padding: 2px 4px;
    font-size: 12px;
}

.nav-link {
    display: inline;
    padding: 5px;
}

/* Sem feedback de touch */
.card {
    cursor: pointer;
}
```

#### CSS Compliant:
```css
/* Touch targets adequados (mínimo 44px) */
.button {
    min-height: 44px;
    min-width: 44px;
    padding: 12px 16px;
    font-size: 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.nav-link {
    display: block;
    padding: 12px 16px;
    min-height: 44px;
    text-decoration: none;
}

/* Feedback visual para touch */
.card {
    cursor: pointer;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.card:hover,
.card:focus {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.card:active {
    transform: translateY(0);
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* Estados de loading */
.button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.button.loading {
    position: relative;
    color: transparent;
}

.button.loading::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 20px;
    height: 20px;
    margin: -10px 0 0 -10px;
    border: 2px solid #fff;
    border-top-color: transparent;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    to { transform: rotate(360deg); }
}
```

---

## 🚀 Regras de Performance Frontend

### Critical CSS e Resource Loading

| Atributo | Valor |
|----------|-------|
| **Categoria** | Loading Performance |
| **Tecnologia** | HTML, CSS |
| **Foco** | First Contentful Paint, Core Web Vitals |

**Descrição**: Otimizar carregamento de CSS crítico e recursos para melhorar métricas de performance.

#### HTML Non-compliant:
```html
<!-- CSS bloqueante -->
<link rel="stylesheet" href="/etc/clientlibs/mysite/css/all-styles.css">
<link rel="stylesheet" href="/etc/clientlibs/mysite/css/components.css">
<link rel="stylesheet" href="/etc/clientlibs/mysite/css/layout.css">

<!-- JavaScript bloqueante -->
<script src="/etc/clientlibs/mysite/js/jquery.js"></script>
<script src="/etc/clientlibs/mysite/js/components.js"></script>
```

#### HTML Compliant:
```html
<!-- CSS crítico inline -->
<style>
/* Critical CSS - above the fold */
body { margin: 0; font-family: Arial, sans-serif; }
.header { background: #fff; padding: 1rem; }
.hero { min-height: 50vh; background: #f5f5f5; }
</style>

<!-- CSS não-crítico com preload -->
<link rel="preload" href="/etc/clientlibs/mysite/css/non-critical.css" 
      as="style" onload="this.onload=null;this.rel='stylesheet'">
<noscript>
    <link rel="stylesheet" href="/etc/clientlibs/mysite/css/non-critical.css">
</noscript>

<!-- JavaScript otimizado -->
<script>
// Critical JavaScript inline
document.documentElement.className += ' js-enabled';
</script>

<!-- Non-critical JavaScript -->
<script async src="/etc/clientlibs/mysite/js/components.js"></script>
<script defer src="/etc/clientlibs/mysite/js/analytics.js"></script>
```

---

*Última atualização: 14 de dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Frontend, UI/UX & Components Rules*