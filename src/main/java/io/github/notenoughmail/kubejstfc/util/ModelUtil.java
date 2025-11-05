package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
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
    ResourceLocation GRASS_INV = KubeJSTFC.tfc("item/grass_inv");
    ResourceLocation FARMLAND = Helpers.identifierMC("block/template_farmland");
    ResourceLocation GRASS_PATH = KubeJSTFC.tfc("block/grass_path");

    String[] PARTICLE_ALL_TEXTURE_KEYS = { "particle", "all" };

    static void ifNotParented(KubeAssetGenerator g, BlockBuilder b, Consumer<ModelGenerator> m) {
        if (b.parentModel == null) ifNotDefined(g, b, m);
    }

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

    static String basicTexture(BuilderBase<?> builder, String registry) {
        return builder.id.getNamespace() + ":" + registry + "/" + builder.id.getPath();
    }

    static ResourceLocation plainModel(BlockBuilder b) {
        return b.parentModel == null ? b.id.withPrefix("block/") : b.parentModel;
    }

    static void defaultTexture(BlockBuilder builder) {
        builder.texture(basicTexture(builder, "block"));
    }

    static void itemModelGen(BlockBuilder builder, boolean useBlockParent, ModelGenerator generator, Consumer<ModelGenerator> m) {
        if (useBlockParent && builder.parentModel != null) {
            generator.parent(builder.parentModel);
        } else if (builder.itemBuilder.modelGenerator != null) {
            builder.itemBuilder.modelGenerator.accept(generator);
        } else {
            m.accept(generator);
        }
    }

    static void itemModelGen(BlockBuilder builder, ModelGenerator generator, Consumer<ModelGenerator> m) {
        itemModelGen(builder, true, generator, m);
    }
}
