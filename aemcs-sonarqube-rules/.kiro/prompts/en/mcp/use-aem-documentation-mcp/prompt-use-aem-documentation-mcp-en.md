# 🚨 MANDATORY PROMPT: Generate Complete AEM SonarQube Rules Documentation

## ⚠️ ATTENTION: MANDATORY USE OF MCP AEM DOCUMENTATION

**🔴 THIS PROMPT REQUIRES MANDATORY USE OF MCP AEM DOCUMENTATION**

**YOU MUST**:
1. ✅ Configure MCP AEM Documentation **BEFORE** any other action
2. ✅ Read official documentation via MCP **IN PARTS** (large documents > 1000 lines)
3. ✅ **WRITE ALL ITEMS** found in official documentation
4. ✅ Compare with CSVs and **ADD NEW RULES** not in documentation
5. ✅ **WAIT 3 MINUTES** before copying to steering file

**🚫 NOT ALLOWED**:
- ❌ Skip using MCP
- ❌ Use only CSV files
- ❌ Generate document without official documentation data
- ❌ Ignore rules or items from documentation
- ❌ Copy to steering immediately (causes truncation)

---

## 🎯 Objective

Generate a **COMPLETE** technical document about Cloud Manager custom code quality rules for Adobe Experience Manager as a Cloud Service (AEMaaCS).

**MANDATORY 3-PHASE PROCESS**:

### PHASE 1: COMPLETE Reading of Official Documentation via MCP
- Read **ALL** official documentation pages
- **IN PARTS** using `start_index` for large documents
- **WRITE EACH RULE** found in the final document

### PHASE 2: Comparison with CSVs
- Read CSV files
- **IDENTIFY NEW RULES** not in official documentation
- **ADD** these rules to final document

### PHASE 3: Steering Creation (WITH WAIT)
- **WAIT 3 MINUTES** after generating main document
- Copy summarized content to steering
- Verify steering file is complete

---

## 📖 PHASE 1: COMPLETE READING OF OFFICIAL DOCUMENTATION

### 🚨 CRITICAL INSTRUCTION: READING IN PARTS

**Official documentation has more than 1000 lines. YOU MUST:**

1. **First reading** - Document beginning:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=0
)
```

2. **Second reading** - Continuation:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=10000
)
```

3. **Third reading** - Continuation:
```
mcp_aem_documentation_mcp_server_read_documentation(
  url="https://experienceleague.adobe.com/en/docs/experience-manager-cloud-manager/content/using/custom-code-quality-rules",
  max_length=10000,
  start_index=20000
)
```

4. **Continue reading** until receiving message that document ended or empty content.

### 📝 FOR EACH PART READ, YOU MUST:

1. **EXTRACT ALL RULES** found in that part
2. **WRITE TO DOCUMENT** immediately (using fsWrite or fsAppend)
3. **DO NOT SKIP ANY RULE** - each rule must be documented
4. **INCLUDE CODE EXAMPLES** compliant and non-compliant

### ✅ READING BY PARTS CHECKLIST:

- [ ] Reading 1 (start_index=0): Extract and write rules
- [ ] Reading 2 (start_index=10000): Extract and write rules
- [ ] Reading 3 (start_index=20000): Extract and write rules
- [ ] Reading 4 (start_index=30000): Extract and write rules
- [ ] Reading 5 (start_index=40000): Extract and write rules
- [ ] Continue until document complete

### 🔍 MANDATORY COMPLEMENTARY SEARCHES:

After reading main documentation, execute additional searches:

```
mcp_aem_documentation_mcp_server_search_experience_league(
  query="custom code quality rules",
  content_types=["Documentation"],
  include_all_aem_products=true
)
```

```
mcp_aem_documentation_mcp_server_search_experience_league(
  query="sonarqube rules cloud manager",
  content_types=["Documentation"],
  include_all_aem_products=true
)
```

```
mcp_aem_documentation_mcp_server_search_experience_league(
  query="oakpal content rules aem",
  content_types=["Documentation"],
  include_all_aem_products=true
)
```

---

## 📊 PHASE 2: COMPARISON WITH CSVs

### CSV Files to Analyze:
- `assets/CodeQuality-rules-latest-AMS-2024-12-0.csv` (latest version)
- `assets/CodeQuality-rules-latest-AMS.csv` (previous version)

### COMPARISON PROCESS:

1. **Read latest CSV**
2. **For each rule in CSV**:
   - Check if already documented in Phase 1
   - If **NOT** documented → **ADD** to document
   - If **ALREADY** documented → Check if there's additional information in CSV
3. **Identify new rules** between CSV versions
4. **Document changes** (key migrations, new rules, removed)

### FORMAT FOR ADDITIONAL CSV RULES:

```markdown
### [Rule Key] - [Rule Name]

| Attribute | Value |
|-----------|-------|
| **Key** | [rule_key] |
| **Type** | [Vulnerability/Bug/Code Smell/Security Hotspot] |
| **Severity** | [Blocker/Critical/Major/Minor/Info] |
| **Tags** | [CSV tags] |
| **Old Key** | [if migration exists] |

**Description**: [CSV description or inferred]

**Source**: CSV (not found in official documentation)
```

---

## ⏳ PHASE 3: STEERING CREATION (WITH MANDATORY WAIT)

### 🚨 CRITICAL INSTRUCTION: WAIT BEFORE COPYING

**PROBLEM**: Steering file frequently gets truncated when copied immediately.

**MANDATORY SOLUTION**:

1. **AFTER generating main document** (`output-aemcs-sonarqube-rules-en.md`)
2. **WAIT 3 MINUTES** (180 seconds)
3. **VERIFY** main document is complete
4. **THEN** create steering file

### STEERING CREATION PROCESS:

```bash
# STEP 1: Verify main document
wc -l output-aemcs-sonarqube-rules-en.md
# Should have more than 1000 lines

# STEP 2: Wait (MANDATORY)
echo "Waiting 3 minutes before creating steering..."
sleep 180

# STEP 3: Create steering with summarized content
```

### STEERING CONTENT:

The `.kiro/steering/aemcs-sonarqube-rules.md` file should contain:

```markdown
---
inclusion: always
---

# AEM Cloud Service - SonarQube Code Quality Rules

## 📊 Statistical Summary
- **Total Rules**: [exact number]
- **Vulnerabilities**: [X] rules
- **Security Hotspots**: [X] rules
- **Bugs**: [X] rules
- **Code Smells**: [X] rules

## 🔴 Critical Rules (Blocker/Critical)

[LIST ALL Blocker and Critical rules with their keys]

## ☁️ Cloud Service Compatibility Rules

[LIST Cloud Service specific rules]

## 🔄 Key Migrations (SonarQube 9.9)

[LIST squid:* → java:* migrations]

## 📚 Complete Reference

Complete document: `output-aemcs-sonarqube-rules-en.md`

Sources:
- Official Adobe Experience League documentation (via MCP)
- CSV: CodeQuality-rules-latest-AMS-2024-12-0.csv
- CSV: CodeQuality-rules-latest-AMS.csv
```

### ✅ STEERING VERIFICATION:

After creating steering, **VERIFY**:

```bash
# Count steering lines
wc -l .kiro/steering/aemcs-sonarqube-rules.md

# Should have at least 100 lines
# If less than 50 lines, file is TRUNCATED - RECREATE
```

---

## 📝 MAIN DOCUMENT STRUCTURE

### File: `output-aemcs-sonarqube-rules-en.md`

```markdown
# Custom Code Quality Rules - AEM Cloud Service

**Last updated:** [Current date]
**Sources:** 
- Adobe Experience League (official documentation via MCP)
- CodeQuality-rules-latest-AMS-2024-12-0.csv
- CodeQuality-rules-latest-AMS.csv

---

## 📊 Statistics

| Type | Count |
|------|-------|
| Total Rules | XXX |
| Vulnerabilities | XX |
| Security Hotspots | XX |
| Bugs | XX |
| Code Smells | XX |

---

## 🔴 Vulnerability Rules

[ALL vulnerability rules]

---

## 🟠 Security Hotspot Rules

[ALL security hotspot rules]

---

## 🔵 Bug Rules

[ALL bug rules]

---

## 🟡 Code Smell Rules

[ALL code smell rules]

---

## 📦 OakPAL Rules

[ALL OakPAL rules]

---

## 🔧 Dispatcher Rules (DOT)

[ALL Dispatcher rules]

---

## 🔄 Changelog and Migrations

[Changes between versions, key migrations]

---

## 📚 References

[Links to official documentation]
```

---

## ✅ COMPLETE EXECUTION CHECKLIST

### PHASE 1 - MCP Reading:
- [ ] Reading part 1 (start_index=0) executed
- [ ] Reading part 2 (start_index=10000) executed
- [ ] Reading part 3 (start_index=20000) executed
- [ ] Reading part 4 (start_index=30000) executed
- [ ] Reading part 5+ (continue until end) executed
- [ ] ALL rules from official documentation written to document
- [ ] Complementary searches executed

### PHASE 2 - CSV Comparison:
- [ ] Latest CSV read
- [ ] Previous CSV read
- [ ] New rules identified
- [ ] Additional CSV rules written to document
- [ ] Key migrations documented

### PHASE 3 - Steering:
- [ ] Main document verified (>1000 lines)
- [ ] **WAITED 3 MINUTES**
- [ ] Steering created
- [ ] Steering verified (>100 lines)
- [ ] If truncated, RECREATED

### FINAL VALIDATION:
- [ ] Main document complete
- [ ] Steering complete (not truncated)
- [ ] All rules documented
- [ ] Code examples included
- [ ] **SYNCHRONIZATION**: If `output-aemcs-sonarqube-rules-en.md` was modified, steering was updated

---

## 🔄 MANDATORY SYNCHRONIZATION RULE

### 🚨 CRITICAL INSTRUCTION: KEEP STEERING SYNCHRONIZED

**EVERY TIME the `output-aemcs-sonarqube-rules-en.md` file is modified, you MUST:**

1. **UPDATE** the `.kiro/steering/aemcs-sonarqube-rules.md` file
2. **WAIT 3 MINUTES** before updating (avoid truncation)
3. **VERIFY** steering was updated correctly

### WHEN TO UPDATE STEERING:

| Main Document Action | Steering Action |
|---------------------|-----------------|
| New rule added | Update count and critical rules list |
| Rule removed | Update count and remove from list |
| Statistics changed | Update statistical summary |
| New migrations | Update migrations section |
| Any modification | **ALWAYS** update steering |

### SYNCHRONIZATION PROCESS:

```bash
# STEP 1: Check if main document was modified
# (If you just modified output-aemcs-sonarqube-rules-en.md)

# STEP 2: Wait 3 minutes
echo "Waiting 3 minutes before updating steering..."
sleep 180

# STEP 3: Update steering with updated data
# - Recalculate statistics
# - Update critical rules list
# - Update migrations

# STEP 4: Verify steering
wc -l .kiro/steering/aemcs-sonarqube-rules.md
# Should have >100 lines
```

### ⚠️ SYNCHRONIZATION VALIDATION:

**BEFORE FINALIZING, CONFIRM:**

- [ ] Main document was modified? → Steering was updated?
- [ ] Steering statistics match main document?
- [ ] Critical rules list is updated?
- [ ] Steering is not truncated (>100 lines)?

---

## 🚨 ERROR HANDLING

### If MCP reading returns truncated:
1. **CONTINUE** with next start_index
2. **DO NOT STOP** until document is complete

### If steering gets truncated:
1. **DELETE** truncated file
2. **WAIT** 2 more minutes
3. **RECREATE** steering file
4. **VERIFY** again

### If CSV cannot be read:
1. **TRY** alternative reading
2. **DOCUMENT** that CSV was not included
3. **CONTINUE** with MCP data

---

## 📊 SUCCESS METRICS

| Metric | Minimum Expected |
|--------|------------------|
| Documented rules | 150+ |
| Main document lines | 1000+ |
| Steering lines | 100+ |
| Code examples | 20+ |
| MCP parts read | 5+ |

---

## 📋 EXECUTIVE SUMMARY

**WHEN FINALIZING, REPORT:**

```
=== EXECUTION REPORT ===

PHASE 1 - MCP:
✅ Parts read: [X] parts
✅ Rules extracted: [X] rules
✅ Examples obtained: [X] examples

PHASE 2 - CSV:
✅ Rules in CSV: [X] rules
✅ New rules (not in MCP): [X] rules
✅ Migrations identified: [X] migrations

PHASE 3 - STEERING:
✅ Waited: [X] minutes
✅ Steering lines: [X] lines
✅ Status: [COMPLETE/TRUNCATED]

TOTALS:
- Main document: [X] lines
- Total rules: [X] rules
- Sources used: MCP + CSV1 + CSV2

FINAL STATUS: [SUCCESS/FAILURE]
```

---

## 🔐 FINAL VALIDATION CONDITION

### 🚨 MANDATORY VALIDATION BEFORE CONCLUDING:

**WORK IS ONLY COMPLETE IF:**

1. ✅ **Main document exists** and has >1000 lines
2. ✅ **Steering exists** and has >100 lines
3. ✅ **Steering is SYNCHRONIZED** with main document
4. ✅ **Statistics match** between document and steering
5. ✅ **No truncated files**

### FINAL VALIDATION COMMAND:

```bash
# Execute MANDATORILY before finalizing

echo "=== FINAL VALIDATION ==="

# 1. Verify main document
DOC_LINES=$(wc -l < output-aemcs-sonarqube-rules-en.md 2>/dev/null || echo "0")
echo "Main document: $DOC_LINES lines"
[ "$DOC_LINES" -gt 1000 ] && echo "✅ OK" || echo "❌ FAILURE: <1000 lines"

# 2. Verify steering
STEERING_LINES=$(wc -l < .kiro/steering/aemcs-sonarqube-rules.md 2>/dev/null || echo "0")
echo "Steering: $STEERING_LINES lines"
[ "$STEERING_LINES" -gt 100 ] && echo "✅ OK" || echo "❌ FAILURE: <100 lines (TRUNCATED)"

# 3. Verify synchronization
echo ""
echo "=== VERIFY MANUAL SYNCHRONIZATION ==="
echo "Compare main document statistics with steering"
echo "If different, UPDATE steering before finalizing"
```

### ❌ DO NOT FINALIZE IF:

- Main document has <1000 lines
- Steering has <100 lines (truncated)
- Steering was not updated after main document modification
- Statistics don't match between document and steering

### ✅ FINALIZE ONLY IF:

- All validations passed
- Steering is synchronized
- No truncated files
- Execution report generated
