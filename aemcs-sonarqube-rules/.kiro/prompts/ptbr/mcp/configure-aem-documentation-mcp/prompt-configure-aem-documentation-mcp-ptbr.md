# Configuração do MCP AEM Documentation

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter:

- **Docker Desktop** instalado e rodando
- Validação: execute `docker --version` no terminal

---

## 🚀 Passo a Passo

### 1️⃣ Verificar se a Imagem Docker Existe

Verifique se a imagem já está disponível:

```bash
docker images | grep aem-docs-mcp-server
```

**Se a imagem já existe**, pule para o passo 3️⃣.

**Se a imagem não existe**, continue para o passo 2️⃣.

### 2️⃣ Clonar e Construir (Apenas se Necessário)

#### Clonar o Repositório

Se você ainda não tem o código-fonte:

```bash
git clone https://github.com/salomao-santos/adobe-experience-manager-mcps.git
cd adobe-experience-manager-mcps/aem_documentation_mcp_server
```

**Nota**: Se você já clonou o repositório anteriormente, apenas navegue até a pasta:

```bash
cd adobe-experience-manager-mcps/aem_documentation_mcp_server
```

#### Construir a Imagem Docker

```bash
docker build -t aem-docs-mcp-server:latest .
```

### 3️⃣ Configurar o MCP no Kiro

Escolha uma das opções abaixo:

#### 🎯 Opção A: Configuração por Workspace (Recomendado)

**Vantagens:**
- Configuração específica para cada projeto
- Não afeta outros projetos
- Versionável no Git
- Sobrescreve configuração global

**Arquivo**: `.kiro/settings/mcp.json` (na raiz do seu projeto)

**Se o arquivo não existe**, crie com o conteúdo completo:

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

**Se o arquivo já existe**, adicione apenas este bloco dentro de `"mcpServers"`:

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

#### 🌐 Opção B: Configuração Global (Todos os Projetos)

**Arquivo**: `~/.kiro/settings/mcp.json`

Use a mesma estrutura JSON da Opção A. Esta configuração será aplicada a todos os projetos que não tenham configuração local.

### 4️⃣ Reiniciar o Kiro

Feche e reabra o Kiro completamente para aplicar as configurações.

---

## ✅ Verificação

Após reiniciar, o servidor MCP deve estar disponível. Você pode testar usando os comandos:
- `search_experience_league` - Buscar na documentação
- `read_documentation` - Ler documentação específica
- `get_available_services` - Listar serviços disponíveis

---

## 🔧 Troubleshooting

**Problema**: Servidor não conecta
- Verifique se o Docker está rodando: `docker ps`
- Verifique se a imagem foi construída: `docker images | grep aem-docs-mcp-server`
- Verifique os logs do MCP no painel do Kiro

**Problema**: Imagem não encontrada
- Reconstrua a imagem: `docker build -t aem-docs-mcp-server:latest .`