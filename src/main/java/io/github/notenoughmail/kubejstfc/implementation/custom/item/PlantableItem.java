package io.github.notenoughmail.kubejstfc.implementation.custom.item;

import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.items.PlantableInfo;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class PlantableItem extends BlockItem implements PlantableInfo {

    public static Item crop(Block block, Properties properties, PlantNutrients nutrients, Supplier<ClimateRange> climate) {
        return new PlantableItem(block, properties, nutrients, climate, null, null, true);
    }

    public static Item bush(Block block, Properties properties, Supplier<ClimateRange> climate, Lifecycle[] lifecycleInfo) {
        return new PlantableItem(block, properties, null, climate, List.of(lifecycleInfo), null, false);
    }

    @Nullable
    private final PlantNutrients nutrients;
    private final Supplier<ClimateRange> climate;
    @Nullable
    private final List<Lifecycle> lifecycleInfo;
    private final IntSupplier growthTime;
    private final boolean uniqueDesc;

    public PlantableItem(Block block, Properties properties, @Nullable PlantNutrients nutrients, @Nullable Supplier<ClimateRange> climate, @Nullable List<Lifecycle> lifecycleInfo, @Nullable IntSupplier growthTime, boolean uniqueDesc) {
        super(block, properties);
        this.nutrients = nutrients;
        this.climate = climate == null ? () -> null : climate;
        this.lifecycleInfo = lifecycleInfo;
        this.growthTime = growthTime == null ? () -> -1 : growthTime;
        this.uniqueDesc = uniqueDesc;
    }

    @Override
    public String getDescriptionId() {
        return uniqueDesc ? getOrCreateDescriptionId() : super.getDescriptionId();
    }

    @Override
    @Nullable
    public PlantNutrients getNutrientsInfo() {
        return nutrients;
    }

    @Override
    @Nullable
    public ClimateRange getClimateRangeInfo() {
        return climate.get();
    }

    @Override
    @Nullable
    public List<Lifecycle> getLifecycleInfo() {
        return lifecycleInfo;
    }

    @Override
    public int getGrowthTimeInfo() {
        return growthTime.getAsInt();
    }
}
