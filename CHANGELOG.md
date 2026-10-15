# Changelog

## 1.3.0

- **Recents** — looked-up words are remembered (up to 10) and listed under a
  "Recents" label in the bottom-left of the home screen; tap the label to
  expand/collapse the list, tap a word to look it up again. The list starts
  collapsed and persists across launches.
- **Show recents toggle** in the About screen (gear icon); when off, the
  Recents section is hidden entirely. **Clear recents** sits next to it with
  a tap-to-confirm step.
- About page reworked to mirror the Wikipedia Light tool's attribution
  (Definitions / Data source / Tool sections).
- LICENSE no longer carries a personal copyright line.

## 1.2.0

- Fixed: pronunciation no longer shows empty boxes on the Light Phone. Wiktionary's
  IPA strings carry stress/length marks and IPA letters outside the phone's font
  coverage; the pronunciation line is folded to a readable ASCII respelling
  (`/ˈdɪk.ʃə.nɛɹ.i/` -> `/dik.shuh.ner.i/`), and the monospace font was dropped
  in favor of LightOS's proportional type.
- Fixed: synonyms and antonyms are now tappable and open that word's entry in
  place (previously every tap fell through to the search editor).
- Package renamed `com.mortimort.dictionary` -> `com.thelightphone.dictionary`
  to meet Light package-name requirements.
- Attribution page credits the Light Phone SDK.

## 1.1.0

- Restructured for submission to the Light Tool Library: standalone repo with the Light SDK as a
  git submodule, `tool/` module layout, MIT license, README, and privacy disclosure.
- Now targets the real Light Phone III (`serverPackage = "com.lightos"`).
- Version bumped to 1.1.0 (versionCode 2).

## 1.0.0

- Initial release.
