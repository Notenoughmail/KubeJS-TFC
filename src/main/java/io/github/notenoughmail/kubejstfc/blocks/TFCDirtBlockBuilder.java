package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.blocks.sub.ConnectedGrassBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCFarmlandBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCPathBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCRootedDirtBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.soil.DirtBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCDirtBlockBuilder extends BlockBuilder {

    public transient final ConnectedGrassBlockBuilder grass;
    @Nullable
    public transient TFCPathBlockBuilder path;
    @Nullable
    public transient TFCFarmlandBlockBuilder farmland;
    public transient final TFCRootedDirtBlockBuilder rooted;
    public transient final BlockBuilder mud;

    public TFCDirtBlockBuilder(ResourceLocation i) {
        super(i);
        Assistant.singleTag(this, TFCTags.Blocks.DIRT);
        grass = new ConnectedGrassBlockBuilder(id.withSuffix("_grass"), this);
        path = null;
        farmland = null;
        rooted = new TFCRootedDirtBlockBuilder(id.withSuffix("_rooted"), this);
        mud = new BasicKubeBlock.Builder(id.withSuffix("_mud"));
    }

    @Info("Sets the properties of the dirt's grass block")
    public TFCDirtBlockBuilder grass(Consumer<ConnectedGrassBlockBuilder> grass) {
        grass.accept(this.grass);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's path block")
    public TFCDirtBlockBuilder path(Consumer<TFCPathBlockBuilder> path) {
        this.path = new TFCPathBlockBuilder(newID("", "_path"), this);
        path.accept(this.path);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's farmland block")
    public TFCDirtBlockBuilder farmland(Consumer<TFCFarmlandBlockBuilder> farmland) {
        this.farmland = new TFCFarmlandBlockBuilder(newID("", "_farmland"), this);
        farmland.accept(this.farmland);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's rooted dirt block")
    public TFCDirtBlockBuilder rooted(Consumer<TFCRootedDirtBlockBuilder> rooted) {
        rooted.accept(this.rooted);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's mud block")
    public TFCDirtBlockBuilder mud(Consumer<BlockBuilder> mud) {
        mud.accept(this.mud);
        return this;
    }

    @Override
    public Block createObject() {
        return new DirtBlock(createProperties(), grass, path, farmland, rooted, mud);
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
