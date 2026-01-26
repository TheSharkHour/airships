package com.shark.airships.events.addon;

import com.shark.airships.events.ItemListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.jetbrains.annotations.NotNull;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

/**
 * @author TheSharkHour
 * @since 01/26/2026
 * <p>Listener Event for Creative Mode initialization</p>
 */
@SuppressWarnings("unused")
public class CreativeListener {
    public static CreativeTab TAB_AIRSHIPS;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void onTabInit(@NotNull TabRegistryEvent event) {
        TAB_AIRSHIPS = new SimpleTab(NAMESPACE.id("airships_tab"), new ItemStack(ItemListener.ITEM_AIRSHIP));
        event.register(TAB_AIRSHIPS);

        TAB_AIRSHIPS.addItem(new ItemStack(ItemListener.ITEM_ENGINE));
        TAB_AIRSHIPS.addItem(new ItemStack(ItemListener.ITEM_BALLOON));
        TAB_AIRSHIPS.addItem(new ItemStack(ItemListener.ITEM_AIRSHIP));
    }
}
