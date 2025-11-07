package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.blocks.AxleBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
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
        wheelTexture(parent.id);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.WATER_WHEEL, this);
        BuilderRefs.waterWheels.add(this);
    }

    @Info("Sets the texture that will be used for the water wheel, the path will be relative to the `/textures/entity/water_wheel/` subdirectory")
    public WaterWheelBlockBuilder wheelTexture(ResourceLocation tex) {
        texture = tex.withPath(s -> "textures/entity/water_wheel" + s + ".png");
        return this;
    }

    @Info("sets the texture that will be used for the water wheel, the path is relative to the `/textures/` subdirectory")
    public WaterWheelBlockBuilder rawWheelTexture(ResourceLocation tex) {
        texture = tex.withPath(s -> "textures/" + s + ".png");
        return this;
    }

    @Override
    public Block createObject() {
        return new WaterWheelBlock(createExtendedProperties(), Cast.to(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.WATER_WHEEL)
                .ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        generator.blockModel(id, m -> m.texture("particle", baseTexture));
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, false, m);
    }
}
