package io.github.lzm222.mc.time_to_rest;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Whether to enable the mod")
            .worldRestart()
            .define("enabled", true);

    public static final ModConfigSpec.LongValue REMINDER_PERIOD = BUILDER // 单位: 秒 TODO 增加时分秒多种单位
            .comment("The seconds between two reminders")
            .worldRestart()
            .defineInRange("reminderPeriod", 30 * 60, 10, Long.MAX_VALUE / 20);

    static final ModConfigSpec SPEC = BUILDER.build();
}
