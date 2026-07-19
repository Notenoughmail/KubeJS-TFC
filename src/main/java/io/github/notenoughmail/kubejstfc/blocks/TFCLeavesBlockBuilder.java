package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.blocks.sub.FallenLeavesBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.LeavesBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: 2.1.0 | RegistryWood builder that can be applied to ICanUseRegistryWood builders
@ReturnsSelf
public class TFCLeavesBlockBuilder extends LeavesBuilder {

    public transient int autumnIndex;
    @Nullable
    public transient Supplier<Block> twig;
    public transient final DelayedBuilder.NullCapable<FallenLeavesBlockBuilder> fallenLeaves;
    public transient boolean seasonalColors;
    public transient float flowerOffset;
    public transient boolean conifer;
    @Nullable
    public transient BiConsumer<DynamicLeavesModel, ModelGenerator> dynamicModel;

    public TFCLeavesBlockBuilder(ResourceLocation i) {
        super(i);
        fallenLeaves = new DelayedBuilder.NullCapable<>(r -> new FallenLeavesBlockBuilder(r, this), () -> id.withSuffix("_fallen"));
        fallenLeaves.onMarkedNull(BuilderRefs.leafColor::remove);
        seasonalColors = true;
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Info("Sets the properties of the fallen leaves block. May be null to not have fallen leaves")
    public TFCLeavesBlockBuilder fallenLeaves(@Nullable Consumer<FallenLeavesBlockBuilder> fallenLeaves) {
        return fallenLeaves(null, fallenLeaves);
    }

    @Info("Sets the properties of the fallen leaves block. May be null to not have fallen leaves")
    public TFCLeavesBlockBuilder fallenLeaves(@Nullable KubeResourceLocation id, @Nullable Consumer<FallenLeavesBlockBuilder> fallenLeaves) {
        this.fallenLeaves.accept(id, fallenLeaves);
        return this;
    }

    @Info("Sets the twig block to be placed when a natural leaf is broken")
    public TFCLeavesBlockBuilder twig(Holder<Block> twig) {
        this.twig = Assistant.holderAsSupplier(twig);
        return this;
    }

    @Info("Mark the leaves block as being conifer-like")
    public TFCLeavesBlockBuilder confier() {
        conifer = true;
        return this;
    }

    @Info("The fractional offset through the warm season where the block will bloom if using the dynamic leaves model")
    public TFCLeavesBlockBuilder flowerOffset(float offset) {
        flowerOffset = offset;
        return this;
    }

    @Info("Use the `tfc:leaves` model loader to dynamically use different models based on the season")
    public TFCLeavesBlockBuilder dynamicLeavesModel(BiConsumer<DynamicLeavesModel, ModelGenerator> models) {
        this.dynamicModel = models;
        return this;
    }

    @Override
    public Block createObject() {
        // Reg wood is never used outside overridden methods
        return new TFCLeavesBlock(createExtendedProperties().randomTicks().noOcclusion(), null, fallenLeaves.get(), twig) {
            @Override
            public int getAutumnIndex() {
                return autumnIndex;
            }

            @Override
            public float getFlowerOffset() {
                return flowerOffset;
            }

            @Override
            public boolean isConifer() {
                return conifer;
            }
        };
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, fallenLeaves);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        if (dynamicModel == null) {
            ModelUtil.ifNotDefined(generator, this, m -> {
                m.parent(LEAVES);
                m.textures(textures);
            });
        } else {
            generator.blockModel(id, m -> {
                m.parent(null);
                m.custom(j -> {
                    j.addProperty("loader", "tfc:leaves");
                    for (DynamicLeavesModel e : DynamicLeavesModel.VALUES) {
                        j.add(e.type, Assistant.json(i -> i.addProperty(
                                "parent",
                                e.modelEx(this).toString()
                        )));
                    }
                });
            });
            for (DynamicLeavesModel e : DynamicLeavesModel.VALUES) {
                generator.blockModel(e.model(this), m -> {
                    m.parent(LEAVES);
                    m.textures(textures);
                    dynamicModel.accept(e, m);
                });
            }
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.inheritItemModelGen(this, m);
    }

    public enum DynamicLeavesModel implements ISupplyModels {
        DENSE_LEAVES,
        SPARSE_LEAVES,
        BARE,
        BLOOMING
        ;

        public static final DynamicLeavesModel[] VALUES = values();

        public final String type;

        DynamicLeavesModel() {
            this.type = makeStr();
        }

        @Override
        public String str() {
            return type;
        }
    }
}
