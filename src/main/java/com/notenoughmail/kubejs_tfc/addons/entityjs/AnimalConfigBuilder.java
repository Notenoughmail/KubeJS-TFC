package com.notenoughmail.kubejs_tfc.addons.entityjs;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.config.animals.*;

public class AnimalConfigBuilder {

    protected String name;
    protected int adulthoodDays, uses;
    protected double familiarityCap;
    protected boolean eatsRotten;
    protected Object cache;

    public AnimalConfigBuilder(String name) {
        this.name = name;
        adulthoodDays = 36;
        uses = 20;
        familiarityCap = 0.35;
        eatsRotten = false;
        cache = null;
    }

    @Info(value = "Sets the name of the animal in the config")
    public AnimalConfigBuilder animalName(String name) {
        this.name = name;
        return this;
    }

    @Info(value = "Sets the number of days before the animal becomes an adult")
    public AnimalConfigBuilder daysToAdult(int days) {
        adulthoodDays = days;
        return this;
    }

    @Info(value = "Sets the number of uses the animal has")
    public AnimalConfigBuilder uses(int uses) {
        this.uses = uses;
        return this;
    }

    @Info(value = "Sets the maximum familiarity, in the range [0, 1], that an adult mammal may be brought up to")
    public AnimalConfigBuilder maxFamiliarity(double max) {
        familiarityCap = max;
        return this;
    }

    @Info(value = "If the mammal will eat rotten food")
    public AnimalConfigBuilder eastRottenFood(boolean eatsRottenFoods) {
        eatsRotten = eatsRottenFoods;
        return this;
    }

    @HideFromJS
    public AnimalConfig animal() {
        if (cache == null) {
            cache = AnimalConfig.build(
                    KubeJSTFC.wrappedServerConfigBuilder,
                    name,
                    familiarityCap,
                    adulthoodDays,
                    uses,
                    eatsRotten
            );
        }
        return (AnimalConfig) cache;
    }

    @HideFromJS
    public static class Mammal extends AnimalConfigBuilder {

        protected int gestationDays, childCount;

        public Mammal(String name) {
            super(name);
            gestationDays = 16;
            childCount = 2;
        }

        @Info(value = "Sets for how many days the mammal will gestate")
        public AnimalConfigBuilder gestationDays(int days) {
            gestationDays = days;
            return this;
        }

        @Info(value = "Sets the number of children this mammal has")
        public AnimalConfigBuilder childCount(int count) {
            childCount = count;
            return this;
        }

        @HideFromJS
        public MammalConfig mammal() {
            if (cache == null) {
                cache = MammalConfig.build(
                        KubeJSTFC.wrappedServerConfigBuilder,
                        name,
                        familiarityCap,
                        adulthoodDays,
                        uses,
                        eatsRotten,
                        gestationDays,
                        childCount
                );
            }
            return (MammalConfig) cache;
        }
    }

    public static class Producing extends AnimalConfigBuilder {

        protected int produceTicks;
        protected double produceFamiliarity;

        public Producing(String name) {
            super(name);
            produceTicks = 100000;
            produceFamiliarity = 0.2D;
        }

        public Producing ticksTillProduce(int ticks) {
            produceTicks = ticks;
            return this;
        }

        public Producing produceFamiliarity(double familiarity) {
            produceFamiliarity = familiarity;
            return this;
        }

        @HideFromJS
        public ProducingAnimalConfig producing() {
            if (cache == null) {
                cache = ProducingAnimalConfig.build(
                        KubeJSTFC.wrappedServerConfigBuilder,
                        name,
                        familiarityCap,
                        adulthoodDays,
                        uses,
                        eatsRotten,
                        produceTicks,
                        produceFamiliarity
                );
            }
            return (ProducingAnimalConfig) cache;
        }
    }

    public static class Oviparous extends Producing {

        protected int hatchDays;

        public Oviparous(String name) {
            super(name);
            hatchDays = 20;
        }

        public Oviparous hatchDays(int days) {
            hatchDays = days;
            return this;
        }

        @HideFromJS
        public OviparousAnimalConfig oviparous() {
            if (cache == null) {
                cache = OviparousAnimalConfig.build(
                        KubeJSTFC.wrappedServerConfigBuilder,
                        name,
                        familiarityCap,
                        adulthoodDays,
                        uses,
                        eatsRotten,
                        produceTicks,
                        produceFamiliarity,
                        hatchDays
                );
            }
            return (OviparousAnimalConfig) cache;
        }
    }

    public static class ProducingMammal extends Mammal {

        protected int produceTicks;
        protected double produceFamiliarity;

        public ProducingMammal(String name) {
            super(name);
            produceTicks = 100000;
            produceFamiliarity = 0.2D;
        }

        public ProducingMammal ticksTillProduce(int ticks) {
            produceTicks = ticks;
            return this;
        }

        public ProducingMammal produceFamiliarity(double familiarity) {
            produceFamiliarity = familiarity;
            return this;
        }

        public ProducingMammalConfig producingMammal() {
            if (cache == null) {
                cache = ProducingMammalConfig.build(
                        KubeJSTFC.wrappedServerConfigBuilder,
                        name,
                        familiarityCap,
                        adulthoodDays,
                        uses,
                        eatsRotten,
                        gestationDays,
                        childCount,
                        produceTicks,
                        produceFamiliarity
                );
            }
            return (ProducingMammalConfig) cache;
        }
    }
}
