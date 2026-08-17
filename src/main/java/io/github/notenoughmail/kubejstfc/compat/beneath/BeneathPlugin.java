package io.github.notenoughmail.kubejstfc.compat.beneath;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.component.LostPage;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;

public class BeneathPlugin implements KubeJSPlugin {

    @Override
    public void init() {
        final DeferredRegister<DataType<?>> dataTypes = DeferredRegister.create(KubeJSTFCRegistries.DATA_TYPE_KEY, Beneath.MOD_ID);
        KubeJSTFC.modBus(dataTypes::register);
        dataTypes.register("nether_fertilizer", () -> DataTypes.cachedItemRegistry(
                NetherFertilizer.MANAGER,
                (f, p) -> p
                        .append("ingredient", f.ingredient())
                        .append("death", f.death())
                        .append("destruction", f.destruction())
                        .append("decay", f.decay())
                        .append("sorrow", f.sorrow())
                        .append("flame", f.flame(), true),
                (f, i) -> f.ingredient().kjs$testItem(i),
                NetherFertilizer.CACHE
        ));
        dataTypes.register("lost_page", () -> DataTypes.registry(
                LostPage.MANAGER,
                BuiltInRegistries.ITEM,
                (l, p) -> p
                        .append("cost", l.cost())
                        .append("costs", l.costs())
                        .append("reward", l.reward())
                        .append("rewards", l.rewards())
                        .append("punishments", l.punishments())
                        .append("translation", l.translation(), true),
                (l, i) -> l.cost().kjs$testItem(i),
                () -> LostPage.MANAGER.getValues().stream()
                        .map(LostPage::cost)
                        .map(Ingredient::getItems)
                        .flatMap(Arrays::stream)
                        .map(ItemStack::getItem)
                        .distinct()
        ));
    }
}
