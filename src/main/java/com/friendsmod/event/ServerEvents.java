package com.friendsmod.event;

import com.friendsmod.registry.ModEntities;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

/**
 * Реєструє команду для спавну бота біля гравця:
 *   /friendsmod summon anomaly011
 * Це зручніше й безпечніше за випадковий природний спавн у світі.
 */
public final class ServerEvents {

    private static final SuggestionProvider<net.minecraft.commands.CommandSourceStack> BOT_NAMES =
            (ctx, builder) -> SharedSuggestionProvider.suggest(ModEntities.ALL.values(), builder);

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("friendsmod")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("summon")
                        .then(Commands.argument("variant", StringArgumentType.word())
                                .suggests(BOT_NAMES)
                                .executes(ServerEvents::summon))));
    }

    private static int summon(com.mojang.brigadier.context.CommandContext<net.minecraft.commands.CommandSourceStack> ctx) {
        String variant = StringArgumentType.getString(ctx, "variant");
        ServerPlayer player;
        try {
            player = ctx.getSource().getPlayerOrException();
        } catch (Exception e) {
            ctx.getSource().sendFailure(Component.literal("Тільки гравець може виконати цю команду."));
            return 0;
        }

        EntityType<?> type = ModEntities.ALL.entrySet().stream()
                .filter(e -> e.getValue().equals(variant))
                .map(e -> e.getKey().get())
                .findFirst().orElse(null);

        if (type == null) {
            ctx.getSource().sendFailure(Component.literal("Невідомий бот: " + variant
                    + ". Доступні: " + String.join(", ", ModEntities.ALL.values())));
            return 0;
        }

        ServerLevel level = player.serverLevel();
        Entity entity = type.create(level);
        if (entity != null) {
            entity.moveTo(player.getX() + 3, player.getY(), player.getZ() + 3, player.getYRot(), 0);
            level.addFreshEntity(entity);
            ctx.getSource().sendSuccess(() -> Component.literal(variant + " заспавнено поруч."), false);
        }
        return 1;
    }

    private ServerEvents() {
    }
}
