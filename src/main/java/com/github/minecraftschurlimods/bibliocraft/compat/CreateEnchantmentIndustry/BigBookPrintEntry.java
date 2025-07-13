package com.github.minecraftschurlimods.bibliocraft.compat.CreateEnchantmentIndustry;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.WrittenBigBookContent;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.material.Fluid;
import plus.dragons.createenchantmentindustry.EnchantmentIndustry;
import plus.dragons.createenchantmentindustry.content.contraptions.enchanting.printer.PrintEntry;
import plus.dragons.createenchantmentindustry.content.contraptions.enchanting.printer.Printing;
import plus.dragons.createenchantmentindustry.entry.CeiFluids;
import plus.dragons.createenchantmentindustry.foundation.config.CeiConfigs;

import java.util.List;

import static plus.dragons.createenchantmentindustry.EnchantmentIndustry.LANG;

//
public class BigBookPrintEntry  implements PrintEntry {
    @Override
    public ResourceLocation id() {
        return BCUtil.bcLoc("written_big_book");
    }

    @Override
    public boolean match(ItemStack toPrint) {
        return toPrint.is(BCItems.WRITTEN_BIG_BOOK.get());
    }

    @Override
    public boolean valid(ItemStack target, ItemStack tested) {
        return tested.is(BCItems.BIG_BOOK.get());
    }

    @Override
    public int requiredInkAmount(ItemStack target) {
        var page = WrittenBigBookContent.decode(target.getTag()).pages().size();
        return page * CeiConfigs.SERVER.copyWrittenBookCostPerPage.get();
    }

    @Override
    public Fluid requiredInkType(ItemStack target) {
        return CeiFluids.INK.get();
    }

    @Override
    public ItemStack print(ItemStack target, ItemStack material) {
        var ret = target.copy();
        if (!CeiConfigs.SERVER.copyingWrittenBookAlwaysGetOriginalVersion.get()) {
            var tag = ret.getOrCreateTag();
//            int generation = tag.getInt("generation");
//            if (generation <= 1)
//                tag.putInt("generation", generation + 1);
            ret.setTag(WrittenBigBookContent.encode(WrittenBigBookContent.decode(tag).tryCraftCopy()));

        }
        return ret;
    }

    @Override
    public boolean isTooExpensive(ItemStack target, int limit) {
        var page = WrittenBigBookContent.decode(target.getTag()).pages().size();
        return page * CeiConfigs.SERVER.copyWrittenBookCostPerPage.get() > limit;
    }

    @Override
    public void addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, ItemStack target) {
//        var page = WrittenBookItem.getPageCount(target);
        var page = WrittenBigBookContent.decode(target.getTag()).pages().size();

        var b = LANG.builder()
                .add(LANG.itemName(target)
                        .style(ChatFormatting.BLUE))
                .text(ChatFormatting.GRAY, " / ")
                .add(LANG.number(page)
                        .text(" ")
                        .add(page == 1 ? LANG.translate("generic.unit.page") : LANG.translate("generic.unit.pages"))
                        .style(ChatFormatting.DARK_GRAY));
        b.forGoggles(tooltip, 1);
        if (Printing.isTooExpensive(this, target, CeiConfigs.SERVER.copierTankCapacity.get()))
            tooltip.add(Component.literal("     ").append(LANG.translate(
                    "gui.goggles.too_expensive").component()
            ).withStyle(ChatFormatting.RED));
        else
            tooltip.add(Component.literal("     ").append(LANG.translate(
                    "gui.goggles.ink_consumption",
                    String.valueOf(CeiConfigs.SERVER.copyWrittenBookCostPerPage.get() * page)).component()
            ).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public MutableComponent getDisplaySourceContent(ItemStack target) {
        var page = WrittenBigBookContent.decode(target.getTag()).pages().size();

        return LANG.builder()
                .add(LANG.itemName(target))
                .text( " / ")
                .add(LANG.number(page)
                        .text(" ")
                        .add(page == 1 ? LANG.translate("generic.unit.page") : LANG.translate("generic.unit.pages"))).component();
    }
}