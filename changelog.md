# Changelog

## Upcoming
* Ablaze now sets projectiles on fire

## Alpha 1.2.0
* Added 6 shields enchantments
  * Reinforced: increases damage blocked by 0.5 per level (max IV)
  * Recoil: Knockbacks entities and launches back projectiles that hit the shield (max II)
  * Reflection: Damages entities that hit the shield by 15% per level of the damage taken (max III)
  * Ablaze: Sets the entities that hit the shield on fire for 4 seconds per level (max II)
  * Lightweight: Makes the player move faster when blocking
  * Fast Recovery: Makes the Shield cooldown 0.6 seconds less (only works with `Combat Test shield disabling` enabled)
  * Shield Bash
* Shields can now be enchanted at the Enchanting Table

## 1.1.1
* Copper shield recipe no longer outputs a diamond shield

## 1.1.0
* Added Copper Shield
* Added recipe to apply banners to shields
* Added Combat Test shield disabling
* Shields can now be repaired, also vanilla shields now require Iron to be repaired
* Fixed (hopefully) vanilla shield recipe not being overridden

## 1.0.3
* Fixed server not launching

## 1.0.2
* Changed recipes to always require a wooden shield as base, instead of upgrading Wood>Stone>Iron>Diamond
* Internal changes to make registering shields easier

## 1.0.1
* Added "blocking damage" tooltip on Shields
* Raised mixin priority, should prevent IguanaTweaks from messing with the blocking delay (0.25 secs)

## 1.0.0
* First Release with shields and a few small features