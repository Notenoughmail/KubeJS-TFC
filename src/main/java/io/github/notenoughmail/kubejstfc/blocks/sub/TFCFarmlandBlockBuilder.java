package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import io.github.notenoughmail.kubejstfc.blocks.TFCDirtBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TFCFarmlandBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "top", "dirt" };

    public transient final TFCDirtBlockBuilder parent;

    public TFCFarmlandBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.FARMLAND, this);
        Assistant.singleTag(this, TFCTags.Blocks.FARMLANDS);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new FarmlandBlock(createExtendedProperties(), parent);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        if (parent.itemBuilder != null) {
            return LootUtil.fallback(parent.itemBuilder::get, this);
        }
        return LootUtil.skipIfEmpty(drops);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.FARMLAND);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(ModelUtil.FARMLAND);
            m.textures(textures);
        });
    }
}
