# Awesome AEM Code Quality - Regras SonarQube para AEMaaCS

Este repositório contém **127 regras oficiais de qualidade de código SonarQube** especificamente selecionadas para Adobe Experience Manager as a Cloud Service (AEMaaCS), extraídas da **documentação oficial da Adobe** e **repositórios públicos da Adobe**.

## 🎯 Por Que Usar Este Repositório?

**Para Desenvolvedores e Tech Leads:** Identifique problemas de qualidade de código **antes que cheguem ao pipeline** usando a integração Agent Hook do Kiro IDE. Esta abordagem proativa ajuda você a:

- ✅ **Prevenir falhas no pipeline** identificando violações durante o desenvolvimento
- ✅ **Economizar tempo de deploy** capturando problemas na sua IDE, não no Cloud Manager
- ✅ **Seguir as melhores práticas da Adobe** com regras diretamente das fontes oficiais
- ✅ **Obter feedback instantâneo** com análise automática de código ao salvar arquivos
- ✅ **Aprender padrões AEM** através de exemplos de código conformes/não-conformes

**Fontes:** Adobe Experience League, documentação do Cloud Manager e arquivos CSV oficiais de qualidade de código AEM.

## 🛠️ Construído com AEM Documentation MCP + Kiro IDE

Este repositório inteiro foi **organizado e desenvolvido** usando a poderosa combinação de:

- **🔍 AEM Documentation MCP Server**: Extração e análise automatizada da documentação oficial da Adobe
- **🤖 Kiro IDE Agent Hooks**: Análise inteligente de código e geração de regras
- **📚 Fontes Oficiais da Adobe**: Integração direta com Experience League e documentação do Cloud Manager

**Processo de Desenvolvimento:**
1. **Integração MCP**: Conectado às fontes oficiais de documentação da Adobe
2. **Extração Automatizada**: Usou ferramentas MCP para buscar e analisar regras SonarQube
3. **Organização Inteligente**: Kiro IDE organizou regras por severidade, tipo e compatibilidade AEM
4. **Exemplos de Código**: Gerou exemplos conformes/não-conformes usando documentação oficial
5. **Integração de Hooks**: Criou fluxos de validação automatizados para experiência perfeita do desenvolvedor

Esta abordagem garante **100% de precisão** com os padrões oficiais da Adobe e **atualizações automáticas** quando novas regras são publicadas.

## 📸 Guia Visual de Configuração

As imagens abaixo mostram o processo real de configuração no Kiro IDE:

| Passo | Descrição | Guia Visual |
|-------|-----------|-------------|
| **1** | Executar prompt no Kiro IDE | ![Execução do Prompt](assets/images/config-mcp-aem-doc/01-configuration-mcp-aem-documentation-using-prompt-in-kiro-ide.png) |
| **2** | MCP Server configurado com sucesso | ![Resultado da Configuração](assets/images/config-mcp-aem-doc/02-configuration-mcp-aem-documentation.png) |

## 🚀 Configuração em 3 Passos

### Passo 1: Configurar MCP AEM Documentation

![Configuração usando Prompt do Kiro IDE](assets/images/config-mcp-aem-doc/01-configuration-mcp-aem-documentation-using-prompt-in-kiro-ide.png)

```bash
run /ptbr/mcp/configure-aem-documentation-mcp/prompt-configure-aem-documentation-mcp-ptbr.md
```

**O que faz:**
- ✅ Instala e configura o MCP AEM Documentation Server
- ✅ Verifica pré-requisitos (Docker)
- ✅ Fornece ferramentas de busca na documentação AEM
- ✅ Configura acesso às 127 regras oficiais SonarQube

![Resultado da Configuração MCP](assets/images/config-mcp-aem-doc/02-configuration-mcp-aem-documentation.png)

### Passo 2: Usar MCP para Gerar Regras

```bash
run /ptbr/configure-agent-hook/prompt-configure-agent-hook-aemcs-analyser-sonarqube-ptbr.md
```

**O que faz:**
- ✅ Busca documentação oficial das regras SonarQube AEM
- ✅ Gera arquivo com regras atualizadas (SonarQube 9.9)
- ✅ Inclui exemplos de código conformes/não-conformes
- ✅ Atualiza regras de steering para validação automática

### Passo 3: Configurar Agent Hook

```bash
run /ptbr/configure-agent-hook/prompt-configure-agent-hook-aemcs-analyser-sonarqube-ptbr.md
```

**O que faz:**
- ✅ Configura hook automático para validação de código
- ✅ Ativa análise ao salvar arquivos Java
- ✅ Integra com regras SonarQube AEM
- ✅ Fornece sugestões de correção automática

## 🧪 Testar a Configuração

Após executar os 3 passos, teste a configuração:

1. Abra o arquivo: `identify-code-smells-using-rules-sonarqube-and-agent-hooks/example-test.java`
2. Salve o arquivo (Ctrl+S)
3. O Agent Hook identificará automaticamente as **20 violações SonarQube** no código

## 📊 Regras SonarQube Incluídas

| Tipo | Quantidade | Exemplos |
|------|------------|----------|
| **Vulnerabilidades** | 14 | Senhas hardcoded, algoritmos fracos |
| **Security Hotspots** | 6 | Cookies inseguros, SQL injection |
| **Bugs** | 32 | Recursos não fechados, NPE |
| **Code Smells** | 75 | System.out, caminhos hardcoded |
| **Total** | **127 regras** | Compatível com AEMaaCS |

## 🔗 Recursos Adicionais

- **Documentação Completa**: Veja `aemcs-sonarqube-rules.md` para lista detalhada
- **Arquivo de Teste**: `example-test.java` com 20 violações intencionais
- **Repositório Principal**: 