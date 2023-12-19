package com.notenoughmail.kubejs_tfc.addons.precpros.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.notenoughmail.kubejs_tfc.item.PropickItemBuilder;
import com.notenoughmail.precisionprospecting.items.ProsHammerItem;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class ProsHammerItemBuilder extends PropickItemBuilder {

    public ProsHammerItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Item createObject() {
        return new ProsHammerItem(toolTier, attackDamageBaseline, speedBaseline, createItemProperties()) {
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
