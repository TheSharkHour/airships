package com.shark.airships.events;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Listener Event for recipe initialization</p>
 */
@SuppressWarnings("unused")
public class RecipeListener {
    @EventListener
    public void registerRecipes(@NotNull RecipeRegisterEvent event) {
        Identifier type = event.recipeId;
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.ITEM_AIRSHIP),
                    "121", "343", "151",
                    '1', Item.STRING,
                    '2', ItemListener.ITEM_BALLOON,
                    '3', ItemListener.ITEM_ENGINE,
                    '4', Block.DISPENSER,
                    '5', Item.BOAT
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.ITEM_ENGINE),
                    "111", "121", "111",
                    '1', Item.IRON_INGOT,
                    '2', Block.PISTON
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.ITEM_BALLOON),
                    "111", "121", "111",
                    '1', Item.LEATHER,
                    '2', Block.WOOL
            );
        }
    }
}
