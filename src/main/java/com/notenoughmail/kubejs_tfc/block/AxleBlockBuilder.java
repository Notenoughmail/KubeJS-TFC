package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.*;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
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

    public transient final WindmillBlockBuilder windmill;
    public transient ResourceLocation texture;
    public transient WaterWheelBlockBuilder waterWheel;
    public transient GearBoxBlockBuilder gearBox;
    public transient ClutchBlockBuilder clutch;
    public transient BladedAxleBlockBuilder bladedAxle;

    public AxleBlockBuilder(ResourceLocation i) {
        super(i);
        windmill = new WindmillBlockBuilder(newID("", "_windmill"), this);
        texture = newID("block/", "");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.AXLE, this);
    }

    @Info(value = "Sets the texture that will be used for the axle")
    public AxleBlockBuilder axleTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    @Info(value = "Sets the properties of the axle's windmill block")
    @Generics(WindmillBlockBuilder.class)
    public AxleBlockBuilder windmill(Consumer<WindmillBlockBuilder> windmill) {
        windmill.accept(this.windmill);
        return this;
    }

    @Info(value = "Creates and sets the properties of the axle's water wheel block")
    @Generics(WaterWheelBlockBuilder.class)
    public AxleBlockBuilder waterWheel(Consumer<WaterWheelBlockBuilder> waterWheel) {
        this.waterWheel = Util.make(new WaterWheelBlockBuilder(newID("", "_waterwheel"), this), waterWheel);
        return this;
    }

    @Info(value = "Creates and sets the properties of the axle's gear box block")
    @Generics(GearBoxBlockBuilder.class)
    public AxleBlockBuilder gearBox(Consumer<GearBoxBlockBuilder> gearBox) {
        this.gearBox = Util.make(new GearBoxBlockBuilder(newID("", "_gearbox"), this), gearBox);
        return this;
    }

    @Info(value = "Creates and sets the properties of the axle's clutch block")
    @Generics(ClutchBlockBuilder.class)
    public AxleBlockBuilder clutch(Consumer<ClutchBlockBuilder> clutch) {
        this.clutch = Util.make(new ClutchBlockBuilder(newID("", "_clutch"), this), clutch);
        return this;
    }

    @Info(value = "Creates and sets the properties of the axle's bladed axle block")
    @Generics(BladedAxleBlockBuilder.class)
    public AxleBlockBuilder bladedAxle(Consumer<BladedAxleBlockBuilder> bladed) {
        bladedAxle = Util.make(new BladedAxleBlockBuilder(newID("", "_bladed"), this), bladed);
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
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
        RegistryInfo.BLOCK.addBuilder(windmill);
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
        if (model.isEmpty()) {
            generator.blockModel(id, g -> {
                g.parent("tfc:block/axle");
                g.textures(textures);
            });
        } else {
            hasModel(generator);
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", "tfc:block/empty");
    }
}
