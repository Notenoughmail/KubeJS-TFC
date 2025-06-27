package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.WaterWheelBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class WaterWheelBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final AxleBlockBuilder parent;
    public transient ResourceLocation texture;

    public WaterWheelBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        texture = parent.newID("entity/water_wheel/", "");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.WATER_WHEEL, this);
    }

    @Info("Sets the texture that will be used for the water wheel, the path will be relative to the `/textures/entity/water_wheel/` subdirectory")
    public WaterWheelBlockBuilder texture(ResourceLocation tex) {
        texture = tex.withPrefix("entity/water_wheel/");
        return this;
    }

    @Info("sets the texture that will be used for the water wheel, the path is relative to the `/textures/` subdirectory")
    public WaterWheelBlockBuilder textureRaw(ResourceLocation tex) {
        texture = tex;
        return this;
    }

    @Override
    public Block createObject() {
        return new WaterWheelBlock(createExtendedProperties(), UtilsJS.cast(parent), texture.withPath(p -> "textures/" + p + ".png"));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.WATER_WHEEL)
                .ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");
            m.texture("layer0", newID("item/", "").toString());
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> m.textures(textures));
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", newID("block/", "").toString());
    }
}
