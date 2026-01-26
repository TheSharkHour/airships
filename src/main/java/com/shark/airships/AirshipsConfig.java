package com.shark.airships;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class AirshipsConfig {
    public static class MiscellaneousConfig {
        @ConfigEntry(name = "Show Boiler", description = "Whether to show the boiler (REQUIRES RESTART)")
        public Boolean showBoiler = false;
    }
}
