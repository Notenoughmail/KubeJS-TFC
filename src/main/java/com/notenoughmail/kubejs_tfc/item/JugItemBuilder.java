package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.JsonUtils;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.JugItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class JugItemBuilder extends ItemBuilder {

    public transient Supplier<Integer> capacity;
    public transient TagKey<Fluid> acceptableFluids;
    @Nullable
    public transient Component filledDisplayName;

    public JugItemBuilder(ResourceLocation i) {
        super(i);
        capacity = () -> 100;
        acceptableFluids = TFCTags.Fluids.USABLE_IN_JUG;
        FluidContainerItemBuilder.colorList.add(this);
    }

    @Info(value = "Sets the display name to use when the jug is filled")
    public JugItemBuilder filledDisplayName(Component c) {
        filledDisplayName = c;
        return this;
    }

    @Override
    public ItemBuilder texture(String tex) {
        texture("base", tex);
        return texture("fluid", tex + "_overlay");
    }

    @Info(value = "Sets the capacity, in mB, of the jug")
    public JugItemBuilder capcity(int capacity) {
        this.capacity = () -> capacity;
        return this;
    }

    @Info(value = "Sets the capacity, in mB, supplier of the jug")
    public JugItemBuilder capacitySupplier(Supplier<Integer> capacity) {
        this.capacity = capacity;
        return this;
    }

    @Info(value = "Sets the fluid tag that the jug is allowed to hold")
    public JugItemBuilder fluidTagAccept(ResourceLocation tag) {
        acceptableFluids = TagKey.create(Registries.FLUID, tag);
        return this;
    }

    @Override
    public Item createObject() {
        return new JugItem(createItemProperties(), capacity, acceptableFluids);
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        JsonUtils.fluidContainerModel(this, generator);
    }

    @Override
    public void generateLang(LangEventJS lang) {
        super.generateLang(lang);
        if (filledDisplayName != null) {
            lang.add(id.getNamespace(), getBuilderTranslationKey() + ".filled", filledDisplayName.getString());
        } else {
            lang.add(id.getNamespace(), getBuilderTranslationKey() + ".filled", "%s " + (displayName == null ? UtilsJS.snakeCaseToTitleCase(id.getPath()) : displayName.getString()));
        }
    }
}
