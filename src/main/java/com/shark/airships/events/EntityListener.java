package com.shark.airships.events;

import com.shark.airships.entity.AirshipEntity;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EntityListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerEntities(@NotNull EntityRegister event) {
        event.register(AirshipEntity.class, NAMESPACE.id("Airship").toString());
    }

    @EventListener
    public static void registerMobHandlers(@NotNull EntityHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("Airship"), AirshipEntity::new);
    }
}
