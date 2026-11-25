package com.example.skilltree;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client -> server: please unlock this skill. */
public record UnlockSkillPayload(String skillId) implements CustomPacketPayload {
    public static final Type<UnlockSkillPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SkillTreeMod.MODID, "unlock_skill"));

    public static final StreamCodec<ByteBuf, UnlockSkillPayload> CODEC =
            ByteBufCodecs.STRING_UTF8.map(UnlockSkillPayload::new, UnlockSkillPayload::skillId);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
