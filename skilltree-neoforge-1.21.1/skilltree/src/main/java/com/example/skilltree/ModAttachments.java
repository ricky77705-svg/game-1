package com.example.skilltree;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SkillTreeMod.MODID);

    public static final Supplier<AttachmentType<PlayerSkills>> SKILLS = ATTACHMENTS.register("skills",
            () -> AttachmentType.builder(() -> PlayerSkills.EMPTY)
                    .serialize(PlayerSkills.CODEC)
                    .copyOnDeath()
                    .build());
}
