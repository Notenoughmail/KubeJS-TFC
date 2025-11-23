package io.github.notenoughmail.kubejstfc.addons.precpros;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.rhino.util.ReturnsSelf;
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

    public ProspectorItemBuilder builtinType(ProspectorType type) {
        defaultType = type;
        return this;
    }

    public ProspectorItemBuilder level(int i) {
        level = i;
        return this;
    }

    public ProspectorItemBuilder prospectTag(TagKey<Block> tag) {
        prospectTag = tag;
        return this;
    }

    public ProspectorItemBuilder coolDown(int c) {
        coolDown = c;
        return this;
    }

    public ProspectorItemBuilder primaryRadius(int i) {
        return primaryRadius(() -> i);
    }

    public ProspectorItemBuilder primaryRadius(IntSupplier v) {
        primaryRadius = v;
        return this;
    }

    public ProspectorItemBuilder secondaryRadius(int i) {
        return secondaryRadius(() -> i);
    }

    public ProspectorItemBuilder secondaryRadius(IntSupplier v) {
        secondaryRadius = v;
        return this;
    }

    public ProspectorItemBuilder displacement(int i) {
        return displacement(() -> i);
    }

    public ProspectorItemBuilder displacement(IntSupplier v) {
        displacement = v;
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        if (defaultType != null) {
            return defaultType.create(Assistant.levelTier(toolTier, level), createItemProperties());
        }
        return new ProspectorItem(toolTier, level, createItemProperties(), primaryRadius, secondaryRadius, displacement, prospectTag, coolDown);
    }
}
