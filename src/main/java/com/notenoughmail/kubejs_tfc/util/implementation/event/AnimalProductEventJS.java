package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.fluid.BoundFluidStackJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.util.events.AnimalProductEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AnimalProductEventJS extends PlayerEventJS {

    private final AnimalProductEvent event;

    public AnimalProductEventJS(AnimalProductEvent event) {
        this.event = event;
    }

    @Override
    @HideFromJS
    public Player getEntity() {
        return getPlayer();
    }

    @Override
    @Nullable
    public Player getPlayer() {
        return event.getPlayer();
    }

    public Entity getAnimal() {
        return event.getEntity();
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    public BlockContainerJS getBlock() {
        return new BlockContainerJS(event.getLevel(), event.getPos());
    }

    public TFCAnimalProperties getAnimalProperties() {
        return event.getAnimalProperties();
    }

    public ItemStack getTool() {
        return event.getTool();
    }

    public ItemStack getItemProduct() {
        return event.getProduct();
    }

    public FluidStackJS getFluidProduct() {
        return new BoundFluidStackJS(FluidStackHooksForge.fromForge(event.getFluidProduct()));
    }

    public boolean isItemProduct() {
        return !getItemProduct().isEmpty();
    }

    public void setItemProduct(ItemStack item) {
        event.setProduct(item);
    }

    public void setFluidProduct(FluidStackJS fluid) {
        event.setProduct(FluidStackHooksForge.toForge(fluid.getFluidStack()));
    }

    public int getUses() {
        return event.getUses();
    }

    public void setUses(int uses) {
        event.setUses(uses);
    }
}