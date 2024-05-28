package com.notenoughmail.kubejs_tfc.addons.precpros.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.notenoughmail.precisionprospecting.items.ProspectorItem;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ProspectorItemBuilder extends HandheldItemBuilder {

    public transient int cooldown;
    public transient Supplier<Integer> r1;
    public transient Supplier<Integer> r2;
    public transient Supplier<Integer> d;
    public transient TagKey<Block> prospectTag;

    public ProspectorItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        cooldown = 20;
        r1 = () -> 10;
        r2 = () -> 10;
        d = () -> 0;
        prospectTag = TFCTags.Blocks.PROSPECTABLE;
    }

    public ProspectorItemBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public ProspectorItemBuilder primaryRadius(int i) {
        r1 = () -> i;
        return this;
    }

    public ProspectorItemBuilder primaryRadius(Supplier<Integer> supplier) {
        r1 = supplier;
        return this;
    }

    public ProspectorItemBuilder secondaryRadius(int i) {
        r2 = () -> i;
        return this;
    }

    public ProspectorItemBuilder secondaryRadius(Supplier<Integer> supplier) {
        r2 = supplier;
        return this;
    }

    public ProspectorItemBuilder displacement(int i) {
        d = () -> i;
        return this;
    }

    public ProspectorItemBuilder displacement(Supplier<Integer> supplier) {
        d = supplier;
        return this;
    }

    public ProspectorItemBuilder prospectTag(ResourceLocation blockTag) {
        prospectTag = TagKey.create(Registries.BLOCK, blockTag);
        return this;
    }

    @Override
    public Item createObject() {
        return new ProspectorItem(toolTier, attackDamageBaseline, speedBaseline, createItemProperties(), cooldown, r1, r2, d, prospectTag) {
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
