package com.notenoughmail.kubejs_tfc.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TFCFishingRodItemBuilder extends HandheldItemBuilder {

    @Nullable
    public transient Float fishingStrength;
    public transient String customCastModel, castTexture;

    public TFCFishingRodItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        fishingStrength = null;
        customCastModel = "";
        castTexture = newID("item/", "_cast").toString();
        BuilderRefs.rodCast.add(this);
    }

    @Info("Sets the rod's fishing strength, defaults the a value based on the speed of the rod's tier")
    public TFCFishingRodItemBuilder fishingStrength(float f) {
        fishingStrength = f;
        return this;
    }

    @Info("Sets the rod's model when cast")
    public TFCFishingRodItemBuilder castModel(String model) {
        customCastModel = model;
        return this;
    }

    @Info("Allows this rod to hold small fishing bait by adding it to the correct tag")
    public TFCFishingRodItemBuilder smallBait() {
        return (TFCFishingRodItemBuilder) tag(TFCTags.Items.HOLDS_SMALL_FISHING_BAIT.location());
    }

    @Info("Allows this rod to hold large fishing bait by adding it the correct tag")
    public TFCFishingRodItemBuilder largeBait() {
        return (TFCFishingRodItemBuilder) tag(TFCTags.Items.HOLDS_LARGE_FISHING_BAIT.location());
    }

    @Info("Sets the texture used when the rod is cast")
    public TFCFishingRodItemBuilder castTexture(String tex) {
        castTexture = tex;
        return this;
    }

    @Override
    public Item createObject() {
        return new TFCFishingRodItem(createItemProperties(), toolTier) {

            @Override
            public float getFishingStrength() {
                return fishingStrength == null ? super.getFishingStrength() : fishingStrength;
            }
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(id), modelJson);
            return;
        }

        final JsonObject primaryModel = Util.make(new ModelGenerator(), m -> {
            if (!parentModel.isEmpty()) {
                m.parent(parentModel);
            } else {
                m.parent("minecraft:item/handheld_rod");
            }

            if (textureJson.size() == 0) {
                texture(newID("item/", "").toString());
            }
            m.textures(textureJson);
        }).toJson();

        final JsonArray overrides = new JsonArray(1);
        overrides.add(ResourceUtils.buildJson(predicate -> {
            predicate.add("predicate", ResourceUtils.buildJson(castPredicate -> castPredicate.addProperty("tfc:cast", 1)));
            predicate.addProperty("model", customCastModel.isEmpty() ? newID("item/", "_cast").toString() : customCastModel);
        }));
        primaryModel.add("overrides", overrides);

        generator.json(AssetJsonGenerator.asItemModelLocation(id), primaryModel);

        if (customCastModel.isEmpty()) {
            generator.itemModel(newID("", "_cast"), m -> {
                m.parent("item/fishing_rod");
                m.texture("layer0", castTexture);
            });
        }
    }
}
