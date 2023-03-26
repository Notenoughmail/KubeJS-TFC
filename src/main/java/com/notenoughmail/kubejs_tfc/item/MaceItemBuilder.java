package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.MaceItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class MaceItemBuilder extends HandheldItemBuilder {

    public MaceItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        return new MaceItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties()) {
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
