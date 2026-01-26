package com.shark.airships;

import net.fabricmc.api.ModInitializer;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Airships implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Airships");

    @ConfigRoot(value = "miscellaneous", visibleName = "Miscellaneous Options", index = 0)
    public static final AirshipsConfig.MiscellaneousConfig MISC_CONFIG = new AirshipsConfig.MiscellaneousConfig();

    @Override
    public void onInitialize() {
    }
}
