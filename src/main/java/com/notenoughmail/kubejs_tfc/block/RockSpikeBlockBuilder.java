package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// TODO: JSDoc
public class RockSpikeBlockBuilder extends BlockBuilder {

    private boolean cyclingAllowed;
    @Nullable
    private Item cycleItem;
    @Nullable
    private TagKey<Item> cycleTag;
    private boolean updateWhenCycling;
    private final List<AABB> middleShape;
    private final List<AABB> tipShape;
    private VoxelShape cachedBaseShape;
    private VoxelShape cachedMiddleShape;
    private VoxelShape cachedTipShape;

    public RockSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        cyclingAllowed = false;
        updateWhenCycling = true;
        middleShape = new ArrayList<>();
        tipShape = new ArrayList<>();
    }

    public RockSpikeBlockBuilder allowCycling() {
        cyclingAllowed = true;
        return this;
    }

    public RockSpikeBlockBuilder cycleItem(ResourceLocation item) {
        cyclingAllowed = true;
        cycleItem = RegistryInfo.ITEM.getValue(item);
        return this;
    }

    public RockSpikeBlockBuilder cycleTag(ResourceLocation tag) {
        cyclingAllowed = true;
        cycleTag = TagKey.create(Registries.ITEM, tag);
        return this;
    }

    /**
     * This exists because when testing the cycling feature any spike blocks above the clicked one would pop off if it
     * was bigger than the new state, this allows devs to decide if that should happen... by eliminating the block update
     * that would usually be emitted, thus making it function like the debug stick. Surprisingly observers still detect
     * it so oh well? And yet there are still strange interactions blockStates state cycling—who would of guessed‽
     */
    public RockSpikeBlockBuilder dontUpdateWhenCycling() {
        updateWhenCycling = false;
        return this;
    }

    private VoxelShape getBaseShape() {
        if (customShape.isEmpty()) {
            return RockSpikeBlock.BASE_SHAPE;
        }
        if (cachedBaseShape == null) {
            cachedBaseShape = BlockBuilder.createShape(customShape);
        }
        return cachedBaseShape;
    }

    public BlockBuilder middleBox(double x0, double y0, double z0, double x1, double y1, double z1, boolean scale16) {
        if (scale16) {
            middleShape.add(new AABB(x0 / 16D, y0 / 16D, z0 / 16D, x1 / 16D, y1 / 16D, z1 / 16D));
        } else {
            middleShape.add(new AABB(x0, y0, z0, x1, y1, z1));
        }

        return this;
    }

    public BlockBuilder middleBox(double x0, double y0, double z0, double x1, double y1, double z1) {
        return middleBox(x0, y0, z0, x1, y1, z1, true);
    }

    private VoxelShape getMiddleShape() {
        if (middleShape.isEmpty()) {
            return RockSpikeBlock.MIDDLE_SHAPE;
        }
        if (cachedMiddleShape == null) {
            cachedMiddleShape = BlockBuilder.createShape(middleShape);
        }
        return cachedMiddleShape;
    }

    public BlockBuilder tipBox(double x0, double y0, double z0, double x1, double y1, double z1, boolean scale16) {
        if (scale16) {
            tipShape.add(new AABB(x0 / 16D, y0 / 16D, z0 / 16D, x1 / 16D, y1 / 16D, z1 / 16D));
        } else {
            tipShape.add(new AABB(x0, y0, z0, x1, y1, z1));
        }

        return this;
    }

    public BlockBuilder tipBox(double x0, double y0, double z0, double x1, double y1, double z1) {
        return tipBox(x0, y0, z0, x1, y1, z1, true);
    }

    private VoxelShape createTipShape() {
        if (tipShape.isEmpty()) {
            return RockSpikeBlock.TIP_SHAPE;
        }
        if (cachedTipShape == null) {
            cachedTipShape = BlockBuilder.createShape(tipShape);
        }
        return cachedTipShape;
    }

    @Override
    public RockSpikeBlock createObject() {
        return new RockSpikeBlock(createProperties()) {

            @Override
            public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
                var isCorrectItem = (cycleItem != null && pPlayer.getItemInHand(pHand).is(cycleItem)) || (cycleTag != null && pPlayer.getItemInHand(pHand).is(cycleTag));
                if (cyclingAllowed && (cycleItem == null && cycleTag == null ? pPlayer.getItemInHand(pHand).isEmpty() : isCorrectItem)) {
                    var newState = switch (pState.getValue(PART)) {
                        case TIP -> Part.BASE;
                        case MIDDLE -> Part.TIP;
                        case BASE -> Part.MIDDLE;
                    };
                    if (pPlayer.isColliding(pPos, pState.setValue(PART, newState))) {
                        return InteractionResult.PASS; // Pass if player is inside the new state
                    }
                    if (!pLevel.isClientSide()) {
                        pLevel.setBlock(pPos, pState.setValue(PART, newState), updateWhenCycling ? 3 : 2);
                    }

                    return InteractionResult.SUCCESS;
                }
                return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
            }

            @Override
            public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
                return switch (state.getValue(PART)) {
                    case BASE -> getBaseShape();
                    case MIDDLE -> getMiddleShape();
                    default -> createTipShape();
                };
            }
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson != null) {
            generator.json(newID("blockstates/", ""), blockstateJson);
        } else {
            var blockModelLoc = newID("block/", "").toString();
            generator.blockState(id, m -> {
                m.variant("part=base", v -> v.model(blockModelLoc + "_base"));
                m.variant("part=middle", v -> v.model(blockModelLoc + "_middle"));
                m.variant("part=tip", v -> v.model(blockModelLoc + "_tip"));
            });
        }

        var texture = id.getNamespace() + ":block/" + id.getPath();
        generator.blockModel(newID("", "_base"), m -> {
            m.parent("tfc:block/rock/spike_base");
            m.texture("texture", texture);
            m.texture("particle", texture);
        });
        generator.blockModel(newID("", "_middle"), m -> {
            m.parent("tfc:block/rock/spike_middle");
            m.texture("texture", texture);
            m.texture("particle", texture);
        });
        generator.blockModel(newID("", "_tip"), m -> {
            m.parent("tfc:block/rock/spike_tip");
            m.texture("texture", texture);
            m.texture("particle", texture);
        });

        if (itemBuilder != null) {
            generator.itemModel(itemBuilder.id, m -> {
                m.parent(newID("block/", "_base").toString());
                m.textures(itemBuilder.textureJson);
            });
        }
    }
}
