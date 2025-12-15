sv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Estatísticas Content & Package

| Tipo | Quantidade |
|------|------------|
| **Total Regras Content** | 35 |
| **Regras de Pacote** | 8 |
| **Regras Oak Index** | 15 |
| **Regras OakPAL** | 7 |
| **Regras JCR** | 5 |

### Por Severidade

| Severidade | Quantidade |
|------------|------------|
| **Blocker** | 3 |
| **Critical** | 1 |
| **Major** | 18 |
| **Minor** | 13 |
| **Info** | 0 |

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
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ libs
  + foundation
    + components
      + myCustomComponent
        + .content.xml
        + dialog.xml
```

#### Estrutura Compliant:
```xml
+ apps
  + myproject
    + components
      + myCustomComponent
        + .content.xml
        + cq:dialog
```

### PackageOverlaps - Customer packages should not overlap

| Atributo | Valor |
|----------|-------|
| **Key** | PackageOverlaps |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Similar à regra de configurações OSGi duplicadas, este é um problema comum em projetos complexos onde o mesmo caminho de nó é escrito por múltiplos pacotes de conteúdo separados. Embora usar dependências de pacotes de conteúdo possa garantir um resultado consistente, é melhor evitar sobreposições completamente.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
<!-- Package A -->
+ apps
  + projectA
    + components
      + header
        + .content.xml

<!-- Package B -->
+ apps
  + projectA
    + components
      + header
        + .content.xml (OVERLAP!)
```

#### Estrutura Compliant:
```xml
<!-- Package A -->
+ apps
  + projectA
    + components
      + header
        + .content.xml

<!-- Package B -->
+ apps
  + projectB
    + components
      + footer
        + .content.xml
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

**Descrição**: Por razões de segurança, caminhos contendo `/config/` e `/install/` são legíveis apenas por usuários administrativos no AEM e devem ser usados apenas para configuração OSGi e bundles OSGi. Colocar outros tipos de conteúdo sob caminhos que contêm esses segmentos resulta em comportamento de aplicação que varia inadvertidamente entre usuários administrativos e não-administrativos.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    + config [nt:unstructured]
      + rtePlugins [nt:unstructured]
```

#### Estrutura Compliant:
```xml
+ cq:editConfig [cq:EditConfig]
  + cq:inplaceEditing [cq:InplaceEditConfig]
    ./configPath = inplaceEditingConfig (String)
    + inplaceEditingConfig [nt:unstructured]
      + rtePlugins [nt:unstructured]
```

### DuplicateOsgiConfigurations - Customer packages should not contain overlapping OSGi configurations

| Atributo | Valor |
|----------|-------|
| **Key** | DuplicateOsgiConfigurations |
| **Type** | Bug |
| **Severity** | Major |
| **Since** | Version 2019.6.0 |

**Descrição**: Um problema comum que ocorre em projetos complexos é onde o mesmo componente OSGi é configurado múltiplas vezes. Este problema cria uma ambiguidade sobre qual configuração é operável. Esta regra é "runmode-aware" no sentido de que identifica apenas problemas onde o mesmo componente é configurado múltiplas vezes no mesmo run mode ou combinação de run modes.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ apps
  + projectA
    + config
      + com.day.cq.commons.impl.ExternalizerImpl.xml
  + projectB
    + config
      + com.day.cq.commons.impl.ExternalizerImpl.xml
```

#### Estrutura Compliant:
```xml
+ apps
  + shared-config
    + config
      + com.day.cq.commons.impl.ExternalizerImpl.xml
```

---

## 🌳 Regras Oak Index

### OakIndexLocation - Custom Search Index Definition Nodes Must Be Direct Children of /oak:index

| Atributo | Valor |
|----------|-------|
| **Key** | OakIndexLocation |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) sejam nós filhos diretos de `/oak:index`. Índices em outras localizações devem ser movidos para serem compatíveis com AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myproject
    + customIndex [oak:QueryIndexDefinition]
      - type = "lucene"
      - compatVersion = 2
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myproject-customIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
```

### IndexCompatVersion - Custom Search Index Definition Nodes Must Have a compatVersion of 2

| Atributo | Valor |
|----------|-------|
| **Key** | IndexCompatVersion |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) tenham a propriedade `compatVersion` definida como `2`. AEM Cloud Service não suporta nenhum outro valor.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 1
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
```

### IndexDescendantNodeType - Descendent Nodes of Custom Search Index Definition Nodes Must Be Of Type nt:unstructured

| Atributo | Valor |
|----------|-------|
| **Key** | IndexDescendantNodeType |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: Problemas difíceis de solucionar podem ocorrer quando um nó de definição de índice de busca customizado tem nós filhos desordenados. Para evitar tais nós, a Adobe recomenda que todos os nós descendentes de um nó `oak:QueryIndexDefinition` sejam do tipo `nt:unstructured`.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    + indexRules [oak:Unstructured]
      + nt:base [nt:base]
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    + indexRules [nt:unstructured]
      + nt:base [nt:unstructured]
```

### IndexRulesNode - Custom Search Index Definition Nodes Must Contain a Child Node Named indexRules that Has Children

| Atributo | Valor |
|----------|-------|
| **Key** | IndexRulesNode |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: Um nó de definição de índice de busca customizado adequadamente definido deve incluir um nó filho chamado `indexRules` e este nó deve ter pelo menos um filho.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
    + indexRules [nt:unstructured]
      + nt:base [nt:unstructured]
        + properties [nt:unstructured]
          + title [nt:unstructured]
            - name = "jcr:title"
            - propertyIndex = true
```

### IndexName - Custom Search Index Definition Nodes Must Follow Naming Conventions

| Atributo | Valor |
|----------|-------|
| **Key** | IndexName |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) sejam nomeadas seguindo um padrão específico descrito na documentação de Content Search and Indexing.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myIndex [oak:QueryIndexDefinition]
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myproject-myIndex [oak:QueryIndexDefinition]
```

### IndexType - Custom Search Index Definition Nodes Must Use the Index Type lucene

| Atributo | Valor |
|----------|-------|
| **Key** | IndexType |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service requer que definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) tenham uma propriedade `type` com o valor definido como `lucene`. Indexação usando tipos de índice legados deve ser atualizada antes da migração para AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "property"
    - compatVersion = 2
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
```

### IndexSeedProperty - Custom Search Index Definition Nodes Must Not Contain a Property Named seed

| Atributo | Valor |
|----------|-------|
| **Key** | IndexSeedProperty |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) de conter uma propriedade chamada `seed`. Indexação usando esta propriedade deve ser atualizada antes da migração para AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
    - seed = 12345
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
```

### IndexReindexProperty - Custom Search Index Definition Nodes Must Not Contain a Property Named reindex

| Atributo | Valor |
|----------|-------|
| **Key** | IndexReindexProperty |
| **Type** | Code Smell |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) de conter uma propriedade chamada `reindex`. Indexação usando esta propriedade deve ser atualizada antes da migração para AEM Cloud Service.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
    - reindex = true
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
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

**Descrição**: A API do AEM contém interfaces e classes Java™ que são destinadas apenas a serem usadas, mas não implementadas, por código customizado. Por exemplo, apenas o AEM implementa a interface `com.day.cq.wcm.api.Page`. Adicionar novos métodos a essas interfaces não afeta o código existente, tornando a adição de novos métodos compatível com versões anteriores. No entanto, se o código customizado implementa uma dessas interfaces, esse código customizado introduziu um risco de compatibilidade com versões anteriores para o cliente.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```java
import com.day.cq.wcm.api.Page;

public class DontDoThis implements Page {
    // implementation here
}
```

#### Estrutura Compliant:
```java
import com.day.cq.wcm.api.Page;

@Component(service = MyPageService.class)
public class DoThis {
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    public void processPage(Page page) {
        // use Page interface, don't implement it
    }
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

**Descrição**: O uso do modo "replacement" no file vault não é permitido para caminhos abaixo de `/content`; não deve ser usado para caminhos abaixo de `/etc` e `/var`. O modo "replace" sobrescreve o conteúdo existente do repositório com conteúdo que vem do pacote. Pacotes que acionam esta ação não devem ser incluídos nos pacotes implantados através do Cloud Manager.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workspaceFilter version="1.0">
    <filter root="/content/mysite" mode="replace"/>
    <filter root="/etc/designs/mysite" mode="replace"/>
</workspaceFilter>
```

#### Estrutura Compliant:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workspaceFilter version="1.0">
    <filter root="/content/mysite" mode="merge"/>
    <filter root="/etc/designs/mysite" mode="merge"/>
</workspaceFilter>
```

---

## 🎨 Regras de Client Libraries

### ClientlibProxyResource - Resources Contained in Proxy-Enabled Client Libraries Should Be in a folder named resources

| Atributo | Valor |
|----------|-------|
| **Key** | ClientlibProxyResource |
| **Type** | Bug |
| **Severity** | Minor |
| **Since** | Version 2021.2.0 |

**Descrição**: Bibliotecas de cliente AEM podem conter recursos estáticos como imagens e fontes. Conforme descrito na documentação Using Client-Side Libraries, ao usar bibliotecas de cliente com proxy, esses recursos estáticos devem estar contidos em uma pasta filha chamada `resources` para serem efetivamente referenciados nas instâncias de publicação.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + images
        + myimage.jpg
```

#### Estrutura Compliant:
```xml
+ apps
  + projectA
    + clientlib
      - allowProxy=true
      + resources
        + myimage.jpg
```

---

## 🔧 Regras de Índices Avançadas

### IndexNotUnderUIContent - Index definition nodes must not be deployed in UI content package

| Atributo | Valor |
|----------|-------|
| **Key** | IndexNotUnderUIContent |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas (nós do tipo `oak:QueryIndexDefinition`) de serem implantadas no pacote UI Content.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
<!-- Em ui.content package -->
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
```

#### Estrutura Compliant:
```xml
<!-- Em ui.apps package -->
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
```

### CustomFulltextIndexesOfTheDamAssetCheck - Custom full-text index definition of type damAssetLucene must be correctly prefixed

| Atributo | Valor |
|----------|-------|
| **Key** | CustomFulltextIndexesOfTheDamAssetCheck |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de texto completo customizadas do tipo `damAssetLucene` de serem prefixadas com qualquer coisa diferente de `damAssetLucene`.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myproject-damAssetLucene [oak:QueryIndexDefinition]
    - type = "lucene"
```

#### Estrutura Compliant:
```xml
+ oak:index
  + damAssetLucene-myproject [oak:QueryIndexDefinition]
    - type = "lucene"
```

### DuplicateNameProperty - Index definition nodes must not contain properties with the same name

| Atributo | Valor |
|----------|-------|
| **Key** | DuplicateNameProperty |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe definições de índice de busca customizadas (ou seja, nós do tipo `oak:QueryIndexDefinition`) de conter propriedades com o mesmo nome.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - type = "property"
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    - type = "lucene"
```

### RestrictIndexCustomization - Customizing of certain out-of-the-box index definitions is prohibited

| Atributo | Valor |
|----------|-------|
| **Key** | RestrictIndexCustomization |
| **Type** | Improvement |
| **Severity** | Major |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe modificações não autorizadas dos seguintes índices OOTB: `nodetypeLucene`, `slingResourceResolver`, `socialLucene`, `appsLibsLucene`, `authorizables`, `pathReference`.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + nodetypeLucene [oak:QueryIndexDefinition]
    - customProperty = "modified"
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myproject-customIndex [oak:QueryIndexDefinition]
    - type = "lucene"
    - compatVersion = 2
```

---

## 🔍 Regras de Configuração de Índices

### AnalyzerTokenizerConfigCheck - Configuration of the tokenizers in analyzers should be created with the name tokenizer

| Atributo | Valor |
|----------|-------|
| **Key** | AnalyzerTokenizerConfigCheck |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2024.6.0 |

**Descrição**: AEM Cloud Service proíbe a criação de tokenizers com nomes incorretos em analyzers. Tokenizers devem sempre ser definidos como `tokenizer`.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    + analyzers [nt:unstructured]
      + default [nt:unstructured]
        + tokenizers [nt:unstructured]
          + myTokenizer [nt:unstructured]
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    + analyzers [nt:unstructured]
      + default [nt:unstructured]
        + tokenizer [nt:unstructured]
```

### PathSpacesCheck - Configuration of indexing definitions should not contain spaces

| Atributo | Valor |
|----------|-------|
| **Key** | PathSpacesCheck |
| **Type** | Improvement |
| **Severity** | Minor |
| **Since** | Version 2024.7.0 |

**Descrição**: AEM Cloud Service proíbe a criação de definições de indexação que contenham propriedades com espaços.

**Categoria**: Content & Package
**Fonte**: Adobe Experience League (documentação oficial)

#### Estrutura Non-compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    + indexRules [nt:unstructured]
      + nt:base [nt:unstructured]
        + properties [nt:unstructured]
          + "my property" [nt:unstructured]
```

#### Estrutura Compliant:
```xml
+ oak:index
  + myCustomIndex [oak:QueryIndexDefinition]
    + indexRules [nt:unstructured]
      + nt:base [nt:unstructured]
        + properties [nt:unstructured]
          + myProperty [nt:unstructured]
```

---

## 📚 Referências Content & Package

### Documentação Oficial
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Content Search and Indexing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/indexing)
- [Package Manager](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/sites/administering/contentmanagement/package-manager)
- [Using Client-Side Libraries](https://experienceleague.adobe.com/en/docs/experience-manager-65/content/implementing/developing/introduction/clientlibs)

### Ferramentas
- [OakPAL Documentation](https://github.com/adamcin/oakpal)
- [FileVault Documentation](https://jackrabbit.apache.org/filevault/)
- [Oak Documentation](https://jackrabbit.apache.org/oak/docs/query/lucene.html)

### Arquivos de Regras
- CodeQuality-rules-latest-AMS-2024-12-0.csv (versão mais recente)
- CodeQuality-rules-latest-AMS.csv (versão anterior)

---

*Última atualização: 14 de dezembro de 2025*
*Gerado via MCP AEM Documentation + análise de CSVs*
*Categoria: Content & Package Rules*