package io.github.notenoughmail.kubejstfc.compat.firmalife;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.util.GreenhouseType;
import com.eerussianguy.firmalife.common.util.Plantable;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FirmaLifePlugin implements KubeJSPlugin {

    @Override
    public void init() {
        final DeferredRegister<DataType<?>> dataTypes = DeferredRegister.create(KubeJSTFCRegistries.DATA_TYPE_KEY, FirmaLife.MOD_ID);
        KubeJSTFC.modBus(dataTypes::register);
        dataTypes.register("greenhouse", () -> DataTypes.cachedRegistry(
                GreenhouseType.MANAGER,
                BuiltInRegistries.BLOCK,
                (g, p) -> p
                        .append("ingredient", g.ingredient())
                        .append("tier", g.tier())
                        .append("translationKey", g.translationKey(), true), // TODO 2.1.0 | This should probably be escaped somehow
                (g, b) -> g.ingredient().test(b),
                GreenhouseType.CACHE
        ));
        dataTypes.register("plantable", () -> DataTypes.cachedItemRegistry(
                Plantable.MANAGER,
                (l, p) -> p
                        .append("ingredient", l.ingredient())
                        .append("planter", l.planter())
                        .append("tier", l.tier())
                        .append("stages", l.stages())
                        .append("extraSeedChance", l.extraSeedChance())
                        .append("seed", l.seed())
                        .append("crop", l.crop())
                        .append("nutrient", l.nutrient())
                        .append("textures", l.textures())
                        .append("specials", l.specials(), true),
                (p, i) -> p.ingredient().kjs$testItem(i),
                Plantable.CACHE
        ));
    }
}
