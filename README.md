# 🛡️ IntelliJ SCA Scanner Plugin

[![Build](https://github.com/gs-skosaraju/intellij-sca-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/gs-skosaraju/intellij-sca-plugin/actions)

Advanced Software Composition Analysis (SCA) scanner plugin for IntelliJ IDEA.

## ⚠️ Cannot Build Locally (Corporate Network)

**This project CANNOT be built from corporate networks** due to SSL certificate blocking. 

### ✅ Download Pre-Built Plugin

1. Go to **[GitHub Actions](https://github.com/gs-skosaraju/intellij-sca-plugin/actions)**
2. Click latest **"Build Plugin"** workflow
3. Download **intellij-sca-plugin** artifact
4. Extract ZIP to get the plugin file inside

## Features

- Multi-source vulnerability scanning (7 sources)
- Reachability analysis
- CISA KEV integration
- npm, Maven, Gradle, Python support
- Beautiful UI with severity breakdown

## Install Plugin

1. Download from GitHub Actions
2. IntelliJ → **Settings → Plugins → ⚙️ → Install Plugin from Disk**
3. Select ZIP file
4.  Restart IntelliJ

## Usage

- **Tools → SCA Scanner → Run Security Scan**
- Keyboard: `Ctrl+Alt+Shift+S`

## Requirements

- IntelliJ IDEA 2023.2+
- Node.js
- Compiled `cli-simple. js` from VS Code extension
EOF

# Commit and push
git add . github/workflows/build.yml README.md
git commit -m "JIRA-1234: Add GitHub Actions CI/CD pipeline"
git push

echo ""
echo "✅ Done! GitHub will build it automatically."
echo "Check: https://github.com/gs-skosaraju/intellij-sca-plugin/actions"