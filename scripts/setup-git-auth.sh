#!/usr/bin/env bash
set -e

echo "=== GitHub CLI & Git Credential Setup ==="

# Check for gh in PATH or /opt/homebrew/bin
if ! command -v gh &> /dev/null; then
    if [ -x "/opt/homebrew/bin/gh" ]; then
        export PATH="/opt/homebrew/bin:$PATH"
    else
        echo "Error: gh (GitHub CLI) is not installed."
        exit 1
    fi
fi

# Check authentication status
if gh auth status &> /dev/null; then
    echo "✅ GitHub CLI is already authenticated."
else
    echo "🔑 Launching GitHub web authentication..."
    gh auth login -w -s repo,workflow
fi

# Configure git to use gh as the credential helper
echo "⚙️ Configuring git to use GitHub CLI credentials..."
gh auth setup-git

echo "✅ Git is now configured to authenticate automatically using gh!"
echo "You can now run 'git push origin main' without password prompts."
