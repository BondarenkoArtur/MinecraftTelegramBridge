package ru.glitchless.telegrambridge.config;

import net.minecraftforge.common.config.Config;

import static ru.glitchless.telegrambridge.TelegramBridgeMod.MODID;
import static ru.glitchless.telegrambridge.config.TelegramBridgeConfig.RelayMode.TWO_SIDE;

@Config(modid = MODID)
public class TelegramBridgeConfig {
    public static TelegramConfig telegram_config = new TelegramConfig();

    @Config.Comment("list of telegram chats that messages will be relayed to")
    public static String[] chat_ids = new String[]{"-1", "-2"};

    @Config.Comment("Init only on server")
    public static boolean server_only = true;

    @Config.Comment("Verbose logging for telegram")
    public static boolean verbose_logging = false;

    public static RelayMode relay_mode = TWO_SIDE;

    public static class TelegramConfig {
        @Config.Comment("the bot api token")
        public String api_token = "";

        @Config.Comment("timeout in seconds for long pooling update")
        public int telegram_long_pooling_timeout = 100;
    }

    public enum RelayMode {
        NONE,
        TO_MINECRAFT,
        TO_TELEGRAM,
        TWO_SIDE
    }
}
