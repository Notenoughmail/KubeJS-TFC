package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.fluid.BoundFluidStackJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.player.PlayerJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.util.events.AnimalProductEvent;
import org.jetbrains.annotations.Nullable;

public class AnimalProductEventJS extends PlayerEventJS {

    private final AnimalProductEvent event;

    public AnimalProductEventJS(AnimalProductEvent event) {
        this.event = event;
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    // Returns the producing animal
    @Override
    public EntityJS getEntity() {
        return new EntityJS(event.getEntity());
    }

    @Override
    @Nullable
    public PlayerJS getPlayer() {
        return new EntityJS(event.getPlayer()).getPlayer();
    }

    @Override
    public LevelJS getLevel() {
        return UtilsJS.getLevel(event.getLevel());
    }

    public BlockContainerJS getBlock() {
        return new BlockContainerJS(event.getLevel(), event.getPos());
    }

    public TFCAnimalProperties getAnimalProperties() {
        return event.getAnimalProperties();
    }

    public ItemStackJS getTool() {
        return new ItemStackJS(event.getTool());
    }

    public ItemStackJS getItemProduct() {
        return new ItemStackJS(event.getProduct());
    }

    public FluidStackJS getFluidProduct() {
        return new BoundFluidStackJS(FluidStackHooksForge.fromForge(event.getFluidProduct()));
    }

    public void setItemProduct(ItemStackJS item) {
        event.setProduct(item.getItemStack());
    }

    public void setFluidProcuct(FluidStackJS fluid) {
        event.setProduct(FluidStackHooksForge.toForge(fluid.getFluidStack()));
    }

    public int getUses() {
        return event.getUses();
    }

    public void setUses(int uses) {
        event.setUses(uses);
    }
}