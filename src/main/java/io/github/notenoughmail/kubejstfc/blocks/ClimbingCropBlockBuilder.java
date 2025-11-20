package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.CropUtil;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.crop.ClimbingCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ClimbingCropBlockBuilder extends DoubleCropBlockBuilder {

    public static final ResourceLocation STICK = KubeJSTFC.tfc("block/crop/stick");

    public transient BiConsumer<Integer, ModelGenerator> stickModels;

    public ClimbingCropBlockBuilder(ResourceLocation i) {
        super(i, Type.CLIMBING);
        BuilderRefs.ghostRendering.add(this);
        renderType(null);
        stickModels = (a, m) -> {
            m.parent(ModelUtil.CROP);
            m.textures(textures);
        };
    }

    @Info("Accepts a `BiConsumer` of a number, representing the age, and a model generator. The model generator is unique for each age")
    public ClimbingCropBlockBuilder stickModels(BiConsumer<Integer, ModelGenerator> models) {
        stickModels = stickModels.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtil.climbingCrop(this);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.fullTable(null, t -> {
            LootUtil.pool(t, p -> {
                LootUtil.survivesExplosion(p);
                p.add(LootItem.lootTableItem(seeds.get())
                        .when(LootUtil.withState(get(), s -> s.hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                );
            });
            final Item prod = getProduct();
            if (prod != null) {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(LootItem.lootTableItem(prod)
                            .when(LootUtil.withState(get(), s ->
                                    s.hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                                            .hasProperty(getAges(), ages + doubleAges)))
                            .apply(LootUtil.count(new CropYieldProvider(
                                    ConstantValue.exactly(0F),
                                    UniformGenerator.between(6F, 10F)
                            )))
                    );
                });
            }
            LootUtil.pool(t, p -> {
                LootUtil.survivesExplosion(p);
                p.add(LootItem.lootTableItem(Items.STICK)
                        .when(LootUtil.withState(get(), s ->
                                s.hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                                        .hasProperty(ClimbingCropBlock.STICK, true)))
                );
            });
        });
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (int i = 0 ; i <= ages + doubleAges ; i++) {
            final int age = i;
            if (age < ages) {
                generator.blockModel(id.withSuffix("_age_" + age), m -> models.accept(age, m));
                generator.blockModel(id.withSuffix("_age_" + age + "_stick"), m -> stickModels.accept(age, m));
            } else {
                generator.blockModel(id.withSuffix("_age_" + age + "_bottom"), m -> doubleModels.apply(age, true, m));
                generator.blockModel(id.withSuffix("_age_" + age + "_top"), m -> doubleModels.apply(age, false, m));
            }
        }
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (int age = 0 ; age <= ages + doubleAges ; age++) {
            final String baseKey = "age=" + age;
            if (age < ages) {
                bs.simpleVariant(baseKey + ",stick=false", newID("block/", "_age_" + age));
                bs.simpleVariant(baseKey + ",stick=true,part=bottom", newID("block/", "_age_" + age + "_stick"));
                bs.simpleVariant(baseKey + ",stick=true,part=top", STICK);
            } else {
                bs.simpleVariant(baseKey + ",part=bottom", newID("block/", "_age_" + age + "_bottom"));
                bs.simpleVariant(baseKey + ",part=top", newID("block/", "_age_" + age + "_top"));
            }
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> g.parent(newID("block/", "_age_" + (ages + doubleAges - 1) + "_bottom")));
    }
}
