package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class AnvilBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient int tier;

    public AnvilBlockBuilder(ResourceLocation i) {
        super(i);
        tier = 0;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.ANVIL, this);
        Assistant.singleTag(this, TFCTags.Items.ANVILS);
        ModelUtil.defaultTexture(this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Info("Sets the tier of recipes the anvil can perform")
    public AnvilBlockBuilder tier(int i) {
        tier = i;
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.ANVIL);
    }

    @Override
    public Block createObject() {
        return new AnvilBlock(createExtendedProperties(), tier);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.variant("facing=north", v -> v.model(m).y(90));
        bs.variant("facing=east", v -> v.model(m).y(180));
        bs.variant("facing=south", v -> v.model(m).y(270));
        bs.simpleVariant("facing=west", m);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotParented(generator, this, m -> {
            m.parent(KubeJSTFC.tfc("block/anvil"));
            m.textures(textures);
        });
    }
}
