package ru.glitchless.telegrambridge;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.LanguageFile;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.handlers.ToMinecraftResender;
import ru.glitchless.telegrambridge.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.telegramapi.TelegramLoop;

import java.util.Properties;

@Mod(modid = TelegramBridgeMod.MODID, name = TelegramBridgeMod.NAME, version = TelegramBridgeMod.VERSION)
public class TelegramBridgeMod {
    public static final String MODID = "telegrambridge";
    public static final String NAME = "Telegram Bridge";
    public static final String VERSION = "1.0";

    private static Logger logger;
    private static Properties langFile;
    private static TelegramContext context;
    private static TelegramLoop telegramLoop;

    public static Properties getLangFile() {
        return langFile;
    }

    public static TelegramContext getContext() {
        return context;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        langFile = LanguageFile.getLanguageFile(event);
        context = new TelegramContext(logger);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide() != Side.SERVER
                && TelegramBridgeConfig.server_only) {
            return;
        }
        telegramLoop = new TelegramLoop(context);
        telegramLoop.start();

        context.addListener(new ToMinecraftResender());
    }
}
