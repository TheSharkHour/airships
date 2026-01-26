package com.shark.airships.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side listener event for keybind initialization</p>
 */
@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class KeybindListener {
    public static KeyBinding KEY_UP;
    public static KeyBinding KEY_DOWN;
    public static KeyBinding KEY_CHEST;
    public static KeyBinding KEY_FIRE;

    @EventListener
    public void registerKeybinds(@NotNull KeyBindingRegisterEvent event) {
        KEY_UP = new KeyBinding("key.airships.up", Keyboard.KEY_SPACE);
        KEY_DOWN = new KeyBinding("key.airships.down", Keyboard.KEY_LSHIFT);
        KEY_CHEST = new KeyBinding("key.airships.chest", Keyboard.KEY_RCONTROL);
        KEY_FIRE = new KeyBinding("key.airships.fire", Keyboard.KEY_LCONTROL);

        event.keyBindings.add(KEY_UP);
        event.keyBindings.add(KEY_DOWN);
        event.keyBindings.add(KEY_CHEST);
        event.keyBindings.add(KEY_FIRE);
    }
}
