package com.notenoughmail.kubejs_tfc.util.helpers;

import net.dries007.tfc.common.capabilities.size.Size;

@FunctionalInterface
public interface SizePredicate {

    boolean test(Size size);
}
