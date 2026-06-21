package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.*;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.AxleBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ReturnsSelf
public class AxleBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public static final String[] TEXTURE_KEYS = { "particle", "wood" };

    public transient final DelayedBuilder<WindmillBlockBuilder> windmill;
    public transient ResourceLocation texture;
    public transient final DelayedBuilder.NullCapable<WaterWheelBlockBuilder> waterwheel;
    public transient final DelayedBuilder.NullCapable<GearBoxBlockBuilder> gearbox;
    public transient final DelayedBuilder.NullCapable<ClutchBlockBuilder> clutch;
    public transient final DelayedBuilder.NullCapable<BladedAxleBlockBuilder> bladedAxle;

    public AxleBlockBuilder(ResourceLocation i) {
        super(i);
        parentModel(KubeJSTFC.tfc("block/axle"));
        windmill = new DelayedBuilder<>(r ->  new WindmillBlockBuilder(r, this), () -> id.withSuffix("_windmill"));
        waterwheel = new DelayedBuilder.NullCapable<>(r -> new WaterWheelBlockBuilder(r, this), () -> id.withSuffix("_waterwheel"));
        waterwheel.markNull();
        gearbox = new DelayedBuilder.NullCapable<>(r -> new GearBoxBlockBuilder(r, this), () -> id.withSuffix("_gearbox"));
        gearbox.markNull();
        clutch = new DelayedBuilder.NullCapable<>(r -> new ClutchBlockBuilder(r, this), () -> id.withSuffix("_clutch"));
        clutch.markNull();
        bladedAxle = new DelayedBuilder.NullCapable<>(r -> new BladedAxleBlockBuilder(r, this), () -> id.withSuffix("_bladed"));
        bladedAxle.markNull();
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
        return windmill(null, windmill);
    }

    @Info("Sets the properties of the axle's windmill block")
    public AxleBlockBuilder windmill(@Nullable KubeResourceLocation id, Consumer<WindmillBlockBuilder> windmill) {
        this.windmill.accept(id, windmill);
        return this;
    }

    @Info("Creates and sets the properties of the axle's water wheel block")
    public AxleBlockBuilder waterWheel(Consumer<WaterWheelBlockBuilder> waterWheel) {
        return waterWheel(null, waterWheel);
    }

    @Info("Creates and sets the properties of the axle's water wheel block")
    public AxleBlockBuilder waterWheel(@Nullable KubeResourceLocation id, Consumer<WaterWheelBlockBuilder> waterWheel) {
        waterwheel.unmarkNull();
        waterwheel.accept(id, waterWheel);
        return this;
    }

    @Info("Creates and sets the properties of the axle's gear box block")
    public AxleBlockBuilder gearBox(Consumer<GearBoxBlockBuilder> gearBox) {
        return gearBox(null, gearBox);
    }

    @Info("Creates and sets the properties of the axle's gear box block")
    public AxleBlockBuilder gearBox(@Nullable KubeResourceLocation id, Consumer<GearBoxBlockBuilder> gearBox) {
        gearbox.unmarkNull();
        gearbox.accept(id, gearBox);
        return this;
    }

    @Info("Creates and sets the properties of the axle's clutch block")
    public AxleBlockBuilder clutch(Consumer<ClutchBlockBuilder> clutch) {
        return clutch(null, clutch);
    }

    @Info("Creates and sets the properties of the axle's clutch block")
    public AxleBlockBuilder clutch(@Nullable KubeResourceLocation id, Consumer<ClutchBlockBuilder> clutch) {
        this.clutch.unmarkNull();
        this.clutch.accept(id, clutch);
        return this;
    }

    @Info("Creates and sets the properties of the axle's bladed axle block")
    public AxleBlockBuilder bladedAxle(Consumer<BladedAxleBlockBuilder> bladed) {
        return bladedAxle(null, bladed);
    }

    @Info("Creates and sets the properties of the axle's bladed axle block")
    public AxleBlockBuilder bladedAxle(@Nullable KubeResourceLocation id, Consumer<BladedAxleBlockBuilder> bladed) {
        bladedAxle.unmarkNull();
        bladedAxle.accept(id, bladed);
        return this;
    }

    @Override
    public Block createObject() {
        return new AxleBlock(createExtendedProperties(), Cast.to(windmill.get()), texture);
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
        Assistant.addBlock(registry, waterwheel);
        Assistant.addBlock(registry, gearbox);
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
