package com.shark.airships.events;

import com.shark.airships.entity.AirshipEntity;
import com.shark.airships.model.render.AirshipEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Client-side listener event for entity renderer initialization</p>
 */
@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class EntityRendererListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerEntityRenderers(@NotNull EntityRendererRegisterEvent event) {
        event.renderers.put(AirshipEntity.class, new AirshipEntityRenderer());
    }
}
