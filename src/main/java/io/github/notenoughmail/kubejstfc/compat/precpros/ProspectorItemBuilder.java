package io.github.notenoughmail.kubejstfc.compat.precpros;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.implementation.extensions.MutableLevelTier;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.precisionprospecting.items.ProspectorItem;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;

@ReturnsSelf
public class ProspectorItemBuilder extends HandheldItemBuilder {

    public transient int level;
    public transient IntSupplier primaryRadius, secondaryRadius, displacement;
    public transient TagKey<Block> prospectTag;
    public transient int coolDown;
    @Nullable
    public transient ProspectorType defaultType;

    public ProspectorItemBuilder(ResourceLocation i) {
        super(i, 3F, -2.4F);
        level = 0;
        coolDown = 20;
        primaryRadius = secondaryRadius = () -> 12;
        displacement = () -> 0;
        prospectTag = TFCTags.Blocks.PROSPECTABLE;
    }

    @Info("Set the radii, displacement, and prospect tag of the prospector based on a preset")
    public ProspectorItemBuilder builtinType(ProspectorType type) {
        defaultType = type;
        return this;
    }

    @Info("set the tool level of the prospector, determines the false negative chance")
    public ProspectorItemBuilder level(int i) {
        Assistant.levelTier(toolTier).kubejs_tfc$SetTFCLevel(i);
        return this;
    }

    @Info("Set the block tag the prospector will scan for")
    public ProspectorItemBuilder prospectTag(TagKey<Block> tag) {
        prospectTag = tag;
        return this;
    }

    @Info("Set the cool down of the prospector")
    public ProspectorItemBuilder coolDown(int c) {
        coolDown = c;
        return this;
    }

    public ProspectorItemBuilder primaryRadius(int i) {
        return primaryRadiusSupplier(() -> i);
    }

    public ProspectorItemBuilder primaryRadiusSupplier(IntSupplier v) {
        primaryRadius = v;
        return this;
    }

    public ProspectorItemBuilder secondaryRadius(int i) {
        return secondaryRadiusSupplier(() -> i);
    }

    public ProspectorItemBuilder secondaryRadiusSupplier(IntSupplier v) {
        secondaryRadius = v;
        return this;
    }

    public ProspectorItemBuilder displacement(int i) {
        return displacementSupplier(() -> i);
    }

    public ProspectorItemBuilder displacementSupplier(IntSupplier v) {
        displacement = v;
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        final MutableLevelTier tier = Assistant.levelTier(toolTier);
        if (defaultType != null) {
            return defaultType.create(tier, createItemProperties());
        }
        return new ProspectorItem(tier, createItemProperties(), primaryRadius, secondaryRadius, displacement, prospectTag, coolDown);
    }
}
