# Changelog

* increase back torches from crafting

## Upcoming
* Enhanced the cooldown mechanic
  * The cooldown of a shield is now based on the time it has been lifted

## 1.11.9
* Definitely removed Shield Bash enchantment
* Fixed italian translation being outdated
* Fixed being able to block with a charged crossbow in hand

## 1.11.8
* Slightly increased shields cooldown

## 1.11.7
* You can now pick-block when block crouching
* Fixed crouch block event not working correctly

## 1.11.6
* Added a new BlockWithCrouchEvent to cancel blocking while crouching
* Blocking with a shield with right-click now slows down less (like when crouching)

## 1.11.5
* Fixed not being able to charge items (e.g. bows and crossbows) when sneaking with a shield on cooldown
* Only charged crossbows will now prevent crouch blocking. Item tag `shieldsplus:requires_two_hands` is now empty but still functional

## 1.11.4
* Fixed being able to crouch block with crossbow in hand
  * Added a new item tag `shieldsplus:requires_two_hands` that contains the items that will prevent crouch blocking

## 1.11.3
* Changed shield recipes to require a log in the middle instead of a wooden shield

## 1.11.2
* Blocking while crouching no longer slows down twice
  * With this, Lightweight only works when blocking with right-click
* Fixed [MC-147694](https://bugs.mojang.com/browse/MC-147694)

## 1.11.1
* You can attack, break and place blocks while block-crouching
* Rebalanced shields
  * More blocked damage and blocking time
* Fixed blocking sound not playing if the damage wasn't fully blocked

## 1.11.0
* You can now block when crouching
  * Only when the shield is in the off-hand

## 1.10.2
* Requires InsaneLib 1.15.0
* Minor rebalancements

## 1.10.1
* Fixed Celestial guardian triggering when not blocking

## 1.10.0
* Added Celestial Guardian treasure enchantment
* Perfect parry
  * The attacking entity (if melee) now gets "stunned"
  * Reduced window to activate to 1 tick (from 2)
* Added support for IguanaTweaks Reborn enchantments descriptions
* Lowered shields blocked damage

## 1.9.0
* Added Perfect Parry enchantment
  * Blocking at the same time as taking damage completely negates the damage
* Reinforced now increases base blocking by a percentage instead of +0.25
* Reflection no longer has a damage cap and reflected damage has been reduced to 8% (from 12.5%)
* Fast recovery now reduces the cooldown of shields (both when disabled with an axe and with natural cooldown) by 40%
* Ablaze
  * Fire is now applied with 1 tick delay to prevent mobs like zombies from setting the blocker on fire
  * Also setting a mob on fire should now account for the kill
  * Reduced time on fire from 3 to 2 seconds per level
* Reduced Lightweight speed when blocking
* Enchantments are now configurable
* Rebalanced shields
* Removed Shield Bash
  * Shields with the enchantment already on it will not lose it

## 1.8.0
* Shields now can be lifted only for a certain amount of time and will go into cooldown
* Shields stats are now defined in a file instead of hardcoded
  * blocked damage, lifted time and cooldown must now be defined in the `shield_definitions` folder
  * blocked damage can now be applied to other mod's shields too
* Adjusted Shield Bash
  * Decreased damage (~~6/9/12~~ -> 4/7/10 at I/II/III)
  * Increased upward force
  * Reduced forward force
* Vanilla shield is no longer affected by the mod

## 1.7.2
* Fixed Stone Shield requiring cobblestone and not using `stone_tool_materials` item tag

## 1.7.1
* Aegis and Reinforced enchantments are now disabled if Shields Block Fixed Damage Amount is disabled
* Sorted shields in the creative tab

## 1.7.0
* Added Aegis enchantment
  * Reduces damage taken when blocking. Incompatible with reinforced & reflection
* Rebalanced shields blocked damage (mostly lowered)
  * Blocked damage can now be customized via data packs by creating a `shield_definitions` folder in the mods namespace and adding a simple json with the name of the item  
    Example: `shieldsplus/shield_definitions/golden_shield.json`  
    ```json
      {
          "blocked_damage": 100
      }
    ```
    This makes Golden Shields block 100 damage
* Added an Iron shield that replaces the vanilla shield
* Removed Copper Shield
* Reinforced enchantment now increases blocked damage by 0.5 + (~~0.5~~ -> 0.25 per level)

## 1.6.7
* SPShieldItem.getBlockedDamage now accepts an ItemStack, @Nullable LivingEntity and Level

## 1.6.6
* Increased Recoil knockback per level (~~0.5~~ -> 0.65)

## 1.6.5
* Buffed lightweight speed bonus (~~+100%~~ -> +300%)

## 1.6.4
* Fixed disabling "Shields Block Fixed Damage Amount" not hiding the tooltip on shields

## 1.6.3
* Fixed hurt sound no longer played

## 1.6.2
* Added `blockingDamageOverride` in `SPShieldItem`
  * Can be used by mods to change the blocked damage of a SPShieldItem
* Now requires MC 1.20.1

## 1.6.1
* Fixed Netherite Shield recipe erroring

## 1.6.0
* MC 1.20

## 1.5.0
* MC 1.19.4

## 1.4.3
* Fixed recipes not unlocking
* Shield material now requires a LazyLoadedValue

## 1.4.2
* Fixed basically every feature of the config not working

## 1.4.1
* Added italian translation
* Fixed registering other mods shields not getting the texture correctly

## 1.4.0
* Updated to MC 1.19.3
* Slight rework of Shield Bash
  * Shield Bash max level is now 3 instead of 1 (it's not possible to find books with shield bash > 1)
  * Higher levels increase bash force, damage and knockback dealt

## 1.3.2
* Updated to InsaneLib 1.7.1
* Fixed Enchantments not having the correct Enchantment Category

## 1.3.1
* Updated to InsaneLib 1.7.0

## 1.3.0
* Updated to 1.19.1+
* Reduced the range at which Shield Bash hits