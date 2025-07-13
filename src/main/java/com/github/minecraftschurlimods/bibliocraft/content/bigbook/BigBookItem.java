package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
//import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.minecraftschurlimods.bibliocraft.util.StringUtil.isBlank;

public class BigBookItem extends Item {
    public boolean written;
    public BigBookItem(boolean isWritten) {
        //                .component(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, WrittenBigBookContent.DEFAULT)
        super(new Properties()
                        .stacksTo(1)
//                .component(DataComponents.MAX_STACK_SIZE, 1)
//                .component(BCDataComponents.BIG_BOOK_CONTENT, BigBookContent.DEFAULT)
        );
        written = isWritten;
    }
    @Override
    public boolean isFoil(@NotNull ItemStack item) {
        return written;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            ClientUtil.openBigBookScreen(stack, player, hand);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("title")) {
            String title = stack.getTag().getString("title");
            if (!isBlank(title)) return Component.literal(title);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag.contains("written") && tag.getBoolean("written") && tag.contains("author"))
            {
                String author = tag.getString("author");
                if (!isBlank(author))
                {
                    tooltipComponents.add(Component.translatable(Translations.VANILLA_BY_AUTHOR_KEY, author).withStyle(ChatFormatting.GRAY));
                }
                int generation = tag.contains("generation") ? tag.getInt("generation") : 0;
                tooltipComponents.add(Component.translatable("book.generation." + generation).withStyle(ChatFormatting.GRAY));
            }
        }
//        if (content != null && content.written()) {
//            String author = content.author();
//            if (!isBlank(author)) {
//                tooltipComponents.add(Component.translatable(Translations.VANILLA_BY_AUTHOR_KEY, author).withStyle(ChatFormatting.GRAY));
//            }
//            tooltipComponents.add(Component.translatable("book.generation." + content.generation()).withStyle(ChatFormatting.GRAY));
//        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
