package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.core.ItemStackKJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ListJS;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.openjdk.nashorn.internal.objects.NativeString;

import java.util.ArrayList;
import java.util.List;

public class IngredientHelpers {

    public static BlockIngredient block(Block block) {
        return new BlockIngredient(List.of(blockObj(block)));
    }

    public static BlockIngredient block(IngredientType.Entry<Block> entry) {
        return new BlockIngredient(List.of(entry));
    }

    public static IngredientType.Entry<Block> blockObj(Block block) {
        return new IngredientType.ObjEntry<>(block);
    }

    public static IngredientType.Entry<Block> blockTag(TagKey<Block> tag) {
        return new BlockIngredient.BlockTag(tag);
    }

    public static FluidIngredient fluid(Fluid fluid) {
        return new FluidIngredient(List.of(fluidObj(fluid)));
    }

    public static FluidIngredient fluid(IngredientType.Entry<Fluid> entry) {
        return new FluidIngredient(List.of(entry));
    }

    public static IngredientType.Entry<Fluid> fluidObj(Fluid fluid) {
        return new IngredientType.ObjEntry<>(fluid);
    }

    public static IngredientType.Entry<Fluid> fluidTag(TagKey<Fluid> tag) {
        return new FluidIngredient.FluidTag(tag);
    }

    // My old friend, instanceof else-if chains
    public static BlockIngredient ofBlockIngredient(Object o) {
        if (o instanceof BlockIngredient block) {
            return block;
        } else if (o instanceof CharSequence || o instanceof NativeString) {
            final String name = o.toString();
            if (name.charAt(0) == '#') {
                return block(blockTag(TagKey.create(Registries.BLOCK, new ResourceLocation(name.substring(1)))));
            } else {
                final Block block = RegistryInfo.BLOCK.getValue(new ResourceLocation(name));
                if (block != null) {
                    return block(block);
                }
            }
        } else if (o instanceof JsonElement json) {
            return BlockIngredient.fromJson(json);
        } else if (o instanceof Block block) {
            return block(block);
        } else if (o instanceof BlockState state) {
            return block(state.getBlock());
        }
        final List<?> objects = ListJS.orEmpty(o);
        final List<IngredientType.Entry<Block>> blocks = new ArrayList<>();
        for (var object : objects) {
            if (object instanceof CharSequence || object instanceof NativeString) {
                final String name = object.toString();
                if (name.charAt(0) == '#') {
                    blocks.add(blockTag(TagKey.create(Registries.BLOCK, new ResourceLocation(name.substring(1)))));
                } else {
                    Block block = RegistryInfo.BLOCK.getValue(new ResourceLocation(name));
                    if (block != null) {
                        blocks.add(blockObj(block));
                    }
                }
            } else if (object instanceof IngredientType.Entry<?> entry) {
                blocks.add((IngredientType.Entry<Block>) entry); // If someone manages to provide an Entry<Fluid> they deserve whatever this causes
            } else if (object instanceof Block block) {
                blocks.add(blockObj(block));
            } else if (object instanceof BlockState state) {
                blocks.add(blockObj(state.getBlock()));
            }
        }
        return new BlockIngredient(blocks);
    }
    
    public static FluidIngredient ofFluidIngredient(Object o) {
        if (o instanceof FluidIngredient fluid) {
            return fluid;
        } else if (o instanceof CharSequence || o instanceof NativeString) {
            final String name = o.toString();
            if (name.charAt(0) == '#') {
                return fluid(fluidTag(TagKey.create(Registries.FLUID, new ResourceLocation(name.substring(1)))));
            } else {
                final Fluid fluid = RegistryInfo.FLUID.getValue(new ResourceLocation(name));
                if (fluid != null) {
                    return fluid(fluid);
                }
            }
        } else if (o instanceof JsonElement json) {
            return FluidIngredient.fromJson(json);
        } else if (o instanceof FluidStackIngredient stack) { // Sometimes people get confused
            return stack.ingredient();
        }
        final List<?> objects = ListJS.orEmpty(o);
        final List<IngredientType.Entry<Fluid>> fluids = new ArrayList<>();
        for (var object : objects) {
            if (object instanceof CharSequence || object instanceof NativeString) {
                final String name = object.toString();
                if (name.charAt(0) == '#') {
                    fluids.add(fluidTag(TagKey.create(Registries.FLUID, new ResourceLocation(name.substring(1)))));
                } else {
                    Fluid fluid = RegistryInfo.FLUID.getValue(new ResourceLocation(name));
                    if (fluid != null) {
                        fluids.add(fluidObj(fluid));
                    }
                }
            } else if (object instanceof IngredientType.Entry<?> entry) {
                fluids.add((IngredientType.Entry<Fluid>) entry); // If someone manages to provide an Entry<Block> they deserve whatever this causes
            } else if (o instanceof Fluid fluid) {
                fluids.add(fluidObj(fluid));
            } else if (o instanceof FluidStackJS fluid) {
                fluids.add(fluidObj(fluid.getFluid()));
            }
        }
        return new FluidIngredient(fluids);
    }

    public static FluidStackIngredient ofFluidStackIngredient(Object o) {
        if (o instanceof FluidStackIngredient fluid) {
            return fluid;
        } else if (o instanceof FluidIngredient fluid) {
            return new FluidStackIngredient(fluid, (int) FluidStack.bucketAmount());
        } else if (o instanceof JsonObject json) {
            return FluidStackIngredient.fromJson(json);
        } else if (o instanceof FluidStackJS fluidStack) {
            return new FluidStackIngredient(fluid(fluidStack.getFluid()), (int) fluidStack.getAmount());
        }
        return new FluidStackIngredient(ofFluidIngredient(o), (int) FluidStack.bucketAmount()); // A not wholly unreasonable fallback
    }

    public static JsonObject itemStackToJson(ItemStack stack) {
        return ((ItemStackKJS) (Object) stack).toJsonJS();
    }

    public static String stringifyItemStack(ItemStack stack) {
        return ((ItemStackKJS) (Object) stack).kjs$toItemString();
    }
}
