package com.shark.airships.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Quad;
import net.minecraft.client.model.Vertex;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Custom client-side quad class, handles custom texture sizes.</p>
 */
@Environment(EnvType.CLIENT)
public class CustomQuad extends Quad {

    public CustomQuad(Vertex[] vertices, int u1, int v1, int u2, int v2, float s1, float s2) {
        super(vertices);
        float var6 = 0.0015625F;
        float var7 = 0.003125F;
        vertices[0] = vertices[0].remap((float)u2 / s1 - var6, (float)v1 / s2 + var7);
        vertices[1] = vertices[1].remap((float)u1 / s1 + var6, (float)v1 / s2 + var7);
        vertices[2] = vertices[2].remap((float)u1 / s1 + var6, (float)v2 / s2 - var7);
        vertices[3] = vertices[3].remap((float)u2 / s1 - var6, (float)v2 / s2 - var7);
    }
}
