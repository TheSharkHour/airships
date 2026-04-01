package com.shark.airships.model;

import com.shark.airships.Airships;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModel;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side model for the Airship.</p>
 */
@Environment(EnvType.CLIENT)
public class AirshipEntityModel extends EntityModel {
    private float bladeSpin = 0.0F;

    public CustomModelPart[] boxes = new CustomModelPart[25];

    // TODO - Document what each 'box' is
    public AirshipEntityModel() {
        byte byte0 = 24;
        byte byte1 = 6;
        byte byte2 = 20;
        byte byte3 = 4;

        // Basket main body
        this.boxes[0] = new CustomModelPart(0, 8);
        this.boxes[0].addCuboid(-12.0F, -8.0F, -3.0F, 24, 16, 4, 0.0F);
        this.boxes[0].setPivot(0.0F, byte3, 0.0F);
        this.boxes[0].pitch = (float)(Math.PI / 2F);

        this.boxes[1] = new CustomModelPart(0, 0);
        this.boxes[1].addCuboid((float)(-byte0 / 2 + 2), (float)(-byte1 - 1), -1.0F, byte0 - 4, byte1, 2, 0.0F);
        this.boxes[1].setPivot((float)(-byte0 / 2 + 1), byte3, 0.0F);
        this.boxes[1].yaw = (float)Math.PI * 3.0F / 2.0F;

        this.boxes[2] = new CustomModelPart(0, 0);
        this.boxes[2].addCuboid((float)(-byte0 / 2 + 2), (float)(-byte1 - 1), -1.0F, byte0 - 4, byte1, 2, 0.0F);
        this.boxes[2].setPivot((float)(byte0 / 2 - 1), byte3, 0.0F);
        this.boxes[2].yaw = (float)(Math.PI / 2F);

        this.boxes[3] = new CustomModelPart(0, 0);
        this.boxes[3].addCuboid((float)(-byte0 / 2 + 2), (float)(-byte1 - 1), -1.0F, byte0 - 4, byte1, 2, 0.0F);
        this.boxes[3].setPivot(0.0F, byte3, (float)(-byte2 / 2 + 1));
        this.boxes[3].yaw = (float) Math.PI;

        this.boxes[4] = new CustomModelPart(0, 0);
        this.boxes[4].addCuboid((float)(-byte0 / 2 + 2), (float)(-byte1 - 1), -1.0F, byte0 - 4, byte1, 2, 0.0F);
        this.boxes[4].setPivot(0.0F, byte3, (float)(byte2 / 2 - 1));

        this.boxes[5] = new CustomModelPart(56, 0);
        this.boxes[5].addCuboid(-2.0F, -13.0F, -1.0F, 2, byte0, 2, 0.0F);
        this.boxes[5].setPivot(-12.0F, -12.5F, (float)(-byte2 / 2) - 3.5F);
        this.boxes[5].yaw = (float)(Math.PI / 8F);
        this.boxes[5].pitch = (float)(Math.PI / 8F);

        this.boxes[6] = new CustomModelPart(56, 0);
        this.boxes[6].addCuboid(-2.0F, -13.0F, -1.0F, 2, byte0, 2, 0.0F);
        this.boxes[6].setPivot(-12.0F, -12.5F, (float)(byte2 / 2) + 3.5F);
        this.boxes[6].yaw = -(float)(Math.PI / 8F);
        this.boxes[6].pitch = -(float)(Math.PI / 8F);

        this.boxes[7] = new CustomModelPart(56, 0);
        this.boxes[7].addCuboid(-2.0F, -13.0F, -1.0F, 2, byte0, 2, 0.0F);
        this.boxes[7].setPivot(13.5F, -12.5F, (float)(-byte2 / 2 - 2));
        this.boxes[7].yaw = -(float)(Math.PI / 8F);
        this.boxes[7].pitch = (float)(Math.PI / 8F);

        this.boxes[8] = new CustomModelPart(56, 0);
        this.boxes[8].addCuboid(-2.0F, -13.0F, -1.0F, 2, byte0, 2, 0.0F);
        this.boxes[8].setPivot(13.5F, -12.5F, (float)(byte2 / 2 + 2));
        this.boxes[8].yaw = (float)(Math.PI / 8F);
        this.boxes[8].pitch = -(float)(Math.PI / 8F);

        this.boxes[9] = new CustomModelPart(36, 39);
        this.boxes[9].addCuboid(-8.0F, -4.0F, 0.0F, 10, 8, 4, 1.0F);
        this.boxes[9].setPivot(0.0F, 3.0F, (float)(byte0 / 2) + 9.0F);
        this.boxes[9].yaw = (float)Math.PI;

        this.boxes[10] = new CustomModelPart(36, 39);
        this.boxes[10].addCuboid(-8.0F, -4.0F, 0.0F, 10, 8, 4, 1.0F);
        this.boxes[10].setPivot(0.0F, 3.0F, (float)(-byte0 / 2) - 5.0F);
        this.boxes[10].yaw = (float)Math.PI;

        this.boxes[11] = new CustomModelPart(0, 55);
        this.boxes[11].addCuboid(-6.0F, -0.5F, -0.5F, 12, 1, 2, 0.0F);
        this.boxes[11].setPivot(9.5F, 0.0F, (float)(byte0 / 2) + 7.0F);
        this.boxes[11].pitch = (float)(Math.PI / 2F);
        this.boxes[11].roll = (float)(Math.PI / 2F);

        this.boxes[12] = new CustomModelPart(0, 55);
        this.boxes[12].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[12].setPivot(9.5F, 0.0F, (float)(byte0 / 2) + 7.0F);
        this.boxes[12].pitch = (float)(Math.PI / 2F);
        this.boxes[12].roll = (float)(Math.PI / 2F);

        this.boxes[13] = new CustomModelPart(0, 55);
        this.boxes[13].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[13].setPivot(9.5F, 0.0F, (float)(byte0 / 2) + 7.0F);
        this.boxes[13].pitch = (float)(Math.PI / 2F);
        this.boxes[13].roll = (float)(Math.PI / 2F);

        this.boxes[14] = new CustomModelPart(0, 55);
        this.boxes[14].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[14].setPivot(9.5F, 0.0F, (float)(byte0 / 2) + 7.0F);
        this.boxes[14].pitch = (float)(Math.PI / 2F);
        this.boxes[14].roll = (float)(Math.PI / 2F);

        this.boxes[15] = new CustomModelPart(0, 55);
        this.boxes[15].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[15].setPivot(9.5F, 0.0F, (float)(-byte0 / 2) - 7.0F);
        this.boxes[15].pitch = (float)(Math.PI / 2F);
        this.boxes[15].roll = (float)(Math.PI / 2F);

        this.boxes[16] = new CustomModelPart(0, 55);
        this.boxes[16].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[16].setPivot(9.5F, 0.0F, (float)(-byte0 / 2) - 7.0F);
        this.boxes[16].pitch = (float)(Math.PI / 2F);
        this.boxes[16].roll = (float)(Math.PI / 2F);

        this.boxes[17] = new CustomModelPart(0, 55);
        this.boxes[17].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[17].setPivot(9.5F, 0.0F, (float)(-byte0 / 2) - 7.0F);
        this.boxes[17].pitch = (float)(Math.PI / 2F);
        this.boxes[17].roll = (float)(Math.PI / 2F);

        this.boxes[18] = new CustomModelPart(0, 55);
        this.boxes[18].addCuboid((float)(-byte3 - 2), -0.5F, -0.5F, byte0 / 2, 1, 2, 0.0F);
        this.boxes[18].setPivot(9.5F, 0.0F, (float)(-byte0 / 2) - 7.0F);
        this.boxes[18].pitch = (float)(Math.PI / 2F);
        this.boxes[18].roll = (float)(Math.PI / 2F);

        this.boxes[19] = new CustomModelPart(56, 0);
        this.boxes[19].addCuboid(-2.0F, -2.0F, -1.0F, 2, 13, 2, 0.0F);
        this.boxes[19].setPivot(0.0F, 3.0F, (float)(-byte0 / 2) - 7.0F);
        this.boxes[19].pitch = (float)(Math.PI / 2F);

        this.boxes[20] = new CustomModelPart(56, 0);
        this.boxes[20].addCuboid(-2.0F, -2.0F, -1.0F, 2, 13, 2, 0.0F);
        this.boxes[20].setPivot(6.0F, 3.0F, (float)(-byte0 / 2) - 7.0F);
        this.boxes[20].pitch = (float)(Math.PI / 2F);

        this.boxes[21] = new CustomModelPart(56, 0);
        this.boxes[21].addCuboid(-2.0F, -2.0F, -1.0F, 2, 13, 2, 0.0F);
        this.boxes[21].setPivot(0.0F, 3.0F, (float)(byte0 / 2) - 2.0F);
        this.boxes[21].pitch = (float)(Math.PI / 2F);

        this.boxes[22] = new CustomModelPart(56, 0);
        this.boxes[22].addCuboid(-2.0F, -2.0F, -1.0F, 2, 13, 2, 0.0F);
        this.boxes[22].setPivot(6.0F, 3.0F, (float)(byte0 / 2) - 2.0F);
        this.boxes[22].pitch = (float)(Math.PI / 2F);

        // Boiler
		this.boxes[23] = new CustomModelPart(0, 43);
		this.boxes[23].addCuboid(-8.0F, -4.0F, 0.0F, 10, 8, 4, 0.0F);
		this.boxes[23].setPivot(0.0F, 3.0F, 0.0F);
		this.boxes[23].pitch = (float)(Math.PI / 2F);

		this.boxes[24] = new CustomModelPart(28, 44);
		this.boxes[24].addCuboid(-2.0F, -13.0F, -1.0F, 2, 14, 2, 0.0F);
		this.boxes[24].setPivot(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
		bladeSpin += 0.005F;

		while (bladeSpin >= 180F) bladeSpin -= 360F;
		while (bladeSpin < -180F) bladeSpin += 360F;

        // Animate propellers
        this.boxes[11].yaw = this.bladeSpin;
        this.boxes[12].yaw = (float) (this.bladeSpin + (Math.PI / 4));
        this.boxes[13].yaw = (float) (this.bladeSpin + (Math.PI / 2));
        this.boxes[14].yaw = (float) (this.bladeSpin + Math.PI / 1.33F);
        this.boxes[15].yaw = -this.bladeSpin;
        this.boxes[16].yaw = -(float) (this.bladeSpin + (Math.PI / 4));
        this.boxes[17].yaw = -(float) (this.bladeSpin + (Math.PI / 2));
        this.boxes[18].yaw = -(float) (this.bladeSpin + Math.PI / 1.33F);

		for (int i = 0; i < 23; i++) {
			boxes[i].render(scale);
		}

		if (Airships.MISC_CONFIG.showBoiler) {
			boxes[23].render(scale);
			boxes[24].render(scale);
		}
    }
}