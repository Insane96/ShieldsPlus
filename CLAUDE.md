# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Related Repositories

Most of Insane96's mods (including InsaneLib) have their source checked out as sibling directories of this one, e.g. `../InsaneLib`. When investigating how an InsaneLib API is implemented (events, mixins, utils), check that sibling folder directly instead of only relying on the compiled dependency jar.

## Project Overview

**Shields+** is a NeoForge mod for Minecraft 1.21.1 that overhauls shield mechanics: tiered shields (wooden/stone/copper/iron/golden/diamond/netherite), fixed-damage blocking, blocking time limits with cooldowns, crouching-to-block, reduced shield disable duration, a configurable blocking angle, parrying, and custom shield-only enchantments.

- Mod ID: `shieldsplus`
- NeoForge version: 21.1.230
- Java 21

## Build Commands

```bash
# Build the mod jar
./gradlew build

# Run the Minecraft client (for manual testing)
./gradlew runClient

# Run the Minecraft server
./gradlew runServer

# Run data generators (generates assets/data into src/generated/resources/)
./gradlew runData

# Refresh dependencies if something breaks
./gradlew --refresh-dependencies
```

There are no unit tests in this project. Testing is done by running the game.

## Architecture

### Feature System (InsaneLib)

All gameplay logic lives in `SPFeature` (`module/SPFeature.java`), which extends InsaneLib's `Feature` base class. Config fields are annotated with `@Config` and automatically exposed to the mod config file. `SPFeature` is the single feature class — it holds all toggle booleans and numeric tuning values, and subscribes to NeoForge events directly.

Notable mechanics driven from `SPFeature`:
- **Blocking angle** — `blockingAngle` (config, default 100°, vanilla is 180°) defines the frontal arc in which an attack can be blocked; enforced in `LivingEntityMixin#shieldsPlus$blockingAngleThreshold`.
- **Parry** — blocking within `parry$window` seconds (default 0.1s) of starting to use the shield multiplies blocked damage by `1 + parry$bonusDamageBlocked`, handled in `SPFeature#onShieldBlock`.
- **Crouch-blocking** — starts/stops blocking with the offhand item based on crouch state (`onPlayerTick`), and attacking while crouch-blocking puts the shield on a short cooldown (`onPlayerAttack`) instead of the vanilla windup penalty.

### Enchantments

Shield-only enchantments are data-driven (`data/shieldsplus/enchantment/*.json`); `SPEnchantments` (`setup/SPEnchantments.java`) holds typed `ResourceKey<Enchantment>` constants for them: `recoil`, `reflection`, `reinforced`, `aegis`, `ablaze`, `lightweight`, `fast_recovery`, `celestial_guardian`.

Gameplay behavior that vanilla's data-driven enchantment effects can't express is implemented in `world/item/enchantment/*EnchantmentEffect.java` classes (each implementing `IBlockingEnchantmentEffect` where relevant) and dispatched from `SPFeature`:
- `onShieldBlock` calls `IBlockingEnchantmentEffect.onBlocked` for enchantments registered in `SPEnchantments.BLOCKING_EFFECTS` (currently Recoil, Reflection, Ablaze, Celestial Guardian), and applies `ReinforcedEnchantmentEffect`'s bonus blocked-damage directly.
- `onLivingDamage` applies `AegisEnchantmentEffect`'s damage reduction and tracks `CelestialGuardianEnchantmentEffect`'s saved-amount bookkeeping.
- `onLivingDeath` lets `CelestialGuardianEnchantmentEffect` cancel a killing blow and set health to 1.
- `LightweightEnchantmentEffect`'s movement-speed bonus is driven by InsaneLib's client-only `PlayerUseItemMovSpeedEvent` (adjusts the vanilla 0.2 use-item movement multiplier directly instead of an attribute modifier), registered in `ShieldsPlus`'s constructor guarded by `Dist.CLIENT`.

`SPSoundEvents` registers the `celestial_guardian` sound event used by that enchantment.

### Mixins

Mixins are in `mixin/` and patch vanilla behavior:
- `LivingEntityMixin` — removes shield windup, enforces the configurable blocking angle, cancels `releaseUsingItem` when crouching-to-block is active, broadcasts block particle event, customizes attacker knockback on block
- `PlayerMixin` — overrides minimum damage to hurt shield, reduces shield disable ticks (100 → 32 for combat-test style disabling)
- `LocalPlayerMixin`, `MinecraftMixin` — additional client-side patches
- `ItemMixin` — item-level patches

MixinExtras (`com.llamalad7.mixinextras`) is used extensively for `@ModifyExpressionValue`, `@WrapOperation`, and `@Definition`/`@Expression` targeting.

### Item Registration

`SPItems` registers seven `SPShieldItem` instances (wooden, stone, copper, iron, golden, diamond, netherite) via `DeferredRegister`. The copper shield is not craftable in survival by default — it's enabled by mods like Insane Survival Overhaul (formerly Iguana Tweaks Reborn). Each shield carries its stats (`damageBlocked`, `blockingTime`, `cooldown`) as data components defined in `SPDataComponents` (three `Float` components: `blocked_damage`, `blocking_time`, `cooldown`).

`SPShieldItem` extends vanilla `ShieldItem` and overrides:
- `getUseDuration` — drives the blocking time limit
- `finishUsingItem` / `onStopUsing` — applies cooldown proportional to time blocked
- `appendHoverText` — shows stats in tooltip when features are enabled
- `isValidRepairItem` — uses the per-shield `repairIngredient`

### Client Setup

`Client.java` registers the `blocking` item property function for all SP shields (needed for the blocking model variant), and inserts shields into the Combat creative tab around vanilla `Items.SHIELD` (vanilla shield is removed from the tab).

`ClientMaterials` is a record holding the two render `Material`s (no-pattern and pattern) for shield entity rendering.

`render/ShieldBlockEntityWithoutLevelRenderer` renders held/dropped shields (with banner pattern support), picking the correct per-shield `Material` via `SPShieldItem#getClientMaterial`.

### Custom Recipe

`SPShieldDecorationRecipe` (registered via `SPRecipeSerializers`) replicates vanilla shield-banner decoration logic but matches any shield from the mod instead of only `Items.SHIELD`.

### Events

`SPEventFactory` posts `BlockWithCrouchEvent` — a cancellable NeoForge event that other mods can listen to in order to prevent crouching-to-block.

### Resource Layout

- `src/main/resources/assets/shieldsplus/` — models, textures, sounds, lang
- `src/main/resources/data/shieldsplus/` — recipes, advancements, enchantments, item/enchantment tags
- `src/main/resources/data/minecraft/recipes/shield.json` — overrides vanilla shield recipe
- `src/generated/resources/` — output of data generators (do not edit manually)
- `src/main/templates/` — template files expanded by `generateModMetadata` Gradle task into `build/generated/sources/modMetadata/`
