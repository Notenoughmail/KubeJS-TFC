package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.JavelinItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class JavelinItemBuilder extends HandheldItemBuilder {

    public JavelinItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        return new JavelinItem(toolTier, attackDamageBaseline, speedBaseline, createItemProperties(), new ResourceLocation(id.getNamespace(), "textures/entity/projectiles/" + id.getPath() + "_javelin.png")) {
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

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.json(AssetJsonGenerator.asItemModelLocation(id), ModelUtils.ITEMS.javelinItemModelJson(id));
    }
}
