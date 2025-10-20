package io.github.notenoughmail.kubejstfc.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.latvian.mods.kubejs.color.KubeColor;
import net.dries007.tfc.client.model.entity.WindmillBladeModel;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.Nullable;

public class KubeWindmillBladeModel extends WindmillBladeModel {

    public static WindmillBladeModel of(ModelPart root, @Nullable KubeColor color) {
        if (color == null) return new WindmillBladeModel(root);
        return new KubeWindmillBladeModel(root, color.kjs$getARGB());
    }

    private final int bladeColor;

    public KubeWindmillBladeModel(ModelPart root, int bladeColor) {
        super(root);
        this.bladeColor = bladeColor;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
        blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, bladeColor);
    }
}
