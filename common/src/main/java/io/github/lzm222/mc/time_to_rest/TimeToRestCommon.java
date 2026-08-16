package io.github.lzm222.mc.time_to_rest;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class TimeToRestCommon {
    public static final String MODID = "time_to_rest";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static long playerJoinGameTick = -1;
    private static int remindedCount = 1;
    static long period = 20 * 60 * 30; // 单位: tick 此处默认为30min
    static boolean flag = false; // 用于判断是否为首个有效tick 具体见#loadTimer
    static boolean enabled = true;

    public static void tickAction() {
        if (flag) loadTimer();
        updateTimer();
    }

    public static void loadTimer() {
        var level = Minecraft.getInstance().level;
        if (level != null && level.getGameTime() > 0) { // 获取第一个有效游戏内tick
            playerJoinGameTick = level.getGameTime();
            flag = false;
            LOGGER.info("playerJoinTick has set: {}", playerJoinGameTick);
        }
    }

    public static void updateTimer() {
        if (enabled
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

    public static void setPeriod(long period) {
        TimeToRestCommon.period = period;
    }

    public static void setFlag(boolean flag) {
        TimeToRestCommon.flag = flag;
    }

    public static void resetTimer() {
        LOGGER.debug("ResetTimer");
        playerJoinGameTick = -1;
        remindedCount = 1;
    }

    public static void setEnabled(boolean enabled) {
        TimeToRestCommon.enabled = enabled;
    }
}
