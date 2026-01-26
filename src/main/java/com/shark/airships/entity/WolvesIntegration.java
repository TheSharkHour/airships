package com.shark.airships.entity;

import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 01/26/2026
 * <p>Better Than Wolves integration for firing arrows.</p>
 */
public class WolvesIntegration {
    public static boolean tryFireArrow(World world, PlayerEntity player, @NotNull ItemStack itemStack,
                                       double lx, double ly, double lz, Vec3d playerLook,
                                       Runnable consumeArrow) {
        if (itemStack.getItem() == ItemListener.broadHeadArrow) {
            BroadheadArrowEntity broadheadArrow = new BroadheadArrowEntity(world, player);
            broadheadArrow.setPosition(lx, ly, lz);
            broadheadArrow.method_1291(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(broadheadArrow);
            return true;
        }
        return false;
    }
}
