package com.example.skilltree;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

/** Server-side skill logic. */
public final class SkillManager {

    public static PlayerSkills get(ServerPlayer player) {
        return player.getData(ModAttachments.SKILLS);
    }

    public static void set(ServerPlayer player, PlayerSkills data) {
        player.setData(ModAttachments.SKILLS, data);
    }

    /** Re-applies every unlocked skill's attribute modifier (modifiers are transient). */
    public static void applyModifiers(ServerPlayer player) {
        PlayerSkills data = get(player);
        for (Skill skill : Skills.ALL) {
            AttributeInstance inst = player.getAttribute(skill.attribute());
            if (inst == null) continue;
            inst.removeModifier(skill.modifierId());
            if (data.has(skill.id())) {
                inst.addTransientModifier(new AttributeModifier(skill.modifierId(), skill.amount(), skill.operation()));
            }
        }
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static void sync(ServerPlayer player) {
        PlayerSkills data = get(player);
        PacketDistributor.sendToPlayer(player, new SyncSkillsPayload(data.points(), data.unlocked()));
    }

    public static void tryUnlock(ServerPlayer player, String id) {
        Skill skill = Skills.BY_ID.get(id);
        if (skill == null) return;
        PlayerSkills data = get(player);

        boolean parentOk = skill.parent() == null || data.has(skill.parent());
        if (data.has(id) || !parentOk || data.points() < skill.cost()) {
            sync(player); // client was out of date
            return;
        }

        set(player, data.withPoints(data.points() - skill.cost()).withUnlocked(id));
        applyModifiers(player);
        if (skill.attribute() == Attributes.MAX_HEALTH) {
            player.heal((float) skill.amount());
        }
        sync(player);
    }

    /** Refunds all spent points and clears unlocked skills. */
    public static void reset(ServerPlayer player) {
        PlayerSkills data = get(player);
        int refund = data.unlocked().stream()
                .map(Skills.BY_ID::get)
                .filter(s -> s != null)
                .mapToInt(Skill::cost)
                .sum();
        set(player, new PlayerSkills(data.points() + refund, data.maxLevel(), List.of()));
        applyModifiers(player);
        sync(player);
    }

    private SkillManager() {}
}
