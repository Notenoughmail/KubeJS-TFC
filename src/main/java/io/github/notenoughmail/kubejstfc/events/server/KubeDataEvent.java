package io.github.notenoughmail.kubejstfc.events.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.*;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public class KubeDataEvent implements KubeEvent {

    private final KubeResourceGenerator gen;

    public static String makePath(Object path) {
        String out;
        if (path instanceof CharSequence s) {
            out = s.toString();
        } else {
            try {
                final byte[] bytes = String.valueOf(path).getBytes(StandardCharsets.UTF_8);
                final MessageDigest digest = MessageDigest.getInstance("MD5");
                out = new BigInteger(HexFormat.of().formatHex(digest.digest(bytes)), 16).toString(36);
            } catch (Exception e) {
                out =  Integer.toHexString(path.hashCode());
            }
        }
        out = out.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_", "")
                .replaceAll("_$", "");
        return out.length() > 64 ? out.substring(0, 64).replaceAll("_$", "") : out;
    }

    static <T> ResourceLocation id(@Nullable ResourceLocation id, T t, Function<T, String> func, String prefix) {
        return (id == null ? KubeJSTFC.id(func.apply(t)) : id).withPrefix(prefix + "/");
    }

    public KubeDataEvent(KubeResourceGenerator gen) {
        this.gen = gen;
    }

    private <T> void add(ResourceLocation id, T t, Codec<T> codec) {
        gen.json(id, codec.encodeStart(JsonOps.INSTANCE, t).getOrThrow());
    }

    private <T> void add(T t, Codec<T> codec, @Nullable ResourceLocation id, Function<T, String> func, String prefix) {
        add(id(id, t, func, prefix), t, codec);
    }

    private <T> void add(T t, Codec<T> codec, @Nullable ResourceLocation id, String prefix) {
        add(t, codec, id, KubeDataEvent::makePath, prefix);
    }

    public void entityDamageResistance(EntityDamageResistance resistance, @Nullable ResourceLocation id) {
        add(resistance, EntityDamageResistance.CODEC, id, r -> r.entity().location().toString().replace(':', '/'), "tfc/entity_damage_resistance");
    }

    public void entityDamageResistance(EntityDamageResistance resistance) {
        entityDamageResistance(resistance, null);
    }

    public void itemDamageResistance(ItemDamageResistance resistance, @Nullable ResourceLocation id) {
        add(resistance, ItemDamageResistance.CODEC, id, "tfc/item_damage_resistance");
    }

    public void itemDamageResistance(ItemDamageResistance resistance) {
        itemDamageResistance(resistance, null);
    }

    public void drinkable(Drinkable drinkable, @Nullable ResourceLocation id) {
        add(drinkable, Drinkable.CODEC, id, "tfc/drinkable");
    }

    public void drinkable(Drinkable drinkable) {
        drinkable(drinkable, null);
    }

    public void fertilizer(Fertilizer fertilizer, @Nullable ResourceLocation id) {
        add(fertilizer, Fertilizer.CODEC, id, "tfc/fertilizer");
    }

    public void fertilizer(Fertilizer fertilizer) {
        fertilizer(fertilizer, null);
    }

    public void fuel(Fuel fuel, @Nullable ResourceLocation id) {
        add(fuel, Fuel.CODEC, id, "tfc/fuel");
    }

    public void fuel(Fuel fuel) {
        fuel(fuel, null);
    }

    public void fluidHeat(FluidHeat fluidHeat, @Nullable ResourceLocation id) {
        add(fluidHeat, FluidHeat.CODEC, id, f -> f.fluid().toString().replace(":", "/"), "tfc/fluid_heat");
    }

    public void knappingType(KnappingType knappingType, ResourceLocation id) {
        add(id(id, null, null, "tfc/knapping_type"), knappingType, KnappingType.CODEC);
    }

    public void support(Support support, @Nullable ResourceLocation id) {
        add(support, Support.CODEC, id, "tfc/support");
    }

    public void support(Support support) {
        support(support, null);
    }

    public void itemSize(ItemSizeDefinition itemSize, @Nullable ResourceLocation id) {
        add(itemSize, ItemSizeDefinition.CODEC, id, "tfc/item_size");
    }

    public void itemSize(ItemSizeDefinition itemSize) {
        itemSize(itemSize, null);
    }

    public void fauna(Consumer<Fauna.Builder> builder, ResourceLocation id) {
        add(id(id, null, null, "tfc/fauna"), Util.make(new Fauna.Builder(), builder).build(), Fauna.CODEC);
    }

    public void climateRange(Consumer<ClimateRange.Builder> builder, ResourceLocation id) {
        add(id(id, null, null, "tfc/climate_range"), Util.make(new ClimateRange.Builder(), builder).build(), ClimateRange.CODEC);
    }

    public void lampFuel(LampFuel lampFuel, @Nullable ResourceLocation id) {
        add(lampFuel, LampFuel.CODEC, id, "tfc/lamp_fuel");
    }

    public void lampFuel(LampFuel lampFuel) {
        lampFuel(lampFuel, null);
    }

    public void deposit(Deposit deposit, @Nullable ResourceLocation id) {
        add(deposit, Deposit.CODEC, id, "tfc/deposit");
    }

    public void deposit(Deposit deposit) {
        deposit(deposit, null);
    }

    public void heat(HeatDefinition heat, @Nullable ResourceLocation id) {
        add(heat, HeatDefinition.CODEC, id, "tfc/item_heat");
    }

    public void heat(HeatDefinition heat) {
        heat(heat, null);
    }

    public void food(FoodDefinition food, @Nullable ResourceLocation id) {
        add(food, FoodDefinition.CODEC, id, "tfc/food");
    }

    public void food(FoodDefinition food) {
        food(food, null);
    }
}
