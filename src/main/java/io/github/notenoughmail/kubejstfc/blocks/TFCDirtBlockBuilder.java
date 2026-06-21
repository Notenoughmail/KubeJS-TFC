package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.blocks.sub.ConnectedGrassBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCFarmlandBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCPathBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCRootedDirtBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.soil.DirtBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ReturnsSelf
@SuppressWarnings("unused")
public class TFCDirtBlockBuilder extends BlockBuilder {

    public transient final DelayedBuilder<ConnectedGrassBlockBuilder> grass;
    public transient final DelayedBuilder.NullCapable<TFCPathBlockBuilder> path;
    public transient final DelayedBuilder.NullCapable<TFCFarmlandBlockBuilder> farmland;
    public transient final DelayedBuilder<TFCRootedDirtBlockBuilder> rooted;
    public transient final DelayedBuilder<BlockBuilder> mud;

    public TFCDirtBlockBuilder(ResourceLocation i) {
        super(i);
        Assistant.singleTag(this, TFCTags.Blocks.DIRT);
        grass = new DelayedBuilder<>(r -> new ConnectedGrassBlockBuilder(r, this), () -> id.withSuffix("_grass"));
        path = new DelayedBuilder.NullCapable<>(r -> new TFCPathBlockBuilder(r, this), () -> id.withSuffix("_path"));
        path.markNull();
        farmland = new DelayedBuilder.NullCapable<>(r -> new TFCFarmlandBlockBuilder(r, this), () -> id.withSuffix("_farmland"));
        farmland.markNull();
        rooted = new DelayedBuilder<>(r -> new TFCRootedDirtBlockBuilder(r, this), () -> id.withSuffix("_rooted"));
        mud = new DelayedBuilder<>(BasicKubeBlock.Builder::new, () -> id.withSuffix("_mud"));
    }

    @Info("Sets the properties of the dirt's grass block")
    public TFCDirtBlockBuilder grass(Consumer<ConnectedGrassBlockBuilder> grass) {
        return grass(null, grass);
    }

    @Info("Sets the properties of the dirt's grass block")
    public TFCDirtBlockBuilder grass(@Nullable KubeResourceLocation id, Consumer<ConnectedGrassBlockBuilder> grass) {
        this.grass.accept(id, grass);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's path block")
    public TFCDirtBlockBuilder path(Consumer<TFCPathBlockBuilder> path) {
        return path(null, path);
    }

    @Info("Creates and sets the properties of the dirt's path block")
    public TFCDirtBlockBuilder path(@Nullable KubeResourceLocation id, Consumer<TFCPathBlockBuilder> path) {
        this.path.unmarkNull();
        this.path.accept(id, path);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's farmland block")
    public TFCDirtBlockBuilder farmland(Consumer<TFCFarmlandBlockBuilder> farmland) {
        return farmland(null, farmland);
    }

    @Info("Creates and sets the properties of the dirt's farmland block")
    public TFCDirtBlockBuilder farmland(@Nullable KubeResourceLocation id, Consumer<TFCFarmlandBlockBuilder> farmland) {
        this.farmland.unmarkNull();
        this.farmland.accept(id, farmland);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's rooted dirt block")
    public TFCDirtBlockBuilder rooted(Consumer<TFCRootedDirtBlockBuilder> rooted) {
        return rooted(null, rooted);
    }

    @Info("Creates and sets the properties of the dirt's rooted dirt block")
    public TFCDirtBlockBuilder rooted(@Nullable KubeResourceLocation id, Consumer<TFCRootedDirtBlockBuilder> rooted) {
        this.rooted.accept(id, rooted);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's mud block")
    public TFCDirtBlockBuilder mud(Consumer<BlockBuilder> mud) {
        return mud(null, mud);
    }

    @Info("Creates and sets the properties of the dirt's mud block")
    public TFCDirtBlockBuilder mud(@Nullable KubeResourceLocation id, Consumer<BlockBuilder> mud) {
        this.mud.accept(id, mud);
        return this;
    }

    @Override
    public Block createObject() {
        return new DirtBlock(createProperties(), grass.get(), path.get(), farmland.get(), rooted.get(), mud.get());
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, grass);
        Assistant.addBlock(registry, path);
        Assistant.addBlock(registry, farmland);
        Assistant.addBlock(registry, rooted);
        Assistant.addBlock(registry, mud);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(KubeAssetGenerator.CUBE_BLOCK_MODEL);
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation model = ModelUtil.plainModel(this);

        bs.variant("", v -> {
            v.model(model);
            v.model(model).y(90);
            v.model(model).y(180);
            v.model(model).y(270);
        });
    }
}
