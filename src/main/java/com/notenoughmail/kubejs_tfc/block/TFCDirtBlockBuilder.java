package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ConnectedGrassBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.TFCFarmlandBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.TFCPathBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.TFCRootedDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.blocks.soil.DirtBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TFCDirtBlockBuilder extends BlockBuilder {

    public transient final ConnectedGrassBlockBuilder grass;
    @Nullable
    public transient TFCPathBlockBuilder path;
    @Nullable
    public transient TFCFarmlandBlockBuilder farmland;
    @Nullable
    public transient TFCRootedDirtBlockBuilder rooted;

    public TFCDirtBlockBuilder(ResourceLocation i) {
        super(i);
        grass = new ConnectedGrassBlockBuilder(newID("", "_grass"), this);
        path = null;
        farmland = null;
        rooted = null;
    }

    @Generics(value = ConnectedGrassBlockBuilder.class)
    public TFCDirtBlockBuilder grass(Consumer<ConnectedGrassBlockBuilder> grass) {
        grass.accept(this.grass);
        return this;
    }

    @Generics(value = TFCPathBlockBuilder.class)
    public TFCDirtBlockBuilder path(Consumer<TFCPathBlockBuilder> path) {
        this.path = new TFCPathBlockBuilder(newID("", "_path"), this);
        path.accept(this.path);
        return this;
    }

    @Generics(value = TFCFarmlandBlockBuilder.class)
    public TFCDirtBlockBuilder farmland(Consumer<TFCFarmlandBlockBuilder> farmland) {
        this.farmland = new TFCFarmlandBlockBuilder(newID("", "_farmland"), this);
        farmland.accept(this.farmland);
        return this;
    }

    @Generics(value = TFCRootedDirtBlockBuilder.class)
    public TFCDirtBlockBuilder rooted(Consumer<TFCRootedDirtBlockBuilder> rooted) {
        this.rooted = new TFCRootedDirtBlockBuilder(newID("", "_rooted"), this);
        rooted.accept(this.rooted);
        return this;
    }

    @Override
    public Block createObject() {
        return new DirtBlock(createProperties(), grass, path, farmland, rooted);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(grass);
        grass.createAdditionalObjects();
        if (path != null) {
            RegistryInfo.BLOCK.addBuilder(path);
            path.createAdditionalObjects();
        }
        if (farmland != null) {
            RegistryInfo.BLOCK.addBuilder(farmland);
            RegistryUtils.addFarmland(farmland);
            farmland.createAdditionalObjects();
        }
        if (rooted != null) {
            RegistryInfo.BLOCK.addBuilder(rooted);
            rooted.createAdditionalObjects();
        }
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        super.generateItemModelJson(m);
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();

        bs.variant("", v -> {
            v.model(model);
            v.model(model).y(90);
            v.model(model).y(180);
            v.model(model).y(270);
        });
    }
}
