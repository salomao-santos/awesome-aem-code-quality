nteúdo e Pacotes - AEM Cloud Service

**Última atualização:** 14 de Dezembro de 2025
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
| **Total Regras Content** | 35 |
| **Regras de Pacote** | 8 |
| **Regras Oak Index** | 15 |
| **Regras OakPAL** | 3 |
| **Regras JCR** | 6 |
| **Regras Filter.xml** | 3 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | 2 |
| **Critical** | 1 |
| **Major** | 12 |
| **Minor** | 18 |
| **Info** | 2 |

---

## 🚫 Regras de Estrutura de Pacotes

### BannedPath - Customer packages should not install content under /libs

| Atributo | Valor |
|----------|-------|
| **Key** | BannedPath |
| **Type** | Bug |
| **Severity** | Blocker |
| **Since** | Version 2019.6.0 |

**Descrição**: A árvore de conteúdo `/libs` no repositório AEM deve ser considerada somente leitura pelos clientes. Modificar nós e propriedades sob `/libs` cria risco significativo para atualizações maiores e menores. Edições em `/libs` são feitas apenas pela Adobe através de canais oficiais.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ libs
  + foundation
    + components
      + myCustomComponent
        - jcr:primaryType="cq:Component"
        - componentGroup="My Project"
```

#### Estrutura Compliant:
```xml
+ apps
  + myproject
    + components
      + myCustomComponent
        - jcr:primaryType="cq:Component"
        - componentGroup="My Project"
        - sling:resourceSuperType="foundation/components/text"
```

---

### PackageOverlaps - Customer packages should not overlap

| Atributo | Valor |
|----------|-------|
| **Key** | PackageOverlaps |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Similar à regra de configurações OSGi duplicadas, este é um problema comum em projetos complexos onde o mesmo caminho de nó é escrito por múltiplos pacotes de conteúdo separados. Embora usar dependências de pacotes de conteúdo possa garantir um resultado consistente, é melhor evitar sobreposições completamente.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
<!-- Pacote A -->
+ apps
  + projectA
    + components
      + header
        - jcr:primaryType="cq:Component"

<!-- Pacote B -->
+ apps
  + projectA
    + components
      + header
        - jcr:primaryType="cq:Component"
        - componentGroup="Different Group"
```

#### Estrutura Compliant:
```xml
<!-- Pacote Único -->
+ apps
  + projectA
    + components
      + header
        - jcr:primaryType="cq:Component"
        - componentGroup="My Project"
```

---

## ⚙️ Regras de Configuração OSGi

### ConfigAndInstallShouldOnlyContainOsgiNodes - Paths with /config/ and /install/ should only be used for OSGi

| Atributo | Valor |
|----------|-------|
| **Key** | ConfigAndInstallShouldOnlyContainOsgiNodes |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Por razões de segurança, caminhos contendo `/config/` e `/install/` são legíveis apenas por usuários administrativos no AEM e devem ser usados apenas para configuração OSGi e bundles OSGi. Colocar outros tipos de conteúdo sob caminhos que contêm esses segmentos resulta em comportamento da aplicação que varia inadvertidamente entre usuários administrativos e não-administrativos.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    + config [nt:unstructured]
      + rtePlugins [nt:unstructured]
        + keys [nt:unstructured]
          - features="*"
```

#### Estrutura Compliant:
```xml
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    ./configPath = inplaceEditingConfig (String)
    + inplaceEditingConfig [nt:unstructured]
      + rtePlugins [nt:unstructured]
        + keys [nt:unstructured]
          - features="*"
```

---

### DuplicateOsgiConfigurations - Customer packages should not contain overlapping OSGi configurations

| Atributo | Valor |
|----------|-------|
| **Key** | DuplicateOsgiConfigurations |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Um problema comum que ocorre em projetos complexos é quando o mesmo componente OSGi é configurado múltiplas vezes. Esta questão cria uma ambiguidade sobre qual configuração é operável. Esta regra é "runmode-aware", identificando apenas problemas onde o mesmo componente é configurado múltiplas vezes no mesmo run mode ou combinação de run modes.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ apps
  + projectA
    + config
      + com.day.cq.commons.impl.ExternalizerImpl
        - domains="[local http://localhost:4502, author http://author.example.com]"
  + projectB
    + config
      + com.day.cq.commons.impl.ExternalizerImpl
        - domains="[local http://localhost:4502, publish http://publish.example.com]"
```

#### Estrutura Compliant:
```xml
+ apps
  + shared-config
    + config
      + com.day.cq.commons.impl.ExternalizerImpl
        - domains="[local http://localhost:4502, author http://author.example.com, publish http://publish.example.com]"
```

---

## 🌳 Regras Oak Index

### OakIndexLocation - Custom Search Index Definition Nodes Must Be Direct Children of /oak:index

| Atributo | Valor |
|----------|-------|
| **Key** | OakIndexLocation |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) sejam nós filhos diretos de `/oak:index`. Índices em outras localizações devem ser movidos para serem compatíveis com AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myproject
    + customIndex
      - jcr:primaryType="oak:QueryIndexDefinition"
      - type="lucene"
      - compatVersion=2
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myproject-customIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
    + indexRules
      + nt:base
        + properties
          + title
            - name="jcr:title"
            - propertyIndex=true
```

---

### IndexCompatVersion - Custom Search Index Definition Nodes Must Have a compatVersion of 2

| Atributo | Valor |
|----------|-------|
| **Key** | IndexCompatVersion |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) tenham a propriedade `compatVersion` definida como `2`. AEM Cloud Service não suporta nenhum outro valor.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=1
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
    + indexRules
      + nt:base
```

---

### IndexDescendantNodeType - Descendent Nodes Must Be Of Type nt:unstructured

| Atributo | Valor |
|----------|-------|
| **Key** | IndexDescendantNodeType |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Problemas difíceis de solucionar podem ocorrer quando um nó de definição de índice de busca customizado tem nós filhos não ordenados. Para evitar tais nós, Adobe recomenda que todos os nós descendentes de um nó `oak:QueryIndexDefinition` sejam do tipo `nt:unstructured`.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    + indexRules [cq:Page]
      + nt:base [cq:PageContent]
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    + indexRules [nt:unstructured]
      + nt:base [nt:unstructured]
        + properties [nt:unstructured]
```

---

### IndexRulesNode - Custom Search Index Definition Nodes Must Contain indexRules Child Node

| Atributo | Valor |
|----------|-------|
| **Key** | IndexRulesNode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Um nó de definição de índice de busca customizado propriamente definido deve incluir um nó filho chamado `indexRules` e este nó deve ter pelo menos um filho.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
    + indexRules
      + nt:base
        + properties
          + title
            - name="jcr:title"
```

---

### IndexName - Custom Search Index Definition Nodes Must Follow Naming Conventions

| Atributo | Valor |
|----------|-------|
| **Key** | IndexName |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas sejam nomeadas seguindo um padrão específico descrito na documentação de Content Search and Indexing.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + index
    - jcr:primaryType="oak:QueryIndexDefinition"
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myproject-customIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
```

---

### IndexType - Custom Search Index Definition Nodes Must Use the Index Type lucene

| Atributo | Valor |
|----------|-------|
| **Key** | IndexType |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) tenham uma propriedade `type` com o valor definido como `lucene`. Indexação usando tipos de índice legados deve ser atualizada antes da migração para AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="property"
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
```

---

### IndexSeedProperty - Custom Search Index Definition Nodes Must Not Contain seed Property

| Atributo | Valor |
|----------|-------|
| **Key** | IndexSeedProperty |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) de conter uma propriedade chamada `seed`. Indexação usando esta propriedade deve ser atualizada antes da migração para AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - seed=12345
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
```

---

### IndexReindexProperty - Custom Search Index Definition Nodes Must Not Contain reindex Property

| Atributo | Valor |
|----------|-------|
| **Key** | IndexReindexProperty |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) de conter uma propriedade chamada `reindex`. Indexação usando esta propriedade deve ser atualizada antes da migração para AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - reindex=true
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
```

---

## 🔍 Regras OakPAL

### CQBP-84 - Product APIs annotated with @ProviderType should not be implemented by customers

| Atributo | Valor |
|----------|-------|
| **Key** | CQBP-84 |
| **Type** | Bug |
| **Severity** | Critical |
| **Since** | Version 2018.7.0 |

**Descrição**: A API do AEM contém interfaces e classes Java que são destinadas apenas a serem usadas, mas não implementadas, por código customizado. Por exemplo, apenas o AEM implementa a interface `com.day.cq.wcm.api.Page`. Adicionar novos métodos a essas interfaces não afeta código existente, tornando a adição de novos métodos compatível com versões anteriores. No entanto, se código customizado implementa uma dessas interfaces, esse código customizado introduziu um risco de compatibilidade com versões anteriores para o cliente.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Código Non-compliant:
```java
import com.day.cq.wcm.api.Page;

public class DontDoThis implements Page {
// implementation here
}
```

#### Código Compliant:
```java
import com.day.cq.wcm.api.Page;

public class DoThis {
    @Reference
    private Page currentPage;
    
    // use the page, don't implement it
}
```

---

## 📦 Regras de Filter.xml

### FilterXmlModeAnalysis - Should not contain mode replacement for forbidden paths

| Atributo | Valor |
|----------|-------|
| **Key** | FilterXmlModeAnalysis |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2025.4.0 |

**Descrição**: O uso do modo "replacement" no file vault não é permitido para caminhos abaixo de `/content`; não deve ser usado para caminhos abaixo de `/etc` e `/var`. O modo "replace" sobrescreve conteúdo existente do repositório com conteúdo que vem do pacote. Pacotes que acionam esta ação não devem ser incluídos nos pacotes deployados através do Cloud Manager.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Filter.xml Non-compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workspaceFilter version="1.0">
    <filter root="/content/mysite" mode="replace"/>
    <filter root="/etc/designs/mysite" mode="replace"/>
</workspaceFilter>
```

#### Filter.xml Compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workspaceFilter version="1.0">
    <filter root="/content/mysite" mode="merge"/>
    <filter root="/etc/designs/mysite" mode="merge"/>
</workspaceFilter>
```

---

## 🎨 Regras de Componentes e UI

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

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ apps
  + myproject
    + components
      + mycomponent
        - jcr:primaryType="cq:Component"
        + dialog
          - jcr:primaryType="cq:Dialog"
          - title="My Component"
```

#### Estrutura Compliant:
```xml
+ apps
  + myproject
    + components
      + mycomponent
        - jcr:primaryType="cq:Component"
        + dialog
          - jcr:primaryType="cq:Dialog"
          - title="My Component (Classic)"
        + cq:dialog
          - jcr:primaryType="nt:unstructured"
          - sling:resourceType="cq/gui/components/authoring/dialog"
          - title="My Component"
```

---

### ClientlibProxyResource - Resources in Proxy-Enabled Client Libraries should be in resources folder

| Atributo | Valor |
|----------|-------|
| **Key** | ClientlibProxyResource |
| **Type** | Bug |
| **Severity** | Minor |
| **Tags** | aem |
| **Since** | Version 2021.2.0 |

**Descrição**: Bibliotecas de cliente AEM podem conter recursos estáticos como imagens e fontes. Conforme descrito na documentação Using Client-Side Libraries, ao usar bibliotecas de cliente com proxy, esses recursos estáticos devem estar contidos em uma pasta filha chamada `resources` para serem efetivamente referenciados nas instâncias de publicação.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Estrutura Non-compliant:
```xml
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      - categories="[myproject.base]"
      + images
        + myimage.jpg
      + fonts
        + myfont.woff
```

#### Estrutura Compliant:
```xml
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      - categories="[myproject.base]"
      + resources
        + images
          + myimage.jpg
        + fonts
          + myfont.woff
```

---

## ☁️ Regras de Compatibilidade Cloud Service

### CloudServiceIncompatibleWorkflowProcess - Usage of Cloud Service Incompatible Workflow Processes

| Atributo | Valor |
|----------|-------|
| **Key** | CloudServiceIncompatibleWorkflowProcess |
| **Type** | Code Smell |
| **Severity** | Blocker |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Com a mudança para Asset micro-services para processamento de assets no AEM Cloud Service, vários processos de workflow que eram usados em versões on-premise e AMS do AEM tornaram-se não suportados ou desnecessários. A ferramenta de migração no repositório GitHub AEM Assets as a Cloud Service pode ser usada para atualizar modelos de workflow durante a migração para AEM as a Cloud Service.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Workflow Non-compliant:
```xml
+ var
  + workflow
    + models
      + dam
        + update_asset
          + nodes
            + process_step
              - PROCESS="com.day.cq.dam.core.process.CreateAssetLanguageCopyProcess"
```

#### Workflow Compliant:
```xml
+ var
  + workflow
    + models
      + dam
        + update_asset_microservices
          + nodes
            + process_step
              - PROCESS="com.adobe.cq.dam.processor.nui.impl.NuiAssetProcessor"
```

---

### StaticTemplateUsage - Usage of Static Templates is Discouraged

| Atributo | Valor |
|----------|-------|
| **Key** | StaticTemplateUsage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |
| **Since** | Version 2021.2.0 |

**Descrição**: Embora o uso de templates estáticos tenha sido historicamente comum em projetos AEM, templates editáveis são altamente recomendados pois fornecem mais flexibilidade e suportam recursos adicionais não presentes em templates estáticos. A migração de templates estáticos para editáveis pode ser amplamente automatizada usando as AEM Modernization Tools.

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Template Non-compliant:
```xml
+ apps
  + myproject
    + templates
      + contentpage
        - jcr:primaryType="cq:Template"
        - label="Content Page"
        - allowedPaths="[/content/mysite(/.*)?]"
        + jcr:content
          - jcr:primaryType="cq:PageContent"
          - sling:resourceType="myproject/components/page"
```

#### Template Compliant:
```xml
+ conf
  + myproject
    + settings
      + wcm
        + templates
          + contentpage
            - jcr:primaryType="cq:Template"
            - enabled=true
            + structure
              + jcr:content
                - jcr:primaryType="cq:PageContent"
                - sling:resourceType="myproject/components/page"
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

**Categoria**: Content & Package
**Fonte**: MCP AEM Documentation

#### Componente Non-compliant:
```xml
+ apps
  + myproject
    + components
      + text
        - jcr:primaryType="cq:Component"
        - sling:resourceSuperType="foundation/components/text"
        - componentGroup="My Project"
```

#### Componente Compliant:
```xml
+ apps
  + myproject
    + components
      + text
        - jcr:primaryType="cq:Component"
        - sling:resourceSuperType="core/wcm/components/text/v2/text"
        - componentGroup="My Project"
```

---

## 🔧 Regras Adicionais do CSV

### ImmutableMutableMixedPackage - Packages Should Not Mix Mutable and Immutable Content

| Atributo | Valor |
|----------|-------|
| **Key** | ImmutableMutableMixedPackage |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Pacotes não devem misturar conteúdo mutável e imutável. Conteúdo imutável (como código, configurações) deve estar separado de conteúdo mutável (como páginas, assets).

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Package Non-compliant:
```xml
<!-- filter.xml misturando conteúdo -->
<workspaceFilter version="1.0">
    <filter root="/apps/myproject"/>
    <filter root="/content/mysite"/>
</workspaceFilter>
```

#### Package Compliant:
```xml
<!-- Pacote de código (imutável) -->
<workspaceFilter version="1.0">
    <filter root="/apps/myproject"/>
</workspaceFilter>

<!-- Pacote de conteúdo (mutável) - separado -->
<workspaceFilter version="1.0">
    <filter root="/content/mysite"/>
</workspaceFilter>
```

---

### SupportedRunmode - Only Supported Runmode Names Should Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | SupportedRunmode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Apenas nomes de runmode suportados devem ser usados no AEM Cloud Service. Runmodes customizados não são suportados.

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```xml
+ apps
  + myproject
    + config.customrunmode
      + com.example.MyService
        - enabled=true
```

#### Configuração Compliant:
```xml
+ apps
  + myproject
    + config.author
      + com.example.MyService
        - enabled=true
    + config.publish
      + com.example.MyService
        - enabled=false
```

---

### ClassicUIAuthoringMode - Default Authoring Mode Should Not Be Classic UI

| Atributo | Valor |
|----------|-------|
| **Key** | ClassicUIAuthoringMode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: A configuração OSGi `com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl` define o modo de autoria padrão no AEM. Como a Classic UI foi deprecada desde o AEM 6.4, um problema é levantado quando o modo de autoria padrão é configurado para Classic UI.

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```xml
+ apps
  + myproject
    + config
      + com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl
        - defaultAuthoringMode="classic"
```

#### Configuração Compliant:
```xml
+ apps
  + myproject
    + config
      + com.day.cq.wcm.core.impl.AuthoringUIModeServiceImpl
        - defaultAuthoringMode="touch"
```

---

### ReverseReplication - Reverse Replication Agents Should Not Be Used

| Atributo | Valor |
|----------|-------|
| **Key** | ReverseReplication |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Tags** | aem, cloud-service-compatibility |

**Descrição**: Suporte para replicação reversa não está disponível em deployments do Cloud Service, conforme descrito nas Release Notes: Removal of Replication Agents. Clientes usando replicação reversa devem entrar em contato com a Adobe para soluções alternativas.

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Configuração Non-compliant:
```xml
+ etc
  + replication
    + agents.publish
      + reverse
        - jcr:primaryType="cq:Page"
        + jcr:content
          - enabled=true
          - transportUri="http://localhost:4503/bin/receive"
```

#### Configuração Compliant:
```xml
<!-- Remover agentes de replicação reversa -->
<!-- Usar Sling Content Distribution para casos de uso similares -->
```

---

## 🔍 Regras Avançadas de Oak Index (CSV)

### IndexNotUnderUIContent - Index definition nodes must not be deployed in UI content package

| Atributo | Valor |
|----------|-------|
| **Key** | IndexNotUnderUIContent |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas de serem deployadas no pacote UI Content. Índices devem estar em pacotes de código (imutável).

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Package Non-compliant:
```xml
<!-- UI Content Package -->
<workspaceFilter version="1.0">
    <filter root="/content"/>
    <filter root="/oak:index/myCustomIndex"/>
</workspaceFilter>
```

#### Package Compliant:
```xml
<!-- Code Package -->
<workspaceFilter version="1.0">
    <filter root="/apps"/>
    <filter root="/oak:index/myCustomIndex"/>
</workspaceFilter>

<!-- UI Content Package -->
<workspaceFilter version="1.0">
    <filter root="/content"/>
</workspaceFilter>
```

---

### CustomFulltextIndexesOfTheDamAssetCheck - damAssetLucene prefix required

| Atributo | Valor |
|----------|-------|
| **Key** | CustomFulltextIndexesOfTheDamAssetCheck |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice full-text customizadas do tipo `damAssetLucene` de serem prefixadas com qualquer coisa diferente de `damAssetLucene`.

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Índice Non-compliant:
```xml
+ oak:index
  + myproject-assetIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - async="[async,fulltext-async]"
    - compatVersion=2
```

#### Índice Compliant:
```xml
+ oak:index
  + damAssetLucene-myproject
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - async="[async,fulltext-async]"
    - compatVersion=2
```

---

### DuplicateNameProperty - No duplicate property names

| Atributo | Valor |
|----------|-------|
| **Key** | DuplicateNameProperty |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas de conter propriedades com o mesmo nome.

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Índice Non-compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    + indexRules
      + nt:base
        + properties
          + title
            - name="jcr:title"
          + title
            - name="dc:title"
```

#### Índice Compliant:
```xml
+ oak:index
  + myCustomIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    + indexRules
      + nt:base
        + properties
          + jcrTitle
            - name="jcr:title"
          + dcTitle
            - name="dc:title"
```

---

### RestrictIndexCustomization - OOTB indexes cannot be customized

| Atributo | Valor |
|----------|-------|
| **Key** | RestrictIndexCustomization |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe modificações não autorizadas dos seguintes índices OOTB: `nodetypeLucene`, `slingResourceResolver`, `socialLucene`, `appsLibsLucene`, `authorizables`, `pathReference`.

**Categoria**: Content & Package
**Fonte**: CSV (não encontrada na documentação oficial)

#### Modificação Non-compliant:
```xml
+ oak:index
  + nodetypeLucene
    - jcr:primaryType="oak:QueryIndexDefinition"
    - customProperty="customValue"
```

#### Abordagem Compliant:
```xml
+ oak:index
  + myproject-customIndex
    - jcr:primaryType="oak:QueryIndexDefinition"
    - type="lucene"
    - compatVersion=2
```

---

## 📚 Referências Content & Package

### Documentação Oficial
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Content Search and Indexing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing)
- [Package Manager](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/sites/administering/contentmanagement/package-manager)
- [Page Templates - Editable](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/platform/templates/page-templates-editable)
- [Using Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)

### Ferramentas
- [OakPAL Documentation](https://github.com/adamcin/oakpal)
- [FileVault Documentation](https://jackrabbit.apache.org/filevault/)
- [AEM Modernization Tools](https://opensource.adobe.com/aem-modernize-tools/)
- [AEM Assets Cloud Migration Tool](https://github.com/adobe/aem-cloud-migration)

### Arquivos CSV de Referência
- `CodeQuality-rules-latest-AMS-2024-12-0.csv` - Versão mais recente (SonarQube 9.9)
- `CodeQuality-rules-latest-AMS.csv` - Versão anterior

---

*Última atualização: 14 de Dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Content & Package Rules*