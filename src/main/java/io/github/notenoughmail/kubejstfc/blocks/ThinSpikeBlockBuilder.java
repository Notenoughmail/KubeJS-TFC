package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.ThinSpikeBlock;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ReturnsSelf
@SuppressWarnings("unused")
public class ThinSpikeBlockBuilder extends BlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "0" };

    private static final ResourceLocation SPIKE = KubeJSTFC.tfc("block/thin_spike");
    private static final ResourceLocation TIP = KubeJSTFC.tfc("block/thin_spike_tip");

    public transient float dripChance;
    public transient float dripTemp;
    public transient Supplier<@Nullable ParticleOptions> particle;
    public transient ResourceLocation tipModel;

    public ThinSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        dripChance = 0.15f;
        particle = () -> null;
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("Sets the chance, in the range [0, 1], the block will drip per tick")
    public ThinSpikeBlockBuilder dripChance(float chance) {
        dripChance = chance;
        return this;
    }

    @Info("Sets the temperature above which the block will produce drip particles")
    public ThinSpikeBlockBuilder dripTemperature(float temperature) {
        dripTemp = temperature;
        return this;
    }

    @Info("The registry name of a particle that will drip from the block")
    public ThinSpikeBlockBuilder dripParticle(@Nullable Holder<ParticleType<?>> particle) {
        this.particle = Assistant.getParticleOptions(particle);
        return this;
    }

    @Info("A supplier for the particle that will drip from the block")
    public ThinSpikeBlockBuilder fullDripParticle(Supplier<ParticleOptions> particle) {
        this.particle = Assistant.wrapParticleOptionsSafely(particle);
        return this;
    }

    @Info("Sets the model of the tip state")
    public ThinSpikeBlockBuilder tipModel(ResourceLocation s) {
        tipModel = s;
        return this;
    }

    @Override
    public ThinSpikeBlock createObject() {
        return new ThinSpikeBlock(createProperties()) {

            @Override
            public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                if (particle.get() != null) {
                    if (
                            state.getValue(TIP) &&
                            state.getValue(FLUID).getFluid() == Fluids.EMPTY &&
                            Climate.getTemperature(level, pos) > dripTemp &&
                            random.nextFloat() < dripChance
                    ) {
                        if (random.nextFloat() < dripChance) {
                            spawnParticle(level, pos, state, particle.get());
                        }
                    }
                }
            }


             // Modified from IcicleBlock#spawnDripParticle to accept arbitrary particles
            private static void spawnParticle(Level level, BlockPos pos, BlockState state, ParticleOptions particle) {
                Vec3 offset = state.getOffset(level, pos);
                level.addParticle(particle, pos.getX() + 0.5D + offset.x, ((pos.getY() + 1) - 0.6875F) - 0.0625D, pos.getZ() + 0.5D + offset.z, 0.0D, 0.0D, 0.0D);
            }
        };
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        bs.simpleVariant("tip=true", tipModel == null ? newID("block/", "_tip") : tipModel);
        bs.simpleVariant("tip=false", ModelUtil.plainModel(this));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(SPIKE);
            m.textures(textures);
        });
        if (tipModel == null) {
            generator.blockModel(id.withSuffix("_tip"), m -> {
                m.parent(TIP);
                m.textures(textures);
            });
        }
    }
}
