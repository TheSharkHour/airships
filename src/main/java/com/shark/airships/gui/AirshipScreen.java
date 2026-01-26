package com.shark.airships.gui;

import com.shark.airships.entity.AirshipEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class AirshipScreen extends HandledScreen {
    private final AirshipEntity airship;

    public AirshipScreen(PlayerInventory playerInventory, AirshipEntity airship) {
        super(new AirshipScreenHandler(playerInventory, airship));
        this.airship = airship;
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw("Airship Inventory", 8, 4, 0x404040);
        textRenderer.draw("Arrows:", 89, 55, 0x404040);
        textRenderer.draw("Fuel:", 105, 20, 0x404040);
        textRenderer.draw("Inventory", 8, backgroundHeight - 96 + 2, 0x404040);
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int t = minecraft.textureManager.getTextureId("/assets/airships/stationapi/textures/gui/airship.png");
        GL11.glColor4f(1F, 1F, 1F, 1F);
        minecraft.textureManager.bindTexture(t);

        int cx = (width - backgroundWidth) / 2;
        int cy = (height - backgroundHeight) / 2;
        drawTexture(cx, cy, 0, 0, backgroundWidth, backgroundHeight);

        if (airship.litTime != 0) {
            int fuel = airship.getLitProgress(32);
            drawTexture(cx + 156, cy + 15 + 32 - fuel, 176, 32 - fuel, 12, fuel);
        }
    }

    @Override
    public void removed() {
        super.removed();
        handler.onClosed(minecraft.player);
    }
}
