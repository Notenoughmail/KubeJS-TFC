package com.notenoughmail.kubejs_tfc.util.implementation.containerlimiter;

import com.mojang.datafixers.util.Pair;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import net.dries007.tfc.common.capabilities.size.Size;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SemiFunctionalContainerLimiterEventJS extends StartupEventJS {

    // Suck it Hunter Gratzner
    public static final Map<ResourceLocation, Pair<Size, List<Pair<Integer, Integer>>>> LIMITED_SIZES = new HashMap<>();

    public void limitContainerSize(ResourceLocation containerName, String size) {
        limitContainerSize(containerName, size, (Integer) null);
    }

    // /kubejs dump_registry minecraft:menu
    public void limitContainerSize(ResourceLocation containerName, String size, @Nullable Integer... slotRanges) {
        if (slotRanges != null && slotRanges.length % 2 != 0) {
            KubeJSTFC.LOGGER.error("Provided slot ranges: '{}' are not provided in pairs! They will be ignored!", (Object) slotRanges);
            slotRanges = null;
        }

        List<Pair<Integer, Integer>> ranges = new ArrayList<>();
        if (slotRanges != null) {
            for (int i = 0; i < slotRanges.length ; i += 2) {
                ranges.add(new Pair<>(slotRanges[i], slotRanges[i + 1] + 1)); // Modify input values so the provided slot values are treated as inclusive -> inclusive instead of inclusive -> exclusive
            }
        }
        Size sizeValue = Size.valueOf(size.toUpperCase(Locale.ROOT));
        LIMITED_SIZES.put(containerName, Pair.of(sizeValue, ranges));

        List<String> rangeDescriptors = new ArrayList<>();
        if (ranges.isEmpty()) {
            rangeDescriptors.add("ALL");
        } else {
            for (Pair<Integer, Integer> range : ranges) {
                rangeDescriptors.add("[" + range.getFirst() + ", " + (range.getSecond() - 1) + "]");
            }
        }

        KubeJSTFC.LOGGER.info("Limited container menu {} to size {} for slots: {}", containerName, sizeValue, rangeDescriptors);
    }
}
