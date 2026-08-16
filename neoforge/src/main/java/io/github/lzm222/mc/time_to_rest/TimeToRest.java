package io.github.lzm222.mc.time_to_rest;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(value = TimeToRest.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TimeToRest.MODID, value = Dist.CLIENT)
public class TimeToRest {
    public static final String MODID = "time_to_rest";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static long playerJoinGameTick = -1;
    private static int remindedCount = 1;
    private static long period = 20 * 60 * 30; // 单位: tick 此处默认为30min

    private static boolean flag = false; // 用于判断是否为首个有效tick 具体见#onClientTick

    public TimeToRest(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onConfigLoading(ModConfigEvent.Loading event) {
        period = Config.REMINDER_PERIOD.getAsLong() * 20; // 转换为tick
    }

    @SubscribeEvent
    static void onConfigChanging(ModConfigEvent.Reloading event) {
        period = Config.REMINDER_PERIOD.getAsLong() * 20; // 转换为tick
    }

    @SubscribeEvent
    static void onPlayerJoinWorld(ClientPlayerNetworkEvent.LoggingIn event) {
        LOGGER.debug("PlayerLoggingIn");
        flag = true;
    }

    @SubscribeEvent
    static void onPlayerLeaveWorld(ClientPlayerNetworkEvent.LoggingOut event) {
        playerJoinGameTick = -1;
        remindedCount = 1;
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        if (flag) { // 设置加入tick
            var level = Minecraft.getInstance().level;
            if (level != null && level.getGameTime() > 0) { // 获取第一个有效游戏内tick
                playerJoinGameTick = level.getGameTime();
                flag = false;
                LOGGER.info("playerJoinTick has set: {}", playerJoinGameTick);
            }
        }

        if (Config.ENABLED.getAsBoolean()
                && playerJoinGameTick != -1
                && Minecraft.getInstance().level != null
        ) {
            long now = Minecraft.getInstance().level.getGameTime();
            long flownTick = now - playerJoinGameTick;
            if (flownTick >= period * remindedCount) {
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable(TTLanguageKeys.REMINDER_TEXT)
                                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                        false
                );
                Minecraft.getInstance().gui.getChat().addMessage(
                        Component.literal("[Time To Rest] ")
                                .withStyle(ChatFormatting.AQUA)
                                .append(
                                        Component.translatable(TTLanguageKeys.REMINDER_TEXT)
                                                .withStyle(ChatFormatting.WHITE)
                                )
                );
                // TODO 添加提示音
                LOGGER.info("period: {}; remindedCount: {}; flownTick: {}", period, remindedCount, flownTick);
                remindedCount++;
            }
        }
    }
}
