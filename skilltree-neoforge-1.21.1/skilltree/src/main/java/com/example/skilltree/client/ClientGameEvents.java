package com.example.skilltree.client;

import com.example.skilltree.SkillTreeMod;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = SkillTreeMod.MODID, value = Dist.CLIENT)
public class ClientGameEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        while (ClientModEvents.OPEN_KEY.consumeClick()) {
            if (mc.player != null && mc.screen == null) {
                mc.setScreen(new SkillTreeScreen());
            }
        }
    }
}
