package io.github.notenoughmail.kubejstfc.blocks.sub;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.blocks.AxleBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WindmillBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.WindmillBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class WindmillBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final AxleBlockBuilder parent;

    public WindmillBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        itemBuilder = null;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.WINDMILL, this);
    }

    @Override
    public Block createObject() {
        return new WindmillBlock(createExtendedProperties(), Cast.to(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.WINDMILL)
                .ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", ModelUtil.TFC_EMPTY);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {}
}
