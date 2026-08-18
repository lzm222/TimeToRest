package io.github.lzm222.mc.time_to_rest.datagen;

import io.github.lzm222.mc.time_to_rest.TTLanguageKeys;
import io.github.lzm222.mc.time_to_rest.TimeToRestCommon;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;


public abstract class TTLanguageProvider {
    public static class EN_US extends LanguageProvider {
        EN_US(PackOutput output) { super(output, TimeToRestCommon.MODID, "en_us"); }

        @Override
        protected void addTranslations() {
            add(TTLanguageKeys.Config.ENABLED, "Enabled");
            add(TTLanguageKeys.Config.REMINDER_PERIOD, "Reminder Period");
            add(TTLanguageKeys.Config.TITLE, "Time To Rest Config");
            add(TTLanguageKeys.Config.ENABLED_TIP, "Whether to enable the mod.");
            add(TTLanguageKeys.Config.REMINDER_PERIOD_TIP, "The reminder period. \nUnit: seconds");
            add(TTLanguageKeys.REMINDER_TEXT, "⏰ Hey! Take a rest, please!");
        }
    }
    public static class ZH_CN extends LanguageProvider {
        ZH_CN(PackOutput output) { super(output, TimeToRestCommon.MODID, "zh_cn"); }

        @Override
        protected void addTranslations() {
            add(TTLanguageKeys.Config.ENABLED, "启用");
            add(TTLanguageKeys.Config.REMINDER_PERIOD, "提醒周期");
            add(TTLanguageKeys.Config.TITLE, "Time To Rest 模组配置");
            add(TTLanguageKeys.Config.ENABLED_TIP, "是否启用模组");
            add(TTLanguageKeys.Config.REMINDER_PERIOD_TIP, "提醒周期 \n单位: 秒");
            add(TTLanguageKeys.REMINDER_TEXT, "⏰ Hey bro! 你该休息了!");
        }
    }
}
