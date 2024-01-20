package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.item.internal.LampBlockItemBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.LampBlockJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Consumer;

public class LampBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    public transient int lightLevel;
    public transient String chainTexture;
    public transient Consumer<ExtendedPropertiesJS> props;

    public LampBlockBuilder(ResourceLocation i) {
        super(i);
        lightLevel = 15;
        itemBuilder = new LampBlockItemBuilder(id, this);
        chainTexture = "";
        tag(Helpers.identifier("lamps"));
        renderType = "cutout";
        props = p -> {};
    }

    @Info(value = "Sets the light level the lamp gives off when it is lit")
    public LampBlockBuilder lightLevel(int i) {
        lightLevel = i;
        return this;
    }

    @Override
    public Block createObject() {
        return new LampBlockJS(createExtendedProperties());
    }

    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = extendedPropsJS();
        props.accept(propsJs);
        return propsJs.delegate()
                .noOcclusion()
                .randomTicks()
                .pushReaction(PushReaction.DESTROY)
                .lightLevel(state -> state.getValue(LampBlock.LIT) ? lightLevel : 0)
                .blockEntity(RegistryUtils.getLamp());
    }

    @Override
    public LampBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryUtils.addLamp(this);
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
        final JsonObject off = new JsonObject();
        off.addProperty("metal", newID("block/", "").toString());
        off.addProperty("chain", chainTexture.isEmpty() ? newID("block/", "_chain").toString() : chainTexture);
        final JsonObject on = off.deepCopy();
        on.addProperty("lamp", "tfc:block/lamp");
        off.addProperty("lamp", "tfc:block/lamp_off");

        generator.blockModel(newID("", "_off"), m -> {
            m.parent("tfc:block/lamp");
            m.textures(off);
        });
        generator.blockModel(newID("", "_hanging_off"), m -> {
            m.parent("tfc:block/lamp_hanging");
            m.textures(off);
        });
        generator.blockModel(newID("", "_on"), m -> {
            m.parent("tfc:block/lamp");
            m.textures(on);
        });
        generator.blockModel(newID("", "_hanging_on"), m -> {
            m.parent("tfc:block/lamp_hanging");
            m.textures(on);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
       bs.simpleVariant("hanging=false,lit=false", newID("block/", "_off").toString());
       bs.simpleVariant("hanging=true,lit=false", newID("block/", "_hanging_off").toString());
       bs.simpleVariant("hanging=false,lit=true", newID("block/", "_on").toString());
       bs.simpleVariant("hanging=true,lit=true", newID("block/", "_hanging_on").toString());
    }
}
