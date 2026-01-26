package com.shark.airships.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side particle class for the airship steam.</p>
 */
@Environment(EnvType.CLIENT)
public class EntitySteamParticle extends Particle {
    public EntitySteamParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX + (Math.random() * 2D - 1D) * 0.05F;
        this.velocityY = velocityY + (Math.random() * 2D - 1D) * 0.05F;
        this.velocityZ = velocityZ + (Math.random() * 2D - 1D) * 0.05F;

        red = 230F;
        green = 230F;
        blue = 230F;

        scale = random.nextFloat() * random.nextFloat() * 6.0F + 1.0F;
        maxParticleAge = (int)(16D / (random.nextFloat() * 0.8D + 0.2D)) + 2;
    }

    @Override
    public void tick() {
        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;
        if (this.age++ >= this.maxParticleAge) this.markDead();

        textureId = 7 - particleAge * 8 / maxParticleAge;
        velocityY += 0.008D;
        move(velocityX, velocityY, velocityZ);

        velocityX *= 0.9D;
        velocityY *= 0.9D;
        velocityZ *= 0.9D;
        if (onGround) {
            velocityX *= 0.7D;
            velocityZ *= 0.7D;
        }
    }
}
