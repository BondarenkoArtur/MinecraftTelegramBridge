package ru.glitchless.telegrambridge.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Date;

import javax.annotation.Nonnull;

import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.config.TelegramConfigHelper;
import ru.glitchless.telegrambridge.utils.TextUtils;

@Mod.EventBusSubscriber
public class ToTelegramEvent {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP)) {
            return;
        }
        final EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
        final ITextComponent textComponent = player.getCombatTracker().getDeathMessage();
        final String deathmessage = TextUtils.boldInText(textComponent.getUnformattedText(), player.getGameProfile().getName());
        final String message = TelegramBridgeConfig.text.death_message.replace("${deathmessage}", deathmessage);
        if (event.getSource().getSourceOfDamage() instanceof EntityPlayer
                && TelegramBridgeConfig.relay_level.user_kill_by_user) {
            broadcastToChats(message);
            return;
        }

        if (TelegramBridgeConfig.relay_level.user_kill_by_other) {
            broadcastToChats(message);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        final String message;
        final long currentTime = new Date().getTime();
        final long lastInteraction = Long.parseLong(TelegramBridgeConfig.last_interaction_time);
        if (TelegramBridgeConfig.relay_level.user_joined_after_long_time && PlayerList.getPlayerList().size() == 1 &&
            lastInteraction <= currentTime - TelegramBridgeConfig.long_time_minutes_amount * 60 * 1000) {
            message = TelegramBridgeConfig.text.player_join_after_long_time
                .replace("${nickname}", event.player.getDisplayNameString());
        } else {
            if (!TelegramBridgeConfig.relay_level.user_join) {
                return;
            }
            message = TelegramBridgeConfig.text.player_join
                .replace("${nickname}", event.player.getDisplayNameString());
        }
        broadcastToChats(message);
        TelegramConfigHelper.setLastInteractionTime(currentTime);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_leave) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_leave
                .replace("${nickname}", event.player.getDisplayNameString());
        broadcastToChats(message);
        TelegramConfigHelper.setLastInteractionTime(new Date().getTime());
    }

    public static void broadcastToChats(@Nonnull String message) {
        for (String id : TelegramBridgeConfig.chat_ids) {
            TelegramBridgeMod.getContext().sendMessage(id, message);
        }
    }
}
