package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.MapJS;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;

@SuppressWarnings("unused")
public class BuildVeinProperties {

    // Common values
    private VeinType type = VeinType.CLUSTER;
    private int rarity = 60;
    private int size = 8;
    private float density = 0.2f;
    private JsonObject minY;
    private JsonObject maxY;
    @Nullable
    private JsonObject indicator;
    @Nullable
    private String biomeTag;
    @Nullable
    private Integer salt;
    private final JsonArray blocks = new JsonArray();
    private final String name; // The wiki doesn't mention this, but the game freaks out when it doesn't have a 'random_name' value in the config

    // Additional configs for disc and pipe veins
    private int height = 6;
    private int radius = 3;
    private int minSkew = 0;
    private int maxSkew = 0;
    private int minSlant = 0;
    private int maxSlant = 0;
    private float sign = 0.5f;

    public BuildVeinProperties(String name) {
        this.name = name;
    }

    public BuildVeinProperties type(String type) {
        this.type = VeinType.valueOf(type.toUpperCase(Locale.ROOT));
        return this;
    }

    public BuildVeinProperties rarity(int i) {
        rarity = i;
        return this;
    }

    public BuildVeinProperties size(int i) {
        size = i;
        return this;
    }

    public BuildVeinProperties density(float f) {
        density = f;
        return this;
    }

    public BuildVeinProperties minY(Object o) {
        if (o instanceof JsonObject obj) {
            minY = obj;
        } else if (o instanceof MapJS map) {
            minY = map.toJson();
        } else if (o instanceof Number num) {
            var obj = new JsonObject();
            var i = num.intValue();
            obj.addProperty("absolute", i);
            minY = obj;
        }
        return this;
    }

    public BuildVeinProperties maxY(Object o) {
        if (o instanceof JsonObject obj) {
            maxY = obj;
        } else if (o instanceof MapJS map) {
            maxY = map.toJson();
        } else if (o instanceof Number num) {
            var obj = new JsonObject();
            var i = num.intValue();
            obj.addProperty("absolute", i);
            maxY = obj;
        }
        return this;
    }

    public BuildVeinProperties indicator(Consumer<Indicator> indicatorConsumer) {
        var indicator = new Indicator();
        indicatorConsumer.accept(indicator);
        this.indicator = indicator.toJson();
        return this;
    }

    public BuildVeinProperties replacementMap(Consumer<BlockReplacementMap> map) {
        var replacement = new BlockReplacementMap();
        map.accept(replacement);
        blocks.addAll(replacement.toJson());
        return this;
    }

    public BuildVeinProperties salt(int i) {
        salt = i;
        return this;
    }

    // https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/1.18.x/src/main/resources/data/tfc/worldgen/configured_feature/vein/volcanic_sulfur.json#L87
    // https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/1.18.x/src/main/java/net/dries007/tfc/world/feature/vein/VeinConfig.java#L63
    // Optional<TagKey<Biome>>
    public BuildVeinProperties biomeFilter(String biomeTag) {
        if (biomeTag.matches("#.+")) {
            this.biomeTag = biomeTag;
        } else {
            this.biomeTag = "#".concat(biomeTag);
        }
        return this;
    }

    public BuildVeinProperties height(int i) {
        height = i;
        return this;
    }

    public BuildVeinProperties radius(int i) {
        radius = i;
        return this;
    }

    public BuildVeinProperties minSkew(int i) {
        minSkew = i;
        return this;
    }

    public BuildVeinProperties maxSkew(int i) {
        maxSkew = i;
        return this;
    }

    public BuildVeinProperties minSlant(int i) {
        minSlant = i;
        return this;
    }

    public BuildVeinProperties maxSlant(int i) {
        maxSlant = i;
        return this;
    }

    public BuildVeinProperties sign(float f) {
        sign = f;
        return this;
    }


    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", switch (type) {
            case DISC -> "tfc:disc_vein";
            case PIPE -> "tfc:pipe_vein";
            default -> "tfc:cluster_vein";
        });
        var config = new JsonObject();
        config.addProperty("rarity", rarity);
        config.addProperty("size", size);
        config.addProperty("density", density);
        config.add("min_y", minY);
        config.add("max_y", maxY);
        config.add("indicator", indicator);
        config.add("blocks", blocks);
        config.addProperty("random_name", name);
        if (salt != null) {
            config.addProperty("salt", salt);
        }
        if (biomeTag != null) {
            config.addProperty("biomes", biomeTag);
        }
        if (type == VeinType.DISC) {
            config.addProperty("height", height);
        }
        if (type == VeinType.PIPE) {
            config.addProperty("radius", radius);
            config.addProperty("min_skew", minSkew);
            config.addProperty("max_skew", maxSkew);
            config.addProperty("min_slant", minSlant);
            config.addProperty("max_slant", maxSlant);
            config.addProperty("sign", sign);
        }
        json.add("config", config);
        return json;
    }

    private enum VeinType {
        CLUSTER,
        DISC,
        PIPE
    }

    public static class Indicator {

        private int depth = 35;
        private int spread = 15;
        private int rarity = 10;
        private final List<JsonObject> blocks = new ArrayList<>();

        public Indicator depth(int i) {
            depth = i;
            return this;
        }

        public Indicator spread(int i) {
            spread = i;
            return this;
        }

        public Indicator rarity(int i) {
            rarity = i;
            return this;
        }

        public Indicator indicators(String... values) {
            for (String value : values) {
                var obj = new JsonObject();
                if (value.matches("[0-9.]+ ?.+")) {
                    var weight = value.split(" ");
                    obj.addProperty("weight", Float.parseFloat(weight[0]));
                    obj.add("block", blockStateToLenient(weight[1]));
                } else {
                    obj.add("block", blockStateToLenient(value));
                }
                blocks.add(obj);
            }
            return this;
        }

        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("depth", depth);
            json.addProperty("spread", spread);
            json.addProperty("rarity", rarity);
            var array = new JsonArray();
            for (JsonObject obj : blocks) {
                array.add(obj);
            }
            json.add("blocks", array);
            return json;
        }
    }

    public static class BlockReplacementMap {

        private final List<JsonObject> blocks = new ArrayList<>();

        public Replace replace(String... replace) {
            return new Replace(this, replace);
        }

        public JsonArray toJson() {
            var json = new JsonArray();
            for (JsonObject block : blocks) {
                json.add(block);
            }
            return json;
        }

        public static class Replace {

            private final BlockReplacementMap map;
            private final String[] replaceList;

            public Replace(BlockReplacementMap map, String... replace) {
                this.map = map;
                this.replaceList = replace;
            }

            public BlockReplacementMap with(String... with) {
                var obj = new JsonObject();
                var replaceArray = new JsonArray();
                for (String s : replaceList) {
                    replaceArray.add(s);
                }
                obj.add("replace", replaceArray);
                var withArray = new JsonArray();
                for (String s : with) {
                    var object = new JsonObject();
                    if (s.matches("[0-9.]+ ?.+")) {
                        var weight = s.split(" ");
                        object.addProperty("weight", Float.parseFloat(weight[0]));
                        object.add("block", blockStateToLenient(weight[1]));
                    } else {
                        object.add("block", blockStateToLenient(s));
                    }
                    withArray.add(object);
                }
                obj.add("with", withArray);
                map.blocks.add(obj);
                return map;
            }
        }
    }
}
