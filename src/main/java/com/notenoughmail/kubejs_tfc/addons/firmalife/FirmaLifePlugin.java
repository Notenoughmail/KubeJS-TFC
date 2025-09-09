package com.notenoughmail.kubejs_tfc.addons.firmalife;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.FLEvents;
import com.eerussianguy.firmalife.common.FLForgeEvents;
import com.eerussianguy.firmalife.common.FLHelpers;
import com.eerussianguy.firmalife.common.blocks.FLBlocks;
import com.eerussianguy.firmalife.common.blocks.FLFluids;
import com.eerussianguy.firmalife.common.items.FLItems;
import com.eerussianguy.firmalife.common.network.FLPackets;
import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import com.eerussianguy.firmalife.common.util.FLMetal;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.addons.firmalife.block.CheeseWheelBlockBuilder;
import com.notenoughmail.kubejs_tfc.addons.firmalife.item.WateringCanItemBuilder;
import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema.*;
import com.notenoughmail.kubejs_tfc.recipe.schema.BasicSchema;
import com.notenoughmail.kubejs_tfc.recipe.schema.SoupPotSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryMetal;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.dries007.tfc.util.Metal;
import org.jetbrains.annotations.Nullable;

public class FirmaLifePlugin extends KubeJSPlugin {

    @Override
    public void init() {
        RegistryInfo.ITEM.addType("firmalife:watering_can", WateringCanItemBuilder.class, WateringCanItemBuilder::new);

        RegistryInfo.BLOCK.addType("firmalife:cheese_wheel", CheeseWheelBlockBuilder.class, CheeseWheelBlockBuilder::new);

        KubeJSTFC.registerMetalListener(builder -> {
            for (FLMetal metal : FLMetal.values()) {
                builder.put(metal.getSerializedName(), new NamedRegistryMetal(
                        metal,
                        FirmaLife.MOD_ID,
                        FLBlocks.METALS.get(metal)::get,
                        type -> {
                            final FLMetal.ItemType item = switch (type) {
                                case INGOT -> FLMetal.ItemType.INGOT;
                                case DOUBLE_INGOT -> FLMetal.ItemType.DOUBLE_INGOT;
                                case SHEET -> FLMetal.ItemType.SHEET;
                                case DOUBLE_SHEET -> FLMetal.ItemType.DOUBLE_SHEET;
                                case ROD -> FLMetal.ItemType.ROD;
                                default -> null;
                            };
                            if (item == null) return null;
                            return FLItems.METAL_ITEMS.get(metal).get(item);
                        },
                        FLFluids.METALS.get(metal).source()::get
                ));
            }
        });
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow("com.eerussianguy.firmalife");
        filter.deny(FirmaLife.class);
        filter.deny("com.eerussianguy.firmalife.mixin");
        filter.deny(FLPackets.class);
        filter.deny(FLEvents.class);
        filter.deny(FLForgeEvents.class);
        filter.deny(FLHelpers.class);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(FirmaLife.MOD_ID)
                .register(FLRecipeSerializers.DRYING.getId().getPath(), BasicSchema.SCHEMA)
                .register(FLRecipeSerializers.SMOKING.getId().getPath(), BasicSchema.SCHEMA)
                .register(FLRecipeSerializers.MIXING_BOWL.getId().getPath(), MixingBowlSchema.SCHEMA)
                .register(FLRecipeSerializers.OVEN.getId().getPath(), OvenSchema.SCHEMA)
                .register(FLRecipeSerializers.STINKY_SOUP.getId().getPath(), SoupPotSchema.SCHEMA) // Yes, they are both direct inheritors of PotRecipe with nothing additional
                .register(FLRecipeSerializers.VAT.getId().getPath(), VatSchema.SCHEMA)
                .register(FLRecipeSerializers.STOMPING.getId().getPath(), StompingSchema.SCHEMA)
                .register(FLRecipeSerializers.BOWL_POT.getId().getPath(), BowlPotSchema.SCHEMA)
                .register(FLRecipeSerializers.PRESS.getId().getPath(), StompingSchema.SCHEMA) // Unused currently, recipes are actually stomping
                ;
    }
}
