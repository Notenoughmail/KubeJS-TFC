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

    @Info("Sets the name of the animal in the config")
    public AnimalConfigBuilder animalName(String name) {
        this.name = name;
        return this;
    }

    @Info("Sets the number of days before the animal becomes an adult")
    public AnimalConfigBuilder daysToAdult(int days) {
        adulthoodDays = days;
        return this;
    }

    @Info("Sets the number of uses the animal has")
    public AnimalConfigBuilder uses(int uses) {
        this.uses = uses;
        return this;
    }

    @Info("Sets the maximum familiarity, in the range [0, 1], that an adult mammal may be brought up to")
    public AnimalConfigBuilder maxFamiliarity(double max) {
        familiarityCap = max;
        return this;
    }

    @Deprecated(since = "1.3.1", forRemoval = true)
    @Info("Deprecated, use the correctly spelled one")
    public AnimalConfigBuilder eastRottenFood(boolean eatsRottenFoods) {
        return eatsRottenFood(eatsRottenFoods);
    }

    @Info("If the mammal will eat rotten food")
    public AnimalConfigBuilder eatsRottenFood(boolean eatsRottenFood) {
        eatsRotten = eatsRottenFood;
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

    public static class Mammal extends AnimalConfigBuilder {

        protected int gestationDays, childCount;

        public Mammal(String name) {
            super(name);
            gestationDays = 16;
            childCount = 2;
        }

        @Info("Sets the number of days the mammal will gestate for")
        public AnimalConfigBuilder gestationDays(int days) {
            gestationDays = days;
            return this;
        }

        @Info("Sets the number of children this mammal will have")
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

        @Info("Sets the number of ticks until produce is ready")
        public Producing ticksTillProduce(int ticks) {
            produceTicks = ticks;
            return this;
        }

        @Info("Sets the minimum familiarity, in the range [0, 1], needed to produce. Set above 1 to disable")
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

        @Info("Sets the number of days until an egg hatches")
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

        @Info("Sets the number of ticks until produce is ready")
        public ProducingMammal ticksTillProduce(int ticks) {
            produceTicks = ticks;
            return this;
        }

        @Info("Sets the minimum familiarity, in the range [0, 1], needed to produce. Set above 1 to disable")
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
