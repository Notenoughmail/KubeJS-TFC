package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.internal.LampBlockItemBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;

import java.util.Locale;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class LampBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient int lightLevel;
    public transient BiConsumer<ModelType, ModelGenerator> models;

    public LampBlockBuilder(ResourceLocation i) {
        super(i);
        lightLevel = 15;
        itemBuilder = new LampBlockItemBuilder(id, this);
        tag(Helpers.identifier("lamps"));
        renderType("cutout");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.LAMP, this);
        texture("chain", id.getNamespace() + ":block/" + id.getPath() + "_chain");
        models = (t, m) -> {
            m.parent(t.hanging ? "tfc:block/lamp_hanging" : "tfc;block/lamp");
            m.texture("lamp", t.on ? "tfc:block/lamp" : "tfc:block/lamp_off");
            m.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of he lamp block, accepts a `BiConsumer` of a `ModelType` and a model generator.
            The generator is unique for each type.
            
            There are 4 types: `OFF`, HANGING_OFF`, `ON`, and `HANGING_ON`. There have 2 boolean properties which can
            be used to determine the type currently in operation. The properties are `.on` and `.hanging`.
            """)
    public LampBlockBuilder models(BiConsumer<ModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Info("Sets the light level the lamp gives off when it is lit")
    public LampBlockBuilder lightLevel(int i) {
        lightLevel = i;
        return this;
    }

    @Override
    public Block createObject() {
        return new LampBlock(createExtendedProperties());
    }

    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .randomTicks()
                .pushReaction(PushReaction.DESTROY)
                .lightLevel(state -> state.getValue(LampBlock.LIT) ? lightLevel : 0)
                .blockEntity(TFCBlockEntities.LAMP);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        texture("metal", tex);
        texture("chain", tex);
        return this;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(generator, this, p -> {
            p.survivesExplosion();
            final JsonObject func = new JsonObject();
            func.addProperty("function", "tfc:copy_fluid");
            p.addItem(get().asItem().getDefaultInstance()).addFunction(func);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");
        }

        if (itemBuilder.textureJson.size() == 0) {
            itemBuilder.textureJson.addProperty("layer0", newID("item/", "").toString());
        }

        m.textures(itemBuilder.textureJson);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (ModelType t : ModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
       bs.simpleVariant("hanging=false,lit=false", ModelType.OFF.modelEx(this));
       bs.simpleVariant("hanging=true,lit=false", ModelType.HANGING_OFF.modelEx(this));
       bs.simpleVariant("hanging=false,lit=true", ModelType.ON.modelEx(this));
       bs.simpleVariant("hanging=true,lit=true", ModelType.HANGING_ON.modelEx(this));
    }

    public enum ModelType {
        OFF(false, false),
        HANGING_OFF(false, true),
        ON(true, false),
        HANGING_ON(true, true);

        public final boolean on, hanging;

        ModelType(boolean on, boolean hanging) {
            this.on = on;
            this.hanging = hanging;
        }

        public static final ModelType[] VALUES = values();

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return builder.newID("", "_" + name().toLowerCase(Locale.ROOT));
        }

        @HideFromJS
        public String modelEx(BlockBuilder builder) {
            return builder.newID("block/", "_" + name().toLowerCase(Locale.ROOT)).toString();
        }
    }
}
