package io.github.lzm222.mc.time_to_rest;

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

@Mod(value = TimeToRestCommon.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TimeToRestCommon.MODID, value = Dist.CLIENT)
public class TimeToRestNeoforge {
    public TimeToRestNeoforge(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static void loadConfig() {
        var period = Config.REMINDER_PERIOD.getAsLong() * 20; // 转换为tick
        var enabled = Config.ENABLED.getAsBoolean();
        TimeToRestCommon.setPeriod(period);
        TimeToRestCommon.setEnabled(enabled);
    }

    @SubscribeEvent
    static void onConfigLoading(ModConfigEvent.Loading event) {
        loadConfig();
    }

    @SubscribeEvent
    static void onConfigChanging(ModConfigEvent.Reloading event) {
        loadConfig();
    }

    @SubscribeEvent
    static void onPlayerJoinWorld(ClientPlayerNetworkEvent.LoggingIn event) {
        TimeToRestCommon.LOGGER.debug("PlayerLoggingIn");
        TimeToRestCommon.setFlag(true);
    }

    @SubscribeEvent
    static void onPlayerLeaveWorld(ClientPlayerNetworkEvent.LoggingOut event) {
        TimeToRestCommon.resetTimer();
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        TimeToRestCommon.tickAction();
    }
}
