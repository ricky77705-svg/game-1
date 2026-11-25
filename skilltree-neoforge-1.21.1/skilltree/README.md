# Simple Skill Tree (NeoForge 1.21.1)

- Gain 1 skill point per XP level (highest level reached counts, so enchanting can't be farmed).
- Press **K** to open the tree; click a node to unlock it (parent skill required).
- 4 branches x 3 tiers: Vitality (max health), Might (attack damage), Swiftness (speed), Prospector (mining speed).
- Commands: `/skilltree reset` (refund all), `/skilltree points <n>` (op only, for testing).

## Build
This folder has no Gradle wrapper. Copy `gradlew`, `gradlew.bat` and `gradle/` from the official
NeoForge 1.21.1 MDK (github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle), or run `gradle wrapper` with Gradle 8.x.
Then: `./gradlew runClient` to test, `./gradlew build` for the jar in build/libs.
Edit skills in `Skills.java`.
