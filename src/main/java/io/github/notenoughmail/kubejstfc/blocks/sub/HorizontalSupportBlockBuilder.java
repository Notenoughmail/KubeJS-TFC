package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.SupportBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class HorizontalSupportBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final ResourceLocation HORIZONTAL = KubeJSTFC.tfc("block/wood/support/horizontal");

    public transient final SupportBlockBuilder parent;

    public HorizontalSupportBlockBuilder(ResourceLocation i, SupportBlockBuilder parent) {
        super(i);
        this.parent = parent;
        itemBuilder = null;
        Assistant.singleTag(this, TFCTags.Blocks.SUPPORT_BEAMS);
    }

    @Override
    public Block createObject() {
        return new HorizontalSupportBlock(createExtendedProperties().noOcclusion());
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        if (parent.itemBuilder != null) {
            return LootUtil.fallback(parent.itemBuilder::get, this);
        } else {
            return LootUtil.skipIfEmpty(drops);
        }
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(HORIZONTAL);
            m.textures(textures);
        });
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        bs.part("", ModelUtil.plainModel(this));
        bs.part("north=true", p -> p.model(parent.connectionModel).y(270));
        bs.part("east=true", parent.connectionModel);
        bs.part("south=true", p -> p.model(parent.connectionModel).y(90));
        bs.part("west=true", p -> p.model(parent.connectionModel).y(180));
    }
}
