# Exemplos de Code Smells para SonarQube - AEM Cloud Service

Esta pasta contém arquivos Java de exemplo com code smells e violações do SonarQube intencionais para fins de teste, demonstração e aprendizado das regras de qualidade de código do AEM Cloud Service.

## 🤖 Agent Hook do Kiro IDE

O Kiro IDE possui um **agent hook** configurado que analisa automaticamente o código quando você salva arquivos nesta pasta, aplicando as regras de qualidade específicas do AEM Cloud Service.

### Como funciona:

1. **Salve o arquivo** - Quando você salva um arquivo Java contendo code smells
2. **Análise automática** - O agent hook é acionado automaticamente
3. **Identificação de violações** - Code smells e violações das regras SonarQube são detectados
4. **Recomendações específicas** - Você recebe sugestões detalhadas de correção baseadas nas melhores práticas do AEM
5. **Orientação prática** - Instruções claras sobre como corrigir os problemas identificados

### O que é detectado:

#### 🔴 Violações Críticas
- Vazamento de recursos (ResourceResolver não fechado, Session não deslogada)
- Timeouts ausentes em requisições HTTP
- Objetos não thread-safe usados como campos de Servlet/Filter
- Senhas hardcoded e vulnerabilidades de segurança

#### 🟠 Problemas Importantes
- Tratamento inadequado de exceções (printStackTrace, uso de System.out)
- Caminhos e valores hardcoded
- Chamadas de métodos obsoletos ou perigosos do AEM
- Uso de APIs deprecadas do AEM

#### 🟡 Melhorias de Qualidade
- Problemas de performance
- Violações de boas práticas de codificação
- Padrões de logging inadequados
- Estruturas de código não otimizadas

### Exemplos de violações nesta pasta:

O arquivo `example-test.java` contém múltiplas violações intencionais das regras do AEM Cloud Service, incluindo:

- **CQBP-72**: ResourceResolver não fechado adequadamente
- **ConnectionTimeoutMechanism**: Requisições HTTP sem configuração de timeout
- **CQBP-44**: Uso inadequado de System.out.println e Exception.printStackTrace()
- **CQBP-71**: Caminhos hardcoded em strings literais
- **CWE-676**: Uso do método Thread.stop() considerado perigoso
- **AEM Rules:AEM-6**: ResourceResolver não fechado em bloco finally
- **AEM Rules:AEM-7**: Session não deslogada em bloco finally

### Como testar:

1. Abra o arquivo `example-test.java`
2. Faça uma pequena alteração (adicione um espaço ou comentário)
3. Salve o arquivo (`Ctrl+S` ou `Cmd+S`)
4. Observe a análise automática sendo executada
5. Revise as recomendações de correção fornecidas pelo agent hook

### Arquivo de correção:

O arquivo `example-correct.java` demonstra como corrigir adequadamente as violações encontradas, seguindo as melhores práticas do AEM Cloud Service.

---

**💡 Dica**: Este sistema de análise automática ajuda a identificar e corrigir problemas de qualidade de código antes mesmo do deploy, garantindo conformidade com os padrões do AEM Cloud Service.
