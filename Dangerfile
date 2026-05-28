# frozen_string_literal: true

# ─────────────────────────────────────────────────────────────────────────────
# Rule 1 — PR description must be filled
# ─────────────────────────────────────────────────────────────────────────────
MIN_DESCRIPTION_LENGTH = 10

pr_body = github.pr_body.to_s.strip
if pr_body.length < MIN_DESCRIPTION_LENGTH
  failure "PR description is missing or too short (< #{MIN_DESCRIPTION_LENGTH} chars). " \
          "Please explain *what* this PR changes and *why*."
end

# ─────────────────────────────────────────────────────────────────────────────
# Rule 2 — PR size limit warning (> 500 changed lines)
# ─────────────────────────────────────────────────────────────────────────────
PR_SIZE_THRESHOLD = 500

total_changes = git.insertions + git.deletions
if total_changes > PR_SIZE_THRESHOLD
  warn "This PR changes **#{total_changes}** lines " \
       "(threshold: #{PR_SIZE_THRESHOLD}). " \
       "Consider splitting it into smaller, focused PRs for easier review."
end

# ─────────────────────────────────────────────────────────────────────────────
# Rule 3 — New Kotlin source files should have accompanying tests
#
# Exempt from this check:
#   • Hilt DI modules  (di/ directory or *Module.kt)
#   • Application / Activity entry-points
#   • Navigation graphs and route definitions
#   • *Screen.kt — thin composables that delegate to *Content; test the Content
# ─────────────────────────────────────────────────────────────────────────────
EXEMPT_PATTERNS = [
  %r{/di/},
  /Module\.kt$/,
  /Application\.kt$/,
  /Activity\.kt$/,
  /NavGraph/,
  /Route\.kt$/,
  /Screen\.kt$/,
].freeze

new_main_kt_files = git.added_files.select do |f|
  f.end_with?(".kt") &&
    f.match?(%r{src/main/}) &&
    EXEMPT_PATTERNS.none? { |pattern| f.match?(pattern) }
end

new_main_kt_files.each do |kt_file|
  # src/main/kotlin/…/Foo.kt → src/test/kotlin/…/FooTest.kt
  expected_test = kt_file
    .sub(%r{src/main/}, "src/test/")
    .sub(/\.kt$/, "Test.kt")

  has_test = git.added_files.include?(expected_test) || File.exist?(expected_test)

  unless has_test
    warn "**`#{File.basename(kt_file)}`** is a new Kotlin file with no accompanying test. " \
         "Expected: `#{expected_test}`"
  end
end
