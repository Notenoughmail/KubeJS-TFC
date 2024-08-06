package com.notenoughmail.kubejs_tfc.util.implementation;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;

// Stripped down version of MixinConstraints ifModLoaded constraint, but scoped down for my purposes
public class MixinLoadingUtil {

    private static final String PRESENT_DESC = Type.getDescriptor(IfPresent.class);

    public static boolean shouldApplyMixin(String mixinClassName) {
        try {
            final ClassNode node = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            if (node.visibleAnnotations != null) {
                for (AnnotationNode annotation : node.visibleAnnotations) {
                    if (!checkAnnotation(annotation)) {
                        return false;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            KubeJSTFC.error("Unable to determine if mixin should apply due to an exception", e);
        }
        return true; // The worst that happens is the log has some ugly warnings about missing classes
    }

    private static boolean checkAnnotation(AnnotationNode node) {
        if (PRESENT_DESC.equals(node.desc)) {
            String mod = null;
            for (int i = 0 ; i < node.values.size() ; i += 2) {
                if ("value".equals(node.values.get(i))) {
                    mod = (String) node.values.get(i + 1);
                }
            }
            if (mod == null) return false;
            return isModLoaded(mod);
        }

        return true;
    }


    private static boolean isModLoaded(String id) {
        if (ModList.get() == null) {
            return LoadingModList.get().getModFileById(id) != null;
        } else {
            return ModList.get().isLoaded(id);
        }
    }
}
