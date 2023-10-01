package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ToolItemBuilder extends HandheldItemBuilder {

    public transient TagKey<Block> mineableBlocks;

    public ToolItemBuilder(ResourceLocation i) {
        super(i, 3F, -2.4F);
        mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_KNIFE;
    }

    public ToolItemBuilder mineableBlocksTag(ResourceLocation blockTag) {
        this.mineableBlocks = TagKey.create(Registry.BLOCK_REGISTRY, blockTag);
        return this;
    }

    public ToolItemBuilder hammer() {
        this.mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_HAMMER;
        tag(TFCTags.Items.HAMMERS.location());
        return this;
    }

    public ToolItemBuilder knife() {
        this.mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_KNIFE;
        tag(TFCTags.Items.KNIVES.location());
        return this;
    }

    @Override
    public Item createObject() {
        return new ToolItem(toolTier, attackDamageBaseline, speedBaseline, mineableBlocks, createItemProperties()) {
            private boolean modified = false;

            {
                defaultModifiers = ArrayListMultimap.create(defaultModifiers);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
                if (!modified) {
                    modified = true;
                    attributes.forEach((r, m) -> defaultModifiers.put(KubeJSRegistries.attributes().get(r), m));
                }
                return super.getDefaultAttributeModifiers(equipmentSlot);
            }
        };
    }
}
