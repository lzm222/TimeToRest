package io.github.lzm222.mc.time_to_rest;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * 由 Mod Menu 通过 fabric.mod.json 中的 "modmenu" entrypoint 加载；
 * 未安装 Mod Menu 时该类不会被加载，模组其余功能不受影响。
 */
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FabricConfigScreen::create;
    }
}
