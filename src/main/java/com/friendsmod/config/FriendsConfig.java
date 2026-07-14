package com.friendsmod.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

/**
 * Загальний (COMMON) конфіг мода. Файл friendsmod-common.toml з'явиться
 * в config/ після першого запуску і його можна редагувати.
 */
public final class FriendsConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.IntValue MAX_BOTS_PER_WORLD;
    public static final ModConfigSpec.IntValue SCARE_COOLDOWN_TICKS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ENABLED_BOTS;
    public static final ModConfigSpec.BooleanValue TAB_LIST_ENABLED;
    public static final ModConfigSpec.DoubleValue TAB_LIST_RANGE;
    public static final ModConfigSpec.BooleanValue JUMPSCARE_ENABLED;
    public static final ModConfigSpec.BooleanValue WHISPER_ENABLED;
    public static final ModConfigSpec.BooleanValue FAKE_SOUNDS_ENABLED;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("general");
        ENABLED = builder
                .comment("Master switch for the whole mod.")
                .define("enabled", true);
        MAX_BOTS_PER_WORLD = builder
                .comment("Max amount of anomaly bots allowed to exist at the same time per world.")
                .defineInRange("maxBotsPerWorld", 6, 0, 64);
        SCARE_COOLDOWN_TICKS = builder
                .comment("Minimum ticks between two scares aimed at the same player (20 ticks = 1s).")
                .defineInRange("scareCooldownTicks", 20 * 30, 20, 20 * 600);
        ENABLED_BOTS = builder
                .comment("Which bot names are allowed to spawn / be summoned.")
                .defineList("enabledBots",
                        List.of("anomaly006", "anomaly008", "anomaly009", "anomaly010", "anomaly011"),
                        o -> o instanceof String);
        builder.pop();

        builder.push("tabList");
        TAB_LIST_ENABLED = builder
                .comment("If true, active bot nicknames appear in the player list (Tab key).")
                .define("enabled", true);
        TAB_LIST_RANGE = builder
                .comment("Range (blocks) within which a player sees a bot's name in the tab list.")
                .defineInRange("range", 64.0, 8.0, 256.0);
        builder.pop();

        builder.push("scares");
        JUMPSCARE_ENABLED = builder.define("jumpscareEnabled", true);
        WHISPER_ENABLED = builder.define("whisperEnabled", true);
        FAKE_SOUNDS_ENABLED = builder.define("fakeSoundsEnabled", true);
        builder.pop();

        SPEC = builder.build();
    }

    private FriendsConfig() {
    }
}
