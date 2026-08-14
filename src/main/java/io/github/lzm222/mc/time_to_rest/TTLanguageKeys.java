package io.github.lzm222.mc.time_to_rest;

public final class TTLanguageKeys {
    private TTLanguageKeys() {}
    public static final String REMINDER_TEXT = genCommon("reminder_text");

    public static final class Config {
        private Config() {}
        public static final String NAME_SPACE = genCommon("configuration");
        public static final String TITLE = genOp("title");
        public static final String ENABLED = genOp("enabled");
        public static final String ENABLED_TIP = genTip("enabled");
        public static final String REMINDER_PERIOD = genOp("reminder_period");
        public static final String REMINDER_PERIOD_TIP = genTip("reminder_period");

        private static String genOp(String key) {
            return NAME_SPACE + "." + key;
        }

        private static String genTip(String key) {
            return genOp(key) + ".tooltip";
        }
    }

    private static String genCommon(String key) {
        return TimeToRest.MODID + "." + key;
    }
}
