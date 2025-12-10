# Prompt: Configure Agent Hook - AEM Cloud Service SonarQube Validator

## Objective

Configure an Agent Hook in Kiro IDE to automatically validate AEM Cloud Service SonarQube code quality rules whenever a file is edited.

## Instructions for the Agent

Create an Agent Hook with the following configurations based on the generated `.kiro/hooks/aemcs-sonarqube-validator.kiro.hook` file:

### 1. Hook Configuration (JSON Structure)

```json
{
  "enabled": true,
  "name": "AEM Cloud Service - SonarQube Code Quality Validator",
  "description": "Validates SonarQube code quality rules for AEM Cloud Service projects when files are edited, analyzing violations by severity and providing specific correction suggestions",
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
    "prompt": "Analyze the modified file applying the SonarQube code quality rules for AEM Cloud Service defined in #[[file:.kiro/steering/aemcs-sonarqube-rules.md]].\n\n## Analysis Instructions\n\n1. **Identify the file type** and apply only relevant rules:\n   - `.java` → Java Rules (java:S*, AEM Rules:*, CQRules:*, findbugs:*)\n   - `.xml` → Package rules, Oak Index, AEM components\n   - `.any`, `.rules`, `.vhost`, `.conf` → Dispatcher Rules (DOTRules:*)\n   - `.js`, `.ts`, `.jsx`, `.tsx` → JavaScript/TypeScript best practices\n   - `.html`, `.jsp`, `.jspx` → Template and component rules\n\n2. **Prioritize by severity**:\n   - 🔴 **Blocker**: MUST be fixed immediately\n   - 🔴 **Critical**: MUST be fixed before deployment\n   - � i**Major**: SHOULD be fixed\n   - 🟡 **Minor**: Consider fixing\n   - ⚪ **Info**: Informational\n\n3. **For each violation found, report**:\n   - Code line\n   - Violated rule (Key)\n   - Problem description\n   - Correction suggestion\n\n4. **Priority rules for Java**:\n   - `java:S2095` - Resources should be closed\n   - `AEM Rules:AEM-6` - ResourceResolver should be closed in finally block\n   - `AEM Rules:AEM-7` - Session should be logged out in finally block\n   - `AEM Rules:AEM-3` - Non-thread safe object used as field of Servlet/Filter\n   - `CQRules:ConnectionTimeoutMechanism` - HTTP requests should have timeouts\n   - `java:S2068` - Hard-coded passwords are security-sensitive\n\n5. **Priority rules for Dispatcher**:\n   - `DOTRules:Disp-2` - statfileslevel should be >= 2\n   - `DOTRules:Disp-4` - Default deny rules from archetype\n   - `DOTRules:Disp-5` - serveStaleOnError should be enabled\n\n6. **Cloud Service compatibility rules**:\n   - `BannedPath` - Customer packages should not install content under /libs\n   - `CloudServiceIncompatibleWorkflowProcess` - Usage of incompatible workflow processes\n   - `CQRules:AMSCORE-553` - AEM Deprecated APIs Should Not Be Used\n\n## Output Format\n\nIf violations are found:\n```\n## 🔍 Code Quality Analysis - AEM Cloud Service\n**File**: `{file_name}`\n**Violations found**: {number}\n\n### Violations\n#### 🔴 Blocker/Critical\n- **Line {X}**: `{code}`\n- **Rule**: {key}\n- **Problem**: {description}\n- **Fix**: {suggestion}\n\n#### 🟠 Major\n...\n\n#### 🟡 Minor\n...\n\n### Summary\n- Blocker: X\n- Critical: X\n- Major: X\n- Minor: X\n```\n\nIf no violations are found:\n```\n## ✅ Code Quality Analysis - AEM Cloud Service\n**File**: `{file_name}`\nNo SonarQube rule violations were found. The code complies with AEM Cloud Service quality guidelines.\n```"
  }
}
```

### 2. Implementation Steps

#### Option A: Via Command Palette
1. Open the Command Palette (`Ctrl+Shift+P` or `Cmd+Shift+P`)
2. Type: `Open Kiro Hook UI`
3. Click "Create New Hook"
4. Configure using the values from the JSON structure above

#### Option B: Via JSON File (Recommended)
1. Create the file `.kiro/hooks/aemcs-sonarqube-validator.kiro.hook`
2. Paste the complete JSON structure above
3. Save the file
4. The hook will be automatically loaded by Kiro IDE

### 3. Configuration Validation

After creating the hook, verify:

1. **File created**: `.kiro/hooks/aemcs-sonarqube-validator.kiro.hook` exists
2. **Steering file**: `.kiro/steering/aemcs-sonarqube-rules.md` is available
3. **Trigger configured**: `fileEdited` for the specified file patterns
4. **Prompt references**: The steering file using `#[[file:.kiro/steering/aemcs-sonarqube-rules.md]]`

### 4. Hook Testing

To test if the hook is working:

1. Edit a `.java` file in the project
2. Save the file
3. The hook should be triggered automatically
4. Verify that SonarQube analysis is executed
5. Confirm that violations are reported in the specified format

### 5. Expected Final Structure

```
.kiro/
├── hooks/
│   └── aemcs-sonarqube-validator.kiro.hook
└── steering/
    └── aemcs-sonarqube-rules.md
```

---

## References

- Steering file: `.kiro/steering/aemcs-sonarqube-rules.md`
- [Custom Code Quality Rules](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules)
- [Code Quality Testing](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/code-quality-testing)

---

*Created on: December 10, 2025*
