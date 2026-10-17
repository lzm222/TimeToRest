package io.github.lzm222.mc.time_to_rest;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class TimeToRestCommon {
    public static final String MODID = "time_to_rest";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * 下一次提醒的绝对游戏时间 tick；-1 表示当前没有生效的计时（未进入世界或已重置）。
     * 使用绝对 tick 而不是 "首次 tick + 周期 * 次数"，这样调小周期时不会一次性补发多个已经越过的提醒。
     */
    private static long nextReminderTick = -1;
    static long period = 20L * 60 * 30; // 单位: tick 此处默认为30min
    static boolean flag = false; // 用于判断是否为首个有效tick 具体见#loadTimer
    static boolean enabled = true;

    public static void tickAction() {
        if (flag) loadTimer();
        updateTimer();
    }

    public static void loadTimer() {
        var level = Minecraft.getInstance().level;
        if (level != null && level.getGameTime() > 0) { // 获取第一个有效游戏内tick
            nextReminderTick = nextTickFrom(level.getGameTime());
            flag = false;
            LOGGER.info("nextReminderTick has set: {}", nextReminderTick);
        }
    }

    public static void updateTimer() {
        var level = Minecraft.getInstance().level;
        if (!enabled || nextReminderTick == -1 || level == null) {
            return;
        }

        long now = level.getGameTime();
        if (now >= nextReminderTick) {
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
            LOGGER.info("period: {}; nextReminderTick: {}; now: {}", period, nextReminderTick, now);
            // 从当前时刻重新排期，不补发因卡顿或配置变更而错过的周期
            nextReminderTick = nextTickFrom(now);
        }
    }

    /**
     * 应用配置。两端（NeoForge / Fabric）都通过该方法把配置写入公共状态。
     *
     * @param newEnabled 是否启用提醒
     * @param seconds    提醒周期（秒）
     */
    public static void applyConfig(boolean newEnabled, long seconds) {
        long newPeriod = seconds * 20L;
        boolean periodChanged = newPeriod != period;
        boolean enabling = newEnabled && !enabled;

        enabled = newEnabled;
        period = newPeriod;

        // 周期变化或重新启用时重排计时，避免调小周期后连续触发历史阈值
        if (newEnabled && (periodChanged || enabling)) {
            rebaseTimer();
        }
    }

    private static void rebaseTimer() {
        var level = Minecraft.getInstance().level;
        if (level != null && level.getGameTime() > 0) {
            nextReminderTick = nextTickFrom(level.getGameTime());
            LOGGER.debug("Timer rebased, nextReminderTick: {}", nextReminderTick);
        }
    }

    /** 计算下一次提醒 tick，并在极端大周期下饱和到 Long.MAX_VALUE，避免溢出后立即重复提醒。 */
    private static long nextTickFrom(long now) {
        long next = now + period;
        return next < now ? Long.MAX_VALUE : next;
    }

    public static void setFlag(boolean flag) {
        TimeToRestCommon.flag = flag;
    }

    public static void resetTimer() {
        LOGGER.debug("ResetTimer");
        nextReminderTick = -1;
    }
}
