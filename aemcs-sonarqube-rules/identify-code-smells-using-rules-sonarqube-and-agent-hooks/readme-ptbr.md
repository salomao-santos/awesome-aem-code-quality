# Exemplo de Code Smell para SonarQube

Esta pasta contém arquivos Java de exemplo com code smells e violações do SonarQube intencionais para fins de teste e demonstração.

## 🤖 Agent Hook do Kiro IDE

O Kiro IDE tem um **agent hook** configurado que analisa automaticamente o código quando você salva arquivos nesta pasta.

### Como funciona:

1. **Salve o arquivo** - Quando você salva um arquivo Java com code smells
2. **Análise automática** - O agent hook é acionado automaticamente
3. **Identificação de vulnerabilidades** - Code smells e violações são detectados
4. **Recomendações** - Você recebe sugestões específicas de correção
5. **Próximos passos** - Orientação prática sobre como corrigir os problemas

### O que é detectado:

- Vazamento de recursos (ResourceResolver não fechado, conexões, etc.)
- Timeouts ausentes em requisições HTTP
- Tratamento inadequado de exceções (printStackTrace, uso de System.out)
- Caminhos e valores hardcoded
- Chamadas de métodos obsoletos ou perigosos
- Vulnerabilidades de segurança
- Problemas de performance
- Violações de boas práticas

### Exemplos de violações nesta pasta:

O arquivo `example-test.java` contém múltiplas violações intencionais incluindo:
- ResourceResolver não fechado (CQBP-72)
- Requisições HTTP sem timeout (ConnectionTimeoutMechanism)
- Uso de System.out.println (CQBP-44)
- Exception.printStackTrace() (CQBP-44)
- Caminhos hardcoded (CQBP-71)
- Uso de Thread.stop() obsoleto (CWE-676)

Simplesmente edite e salve o arquivo para ver o agent hook em ação!
