package com.shark.airships.mixin;

import com.shark.airships.particle.EntitySteamParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side mixin for the WorldRenderer class. Handles steam particle rendering.</p>
 */
@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class)
public abstract class WorldRendererMixin implements GameEventListener {

    @Shadow
    private Minecraft client;

    @Shadow
    private World world;

    @Inject(method = "addParticle", at = @At("HEAD"))
    private void airships_addParticle(String particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (client == null || client.camera == null || client.particleManager == null) return;
        double nx = this.client.camera.x - x;
        double ny = this.client.camera.y - y;
        double nz = this.client.camera.z - z;
        double mDist = 16D;
        if (nx * nx + ny * ny + nz * nz > mDist * mDist) return;

        if (particle.equals("AirshipsSteam"))
            client.particleManager.addParticle(new EntitySteamParticle(world,
                    x,
                    y,
                    z,
                    velocityX,
                    velocityY,
                    velocityZ)
            );
    }
}
