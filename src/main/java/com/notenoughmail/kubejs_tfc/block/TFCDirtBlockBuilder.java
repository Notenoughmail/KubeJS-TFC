package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.sub.ConnectedGrassBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.TFCFarmlandBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.TFCPathBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.TFCRootedDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.DelayedBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.BasicBlockJS;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.soil.DirtBlock;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCDirtBlockBuilder extends BlockBuilder {

    public transient final DelayedBuilder<ConnectedGrassBlockBuilder> grass;
    @Nullable
    public transient TFCPathBlockBuilder path;
    @Nullable
    public transient TFCFarmlandBlockBuilder farmland;
    @Nullable
    public transient TFCRootedDirtBlockBuilder rooted;
    @Nullable
    public transient BlockBuilder mud;

    public TFCDirtBlockBuilder(ResourceLocation i) {
        super(i);
        grass = new DelayedBuilder<>(r -> new ConnectedGrassBlockBuilder(r, this), () -> newID("", "_grass"));
        path = null;
        farmland = null;
        rooted = null;
        mud = null;
    }

    @Info("Sets the properties of the dirt's grass block")
    @Generics(ConnectedGrassBlockBuilder.class)
    public TFCDirtBlockBuilder grass(Consumer<ConnectedGrassBlockBuilder> grass) {
        return grass(this.grass.fallbackId(), grass);
    }

    @Info("Sets the properties of the dirt's grass block")
    @Generics(ConnectedGrassBlockBuilder.class)
    public TFCDirtBlockBuilder grass(ResourceLocation id, Consumer<ConnectedGrassBlockBuilder> grass) {
        grass.accept(this.grass.get(id));
        return this;
    }

    @Info("Creates and sets the properties of the dirt's path block")
    @Generics(TFCPathBlockBuilder.class)
    public TFCDirtBlockBuilder path(Consumer<TFCPathBlockBuilder> path) {
        return path(newID("", "_path"), path);
    }

    @Info("Creates and sets the properties of the dirt's path block")
    @Generics(TFCPathBlockBuilder.class)
    public TFCDirtBlockBuilder path(ResourceLocation id, Consumer<TFCPathBlockBuilder> path) {
        this.path = Util.make(new TFCPathBlockBuilder(id ,this), path);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's farmland block")
    @Generics(TFCFarmlandBlockBuilder.class)
    public TFCDirtBlockBuilder farmland(Consumer<TFCFarmlandBlockBuilder> farmland) {
        return farmland(newID("", "_farmland"), farmland);
    }

    @Info("Creates and sets the properties of the dirt's farmland block")
    @Generics(TFCFarmlandBlockBuilder.class)
    public TFCDirtBlockBuilder farmland(ResourceLocation id, Consumer<TFCFarmlandBlockBuilder> farmland) {
        this.farmland = Util.make(new TFCFarmlandBlockBuilder(id, this), farmland);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's rooted dirt block")
    @Generics(TFCRootedDirtBlockBuilder.class)
    public TFCDirtBlockBuilder rooted(Consumer<TFCRootedDirtBlockBuilder> rooted) {
        return rooted(newID("", "_rooted"), rooted);
    }

    @Info("Creates and sets the properties of the dirt's rooted dirt block")
    @Generics(TFCRootedDirtBlockBuilder.class)
    public TFCDirtBlockBuilder rooted(ResourceLocation id, Consumer<TFCRootedDirtBlockBuilder> rooted) {
        this.rooted = Util.make(new TFCRootedDirtBlockBuilder(id, this), rooted);
        return this;
    }

    @Info("Creates and sets the properties of the dirt's mud block")
    @Generics(BlockBuilder.class)
    public TFCDirtBlockBuilder mud(Consumer<BlockBuilder> mud) {
        return mud(newID("", "_mud"), mud);
    }

    @Info("Creates and sets the properties of the dirt's mud block")
    @Generics(BlockBuilder.class)
    public TFCDirtBlockBuilder mud(ResourceLocation id, Consumer<BlockBuilder> mud) {
        this.mud = Util.make(new BasicBlockJS.Builder(id), mud);
        return this;
    }

    @Override
    public Block createObject() {
        return new DirtBlock(createProperties(), grass.get(), path, farmland, rooted, mud);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(grass.get());
        grass.get().createAdditionalObjects();
        if (path != null) {
            RegistryInfo.BLOCK.addBuilder(path);
            path.createAdditionalObjects();
        }
        if (farmland != null) {
            RegistryInfo.BLOCK.addBuilder(farmland);
            farmland.createAdditionalObjects();
        }
        if (rooted != null) {
            RegistryInfo.BLOCK.addBuilder(rooted);
            rooted.createAdditionalObjects();
        }
        if (mud != null) {
            RegistryInfo.BLOCK.addBuilder(mud);
            mud.createAdditionalObjects();
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = ResourceUtils.plainModel(this);

        bs.variant("", v -> {
            v.model(model);
            v.model(model).y(90);
            v.model(model).y(180);
            v.model(model).y(270);
        });
    }
}
