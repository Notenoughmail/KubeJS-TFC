package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.*;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.AxleBlock;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@ReturnsSelf
public class AxleBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public static final String[] TEXTURE_KEYS = { "particle", "wood" };

    public transient final WindmillBlockBuilder windmill;
    public transient ResourceLocation texture;
    public transient WaterWheelBlockBuilder waterWheel;
    public transient GearBoxBlockBuilder gearBox;
    public transient ClutchBlockBuilder clutch;
    public transient BladedAxleBlockBuilder bladedAxle;

    public AxleBlockBuilder(ResourceLocation i) {
        super(i);
        parentModel(KubeJSTFC.tfc("block/axle"));
        windmill = new WindmillBlockBuilder(id.withSuffix("_windmill"), this);
        texture = id.withPrefix("block/");
        BuilderRefs.hackBlockEntity(TFCBlockEntities.AXLE, this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("Sets the texture used for the axle")
    public AxleBlockBuilder axleTexture(ResourceLocation texture) {
        this.texture = texture;
        textures.put("wood", texture.toString());
        return this;
    }

    @Info("Sets the properties of the axle's windmill block")
    public AxleBlockBuilder windmill(Consumer<WindmillBlockBuilder> windmill) {
        windmill.accept(this.windmill);
        return this;
    }

    @Info("Creates and sets the properties of the axle's water wheel block")
    public AxleBlockBuilder waterWheel(Consumer<WaterWheelBlockBuilder> waterWheel) {
        this.waterWheel = Util.make(new WaterWheelBlockBuilder(id.withSuffix("_waterwheel"), this), waterWheel);
        return this;
    }

    @Info("Creates and sets the properties of the axle's gear box block")
    public AxleBlockBuilder gearBox(Consumer<GearBoxBlockBuilder> gearBox) {
        this.gearBox = Util.make(new GearBoxBlockBuilder(id.withSuffix("_gearbox"), this), gearBox);
        return this;
    }

    @Info("Creates and sets the properties of the axle's clutch block")
    public AxleBlockBuilder clutch(Consumer<ClutchBlockBuilder> clutch) {
        this.clutch = Util.make(new ClutchBlockBuilder(id.withSuffix("_clutch"), this), clutch);
        return this;
    }

    @Info("Creates and sets the properties of the axle's bladed axle block")
    public AxleBlockBuilder bladedAxle(Consumer<BladedAxleBlockBuilder> bladed) {
        bladedAxle = Util.make(new BladedAxleBlockBuilder(id.withSuffix("_bladed"), this), bladed);
        return this;
    }

    @Override
    public Block createObject() {
        return new AxleBlock(createExtendedProperties(), Cast.to(windmill), texture);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.AXLE);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, windmill);
        Assistant.addBlock(registry, waterWheel);
        Assistant.addBlock(registry, gearBox);
        Assistant.addBlock(registry, clutch);
        Assistant.addBlock(registry, bladedAxle);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", ModelUtil.TFC_EMPTY);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, g -> {
            g.parent(parentModel);
            g.textures(textures);
        });
    }
}
