package io.github.notenoughmail.kubejstfc.compat.beneath;

import com.eerussianguy.beneath.common.component.LostPage;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.Context;
import io.github.notenoughmail.kubejstfc.events.server.KubeDataEvent;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import org.jetbrains.annotations.Nullable;

public class KubeBeneathDataEvent extends KubeDataEvent {

    public KubeBeneathDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    public void lostPage(Context ctx, LostPage page, @Nullable KubeResourceLocation id) {
        Assistant.notNull(page.cost(), "lostPage.cost", ctx);
        Assistant.notNull(page.reward(), "lostPage.reward", ctx);
        add(page, LostPage.MANAGER, id);
    }

    public void lostPage(Context ctx, LostPage page) {
        lostPage(ctx, page, null);
    }

    public void netherFertilizer(Context ctx, NetherFertilizer fertilizer, @Nullable KubeResourceLocation id) {
        Assistant.notNull(fertilizer.ingredient(), "netherFertilizer.ingredient", ctx);
        add(fertilizer, NetherFertilizer.MANAGER, id);
    }

    public void netherFetilizer(Context ctx, NetherFertilizer fertilizer) {
        netherFertilizer(ctx, fertilizer, null);
    }
}
