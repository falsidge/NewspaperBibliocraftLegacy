package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.compat.CreateEnchantmentIndustry.CreateEnchantmentIndustryCompat;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.WrittenBigBookContent;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import com.github.minecraftschurlimods.bibliocraft.net.Payload;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.LecternUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.OpenBookInLecternPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;

import static com.github.minecraftschurlimods.bibliocraft.net.Payload.CHANNEL;

public final class EventHandler {
    @ApiStatus.Internal
    public static void init(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, BibliocraftConfig.SPEC);
        IEventBus modBus = context.getModEventBus();
        BCRegistries.init(modBus);
//        modBus.addListener(EventPriority.LOWEST, EventHandler::constructMod);
//        modBus.addListener(EventPriority.LOWEST, EventHandler::commonSetup);
//        modBus.addListener(EventHandler::registerPayloadHandlers);
        Payload.registerMesssages();
        MinecraftForge.EVENT_BUS.addListener(EventHandler::rightClickBlock);
        if (ModList.get().isLoaded("create_enchantment_industry")) {
            CreateEnchantmentIndustryCompat.load(MinecraftForge.EVENT_BUS);
        }
    }

//    private static void constructMod(FMLConstructModEvent event) {
////        ((BibliocraftWoodTypeRegistryImpl) BibliocraftApi.getWoodTypeRegistry()).register();
//        BCRegistries.init(Objects.requireNonNull(ModList.get().getModContainerById(BibliocraftApi.MOD_ID).orElseThrow().getEventBus()));
//    }




    @SuppressWarnings("DataFlowIssue")
    private static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return;
        ItemStack book = lectern.getBook();
        if (book.isEmpty()) {
            // this workaround is needed to set the page count correctly
            // if it can be added to vanilla/neo somehow, this can be scrapped
            ItemStack stack = player.getItemInHand(event.getHand());

            if (!stack.is(ItemTags.LECTERN_BOOKS)) return;
            if (!stack.hasTag()) return;
            lectern.setBook(stack);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            LecternBlock.resetBookState(player, level, pos, state, true);
            level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1f, 1f);
            WrittenBigBookContent content;
            if ((content = WrittenBigBookContent.decode(stack.getTag())) != null) {
                lectern.pageCount = content.pages().size();
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        } else if (book.hasTag()) {
            if (player.isSecondaryUseActive()) {
                LecternUtil.takeLecternBook(player, level, pos);
            } else if (!level.isClientSide() && player instanceof ServerPlayer sp) {
                CHANNEL.send(PacketDistributor.PLAYER.with(()->sp), new OpenBookInLecternPacket(pos, book));
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
