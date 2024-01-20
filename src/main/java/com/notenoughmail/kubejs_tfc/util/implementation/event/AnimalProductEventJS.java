package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.fluid.BoundFluidStackJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.util.events.AnimalProductEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@Info(value = """
        This event is fired whenever a TFC cow is milked or sheep is sheared.
        Cancelling this event prevents the default behaviour, which is controlled
        by the entity's implementation
        """)
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

    @Info(value = "Returns the animal the product comes from")
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

    @Info(value = "Returns TFC animal properties of the animal")
    public TFCAnimalProperties getAnimalProperties() {
        return event.getAnimalProperties();
    }

    @Info(value = "Returns the 'too' used to get a product, either a bucket or shears")
    public ItemStack getTool() {
        return event.getTool();
    }

    @Info(value = "Returns the item product of the event, may be empty if the product is a fluid")
    public ItemStack getItemProduct() {
        return event.getProduct();
    }

    @Info(value = "Returns the fluid product of the event, may be empty if the product is an item")
    public FluidStackJS getFluidProduct() {
        return new BoundFluidStackJS(FluidStackHooksForge.fromForge(event.getFluidProduct()));
    }

    @Info(value = "Returns true if the event's product is an item and not a fluid")
    public boolean isItemProduct() {
        return !getItemProduct().isEmpty();
    }

    @Info(value = "Sets the item product, attempting to use this on an event originally producing a fluid will void the product")
    public void setItemProduct(ItemStack item) {
        event.setProduct(item);
    }

    @Info(value = "Sets the fluid product, attempting to use this on an event originally producing an item will void the product")
    public void setFluidProduct(FluidStackJS fluid) {
        event.setProduct(FluidStackHooksForge.toForge(fluid.getFluidStack()));
    }

    @Info(value = "How much 'wear' the animal will take from the event")
    public int getUses() {
        return event.getUses();
    }

    @Info(value = "Sets how much 'wear' the animal will take from the event")
    public void setUses(int uses) {
        event.setUses(uses);
    }
}