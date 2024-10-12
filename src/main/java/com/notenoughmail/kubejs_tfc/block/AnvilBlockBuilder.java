package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class AnvilBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient int tier;
    public transient Component inventoryName;

    public AnvilBlockBuilder(ResourceLocation i) {
        super(i);
        tier = 0;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.ANVIL, this);
        tag(TFCTags.Items.ANVILS.location());
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        return texture("all", tex);
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
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.ANVIL);
    }

    @Override
    public Block createObject() {
        return new AnvilBlockJS(createExtendedProperties(), tier, inventoryName);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        if (model.isEmpty()) {
            generator.blockModel(id, m -> {
                m.parent("tfc:block/anvil");
                m.textures(textures);
            });
        } else {
            hasModel(generator);
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();
        bs.variant("facing=north", v -> v.model(model).y(90));
        bs.variant("facing=east", v -> v.model(model).y(180));
        bs.variant("facing=south", v -> v.model(model).y(270));
        bs.simpleVariant("facing=west", model);
    }

    public static class AnvilBlockJS extends AnvilBlock {

        @Nullable
        private final Component defaultName;

        public AnvilBlockJS(ExtendedProperties properties, int tier, @Nullable Component defaultName) {
            super(properties, tier);
            this.defaultName = defaultName;
        }

        @Nullable
        public Component getDefaultName() {
            return defaultName;
        }
    }
}
