package com.example.skilltree;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SkillTreeMod.MODID)
public class SkillTreeMod {
    public static final String MODID = "skilltree";

    public SkillTreeMod(IEventBus modBus) {
        ModAttachments.ATTACHMENTS.register(modBus);
    }
}
