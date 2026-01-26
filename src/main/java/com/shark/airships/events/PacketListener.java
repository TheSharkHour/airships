package com.shark.airships.events;

import com.shark.airships.network.AirshipControlsPacketC2S;
import com.shark.airships.network.AirshipOpenGUIPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Listener Event for packet initialization</p>
 */
public class PacketListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("controlsPacket"), AirshipControlsPacketC2S.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("guiPacket"), AirshipOpenGUIPacket.TYPE);
    }
}
