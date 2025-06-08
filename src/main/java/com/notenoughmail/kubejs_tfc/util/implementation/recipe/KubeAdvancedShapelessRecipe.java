package com.notenoughmail.kubejs_tfc.util.implementation.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.KubeJSCraftingRecipeAccessor;
import dev.latvian.mods.kubejs.core.CraftingContainerKJS;
import dev.latvian.mods.kubejs.core.PlayerKJS;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeCraftingGrid;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.KubeJSCraftingRecipe;
import net.dries007.tfc.common.recipes.AdvancedShapelessRecipe;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KubeAdvancedShapelessRecipe extends AdvancedShapelessRecipe implements KubeJSCraftingRecipe {

    private final String stage;
    private final List<IngredientAction> actions;
    @Nullable
    private final ModifyRecipeResultCallback resultModification;

    public KubeAdvancedShapelessRecipe(
            ResourceLocation id,
            String group,
            ItemStackProvider result,
            NonNullList<Ingredient> ingredients,
            @Nullable Ingredient primaryIngredient,
            String stage,
            List<IngredientAction> actions,
            @Nullable ModifyRecipeResultCallback resultModification
    ) {
        super(id, group, result, ingredients, primaryIngredient);
        this.stage = stage;
        this.actions = actions;
        this.resultModification = resultModification;
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
        if (!stage.isEmpty()) {
            final Player player = KubeJSCraftingRecipeAccessor.kubejs_tfc$getPlayer(((CraftingContainerKJS) inventory).kjs$getMenu());
            if (player == null || !((PlayerKJS) player).kjs$getStages().has(stage)) {
                return ItemStack.EMPTY;
            }
        }

        final ItemStack result = super.assemble(inventory, registryAccess);

        if (resultModification != null) {
            return resultModification.modify(new ModifyRecipeCraftingGrid(inventory), result);
        } else {
            return result;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
        return kjs$getRemainingItems(pContainer);
    }

    @Override
    public List<IngredientAction> kjs$getIngredientActions() {
        return actions;
    }

    @Override
    @Nullable
    public ModifyRecipeResultCallback kjs$getModifyResult() {
        return resultModification;
    }

    @Override
    public String kjs$getStage() {
        return stage;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return KubeJSTFCRecipeSerializers.SHAPELESS.get();
    }

    public static class Serializer extends RecipeSerializerImpl<KubeAdvancedShapelessRecipe> {

        @Override
        public KubeAdvancedShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
            final String group = GsonHelper.getAsString(json, "group", "");
            final NonNullList<Ingredient> ingredients = RecipeHelpers.itemsFromJson(JsonHelpers.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty() || ingredients.size() > 9) {
                throw new JsonParseException("'ingredients' size should be 1 to 9 ingredients long, it was %s long".formatted(ingredients.size()));
            }
            final ItemStackProvider provider = ItemStackProvider.fromJson(JsonHelpers.getAsJsonObject(json, "result"));
            final Ingredient primaryIngredient = json.has("primary_ingredient") ? Ingredient.fromJson(json.get("primary_ingredient")) : null;

            final String stage = GsonHelper.getAsString(json, "kubejs:stage", "");
            final List<IngredientAction> actions = IngredientAction.parseList(json.get("kubejs:actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs:modify_result")) {
                modifyResult = RecipesEventJS.MODIFY_RESULT_CALLBACKS.get(id);
            }

            return new KubeAdvancedShapelessRecipe(
                    id, group, provider, ingredients, primaryIngredient, stage, actions, modifyResult
            );
        }

        @Override
        public KubeAdvancedShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            final String group = buffer.readUtf();
            final int size = buffer.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0 ; i < size ; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }
            final ItemStackProvider provider = ItemStackProvider.fromNetwork(buffer);
            final Ingredient primary = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);

            int flags = buffer.readByte();
            final List<IngredientAction> actions = (flags & 1) != 0 ? IngredientAction.readList(buffer) : List.of();
            final String stage = (flags & 2) != 0 ? buffer.readUtf() : "";
            return new KubeAdvancedShapelessRecipe(
                    id, group, provider, ingredients, primary, stage, actions, null
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, KubeAdvancedShapelessRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());
            for (Ingredient i : recipe.getIngredients()) {
                i.toNetwork(buffer);
            }
            recipe.result.toNetwork(buffer);
            Helpers.encodeNullable(recipe.primaryIngredient, buffer, Ingredient::toNetwork);

            int flags = 0;
            if (!recipe.actions.isEmpty()) {
                flags |= 1;
            }
            if (!recipe.stage.isEmpty()) {
                flags |= 2;
            }
            buffer.writeByte(flags);
            if (!recipe.actions.isEmpty()) {
                IngredientAction.writeList(buffer, recipe.actions);
            }
            if (!recipe.stage.isEmpty()) {
                buffer.writeUtf(recipe.stage);
            }
        }
    }
}
