package com.example.skilltree;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = SkillTreeMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(UnlockSkillPayload.TYPE, UnlockSkillPayload.CODEC, (payload, ctx) -> {
            if (ctx.player() instanceof ServerPlayer sp) {
                SkillManager.tryUnlock(sp, payload.skillId());
            }
        });

        registrar.playToClient(SyncSkillsPayload.TYPE, SyncSkillsPayload.CODEC,
                (payload, ctx) -> ClientSkillData.set(payload.points(), payload.unlocked()));
    }
}
