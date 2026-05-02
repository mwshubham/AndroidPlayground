#!/bin/sh
# Git pre-commit hook that runs code quality checks
# Generated automatically by Gradle task 'installGitHooks'

echo "Running code quality checks..."
echo "This may take a moment..."

# Resolve the repo root so this hook works regardless of where git invokes it
REPO_ROOT=$(git rev-parse --show-toplevel)

# Capture which tracked files were staged before we run format
STAGED_FILES=$(git diff --name-only --cached --diff-filter=ACM)

# Run ktlintFormat first (auto-fixes formatting violations in-place)
"$REPO_ROOT/gradlew" -p "$REPO_ROOT" ktlintFormat --console=plain

# Re-stage any files that ktlintFormat modified so the commit includes the fixed versions
if [ -n "$STAGED_FILES" ]; then
    for file in $STAGED_FILES; do
        if [ -f "$REPO_ROOT/$file" ]; then
            git add "$REPO_ROOT/$file"
        fi
    done
fi

# Now run the full check (ktlint + detekt); fail the commit if anything is still broken
if ! "$REPO_ROOT/gradlew" -p "$REPO_ROOT" ktlintCheck detektCheckAll --console=plain; then
    echo ""
    echo "❌ Code quality checks failed!"
    echo "Please fix the issues above and try committing again."
    echo ""
    echo "To temporarily skip these checks, use: git commit --no-verify"
    echo "To disable the hook permanently, run: ./gradlew uninstallGitHooks"
    exit 1
fi

echo "✅ Code quality checks passed!"
exit 0
