package com.shark.airships.events;

import com.shark.airships.entity.AirshipEntity;
import com.shark.airships.gui.AirshipScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Listener Event for screen handler initialization</p>
 */
@SuppressWarnings("unused")
public class ScreenHandlerListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(@NotNull GuiHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("openAirship"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openAirship, () -> null));
    }

    @Environment(EnvType.CLIENT)
    @Contract("_, _ -> new")
    private @NotNull Screen openAirship(@NotNull PlayerEntity player, Inventory inventory) {
        return new AirshipScreen(player.inventory, (AirshipEntity) player.vehicle);
    }
}
