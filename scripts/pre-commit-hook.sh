#!/bin/sh
# Git pre-commit hook that runs code quality checks
# Generated automatically by Gradle task 'installGitHooks'

echo "Running code quality checks..."
echo "This may take a moment..."

# Run the code quality check task and check if it was successful
if ! ./gradlew codeQualityFormatAndCheck --console=plain; then
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
