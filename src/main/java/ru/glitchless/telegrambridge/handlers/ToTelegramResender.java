package ru.glitchless.telegrambridge.handlers;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;

import static ru.glitchless.telegrambridge.handlers.ToTelegramEvent.broadcastToChats;

@Mod.EventBusSubscriber
public class ToTelegramResender {
    @SubscribeEvent
    public static void onChatMessage(ServerChatEvent event) {
        if (TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TWO_SIDE
                && TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TO_TELEGRAM) {
            return; // ignore
        }

        String message = event.getMessage();

        if (message == null || message.isEmpty()) {
            return;
        }

        if (TelegramBridgeConfig.relay_prefix_enabled == TelegramBridgeConfig.RelayPrefixEnabled.TWO_SIDE
            || TelegramBridgeConfig.relay_prefix_enabled == TelegramBridgeConfig.RelayPrefixEnabled.TO_TELEGRAM) {
            if (!message.startsWith(TelegramBridgeConfig.relay_prefix)) {
                return; // ignore
            } else {
                message = message.substring(TelegramBridgeConfig.relay_prefix.length());
            }
        }

        final String textMessage = TelegramBridgeConfig.text.chatmessage_to_telegram.replace("${nickname}", event.getUsername()).replace("${message}", message);
        broadcastToChats(textMessage);
    }

}
