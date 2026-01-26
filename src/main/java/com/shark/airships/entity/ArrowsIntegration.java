package com.shark.airships.entity;

import net.danygames2014.elementalarrows.ElementalArrows;
import net.danygames2014.elementalarrows.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 01/26/2026
 * <p>Elemental Arrows integration.</p>
 */
public class ArrowsIntegration {
    public static boolean tryFireArrow(World world, PlayerEntity player, @NotNull ItemStack itemStack,
                                       double lx, double ly, double lz, Vec3d playerLook,
                                       Runnable consumeArrow) {
        var item = itemStack.getItem();

        if (item == ElementalArrows.eggArrow) {
            EggArrowEntity arrow = new EggArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(arrow);
            return true;
        }

        if (item == ElementalArrows.explosiveArrow) {
            ExplosiveArrowEntity arrow = new ExplosiveArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(arrow);
            return true;
        }

        if (item == ElementalArrows.fireArrow) {
            FireArrowEntity arrow = new FireArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(arrow);
            return true;
        }

        if (item == ElementalArrows.iceArrow) {
            IceArrowEntity arrow = new IceArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(arrow);
            return true;
        }

        if (item == ElementalArrows.lightingArrow) {
            LightingArrowEntity arrow = new LightingArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(arrow);
            return true;
        }

        if (item == ElementalArrows.torchArrow) {
            TorchArrowEntity arrow = new TorchArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            consumeArrow.run();
            world.spawnEntity(arrow);
            return true;
        }

        return false;
    }
}
