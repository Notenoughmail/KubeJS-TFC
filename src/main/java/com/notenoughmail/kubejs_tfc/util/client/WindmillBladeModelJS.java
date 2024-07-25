package com.notenoughmail.kubejs_tfc.util.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dries007.tfc.client.model.entity.WindmillBladeModel;
import net.minecraft.client.model.geom.ModelPart;

public class WindmillBladeModelJS extends WindmillBladeModel {

    private final ModelPart main, blade;
    private final float[] color;

    public WindmillBladeModelJS(ModelPart root, float[] color) {
        super(root);
        main = root.getChild("main");
        blade = root.getChild("blade");
        this.color = color;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1F, 1F, 1F, alpha);
        blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, color[0], color[1], color[2], alpha);
    }
}
