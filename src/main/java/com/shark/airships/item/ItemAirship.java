package com.shark.airships.item;

import com.shark.airships.entity.AirshipEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 01/26/2026
 * <p>Item class for the airship entity</p>
 */
public class ItemAirship extends TemplateItem {
    public ItemAirship(Identifier identifier) {
        super(identifier);
    }

    /**
     * <p>Use method for the airship item</p>
     * <p>Detects where the player is looking and spawns an airship at that position.</p>
     * @param itemStack The item
     * @param world The world
     * @param player The player
     * @return the item
     */
    @Override
    public ItemStack use(ItemStack itemStack, @NotNull World world, @NotNull PlayerEntity player) {
        float partialTick = 1.0F;
        float pitch = player.prevPitch + (player.pitch - player.prevPitch) * partialTick;
        float yaw = player.prevYaw + (player.yaw - player.prevYaw) * partialTick;

        double ix = player.prevX + (player.x - player.prevX) * (double)partialTick;
        double iy = player.prevY + (player.y - player.prevY) * (double)partialTick + 1.62 - (double)player.standingEyeHeight;
        double iz = player.prevZ + (player.z - player.prevZ) * (double)partialTick;
        Vec3d eyePos = Vec3d.createCached(ix, iy, iz);

        float lookDirX = MathHelper.cos(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float lookDirZ = MathHelper.sin(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float hScale = -MathHelper.cos(-pitch * ((float)Math.PI / 180F));
        float lookDirY = MathHelper.sin(-pitch * ((float)Math.PI / 180F));

        float fx = lookDirZ * hScale;
        float fz = lookDirX * hScale;
        double m = 5D;
        Vec3d target = eyePos.add((double)fx * m, (double)lookDirY * m, (double)fz * m);

        HitResult result = world.raycast(eyePos, target, true);
        if (result == null) return itemStack;

        if (result.type == HitResultType.BLOCK) {
            int hx = result.blockX;
            int hy = result.blockY;
            int hz = result.blockZ;

            if (!world.isRemote) {
                AirshipEntity airship = new AirshipEntity(world, hx, hy + 2.0D, hz);
                world.spawnEntity(airship);

                --itemStack.count;
            }
        }

        return itemStack;
    }
}
