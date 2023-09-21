# Changelog

## 1.7.0
* Added Aegis enchantment
  * Reduces damage taken when blocking. Incompatible with reinforced & reflection
* Rebalanced shields blocked damage (mostly lowered)
  * Blocked damage can now be customized via data packs by creating a shield_definitions folder in the mods namespace and adding a simple json with the name of the item  
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