package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import net.dries007.tfc.common.capabilities.heat.IHeatBlock;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

// UNUSED
// TODO: [Future] PR ticking attachments to kube
public class HeatAttachemnt extends CalendarTickingAttachment implements IHeatBlock {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:heat",
            TypeDescJS.object()
                    .add("onCalendarUpdate", UPDATE_DESC, true)
                    .add("consumeFuel", TypeDescJS.BOOLEAN, true)
                    .add("thermalInertia", TypeDescJS.NUMBER, true)
                    .add("handleCrucibleRecipes", TypeDescJS.BOOLEAN, true),
            map -> entity -> new HeatAttachemnt(entity, getUpdater(map), getBool(map, "consumeFuel"), getThermalInertia(map), getBool(map, "handleCrucibleRecipes"))
    );

    private static boolean getBool(Map<String, Object> map, String type) {
        final Object possibleBool = map.get(type);
        if (possibleBool instanceof Boolean bool) {
            return bool;
        }
        return false;
    }

    private static float getThermalInertia(Map<String, Object> map) {
        final Object possibleInertia = map.get("thermalInertia");
        if (possibleInertia instanceof Number num) {
            return num.floatValue();
        }
        return 1.0F;
    }

    private float temperature;
    private float targetTemperature;
    private final boolean consumeFuel;
    private final float thermalInertia;
    private final boolean handleCrucibleRecipes;
    private final HeatingRecipe[] cachedRecipes;
    @Nullable
    private final InventoryKJS inventoryKJS;
    private boolean needsRecipeUpdate;

    public HeatAttachemnt(BlockEntityJS entity, @Nullable OnCalendarUpdate updater, boolean consumeFuel, float thermalInertia, boolean handleCrucibleRecipes) {
        super(entity, updater);
        temperature = 0;
        targetTemperature = 0;
        this.consumeFuel = consumeFuel;
        this.thermalInertia = Math.max(0, thermalInertia);
        this.handleCrucibleRecipes = handleCrucibleRecipes;
        inventoryKJS = entity.inventory;
        if (this.handleCrucibleRecipes && inventoryKJS != null) {
            cachedRecipes = new HeatingRecipe[inventoryKJS.kjs$getSlots()];
            needsRecipeUpdate = true;
        } else {
            cachedRecipes = new HeatingRecipe[0];
            needsRecipeUpdate = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (needsRecipeUpdate && handleCrucibleRecipes) {
            needsRecipeUpdate = false;
            updateRecipeCache();
        }
    }

    private void updateRecipeCache() {
        for (int i = 0 ; i <= cachedRecipes.length ; i++) {
            cachedRecipes[i] = HeatingRecipe.getRecipe(inventoryKJS.kjs$getStackInSlot(i));
        }
    }

    @Override
    public void onCalendarUpdate(long ticks) {
        super.onCalendarUpdate(ticks);
        if (consumeFuel) {
            targetTemperature = adjustTemp(targetTemperature, 0, ticks);
        }
        temperature = adjustTemp(temperature, targetTemperature, ticks);
    }

    // Reimplementation of HeatCapability#adjustTempTowards that uses a custom thermal inertia instead of the global value
    private float adjustTemp(float temp, float target, float delta) {
        if (temp < target) {
            return Math.min(temp + thermalInertia * delta, target);
        } else if (temp > target) {
            return Math.max(temp - thermalInertia * delta, target);
        } else {
            return target;
        }
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(float temperature) {
        targetTemperature = temperature;
    }

    @Override
    public CompoundTag writeAttachment() {
        final CompoundTag tag = super.writeAttachment();
        tag.putFloat("temperature", temperature);
        tag.putFloat("targetTemperature", targetTemperature);
        return tag;
    }

    @Override
    public void readAttachment(CompoundTag tag) {
        super.readAttachment(tag);
        temperature = tag.getFloat("temperature");
        targetTemperature = tag.getFloat("targetTemperature");
    }
}
