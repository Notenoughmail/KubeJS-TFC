package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.ShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.rock.RawRockBlock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

// Very "shaped" this is
@SuppressWarnings("unused")
public class RawRockBlockBuilder extends ShapedBlockBuilder {

    public transient boolean naturallySupported;
    @Nullable
    public transient Component rockTypeTooltip;
    private final String model;
    private final String mirrorModel;

    public RawRockBlockBuilder(ResourceLocation i) {
        super(i);
        naturallySupported = false;
        rockTypeTooltip = null;
        model = newID("block/", "").toString();
        mirrorModel = newID("block/", "_mirrored").toString();
    }

    @Info(value = "Determines if the block is considered to be naturally supported for the purposes of spawning particles indicating unsupported regions")
    public RawRockBlockBuilder naturallySupported(boolean supported) {
        naturallySupported = supported;
        return this;
    }

    @Info(value = "Sets the tooltip component indicating the block's rock type")
    public RawRockBlockBuilder rockTypeTooltip(Component comp) {
        rockTypeTooltip = comp;
        return this;
    }

    @Info(value = "Sets the rock type component to that of felsic igneous extrusive rocks")
    public RawRockBlockBuilder felsicIgneousExtrusive() {
        return rockTypeTooltip(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of intermediate igneous extrusive rocks")
    public RawRockBlockBuilder intermediateIgneousExtrusive() {
        return rockTypeTooltip(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of mafic igneous extrusive rocks")
    public RawRockBlockBuilder maficIgneousExtrusive() {
        return rockTypeTooltip(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of mafic igneous intrusive rocks")
    public RawRockBlockBuilder maficIgneousIntrusive() {
        return rockTypeTooltip(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of felsic igneous intrusive")
    public RawRockBlockBuilder felsicIgneousIntrusive() {
        return rockTypeTooltip(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of intermediate igneous intrusive rocks")
    public RawRockBlockBuilder intermediateIgneousIntrusive() {
        return rockTypeTooltip(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of metamorphic rocks")
    public RawRockBlockBuilder metamorphic() {
        return rockTypeTooltip(RockDisplayCategory.METAMORPHIC.createTooltip());
    }

    @Info(value = "Sets the rock type component to that of sedimentary rocks")
    public RawRockBlockBuilder sedimentary() {
        return rockTypeTooltip(RockDisplayCategory.SEDIMENTARY.createTooltip());
    }

    @Override
    public RawRockBlock createObject() {
        return new RawRockBlock(createProperties(), naturallySupported, rockTypeTooltip);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        return texture("all", tex);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        if (!super.model.isEmpty()) {
               generator.blockModel(id, m -> m.parent(model));
               generator.blockModel(newID("", "_mirrored"), m -> m.parent(model));
        } else {
            generator.blockModel(id, m -> {
                m.parent("block/cube_all");
                m.textures(textures);
            });
            generator.blockModel(newID("", "_mirrored"), m -> {
                m.parent("block/cube_all");
                m.textures(textures);
            });
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.variant("axis=x", v -> {
            v.model(model).x(90).y(90);
            v.model(mirrorModel).x(90).y(90);
        });
        bs.variant("axis=y", v -> {
            v.model(model);
            v.model(mirrorModel);
            v.model(model).y(180);
            v.model(mirrorModel).y(180);
        });
        bs.variant("axis=z", v -> {
            v.model(model).x(90);
            v.model(mirrorModel).x(90);
            v.model(model).x(90).y(180);
            v.model(mirrorModel).x(90).y(180);
        });
    }
}
