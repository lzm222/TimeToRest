package io.github.lzm222.mc.time_to_rest;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = TimeToRest.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = TimeToRest.MODID, value = Dist.CLIENT)
public class TimeToRestClient {
    private static long playerJoinGameTick = -1;
    private static int remindedCount = 1;
    public static final Logger LOGGER = TimeToRest.LOGGER;

    public TimeToRestClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    @SubscribeEvent
    static void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.debug("PlayerLoggedInEvent fired");
        playerJoinGameTick = event.getEntity().level().getGameTime();
        LOGGER.info("JoinTick has set: {}", playerJoinGameTick);
    }

    @SubscribeEvent
    static void onPlayerLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        playerJoinGameTick = -1;
        remindedCount = 1;
    }

    @SubscribeEvent
    static void onPlayerTick(PlayerTickEvent.Post event) {
        if (playerJoinGameTick != -1) {
            long now = event.getEntity().level().getGameTime();
            long flownTick = now - playerJoinGameTick;
            long setTimeSec = 10; // TODO 添加到Config
            long setTimeTick = setTimeSec * 20;
            if (flownTick >= setTimeTick*remindedCount) {
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
                LOGGER.debug("remindedCount: {}; flownTick: {}", remindedCount, flownTick);
                remindedCount++;
            }
        }
    }
}
