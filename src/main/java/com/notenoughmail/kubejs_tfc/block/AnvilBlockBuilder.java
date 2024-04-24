package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.AnvilBlockJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class AnvilBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    public static final Component DEFAULT_NAME = Component.translatable("tfc.block_entity.anvil");

    public transient int tier;
    public transient Consumer<ExtendedPropertiesJS> props;
    public transient Component inventoryName;

    public AnvilBlockBuilder(ResourceLocation i) {
        super(i);
        tier = 0;
        props = p -> {};
        inventoryName = DEFAULT_NAME;
    }

    @Info(value = "Sets the tier of recipes the anvil can perform")
    public AnvilBlockBuilder tier(int i) {
        tier = i;
         return this;
    }

    @Info(value = "Sets the default name of the anvil screen")
    public AnvilBlockBuilder defaultName(Component name) {
        inventoryName = name;
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJS = extendedPropsJS();
        props.accept(propsJS);
        return propsJS.delegate()
                .blockEntity(RegistryUtils.getAnvil());
    }

    @Override
    public AnvilBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @Override
    public Block createObject() {
        return new AnvilBlockJS(createExtendedProperties(), tier, inventoryName);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryUtils.addAnvil(this);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String texture = newID("block/", "").toString();
        generator.blockModel(id, m -> {
            m.parent("tfc:block/anvil");
            m.texture("all", texture);
            m.texture("particle", texture);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();
        bs.variant("facing=north", v -> v.model(model).y(90));
        bs.variant("facing=east", v -> v.model(model).y(180));
        bs.variant("facing=south", v -> v.model(model).y(270));
        bs.simpleVariant("facing=west", model);
    }
}
