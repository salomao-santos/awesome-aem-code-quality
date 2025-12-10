# Prompt: Configurar Agent Hook - Validador SonarQube AEM Cloud Service

## Objetivo

Configure um Agent Hook no Kiro IDE para validar automaticamente as regras de qualidade de código SonarQube do AEM Cloud Service sempre que um arquivo for editado.

## Instruções para o Agente

Crie um Agent Hook com as seguintes configurações baseadas no arquivo `.kiro/hooks/aemcs-sonarqube-validator.kiro.hook` gerado:

### 1. Configuração do Hook (Estrutura JSON)

```json
{
  "enabled": true,
  "name": "AEM Cloud Service - SonarQube Code Quality Validator",
  "description": "Valida regras de qualidade de código SonarQube para projetos AEM Cloud Service quando arquivos são editados, analisando violações por severidade e fornecendo sugestões específicas de correção",
  "version": "1",
  "when": {
    "type": "fileEdited",
    "patterns": [
      "**/*.java",
      "**/*.xml", 
      "**/*.html",
      "**/*.js",
      "**/*.ts",
      "**/*.jsx",
      "**/*.tsx",
      "**/*.css",
      "**/*.scss",
      "**/*.less",
      "**/*.json",
      "**/*.cfg",
      "**/*.config",
      "**/*.conf",
      "**/*.any",
      "**/*.rules",
      "**/*.vhost",
      "**/*.txt",
      "**/*.properties",
      "**/*.jsp",
      "**/*.jspx",
      "**/*.tag",
      "**/*.tagx",
      "**/*.md",
      "**/*.yml",
      "**/*.yaml",
      "**/*.MF",
      "**/.gitignore",
      "**/.babelrc",
      "**/.eslintrc"
    ]
  },
  "then": {
    "type": "askAgent",
    "prompt": "Analise o arquivo modificado aplicando as regras de qualidade de código SonarQube para AEM Cloud Service definidas em #[[file:.kiro/steering/aemcs-sonarqube-rules.md]].\n\n## Instruções de Análise\n\n1. **Identifique o tipo de arquivo** e aplique apenas as regras relevantes:\n   - `.java` → Regras Java (java:S*, AEM Rules:*, CQRules:*, findbugs:*)\n   - `.xml` → Regras de pacotes, Oak Index, componentes AEM\n   - `.any`, `.rules`, `.vhost`, `.conf` → Regras Dispatcher (DOTRules:*)\n   - `.js`, `.ts`, `.jsx`, `.tsx` → Boas práticas JavaScript/TypeScript\n   - `.html`, `.jsp`, `.jspx` → Regras de templates e componentes\n\n2. **Priorize por severidade**:\n   - 🔴 **Blocker**: DEVE ser corrigido imediatamente\n   - 🔴 **Critical**: DEVE ser corrigido antes do deploy\n   - 🟠 **Major**: DEVERIA ser corrigido\n   - 🟡 **Minor**: Considere corrigir\n   - ⚪ **Info**: Informativo\n\n3. **Para cada violação encontrada, reporte**:\n   - Linha do código\n   - Regra violada (Key)\n   - Descrição do problema\n   - Sugestão de correção\n\n4. **Regras prioritárias para Java**:\n   - `java:S2095` - Resources should be closed\n   - `AEM Rules:AEM-6` - ResourceResolver should be closed in finally block\n   - `AEM Rules:AEM-7` - Session should be logged out in finally block\n   - `AEM Rules:AEM-3` - Non-thread safe object used as field of Servlet/Filter\n   - `CQRules:ConnectionTimeoutMechanism` - HTTP requests should have timeouts\n   - `java:S2068` - Hard-coded passwords are security-sensitive\n\n5. **Regras prioritárias para Dispatcher**:\n   - `DOTRules:Disp-2` - statfileslevel should be >= 2\n   - `DOTRules:Disp-4` - Default deny rules from archetype\n   - `DOTRules:Disp-5` - serveStaleOnError should be enabled\n\n6. **Regras de compatibilidade Cloud Service**:\n   - `BannedPath` - Customer packages should not install content under /libs\n   - `CloudServiceIncompatibleWorkflowProcess` - Usage of incompatible workflow processes\n   - `CQRules:AMSCORE-553` - AEM Deprecated APIs Should Not Be Used\n\n## Formato de Saída\n\nSe encontrar violações:\n```\n## 🔍 Análise de Qualidade de Código - AEM Cloud Service\n**Arquivo**: `{nome_do_arquivo}`\n**Violações encontradas**: {número}\n\n### Violações\n#### 🔴 Blocker/Critical\n- **Linha {X}**: `{código}`\n- **Regra**: {key}\n- **Problema**: {descrição}\n- **Correção**: {sugestão}\n\n#### 🟠 Major\n...\n\n#### 🟡 Minor\n...\n\n### Resumo\n- Blocker: X\n- Critical: X\n- Major: X\n- Minor: X\n```\n\nSe não encontrar violações:\n```\n## ✅ Análise de Qualidade de Código - AEM Cloud Service\n**Arquivo**: `{nome_do_arquivo}`\nNenhuma violação das regras SonarQube foi encontrada. O código está em conformidade com as diretrizes de qualidade do AEM Cloud Service.\n```"
  }
}
```

### 2. Passos para Implementação

#### Opção A: Via Command Palette
1. Abra o Command Palette (`Ctrl+Shift+P` ou `Cmd+Shift+P`)
2. Digite: `Open Kiro Hook UI`
3. Clique em "Create New Hook"
4. Configure usando os valores da estrutura JSON acima

#### Opção B: Via Arquivo JSON (Recomendado)
1. Crie o arquivo `.kiro/hooks/aemcs-sonarqube-validator.kiro.hook`
2. Cole a estrutura JSON completa acima
3. Salve o arquivo
4. O hook será automaticamente carregado pelo Kiro IDE

### 3. Validação da Configuração

Após criar o hook, verifique:

1. **Arquivo criado**: `.kiro/hooks/aemcs-sonarqube-validator.kiro.hook` existe
2. **Steering file**: `.kiro/steering/aemcs-sonarqube-rules.md` está disponível
3. **Trigger configurado**: `fileEdited` para os padrões de arquivo especificados
4. **Prompt referencia**: O steering file usando `#[[file:.kiro/steering/aemcs-sonarqube-rules.md]]`

### 4. Teste do Hook

Para testar se o hook está funcionando:

1. Edite um arquivo `.java` no projeto
2. Salve o arquivo
3. O hook deve ser acionado automaticamente
4. Verifique se a análise SonarQube é executada
5. Confirme se as violações são reportadas no formato especificado

### 5. Estrutura Final Esperada

```
.kiro/
├── hooks/
│   └── aemcs-sonarqube-validator.kiro.hook
└── steering/
    └── aemcs-sonarqube-rules.md
```

---

## Referências

- Steering file: `.kiro/steering/aemcs-sonarqube-rules.md`
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Code Quality Testing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/code-quality-testing)

---

*Criado em: 10 de Dezembro de 2025*
