package com.shark.airships.mixin;

import com.shark.airships.entity.AirshipEntity;
import com.shark.airships.events.KeybindListener;
import com.shark.airships.network.AirshipControlsPacketC2S;
import com.shark.airships.network.AirshipOpenGUIPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side mixin for the Minecraft class. Handles controls in the 'tick' method.</p>
 */
@Environment(EnvType.CLIENT)
@Mixin(value = Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public Screen currentScreen;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z"))
    private void airships_tick(CallbackInfo ci) {
        if (player == null || player.dead || !(player.vehicle instanceof AirshipEntity)) return;

        if (Keyboard.getEventKeyState() && currentScreen == null) {
            if (Keyboard.getEventKey() == KeybindListener.KEY_CHEST.code) {
                AirshipOpenGUIPacket openGUI = new AirshipOpenGUIPacket();
                PacketHelper.send(openGUI);
            }

            boolean up = Keyboard.isKeyDown(KeybindListener.KEY_UP.code);
            boolean down = Keyboard.isKeyDown(KeybindListener.KEY_DOWN.code);
            boolean fire = Keyboard.isKeyDown(KeybindListener.KEY_FIRE.code);

            AirshipControlsPacketC2S controlsPacket = new AirshipControlsPacketC2S(up, down, fire);
            PacketHelper.send(controlsPacket);
        }
    }
}
