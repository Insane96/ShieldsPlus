# Changelog

## Upcoming
* Increased Shield Bash damage (~~5~~ -> 6)
* Reinforced Enchantment
  * No longer compatible with Reflection
  * Max level increased (~~4~~ -> 5)
  * Damage blocked increased (~~0.5 per level~~ -> 0.5 per level + 0.5)

## 1.2.4
* Fixed possible crash with Ablaze

## 1.2.3
* Increased projectile speed and knockback with Recoil
* Ablaze now sets blocked projectiles on fire

## 1.2.2
* Reduced shield bash bounding box
* Fixed Shield Bash working without the enchantment
* Fixed shield bash charging too fast
* Fixed Shield Bash being applied to non-Shield Items
* Player no longer gets Step Height when using shield bash

## Beta 1.2.1
* Shield Bash now works
  * Treasure enchantment
  * Block with the Shield for 1.5 seconds.  
    After that, tap crouch to bash and damage any entity in your path (Ablaze works too)  
    On bashing the ability is put on a 1.5 seconds cooldown
  * Incompatible with Recoil and Reflection
* Ablaze now sets projectiles on fire
* Reduced Ablaze time on fire (~~4~~ -> 3 seconds per level)

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