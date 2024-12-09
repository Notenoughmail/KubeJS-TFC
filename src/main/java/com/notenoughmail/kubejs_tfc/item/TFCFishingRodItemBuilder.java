package com.notenoughmail.kubejs_tfc.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TFCFishingRodItemBuilder extends HandheldItemBuilder {

    @Nullable
    public transient Float fishingStrength;
    public static final List<TFCFishingRodItemBuilder> thisList = new ArrayList<>();
    public transient String customCastModel, castTexture;

    public TFCFishingRodItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        fishingStrength = null;
        thisList.add(this);
        customCastModel = "";
        castTexture = newID("item/", "_cast").toString();
    }

    @Info(value = "Sets the rod's fishing strength, defaults the a value based on the speed of the rod's tier")
    public TFCFishingRodItemBuilder fishingStrength(float f) {
        fishingStrength = f;
        return this;
    }

    @Info(value = "Sets the rod's model when cast")
    public TFCFishingRodItemBuilder castModel(String model) {
        customCastModel = model;
        return this;
    }

    @Info(value = "Allows this rod to hold small fishing bait by adding it to the correct tag")
    public TFCFishingRodItemBuilder smallBait() {
        return (TFCFishingRodItemBuilder) tag(TFCTags.Items.HOLDS_SMALL_FISHING_BAIT.location());
    }

    @Info(value = "Allows this rod to hold large fishing bait by adding it the correct tag")
    public TFCFishingRodItemBuilder largeBait() {
        return (TFCFishingRodItemBuilder) tag(TFCTags.Items.HOLDS_LARGE_FISHING_BAIT.location());
    }

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

        final JsonObject predicate = new JsonObject();
        final JsonObject castPredicate = new JsonObject();
        castPredicate.addProperty("tfc:cast", 1);
        predicate.add("predicate", castPredicate);
        predicate.addProperty("model", customCastModel.isEmpty() ? newID("item/", "_cast").toString() : customCastModel);
        final JsonArray overrides = new JsonArray(1);
        overrides.add(predicate);
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
