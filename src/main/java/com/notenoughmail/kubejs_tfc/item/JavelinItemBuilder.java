package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.JavelinItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.HashMap;
import java.util.Map;

// TODO: 2.0.0 | Ow
@SuppressWarnings("unused")
public class JavelinItemBuilder extends HandheldItemBuilder {

    public float thrownDamage;

    public transient final Map<ItemDisplayContext, String> perspectives = new HashMap<>(4);
    public transient String throwingModel = "";

    public JavelinItemBuilder(ResourceLocation i) {
        super(i, 0.7F, -2.6f);
        thrownDamage = 0.3f;
        guiModel(newID("item/", "_gui").toString());
        texture(newID("item/", "").toString());
        BuilderRefs.javelinThrow.add(this);
    }

    @Info("Sets the model used when throwing")
    public JavelinItemBuilder throwingModel(String model) {
        throwingModel = model;
        return this;
    }

    @Info("Sets the javelin's thrown damage")
    public JavelinItemBuilder thrownDamage(float damage) {
        thrownDamage = damage;
        return this;
    }

    @Info("Adds this to the 'tfc:skeleton_weapons' tag")
    public JavelinItemBuilder skeletonWeapon() {
        tag(TFCTags.Items.SKELETON_WEAPONS.location());
        return this;
    }

    @Info(value = "Sets the model to use at the specified display context", params = {
            @Param(name = "perspective", value = "The display context which the specified model should be shown"),
            @Param(name = "model", value = "The model to use with the given perspective")
    })
    public JavelinItemBuilder modelAtPerspective(ItemDisplayContext perspective, String model) {
        perspectives.put(perspective, model);
        return this;
    }

    @Info("Sets the model to be used for the 'none', 'fixed', 'ground', and 'gui' display contexts")
    public JavelinItemBuilder guiModel(String model) {
        perspectives.put(ItemDisplayContext.NONE, model);
        perspectives.put(ItemDisplayContext.FIXED, model);
        perspectives.put(ItemDisplayContext.GROUND, model);
        perspectives.put(ItemDisplayContext.GUI, model);
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        if (thrownDamage < 0) thrownDamage = 1.5F * toolTier.getAttackDamageBonus();
        return new JavelinItem(toolTier, createItemProperties()) {
            @Override
            public float getThrownDamage() {
                return thrownDamage;
            }
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(id), modelJson);
        } else {
            generator.itemModel(newID("", "_gui"), m -> {
                m.parent("item/generated");
                m.textures(textureJson);
            });

            if (parentModel.isEmpty()) {
                generator.itemModel(newID("", "_in_hand"), m -> {
                    m.parent("item/trident_in_hand");
                    m.textures(textureJson);
                });
            }

            if (throwingModel.isEmpty()) {
                generator.itemModel(newID("", "_throwing_base"), m -> {
                    m.parent("item/trident_throwing");
                    m.textures(textureJson);
                });
                generator.json(
                        newID("models/item/", "_throwing"),
                        transforms(throwingModel, true)
                );
            }

            generator.json(
                    AssetJsonGenerator.asItemModelLocation(id),
                    transforms(parentModel, false)
            );
        }
    }

    private JsonObject transforms(String baseModel, boolean throwing) {
        return ResourceUtils.buildJson(model -> {
            model.addProperty("loader", "forge:separate_transforms");
            model.addProperty("gui_light", "front");

            model.add("base", ResourceUtils.buildJson(base ->
                    base.addProperty(
                            "parent",
                            baseModel.isEmpty() ?
                                    newID("item/", throwing ? "_throwing_base" : "_in_hand").toString() :
                                    baseModel
                    )
            ));

            model.add("perspectives", ResourceUtils.buildJson(spectives ->
                    perspectives.forEach((p, m) ->
                            spectives.add(
                                    p.getSerializedName(),
                                    ResourceUtils.buildJson(j -> j.addProperty("parent", m))
                            )
                    )
            ));

            if (!throwing) {
                final JsonArray overrides = new JsonArray(1);
                overrides.add(ResourceUtils.buildJson(override -> {
                    override.add(
                            "predicate",
                            ResourceUtils.buildJson(predicate ->
                                    predicate.addProperty("tfc:throwing", 1)
                            )
                    );
                    override.addProperty(
                            "model",
                            throwingModel.isEmpty() ?
                                    newID("item/", "_throwing").toString() :
                                    throwingModel
                    );
                }));
                model.add("overrides", overrides);
            }
        });
    }
}
