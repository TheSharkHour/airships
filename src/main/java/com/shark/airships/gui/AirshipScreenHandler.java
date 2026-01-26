package com.shark.airships.gui;

import com.shark.airships.entity.AirshipEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Screen Handler for the Airship GUI</p>
 */
public class AirshipScreenHandler extends ScreenHandler {
    private final AirshipEntity airship;
    private int litTime;
    private int litDuration;

    public AirshipScreenHandler(PlayerInventory playerInventory, AirshipEntity airship) {
        this.airship = airship;

        this.addSlot(new Slot(airship, 13, 134, 16));
        this.addSlot(new Slot(airship, 12, 134, 52));

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 4; ++x) {
                this.addSlot(new Slot(airship, x + y * 4, 8 + x * 18, 16 + y * 18));
            }
        }

        for(int iy = 0; iy < 3; ++iy) {
            for(int ix = 0; ix < 9; ++ix) {
                this.addSlot(new Slot(playerInventory, ix + (iy + 1) * 9, 8 + ix * 18, 84 + iy * 18));
            }
        }

        for(int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(playerInventory, hotbar, 8 + hotbar * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return airship.canPlayerUse(player);
    }

    @Environment(EnvType.SERVER)
    @Override
    public void addListener(ScreenHandlerListener listener) {
        super.addListener(listener);
        listener.onPropertyUpdate(this, 0, airship.litTime);
        listener.onPropertyUpdate(this, 1, airship.litDuration);
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();

        for (Object listener : listeners) {
            ScreenHandlerListener shl = (ScreenHandlerListener) listener;
            if (this.litTime != airship.litTime) shl.onPropertyUpdate(this, 0, airship.litTime);
            if (this.litDuration != airship.litDuration) shl.onPropertyUpdate(this, 1, airship.litDuration);
        }

        this.litTime = airship.litTime;
        this.litDuration = airship.litDuration;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setProperty(int id, int value) {
        if (id == 0) airship.litTime = value;
        if (id == 1) airship.litDuration = value;
    }

    @Override
    public ItemStack quickMove(int slot) {
        ItemStack item = null;
        Slot iSlot = (Slot)this.slots.get(slot);

        if(iSlot != null && iSlot.hasStack()) {
            ItemStack itemIS = iSlot.getStack();
            item = itemIS.copy();

            if(slot >= 9 && slot < 36) this.insertItem(itemIS, 36, 45, false);
            else if(slot >= 36 && slot < 45) this.insertItem(itemIS, 9, 36, false);
            else this.insertItem(itemIS, 9, 45, false);

            if(itemIS.count == 0) iSlot.setStack(null);
            else iSlot.markDirty();

            if(itemIS.count == item.count) return null;
            iSlot.onTakeItem(itemIS);
        }

        return item;
    }
}
