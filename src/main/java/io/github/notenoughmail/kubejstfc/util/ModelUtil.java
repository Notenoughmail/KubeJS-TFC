package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface ModelUtil {

    ResourceLocation DEFAULT_ITEM_PARENT = ResourceLocation.fromNamespaceAndPath("neoforge", "item/default");
    ResourceLocation TFC_EMPTY = KubeJSTFC.tfc("block/empty");
    ResourceLocation CUBE_COLUMN = Helpers.identifierMC("block/cube_column");
    ResourceLocation ORE_COLUMN = KubeJSTFC.tfc("block/ore_column");
    ResourceLocation CROSS = Helpers.identifierMC("block/cross");

    String[] PARTICLE_ALL_TEXTURE_KEYS = { "particle", "all" };

    static void ifNotDefined(KubeAssetGenerator g, BlockBuilder b, Consumer<ModelGenerator> m) {
        g.blockModel(b.id, c -> {
            if (b.parentModel != null) {
                c.parent(b.parentModel);
                c.textures(b.textures);
            } else if (b.modelGenerator != null) {
                b.modelGenerator.accept(c);
            } else {
                m.accept(c);
            }
        });
    }

    static ResourceLocation plainModel(BlockBuilder b) {
        return b.id.withPrefix("block/");
    }

    static void basicItemModelGen(BlockBuilder builder, boolean useBlockParent, ModelGenerator generator) {
        itemModelGen(builder, useBlockParent, generator, m -> {
            m.parent(KubeAssetGenerator.GENERATED_ITEM_MODEL);
            m.textures(builder.itemBuilder.textures);
        });
    }

    static void itemModelGen(BlockBuilder builder, boolean useBlockParent, ModelGenerator generator, Consumer<ModelGenerator> m) {
        if (builder.itemBuilder.modelGenerator != null) {
            builder.itemBuilder.modelGenerator.accept(generator);
        } else if (useBlockParent && builder.parentModel != null) {
            generator.parent(builder.parentModel);
        } else {
            m.accept(generator);
        }
    }

    static void itemModelGen(BlockBuilder builder, ModelGenerator generator, Consumer<ModelGenerator> m) {
        itemModelGen(builder, true, generator, m);
    }

    static void inheritItemModelGen(BlockBuilder builder, boolean useBlockParent, ModelGenerator generator) {
        itemModelGen(builder, useBlockParent, generator, m -> m.parent(builder.id.withPrefix("block/")));
    }
}
