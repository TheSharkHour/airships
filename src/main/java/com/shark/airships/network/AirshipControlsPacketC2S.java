package com.shark.airships.network;

import com.shark.airships.entity.AirshipEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Packet class for Airship controls. Sends Player controls to the server.</p>
 */
public class AirshipControlsPacketC2S extends Packet implements ManagedPacket<AirshipControlsPacketC2S> {
    public static final PacketType<AirshipControlsPacketC2S> TYPE = PacketType.builder(false, true, AirshipControlsPacketC2S::new).build();
    private static final Logger log = LogManager.getLogger("[AIRSHIPS]");

    public boolean up;
    public boolean down;
    public boolean fire;

    public AirshipControlsPacketC2S() {
    }

    public AirshipControlsPacketC2S(boolean up, boolean down, boolean fire) {
        this.up = up;
        this.down = down;
        this.fire = fire;
    }

    @Override
    public void read(@NotNull DataInputStream stream) {
        try {
            this.up = stream.readBoolean();
            this.down = stream.readBoolean();
            this.fire = stream.readBoolean();
        } catch (IOException e) {
            log.warn("Failed to read AirshipControlsPacket!");
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeBoolean(up);
            stream.writeBoolean(down);
            stream.writeBoolean(fire);
        } catch (IOException e) {
            log.warn("Failed to write AirshipControlsPacket!");
            log.error(e.getLocalizedMessage(), e);
        }

    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> handleServer(networkHandler));
    }

    @Override
    public int size() {
        return 12;
    }

    @Override
    public @NotNull PacketType<AirshipControlsPacketC2S> getType() {
        return TYPE;
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);

        if (player != null && player.vehicle instanceof AirshipEntity airship) {
            airship.clientPressingUp = this.up;
            airship.clientPressingDown = this.down;
            airship.clientPressingFire = this.fire;
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);

        if (player != null && player.vehicle instanceof AirshipEntity airship) {
            airship.clientPressingUp = this.up;
            airship.clientPressingDown = this.down;
            airship.clientPressingFire = this.fire;
        }
    }
}
