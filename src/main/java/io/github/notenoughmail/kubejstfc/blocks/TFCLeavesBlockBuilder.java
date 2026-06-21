package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.blocks.sub.FallenLeavesBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.LeavesBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ReturnsSelf
public class TFCLeavesBlockBuilder extends LeavesBuilder {

    public transient int autumnIndex;
    @Nullable
    public transient Supplier<Block> twig;
    public transient final DelayedBuilder.NullCapable<FallenLeavesBlockBuilder> fallenLeaves;
    public transient boolean seasonalColors;

    public TFCLeavesBlockBuilder(ResourceLocation i) {
        super(i);
        fallenLeaves = new DelayedBuilder.NullCapable<>(r -> new FallenLeavesBlockBuilder(r, this), () -> id.withSuffix("_fallen"));
        fallenLeaves.onMarkedNull(BuilderRefs.leafColor::remove);
        seasonalColors = true;
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Info("Sets the properties of the fallen leaves block. May be null to not have fallen leaves")
    public TFCLeavesBlockBuilder fallenLeaves(@Nullable Consumer<FallenLeavesBlockBuilder> fallenLeaves) {
        return fallenLeaves(null, fallenLeaves);
    }

    @Info("Sets the properties of the fallen leaves block. May be null to not have fallen leaves")
    public TFCLeavesBlockBuilder fallenLeaves(@Nullable KubeResourceLocation id, @Nullable Consumer<FallenLeavesBlockBuilder> fallenLeaves) {
        this.fallenLeaves.accept(id, fallenLeaves);
        return this;
    }

    @Info("Sets the twig block to be placed when a natural leaf is broken")
    public TFCLeavesBlockBuilder twig(Holder<Block> twig) {
        this.twig = Assistant.holderAsSupplier(twig);
        return this;
    }

    @Override
    public Block createObject() {
        return new TFCLeavesBlock(createExtendedProperties().randomTicks().noOcclusion(), autumnIndex, fallenLeaves.get(), twig);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, fallenLeaves);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(LEAVES);
            m.textures(textures);
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.inheritItemModelGen(this, m);
    }
}
