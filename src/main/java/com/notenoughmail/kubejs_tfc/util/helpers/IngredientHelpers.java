package com.notenoughmail.kubejs_tfc.util.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.core.ItemStackKJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        } else if (o instanceof CharSequence) {
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
        } else if (o instanceof Pattern || o instanceof NativeRegExp) {
            final Pattern reg = UtilsJS.parseRegex(o);
            assert reg != null;
            final List<IngredientType.Entry<Block>> blocks = new ArrayList<>();
            RegistryInfo.BLOCK.getVanillaRegistry().keySet().forEach(rl -> {
                if (reg.matcher(rl.toString()).matches()) {
                    blocks.add(blockObj(RegistryInfo.BLOCK.getValue(rl)));
                }
            });
            return new BlockIngredient(blocks);
        }
        final List<?> objects = ListJS.orEmpty(o);
        final List<IngredientType.Entry<Block>> blocks = new ArrayList<>();
        for (var object : objects) {
            if (object instanceof BlockIngredient block) {
                blocks.addAll(block.entries());
            } else if (object instanceof CharSequence) {
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
            } else if (object instanceof Pattern || object instanceof NativeRegExp) {
                final Pattern reg = UtilsJS.parseRegex(object);
                assert reg != null;
                RegistryInfo.BLOCK.getVanillaRegistry().keySet().forEach(rl -> {
                    if (reg.matcher(rl.toString()).matches()) {
                        blocks.add(blockObj(RegistryInfo.BLOCK.getValue(rl)));
                    }
                });
            }
        }
        return new BlockIngredient(blocks);
    }
    
    public static FluidIngredient ofFluidIngredient(Object o) {
        if (o instanceof FluidIngredient fluid) {
            return fluid;
        } else if (o instanceof CharSequence) {
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
        } else if (o instanceof Pattern || o instanceof NativeRegExp) {
            final Pattern reg = UtilsJS.parseRegex(o);
            assert reg != null;
            final List<IngredientType.Entry<Fluid>> fluids = new ArrayList<>();
            RegistryInfo.FLUID.getVanillaRegistry().keySet().forEach(rl -> {
                if (reg.matcher(rl.toString()).matches()) {
                    fluids.add(fluidObj(RegistryInfo.FLUID.getValue(rl)));
                }
            });
            return new FluidIngredient(fluids);
        }
        final List<?> objects = ListJS.orEmpty(o);
        final List<IngredientType.Entry<Fluid>> fluids = new ArrayList<>();
        for (var object : objects) {
            if (object instanceof FluidIngredient fluid) {
                fluids.addAll(fluid.entries());
            } else if (object instanceof FluidStackIngredient fluid) {
                fluids.addAll(fluid.ingredient().entries());
            } else if (object instanceof CharSequence) {
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
            } else if (object instanceof Fluid fluid) {
                fluids.add(fluidObj(fluid));
            } else if (object instanceof FluidStackJS fluid) {
                fluids.add(fluidObj(fluid.getFluid()));
            } else if (object instanceof Pattern || object instanceof NativeRegExp) {
                final Pattern reg = UtilsJS.parseRegex(object);
                assert reg != null;
                RegistryInfo.FLUID.getVanillaRegistry().keySet().forEach(rl -> {
                    if (reg.matcher(rl.toString()).matches()) {
                        fluids.add(fluidObj(RegistryInfo.FLUID.getValue(rl)));
                    }
                });
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

    public static JsonObject inputItemToItemStackIngredient(InputItem value) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", value.ingredient.toJson());
        if (value.count > 1) {
            json.addProperty("count", value.count);
        }
        return json;
    }
}
