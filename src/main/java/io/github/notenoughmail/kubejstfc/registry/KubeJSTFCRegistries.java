package io.github.notenoughmail.kubejstfc.registry;

import com.mojang.serialization.MapCodec;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.RockSurfaceRuleSource;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import io.github.notenoughmail.kubejstfc.util.commands.Range;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.commands.TreeSolver;
import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class KubeJSTFCRegistries {

    public static void init(IEventBus modBus) {
        COMMAND_ARGS.register(modBus);
        CHUNK_GENERATOR.register(modBus);
        SURFACE_RULE_SOURCE.register(modBus);
        DATA_TYPE.register(modBus);
    }

    public static final ResourceKey<? extends Registry<DataType<?>>> DATA_TYPE_KEY = ResourceKey.createRegistryKey(KubeJSTFC.id("data_type"));
    public static final Registry<DataType<?>> DATA_TYPES = new RegistryBuilder<>(DATA_TYPE_KEY).create();

    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGS = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, KubeJSTFC.ID);
    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = DeferredRegister.create(Registries.CHUNK_GENERATOR, KubeJSTFC.ID);
    private static final DeferredRegister<MapCodec<? extends SurfaceRules.RuleSource>> SURFACE_RULE_SOURCE = DeferredRegister.create(Registries.MATERIAL_RULE, KubeJSTFC.ID);
    private static final DeferredRegister<DataType<?>> DATA_TYPE = DeferredRegister.create(DATA_TYPE_KEY, TerraFirmaCraft.MOD_ID);

    static {
        COMMAND_ARGS.register("range", () -> ArgumentTypeInfos.registerByClass(
                Range.RangeArgumentType.class,
                SingletonArgumentInfo.contextFree(Range::arg)
        ));
        COMMAND_ARGS.register("tree_solver", () -> ArgumentTypeInfos.registerByClass(
                TreeSolver.ArgType.class,
                TreeSolver.TYPE_INFO
        ));
        COMMAND_ARGS.register("data_type", () -> ArgumentTypeInfos.registerByClass(
                DataType.Argument.class,
                DataType.TYPE_INFO
        ));
        SURFACE_RULE_SOURCE.register("rock", RockSurfaceRuleSource.CODEC::codec);
    }
}
