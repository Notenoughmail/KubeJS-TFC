package com.notenoughmail.kubejs_tfc.util.implementation.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.latvian.mods.kubejs.core.PlayerKJS;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeCraftingGrid;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.KubeJSCraftingRecipe;
import net.dries007.tfc.common.recipes.AdvancedShapedRecipe;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class KubeAdvancedShapedRecipe extends AdvancedShapedRecipe implements KubeJSCraftingRecipe {

    private final String stage;
    private final List<IngredientAction> actions;
    @Nullable
    private final ModifyRecipeResultCallback resultModification;

    private final ItemStackProvider provider;
    private final int inputSlot;

    public KubeAdvancedShapedRecipe(
            ResourceLocation id,
            String group,
            int width,
            int height,
            NonNullList<Ingredient> recipeItems,
            ItemStackProvider result,
            int inputSlot,
            String stage,
            List<IngredientAction> actions,
            @Nullable ModifyRecipeResultCallback resultModification
    ) {
        super(id, group, width, height, recipeItems, result, inputSlot);
        this.stage = stage;
        this.actions = actions;
        this.resultModification = resultModification;
        this.provider = result;
        this.inputSlot = inputSlot;
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
        if (!kjs$getStage().isEmpty()) {
            // final Player player = KubeJSCraftingRecipeAccessor.kubejs_tfc$getPlayer(((CraftingContainerKJS)inventory).kjs$getMenu());
            if (player == null || !((PlayerKJS) player).kjs$getStages().has(kjs$getStage())) {
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
        return KubeJSTFCRecipeSerializers.SHAPED.get();
    }

    public static class Serializer extends RecipeSerializerImpl<KubeAdvancedShapedRecipe> {

        @Override
        public KubeAdvancedShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
            final String group = GsonHelper.getAsString(json, "group", "");
            final Map<String, Ingredient> keys = RecipeHelpers.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            final String[] pattern = RecipeHelpers.shrink(RecipeHelpers.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            final int width = pattern[0].length(), height = pattern.length;
            final NonNullList<Ingredient> recipeItems = RecipeHelpers.dissolvePattern(pattern, keys, width, height);
            final ItemStackProvider provider = ItemStackProvider.fromJson(JsonHelpers.getAsJsonObject(json, "result"));
            final int inputRow = JsonHelpers.getAsInt(json, "input_row"), inputCol = JsonHelpers.getAsInt(json, "input_column");
            if (inputRow < 0 || inputRow >= width) {
                throw new JsonParseException("'input_row' must be in the range [0, 'width']");
            }
            if (inputCol < 0 || inputCol >= height) {
                throw new JsonParseException("'input_column' must be in the range [0, 'height']");
            }
            final int inputSlot = RecipeHelpers.dissolveRowColumn(inputRow, inputCol, width);

            final String stage = GsonHelper.getAsString(json, "kubejs:stage", "");
            final List<IngredientAction> actions = IngredientAction.parseList(json.get("kubejs:actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (GsonHelper.getAsBoolean(json, "kubejs:modify_result", false)) {
                modifyResult = RecipesEventJS.MODIFY_RESULT_CALLBACKS.get(id);
            }

            return new KubeAdvancedShapedRecipe(
                    id, group, width, height, recipeItems, provider, inputSlot, stage, actions, modifyResult
            );
        }

        @Override
        public KubeAdvancedShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            final int width = buffer.readVarInt(), height = buffer.readVarInt();
            final String group = buffer.readUtf();
            final NonNullList<Ingredient> recipeItems = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for (int i = 0 ; i < recipeItems.size() ; i++) {
                recipeItems.set(i, Ingredient.fromNetwork(buffer));
            }
            final ItemStackProvider provider = ItemStackProvider.fromNetwork(buffer);
            final int inputSlot = buffer.readVarInt();

            final int flags = buffer.readByte();
            final List<IngredientAction> actions = (flags & 1) != 0 ? IngredientAction.readList(buffer) : List.of();
            final String stage = (flags & 2) != 0 ? buffer.readUtf() : "";

            return new KubeAdvancedShapedRecipe(
                    id, group, width, height, recipeItems, provider, inputSlot, stage, actions, null
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, KubeAdvancedShapedRecipe recipe) {
            buffer.writeVarInt(recipe.getWidth());
            buffer.writeVarInt(recipe.getHeight());
            buffer.writeUtf(recipe.getGroup());
            for (Ingredient i : recipe.getIngredients()) {
                i.toNetwork(buffer);
            }
            recipe.provider.toNetwork(buffer);
            buffer.writeVarInt(recipe.inputSlot);

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
