package com.notenoughmail.kubejs_tfc.util.hell;

import com.google.gson.JsonElement;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.kubejs.util.WrappedJS;
import dev.latvian.mods.rhino.mod.util.Copyable;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class FluidStackIngredientJS implements WrappedJS, Copyable {

    public static FluidStackIngredientJS of(@Nullable Object o) {
        if (o == null) {
            return EmptyFluidStackIngredientJS.INSTANCE;
        } else if (o instanceof FluidStackIngredientJS js) {
            return js;
        } else if (o instanceof FluidStackIngredient fluidStackIngredient) {
            return new UnboundedFluidStackIngredientJS(fluidStackIngredient.ingredient(), fluidStackIngredient.amount());
        } else if (o instanceof FluidIngredient fluidIngredient) {
            var f = new UnboundedFluidStackIngredientJS(fluidIngredient, (int) FluidStack.bucketAmount());
            return f.isEmpty() ? EmptyFluidStackIngredientJS.INSTANCE : f;
        } else if (o instanceof FluidStack fluidStack) {
            var f = new UnboundedFluidStackIngredientJS(FluidIngredient.of(fluidStack.getFluid()), (int) fluidStack.getAmount());
            return f.isEmpty() ? EmptyFluidStackIngredientJS.INSTANCE : f;
        } else if (o instanceof Fluid fluid) {
            var f = new UnboundedFluidStackIngredientJS(FluidIngredient.of(fluid), (int) FluidStack.bucketAmount());
            return f.isEmpty() ? EmptyFluidStackIngredientJS.INSTANCE : f;
        } else if (o instanceof JsonElement json) {
            return fromJson(json);
        } else if (o instanceof ResourceLocation || o instanceof CharSequence) {
            var s = o.toString();
            if (s.isEmpty() || s.equals("-") || s.equals("empty") || s.equals("minecraft:empty")) {
                return EmptyFluidStackIngredientJS.INSTANCE;
            }

            var fluidStrings = s.split(" ", 0);
            var fluidArrayList = new ArrayList<Fluid>();
            for (int i = 0 ; i < fluidStrings.length - 1 ; i++) {
                fluidArrayList.add(KubeJSRegistries.fluids().get(new ResourceLocation(fluidStrings[i])));
            }
            Fluid[] fluids = new Fluid[fluidArrayList.size()];
            fluids = fluidArrayList.toArray(fluids);
            return new UnboundedFluidStackIngredientJS(FluidIngredient.of(fluids), UtilsJS.parseInt(fluidStrings[fluidStrings.length -1], (int) FluidStack.bucketAmount()));
        }

        // Not going to bother with the MapJS stuff
        return EmptyFluidStackIngredientJS.INSTANCE;
    }

    public static FluidStackIngredientJS of(@Nullable Object o, int amount) {
        var stack = of(o);
        stack.setAmount(amount);
        return stack;
    }

    public static FluidStackIngredientJS fromJson(JsonElement json) {
        var f = FluidStackIngredient.fromJson(json.getAsJsonObject());
        return new UnboundedFluidStackIngredientJS(f.ingredient(), f.amount());
    }

    public FluidIngredient getFluidIngedient() {
        return getFluidstackIngredient().ingredient();
    }

    public abstract FluidStackIngredient getFluidstackIngredient();

    public boolean isEmpty() {
        return getAmount() <= 0;
    }
    public abstract int getAmount();

    public abstract void setAmount(int amount);

    public final FluidStackIngredientJS withAmount(int amount) {
        if (amount < 0) {
            return EmptyFluidStackIngredientJS.INSTANCE;
        }

        var fsi = copy();
        fsi.setAmount(amount);
        return fsi;
    }

    @Override
    public abstract FluidStackIngredientJS copy();
}
