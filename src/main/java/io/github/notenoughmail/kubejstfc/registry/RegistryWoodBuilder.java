package io.github.notenoughmail.kubejstfc.registry;

import com.google.common.base.Suppliers;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@ReturnsSelf
public class RegistryWoodBuilder implements RegistryWood {

    public transient final ResourceLocation id;
    private transient final Map<Wood.BlockType, Supplier<Block>> blocks;

    private transient MapColor woodColor, barkColor;
    private transient Supplier<TreeGrower> treeGrower;
    private transient Supplier<Integer> ticksToGrow;
    private transient int autumnIndex;
    private transient float flowerOffset;
    private transient BlockSetType blockSetType;
    private transient WoodType woodType;
    private transient boolean isConifer;

    public RegistryWoodBuilder(ResourceLocation id) {
        this.id = id;
        blocks = new EnumMap<>(Wood.BlockType.class);
        woodColor = barkColor = MapColor.NONE;
        treeGrower = () -> TreeGrower.OAK;
        ticksToGrow = () -> 1000;
        autumnIndex = 0;
        flowerOffset = 0;
        blockSetType = BlockSetType.OAK;
        woodType = WoodType.OAK;
        isConifer = false;
        Assistant.registerWoods(b -> b.put(this.id, this));
    }

    @Info("Set the color that describes the wood")
    public RegistryWoodBuilder woodColor(MapColor color) {
        woodColor = color;
        return this;
    }

    @Info("Set the color that describes the bark")
    public RegistryWoodBuilder barkColor(MapColor color) {
        barkColor = color;
        return this;
    }

    @Info("Set the tree grower used by the wood from an id")
    public RegistryWoodBuilder treeGrowerId(String name) {
        return treeGrower(Suppliers.memoize(() -> TreeGrower.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive(name)).getOrThrow().getFirst()));
    }

    @Info("Set the tree grower used by the wood")
    public RegistryWoodBuilder treeGrower(TreeGrower grower) {
        return treeGrower(() -> grower);
    }

    @HideFromJS
    public RegistryWoodBuilder treeGrower(Supplier<TreeGrower> grower) {
        treeGrower = grower;
        return this;
    }

    @Info("Set the number of ticks a tree needs to grow as a supplier")
    public RegistryWoodBuilder ticksToGrowSupplier(Supplier<Integer> ticks) {
        ticksToGrow = ticks;
        return this;
    }

    @Info("Set the number of ticks a tree needs to grow")
    public RegistryWoodBuilder ticksToGrow(int ticks) {
        return ticksToGrowSupplier(() -> ticks);
    }

    @Info("The autumn index of the wood")
    public RegistryWoodBuilder autumnIndex(int index) {
        autumnIndex = index;
        return this;
    }

    @Info("Set the flower offset of the wood")
    public RegistryWoodBuilder flowerOffset(float offset) {
        flowerOffset = offset;
        return this;
    }

    @Info("Set if this wood is coniferous")
    public RegistryWoodBuilder conifer(boolean conifer) {
        isConifer = conifer;
        return this;
    }

    @Info("Set the underlying vanilla wood type describing the wood")
    public RegistryWoodBuilder vanillaWoodType(WoodType type) {
        woodType = type;
        return this;
    }

    @Info("Set the underlying vanilla block set type describing the wood")
    public RegistryWoodBuilder blockSetType(BlockSetType type) {
        blockSetType = type;
        return this;
    }

    @HideFromJS
    public void accept(Supplier<Block> builder, Wood.BlockType type) {
        blocks.put(type, builder);
    }

    @HideFromJS
    public void remove(Wood.BlockType type, Supplier<Block> builder) {
        blocks.remove(type, builder);
    }

    @Info("Associate a block with its type for the wood")
    public RegistryWoodBuilder set(Wood.BlockType type, Holder<Block> block) {
        blocks.put(type, Assistant.holderAsSupplier(block));
        return this;
    }

    @Info("Get the color describing the wood")
    @Override
    public MapColor woodColor() {
        return woodColor;
    }

    @Info("Get the color describing the bark color")
    @Override
    public MapColor barkColor() {
        return barkColor;
    }

    @Info("Get the tree grower of the wood")
    @Override
    public TreeGrower tree() {
        return treeGrower.get();
    }

    @Info("Get the ticks it takes for a tree of this wood to grow")
    @Override
    public Supplier<Integer> ticksToGrow() {
        return ticksToGrow;
    }

    @Info("Get the autumn index of the wood")
    @Override
    public int autumnIndex() {
        return autumnIndex;
    }

    @Info("Get the block corresponding to a block type, may be null")
    @Nullable
    @Override
    public Supplier<Block> getBlock(Wood.BlockType type) {
        return blocks.get(type);
    }

    @Info("Get the vanilla block set describing the wood")
    @Override
    public BlockSetType getBlockSet() {
        return blockSetType;
    }

    @Info("Get the vanilla wood type describing the wood")
    @Override
    public WoodType getVanillaWoodType() {
        return woodType;
    }

    @Info("If the wood is coniferous")
    @Override
    public boolean isConifer() {
        return isConifer;
    }

    @Info("Get the flower offset of the wood")
    @Override
    public float getFlowerOffset() {
        return flowerOffset;
    }

    @Info("Get the path of the wood's id")
    @Override
    public String getSerializedName() {
        return id.getPath();
    }
}
