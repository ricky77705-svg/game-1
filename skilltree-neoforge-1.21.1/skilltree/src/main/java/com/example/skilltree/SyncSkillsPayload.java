package com.example.skilltree;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/** Server -> client: current points and unlocked skills. */
public record SyncSkillsPayload(int points, List<String> unlocked) implements CustomPacketPayload {
    public static final Type<SyncSkillsPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SkillTreeMod.MODID, "sync_skills"));

    public static final StreamCodec<ByteBuf, SyncSkillsPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncSkillsPayload::points,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SyncSkillsPayload::unlocked,
            SyncSkillsPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
