package com.insane96mcp.shieldsplus.item;

import com.insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import com.insane96mcp.shieldsplus.setup.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;

public class SPShieldItem extends ShieldItem {

    public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");
    private final double blockedDamage;

    public SPShieldItem(double blockedDamage, Properties p_43089_) {
        super(p_43089_);
        this.blockedDamage = blockedDamage;
    }

    public double getBlockedDamage() {
        return this.blockedDamage;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return ShieldBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        addDamageBlockedText(components, this.getBlockedDamage());
    }

    public static void addDamageBlockedText(List<Component> components, double blockedDamage) {
        components.add(new TranslatableComponent(Strings.Translatable.DAMAGE_BLOCKED, new DecimalFormat("#.#").format(blockedDamage)).withStyle(ChatFormatting.BLUE));
    }
}
