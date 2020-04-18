package ru.glitchless.telegrambridge.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class TelegramConfigHelper {

    private static Configuration config;

    public static void init(Configuration newConfig) {
        config = newConfig;
        config.load();
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void setLastInteractionTime(long lastInteractionTime) {
        TelegramBridgeConfig.last_interaction_time = String.valueOf(lastInteractionTime);
        Property property = config.getCategory("general").get("last_interaction_time");
        property.set(TelegramBridgeConfig.last_interaction_time);
        if (config.hasChanged()) {
            config.save();
        }
    }
}
