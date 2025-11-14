package io.github.notenoughmail.kubejstfc.events.server;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.data.*;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildSpreadingCropBlock;
import net.dries007.tfc.common.blocks.plant.KrummholzBlock;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.world.feature.*;
import net.dries007.tfc.world.feature.cave.ThinSpikeConfig;
import net.dries007.tfc.world.feature.tree.*;
import net.dries007.tfc.world.feature.vein.ClusterVeinConfig;
import net.dries007.tfc.world.feature.vein.DiscVeinConfig;
import net.dries007.tfc.world.feature.vein.PipeVeinConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class KubeTFCWorldgenDataEvent extends KubeDataEvent {

    private static final String
            CONFIGURED = Registries.CONFIGURED_FEATURE.registry().getPath() + "/",
            PLACED = Registries.PLACED_FEATURE.registry().getPath() + "/";

    private final HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> confLookup;

    public KubeTFCWorldgenDataEvent(KubeResourceGenerator gen) {
        super(gen);
        confLookup = gen.getRegistries().access().lookupOrThrow(Registries.CONFIGURED_FEATURE);
    }

    private <FC extends FeatureConfiguration, F extends Feature<FC>> void conf(FC fc, TFCFeatures.Id<F> f, KubeResourceLocation id) {
        conf(fc, f.get(), id);
    }

    private <FC extends FeatureConfiguration, F extends Feature<FC>> void conf(FC fc, F f, KubeResourceLocation id) {
        add(id.wrapped().withPrefix(CONFIGURED), new ConfiguredFeature<>(f, fc), ConfiguredFeature.DIRECT_CODEC);
    }

    private void placed(@Nullable Consumer<FeaturePlacements> placement, KubeResourceLocation id) {
        if (placement != null) {
            placedFeature(id, Holder.Reference.createStandAlone(
                    confLookup,
                    ResourceKey.create(Registries.CONFIGURED_FEATURE, id.wrapped())
            ), placement);
        }
    }

    @Info("Methods for creating vertical anchors")
    public VerticalAnchors anchors = VerticalAnchors.INSTANCE;

    @Info(value = "Create a placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "configuredFeature", value = "The id of the configured feature that will be placed by the placed feature"),
            @Param(name = "placementModifiers", value = "The modifiers and filters to apply to the placement")
    })
    public void placedFeature(KubeResourceLocation id, Holder<ConfiguredFeature<?, ?>> configuredFeature, Consumer<FeaturePlacements> placementModifiers) {
        add(id.wrapped().withPrefix(PLACED), FeaturePlacements.make(configuredFeature, placementModifiers), PlacedFeature.DIRECT_CODEC);
    }

    @Info(value = "Create a configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "type", value = "The configured feature type"),
            @Param(name = "config", value = "The properties of the feature"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void configuredFeature(KubeResourceLocation id, ResourceLocation type, JsonObject config, @Nullable Consumer<FeaturePlacements> placements) {
        gen.json(id.wrapped().withPrefix(CONFIGURED), Assistant.json(j -> {
            j.addProperty("type", type.toString());
            j.add("config", config);
        }));
        placed(placements, id);
    }

    @Info(value = "Create a `tfc:geode` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "outer", value = "The outer block"),
            @Param(name = "middle", value = "The middle block"),
            @Param(name = "inner", value = "The inner blocks"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void geode(KubeResourceLocation id, BlockState outer, BlockState middle, List<Weighted<BlockState>> inner, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new TFCGeodeConfig(outer, middle, Weighted.toVanilla(inner)), TFCFeatures.GEODE, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:boulder` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "states", value = "A mapping between a region's rock block and the blocks to place"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void boulder(KubeResourceLocation id, Map<Block, List<BlockState>> states, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new BoulderConfig(states), TFCFeatures.BOULDER, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:baby_boulder` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "states", value = "A mapping between a region's rock block and the blocks to place"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void babyBoulder(KubeResourceLocation id, Map<Block, List<BlockState>> states, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new BoulderConfig(states), TFCFeatures.BABY_BOULDER, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:thin_spike` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "config", value = "The config values of the feature"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void thinSpike(KubeResourceLocation id, ThinSpikeConfig config, @Nullable Consumer<FeaturePlacements> placement) {
        conf(config, TFCFeatures.THIN_SPIKE, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:cluster_vein` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "veinConfig", value = "The base vein config values"),
            @Param(name = "size", value = "The size of the vein"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void clusterVein(
            KubeResourceLocation id,
            VeinBaseBuilder veinConfig,
            int size,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new ClusterVeinConfig(veinConfig.build(), size), TFCFeatures.CLUSTER_VEIN, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:pipe_vein` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "veinConfig", value = "The base vein config values"),
            @Param(name = "height", value = "The height of the vein"),
            @Param(name = "radius", value = "The radius of the vein's pipe"),
            @Param(name = "minSkew", value = "The minimum skew of the vein"),
            @Param(name = "maxSkew", value = "The maximum skew of the vein"),
            @Param(name = "minSlant", value = "The minimum slant of the vein"),
            @Param(name = "maxSlant", value = "The maximum slant of the vein"),
            @Param(name = "sign", value = "The sign of the vein, in the range [0, 1]"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void pipeVein(
            KubeResourceLocation id,
            VeinBaseBuilder veinConfig,
            int height,
            int radius,
            int minSkew,
            int maxSkew,
            int minSlant,
            int maxSlant,
            float sign,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new PipeVeinConfig(
                veinConfig.build(),
                height,
                radius,
                minSkew,
                maxSkew,
                minSlant,
                maxSlant,
                sign
        ), TFCFeatures.PIPE_VEIN, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:disc_vein` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "veinConfig", value = "The base vein config values"),
            @Param(name = "size", value = "The radius of the disc"),
            @Param(name = "height", value = "The height of the disc"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void discVein(
            KubeResourceLocation id,
            VeinBaseBuilder veinConfig,
            int size,
            int height,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new DiscVeinConfig(veinConfig.build(), size, height), TFCFeatures.DISC_VEIN, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:if_then` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "if", value = "The first feature to attempt to place"),
            @Param(name = "then", value = "The feature to attempt to place if the first succeeds"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void ifThen(KubeResourceLocation id, Holder<PlacedFeature> if_, Holder<PlacedFeature> then, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new IfThenConfig(if_, then), TFCFeatures.IF_THEN, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:soil_disc` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "states", value = "A mapping of soil block to blocks to be placed"),
            @Param(name = "minRadius", value = "The minimum radius of the disc"),
            @Param(name = "maxRadius", value = "The maximum radius of the disc"),
            @Param(name = "height", value = "The height of the disc"),
            @Param(name = "integrity", value = "The percent of blocks to replace within the disc, in the range [0, 1]"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void soilDisc(KubeResourceLocation id, Map<Block, BlockState> states, int minRadius, int maxRadius, int height, float integrity, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new SoilDiscConfig(states, minRadius, maxRadius, height, integrity), TFCFeatures.SOIL_DISC, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:hot_spring` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "fluidState", value = "The fluid to place"),
            @Param(name = "radius", value = "The radius of the spring"),
            @Param(name = "allowUnderwater", value = "If the spring can spawn underwater"),
            @Param(name = "wallState", value = "The block to place as the spring walls, may be null"),
            @Param(name = "decoration", value = "Additional decoration properties"),
            @Param(name = "replaceOnFluidContact", value = "A mapping of blocks to weighted blocks that should be replaced on contact with the fluid, may be null"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void hotSpring(
            KubeResourceLocation id,
            BlockState fluidState,
            int radius,
            boolean allowUnderwater,
            @Nullable BlockState wallState,
            @Nullable FissureDecorationBuilder decoration,
            @Nullable Map<Block, List<Weighted<BlockState>>> replaceOnFluidContact,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new HotSpringConfig(
                Optional.ofNullable(wallState),
                fluidState,
                radius,
                Optional.ofNullable(decoration)
                        .map(FissureDecorationBuilder::builder),
                allowUnderwater,
                Optional.ofNullable(replaceOnFluidContact)
                        .map(Weighted::toTFC)
        ), TFCFeatures.HOT_SPRING, id);
        placed(placement, id);
    }

    @Info(value = "Create a `minecraft:random_patch` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "tries", value = "How many attempts should be made to place the feature"),
            @Param(name = "xzSpread", value = "The horizontal spread of tries"),
            @Param(name = "ySpread", value = "The vertical spread of tries"),
            @Param(name = "feature", value = "The feature to place"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void randomPatch(KubeResourceLocation id, int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new RandomPatchConfiguration(
                tries,
                xzSpread,
                ySpread,
                feature
        ), Feature.RANDOM_PATCH, id);
        placed(placement, id);
    }

    @Info(value = "Create a `minecraft:simple_block` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "stateProvider", value = "The state(s) to place"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void simpleBlock(KubeResourceLocation id, BlockStateProvider stateProvider, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new SimpleBlockConfiguration(stateProvider), Feature.SIMPLE_BLOCK, id);
        placed(placement, id);
    }

    @Info(value = "Create a `minecraft:simple_block` configured feature, placing only a single block, and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "state", value = "The block to place"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void singleBlockState(KubeResourceLocation id, BlockState state, @Nullable Consumer<FeaturePlacements> placement) {
        simpleBlock(id, BlockStateProvider.simple(state), placement);
    }

    @Info(value = "Create a `tfc:spreading_crop` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "crop", value = "The crop block"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void spreadingCrop(KubeResourceLocation id, Block crop, @Nullable Consumer<FeaturePlacements> placement) {
        if (crop instanceof WildSpreadingCropBlock w) {
            conf(new BlockConfig<>(w), TFCFeatures.SPREADING_CROP, id);
        } else {
            throw new IllegalArgumentException("Not a wild spreading crop! %s".formatted(crop));
        }
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:spreading_bush` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "bush", value = "The bush block"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void spreadingBush(KubeResourceLocation id, Block bush, @Nullable Consumer<FeaturePlacements> placement) {
        if (bush instanceof SpreadingBushBlock s) {
            conf(new BlockConfig<>(s), TFCFeatures.SPREADING_BUSH, id);
        } else {
            throw new IllegalArgumentException("Not a spreading bush! %s".formatted(bush));
        }
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:tall_wild_crop` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "crop", value = "The crop block"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void tallWildCrop(KubeResourceLocation id, Block crop, @Nullable Consumer<FeaturePlacements> placement) {
        if (crop instanceof WildDoubleCropBlock d) {
            conf(new BlockConfig<>(d), TFCFeatures.TALL_WILD_CROP, id);
        } else {
            throw new IllegalArgumentException("Not a tall wild crop! %s".formatted(crop));
        }
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:fissure` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "fluid", value = "The fluid to place"),
            @Param(name = "count", value = "The number of fissures to attempt to place"),
            @Param(name = "radius", value = "The range to place fissures within"),
            @Param(name = "minPieces", value = "The minimum number of pieces per fissure"),
            @Param(name = "maxPieces", value = "The maximum number of pieces per fissure"),
            @Param(name = "maxPieceLength", value = "The maximum length an individual piece can be"),
            @Param(name = "minDepth", value = "A `VerticalAnchor`, the minimum depth of the fissure"),
            @Param(name = "wallState", value = "The wall block of the fissures, may be null"),
            @Param(name = "decoration", value = "Additional deocration properties"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void fissure(
            KubeResourceLocation id,
            BlockState fluid,
            int count,
            int radius,
            int minPieces,
            int maxPieces,
            int maxPieceLength,
            VerticalAnchor minDepth,
            @Nullable BlockState wallState,
            @Nullable FissureDecorationBuilder decoration,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new FissureConfig(
                Optional.ofNullable(wallState),
                fluid,
                count,
                radius,
                minDepth,
                minPieces,
                maxPieces,
                maxPieceLength,
                Optional.ofNullable(decoration)
                        .map(FissureDecorationBuilder::builder)
        ), TFCFeatures.FISSURE, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:forest` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "entries", value = "The forest entries to place in the forest"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void forest(KubeResourceLocation id, HolderSet<ConfiguredFeature<?, ?>> entries, @Nullable Consumer<FeaturePlacements> placement) {
        conf(new ForestConfig(entries), TFCFeatures.FOREST, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:forest_entry` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "climate", value = "The climate properties of the entry"),
            @Param(name = "tree", value = "The id of the feature to place as trees"),
            @Param(name = "deadTree", value = "The id of the feature to place as dead trees"),
            @Param(name = "oldGrowthChance", value = "The chance an old growth tree is placed"),
            @Param(name = "spoilerOldGrowthChance", value = "The secondary chance an old growth tree is placed"),
            @Param(name = "fallenTreeChance", value = "The chance a fallen tree is placed"),
            @Param(name = "deadChance", value = "The chance a dead tree is placed"),
            @Param(name = "floating", value = "If trees can be placed 'floating'"),
            @Param(name = "bushLog", value = "The block to place for bush logs, may be null"),
            @Param(name = "bushLeaves", value = "The block to place for bush leaves, may be null"),
            @Param(name = "fallenLog", value = "The block to place for fallen tree logs, may be null"),
            @Param(name = "fallenLeaves", value = "The block to place for fallen tree leaves, may be null"),
            @Param(name = "groundcover", value = "A weighted list of groundcover blocks, may be null"),
            @Param(name = "oldGrowthTree", value = "The id of the feature to place for old growth trees, may be null"),
            @Param(name = "krummholz", value = "The id of the feature to place for krummholz, may be null"),
            @Param(name = "soilDisc", value = "The id of the feature to place for soil discs, may be null")
    })
    public void forestEntry(
            KubeResourceLocation id,
            Consumer<ClimatePlacementBuilder> climate,
            Holder<ConfiguredFeature<?, ?>> tree,
            Holder<ConfiguredFeature<?, ?>> deadTree,
            int oldGrowthChance,
            int spoilerOldGrowthChance,
            int fallenTreeChance,
            int deadChance,
            boolean floating,
            @Nullable BlockState bushLog,
            @Nullable BlockState bushLeaves,
            @Nullable BlockState fallenLog,
            @Nullable BlockState fallenLeaves,
            @Nullable List<Weighted<BlockState>> groundcover,
            @Nullable Holder<ConfiguredFeature<?, ?>> oldGrowthTree,
            @Nullable Holder<ConfiguredFeature<?, ?>> krummholz,
            @Nullable Holder<ConfiguredFeature<?, ?>> soilDisc
    ) {
        conf(new ForestConfig.Entry(
                ClimatePlacementBuilder.make(climate),
                Optional.ofNullable(bushLog),
                Optional.ofNullable(bushLeaves),
                Optional.ofNullable(fallenLog),
                Optional.ofNullable(fallenLeaves),
                Optional.ofNullable(groundcover)
                        .map(Weighted::toTFC),
                tree,
                deadTree,
                Optional.ofNullable(oldGrowthTree),
                Optional.ofNullable(krummholz),
                Optional.ofNullable(soilDisc),
                oldGrowthChance,
                spoilerOldGrowthChance,
                fallenTreeChance,
                deadChance,
                floating
        ), TFCFeatures.FOREST_ENTRY, id);
    }

    @Info(value = "Create a `tfc:overlay_tree` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "base", value = "The id of the base structure"),
            @Param(name = "overlay", value = "The id of the overlay structure"),
            @Param(name = "overlayIntegrity", value = "The percent of the overlay that should be placed, in the range [0, 1]"),
            @Param(name = "treePlacement", value = "Tree placement properties"),
            @Param(name = "trunk", value = "Trunk properties, may be null"),
            @Param(name = "roots", value = "Root properties, may be null"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void overlayTree(
            KubeResourceLocation id,
            ResourceLocation base,
            ResourceLocation overlay,
            float overlayIntegrity,
            TreePlacementConfig treePlacement,
            @Nullable TrunkConfig trunk,
            @Nullable TreeRootBuilder roots,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new OverlayTreeConfig(
                base,
                overlay,
                Optional.ofNullable(trunk),
                overlayIntegrity,
                treePlacement,
                Optional.ofNullable(roots)
                        .map(TreeRootBuilder::build)
        ), TFCFeatures.OVERLAY_TREE, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:random_tree` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "trees", value = "The ids of structures that can be placed as trees"),
            @Param(name = "treePlacement", value = "Tree placement properties"),
            @Param(name = "trunk", value = "Trunk properties, may be null"),
            @Param(name = "roots", value = "Root properties, may be null"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void randomTree(
            KubeResourceLocation id,
            List<ResourceLocation> trees,
            TreePlacementConfig treePlacement,
            @Nullable TrunkConfig trunk,
            @Nullable TreeRootBuilder roots,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new RandomTreeConfig(
                trees,
                Optional.ofNullable(trunk),
                treePlacement,
                Optional.ofNullable(roots)
                        .map(TreeRootBuilder::build)
        ), TFCFeatures.RANDOM_TREE, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:stacked_tree` configured feature and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "layers", value = "The tree layers"),
            @Param(name = "treePlacement", value = "Tree placement properties"),
            @Param(name = "trunk", value = "Trunk properties"),
            @Param(name = "roots", value = "Root properties, may be null"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void stackedTree(
            KubeResourceLocation id,
            List<StackedTreeConfig.Layer> layers,
            TreePlacementConfig treePlacement,
            TrunkConfig trunk,
            @Nullable TreeRootBuilder roots,
            @Nullable Consumer<FeaturePlacements> placement
    ) {
        conf(new StackedTreeConfig(
                layers,
                trunk,
                treePlacement,
                Optional.ofNullable(roots)
                        .map(TreeRootBuilder::build)
        ), TFCFeatures.STACKED_TREE, id);
        placed(placement, id);
    }

    @Info(value = "Create a `tfc:krummholz` configured featue and matching placed feature", params = {
            @Param(name = "id", value = "The id of the feature"),
            @Param(name = "krummholz", value = "The krummholz block"),
            @Param(name = "height", value = "The height of the krummholz"),
            @Param(name = "spawnOnStone", value = "If the krummholz can spawn on stone"),
            @Param(name = "spawnsOnGravel", value = "If the krummholz can spawn on gravel"),
            @Param(name = "placement", value = "The modifiers and filters to apply to the placement")
    })
    public void krummholz(KubeResourceLocation id, Block krummholz, IntProvider height, boolean spawnsOnStone, boolean spawnsOnGravel, @Nullable Consumer<FeaturePlacements> placement) {
        if (krummholz instanceof KrummholzBlock) {
            conf(new KrummholzConfig(
                    krummholz,
                    height,
                    spawnsOnStone,
                    spawnsOnGravel
            ), TFCFeatures.KRUMMHOLZ, id);
        } else {
            throw new IllegalArgumentException("Not a krummholz! %s".formatted(krummholz));
        }
        placed(placement, id);
    }

    public enum VerticalAnchors {
        INSTANCE;

        public VerticalAnchor absolute(int y) {
            return VerticalAnchor.absolute(y);
        }

        public VerticalAnchor aboveBottom(int height) {
            return VerticalAnchor.aboveBottom(height);
        }

        public VerticalAnchor belowTop(int height) {
            return VerticalAnchor.belowTop(height);
        }

        public VerticalAnchor bottom() {
            return VerticalAnchor.bottom();
        }

        public VerticalAnchor top() {
            return VerticalAnchor.top();
        }
    }
}
