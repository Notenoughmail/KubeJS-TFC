package io.github.notenoughmail.kubejstfc.blocks.sub;

import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.TFCTorchBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCWallTorchBlock;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TFCWallTorchBuilder extends ExtendedPropertiesBlockBuilder {

    private static final ResourceLocation WALL_TORCH = KubeJSTFC.mc("block/wall_torch");

    private final TFCTorchBlockBuilder parent;

    public TFCWallTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
        super(i);
        itemBuilder = null;
        this.parent = parent;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.TICK_COUNTER, this);
        lightLevel(14F / 15F);
        renderType(BlockRenderType.CUTOUT);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TFCTorchBlockBuilder.TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new Impl(createExtendedProperties());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks()
                .blockEntity(TFCBlockEntities.TICK_COUNTER)
                .dropsLike(parent);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(WALL_TORCH);
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.simpleVariant("facing=east", m);
        bs.variant("facing=north", v -> v.model(m).y(270));
        bs.variant("facing=south", v -> v.model(m).y(90));
        bs.variant("facing=west", v -> v.model(m).y(180));
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return null;
    }

    private class Impl extends TFCWallTorchBlock implements ICustomTorchBlock {

        public Impl(ExtendedProperties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
            final Direction dir = pState.getValue(FACING).getOpposite();
            final double
                    x = pPos.getX() + 0.5D + 0.27D * (double) dir.getStepX(),
                    y = pPos.getY() + 0.92D,
                    z = pPos.getZ() + 0.5D + 0.27D * (double) dir.getStepZ();
            if (parent.smokeParticle != null) {
                pLevel.addParticle(parent.smokeParticle.get(), x, y, z, 0D, 0D, 0D);
            }
            if (parent.flameParticle != null) {
                pLevel.addParticle(parent.flameParticle.get(), x, y, z, 0D, 0D, 0D);
            }
        }

        @Override
        public void handleFireDouse(DouseFireEvent event) {
            event.getLevel().setBlockAndUpdate(event.getPos(), parent.deadWall.get().withPropertiesOf(event.getState()));
            event.setCanceled(true);
        }

        @Override
        public int getTotalTicks() {
            return parent.decayLength.get();
        }

        @Override
        protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
            return parent.get().defaultBlockState().useItemOn(stack, level, player, hand, hitResult);
        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
            TFCTorchBlockBuilder.randomTick(level, pos, parent.deadWall.get().withPropertiesOf(state), parent.decayLength);
        }

        @Override
        public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
            parent.get().setPlacedBy(level, pos, state, placer, stack);
        }
    }
}
