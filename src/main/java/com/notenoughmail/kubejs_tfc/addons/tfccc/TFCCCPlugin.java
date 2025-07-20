package com.notenoughmail.kubejs_tfc.addons.tfccc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ljuangbminecraft.tfcchannelcasting.TFCChannelCasting;
import com.ljuangbminecraft.tfcchannelcasting.common.recipes.outputs.ConditionalItemStackModifier;
import com.ljuangbminecraft.tfcchannelcasting.common.recipes.outputs.DateRangeModifyCondition;
import com.ljuangbminecraft.tfcchannelcasting.common.recipes.outputs.HasTraitModifyCondition;
import com.ljuangbminecraft.tfcchannelcasting.common.recipes.outputs.SetFoodDataItemStackModifier;
import com.mojang.serialization.JsonOps;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.nbt.NbtOps;

public class TFCCCPlugin extends KubeJSPlugin {

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.deny(TFCCCPlugin.class);
        filter.deny(TFCChannelCasting.class);
        filter.allow("com.ljuangbminecraft.tfcchannelcasting");
        filter.deny("com.ljuangbminecraft.tfcchannelcasting.mixin");
    }

    @Override
    public void init() {
        KubeJSTFC.registerISMConverter(ConditionalItemStackModifier.class, (conditional, json) -> {
            if (conditional.nestedModifiers().length != 0) {
                final JsonArray arr = new JsonArray();
                for (ItemStackModifier mod : conditional.nestedModifiers()) {
                    arr.add(KubeJSTFC.convertISM(mod));
                }
                json.add("modifiers", arr);
            }
            if (conditional.elseNestedModifiers().length != 0) {
                final JsonArray arr = new JsonArray();
                for (ItemStackModifier mod : conditional.elseNestedModifiers()) {
                    arr.add(KubeJSTFC.convertISM(mod));
                }
                json.add("else_modifiers", arr);
            }
            final JsonObject condition = new JsonObject();
            if (conditional.condition() instanceof DateRangeModifyCondition date) {
                json.addProperty("min_day", date.minCalendarDayOfMonth());
                json.addProperty("max_day", date.maxCalendarDayOfMonth());
                json.addProperty("min_month", date.minCalendarMonthOfYear().ordinal() + 1);
                json.addProperty("max_month", date.maxCalendarMonthOfYear().ordinal() + 1);
            } else if (conditional.condition() instanceof HasTraitModifyCondition trait) {
                json.addProperty("trait", FoodTrait.getId(trait.trait()).toString());
            } else {
                throw new IllegalArgumentException("Unknown conditional type!");
            }
            json.add("condition", condition);
        });
        KubeJSTFC.registerISMConverter(SetFoodDataItemStackModifier.class, (food, json) -> {
            final JsonElement foodJson = NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, food.foodData().write());
            if (foodJson instanceof JsonObject obj) {
                obj.entrySet().forEach(entry -> json.add(entry.getKey(), entry.getValue()));
            } else {
                throw new IllegalStateException("Compound Nbt Tag did not convert into a JsonObject?");
            }
        });
    }
}
