package io.github.notenoughmail.kubejstfc.items;

import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class TFCFishingRodItemBuilder extends ItemBuilder {

    private static final ResourceLocation[] SMALL_BAIT = Assistant.single(TFCTags.Items.HOLDS_SMALL_FISHING_BAIT.location());
    private static final ResourceLocation[] LARGE_BAIT = Assistant.single(TFCTags.Items.HOLDS_LARGE_FISHING_BAIT.location());
    private static final ResourceLocation DEFAULT_PARENT = ResourceLocation.withDefaultNamespace("item/handheld_rod");
    private static final ResourceLocation DEFAULT_CAST_MODEL = ResourceLocation.withDefaultNamespace("item/fishing_rod/cast");
    private static final ResourceLocation CAST = KubeJSTFC.tfc("cast");

    public transient float fishingStrength;
    public transient ResourceLocation castModel;

    public TFCFishingRodItemBuilder(ResourceLocation i) {
        super(i);
        fishingStrength = 1.0F;
        BuilderRefs.rodCast.add(this);
    }

    @Info("Sets the rod's fishing strength, defaults the a value based on the speed of the rod's tier")
    public TFCFishingRodItemBuilder fishingStrength(float f) {
        fishingStrength = f;
        return this;
    }

    @Info("Sets the rod's model when cast")
    public TFCFishingRodItemBuilder castModel(ResourceLocation model) {
        castModel = model;
        return this;
    }

    @Info("Allows this rod to hold small fishing bait by adding it to the correct tag")
    public TFCFishingRodItemBuilder smallBait() {
        return (TFCFishingRodItemBuilder) tag(SMALL_BAIT);
    }

    @Info("Allows this rod to hold large fishing bait by adding it the correct tag")
    public TFCFishingRodItemBuilder largeBait() {
        return (TFCFishingRodItemBuilder) tag(LARGE_BAIT);
    }

    @Override
    public Item createObject() {
        return new TFCFishingRodItem(createItemProperties(), TFCTiers.COPPER) {

            @Override
            public float getFishingStrength() {
                return fishingStrength;
            }
        };
    }

    @Override
    protected void generateItemModels(KubeAssetGenerator generator) {
        generator.itemModel(id, m -> {
            if (modelGenerator != null) {
                modelGenerator.accept(m);
            } else {
                m.parent(parentModel == null ? DEFAULT_PARENT : parentModel);
                if (textures.isEmpty()) {
                    m.texture("layer0", baseTexture);
                } else {
                    m.textures(textures);
                }

                m.override(castModel == null ? DEFAULT_CAST_MODEL : castModel, o -> o.predicate(CAST, 1F));
            }
        });
    }
}
