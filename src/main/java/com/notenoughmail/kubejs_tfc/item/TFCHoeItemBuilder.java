package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.TFCHoeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class TFCHoeItemBuilder extends HandheldItemBuilder {

    public TFCHoeItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        tag(TFCTags.Items.HOES.location());
    }

    @Override
    public Item createObject() {
        return new TFCHoeItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties()) {
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
