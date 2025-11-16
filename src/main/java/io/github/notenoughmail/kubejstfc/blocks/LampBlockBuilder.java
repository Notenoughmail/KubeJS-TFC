package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.item.LampBlockItemBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class LampBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "metal", "chain" };
    private static final ResourceLocation LAMP = KubeJSTFC.tfc("block/lamp"), HANGING = KubeJSTFC.tfc("block/lamp_hanging");

    public transient int lightLevel;
    public transient BiConsumer<LampModelType, ModelGenerator> models;

    public LampBlockBuilder(ResourceLocation i) {
        super(i);
        lightLevel = 15;
        itemBuilder = new LampBlockItemBuilder(id, this);
        Assistant.singleTag(this, TFCTags.Blocks.LAMPS);
        renderType(BlockRenderType.CUTOUT);
        BuilderRefs.lamps.add(this);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.LAMP, this);
        models = (t, m) -> {
            m.parent(t.hanging ? HANGING : LAMP);
            m.texture("lamp", t.on ? "tfc:block/lamp" : "tfc:block/lamp_off");
            m.textures(textures);
        };
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("""
            Sets the model generation of the lamp block, accepts a `BiConsumer` of a `LampModelType` and a model generator.
            The generator is unique for each type.
            
            There are four types: `OFF`, HANGING_OFF`, `ON`, and `HANGING_ON`. These have two boolean properties which can
            be used to determine the model currently being generated. The properties are `.on` and `.hanging`.
            """)
    public LampBlockBuilder models(BiConsumer<LampModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Info("Sets the light level the lamp gives off when it is lit")
    public LampBlockBuilder lightLevel(int i) {
        lightLevel = i;
        return this;
    }

    @Override
    public Block createObject() {
        return new LampBlock(createExtendedProperties());
    }

    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .randomTicks()
                .pushReaction(PushReaction.DESTROY)
                .lightLevel(state -> state.getValue(LampBlock.LIT) ? lightLevel : 0)
                .blockEntity(TFCBlockEntities.LAMP);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.determinedSinglePool(this, p -> p.apply(LootUtil.copyFluid()));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (LampModelType t : LampModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (LampModelType t : LampModelType.VALUES) {
            bs.simpleVariant("hanging=" + t.hanging + ",lit=" + t.on, t.modelEx(this));
        }
    }

    public enum LampModelType implements ISupplyModels {
        OFF(false, false),
        HANGING_OFF(false, true),
        ON(true, false),
        HANGING_ON(true, true);

        public final boolean on, hanging;
        private final String str;

        LampModelType(boolean on, boolean hanging) {
            this.on = on;
            this.hanging = hanging;
            str = name().toLowerCase(Locale.ROOT);
        }

        public static final LampModelType[] VALUES = values();

        @Override
        public String str() {
            return str;
        }
    }
}
