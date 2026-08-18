package io.github.notenoughmail.kubejstfc.compat.firmalife;

import com.eerussianguy.firmalife.common.util.GreenhouseType;
import com.eerussianguy.firmalife.common.util.Plantable;
import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.Context;
import io.github.notenoughmail.kubejstfc.events.server.KubeDataEvent;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import org.jetbrains.annotations.Nullable;

public class KubeFirmaLifeDataEvent extends KubeDataEvent {

    public KubeFirmaLifeDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    public void greenhouseType(Context ctx, GreenhouseType type, @Nullable KubeResourceLocation id) {
        Assistant.notNull(type.ingredient(), "greenhouseType.ingredient", ctx);
        Assistant.notNull(type.getTitle(), "greenhouseType.translationKey", ctx);
        add(type, GreenhouseType.MANAGER, id);
    }

    public void greenhouseType(Context ctx, GreenhouseType type) {
        greenhouseType(ctx, type, null);
    }

    public void plantable(Context ctx, Plantable plantable, @Nullable KubeResourceLocation id) {
        Assistant.notNull(plantable.ingredient(), "plantable.ingredient", ctx);
        Assistant.notNull(plantable.planter(), "plantable.planter", ctx);
        Assistant.notNull(plantable.seed(), "plantable.seed", ctx);
        Assistant.notNull(plantable.crop(), "plantable.crop", ctx);
        add(plantable, Plantable.MANAGER, id);
    }

    public void plantable(Context ctx, Plantable plantable) {
        plantable(ctx, plantable, null);
    }
}
