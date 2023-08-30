package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.util.MCUtils;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.UUID;

public class ShieldLightweightEnchantment extends Enchantment {

	public static final double BONUS_SPEED = 3d;
	public static final UUID BONUS_SPEED_UUID = UUID.fromString("bf9ce34a-a825-4b22-a050-f6f752879332");

	public ShieldLightweightEnchantment() {
		super(Rarity.RARE, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 22;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 22;
	}

	public int getMaxLevel() {
		return 1;
	}

	public static void onTick(Player player) {
		if (player.isBlocking()) {
			int lightweight = player.getUseItem().getEnchantmentLevel(SPEnchantments.LIGHTWEIGHT.get());
			if (lightweight > 0)
				MCUtils.applyModifier(player, Attributes.MOVEMENT_SPEED, BONUS_SPEED_UUID, "Lightweight bonus speed", BONUS_SPEED * lightweight, AttributeModifier.Operation.MULTIPLY_BASE, false);
		}
		else {
			AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
			attribute.removeModifier(ShieldLightweightEnchantment.BONUS_SPEED_UUID);
		}
	}
}
