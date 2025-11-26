package io.github.notenoughmail.kubejstfc.items;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.JavelinItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

@ReturnsSelf
@SuppressWarnings("unused")
public class JavelinItemBuilder extends HandheldItemBuilder {

    public static final ResourceLocation THROWING = KubeJSTFC.tfc("throwing");

    public float thrownDamage;
    public transient final Map<ItemDisplayContext, Consumer<ModelGenerator>> perspectives = new EnumMap<>(ItemDisplayContext.class);
    public transient ResourceLocation throwingModel;
    private boolean generateThrownModel = false, generateHandModel = false;
    public transient ResourceLocation thrownTexture;

    public JavelinItemBuilder(ResourceLocation i) {
        super(i, 0.7F, -2.6F);
        thrownDamage = -1F;
        thrownTexture(id);
        guiModel(newID("item/", "_gui"));
        BuilderRefs.javelins.add(this);
    }

    @Info("Sets the texture used by the javelin when thrown as an entity")
    public JavelinItemBuilder thrownTexture(ResourceLocation texture) {
        thrownTexture = texture.withPath(s -> "textures/" + s + ".png");
        return this;
    }

    @Info("Sets the model used when throwing")
    public JavelinItemBuilder throwingModel(ResourceLocation model) {
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
        Assistant.singleTag(this, TFCTags.Items.SKELETON_WEAPONS);
        return this;
    }

    @Info(value = "Sets the model to use at the specified display context", params = {
            @Param(name = "perspective", value = "The display context which the specified model should be shown"),
            @Param(name = "model", value = "The model to use with the given perspective")
    })
    public JavelinItemBuilder modelAtPerspective(ItemDisplayContext perspective, ResourceLocation model) {
        return fullModelAtPerspective(perspective, m -> m.parent(model));
    }

    @Info(value = "Modifies the model to use at the specified display context", params = {
            @Param(name = "perspective", value = "The display context which the model applies to"),
            @Param(name = "model", value = "The model to use")
    })
    public JavelinItemBuilder fullModelAtPerspective(ItemDisplayContext perspective, Consumer<ModelGenerator> model) {
        perspectives.put(perspective, model);
        return this;
    }

    @Info("Sets the model to be used for the 'none', 'fixed', 'ground', and 'gui' display contexts")
    public JavelinItemBuilder guiModel(ResourceLocation model) {
        return guiFullModel(m -> m.parent(model));
    }

    @Info("Modifies the model to be used for the 'none', 'fixed', 'ground', and 'gui' display contexts")
    public JavelinItemBuilder guiFullModel(Consumer<ModelGenerator> m) {
        perspectives.put(ItemDisplayContext.NONE, m);
        perspectives.put(ItemDisplayContext.FIXED, m);
        perspectives.put(ItemDisplayContext.GROUND, m);
        perspectives.put(ItemDisplayContext.GUI, m);
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
    protected void generateItemModels(KubeAssetGenerator generator) {
        if (throwingModel == null) {
            throwingModel = id.withPath(s -> "item/" + s +"_throwing");
            generateThrownModel = true;
        }
        if (generateThrownModel) {
            // TFC does it this way...
            generator.itemModel(id.withSuffix("_throwing_base"), simple);
            generator.itemModel(id.withSuffix("_throwing"), m -> m.custom(j -> {
                transforms(j);
                j.add("base", Assistant.json(b -> b.addProperty("parent", id.withPath(s -> "item/" + s + "_throwing_base").toString())));
            }));
        }
        if (parentModel == null) {
            parentModel = id.withPath(s -> "item/" + s + "_in_hand");
            generateHandModel = true;
        }
        if (generateHandModel) {
            generator.itemModel(id.withSuffix("_in_hand"), simple);
        }
        generator.itemModel(id.withSuffix("_gui"), m -> {
            m.parent(KubeAssetGenerator.GENERATED_ITEM_MODEL);
            tex(m);
        });
        generator.itemModel(id, m -> {
            if (modelGenerator != null) {
                modelGenerator.accept(m);
            } else {
                tex(m);
                m.override(throwingModel, o -> o.predicate(THROWING, 1F));
                m.custom(j -> {
                    transforms(j);
                    j.add("base", Assistant.json(b -> b.addProperty("parent", parentModel.toString())));
                });
            }
        });
    }

    private final Consumer<ModelGenerator> simple = m -> {
        m.parent(Helpers.identifier("item/trident/throwing"));
        tex(m);
    };

    private void tex(ModelGenerator m) {
        if (textures.isEmpty()) {
            m.texture("particle", baseTexture);
        } else {
            m.textures(textures);
        }
    }

    private void transforms(JsonObject model) {
        model.addProperty("loader", "neoforge:separate_transforms");
        model.addProperty("gui_light", "front");
        model.add("perspectives", Assistant.json(p ->
                perspectives.forEach((ctx, m) ->
                        p.add(ctx.getSerializedName(), Util.make(new ModelGenerator(), m).toJson()))
        ));
    }
}
