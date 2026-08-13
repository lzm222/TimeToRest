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
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

@Mod(value = TimeToRest.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TimeToRest.MODID, value = Dist.CLIENT)
public class TimeToRest {
    public static final String MODID = "time_to_rest";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static long playerJoinGameTick = -1;
    private static int remindedCount = 1;
    private static long period = 20 * 60 * 30; // 单位: tick 此处默认为30min

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
    static void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.debug("PlayerLoggedInEvent fired");
        playerJoinGameTick = event.getEntity().level().getGameTime(); // FIXME 多人游戏可能导致计数异常
        LOGGER.info("JoinTick has set: {}", playerJoinGameTick);
    }

    @SubscribeEvent
    static void onPlayerLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        playerJoinGameTick = -1;
        remindedCount = 1;
    }

    @SubscribeEvent
    static void onPlayerTick(PlayerTickEvent.Post event) {
        if (Config.ENABLED.getAsBoolean() && playerJoinGameTick != -1) {
            long now = event.getEntity().level().getGameTime();
            long flownTick = now - playerJoinGameTick;
            if (flownTick >= period * remindedCount) {
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.literal("⏰ Hey! Take a rest, please!") // TODO i18n
                                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                        false
                );
                Minecraft.getInstance().gui.getChat().addMessage(
                        Component.literal("[Time To Rest] ")
                                .withStyle(ChatFormatting.AQUA)
                                .append(
                                        Component.literal("⏰ Hey! Take a rest, please!")
                                                .withStyle(ChatFormatting.WHITE)
                                )
                );
                // TODO 添加提示音
                LOGGER.debug("remindedCount: {}; flownTick: {}", remindedCount, flownTick);
                remindedCount++;
            }
        }
    }
}
