package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.JavelinItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavelinItemBuilder extends HandheldItemBuilder {

    public float thrownDamage;

    public static final List<JavelinItemBuilder> thisList = new ArrayList<>();

    public JavelinItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        thrownDamage = 0.3f;
        thisList.add(this);
    }

    @Info(value = "Sets the javelin's thrown damage")
    public JavelinItemBuilder thrownDamage(float damage) {
        thrownDamage = damage;
        return this;
    }

    @Info(value = "Adds this to the 'tfc:skeleton_weapons' tag")
    public JavelinItemBuilder skeletonWeapon() {
        tag(TFCTags.Items.SKELETON_WEAPONS.location());
        return this;
    }

    @Override
    public Item createObject() {
        return new JavelinItem(toolTier, attackDamageBaseline, thrownDamage, speedBaseline, createItemProperties(), new ResourceLocation(id.getNamespace(), "textures/entity/projectiles/" + id.getPath() + "_javelin.png")) {
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

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        ModelUtils.ITEMS.javelin(id, generator);
    }
}
