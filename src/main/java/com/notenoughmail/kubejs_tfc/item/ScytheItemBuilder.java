package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ScytheItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class ScytheItemBuilder extends HandheldItemBuilder {

    public transient TagKey<Block> mineableBlocks;

    public ScytheItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        this.mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_SCYTHE;
    }

    @Info(value = "sets the block tag that the scythe can properly dig at full speed")
    public ScytheItemBuilder mineableBlocksTag(ResourceLocation blockTag) {
        this.mineableBlocks = TagKey.create(Registries.BLOCK, blockTag);
        return this;
    }

    @Override
    public Item createObject() {
        return new ScytheItem(toolTier, attackDamageBaseline, speedBaseline, mineableBlocks, createItemProperties()) {
            private boolean modified = false;

            {
                defaultModifiers = ArrayListMultimap.create(defaultModifiers);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
                if (!modified) {
                    modified = true;
                    attributes.forEach((r, m) -> defaultModifiers.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                }
                return super.getDefaultAttributeModifiers(equipmentSlot);
            }
        };
    }
}
