# Submission: Dictionary

Light Tool Library metadata - paste into the **Add Tool** form.

| Field | Value |
|---|---|
| **Title** | Dictionary |
| **GitHub Repo** | https://github.com/<you>/dictionary-light |
| **Description** | An English dictionary for the Light Phone III, powered by Wiktionary. Search any word and read its definitions. No account, no ads, no tracking. |
| **Tags** | Education *(provisional)* |

## Checklist
- [x] versionCode bumped (4)
- [x] Built with the latest light-sdk
- [ ] Public repo created
- [ ] >=1 screenshot added (required for approval)
- [x] Description <= 255 chars

## Build artifact
- **Release APK (minified + shrink + arm64-v8a):** `Dictionary-1.3.0-release.apk`
  - Package: `com.thelightphone.dictionary` / `serverPackage = com.lightos`
  - versionName = 1.3.0, versionCode = 4 (verified inside the APK via aapt2)
- **Debug APK:** `Dictionary-1.3.0-debug.apk`
- **Source:** `tool/src/main/kotlin/com/thelightphone/dictionary/`

## What's new in 1.3.0
- **Recents** — recent words persist (max 10), shown under a bottom-left
  "Recents" label that expands/collapses on tap; the list starts collapsed.
- **Show recents toggle** in About — hides the section when off.
  **Clear recents** with a tap-to-confirm step.
- **1.2.0 fixes carried over:** pronunciation no longer renders as boxes
  (IPA folded to a readable ASCII respelling); synonyms/antonyms are tappable
  and open that word's entry in place.
- Package `com.thelightphone.dictionary` (Light package-name requirements);
  About page mirrors the Wikipedia Light tool's attribution (Definitions /
  Data source / Tool); LICENSE carries no personal copyright line.
