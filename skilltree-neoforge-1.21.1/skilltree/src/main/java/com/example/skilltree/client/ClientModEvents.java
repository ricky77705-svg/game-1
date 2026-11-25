package com.example.skilltree.client;

import com.example.skilltree.SkillTreeMod;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = SkillTreeMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    public static final KeyMapping OPEN_KEY =
            new KeyMapping("key.skilltree.open", GLFW.GLFW_KEY_K, "key.categories.skilltree");

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_KEY);
    }
}
