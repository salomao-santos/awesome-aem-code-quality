# AEM Documentation MCP Configuration

## 📋 Prerequisites

Before starting, make sure you have:

- **Docker Desktop** installed and running
- Validation: run `docker --version` in the terminal

---

## 🚀 Step by Step

### 1️⃣ Check if Docker Image Exists

Check if the image is already available:

```bash
docker images | grep aem-docs-mcp-server
```

**If the image already exists**, skip to step 3️⃣.

**If the image doesn't exist**, continue to step 2️⃣.

### 2️⃣ Clone and Build (Only if Necessary)

#### Clone the Repository

If you don't have the source code yet:

```bash
git clone https://github.com/salomao-santos/adobe-experience-manager-mcps.git
cd adobe-experience-manager-mcps/aem_documentation_mcp_server
```

**Note**: If you've already cloned the repository previously, just navigate to the folder:

```bash
cd adobe-experience-manager-mcps/aem_documentation_mcp_server
```

#### Build the Docker Image

```bash
docker build -t aem-docs-mcp-server:latest .
```

### 3️⃣ Configure MCP in Kiro

Choose one of the options below:

#### 🎯 Option A: Workspace Configuration (Recommended)

**Advantages:**
- Project-specific configuration
- Doesn't affect other projects
- Version-controllable in Git
- Overrides global configuration

**File**: `.kiro/settings/mcp.json` (at your project root)

**If the file doesn't exist**, create it with the complete content:

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

**If the file already exists**, add only this block inside `"mcpServers"`:

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

#### 🌐 Option B: Global Configuration (All Projects)

**File**: `~/.kiro/settings/mcp.json`

Use the same JSON structure as Option A. This configuration will be applied to all projects that don't have a local configuration.

### 4️⃣ Restart Kiro

Close and reopen Kiro completely to apply the settings.

---

## ✅ Verification

After restarting, the MCP server should be available. You can test using the commands:
- `search_experience_league` - Search documentation
- `read_documentation` - Read specific documentation
- `get_available_services` - List available services

---

## 🔧 Troubleshooting

**Problem**: Server doesn't connect
- Check if Docker is running: `docker ps`
- Check if the image was built: `docker images | grep aem-docs-mcp-server`
- Check MCP logs in the Kiro panel

**Problem**: Image not found
- Rebuild the image: `docker build -t aem-docs-mcp-server:latest .`