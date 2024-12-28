package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.misc.LostPage;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public interface IBeneathDataExtension extends IDataConstructor {

    @Info(value = "Defines a new nether fertilizer", params = {
            @Param(name = "ingredient", value = "The ingredient for the nether fertilizer"),
            @Param(name = "death", value = "The death value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "destruction", value = "The destruction value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "decay", value = "The decay value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "sorrow", value = "The sorrow value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "flame", value = "The flame value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "name", value = "The name of the nether fertilizer")
    })
    default void beneathNetherFertilizer(Ingredient ingredient, @Nullable Float death, @Nullable Float destruction, @Nullable Float decay, @Nullable Float sorrow, @Nullable Float flame, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        ResourceUtils.handleNetherFertilizers(json, death, destruction, decay, sorrow, flame);
        addJson(ResourceUtils.dataID(name, Beneath.MOD_ID, "nether_fertilizers"), json);
    }

    @Info(value = "Defines a new nether fertilizer", params = {
            @Param(name = "ingredient", value = "The ingredient for the nether fertilizer"),
            @Param(name = "death", value = "The death value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "destruction", value = "The destruction value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "decay", value = "The decay value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "sorrow", value = "The sorrow value of the fertilizer, may be null, defaults to 0"),
            @Param(name = "flame", value = "The flame value of the fertilizer, may be null, defaults to 0")
    })
    default void beneathNetherFertilizer(Ingredient ingredient, @Nullable Float death, @Nullable Float destruction, @Nullable Float decay, @Nullable Float sorrow, @Nullable Float flame) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        ResourceUtils.handleNetherFertilizers(json, death, destruction, decay, sorrow, flame);
        addJson(ResourceUtils.dataIDFromObject(ingredient, Beneath.MOD_ID, "nether_fertilizers"), json);
    }

    @Info(value = "Defines a new lost page", params = {
            @Param(name = "cost", value = "The item required for this ritual"),
            @Param(name = "reward", value = "The item reward for this ritual"),
            @Param(name = "costs", value = "The number of `cost` items that are required, will be randomly selected from when the page is initialized"),
            @Param(name = "rewards", value = "The number of `reward` items that will be resultant from a ritual, will be selected from when the page is initialized"),
            @Param(name = "punishments", value = "Possible punishments for this ritual, will be randomly selected from when the page is initialized"),
            @Param(name = "langKey", value = "A custom lang key to use for the ingredient in the lost page screen")
    })
    default void beneathLostPage(Ingredient cost, Item reward, int[] costs, int[] rewards, LostPage.Punishment[] punishments, @Nullable String langKey) {
        addJson(
                ResourceUtils.dataIDFromObject(cost, Beneath.MOD_ID, "lost_pages"),
                ResourceUtils.lostPage(cost, reward, costs, rewards, punishments, langKey)
        );
    }

    @Info(value = "Defines a new lost page", params = {
            @Param(name = "cost", value = "The item required for this ritual"),
            @Param(name = "reward", value = "The item reward for this ritual"),
            @Param(name = "costs", value = "The number of `cost` items that are required, will be randomly selected from when the page is initialized"),
            @Param(name = "rewards", value = "The number of `reward` items that will be resultant from a ritual, will be selected from when the page is initialized"),
            @Param(name = "punishments", value = "Possible punishments for this ritual, will be randomly selected from when the page is initialized"),
            @Param(name = "langKey", value = "A custom lang key to use for the ingredient in the lost page screen"),
            @Param(name = "name", value = "The name of the lost page definition")
    })
    default void beneathLostPage(Ingredient cost, Item reward, int[] costs, int[] rewards, LostPage.Punishment[] punishments, @Nullable String langKey, ResourceLocation name) {
        addJson(
                ResourceUtils.dataID(name, Beneath.MOD_ID, "lost_pages"),
                ResourceUtils.lostPage(cost, reward, costs, rewards, punishments, langKey)
        );
    }
}
