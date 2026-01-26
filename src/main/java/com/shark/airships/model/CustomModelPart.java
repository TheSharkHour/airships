package com.shark.airships.model;

import com.shark.airships.mixin.ModelPartAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.Vertex;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Custom client-side model part class, handles custom texture sizes.</p>
 * <p>Might make this a custom library later.</p>
 */
@Environment(EnvType.CLIENT)
public class CustomModelPart extends ModelPart {
    public CustomModelPart(int u, int v) {
        super(u, v);
    }

    @Override
    public void addCuboid(float x, float y, float z, int sizeX, int sizeY, int sizeZ, float dilation) {
        ModelPartAccessor accessor = (ModelPartAccessor) this;

        Vertex[] corners = new Vertex[8];
        CustomQuad[] faces = new CustomQuad[6];

        int u = accessor.getU();
        int v = accessor.getV();

        float sx = x + (float)sizeX;
        float sy = y + (float)sizeY;
        float sz = z + (float)sizeZ;
        x -= dilation;
        y -= dilation;
        z -= dilation;
        sx += dilation;
        sy += dilation;
        sz += dilation;
        if (mirror) {
            float var11 = sx;
            sx = x;
            x = var11;
        }

        Vertex var26 = new Vertex(x, y, z, 0.0F, 0.0F);
        Vertex var12 = new Vertex(sx, y, z, 0.0F, 8.0F);
        Vertex var13 = new Vertex(sx, sy, z, 8.0F, 8.0F);
        Vertex var14 = new Vertex(x, sy, z, 8.0F, 0.0F);
        Vertex var15 = new Vertex(x, y, sz, 0.0F, 0.0F);
        Vertex var16 = new Vertex(sx, y, sz, 0.0F, 8.0F);
        Vertex var17 = new Vertex(sx, sy, sz, 8.0F, 8.0F);
        Vertex var18 = new Vertex(x, sy, sz, 8.0F, 0.0F);
        corners[0] = var26;
        corners[1] = var12;
        corners[2] = var13;
        corners[3] = var14;
        corners[4] = var15;
        corners[5] = var16;
        corners[6] = var17;
        corners[7] = var18;
        faces[0] = new CustomQuad(new Vertex[]{var16, var12, var13, var17}, u + sizeZ + sizeX, v + sizeZ, u + sizeZ + sizeX + sizeZ, v + sizeZ + sizeY, 64F, 64F);
        faces[1] = new CustomQuad(new Vertex[]{var26, var15, var18, var14}, u + 0, v + sizeZ, u + sizeZ, v + sizeZ + sizeY, 64F, 64F);
        faces[2] = new CustomQuad(new Vertex[]{var16, var15, var26, var12}, u + sizeZ, v + 0, u + sizeZ + sizeX, v + sizeZ, 64F, 64F);
        faces[3] = new CustomQuad(new Vertex[]{var13, var14, var18, var17}, u + sizeZ + sizeX, v + 0, u + sizeZ + sizeX + sizeX, v + sizeZ, 64F, 64F);
        faces[4] = new CustomQuad(new Vertex[]{var12, var26, var14, var13}, u + sizeZ, v + sizeZ, u + sizeZ + sizeX, v + sizeZ + sizeY, 64F, 64F);
        faces[5] = new CustomQuad(new Vertex[]{var15, var16, var17, var18}, u + sizeZ + sizeX + sizeZ, v + sizeZ, u + sizeZ + sizeX + sizeZ + sizeX, v + sizeZ + sizeY, 64F, 64F);

        if (mirror) {
            for (CustomQuad face : faces) {
                face.flip();
            }
        }

        accessor.setCorners(corners);
        accessor.setFaces(faces);
    }
}
