package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.DeadTorchBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.DeadWallTorchBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.TFCWallTorchBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.item.StandingAndWallBlockItemBuilder;
import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomTorchBlock;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCTorchBlock;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.items.TorchItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ReturnsSelf
public class TFCTorchBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public static void randomTick(ServerLevel level, BlockPos pos, BlockState place, Supplier<Integer> decayLength) {
        if (decayLength.get() > 0) {
            level.getBlockEntity(pos, TFCBlockEntities.TICK_COUNTER.get()).ifPresent(counter -> {
                if (counter.getTicksSinceUpdate() > decayLength.get()) {
                    level.setBlockAndUpdate(pos, place);
                }
            });
        }
    }

    public static final String[] TEXTURE_KEYS = { "particle", "torch" };

    public static final ResourceLocation TORCH = KubeJSTFC.mc("block/torch");

    public transient Supplier<Integer> decayLength;
    @Nullable
    public transient Supplier<ParticleOptions> flameParticle, smokeParticle;
    public transient final DelayedBuilder<DeadTorchBuilder> dead;
    public transient final DelayedBuilder<TFCWallTorchBuilder> wall;
    public transient final DelayedBuilder<DeadWallTorchBuilder> deadWall;
    public transient DelayedBuilder.NullCapable<ItemBuilder> deadTorchItem;

    public TFCTorchBlockBuilder(ResourceLocation i) {
        super(i);
        decayLength = TFCConfig.SERVER.torchTicks;
        flameParticle = () -> ParticleTypes.FLAME;
        smokeParticle = () -> ParticleTypes.SMOKE;
        dead = new DelayedBuilder<>(r -> new DeadTorchBuilder(r, this), () -> id.withSuffix("_dead"));
        wall = new DelayedBuilder<>(r -> new TFCWallTorchBuilder(r, this), () -> id.withSuffix("_wall"));
        deadWall = new DelayedBuilder<>(r -> new DeadWallTorchBuilder(r, this), () -> id.withSuffix("_dead_wall"));
        itemBuilder = new StandingAndWallBlockItemBuilder(i, this, wall) {
            @Override
            public Item createObject() {
                return new TorchItem(TFCTorchBlockBuilder.this.get(), wall.get().get(), createItemProperties());
            }
        };
        deadTorchItem = new DelayedBuilder.NullCapable<>(r -> new StandingAndWallBlockItemBuilder(r, dead.get(), deadWall), () -> dead.get().id);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.TICK_COUNTER, this);
        lightLevel(14F / 15F); // WTF Kube
        renderType(BlockRenderType.CUTOUT);
        noCollision();
        dead.onConstruct(BlockBuilder::noCollision);
        wall.onConstruct(BlockBuilder::noCollision);
        deadWall.onConstruct(BlockBuilder::noCollision);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("Sets the properties of the dead item, may be null to remove")
    public TFCTorchBlockBuilder deadItem(@Nullable Consumer<ItemBuilder> item) {
        return deadItem(null, item);
    }

    @Info("Sets the properties of the dead item, may be null to remove")
    public TFCTorchBlockBuilder deadItem(@Nullable KubeResourceLocation id, @Nullable Consumer<ItemBuilder> item) {
        deadTorchItem.accept(id, item);
        return this;
    }

    @Info("Sets the time, in ticks, the torch will burn for")
    public TFCTorchBlockBuilder decayLength(int length) {
        decayLength = () -> length;
        return this;
    }

    @Info("Sets the supplier for the time, in ticks, the torch will burn for")
    public TFCTorchBlockBuilder decayLengthSupplier(Supplier<Integer> length) {
        decayLength = length;
        return this;
    }

    @Info("Sets the torch's flame particle, may be null to not have one")
    public TFCTorchBlockBuilder flameParticle(@Nullable Holder<ParticleOptions> particle) {
        flameParticle = Assistant.holderAsSupplier(particle);
        return this;
    }

    @Info("Sets the torch's smoke particle, may be null to not have one")
    public TFCTorchBlockBuilder smokeParticle(@Nullable Holder<ParticleOptions> particle) {
        smokeParticle = Assistant.holderAsSupplier(particle);
        return this;
    }

    @Info("Sets the properties of the dead block")
    public TFCTorchBlockBuilder dead(Consumer<DeadTorchBuilder> dead) {
        return dead(null, dead);
    }

    @Info("Sets the properties of the dead block")
    public TFCTorchBlockBuilder dead(@Nullable KubeResourceLocation id, Consumer<DeadTorchBuilder> dead) {
        this.dead.accept(id, dead);
        return this;
    }

    @Info("Sets the properties of the wall block")
    public TFCTorchBlockBuilder wall(Consumer<TFCWallTorchBuilder> wall) {
        return wall(null, wall);
    }

    @Info("Sets the properties of the wall block")
    public TFCTorchBlockBuilder wall(@Nullable KubeResourceLocation id, Consumer<TFCWallTorchBuilder> wall) {
        this.wall.accept(id, wall);
        return this;
    }

    @Info("Sets the properties of the dead wall block")
    public TFCTorchBlockBuilder deadWall(Consumer<DeadWallTorchBuilder> deadWall) {
        return deadWall(null, deadWall);
    }

    @Info("Sets the properties of the dead wall block")
    public TFCTorchBlockBuilder deadWall(@Nullable KubeResourceLocation id, Consumer<DeadWallTorchBuilder> deadWall) {
        this.deadWall.accept(id, deadWall);
        return this;
    }

    @Override
    public Block createObject() {
        return new Impl(createExtendedProperties());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks()
                .blockEntity(TFCBlockEntities.TICK_COUNTER);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, dead);
        Assistant.addBlock(registry, wall);
        Assistant.addBlock(registry, deadWall);
        Assistant.addItem(registry, deadTorchItem.get());
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        super.generateAssets(generator);
        ModelUtil.basicItemModelGen(deadTorchItem.get(), generator);
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(TORCH);
            m.textures(textures);
        });
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.singlePool(drops, p -> {
            LootUtil.survivesExplosion(p);
            p.add(LootUtil.alternatives(
                    LootItem.lootTableItem(Items.STICK)
                            .when(LootUtil.burntOut())
                            .when(LootUtil.chance(0.25F)),
                    LootItem.lootTableItem(TFCItems.POWDERS.get(Powder.WOOD_ASH))
                            .when(LootUtil.burntOut())
                            .when(LootUtil.chance(0.25F)),
                    LootItem.lootTableItem(get())
                            .when(LootUtil.not(LootUtil.burntOut()))
            ));
        });
    }

    private class Impl extends TFCTorchBlock implements ICustomTorchBlock {

        public Impl(ExtendedProperties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
            TFCTorchBlockBuilder.randomTick(level, pos, dead.get().get().defaultBlockState(), decayLength);
        }

        @Override
        public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
            final double
                    x = pPos.getX() + 0.5D,
                    y = pPos.getY() + 0.7D,
                    z = pPos.getZ() + 0.5D;
            if (smokeParticle != null) {
                pLevel.addParticle(smokeParticle.get(), x, y, z, 0D, 0D, 0D);
            }
            if (TFCTorchBlockBuilder.this.flameParticle != null) {
                pLevel.addParticle(TFCTorchBlockBuilder.this.flameParticle.get(), x, y, z, 0D, 0D, 0D);
            }
        }

        @Override
        public void handleFireDouse(DouseFireEvent event) {
            final Level level = event.getLevel();
            final BlockPos pos = event.getPos();
            final BlockState state = event.getState();

            level.setBlockAndUpdate(pos, dead.get().get().withPropertiesOf(state));
            event.setCanceled(true);
        }

        @Override
        public int getTotalTicks() {
            return decayLength.get();
        }
    }
}
