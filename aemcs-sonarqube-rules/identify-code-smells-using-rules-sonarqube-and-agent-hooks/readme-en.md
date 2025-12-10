# Example Code Smell for SonarQube

This folder contains example Java files with intentional code smells and SonarQube violations for testing and demonstration purposes.

## 🤖 Kiro IDE Agent Hook

Kiro IDE has a configured **agent hook** that automatically analyzes code when you save files in this folder. 

### How it works:

1. **Save the file** - When you save a Java file with code smells
2. **Automatic analysis** - The agent hook triggers automatically
3. **Vulnerability identification** - Code smells and violations are detected
4. **Recommendations** - You receive specific correction suggestions
5. **Next steps** - Actionable guidance on how to fix the issues

### What gets detected:

- Resource leaks (unclosed ResourceResolver, connections, etc.)
- Missing timeouts in HTTP requests
- Improper exception handling (printStackTrace, System.out usage)
- Hardcoded paths and values
- Deprecated or dangerous method calls
- Security vulnerabilities
- Performance issues
- Best practice violations

### Example violations in this folder:

The `example-test.java` file contains multiple intentional violations including:
- Unclosed ResourceResolver (CQBP-72)
- HTTP requests without timeout (ConnectionTimeoutMechanism)
- System.out.println usage (CQBP-44)
- Exception.printStackTrace() (CQBP-44)
- Hardcoded paths (CQBP-71)
- Deprecated Thread.stop() usage (CWE-676)

Simply edit and save the file to see the agent hook in action!
