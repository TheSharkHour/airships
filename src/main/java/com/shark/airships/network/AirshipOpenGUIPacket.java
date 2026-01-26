package com.shark.airships.network;

import com.shark.airships.entity.AirshipEntity;
import com.shark.airships.gui.AirshipScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>GUI Packet handler for the Airship. Sends the UI to the client.</p>
 * <p>Not really sure what I'm doing here, honestly. Packets are new to me.</p>
 */
public class AirshipOpenGUIPacket extends Packet implements ManagedPacket<AirshipOpenGUIPacket> {
    public static final PacketType<AirshipOpenGUIPacket> TYPE = PacketType.builder(false, true, AirshipOpenGUIPacket::new).build();
    private static final Logger log = LogManager.getLogger("[AIRSHIPS]");

    public AirshipOpenGUIPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        // No data
    }

    @Override
    public void write(DataOutputStream stream) {
        // No data
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> handleServer(networkHandler));
    }

    @Environment(EnvType.CLIENT)
    private void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if (player == null || !(player.vehicle instanceof AirshipEntity)) return;
        AirshipEntity airship = (AirshipEntity) player.vehicle;

        GuiHelper.openGUI(
                player,
                Identifier.of(Namespace.of("airships"), "openAirship"),
                airship,
                new AirshipScreenHandler(player.inventory, airship)
        );
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if (player == null || !(player.vehicle instanceof AirshipEntity)) return;
        AirshipEntity airship = (AirshipEntity) player.vehicle;

        GuiHelper.openGUI(
                player,
                Identifier.of(Namespace.of("airships"), "openAirship"),
                airship,
                new AirshipScreenHandler(player.inventory, airship)
        );
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public @NotNull PacketType<AirshipOpenGUIPacket> getType() {
        return TYPE;
    }
}
