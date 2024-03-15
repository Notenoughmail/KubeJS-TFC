package com.notenoughmail.kubejs_tfc.util.helpers;

import net.dries007.tfc.common.capabilities.size.Weight;

@FunctionalInterface
public interface WeightPredicate {

    boolean test(Weight weight);
}
