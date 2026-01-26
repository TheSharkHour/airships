package com.shark.airships.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.Quad;
import net.minecraft.client.model.Vertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>AClient-side accessor for the ModelPart class.</p>
 */
@Environment(EnvType.CLIENT)
@Mixin(ModelPart.class)
public interface ModelPartAccessor {
    @Accessor("corners")
    void setCorners(Vertex[] corners);

    @Accessor("faces")
    void setFaces(Quad[] faces);

    @Accessor("u")
    int getU();

    @Accessor("v")
    int getV();

    @Accessor("mirror")
    boolean isMirror();
}