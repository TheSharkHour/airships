package com.shark.airships.model.render;

import com.shark.airships.entity.AirshipEntity;
import com.shark.airships.model.AirshipEntityModel;
import com.shark.airships.model.BalloonEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side renderer class for the Airship and balloon models.</p>
 */
@Environment(EnvType.CLIENT)
public class AirshipEntityRenderer extends EntityRenderer {
    private final EntityModel airshipModel;
    private final EntityModel balloonModel;

    public AirshipEntityRenderer() {
        shadowRadius = 0.5F;
        this.airshipModel = new AirshipEntityModel();
        this.balloonModel = new BalloonEntityModel();
    }

    /**
     * Renders the airship and balloon models.
     * <p>Animates when the airship is hurt and renders balloon colors.</p>
     * @param entity The airship entity.
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param yaw Yaw rotation
     * @param pitch Pitch rotation (Might actually be partialTick?)
     */
    @Override
    public void render(Entity entity, double x, double y, double z, float yaw, float pitch) {
        AirshipEntity airshipEntity = (AirshipEntity) entity;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotated(180F - yaw, 0F, 1F, 0F);
        float hurtTime = airshipEntity.hurtTime - pitch;
        float damageScale = airshipEntity.damage - pitch;

        if (damageScale < 0F) damageScale = 0;
        if (damageScale > 0F) GL11.glRotatef(
                ((MathHelper.sin(hurtTime) * hurtTime * damageScale) / 10F) * airshipEntity.hurtDir,
                1F,
                0F,
                0F
        );

        // Setup the initial texture and scale
        bindTexture("/terrain.png");
        float scale = 0.75F;
        GL11.glScalef(scale, scale, scale);
        GL11.glScalef(1F / scale, 1F / scale, 1F / scale);


        // Bind the airship texture and render the airship model
        bindTexture("/assets/airships/stationapi/textures/entity/airship/0.png");
        GL11.glScalef(-1F, -1F, 1F);
        airshipModel.render(0F, 0F, -0.1F, 0F, 0F, 0.0625F);

        // Now bind the balloon texture
        bindTexture("/assets/airships/stationapi/textures/entity/balloon/0.png");
        GL11.glScalef(4F, 4F, 4F);
        GL11.glTranslatef(0F, -0.85F, 0F);

        // Render the balloon color based on the airship's color and render the balloon model
        float brightness = airshipEntity.getBrightnessAtEyes(pitch);
        int balloonColor = airshipEntity.getBalloonColor();
        GL11.glColor3f(brightness * AirshipEntity.COLORS[balloonColor][0],
                brightness * AirshipEntity.COLORS[balloonColor][1],
                brightness * AirshipEntity.COLORS[balloonColor][2]);
        GL11.glRotatef(90F, 0F, 1F, 0F);

        balloonModel.render(0F, 0F, -0.1F, 0F, 0F, 0.0625F);

        // Bind an additional "face" layer so it's not colored.
        bindTexture("/assets/airships/stationapi/textures/entity/balloon/0_face.png");
        GL11.glColor3f(brightness, brightness, brightness);
        balloonModel.render(0F, 0F, -0.1F, 0F, 0F, 0.0625F);

        GL11.glPopMatrix();
    }
}
