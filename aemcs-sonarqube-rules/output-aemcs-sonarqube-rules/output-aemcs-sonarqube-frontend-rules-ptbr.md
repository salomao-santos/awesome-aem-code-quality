tl`  
**Fontes:**
- Adobe Experience League (documentação oficial via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Frontend & Template

| Tipo | Quantidade |
|------|------------|
| **Total Regras Frontend** | 12 |
| **Regras UI** | 4 |
| **Regras Template** | 3 |
| **Regras Clientlib** | 1 |
| **Regras Compatibilidade** | 4 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | 1 |
| **Major** | 0 |
| **Minor** | 11 |

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

---

### ComponentWithOnlyClassicUIDialog - Components Should Have Touch UI Dialogs

| Atributo | Valor |
|----------|-------|
| **Key** | ComponentWithOnlyClassicUIDialog |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: Componentes AEM com um diálogo Classic UI também devem ter um diálogo Touch UI para autoria otimizada e compatibilidade com o modelo de deployment do Cloud Service, que não suporta Classic UI. Esta regra verifica os seguintes cenários:

- Um componente com diálogo Classic UI (nó filho `dialog`) deve ter um diálogo Touch UI correspondente (nó filho `cq:dialog`)
- Um componente com diálogo de design Classic UI (nó `design_dialog`) deve ter um diálogo de design Touch UI correspondente (nó filho `cq:design_dialog`)
- Um componente com ambos os diálogos Classic UI deve ter ambos os diálogos Touch UI correspondentes

#### Estrutura Non-compliant:
```
+ apps
  + myproject
    + components
      + mycomponent
        + dialog [cq:Dialog]
          - jcr:primaryType = "cq:Dialog"
          - xtype = "panel"
        // Falta cq:dialog para Touch UI
```

#### Estrutura Compliant:
```
+ apps
  + myproject
    + components
      + mycomponent
        + dialog [cq:Dialog]
          - jcr:primaryType = "cq:Dialog"
          - xtype = "panel"
        + cq:dialog [nt:unstructured]
          - jcr:primaryType = "nt:unstructured"
          - sling:resourceType = "cq/gui/components/authoring/dialog"
```

#### Exemplo HTL Touch UI Dialog:
```html
<div data-sly-use.dialog="com.adobe.cq.wcm.core.components.models.form.Container">
    <coral-dialog variant="default">
        <coral-dialog-header>
            <coral-dialog-title>Component Configuration</coral-dialog-title>
        </coral-dialog-header>
        <coral-dialog-content>
            <coral-tabview>
                <coral-tablist>
                    <coral-tab>Properties</coral-tab>
                </coral-tablist>
                <coral-panelstack>
                    <coral-panel>
                        <!-- Touch UI form fields -->
                    </coral-panel>
                </coral-panelstack>
            </coral-tabview>
        </coral-dialog-content>
    </coral-dialog>
</div>
```

---

### ReverseReplication - Reverse Replication Agents Should Not Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | ReverseReplication |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2020.5.0 |

**Descrição**: Suporte para replicação reversa não está disponível em deployments do Cloud Service. Clientes usando replicação reversa devem contatar a Adobe para soluções alternativas.

#### Configuração Non-compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="cq:ReplicationAgent"
    enabled="{Boolean}true"
    transportUri="http://localhost:4503/bin/receive?sling:authRequestLogin=1"
    transportUser="admin"
    transportPassword="admin"
    reverseReplication="{Boolean}true"/>
```

#### Alternativa Compliant (Event-based):
```java
@Component(service = EventHandler.class, immediate = true,
    property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationEvent.EVENT_TOPIC
    })
public class CustomReplicationHandler implements EventHandler {
    
    @Override
    public void handleEvent(Event event) {
        // Handle replication events without reverse replication
        String path = (String) event.getProperty("path");
        // Custom logic for content synchronization
    }
}
```

---

### ConfigAndInstallShouldOnlyContainOsgiNodes - Config and Install Folders Should Only Contain OSGi Nodes

| Atributo | Valor |
|----------|-------|
| **Key** | ConfigAndInstallShouldOnlyContainOsgiNodes |
| **Type** | Bug |
| **Severity** | Major |
| **Tags** | aem |
| **Since** | Version 2019.6.0 |

**Descrição**: Por razões de segurança, caminhos contendo `/config/` e `/install/` são legíveis apenas por usuários administrativos no AEM e devem ser usados apenas para configuração OSGi e bundles OSGi. Um problema comum é o uso de nós chamados `config` dentro de caixas de diálogo de componentes ou ao especificar a configuração do rich text editor para edição inline.

#### Estrutura Non-compliant:
```
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    + config [nt:unstructured]
      + rtePlugins [nt:unstructured]
        + format [nt:unstructured]
          - features = ["bold", "italic"]
```

#### Estrutura Compliant:
```
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    ./configPath = "inplaceEditingConfig" (String)
    + inplaceEditingConfig [nt:unstructured]
      + rtePlugins [nt:unstructured]
        + format [nt:unstructured]
          - features = ["bold", "italic"]
```

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

**Descrição**: Embora o uso de templates estáticos tenha sido historicamente comum em projetos AEM, templates editáveis são altamente recomendados pois fornecem mais flexibilidade e suportam recursos adicionais não presentes em templates estáticos. A migração de templates estáticos para editáveis pode ser amplamente automatizada usando as AEM Modernization Tools.

#### Template Estático Non-compliant:
```html
<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<html>
<head>
    <title>Static Template</title>
    <cq:include script="/libs/wcm/core/components/init/init.jsp"/>
</head>
<body>
    <div class="page">
        <cq:include path="content" resourceType="foundation/components/parsys"/>
    </div>
</body>
</html>
```

#### Template Editável Compliant (HTL):
```html
<template data-sly-template.page="${@ wcmmode}">
    <div class="page" data-sly-use.page="com.example.models.PageModel">
        <sly data-sly-use.template="core/wcm/components/commons/v1/templates.html"/>
        <sly data-sly-call="${template.page @ page=page}"/>
        
        <main class="main-content">
            <div data-sly-resource="${'content' @ resourceType='wcm/foundation/components/parsys'}"></div>
        </main>
    </div>
</template>
```

#### Configuração Template Editável:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="cq:Template"
    jcr:title="Editable Page Template"
    status="enabled"
    ranking="{Long}100">
    <jcr:content
        jcr:primaryType="cq:PageContent"
        sling:resourceType="myproject/components/page"/>
    <policies jcr:primaryType="nt:unstructured">
        <jcr:content
            jcr:primaryType="nt:unstructured"
            sling:resourceType="wcm/core/components/policies/mappings">
            <content
                jcr:primaryType="nt:unstructured"
                sling:resourceType="wcm/core/components/policies/mapping"
                cq:policy="myproject/components/policies/content"/>
        </jcr:content>
    </policies>
</jcr:root>
```

---

### LegacyFoundationComponentUsage - Usage of Legacy Foundation Components is Discouraged

| Atributo | Valor |
|----------|-------|
| **Key** | LegacyFoundationComponentUsage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Os Foundation Components legados (componentes sob `/libs/foundation`) foram deprecados por várias releases do AEM em favor dos Core Components. O uso dos Foundation Components legados como base para componentes customizados, seja por overlay ou herança, é desencorajado e deve ser convertido para o core component correspondente.

#### Uso Non-compliant (Foundation Component):
```html
<div data-sly-use.text="foundation/components/text">
    <div class="text">
        ${text.text @ context='html'}
    </div>
</div>
```

#### Uso Compliant (Core Component):
```html
<div data-sly-use.text="core/wcm/components/text/v2/text">
    <div class="cmp-text" data-cmp-is="text">
        <div class="cmp-text__richtext" data-sly-test="${text.richText}">
            ${text.text @ context='html'}
        </div>
        <div class="cmp-text__plaintext" data-sly-test="${!text.richText}">
            ${text.text @ context='text'}
        </div>
    </div>
</div>
```

#### Migração de Componente Customizado:
```java
// Non-compliant: Extending Foundation Component
@Model(adaptables = Resource.class)
public class CustomText extends com.day.cq.wcm.foundation.Text {
    // Custom implementation
}

// Compliant: Using Core Component
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class CustomText implements ComponentExporter {
    
    @Self
    private SlingHttpServletRequest request;
    
    @Inject
    private com.adobe.cq.wcm.core.components.models.Text coreText;
    
    public String getText() {
        return coreText.getText();
    }
    
    @Override
    public String getExportedType() {
        return "myproject/components/text";
    }
}
```

---

### ImmutableMutableMixedPackage - Packages Should Not Mix Mutable and Immutable Content

| Atributo | Valor |
|----------|-------|
| **Key** | ImmutableMutableMixedPackage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Pacotes não devem misturar conteúdo mutável e imutável. Conteúdo imutável (como código, templates, componentes) deve ser separado do conteúdo mutável (como páginas, assets).

#### Estrutura Non-compliant:
```
+ myproject-all.zip
  + apps/myproject/components/  (imutável)
  + content/myproject/pages/    (mutável)
  + etc/designs/myproject/      (imutável)
```

#### Estrutura Compliant:
```
+ myproject-code.zip
  + apps/myproject/components/  (apenas imutável)
  + etc/designs/myproject/

+ myproject-content.zip  
  + content/myproject/pages/    (apenas mutável)
```

---

## 📚 Regras Clientlib

### ClientlibProxyResource - Resources in Proxy-Enabled Client Libraries Should Be in Resources Folder

| Atributo | Valor |
|----------|-------|
| **Key** | ClientlibProxyResource |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | aem |
| **Since** | Version 2021.2.0 |

**Descrição**: Bibliotecas de cliente AEM podem conter recursos estáticos como imagens e fontes. Ao usar bibliotecas de cliente com proxy, esses recursos estáticos devem estar em uma pasta filha chamada `resources` para serem efetivamente referenciados nas instâncias de publicação.

#### Estrutura Non-compliant:
```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      - categories="[myproject.base]"
      + css
        - base.css
      + js  
        - main.js
      + images
        + logo.png
        + background.jpg
```

#### Estrutura Compliant:
```
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      - categories="[myproject.base]"
      + css
        - base.css
      + js
        - main.js  
      + resources
        + images
          + logo.png
          + background.jpg
```

#### Exemplo CSS com Recursos:
```css
/* Non-compliant: referência direta */
.header {
    background-image: url('../images/logo.png');
}

/* Compliant: referência via resources */
.header {
    background-image: url('../resources/images/logo.png');
}
```

#### Configuração Clientlib:
```
#base=.
categories=[myproject.base]
allowProxy=true

# CSS files
css/base.css
css/components.css

# JavaScript files  
js/main.js
js/utils.js

# Resources (images, fonts, etc.)
resources/images/logo.png
resources/fonts/custom-font.woff2
```

---

## ☁️ Regras Compatibilidade Cloud Service UI

### CloudServiceIncompatibleWorkflowProcess - Usage of Cloud Service Incompatible Workflow Processes

| Atributo | Valor |
|----------|-------|
| **Key** | CloudServiceIncompatibleWorkflowProcess |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Com a mudança para Asset micro-services para processamento de assets no AEM Cloud Service, vários processos de workflow que eram usados em versões on-premise e AMS do AEM tornaram-se não suportados ou desnecessários. Isso inclui workflows relacionados à interface de usuário e processamento de assets.

#### Processos Workflow Non-compliant:
```xml
<!-- Workflow model com processos incompatíveis -->
<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="cq:WorkflowModel"
    jcr:title="Asset Processing Workflow">
    <nodes jcr:primaryType="nt:unstructured">
        <node0
            jcr:primaryType="cq:WorkflowNode"
            title="DAM Update Asset"
            type="PROCESS"
            process="com.day.cq.dam.core.process.DamUpdateAssetWorkflowProcess"/>
        <node1
            jcr:primaryType="cq:WorkflowNode" 
            title="Create Renditions"
            type="PROCESS"
            process="com.day.cq.dam.core.process.CreateRenditionsProcess"/>
    </nodes>
</jcr:root>
```

#### Alternativa Compliant (Asset Processing Profiles):
```xml
<!-- Processing Profile para Cloud Service -->
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0"
    jcr:primaryType="dam:AssetProcessingProfile"
    jcr:title="Custom Asset Processing">
    <renditions jcr:primaryType="nt:unstructured">
        <web
            jcr:primaryType="dam:AssetRendition"
            fmt="jpeg"
            width="1200"
            height="800"
            quality="85"/>
        <thumbnail
            jcr:primaryType="dam:AssetRendition"
            fmt="jpeg"
            width="300"
            height="200"
            quality="90"/>
    </renditions>
</jcr:root>
```

---

### SupportedRunmode - Only Supported Runmode Names Should Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | SupportedRunmode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Apenas nomes de runmode suportados devem ser usados no AEM Cloud Service. Runmodes customizados ou não suportados podem causar problemas de deployment.

#### Runmodes Non-compliant:
```
+ apps
  + myproject
    + config.dev        // runmode customizado não suportado
    + config.staging    // runmode customizado não suportado  
    + config.prod       // runmode customizado não suportado
```

#### Runmodes Compliant:
```
+ apps
  + myproject
    + config                    // configuração padrão
    + config.author             // runmode suportado
    + config.publish            // runmode suportado
    + config.author.dev         // combinação suportada
    + config.publish.stage      // combinação suportada
    + config.author.prod        // combinação suportada
    + config.publish.prod       // combinação suportada
```

#### Exemplo Configuração OSGi por Runmode:
```xml
<!-- config.author/com.example.MyService.cfg.json -->
{
  "service.enabled": true,
  "author.specific.setting": "value"
}

<!-- config.publish/com.example.MyService.cfg.json -->
{
  "service.enabled": true,
  "publish.specific.setting": "value"
}
```

---

## 🔧 Regras de Configuração UI

### CQRules:CQBP-71 - Do not hardcode paths using String literals

| Atributo | Valor |
|----------|-------|
| **Key** | CQRules:CQBP-71 |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | cqsoftwarequality |
| **Since** | Version 2018.4.0 |

**Descrição**: Caminhos começando com `/libs` e `/apps` geralmente não devem ser hardcoded em componentes e templates. Esses caminhos são tipicamente armazenados relativos ao caminho de busca do Sling.

#### Código Non-compliant:
```java
// Em um modelo Sling
public boolean isTextComponent(Resource resource) {
    return resource.isResourceType("/libs/foundation/components/text");
}

// Em HTL template
<div data-sly-test="${resource.resourceType == '/apps/myproject/components/text'}">
    <!-- content -->
</div>
```

#### Código Compliant:
```java
// Usando caminho relativo
public boolean isTextComponent(Resource resource) {
    return resource.isResourceType("foundation/components/text");
}

// Em HTL template
<div data-sly-test="${resource.resourceType == 'myproject/components/text'}">
    <!-- content -->
</div>
```

#### Exemplo HTL com Caminhos Relativos:
```html
<template data-sly-template.component="${@ resourceType}">
    <sly data-sly-resource="${'.' @ resourceType=resourceType}"/>
</template>

<!-- Uso do template -->
<sly data-sly-call="${component @ resourceType='core/wcm/components/text/v2/text'}"/>
```

---

## 📚 Referências Frontend & Template

### Documentação Oficial
- [Page Templates - Editable](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)
- [Using Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)
- [HTL Specification](https://experienceleague.adobe.com/en/docs/experience-manager-htl/content/specification)
- [Touch UI Dialogs](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/components/touch-ui-concepts)
- [AEM Core Components](https://experienceleague.adobe.com/en/docs/experience-manager-core-components/using/introduction)

### Ferramentas
- [AEM Core Components](https://github.com/adobe/aem-core-wcm-components)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)
- [AEM Project Archetype](https://github.com/adobe/aem-project-archetype)

### Guias de Migração
- [Classic UI to Touch UI Migration](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/components/touch-ui-migration)
- [Foundation Components to Core Components](https://experienceleague.adobe.com/en/docs/experience-manager-learn/getting-started-wknd-tutorial-develop/project-archetype/component-basics)

---

## 🔄 Changelog Frontend & Template

### Versão 2025.2.0 (Fevereiro 2025)
- Atualização para SonarQube 9.9
- Migração de chaves `squid:*` para `java:*` (não afeta regras Frontend)
- Novas regras de compatibilidade Cloud Service

### Versão 2021.2.0
- Introdução das regras `StaticTemplateUsage` e `LegacyFoundationComponentUsage`
- Nova regra `ClientlibProxyResource` para bibliotecas de cliente
- Regras de compatibilidade Cloud Service expandidas

### Versão 2020.5.0  
- Introdução das regras `ClassicUIAuthoringMode` e `ComponentWithOnlyClassicUIDialog`
- Foco em migração de Classic UI para Touch UI
- Regras de compatibilidade Cloud Service iniciais

---

*Última atualização: 14 de Dezembro de 2025*  
*Gerado via MCP AEM Documentation + análise de CSVs*  
*Categoria: Frontend & Template Rules*