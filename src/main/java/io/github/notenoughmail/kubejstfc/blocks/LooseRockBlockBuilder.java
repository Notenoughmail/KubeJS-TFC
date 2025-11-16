package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class LooseRockBlockBuilder extends BlockBuilder {

    public transient int rotate;
    public transient String rockCategory;
    public transient BiConsumer<PebbleCount, ModelGenerator> models;

    public LooseRockBlockBuilder(ResourceLocation i) {
        super(i);
        rotate = 0;
        noCollision = true;
        rockCategory = "metamorphic";
        models = (c, m) -> {
            m.parent(c.parentModel(rockCategory));
            m.textures(textures);
        };
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Info("Rotates the models by the given amount")
    public LooseRockBlockBuilder rotateModel(int i) {
        rotate = i;
        return this;
    }

    @Info("Makes the block collide with entities")
    public LooseRockBlockBuilder collision() {
        noCollision = false;
        return this;
    }

    @Info("sets the rock category the block model should use, may be 'igneous_extrusive', 'igneous_intrusive', 'metamorphic', or 'sedimentary'")
    public LooseRockBlockBuilder rockCategory(String s) {
        rockCategory = s;
        return this;
    }

    @Info("""
            Sets the model generation of the loose rock block, accepts a `BiConsumer` of a `PebbleCount` and a model generator.
            The generator is unique for each type.
            
            There are three types: `ONE`, `TWO`, and `THREE` with a `.count` property which returns the number of pebbles the
            `PebbleCount` represents.
            """)
    public LooseRockBlockBuilder models(BiConsumer<PebbleCount, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public LooseRockBlock createObject() {
        return new LooseRockBlock(createProperties());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (PebbleCount c : PebbleCount.VALUES) {
            generator.blockModel(c.model(this), m -> models.accept(c, m));
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (PebbleCount c : PebbleCount.VALUES) {
            final ResourceLocation m = c.modelEx(this);
            bs.variant("count=" + c.str(), v -> {
                v.model(m).y(rotate);
                v.model(m).y(90 + rotate);
                v.model(m).y(180 + rotate);
                v.model(m).y(270 + rotate);
            });
        }
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.determinedSinglePool(this, p -> {
            p.apply(ApplyExplosionDecay.explosionDecay())
                    .apply(LootUtil.count(2F)
                            .when(LootUtil.withState(get(), b -> b.hasProperty(LooseRockBlock.COUNT, 2))))
                    .apply(LootUtil.count(3F)
                            .when(LootUtil.withState(get(), b -> b.hasProperty(LooseRockBlock.COUNT, 3))));
        });
    }

    public enum PebbleCount implements ISupplyModels {
        ONE,
        TWO,
        THREE;

        static final PebbleCount[] VALUES = values();

        private final String str;
        public final int count;

        PebbleCount() {
            count = ordinal() + 1;
            str = Integer.toString(count);
        }

        @Override
        public String str() {
            return str;
        }

        public ResourceLocation parentModel(String rockCategory) {
            return KubeJSTFC.id("block/ground_cover/loose/" + rockCategory + "_" + str);
        }
    }
}
