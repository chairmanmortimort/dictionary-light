# Dictionary

A minimal dictionary for the Light Phone III. Look up a word and read a concise definition, sourced live from Wiktionary.

A community tool for the [Light Phone III](https://www.thelightphone.com/), built on the official
[light-sdk](https://github.com/lightphone/light-sdk) with Kotlin, Jetpack Compose, `LightScreen`
and `LightViewModel`, themed with `sdk:ui`.

## What this tool does

- **Search** — look up any English word and read its grouped definitions
  (by part of speech), with a readable pronunciation line and tappable
  synonyms and antonyms that open that word's entry in place.
- **Recents** — your last looked-up words, collapsed under a single "Recents"
  label in the bottom-left; tap it to expand the list, tap a word to look it up
  again. Toggleable in About — turning it off hides the section entirely.
- **Haptics** — taps give a small vibration, following the phone's system
  haptic setting.

## Settings

Accessed via the gear icon on the home screen. Toggle Show recents on or off;
clear your recent words list (tap once to arm, again to confirm). The About
page carries full attribution (definitions license, data source, tool).

## Layout

```
dictionary/
├── light-sdk/            # git submodule -> upstream Light SDK (pinned commit)
├── tool/                 # the ONLY dev-owned module
│   ├── lighttool.toml    # tool id, label, version, declared permissions
│   ├── build.gradle.kts  # dependencies (project(":sdk:client") + light.sdk plugin)
│   └── src/main/**       # tool source
├── settings.gradle.kts   # grafts the submodule's SDK projects into this build
├── build.gradle.kts      # thin root: plugin classpath + ext build knobs
├── gradle.properties
└── gradlew, gradle/       # wrapper
```

Light's containerized builder clones this repo and extracts **only**
`tool/build.gradle.kts`, `tool/lighttool.toml`, and `tool/src/main/**`, then compiles them against
its own pinned, sandboxed copy of the SDK. It generates `AndroidManifest.xml` from `lighttool.toml`,
so this repo intentionally contains no hand-written manifest and never sets `applicationId`,
`versionCode`, `versionName`, or `namespace`.

## Building locally

Requires JDK 17 and an Android SDK. Then:

```
git submodule update --init --recursive
./gradlew :tool:assembleDebug
# -> tool/build/outputs/apk/debug/tool-debug.apk
```

The APK is signed with the shared Light dev keystore (from the submodule) for local sideloading.
Light's build service re-signs with its own key.

## Privacy

The Dictionary sends the word you search to the public Wiktionary API (en.wiktionary.org) to fetch its definition. No account is required, nothing is stored on a server, and there is no analytics or tracking. Search history stays on the device.

See PRIVACY.md for the network endpoints it contacts.

## License

MIT, the same as upstream light-sdk. See [LICENSE](LICENSE).

The Light Phone, LightOS, and Light Phone III are names of The Light Phone; this project is not
affiliated with or endorsed by The Light Phone.
