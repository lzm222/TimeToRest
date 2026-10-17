package io.github.lzm222.mc.time_to_rest;

import com.google.gson.annotations.SerializedName;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

/**
 * Fabric 端配置，字段与 NeoForge 的 Config 保持一致。
 * 由 Cloth Config 的 AutoConfig 负责读写 config/time_to_rest.json。
 */
@Config(name = TimeToRestCommon.MODID)
public class FabricConfig implements ConfigData {
    public static final int MIN_PERIOD_SECONDS = 10;
    public static final int DEFAULT_PERIOD_SECONDS = 30 * 60;
    public static final int MAX_PERIOD_SECONDS = 24 * 60 * 60;

    public boolean enabled = true;

    @SerializedName("reminder_period")
    public int reminderPeriod = DEFAULT_PERIOD_SECONDS;

    private static ConfigHolder<FabricConfig> holder;

    public static void init() {
        holder = AutoConfig.register(FabricConfig.class, GsonConfigSerializer::new);
        clamp(holder.getConfig());
        apply(holder.getConfig());
    }

    public static FabricConfig get() {
        return holder.getConfig();
    }

    /** 配置界面保存时调用：先落盘再应用，保存后立即生效。 */
    public static void saveAndApply() {
        FabricConfig config = get();
        clamp(config);
        holder.save();
        apply(config);
    }

    private static void apply(FabricConfig config) {
        TimeToRestCommon.applyConfig(config.enabled, config.reminderPeriod);
    }

    /** 防止手改 JSON 写入低于下限的值（Cloth 的滑块范围只约束 UI）。 */
    private static void clamp(FabricConfig config) {
        config.reminderPeriod = Math.max(MIN_PERIOD_SECONDS, config.reminderPeriod);
    }
}
