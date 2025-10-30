package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.FloodedWildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildSpreadingCropBlock;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class WildCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient Type type;
    public transient Supplier<Supplier<? extends Block>> spreadingFruitBlock;
    @Nullable
    public transient ResourceLocation seedItem, foodItem;
    public transient String @Nullable [] deadModels;

    public WildCropBlockBuilder(ResourceLocation i) {
        super(i);
        spreadingFruitBlock = () -> () -> Blocks.HONEY_BLOCK;
        type = Type.DEFAULT;
        seedItem = null;
        foodItem = null;
        renderType("cutout");
        noCollision();
        BuilderRefs.grassColor.add(this);
    }

    @Override
    @Generics(BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            var item = getOrCreateItemBuilder();
            item.blockBuilder = this;
            i.accept(item);
        }

        return this;
    }

    @Info("Specifies the model to use when the crop is dead/immature, also see doubleDeadModels and spreadingDeadModels")
    public WildCropBlockBuilder deadModel(String model) {
        if (type == Type.DEFAULT || type == Type.FLOODED) {
            deadModels = new String[]{model};
        } else {
            ConsoleJS.STARTUP.warn("WildCropBlockBuilder.deadModel called on a non default or flooded wild crop, please use .doubleDeadModels for double types and .spreadingDeadModels for spreading types");
        }
        return this;
    }

    @Info(value = "Specifies the models to use when the crop is dead/immature. Additionally sets the type tot `double`. Also see deadModel and spreadingDeadModels", params = {
            @Param(name = "topModel", value = "The model for the top block state when the crop is dead"),
            @Param(name = "bottomModel", value = "The model for the bottom block state when the crop is dead")
    })
    public WildCropBlockBuilder doubleDeadModels(String topModel, String bottomModel) {
        type = Type.DOUBLE;
        deadModels = new String[] {topModel, bottomModel};
        return this;
    }

    @Info(value = "Specifies the models to use when the crop is dead/immature. Additionally sets the type to `spreading`. Also see deadModel and doubleDeadModels", params = {
            @Param(name = "coreModel", value = "The model for the non-sided state when the crop is dead"),
            @Param(name = "sideModel", value = "The model for the side block state when the crop is dead")
    })
    public WildCropBlockBuilder spreadingDeadModels(String coreModel, String sideModel) {
        type = Type.SPREADING;
        deadModels = new String[] {coreModel, sideModel};
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks();
    }

    @Info("Sets the block to use as the crop's fruit block, only applicable to the spreading type")
    public WildCropBlockBuilder spreadingFruitBlock(ResourceLocation fruitBlock) {
        spreadingFruitBlock = () -> () -> RegistryInfo.BLOCK.getValue(fruitBlock);
        return this;
    }

    @Info("Sets the type of wild crop being made, may be 'default', 'double', 'flooded', or 'spreading'")
    public WildCropBlockBuilder type(Type type) {
        this.type = type;
        return this;
    }

    @Info("Sets the seeds that the crop drops when broken")
    public WildCropBlockBuilder seeds(ResourceLocation seedItem) {
        this.seedItem = seedItem;
        return this;
    }

    @Info("Sets the food item that the crop drops when broken")
    public WildCropBlockBuilder food(ResourceLocation foodItem) {
        this.foodItem = foodItem;
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("crop", tex);
    }

    @Override
    public Block createObject() {
        return switch (type) {
            case DEFAULT -> new WildCropBlock(createExtendedProperties());
            case DOUBLE -> new WildDoubleCropBlock(createExtendedProperties());
            case FLOODED -> new FloodedWildCropBlock(createExtendedProperties());
            case SPREADING -> new WildSpreadingCropBlock(createExtendedProperties(), spreadingFruitBlock);
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson == null && type == Type.SPREADING) {
            blockstateJson = Util.make(new MultipartBlockStateGenerator(), this::spreadingBlockState).toJson();
        }
        super.generateAssetJsons(generator);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(b -> {
            if (seedItem != null) {
                b.addPool(p -> {
                    p.survivesExplosion();
                    final LootTableEntry item = p.addItem(RegistryInfo.ITEM.getValue(seedItem).getDefaultInstance());
                    if (type == Type.DOUBLE || type == Type.SPREADING) {
                        item.addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("part", "bottom")));
                    }
                });
            }
            if (foodItem != null) {
                b.addPool(p -> {
                    p.survivesExplosion();
                    p.addItem(RegistryInfo.ITEM.getValue(foodItem).getDefaultInstance())
                            .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                                if (type == Type.DOUBLE || type == Type.SPREADING) {
                                    j.addProperty("part", "bottom");
                                }
                                j.addProperty("mature", "true");
                            }))
                            .count(UniformGenerator.between(1, 3));
                });
            }
        }, generator, this);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        // All block state generators are based on the model, or the id if not present
        if (model.isEmpty()) {
            final String base = newID("block/", "").toString();
            switch (type) {
                case DEFAULT, FLOODED -> {
                    generator.blockModel(id, m -> {
                        m.parent("tfc:block/wild_crop/crop");
                        m.textures(textures);
                    });
                }
                case DOUBLE -> {
                    generator.blockModel(newID("", "_top"), m -> {
                        m.parent("block/crop");
                        m.texture("crop", base + "_top");
                    });
                    generator.blockModel(newID("", "_bottom"), m -> {
                        m.parent("tfc:block/wild_crop/crop");
                        m.texture("crop", base + "_bottom");
                    });
                }
                case SPREADING -> {
                    generator.blockModel(id, m -> {
                        m.parent("tfc:block/wild_crop/crop");
                        m.texture("crop", base);
                    });
                    generator.blockModel(newID("", "_side"), m -> {
                        m.parent("tfc:block/crop/spreading_crop_side");
                        m.texture("crop", base + "_side");
                    });
                }
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        if (type != Type.SPREADING) {
            final String baseModel = ResourceUtils.plainModel(this);
            switch (type) {
                case DEFAULT, FLOODED -> {
                    bs.simpleVariant("mature=true", baseModel);
                    bs.simpleVariant("mature=false", deadModels == null ? baseModel : deadModels[0]);
                }
                case DOUBLE -> {
                    final String top = baseModel + "_top";
                    final String bottom = baseModel + "_bottom";
                    bs.simpleVariant("part=top,mature=true", top);
                    bs.simpleVariant("part=top,mature=false", deadModels == null ? top : deadModels[0]);
                    bs.simpleVariant("part=bottom,mature=true", bottom);
                    bs.simpleVariant("part=bottom,mature=false", deadModels == null ? bottom : deadModels[1]);
                }
            }
        }
    }

    private void spreadingBlockState(MultipartBlockStateGenerator ms) {
        final String baseModel = ResourceUtils.plainModel(this);
        final String side = baseModel + "_side";
        ms.part("mature=true", baseModel);
        ms.part("mature=false", deadModels == null ? baseModel : deadModels[0]);
        ms.part("east=true,mature=true", p -> p.model(side).y(90));
        ms.part("east=true,mature=false", p -> p.model(deadModels == null ? side : deadModels[1]).y(90));
        ms.part("north=true,mature=true", side);
        ms.part("north=true,mature=false", deadModels == null ? side : deadModels[1]);
        ms.part("south=true,mature=true", p -> p.model(side).y(180));
        ms.part("south=true,mature=false", p -> p.model(deadModels == null ? side : deadModels[1]).y(180));
        ms.part("west=true,mature=true", p -> p.model(side).y(270));
        ms.part("west=true,mature=false", p -> p.model(deadModels == null ? side : deadModels[1]).y(270));
    }

    public enum Type {
        DEFAULT,
        DOUBLE,
        FLOODED,
        SPREADING
    }
}
