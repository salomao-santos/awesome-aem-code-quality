# Awesome AEM Code Quality - Regras SonarQube para AEMaaCS

Este repositório contém **127 regras oficiais de qualidade de código SonarQube** especificamente curadas para Adobe Experience Manager as a Cloud Service (AEMaaCS), obtidas de **documentação oficial da Adobe** e **repositórios públicos da Adobe**.

## 🎯 Por Que Usar Este Repositório?

**Para Desenvolvedores e Líderes Técnicos:** Identifique problemas de qualidade de código **antes que cheguem à pipeline** usando a integração Agent Hook do Kiro IDE. Esta abordagem proativa ajuda você a:

- ✅ **Prevenir falhas na pipeline** identificando violações durante o desenvolvimento
- ✅ **Economizar tempo de deploy** capturando problemas na IDE, não no Cloud Manager
- ✅ **Seguir boas práticas Adobe** com regras diretamente de fontes oficiais
- ✅ **Receber feedback instantâneo** com análise automática ao salvar arquivos
- ✅ **Aprender padrões AEM** através de exemplos de código compliant/non-compliant

**Fontes:** Adobe Experience League, documentação Cloud Manager e arquivos CSV oficiais de qualidade de código AEM.

## 🛠️ Desenvolvido com AEM Documentation MCP + Kiro IDE

Todo este repositório foi **organizado e desenvolvido** usando a poderosa combinação de:

- **🔍 AEM Documentation MCP Server**: Extração e análise automatizada da documentação oficial Adobe
- **🤖 Kiro IDE Agent Hooks**: Análise inteligente de código e geração de regras
- **📚 Fontes Oficiais Adobe**: Integração direta com Experience League e documentação Cloud Manager

**Processo de Desenvolvimento:**
1. **Integração MCP**: Conectado às fontes oficiais de documentação Adobe
2. **Extração Automatizada**: Usou ferramentas MCP para buscar e analisar regras SonarQube
3. **Organização Inteligente**: Kiro IDE organizou regras por severidade, tipo e compatibilidade AEM
4. **Exemplos de Código**: Gerou exemplos compliant/non-compliant usando documentação oficial
5. **Integração Hook**: Criou workflows de validação automatizada para experiência seamless do desenvolvedor

Esta abordagem garante **100% de precisão** com padrões oficiais Adobe e **atualizações automáticas** quando novas regras são publicadas.

## 🚀 Configuração em 3 Passos

### Passo 1: Configurar MCP AEM Documentation

```bash
run #prompt-configure-aem-documentation-mcp.md
```

**O que faz:**
- ✅ Instala e configura MCP AEM Documentation Server
- ✅ Verifica pré-requisitos (Docker)
- ✅ Disponibiliza ferramentas de busca na documentação AEM
- ✅ Configura acesso às 127 regras SonarQube oficiais

### Passo 2: Usar MCP para Gerar Regras

```bash
run #prompt-use-aem-documentation-mcp.md
```

**O que faz:**
- ✅ Busca documentação oficial das regras SonarQube AEM
- ✅ Gera arquivo com regras atualizadas (SonarQube 9.9)
- ✅ Inclui exemplos de código compliant/non-compliant
- ✅ Atualiza steering rules para validação automática

### Passo 3: Configurar Agent Hook

```bash
run #prompt-configure-agent-hook-aemcs-analyser-sonarqube.md
```

**O que faz:**
- ✅ Configura hook automático para validação de código
- ✅ Ativa análise ao salvar arquivos Java
- ✅ Integra com as regras SonarQube AEM
- ✅ Fornece sugestões de correção automáticas

## 🧪 Teste a Configuração

Após executar os 3 passos, teste a configuração:

1. Abra o arquivo: `identify-code-smells-using-rules-sonarqube-and-agent-hooks/example-test.java`
2. Salve o arquivo (Ctrl+S)
3. O Agent Hook irá automaticamente identificar as **20 violações SonarQube** no código

## 📊 Regras SonarQube Incluídas

| Tipo | Quantidade | Exemplos |
|------|------------|----------|
| **Vulnerabilities** | 14 | Senhas hardcoded, algoritmos fracos |
| **Security Hotspots** | 6 | Cookies inseguros, SQL injection |
| **Bugs** | 32 | Recursos não fechados, NPE |
| **Code Smells** | 75 | System.out, caminhos hardcoded |
| **Total** | **127 regras** | Compatível com AEMaaCS |

## 🔗 Recursos Adicionais

- **Documentação Completa**: Ver `aemcs-sonarqube-rules.md` para lista detalhada
- **Arquivo de Teste**: `example-test.java` com 20 violações intencionais
- **Repositório Principal**: [awesome-aem-code-quality](https://github.com/salomao-santos/awesome-aem-code-quality)

---

*Testado com SonarQube 9.9 e Cloud Manager 2025.2.0*