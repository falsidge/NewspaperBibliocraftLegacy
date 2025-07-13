package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookCloningRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookCloningWrapper;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;

@JeiPlugin
public final class BibliocraftJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = BCUtil.bcLoc("jei_plugin");

    public static final RecipeType<BigBookCloningRecipe> BOOK_CLONE = RecipeType.create(Bibliocraft.MOD_ID, "bigbookclone", BigBookCloningRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IExtendableRecipeCategory<CraftingRecipe, ICraftingCategoryExtension> craftingCategory = registration.getCraftingCategory();
        craftingCategory.addCategoryExtension(BigBookCloningRecipe.class, BigBookCloningWrapper::new);
    }
}
