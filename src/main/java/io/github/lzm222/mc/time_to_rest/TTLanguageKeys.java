package io.github.lzm222.mc.time_to_rest;

public final class TTLanguageKeys {
    private TTLanguageKeys() {}

    public static final class Config {
        private Config() {}
        public static final String TITLE = genOp("title");
        public static final String ENABLED = genOp("enabled");
        public static final String ENABLED_TIP = genTip("enabled");
        public static final String REMINDER_PERIOD = genOp("reminder_period");
        public static final String REMINDER_PERIOD_TIP = genTip("reminder_period");

        private static String genOp(String key) {
            return "%s.configuration.%s".formatted(TimeToRest.MODID, key);
        }

        private static String genTip(String key) {
            return genOp(key) + ".tooltip";
        }
    }
}
