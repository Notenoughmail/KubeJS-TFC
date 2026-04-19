package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.*;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.DelayedBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.AxleBlock;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class AxleBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final DelayedBuilder<WindmillBlockBuilder> windmill;
    public transient ResourceLocation texture;
    public transient WaterWheelBlockBuilder waterWheel;
    public transient GearBoxBlockBuilder gearBox;
    public transient ClutchBlockBuilder clutch;
    public transient BladedAxleBlockBuilder bladedAxle;

    public AxleBlockBuilder(ResourceLocation i) {
        super(i);
        windmill = new DelayedBuilder<>(r -> new WindmillBlockBuilder(r, this), () -> newID("", "_windmill"));
        texture = newID("block/", "");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.AXLE, this);
    }

    @Info("Sets the texture used for the axle")
    public AxleBlockBuilder axleTexture(ResourceLocation texture) {
        this.texture = texture;
        texture("wood", texture.toString());
        return this;
    }

    @Info("Sets the properties of the axle's windmill block")
    @Generics(WindmillBlockBuilder.class)
    public AxleBlockBuilder windmill(Consumer<WindmillBlockBuilder> windmill) {
        return windmill(this.windmill.fallbackId(), windmill);
    }

    @Info("Sets the properties of the axle's windmill block")
    @Generics(WindmillBlockBuilder.class)
    public AxleBlockBuilder windmill(ResourceLocation id, Consumer<WindmillBlockBuilder> windmill) {
        windmill.accept(this.windmill.get(id));
        return this;
    }

    @Info("Creates and sets the properties of the axle's water wheel block")
    @Generics(WaterWheelBlockBuilder.class)
    public AxleBlockBuilder waterWheel(Consumer<WaterWheelBlockBuilder> waterWheel) {
        return waterWheel(newID("", "_waterwheel"), waterWheel);
    }

    @Info("Creates and sets the properties of the axle's water wheel block")
    @Generics(WaterWheelBlockBuilder.class)
    public AxleBlockBuilder waterWheel(ResourceLocation id, Consumer<WaterWheelBlockBuilder> waterWheel) {
        this.waterWheel = Util.make(new WaterWheelBlockBuilder(id, this), waterWheel);
        return this;
    }

    @Info("Creates and sets the properties of the axle's gear box block")
    @Generics(GearBoxBlockBuilder.class)
    public AxleBlockBuilder gearBox(Consumer<GearBoxBlockBuilder> gearBox) {
        return gearBox(newID("", "_gearbox"), gearBox);
    }

    public AxleBlockBuilder gearBox(ResourceLocation id, Consumer<GearBoxBlockBuilder> gearBox) {
        this.gearBox = Util.make(new GearBoxBlockBuilder(id, this), gearBox);
        return this;
    }

    @Info("Creates and sets the properties of the axle's clutch block")
    @Generics(ClutchBlockBuilder.class)
    public AxleBlockBuilder clutch(Consumer<ClutchBlockBuilder> clutch) {
        return clutch(newID("", "_clutch"), clutch);
    }

    @Info("Creates and sets the properties of the axle's clutch block")
    @Generics(ClutchBlockBuilder.class)
    public AxleBlockBuilder clutch(ResourceLocation id, Consumer<ClutchBlockBuilder> clutch) {
        this.clutch = Util.make(new ClutchBlockBuilder(id, this), clutch);
        return this;
    }

    @Info("Creates and sets the properties of the axle's bladed axle block")
    @Generics(BladedAxleBlockBuilder.class)
    public AxleBlockBuilder bladedAxle(Consumer<BladedAxleBlockBuilder> bladed) {
        return bladedAxle(newID("", "_bladed"), bladed);
    }

    @Info("Creates and sets the properties of the axle's bladed axle block")
    @Generics(BladedAxleBlockBuilder.class)
    public AxleBlockBuilder bladedAxle(ResourceLocation id, Consumer<BladedAxleBlockBuilder> bladed) {
        bladedAxle = Util.make(new BladedAxleBlockBuilder(id, this), bladed);
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("wood", tex);
    }

    @Override
    public Block createObject() {
        return new AxleBlock(createExtendedProperties(), UtilsJS.cast(windmill), texture);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.AXLE);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(windmill.get());
        if (waterWheel != null) {
            RegistryInfo.BLOCK.addBuilder(waterWheel);
            waterWheel.createAdditionalObjects();
        }
        if (gearBox != null) {
            RegistryInfo.BLOCK.addBuilder(gearBox);
            gearBox.createAdditionalObjects();
        }
        if (clutch != null) {
            RegistryInfo.BLOCK.addBuilder(clutch);
            clutch.createAdditionalObjects();
        }
        if (bladedAxle != null) {
            RegistryInfo.BLOCK.addBuilder(bladedAxle);
            bladedAxle.createAdditionalObjects();
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.hasModelOrElse(generator, this, g -> {
            g.parent("tfc:block/axle");
            g.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", "tfc:block/empty");
    }
}
