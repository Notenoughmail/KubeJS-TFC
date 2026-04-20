package io.github.notenoughmail.kubejstfc.compat.worldjs;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.BuilderFactory;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.RecordDefaultsRegistry;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.BoulderBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.GeodeBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.ThinSpikeBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.IfThenBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.SoilDiscBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.HotSpringBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.FloodFillLakeBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.CaveVegetationBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.FissureBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.BlockConfigBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.vein.ClusterVeinBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.vein.PipeVeinBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.vein.DiscVeinBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest.ForestBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest.ForestEntryBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest.OverlayTreeBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest.RandomTreeBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest.StackedTreeBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest.KrummholzBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.ClimatePlacementBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.TreeRootBuilder;
import io.github.notenoughmail.worldjs.PlacedFeatureModifierEvent;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildSpreadingCropBlock;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.util.collections.Weighted;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.dries007.tfc.world.placement.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForge;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.github.notenoughmail.kubejstfc.KubeJSTFC.tfc;

public class WorldgenPlugin implements KubeJSPlugin {

    public static <T> IWeighted<T> weightedTFC(List<WeightedValue<T>> values) {
        return switch (values.size()) {
            case 0 -> IWeighted.empty();
            case 1 -> IWeighted.singleton(values.getFirst().value());
            default -> new Weighted<>(
                    values.stream()
                            .map(w -> Pair.of(w.value(), (double) w.weight()))
                            .toList()
            );
        };
    }

    public static <K, V> Map<K, IWeighted<V>> weightedTFC(Map<K, List<WeightedValue<V>>> map) {
        final Map<K, IWeighted<V>> ret = new LinkedHashMap<>();
        map.forEach((k, l) -> ret.put(k, weightedTFC(l)));
        return ret;
    }

    @Override
    public void init() {
        NeoForge.EVENT_BUS.addListener(this::addPlacementModifiers);
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.CONFIGURED_FEATURE, c -> {
            cf(c, tfc("boulder"), BoulderBuilder.class, r -> new BoulderBuilder(r, TFCFeatures.BOULDER));
            cf(c, tfc("baby_boulder"), BoulderBuilder.class, r -> new BoulderBuilder(r, TFCFeatures.BABY_BOULDER));
            cf(c, tfc("geode"), GeodeBuilder.class, GeodeBuilder::new);
            cf(c, tfc("thin_spike"), ThinSpikeBuilder.class, ThinSpikeBuilder::new);
            cf(c, tfc("if_then"), IfThenBuilder.class, IfThenBuilder::new);
            cf(c, tfc("soil_disc"), SoilDiscBuilder.class, SoilDiscBuilder::new);
            cf(c, tfc("hot_spring"), HotSpringBuilder.class, HotSpringBuilder::new);
            cf(c, tfc("flood_fill_lake"), FloodFillLakeBuilder.class, FloodFillLakeBuilder::new);
            cf(c, tfc("cave_vegetation"), CaveVegetationBuilder.class, CaveVegetationBuilder::new);
            cf(c, tfc("fissure"), FissureBuilder.class, FissureBuilder::new);
            cf(c, tfc("cluster_vein"), ClusterVeinBuilder.class, ClusterVeinBuilder::new);
            cf(c, tfc("pipe_vein"), PipeVeinBuilder.class, PipeVeinBuilder::new);
            cf(c, tfc("disc_vein"), DiscVeinBuilder.class, DiscVeinBuilder::new);
            cf(c, tfc("spreading_crop"), BlockConfigBuilder.class, r -> new BlockConfigBuilder<>(r, WildSpreadingCropBlock.class, TFCFeatures.SPREADING_CROP));
            cf(c, tfc("spreading_bush"), BlockConfigBuilder.class, r -> new BlockConfigBuilder<>(r, SpreadingBushBlock.class, TFCFeatures.SPREADING_BUSH));
            cf(c, tfc("tall_wild_crop"), BlockConfigBuilder.class, r -> new BlockConfigBuilder<>(r, WildDoubleCropBlock.class, TFCFeatures.TALL_WILD_CROP));
            cf(c, tfc("forest"), ForestBuilder.class, ForestBuilder::new);
            cf(c, tfc("forest_entry"), ForestEntryBuilder.class, ForestEntryBuilder::new);
            cf(c, tfc("overlay_tree"), OverlayTreeBuilder.class, OverlayTreeBuilder::new);
            cf(c, tfc("random_tree"), RandomTreeBuilder.class, RandomTreeBuilder::new);
            cf(c, tfc("stacked_tree"), StackedTreeBuilder.class, StackedTreeBuilder::new);
            cf(c, tfc("krummholz"), KrummholzBuilder.class, KrummholzBuilder::new);
        });
    }

    private static <C, B extends BuilderBase<? extends C>> void add(BuilderTypeRegistry.Callback<C> callback, ResourceLocation type, Class<B> builderType, BuilderFactory factory) {
        callback.add(type, builderType, factory);
    }

    private static <C extends ConfiguredFeatureBuilder<FC>, FC extends FeatureConfiguration> void cf(BuilderTypeRegistry.Callback<ConfiguredFeature<?, ?>> callback, ResourceLocation type, Class<C> builderType, Function<ResourceLocation, C> factory) {
        add(callback, type, builderType, factory::apply);
    }

    @Override
    public void registerRecordDefaults(RecordDefaultsRegistry registry) {
        registry.register(new TrunkConfig(Blocks.AIR.defaultBlockState(), 0, 2, false));
        registry.register(new TreePlacementConfig(5, 3, TreePlacementConfig.GroundType.NORMAL));
        registry.register(new TreeRootBuilder(Map.of(), 6, 3, 5, null, false));
    }

    private void addPlacementModifiers(PlacedFeatureModifierEvent event) {

        final PlacedFeatureModifierEvent.Args.Arg codd, nfr, iie, iae, cmi, cma, cms;

        final TypeInfo verticalAnchor = TypeInfo.of(VerticalAnchor.class);

        event.namespace(TerraFirmaCraft.MOD_ID)
                .unit(
                        "underground",
                        new UndergroundPlacement(),
                        "Add a 'tfc:underground' modifier"
                )
                .unit(
                        "noSolidNeighbors",
                        new NoSolidNeighborsPlacement(),
                        "Add a 'tfc:no_solid_neighbors' modifier"
                )
                .registerSingleArg(
                        "volcano",
                        codd = event.singleArg("distance", float.class, "The normalized distance from a noise center the position must be in order to generate"),
                        centerOrDistDist(CinderConePlacement::new),
                        "Add a 'tfc:volcano' modifier"
                )
                .unit(
                        "volcanoCenter",
                        new CinderConePlacement(true, 0f),
                        "Add a 'tfc:volcano' modifier which only accepts positions at the center"
                )
                .registerSingleArg(
                        "tuffCone",
                        codd,
                        centerOrDistDist(TuffRingPlacement::new),
                        "Add a 'tfc:tuff_cone' modifier"
                )
                .unit(
                        "tuffConeCenter",
                        new TuffRingPlacement(true, 0f),
                        "Add a 'tfc:tuff_cone' modifier which only accepts positions at the center"
                )
                .registerSingleArg(
                        "tuya",
                        codd,
                        centerOrDistDist(TuffRingPlacement::new),
                        "Add a 'tfc:tuya' modifier"
                )
                .unit(
                        "tuyaCenter",
                        new TuyaPlacement(true, 0f),
                        "Add a 'tfc:tuya' modifier which only accepts positions at the center"
                )
                .register(
                        "shallowWater",
                        event.arg("minDepth", int.class, "The minimum depth of water required")
                                .arg("maxDepth", int.class, "The maximum depth of water permitted"),
                        a -> {
                            final int min = i(a[0]), max = i(a[1]);
                            if (min < 1 || max < 1)
                                throw new IllegalArgumentException("'minDepth' and 'maxDepth' must be positive");
                            return new ShallowWaterPlacement(min, max);
                        },
                        "Add a 'tfc:shallow_water' modifier"
                )
                .register(
                        "nearFluid",
                        event.arg(nfr = event.singleArg("radius", int.class, "The radius to check for fluid"))
                                .arg("fluids", TypeInfo.RAW_LIST.withParams(TypeInfo.of(Fluid.class)), "The fluids to consider"),
                        a -> {
                            final int r = i(a[0]);
                            if (r < 0)
                                throw new IllegalArgumentException("'radius' must be non-negative");
                            return new NearFluidPlacement(r, c(a[1]));
                        },
                        "Add a 'tfc:near_fluid' modifier"
                )
                .<Integer>registerSingleArg(
                        "nearFluid",
                        nfr,
                        i -> {
                            if (i < 0)
                                throw new IllegalArgumentException("'radius' must be non-negative");
                            return new NearFluidPlacement(i, null);
                        },
                        "Add a 'tfc:near_fluid' modifier that considers all fluids"
                )
                .register(
                        "intertidal",
                        event.arg(iie = event.singleArg("minElevation", int.class, "The minimum valid tide elevation"))
                                .arg(iae = event.singleArg("maxElevation", int.class, "The maximum valid tide elevation")),
                        a -> new IntertidalPlacement(i(a[0]), i(a[1])),
                        "Add a 'tfc:intertidal' modifier"
                )
                .<Integer>registerSingleArg(
                        "intertidalMin",
                        iie,
                        i -> new IntertidalPlacement(i, 320),
                        "Add a 'tfc:intertidal' modifier which is unbounded on the top"
                )
                .<Integer>registerSingleArg(
                        "intertidalMax",
                        iae,
                        i -> new IntertidalPlacement(-64, i),
                        "Add a 'tfc:intertidal' modifier which is unbounded on the bottom"
                )
                .register(
                        "flatEnough",
                        event.arg("flatness", float.class, "The minimum flatness of the checked area")
                                .arg("radius", int.class, "The horizontal distance to check")
                                .arg("maxDepth", int.class, "The depth below the initial position to check"),
                        a -> {
                            final float f = f(a[0]);
                            final int r = i(a[1]), d = i(a[2]);
                            if (f < 0 || f > 1)
                                throw new IllegalArgumentException("'flatness' must be in the range [0, 1]");
                            if ( r < 1 || d < 1)
                                throw new IllegalArgumentException("'radius' and 'maxDepth' must be positive");
                            return new FlatEnoughPlacement(f, r, d);
                        },
                        "Add a 'tfc:flat_enough' modifier"
                )
                .registerSingleArg(
                        "climate",
                        "climate",
                        TypeInfo.RAW_CONSUMER.withParams(TypeInfo.of(ClimatePlacementBuilder.class)),
                        "The climate restrictions",
                        ClimatePlacementBuilder::make,
                        "Add a 'tfc:climate' modifier"
                )
                .register(
                        "carvingMask",
                        event.arg(cmi = event.singleArg("minY", verticalAnchor, "The minimum valid vertical bounds"))
                                .arg(cma = event.singleArg("maxY", verticalAnchor, "The maximum valid vertical bounds"))
                                .arg(cms = event.singleArg("step", GenerationStep.Carving.class, "The carving step volume to check")),
                        a -> new BoundedCarvingMaskPlacement(c(a[0]), c(a[1]), c(a[2])),
                        "Add a 'tfc:carving_mask' modifier"
                )
                .register(
                        "carvingMaskMin",
                        event.arg(cmi).arg(cms),
                        a -> new BoundedCarvingMaskPlacement(c(a[0]), VerticalAnchor.top(), c(a[1])),
                        "Add a 'tfc:carving_mask' modifier which is unbounded above"
                )
                .register(
                        "carvingMaskMax",
                        event.arg(cma).arg(cms),
                        a -> new BoundedCarvingMaskPlacement(VerticalAnchor.bottom(), c(a[0]), c(a[1])),
                        "Add a 'tfc:carving_mask' modifier which is unbounded below"
                )
                .registerSingleArg(
                        "onTop",
                        "predicate",
                        BlockPredicate.class,
                        "The block required below for a position to be valid",
                        OnTopPlacement::new,
                        "Add a 'tfc:on_top' modifier"
                )
        ;
    }

    private static int i(Object o) {
        return (int) o;
    }

    private static float f(Object o) {
        return (float) o;
    }

    private static <T> T c(Object o) {
        return Cast.to(o);
    }

    private static <M extends CenterOrDistanceToPlacement<?>> PlacedFeatureModifierEvent.Method<Float> centerOrDistDist(BiFunction<Boolean, Float, M> constructor) {
        return f -> {
            if (f > 1 || f < 0)
                throw new IllegalArgumentException("'distance' must be in the range [0, 1]");
            return constructor.apply(false, f);
        };
    }
}
