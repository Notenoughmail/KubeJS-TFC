package io.github.notenoughmail.kubejstfc.registry;

import com.google.common.base.Suppliers;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
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

// TODO: 2.1.1 | Document
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

    public RegistryWoodBuilder woodColor(MapColor color) {
        woodColor = color;
        return this;
    }

    public RegistryWoodBuilder barkColor(MapColor color) {
        barkColor = color;
        return this;
    }

    public RegistryWoodBuilder treeGrowerId(String name) {
        return treeGrower(Suppliers.memoize(() -> TreeGrower.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive(name)).getOrThrow().getFirst()));
    }

    public RegistryWoodBuilder treeGrower(TreeGrower grower) {
        return treeGrower(() -> grower);
    }

    @HideFromJS
    public RegistryWoodBuilder treeGrower(Supplier<TreeGrower> grower) {
        treeGrower = grower;
        return this;
    }

    public RegistryWoodBuilder ticksToGrowSupplier(Supplier<Integer> ticks) {
        ticksToGrow = ticks;
        return this;
    }

    public RegistryWoodBuilder ticksToGrow(int ticks) {
        return ticksToGrowSupplier(() -> ticks);
    }

    public RegistryWoodBuilder autumnIndex(int index) {
        autumnIndex = index;
        return this;
    }

    public RegistryWoodBuilder flowerOffset(float offset) {
        flowerOffset = offset;
        return this;
    }

    public RegistryWoodBuilder conifer(boolean conifer) {
        isConifer = conifer;
        return this;
    }

    public RegistryWoodBuilder vanillaWoodType(WoodType type) {
        woodType = type;
        return this;
    }

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

    public RegistryWoodBuilder set(Wood.BlockType type, Holder<Block> block) {
        blocks.put(type, Assistant.holderAsSupplier(block));
        return this;
    }

    @Override
    public MapColor woodColor() {
        return woodColor;
    }

    @Override
    public MapColor barkColor() {
        return barkColor;
    }

    @Override
    public TreeGrower tree() {
        return treeGrower.get();
    }

    @Override
    public Supplier<Integer> ticksToGrow() {
        return ticksToGrow;
    }

    @Override
    public int autumnIndex() {
        return autumnIndex;
    }

    @Nullable
    @Override
    public Supplier<Block> getBlock(Wood.BlockType type) {
        return blocks.get(type);
    }

    @Override
    public BlockSetType getBlockSet() {
        return blockSetType;
    }

    @Override
    public WoodType getVanillaWoodType() {
        return woodType;
    }

    @Override
    public boolean isConifer() {
        return isConifer;
    }

    @Override
    public float getFlowerOffset() {
        return flowerOffset;
    }

    @Override
    public String getSerializedName() {
        return id.getPath();
    }
}
