package com.example.skilltree;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = SkillTreeMod.MODID)
public class SkillEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) refresh(sp);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) refresh(sp);
    }

    @SubscribeEvent
    public static void onDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) refresh(sp);
    }

    private static void refresh(ServerPlayer sp) {
        SkillManager.applyModifiers(sp);
        SkillManager.sync(sp);
    }

    /**
     * 1 skill point per XP level gained. We track the highest level ever rewarded, so spending
     * levels on enchanting can't be farmed for extra points.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post e) {
        if (!(e.getEntity() instanceof ServerPlayer sp) || sp.tickCount % 10 != 0) return;
        PlayerSkills data = SkillManager.get(sp);
        int level = sp.experienceLevel;
        if (level > data.maxLevel()) {
            int gained = level - data.maxLevel();
            SkillManager.set(sp, data.withPoints(data.points() + gained).withMaxLevel(level));
            SkillManager.sync(sp);
            sp.displayClientMessage(Component.literal("+" + gained + " skill point(s)! Press K to spend."), true);
        }
    }

    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("skilltree")
                .then(Commands.literal("points")
                        .requires(s -> s.hasPermission(2))
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 1000))
                                .executes(ctx -> {
                                    ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                    int n = IntegerArgumentType.getInteger(ctx, "amount");
                                    SkillManager.set(sp, SkillManager.get(sp).withPoints(SkillManager.get(sp).points() + n));
                                    SkillManager.sync(sp);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Added " + n + " skill points."), false);
                                    return 1;
                                })))
                .then(Commands.literal("reset")
                        .executes(ctx -> {
                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                            SkillManager.reset(sp);
                            ctx.getSource().sendSuccess(() -> Component.literal("Skills reset; points refunded."), false);
                            return 1;
                        })));
    }
}
