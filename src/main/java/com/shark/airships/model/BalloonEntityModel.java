package com.shark.airships.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModel;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side model class for the Balloon.</p>
 */
@Environment(EnvType.CLIENT)
public class BalloonEntityModel extends EntityModel {
    public CustomModelPart boxes = new CustomModelPart(0, 0);

    public BalloonEntityModel() {
        this.boxes = new CustomModelPart(0, 0);
        this.boxes.addCuboid(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
    }

    public void render(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
        this.boxes.render(scale);
    }
}
