package io.github.lzm222.mc.time_to_rest.datagen;

import io.github.lzm222.mc.time_to_rest.TimeToRestCommon;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber (modid = TimeToRestCommon.MODID)
public class DataGen {
    @SubscribeEvent
    static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(
                event.includeClient(),
                new TTLanguageProvider.EN_US(output)
        );
        generator.addProvider(
                event.includeClient(),
                new TTLanguageProvider.ZH_CN(output)
        );
    }
}
