package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
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

    @Info(value = "Rotates the models by the given amount")
    public LooseRockBlockBuilder rotateModel(int i) {
        rotate = i;
        return this;
    }

    @Info(value = "Makes the block collide with entities")
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
        super.textureAll(tex);
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
            v.model(blockModelLocation + "_pebble").y(rotate);
            v.model(blockModelLocation + "_pebble").y(90 + rotate);
            v.model(blockModelLocation + "_pebble").y(180 + rotate);
            v.model(blockModelLocation + "_pebble").y(270 + rotate);
        });
        bs.variant("count=2", v -> {
            v.model(blockModelLocation + "_rubble").y(rotate);
            v.model(blockModelLocation + "_rubble").y(90 + rotate);
            v.model(blockModelLocation + "_rubble").y(180 + rotate);
            v.model(blockModelLocation + "_rubble").y(270 + rotate);
        });
        bs.variant("count=3", v -> {
            v.model(blockModelLocation + "_boulder").y(rotate);
            v.model(blockModelLocation + "_boulder").y(90 + rotate);
            v.model(blockModelLocation + "_boulder").y(180 + rotate);
            v.model(blockModelLocation + "_boulder").y(270 + rotate);
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
        final LootBuilder lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else if (itemBuilder != null) {
            final JsonObject decay = new JsonObject();
            decay.addProperty("function", "minecraft:explosion_decay");
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(itemBuilder.get()))
                        .addFunction(setCountFunction(2))
                        .addFunction(setCountFunction(3))
                        .addFunction(decay);
            });
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    private JsonObject setCountFunction(int count) {
        final JsonObject function = new JsonObject();
        function.addProperty("function", "minecraft:set_count");
        function.addProperty("count", count);

        final JsonArray conditions = new JsonArray();
        conditions.add(DataUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("count", Integer.toString(count))));
        function.add("conditions", conditions);
        return function;
    }
}
