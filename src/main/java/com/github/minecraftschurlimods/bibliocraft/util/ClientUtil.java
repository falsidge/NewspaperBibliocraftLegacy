package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.BibliocraftConfig;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BigBookScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Calendar;

/**
 * Utility class holding various helper methods. Kept separate from {@link BCUtil} for classloading reasons.
 */
public final class ClientUtil {
    /**
     * Opens a {@link BigBookScreen} on the client.
     *
     * @param stack  The owning {@link ItemStack} of the screen.
     * @param player The owning {@link Player} of the screen.
     * @param hand   The {@link InteractionHand} in which the big book is held.
     */
    public static void openBigBookScreen(ItemStack stack, Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new BigBookScreen(stack, player, hand));
    }

    /**
     * Opens a {@link BigBookScreen} on the client.
     *
     * @param stack   The owning {@link ItemStack} of the screen.
     * @param player  The owning {@link Player} of the screen.
     * @param lectern The owning lectern's {@link BlockPos}.
     */
    public static void openBigBookScreen(ItemStack stack, Player player, BlockPos lectern) {
        Minecraft.getInstance().setScreen(new BigBookScreen(stack, player, lectern));
    }




    /**
     * Renders an {@link ItemStack} in the {@link ItemDisplayContext#FIXED} pose.
     *
     * @param item    The {@link ItemStack} to render.
     * @param stack   The {@link PoseStack} to use.
     * @param buffer  The {@link MultiBufferSource} to use.
     * @param light   The light value to use.
     * @param overlay The overlay value to use.
     */
    public static void renderFixedItem(ItemStack item, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        renderItem(item, stack, buffer, light, overlay, ItemDisplayContext.FIXED);
    }

    /**
     * Renders an {@link ItemStack} in the {@link ItemDisplayContext#GUI} pose.
     *
     * @param item    The {@link ItemStack} to render.
     * @param stack   The {@link PoseStack} to use.
     * @param buffer  The {@link MultiBufferSource} to use.
     * @param light   The light value to use.
     * @param overlay The overlay value to use.
     */
    public static void renderGuiItem(ItemStack item, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        renderItem(item, stack, buffer, light, overlay, ItemDisplayContext.GUI);
    }

    /**
     * Renders an {@link ItemStack} for use in a BER or GUI.
     *
     * @param item    The {@link ItemStack} to render.
     * @param stack   The {@link PoseStack} to use.
     * @param buffer  The {@link MultiBufferSource} to use.
     * @param light   The light value to use.
     * @param overlay The overlay value to use.
     * @param context The {@link ItemDisplayContext} to use.
     */
    public static void renderItem(ItemStack item, PoseStack stack, MultiBufferSource buffer, int light, int overlay, ItemDisplayContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer renderer = minecraft.getItemRenderer();
        renderer.render(item, context, context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND, stack, buffer, light, overlay, renderer.getModel(item, minecraft.level, null, 0));
    }

//    /**
//     * Renders the given {@link BakedModel} in the world.
//     *
//     * @param model     The {@link BakedModel} to render.
//     * @param stack     The {@link PoseStack} to use.
//     * @param buffer    The {@link MultiBufferSource} to use.
//     * @param level     The {@link Level} to render the model in.
//     * @param pos       The {@link BlockPos} to render the model at.
//     * @param state     The {@link BlockState} to render the model for.
//     * @param random    The {@link RandomSource} to use for random models.
//     * @param modelData The {@link ModelData} to use.
//     */
//    public static void renderBakedModel(BakedModel model, PoseStack stack, MultiBufferSource buffer, Level level, BlockPos pos, BlockState state, RandomSource random, ModelData modelData) {
//        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
//        int color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
//        float red = (float) (color >> 16 & 255) / 255f;
//        float green = (float) (color >> 8 & 255) / 255f;
//        float blue = (float) (color & 255) / 255f;
//        int light = LevelRenderer.getLightColor(level, pos);
//        for (RenderType type : model.getRenderTypes(state, random, modelData)) {
//            renderer.renderModel(stack.last(), buffer.getBuffer(RenderTypeHelper.getEntityRenderType(type, false)), state, model, red, green, blue, light, OverlayTexture.NO_OVERLAY, modelData, type);
//        }
//    }

    /**
     * @return Whether pride-themed content should be displayed.
     */
    public static boolean isPride() {
        return BibliocraftConfig.ENABLE_PRIDE.get() && (BibliocraftConfig.ENABLE_PRIDE_ALWAYS.get() || Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE);
    }


}
