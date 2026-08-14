package io.github.lzm222.mc.time_to_rest.datagen;

import io.github.lzm222.mc.time_to_rest.TTLanguageKeys;
import io.github.lzm222.mc.time_to_rest.TimeToRest;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;


public class TTLanguageProvider extends LanguageProvider {
    TTLanguageProvider(PackOutput output) { super(output, TimeToRest.MODID, "en_us"); }

    @Override
    protected void addTranslations() { // TODO DataGen并测试翻译
        add(TTLanguageKeys.Config.ENABLED, "Enabled");
        add(TTLanguageKeys.Config.REMINDER_PERIOD, "Reminder Period");
        add(TTLanguageKeys.Config.TITLE, "Time To Rest Config");
        add(TTLanguageKeys.Config.ENABLED_TIP, "Whether to enable the mod.");
        add(TTLanguageKeys.Config.REMINDER_PERIOD_TIP, "The reminder period. \nUnit: seconds");
        add(TTLanguageKeys.REMINDER_TEXT, "⏰ Hey! Take a rest, please!");
    }
}
