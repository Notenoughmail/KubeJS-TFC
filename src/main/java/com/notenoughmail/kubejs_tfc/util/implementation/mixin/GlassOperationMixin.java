package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.notenoughmail.kubejs_tfc.event.CreateGlassOperationsEventJS;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.CustomGlassOperations;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.blocks.Gem;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Purpose:</b><p>
 * Enables custom glass operation creation with custom heat and sounds
 */
@Mixin(value = GlassOperation.class, remap = false)
public abstract class GlassOperationMixin {

    @Unique
    @Nullable
    private static CreateGlassOperationsEventJS kubejs_tfc$CreateEvent;

    @Invoker(value = "<init>", remap = false)
    static GlassOperation create(String name, int index) {
        throw new IllegalStateException("Unreachable");
    }

    @Mutable
    @Shadow(remap = false)
    @Final
    private static GlassOperation[] $VALUES;

    @Mutable
    @Shadow(remap = false)
    @Final
    public static GlassOperation[] VALUES;

    static {
        if (EventHandlers.createGlassOperations.hasListeners()) {
            //noinspection Convert2MethodRef
            kubejs_tfc$CreateEvent = new CreateGlassOperationsEventJS($VALUES.length, (n, o) -> create(n, o));
            EventHandlers.createGlassOperations.post(kubejs_tfc$CreateEvent);
            $VALUES = ArrayUtils.addAll($VALUES, kubejs_tfc$CreateEvent.created.toArray(GlassOperation[]::new));
            // Not a clone because mixin was having problems with the clone for some reason
            VALUES = new GlassOperation[$VALUES.length];
            System.arraycopy($VALUES, 0, VALUES, 0, $VALUES.length);
        }

        CustomGlassOperations.lock();
    }

    @Inject(method = "lambda$static$0", remap = false, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;", ordinal = 17, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void kubejs_tfc$AddPowders(CallbackInfoReturnable<Map<?,?>> cir, ImmutableMap.Builder<Item, GlassOperation> builder) {
        if (kubejs_tfc$CreateEvent != null && !kubejs_tfc$CreateEvent.powders.isEmpty()) {
            final List<Item> validation = new ArrayList<>();
            validation.add(TFCItems.POWDERS.get(Powder.SODA_ASH).get());
            validation.add(TFCItems.POWDERS.get(Powder.SULFUR).get());
            validation.add(TFCItems.POWDERS.get(Powder.GRAPHITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.HEMATITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.LIMONITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.MAGNETITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.NATIVE_GOLD).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.NATIVE_COPPER).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.MALACHITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.TETRAHEDRITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.CASSITERITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.GARNIERITE).get());
            validation.add(TFCItems.ORE_POWDERS.get(Ore.NATIVE_SILVER).get());
            validation.add(TFCItems.GEM_DUST.get(Gem.AMETHYST).get());
            validation.add(TFCItems.GEM_DUST.get(Gem.RUBY).get());
            validation.add(TFCItems.GEM_DUST.get(Gem.LAPIS_LAZULI).get());
            validation.add(TFCItems.GEM_DUST.get(Gem.PYRITE).get());
            validation.add(TFCItems.GEM_DUST.get(Gem.SAPPHIRE).get());

            for (var entry : kubejs_tfc$CreateEvent.powders.entrySet()) {
                final Item powder = RegistryInfo.ITEM.getValue(entry.getKey());
                if (powder == null || powder == Items.AIR) {
                    ConsoleJS.SERVER.error("Unknown item for glass operation powder: %s. Skipping...".formatted(entry.getKey()));
                } else if (validation.contains(powder)) {
                    ConsoleJS.SERVER.error("Item '%s' already exists as a powder! Skipping...".formatted(entry.getKey()));
                } else {
                    validation.add(powder);
                    builder.put(powder, entry.getValue());
                }
            }
        }
    }

    @ModifyReturnValue(method = "getSound", remap = false, at = @At("RETURN"))
    private SoundEvent kubejs_tfc$CustomSound(SoundEvent prev) {
        if (prev == SoundEvents.ANVIL_USE) {
            final SoundEvent custom = CustomGlassOperations.getSound((GlassOperation) (Object) this);
            return custom == null ? prev : custom;
        }
        return prev;
    }

    @ModifyExpressionValue(method = "hasRequiredTemperature", remap = false, at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/common/capabilities/heat/Heat;getMin()F", remap = false))
    private float kubejs_tfc$CustomHeat(float original) {
        final float custom = CustomGlassOperations.getMinHeat((GlassOperation) (Object) this);
        return custom == Float.NEGATIVE_INFINITY ? original : custom;
    }
}
