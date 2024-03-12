package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.util.MCUtils;
import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.UUID;

public class LightweightEnchantment extends Enchantment implements IEnchantmentTooltip {
	public static final UUID BONUS_SPEED_UUID = UUID.fromString("bf9ce34a-a825-4b22-a050-f6f752879332");

	public LightweightEnchantment() {
		super(Rarity.RARE, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 22;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 22;
	}

	public static void onTick(Player player) {
		if (player.isBlocking()) {
			int lvl = player.getUseItem().getEnchantmentLevel(SPEnchantments.LIGHTWEIGHT.get());
			if (lvl > 0)
				MCUtils.applyModifier(player, Attributes.MOVEMENT_SPEED, BONUS_SPEED_UUID, "Lightweight bonus speed", BaseFeature.enchantmentsLightweightBonusSpeed * lvl, AttributeModifier.Operation.MULTIPLY_BASE, false);
		}
		else {
			AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
			//noinspection DataFlowIssue
			attribute.removeModifier(LightweightEnchantment.BONUS_SPEED_UUID);
		}
	}

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.DARK_PURPLE);
	}
}
