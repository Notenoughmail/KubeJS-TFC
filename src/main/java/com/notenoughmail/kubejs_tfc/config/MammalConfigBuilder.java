package com.notenoughmail.kubejs_tfc.config;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.config.ConfigBuilder;
import net.dries007.tfc.config.animals.MammalConfig;

public class MammalConfigBuilder {

    private final ConfigBuilder parent;
    private String name;
    private int adulthoodDays, uses, gestationDays, childCount;
    private double familiarityCap;
    private boolean eatsRotten;
    private MammalConfig cache;

    public MammalConfigBuilder(ConfigBuilder parent, String name) {
        this.parent = parent;
        this.name = name;
        adulthoodDays = 36;
        uses = 20;
        gestationDays = 16;
        childCount = 2;
        familiarityCap = 0.35;
        eatsRotten = false;
        cache = null;
    }

    @Info(value = "Sets the name of the mammal in the config")
    public MammalConfigBuilder mammalName(String name) {
        this.name = name;
        return this;
    }

    @Info(value = "Sets the number of days before the animal becomes an adult")
    public MammalConfigBuilder daysToAdult(int days) {
        adulthoodDays = days;
        return this;
    }

    @Info(value = "Sets the number of uses the animal has")
    public MammalConfigBuilder uses(int uses) {
        this.uses = uses;
        return this;
    }

    @Info(value = "Sets for how many days the mammal will gestate")
    public MammalConfigBuilder gestationDays(int days) {
        gestationDays = days;
        return this;
    }

    @Info(value = "Sets the number of children this mammal has")
    public MammalConfigBuilder childCount(int count) {
        childCount = count;
        return this;
    }

    @Info(value = "Sets the maximum familiarity, in the range [0, 1], that the first generation may go up to")
    public MammalConfigBuilder maxFamiliarity(double max) {
        familiarityCap = max;
        return this;
    }

    @Info(value = "If the mammal will eat rotten food")
    public MammalConfigBuilder eastRottenFood(boolean eatsRottenFoods) {
        eatsRotten = eatsRottenFoods;
        return this;
    }

    public MammalConfig build() {
        if (cache == null) {
            cache = MammalConfig.build(
                    parent,
                    name,
                    familiarityCap,
                    adulthoodDays,
                    uses,
                    eatsRotten,
                    gestationDays,
                    childCount
            );
        }
        return cache;
    }
}
