package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCFarmlandBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public TFCFarmlandBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        tag(TFCTags.Blocks.FARMLAND.location());
        RegistryUtils.hackBlockEntity(TFCBlockEntities.FARMLAND, this);
        texture("dirt", parent.textures.get("particle").getAsString()); // Parent does not yet exist when #textureAll is called in super constructor
    }

    @Info("Makes the farmland block use a unique texture for the dirt part of its texture, by default uses the texture of its parent dirt block")
    public TFCFarmlandBlockBuilder uniqueDirtTexture() {
        texture("dirt", id.getNamespace() + ":block/" + id.getPath());
        return this;
    }

    @Override
    public Block createObject() {
        return new FarmlandBlock(createExtendedProperties(), parent);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        texture("top", tex);
        texture("dirt", tex);
        return this;
    }

    @Override
    @Generics(BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTableBasic(generator, this, parent);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.FARMLAND);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("block/template_farmland");
            m.textures(textures);
        });
    }
}
