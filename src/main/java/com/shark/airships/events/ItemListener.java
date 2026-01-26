package com.shark.airships.events;

import com.shark.airships.item.ItemAirship;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Namespace;

/**
 * @author TheSharkHour
 * @since 01/26/2026
 * <p>Listener Event class for item initialization</p>
 */
@SuppressWarnings("unused")
public class ItemListener {

    public static Item ITEM_AIRSHIP;
    public static Item ITEM_ENGINE;
    public static Item ITEM_BALLOON;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        ITEM_AIRSHIP = new ItemAirship(NAMESPACE.id("airship"))
                .setTranslationKey(NAMESPACE, "airship");

        ITEM_ENGINE = new TemplateItem(NAMESPACE.id("engine"))
                .setTranslationKey(NAMESPACE, "engine");

        ITEM_BALLOON = new TemplateItem(NAMESPACE.id("balloon"))
                .setTranslationKey(NAMESPACE, "balloon");
    }
}
