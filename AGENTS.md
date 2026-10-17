# AGENTS.md

Time To Rest：Minecraft 1.21.1 纯客户端模组（提醒玩家休息），Gradle 多模块，同时支持 NeoForge 与 Fabric。

## 模块与代码共享

- `common/`：共享客户端逻辑（`TimeToRestCommon` 计时/提醒、`TTLanguageKeys` 语言键）。**只能用原版 Minecraft API / Mojang 映射**，禁止 import NeoForge 或 Fabric API —— 这份源码会被两个加载器分别编译。
- `neoforge/`：NeoForge 入口 `TimeToRestNeoforge`（`@EventBusSubscriber`）+ `Config`（`ModConfigSpec`，CLIENT 配置，读取后写入 common 的静态状态）。
- `fabric/`：Fabric 入口 `TimeToRestFabric`；尚无配置实现，提醒周期在代码中硬编码为 1 分钟。
- `data/`：仅用于 datagen（语言文件）。`data/src/generated/resources/` 是**提交进仓库的生成结果**，不是构建产物，会被其他模块的 resources 直接引用。
- 关键非标准点：`neoforge`、`fabric`、`data` 都通过 `tasks.named('compileJava') { source project(":common").file('src/main') }` 直接编译 common 源码。改 `common/` 会同时影响所有 jar；公共逻辑不要在各 loader 模块重复实现。

## 语言 / datagen

- 新增文本：在 `TTLanguageKeys` 加 key → 在 `TTLanguageProvider.EN_US` 和 `ZH_CN` 同时补翻译 → 运行 `.\gradlew.bat :data:runData` 重新生成 `data/src/generated/resources/assets/time_to_rest/lang/*.json`。不要手改生成 JSON，下次 datagen 会覆盖。
- 配置项 tooltip key 由 `TTLanguageKeys.Config` 的 `genTip` 规则生成（`<key>.tooltip` 后缀）。

## 命令（PowerShell / Windows）

- 构建（CI 同款）：`.\gradlew.bat build`（CI 中为 `./gradlew build`）。没有测试源码，也没有 lint / formatter / typecheck 配置，`build` 就是唯一的自动验证步骤。
- 单模块：`.\gradlew.bat :fabric:build`、`.\gradlew.bat :common:build`。
- 运行客户端：`.\gradlew.bat :neoforge:runClient`、`.\gradlew.bat :fabric:runClient`。
- 重新生成数据：`.\gradlew.bat :data:runData`。
- 运行目录：`neoforge/run/`、`fabric/runs/client/`、`data/run/`（均在 .gitignore 中，含日志和配置）。
- 需要 JDK 21（Gradle toolchain）。wrapper 为 Gradle 9.2.1、走阿里云镜像；ModDevGradle / Loom 首次运行会下载 Minecraft 资源，耗时较长。配置缓存已被有意禁用（见 `gradle.properties`），不要为此改动。

## 约定与坑

- Git 提交信息从下一次提交起使用英文（en-US）。
- 代码注释目前以中文为主，保持现有风格。
- `README.md` 只提 NeoForge，已过时；版本、依赖、mod id 等以 `gradle.properties` 和构建脚本为准。
- `fabric.mod.json` 与 `META-INF/neoforge.mods.toml` 是模板：`${mod_version}` 等占位符在构建时替换，改版本请改 `gradle.properties`。
- NeoForge 配置项使用 `worldRestart()`：游戏内改配置后需重进世界才生效，测试时别误以为未生效。
- 计时基于客户端 `level.getGameTime()`；进入世界后的首个有效 tick 由 `flag`（`setFlag(true)`）触发捕获，改计时逻辑时需保留这个初始化顺序。
