package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ReturnsSelf
public class FoodTraitBuilder extends BuilderBase<FoodTrait> {

    public transient Supplier<Double> decayModifier = () -> 1D;
    @Nullable
    public transient String tooltipKey;
    @Nullable
    public transient Component tooltipText;

    public FoodTraitBuilder(ResourceLocation id) {
        super(id);
    }

    @Info("Sets the decay modifier of the the trait")
    public FoodTraitBuilder decayModifier(double modifier) {
        return decayModifierSupplier(() -> modifier);
    }

    @Info("Sets the decay modifier, as a supplier, of the trait")
    public FoodTraitBuilder decayModifierSupplier(Supplier<Double> modifier) {
        decayModifier = modifier;
        return this;
    }

    @Info("Gives the trait a tooltip with the given lang key")
    public FoodTraitBuilder tooltipKey(String key) {
        tooltipKey = key;
        return this;
    }

    @Info("Automatically adds the given text as the trait's tooltip")
    public FoodTraitBuilder tooltipText(Component text) {
        if (tooltipKey == null) {
            tooltipKey = id.toLanguageKey("tooltip.food_trait");
        }
        tooltipText = text;
        return this;
    }

    @Override
    public FoodTrait createObject() {
        return new FoodTrait(decayModifier, tooltipKey);
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        if (tooltipText != null && tooltipKey != null) {
            lang.add(id.getNamespace(), tooltipKey, tooltipText.getString());
        }
    }
}
