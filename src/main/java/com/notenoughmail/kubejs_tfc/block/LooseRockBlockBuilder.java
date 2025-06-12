package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

@SuppressWarnings("unused")
public class LooseRockBlockBuilder extends BlockBuilder {

    private int rotate;
    public JsonObject itemTextures;
    public String rockType;

    public LooseRockBlockBuilder(ResourceLocation i) {
        super(i);
        rotate = 0;
        noCollision = true;
        itemTextures = new JsonObject();
        rockType = "metamorphic";
    }

    @Info("Rotates the models by the given amount")
    public LooseRockBlockBuilder rotateModel(int i) {
        rotate = i;
        return this;
    }

    @Info("Makes the block collide with entities")
    public LooseRockBlockBuilder collision() {
        noCollision = false;
        return this;
    }

    @Info("Sets the item's texture (layer0).")
    public LooseRockBlockBuilder itemTexture(String tex) {
        itemTextures.addProperty("layer0", tex);
        return this;
    }

    @Info("Sets the item's texture by given key.")
    public LooseRockBlockBuilder itemTexture(String key, String tex) {
        itemTextures.addProperty(key, tex);
        return this;
    }

    @Info("Directly set the item's texture json.")
    public LooseRockBlockBuilder itemTextureJson(JsonObject json) {
        itemTextures = json;
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        texture("all", tex);
        return this;
    }

    @Info("sets the rock type the block model should use, may be 'igneous_extrusive', 'igneous_intrusive', 'metamorphic', or 'sedimentary'")
    public LooseRockBlockBuilder rockTypeModel(String s) {
        rockType = s;
        return this;
    }

    @Override
    public LooseRockBlock createObject() {
        return new LooseRockBlock(createProperties());
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(newID("", "_pebble"), m -> {
            m.parent("kubejs_tfc:block/ground_cover/loose/" + rockType + "_1");
            m.textures(textures);
        });
        generator.blockModel(newID("", "_rubble"), m -> {
            m.parent("kubejs_tfc:block/ground_cover/loose/" + rockType + "_2");
            m.textures(textures);
        });
        generator.blockModel(newID("", "_boulder"), m -> {
            m.parent("kubejs_tfc:block/ground_cover/loose/" + rockType + "_3");
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String blockModelLocation = newID("block/", "").toString();
        bs.variant("count=1", v -> {
            final String m = blockModelLocation + "_pebble";
            v.model(m).y(rotate);
            v.model(m).y(90 + rotate);
            v.model(m).y(180 + rotate);
            v.model(m).y(270 + rotate);
        });
        bs.variant("count=2", v -> {
            final String m = blockModelLocation + "_rubble";
            v.model(m).y(rotate);
            v.model(m).y(90 + rotate);
            v.model(m).y(180 + rotate);
            v.model(m).y(270 + rotate);
        });
        bs.variant("count=3", v -> {
            final String m = blockModelLocation + "_boulder";
            v.model(m).y(rotate);
            v.model(m).y(90 + rotate);
            v.model(m).y(180 + rotate);
            v.model(m).y(270 + rotate);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");

            if (itemTextures.size() == 0) {
                itemTexture(newID("item/", "").toString());
            }
            m.textures(itemTextures);
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(generator, this, p -> {
            final JsonObject decay = new JsonObject();
            decay.addProperty("function", "minecraft:explosion_decay");
            p.survivesExplosion();
            p.addItem(new ItemStack(itemBuilder.get()))
                    .addConditionalFunction(func -> {
                        func.count(ConstantValue.exactly(2F));
                        func.addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("count", "2")));
                    })
                    .addConditionalFunction(func -> {
                        func.count(ConstantValue.exactly(3F));
                        func.addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("count", "3")));
                    })
                    .addFunction(decay);

        });
    }
}
