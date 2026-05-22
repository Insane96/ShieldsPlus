package insane96mcp.shieldsplus.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

public class ShieldBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

    public static ShieldBlockEntityWithoutLevelRenderer instance;

    private final EntityModelSet entityModelSet;
    private ShieldModel shieldModel;

    public ShieldBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher rd, EntityModelSet ems) {
        super(rd, ems);
        this.entityModelSet = ems;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
    }

    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        instance = new ShieldBlockEntityWithoutLevelRenderer(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels()
        );
        event.registerReloadListener(instance);
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        boolean hasBanner = itemStack.has(DataComponents.BASE_COLOR);
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);

        Material material = hasBanner ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
        if (itemStack.getItem() instanceof SPShieldItem spShieldItem) {
            material = spShieldItem.getClientMaterial(hasBanner);
        }

        VertexConsumer vertexconsumer = material.sprite().wrap(
            ItemRenderer.getFoilBufferDirect(bufferSource, this.shieldModel.renderType(material.atlasLocation()), true, itemStack.hasFoil())
        );
        this.shieldModel.handle().render(poseStack, vertexconsumer, packedLight, packedOverlay);

        if (hasBanner) {
            DyeColor baseColor = itemStack.getOrDefault(DataComponents.BASE_COLOR, DyeColor.WHITE);
            BannerPatternLayers patterns = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            BannerRenderer.renderPatterns(poseStack, bufferSource, packedLight, packedOverlay, this.shieldModel.plate(), material, false, baseColor, patterns, itemStack.hasFoil());
        } else {
            this.shieldModel.plate().render(poseStack, vertexconsumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }
}
