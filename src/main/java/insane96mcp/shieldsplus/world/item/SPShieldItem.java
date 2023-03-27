package insane96mcp.shieldsplus.world.item;

import insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.setup.Strings;
import insane96mcp.shieldsplus.setup.client.ClientMaterials;
import insane96mcp.shieldsplus.world.item.enchantment.ShieldReflectionEnchantment;
import insane96mcp.shieldsplus.world.item.enchantment.ShieldReinforcedEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;

public class SPShieldItem extends ShieldItem {

    public static final EnchantmentCategory SHIELD = EnchantmentCategory.create("shield", s -> s instanceof ShieldItem);

    public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");
    public final SPShieldMaterial material;

    public ClientMaterials clientMaterials;

    public SPShieldItem(SPShieldMaterial material, Properties p_43089_) {
        super(p_43089_);
        this.material = material;
    }

    public double getBlockedDamage() {
        return this.material.damageBlocked;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ShieldBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return this.material.enchantmentValue;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        addDamageBlockedText(itemStack, components, this.getBlockedDamage());
    }

    public static void addDamageBlockedText(ItemStack itemStack, List<Component> components, double blockedDamage) {
        int reinforced = itemStack.getEnchantmentLevel(SPEnchantments.REINFORCED.get());
        float finalBlockedDamage = (float) (blockedDamage + ShieldReinforcedEnchantment.getDamageBlocked(reinforced));
        int reflection = itemStack.getEnchantmentLevel(SPEnchantments.REFLECTION.get());
        float reflectedDamage = ShieldReflectionEnchantment.getReflectedDamage(reflection);
        components.add(Component.translatable(Strings.Translatable.DAMAGE_BLOCKED, new DecimalFormat("#.#").format(finalBlockedDamage)).withStyle(ChatFormatting.BLUE));
        /*if (reinforced > 0) {
            components.add(Component.translatable(Strings.Translatable.REINFORCED_BONUS, new DecimalFormat("#.#").format(ShieldReinforcedEnchantment.getDamageBlocked(reinforced))).withStyle(ChatFormatting.DARK_GRAY));
        }*/
        //Add here more blocking damage modifiers

        if (reflection > 0) {
            components.add(Component.translatable(Strings.Translatable.DAMAGE_REFLECTED, new DecimalFormat("#.#").format(reflectedDamage * 100)).withStyle(ChatFormatting.BLUE));
            components.add(Component.translatable(Strings.Translatable.CAPPED_DAMAGE_REFLECTED, new DecimalFormat("#.#").format(ShieldReflectionEnchantment.getCappedReflectedDamage(reflection))).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack repaired, @NotNull ItemStack repairingMaterial) {
        if (this.material.repairItem != null)
            return repairingMaterial.is(this.material.repairItem.get());
        return repairingMaterial.is(this.material.repairTag);
    }

    public void tryCacheClientMaterials() {
        if (this.clientMaterials == null) {
            this.clientMaterials = new ClientMaterials(new Material(Sheets.SHIELD_SHEET, new ResourceLocation(ForgeRegistries.ITEMS.getKey(this).getNamespace(), "entity/shield/%s_shield_nopattern".formatted(this.material.materialName))), new Material(Sheets.SHIELD_SHEET, new ResourceLocation(ForgeRegistries.ITEMS.getKey(this).getNamespace(), "entity/shield/%s_shield".formatted(this.material.materialName))));
        }
    }

    public Material getClientMaterial(boolean hasBanner) {
        tryCacheClientMaterials();
        return hasBanner ? this.clientMaterials.patternMaterial() : this.clientMaterials.noPatternMaterial();
    }
}
