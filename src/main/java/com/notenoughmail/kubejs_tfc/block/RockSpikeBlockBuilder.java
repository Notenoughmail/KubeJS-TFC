package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RockSpikeBlockBuilder extends BlockBuilder {

    private boolean cyclingAllowed;
    @Nullable
    private Item cycleItem;
    @Nullable
    private TagKey<Item> cycleTag;
    private boolean updateWhenCycling;

    public RockSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        cyclingAllowed = false;
        updateWhenCycling = true;
        noDrops();
    }

    public RockSpikeBlockBuilder allowCycling() {
        cyclingAllowed = true;
        return this;
    }

    public RockSpikeBlockBuilder cycleItem(String item) {
        cyclingAllowed = true;
        cycleItem = KubeJSRegistries.items().get(new ResourceLocation(item));
        return this;
    }

    public RockSpikeBlockBuilder cycleTag(String tag) {
        cyclingAllowed = true;
        cycleTag = new TagKey<>(Registry.ITEM_REGISTRY, new ResourceLocation(tag));
        return this;
    }

    // TODO: replace this with an override of #isFaceSturdy or something
    /**
     * This exists because when testing the cycling feature any spike blocks above the clicked one would pop off if it
     * was bigger than the new state, this allows devs to decide if that should happen... by eliminating the block update
     * that would usually be emitted, thus making it function like the debug stick. Surprisingly observers still detect
     * it so oh well? And yet there are still strange interactions with state cycling—who would of guessed‽
     */
    public RockSpikeBlockBuilder dontUpdateWhenCycling() {
        updateWhenCycling = false;
        return this;
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
