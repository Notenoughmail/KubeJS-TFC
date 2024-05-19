package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.NativeJavaObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

// UNUSED
// TODO: [Future] PR ticking attachments to Kube
public class CalendarTickingAttachment implements BlockEntityAttachment, ICalendarTickableJS {

    protected static final TypeDescJS UPDATE_DESC = new PrimitiveDescJS("BiConsumer<BlockEntityJS,long>");

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:calendarTickable",
            TypeDescJS.object()
                    .add("onCalendarUpdate", UPDATE_DESC, true),
            map -> entity -> new CalendarTickingAttachment(entity, getUpdater(map))
    );

    @Nullable
    protected static OnCalendarUpdate getUpdater(Map<String, Object> map) {
        final Object possibleFunction = map.get("onCalendarUpdate");
        if (possibleFunction instanceof BaseFunction func) {
            return (OnCalendarUpdate) NativeJavaObject.createInterfaceAdapter(ScriptType.STARTUP.manager.get().context, OnCalendarUpdate.class, func);
        }
        return null;
    }

    protected final BlockEntityJS entity;
    protected long lastUpdateTick;
    @Nullable
    protected final OnCalendarUpdate updater;

    public CalendarTickingAttachment(BlockEntityJS entity, @Nullable OnCalendarUpdate updater) {
        this.entity = entity;
        lastUpdateTick = Integer.MIN_VALUE;
        this.updater = updater;
    }

    // Currently required to be called by scripters
    public void tick() {
        final Level level = entity.getLevel();
        if (level != null && !level.isClientSide()) {
            checkForCalendarUpdate();
        }
    }

    @Override
    public void onCalendarUpdate(long ticks) {
        if (updater != null) {
            updater.update(entity, ticks);
        }
    }

    @Override
    public long getLastCalendarUpdateTick() {
        return lastUpdateTick;
    }

    @Override
    public void setLastCalendarUpdateTick(long tick) {
        lastUpdateTick = tick;
    }

    @Override
    public CompoundTag writeAttachment() {
        final CompoundTag tag = new CompoundTag();
        tag.putLong("lastUpdateTick", lastUpdateTick);
        return tag;
    }

    @Override
    public void readAttachment(CompoundTag tag) {
        lastUpdateTick = tag.getLong("lastUpdateTick");
    }

    @Override
    public BlockEntityJS getEntity() {
        return entity;
    }

    @FunctionalInterface
    public interface OnCalendarUpdate {
        void update(BlockEntityJS entity, long tickSkip);
    }
}
