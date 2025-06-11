package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.DeadTorchBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.DeadWallTorchBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.TFCWallTorchBuilder;
import com.notenoughmail.kubejs_tfc.item.internal.StandingAndWallBlockItemBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCTorchBlock;
import net.dries007.tfc.common.items.TorchItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TFCTorchBlockBuilder extends ExtendedPropertiesShapedBlockBuilder {

    public static void randomTick(ServerLevel level, BlockPos pos, BlockState place, Supplier<Integer> decayLength) {
        if (decayLength.get() > 0) {
            level.getBlockEntity(pos, TFCBlockEntities.TICK_COUNTER.get()).ifPresent(counter -> {
                if (counter.getTicksSinceUpdate() > decayLength.get()) {
                    level.setBlockAndUpdate(pos, place);
                }
            });
        }
    }

    public transient Supplier<Integer> decayLength;
    public transient Supplier<Optional<ParticleOptions>> flameParticle, smokeParticle;
    @HideFromJS
    public final DeadTorchBuilder dead;
    @HideFromJS
    public final TFCWallTorchBuilder wall;
    @HideFromJS
    public final DeadWallTorchBuilder deadWall;
    public transient ItemBuilder deadTorchItem;

    public TFCTorchBlockBuilder(ResourceLocation i) {
        super(i);
        decayLength = TFCConfig.SERVER.torchTicks;
        flameParticle = Lazy.of(() -> Optional.of(ParticleTypes.FLAME));
        smokeParticle = Lazy.of(() -> Optional.of(ParticleTypes.SMOKE));
        dead = new DeadTorchBuilder(newID("", "_dead"), this);
        wall = new TFCWallTorchBuilder(newID("", "_wall"), this);
        deadWall = new DeadWallTorchBuilder(newID("", "_dead_wall"), this);
        itemBuilder = new StandingAndWallBlockItemBuilder(i, this, wall) {
            @Override
            public Item createObject() {
                return new TorchItem(TFCTorchBlockBuilder.this.get(), wall.get(), createItemProperties());
            }
        };
        deadTorchItem = new StandingAndWallBlockItemBuilder(newID("", "_dead"), dead, deadWall);
        RegistryUtils.hackBlockEntity(TFCBlockEntities.TICK_COUNTER, this);
        itemBuilder.texture("layer0", "minecraft:block/torch");
        deadTorchItem.texture("layer0", "tfc:block/torch_off");
        textureAll("minecraft:block/torch");
        lightLevel(14F / 15F); // WTF Kube
        renderType("cutout");
        noCollision();
        dead.noCollision();
        wall.noCollision();
        deadWall.noCollision();
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("torch", tex);
    }

    @Info("Sets the properties for the dead item, may be null to remove")
    public TFCTorchBlockBuilder deadItem(@Nullable Consumer<ItemBuilder> item) {
        if (item == null || deadTorchItem == null) {
            deadTorchItem = null;
        } else {
            item.accept(deadTorchItem);
        }
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
    public TFCTorchBlockBuilder flameParticle(@Nullable ResourceLocation particle) {
        flameParticle = RegistryUtils.getParticleOrLogError(particle);
        return this;
    }

    @Info("Sets the torch's smoke particle, may be null to not have one")
    public TFCTorchBlockBuilder smokeParticle(@Nullable ResourceLocation particle) {
        smokeParticle = RegistryUtils.getParticleOrLogError(particle);
        return this;
    }

    @Info("Sets the properties for the dead block")
    public TFCTorchBlockBuilder dead(Consumer<DeadTorchBuilder> dead) {
        dead.accept(this.dead);
        return this;
    }

    @Info("Sets the properties for the wall block")
    public TFCTorchBlockBuilder wall(Consumer<TFCWallTorchBuilder> wall) {
        wall.accept(this.wall);
        return this;
    }

    @Info("Sets the properties for the dead wall block")
    public TFCTorchBlockBuilder deadWall(Consumer<DeadWallTorchBuilder> deadWall) {
        deadWall.accept(this.deadWall);
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
    public void generateAssetJsons(AssetJsonGenerator generator) {
        super.generateAssetJsons(generator);
        if (deadTorchItem != null) {
            if (deadTorchItem.modelJson != null) {
                generator.json(deadTorchItem.newID("models/item/", ""), deadTorchItem.modelJson);
            } else {
                generator.itemModel(deadTorchItem.id, this::deadItemModel);
            }
        }
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent(itemBuilder.parentModel.isEmpty() ? "item/generated" : itemBuilder.parentModel);
        m.textures(itemBuilder.textureJson);
    }

    private void deadItemModel(ModelGenerator m) {
        m.parent(deadTorchItem.parentModel.isEmpty() ? "item/generated" : deadTorchItem.parentModel);
        m.textures(deadTorchItem.textureJson);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("minecraft:block/torch");
            m.textures(textures);
        });
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(generator, this, p -> {
            p.survivesExplosion();
            p.addEntry(ResourceUtils.alternatives(
                    (LootTableEntry) ResourceUtils.createEntry("minecraft:stick")
                            .addCondition(burntOut())
                            .randomChance(0.25D),
                    (LootTableEntry) ResourceUtils.createEntry("tfc:powder/wood_ash")
                            .addCondition(burntOut())
                            .randomChance(0.25D),
                    ResourceUtils.createEntry(id.toString())
                            .addCondition(notBurntOut())
            ));
        });
    }

    private JsonObject burntOut() {
        return ResourceUtils.buildJson(json -> json.addProperty("condition", "tfc:is_burnt_out"));
    }

    private JsonObject notBurntOut() {
        return ResourceUtils.buildJson(json -> {
            json.addProperty("condition", "minecraft:inverted");
            json.add("term", burntOut());
        });
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        if (deadTorchItem != null) {
            RegistryInfo.ITEM.addBuilder(deadTorchItem);
        }
        RegistryInfo.BLOCK.addBuilder(dead);
        RegistryInfo.BLOCK.addBuilder(wall);
        RegistryInfo.BLOCK.addBuilder(deadWall);
    }

    private class Impl extends TFCTorchBlock implements ICustomTorchBlock {

        public Impl(ExtendedProperties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
            TFCTorchBlockBuilder.randomTick(level, pos, dead.get().defaultBlockState(), decayLength);
        }

        @Override
        public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
            final double
                    x = pPos.getX() + 0.5D,
                    y = pPos.getY() + 0.7D,
                    z = pPos.getZ() + 0.5D;
            smokeParticle.get().ifPresent(particle -> pLevel.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D));
            TFCTorchBlockBuilder.this.flameParticle.get().ifPresent(particle -> pLevel.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D));
        }

        @Override
        public void handleFireDouse(DouseFireEvent event) {
            final Level level = event.getLevel();
            final BlockPos pos = event.getPos();
            final BlockState state = event.getState();

            level.setBlockAndUpdate(pos, dead.get().withPropertiesOf(state));
            event.setCanceled(true);
        }

        @Override
        public int getTotalTicks() {
            return decayLength.get();
        }
    }
}
