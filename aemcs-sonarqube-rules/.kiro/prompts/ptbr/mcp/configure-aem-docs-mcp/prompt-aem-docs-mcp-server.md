# AEM Documentation MCP Server

## O que é

Servidor MCP (Model Context Protocol) que fornece acesso integrado à documentação Adobe Experience Manager (AEM) e repositórios relacionados. Permite buscar e ler documentação em formato markdown.

**Compatível com**: Kiro • Amazon Q • Cursor • GitHub Copilot • Claude Desktop • VS Code • JetBrains IDEs

---

## 🚀 INÍCIO RÁPIDO - Detecção Automática

### Passo 1: Detectar sua IDE

Execute este comando no terminal para identificar sua ferramenta **pelo processo em execução**:

```bash
# Linux/macOS - Detectar pelo processo (prioridade: Code > Cursor > Claude > Kiro > JetBrains)
if pgrep -f "/usr/share/code/code" > /dev/null 2>&1; then 
  echo "IDE: VS Code / GitHub Copilot / Amazon Q"
elif pgrep -f "cursor" > /dev/null 2>&1; then 
  echo "IDE: Cursor"
elif pgrep -f "claude" > /dev/null 2>&1; then 
  echo "IDE: Claude Desktop"
elif pgrep -f "/usr/share/kiro/kiro" > /dev/null 2>&1; then 
  echo "IDE: Kiro"
elif pgrep -f -E "idea|pycharm|webstorm|goland" > /dev/null 2>&1; then 
  echo "IDE: JetBrains"
else 
  echo "IDE: Não identificada - abra uma IDE com suporte MCP"
fi
```

```batch
REM Windows - Detectar pelo processo
tasklist /fo table | findstr /i "code.exe" >nul && (echo IDE: VS Code / GitHub Copilot / Amazon Q) || tasklist /fo table | findstr /i "cursor.exe" >nul && (echo IDE: Cursor) || tasklist /fo table | findstr /i "claude.exe" >nul && (echo IDE: Claude Desktop) || tasklist /fo table | findstr /i "kiro.exe" >nul && (echo IDE: Kiro) || tasklist /fo table | findstr /i "idea.exe" >nul && (echo IDE: JetBrains) || (echo IDE: Não identificada)
```

**Dica**: Deixe sua IDE aberta antes de executar este comando!

### Passo 2: Verificar Configuração Existente

Localize o arquivo de config conforme sua IDE e procure por `"mcpServers"`:

**Kiro (WORKSPACE - Recomendado)**
```bash
cat .kiro/settings/mcp.json 2>/dev/null | grep -A 5 "aem-documentation" || echo "MCP não configurado no workspace"
```


**Amazon Q (WORKSPACE - Recomendado)**
```bash
cat .amazonq/mcp.json 2>/dev/null | grep -A 5 "aem-documentation" || echo "MCP não configurado no workspace"
```

**Amazon Q (Global - Alternativa)**
```bash
cat ~/.amazonq/mcp.json 2>/dev/null | grep -A 5 "aem-documentation" || echo "MCP não configurado globalmente"
```

**Cursor / VS Code (WORKSPACE)**
```bash
cat .vscode/settings.json 2>/dev/null | grep -A 5 "mcpServers" || echo "MCP não configurado no workspace"
```

**Claude Desktop (macOS/Linux)**
```bash
cat ~/.claude/claude_desktop_config.json 2>/dev/null | grep -A 5 "mcpServers" || echo "MCP não configurado"
```

**Claude Desktop (Windows)**
```batch
type "%APPDATA%\Claude\claude_desktop_config.json" | findstr "mcpServers" || echo MCP não configurado
```

---

## ⚠️ PRÓXIMO PASSO: REINICIAR IDE

**Após configurar o MCP no arquivo settings.json/config, você DEVE reiniciar sua IDE completamente para ativar a configuração.**

### Como Reiniciar por IDE:

**VS Code / GitHub Copilot / Amazon Q**
- Feche completamente: `Ctrl+Shift+Q` ou `File > Exit`
- Reabra a aplicação

Ou execute no terminal:
```bash
pkill -f "/usr/share/code/code" && sleep 2 && code &
```

**Cursor**
- Feche: `Ctrl+Shift+Q` ou `File > Exit`
- Reabra a aplicação

**Kiro**
- Feche completamente
- Reabra a aplicação

**Claude Desktop**
- Feche completamente
- Reabra a aplicação

**⏳ Aguarde 3-5 segundos após abrir para a IDE carregar a configuração MCP**

---

## Pré-requisitos

### Obrigatório
- Docker Desktop instalado e rodando
- Validação: `docker --version`

### Opcional (Alternativa ao Docker)
- Python 3.10+
- uv instalado
- Validação: `python3 --version` e `uv --version`

---

## ⚙️ Construir Imagem Docker

Se usar Docker pela primeira vez:

```bash
cd aem_documentation_mcp_server
docker build -t aem-docs-mcp-server:latest .
```

Se a imagem já existe, pule este passo.

---

## 📝 Configuração por IDE

### 1️⃣ KIRO

**🎯 RECOMENDADO: Configuração por Workspace (Isolada por Projeto)**

**Arquivo**: `.kiro/settings/mcp.json` (na raiz do seu projeto)

**Vantagens:**
- Configuração específica para cada projeto
- Não afeta outros projetos
- Versionável no Git (se desejar)
- Sobrescreve configuração global

**Se não tem arquivo**, crie com:

```json
{
  "mcpServers": {
    "aem-documentation-mcp-server": {
      "command": "docker",
      "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
      "env": {
        "FASTMCP_LOG_LEVEL": "ERROR"
      },
      "disabled": false,
      "autoApprove": [
        "search_experience_league",
        "read_documentation",
        "get_available_services"
      ]
    }
  }
}
```

**Se já tem arquivo**, adicione apenas este bloco dentro de `"mcpServers"`:

```json
"aem-documentation-mcp-server": {
  "command": "docker",
  "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
  "env": {
    "FASTMCP_LOG_LEVEL": "ERROR"
  },
  "disabled": false,
  "autoApprove": [
    "search_experience_league",
    "read_documentation",
    "get_available_services"
  ]
}
```

**Reiniciar**: Feche e reabra o Kiro completamente

---

**Alternativa: Configuração Global (Todos os Projetos)**

**Arquivo**: `~/.kiro/settings/mcp.json`

Use a mesma estrutura JSON acima. Esta configuração será aplicada a todos os projetos que não tenham configuração local.

---

### 2️⃣ AMAZON Q (AWS Toolkit for VS Code)

**🎯 RECOMENDADO: Configuração por Workspace**

**Arquivo**: `.amazonq/mcp.json` (na raiz do seu projeto)

**Como criar:**
- Crie a pasta `.amazonq` na raiz do projeto (se não existir)
- Crie o arquivo `mcp.json` dentro dela

⚠️ **Amazon Q usa estrutura própria de configuração MCP:**

**Conteúdo do arquivo:**

```json
{
  "mcpServers": {
    "aem-documentation-mcp-server": {
      "command": "docker",
      "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
      "env": {
        "FASTMCP_LOG_LEVEL": "ERROR"
      },
      "disabled": false
    }
  }
}
```

**Se já tem arquivo**, adicione apenas este bloco dentro de `"mcpServers"`:

```json
"aem-documentation-mcp-server": {
  "command": "docker",
  "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
  "env": {
    "FASTMCP_LOG_LEVEL": "ERROR"
  },
  "disabled": false
}
```

**Reiniciar**: Ctrl+Shift+P → "Developer: Reload Window"

---

**Alternativa: Configuração Global**

**Arquivo**: `~/.amazonq/mcp.json` (Linux/macOS) ou `%USERPROFILE%\.amazonq\mcp.json` (Windows)

Use a mesma estrutura JSON acima.

---

### 3️⃣ VS CODE / GITHUB COPILOT

**Arquivo**: `~/.vscode/settings.json` (Linux/macOS) ou `%APPDATA%\Code\User\settings.json` (Windows)

### 3️⃣ VS CODE / GITHUB COPILOT

**🎯 RECOMENDADO: Configuração por Workspace**

**Arquivo**: `.vscode/settings.json` (na raiz do seu projeto)

**Como criar:**
- Crie a pasta `.vscode` na raiz do projeto (se não existir)
- Crie o arquivo `settings.json` dentro dela

**Se não tem `"modelContextProtocol"`, adicione:**

```json
{
  "modelContextProtocol": {
    "mcpServers": {
      "aem-documentation-mcp-server": {
        "command": "docker",
        "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
        "env": {
          "FASTMCP_LOG_LEVEL": "ERROR"
        },
        "disabled": false,
        "autoApprove": [
          "search_experience_league",
          "read_documentation",
          "get_available_services"
        ]
      }
    }
  }
}
```

**Se já tem `"modelContextProtocol"`, adicione apenas:**

```json
"aem-documentation-mcp-server": {
  "command": "docker",
  "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
  "env": {
    "FASTMCP_LOG_LEVEL": "ERROR"
  },
  "disabled": false,
  "autoApprove": [
    "search_experience_league",
    "read_documentation",
    "get_available_services"
  ]
}
```

ao objeto `"mcpServers"` dentro de `"modelContextProtocol"`.

**Reiniciar**: Ctrl+Shift+P → "Developer: Reload Window"

---

**Alternativa: Configuração Global**

**Arquivo**: `~/.vscode/settings.json` (Linux/macOS) ou `%APPDATA%\Code\User\settings.json` (Windows)

**Como abrir:**
- Ctrl+Shift+P → "Preferences: Open Settings (JSON)" ou
- `Code > Preferences > Settings > JSON`

Use a mesma estrutura JSON acima.

---

### 4️⃣ CURSOR

**🎯 RECOMENDADO: Configuração por Workspace**

**Arquivo**: `.vscode/settings.json` (na raiz do seu projeto)

**Como criar:**
- Crie a pasta `.vscode` na raiz do projeto (se não existir)
- Crie o arquivo `settings.json` dentro dela

Idêntico ao VS Code. Use a mesma configuração da seção anterior.

**Reiniciar**: Ctrl+Shift+P → "Developer: Reload Window"

---

**Alternativa: Configuração Global**

**Arquivo**: `~/.cursor/settings.json` (Linux/macOS) ou `%APPDATA%\Cursor\User\settings.json` (Windows)

**Como abrir:**
- Ctrl+Shift+P → "Preferences: Open Settings (JSON)" ou
- `File > Preferences > Settings > JSON`

Use a mesma estrutura JSON da seção VS Code.

**Reiniciar**: Ctrl+Shift+P → "Developer: Reload Window"

---

### 5️⃣ CLAUDE DESKTOP

**Arquivo**:
- macOS/Linux: `~/.claude/claude_desktop_config.json`
- Windows: `%APPDATA%\Claude\claude_desktop_config.json`

**Se não tem arquivo**, crie com:

```json
{
  "mcpServers": {
    "aem-documentation-mcp-server": {
      "command": "docker",
      "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
      "env": {
        "FASTMCP_LOG_LEVEL": "ERROR"
      }
    }
  }
}
```

**Se já tem arquivo**, adicione apenas este bloco dentro de `"mcpServers"`:

```json
"aem-documentation-mcp-server": {
  "command": "docker",
  "args": ["run", "--rm", "-i", "aem-docs-mcp-server:latest"],
  "env": {
    "FASTMCP_LOG_LEVEL": "ERROR"
  }
}
```

**Reiniciar**: Feche e reabra Claude Desktop completamente

---

### 6️⃣ JETBRAINS IDES

**Status**: Suporte via plugin em desenvolvimento

**Alternativa Atual**: Use VS Code + Remote SSH ou terminal integrado:

```bash
# No terminal do JetBrains, execute diretamente:
docker run --rm -i aem-docs-mcp-server:latest
```

---

## ✅ Validar Configuração

### 1. Verificar Arquivo JSON

Valide em: https://jsonlint.com/

Certifique-se que o arquivo está bem formado (sem erros de sintaxe).

### 2. Verificar Status do MCP

Na sua IDE, procure em settings/config por `aem-documentation-mcp-server`.

Confirme que está com `"disabled": false`.

### 3. Testar as Ferramentas

Na sua IDE, converse com o assistant/copilot:

**Teste 1 - Listar recursos:**
```
Use a ferramenta "get_available_services" para listar recursos AEM disponíveis
```

**Teste 2 - Pesquisar documentação:**
```
Use a ferramenta "search_experience_league" para pesquisar "AEM as a Cloud Service"
```

Se funcionar, você verá resultados da documentação Adobe.

---

## 📊 Variáveis de Ambiente (Opcionais)

| Variável | Valores | Padrão | Descrição |
|----------|---------|--------|-----------|
| `FASTMCP_LOG_LEVEL` | DEBUG, INFO, WARNING, ERROR, CRITICAL | WARNING | Nível de log |
| `MCP_USER_AGENT` | Qualquer string | Padrão | User-Agent personalizado (redes corporativas) |

Adicione em `"env"` na configuração JSON se precisar customizar.

---

## 🔧 Troubleshooting

| Problema | Solução |
|----------|---------|
| **"MCP not recognized"** | 1) Valide JSON em jsonlint.com 2) Restart IDE completamente 3) Confirme `"disabled": false` |
| **Docker command not found** | Instale Docker Desktop e reinicie o terminal |
| **"Connection refused"** | Execute `docker ps` - Docker não está rodando |
| **Ferramentas retornam erro** | Execute no terminal: `docker run -i aem-docs-mcp-server:latest` |
| **JSON inválido no arquivo config** | Copie configuração exatamente de um editor JSON online |
| **Arquivo config não encontrado** | Crie-o no caminho correto conforme sua IDE |

---

## 📚 Fontes Suportadas

- Adobe Experience League, Developer, HelpX, Docs
- Repositórios GitHub (Adobe, ACS, Netcentric, etc.)
- GitHub Pages (*.github.io)
- Apache Sling
- adaptTo() Conference (2011-2025+)
- YouTube (com transcrição)
- Adobe Summit

---

## Referências

- **Repositório**: [adobe-experience-manager-mcps](https://github.com/salomao-santos/adobe-experience-manager-mcps)
- **Autor**: Salomão Santos
- **Status**: Docker/uv (disponível) | PyPI (futuro)
