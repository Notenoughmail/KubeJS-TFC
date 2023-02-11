package com.notenoughmail.kubejs_tfc.item;

import net.dries007.tfc.common.TFCTiers;
import net.minecraft.world.item.Tier;

// Convenience enum for registering tiers w/ KubeJS
public enum TFCTiersJS {

    IGNEOUS_INTRUSIVE(TFCTiers.IGNEOUS_INTRUSIVE),
    IGNEOUS_EXTRUSIVE(TFCTiers.IGNEOUS_EXTRUSIVE),
    SEDIMENTARY(TFCTiers.SEDIMENTARY),
    METAMORPHIC(TFCTiers.METAMORPHIC),
    COPPER(TFCTiers.COPPER),
    BRONZE(TFCTiers.BRONZE),
    BISMUTH_BRONZE(TFCTiers.BISMUTH_BRONZE),
    BLACK_BRONZE(TFCTiers.BLACK_BRONZE),
    WROUGHT_IRON(TFCTiers.WROUGHT_IRON),
    STEEL(TFCTiers.STEEL),
    BLACK_STEEL(TFCTiers.BLACK_STEEL),
    BLUE_STEEL(TFCTiers.BLUE_STEEL),
    RED_STEEL(TFCTiers.RED_STEEL);

    private final Tier tier;

    TFCTiersJS(Tier tier) {
        this.tier = tier;
    }
    public Tier getTier() {
        return this.tier;
    }
}
