package io.github.lzm222.mc.time_to_rest;

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

import java.time.Instant;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = TimeToRest.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = TimeToRest.MODID, value = Dist.CLIENT)
public class TimeToRestClient {
    private static long playerJoinTimeStamp = -1;
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
        playerJoinTimeStamp = Instant.now().getEpochSecond();
        LOGGER.info("JoinTimeStamp has set: {}", playerJoinTimeStamp);
    }

    @SubscribeEvent
    static void onPlayerLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        playerJoinTimeStamp = -1;
        remindedCount = 1;
    }

    @SubscribeEvent
    static void onPlayerTick(PlayerTickEvent.Post event) {
        if (playerJoinTimeStamp != -1) {
            long now = Instant.now().getEpochSecond();
            long flownTime = now - playerJoinTimeStamp;
            long setTimeSec = 60;
            if (Math.abs(flownTime-setTimeSec*remindedCount) < 10) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("Hey! Take a rest, please!"));
                remindedCount++;
                LOGGER.debug("remindedCount: {}; fliedTime: {}", remindedCount, flownTime);
            }
        }
    }
}
