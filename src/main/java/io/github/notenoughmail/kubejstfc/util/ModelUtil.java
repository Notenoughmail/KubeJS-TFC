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

    String[] PARTICLE_ALL_TEXTURE_KEYS = { "particle", "all" };

    static void ifNotParented(KubeAssetGenerator g, BlockBuilder b, Consumer<ModelGenerator> m) {
        if (b.parentModel == null) ifNotDefined(g, b, m);
    }

    static void ifNotDefined(KubeAssetGenerator g, BlockBuilder b, Consumer<ModelGenerator> m) {
        g.blockModel(b.id, c -> {
            if (b.modelGenerator == null) {
                m.accept(c);
            } else {
                b.modelGenerator.accept(c);
            }
        });
    }

    static ResourceLocation plainModel(BlockBuilder b) {
        return b.parentModel == null ? b.id.withPrefix("block/") : b.parentModel;
    }

    static void defaultTexture(BlockBuilder builder) {
        builder.texture(builder.id.getNamespace() + ":block/" + builder.id.getPath());
    }

    static void itemModelGen(BlockBuilder builder, ModelGenerator generator, Consumer<ModelGenerator> m) {
        if (builder.parentModel != null) {
            generator.parent(builder.parentModel);
        } else if (builder.itemBuilder.modelGenerator != null) {
            builder.itemBuilder.modelGenerator.accept(generator);
        } else {
            m.accept(generator);
        }
    }
}
