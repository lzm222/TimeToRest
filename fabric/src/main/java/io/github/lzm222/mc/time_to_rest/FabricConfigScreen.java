package io.github.lzm222.mc.time_to_rest;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** 复用 NeoForge 配置界面已有的语言键，构建 Cloth Config 配置屏。 */
public final class FabricConfigScreen {
    private FabricConfigScreen() {
    }

    public static Screen create(Screen parent) {
        FabricConfig config = FabricConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable(TTLanguageKeys.Config.TITLE))
                .setSavingRunnable(FabricConfig::saveAndApply);

        ConfigEntryBuilder entries = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Component.empty());

        category.addEntry(entries.startBooleanToggle(
                        Component.translatable(TTLanguageKeys.Config.ENABLED), config.enabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable(TTLanguageKeys.Config.ENABLED_TIP))
                .setSaveConsumer(value -> config.enabled = value)
                .build());

        category.addEntry(entries.startIntSlider(
                        Component.translatable(TTLanguageKeys.Config.REMINDER_PERIOD),
                        config.reminderPeriod,
                        FabricConfig.MIN_PERIOD_SECONDS,
                        FabricConfig.MAX_PERIOD_SECONDS)
                .setDefaultValue(FabricConfig.DEFAULT_PERIOD_SECONDS)
                .setTextGetter(seconds -> Component.literal(seconds + " s"))
                .setTooltip(Component.translatable(TTLanguageKeys.Config.REMINDER_PERIOD_TIP))
                .setSaveConsumer(value -> config.reminderPeriod = value)
                .build());

        return builder.build();
    }
}
