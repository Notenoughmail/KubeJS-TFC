package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
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

    public transient boolean naturallySupported, uniqueSideTextures;
    @Nullable
    public transient Component rockTypeTooltip;

    public RawRockBlockBuilder(ResourceLocation i) {
        super(i);
        naturallySupported = false;
        uniqueSideTextures = false;
        rockTypeTooltip = null;
        notSolid = false; // Super class sets this to true
        itemBuilder.parentModel = id.getNamespace() + ":block/" + id.getPath();
    }

    @Info(value = "Makes the default model generator use the 'side' and 'end' textures instead of just the 'end'")
    public RawRockBlockBuilder uniqueSideTextures() {
        uniqueSideTextures = true;
        return this;
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
    public RawRockBlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        texture("all", tex);
        texture("side", tex);
        texture("end", tex);
        return this;
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        if (!model.isEmpty()) {
            ResourceUtils.hasModel(generator, this);
            generator.blockModel(newID("", "_mirrored"), m -> m.parent(model));
        } else {
            generator.blockModel(id, m -> {
                m.parent(uniqueSideTextures ? "block/cube_column" : "block/cube_all");
                m.textures(textures);
            });
            generator.blockModel(newID("", "_mirrored"), m -> {
                m.parent(uniqueSideTextures ? "block/cube_column" : "block/cube_all");
                m.textures(textures);
            });
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String modelId = id.getNamespace() + ":block/" + id.getPath();
        final String mirrorId = modelId + "_mirrored";
        bs.variant("axis=x", v -> {
            v.model(modelId).x(90).y(90);
            v.model(mirrorId).x(90).y(90);
        });
        bs.variant("axis=y", v -> {
            v.model(modelId);
            v.model(mirrorId);
            v.model(modelId).y(180);
            v.model(mirrorId).y(180);
        });
        bs.variant("axis=z", v -> {
            v.model(modelId).x(90);
            v.model(mirrorId).x(90);
            v.model(modelId).x(90).y(180);
            v.model(mirrorId).x(90).y(180);
        });
    }
}
