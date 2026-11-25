package com.example.skilltree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

/** Per-player skill data: unspent points, highest level already rewarded, unlocked skill ids. */
public record PlayerSkills(int points, int maxLevel, List<String> unlocked) {
    public static final PlayerSkills EMPTY = new PlayerSkills(0, 0, List.of());

    public static final Codec<PlayerSkills> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("points").forGetter(PlayerSkills::points),
            Codec.INT.fieldOf("max_level").forGetter(PlayerSkills::maxLevel),
            Codec.STRING.listOf().fieldOf("unlocked").forGetter(PlayerSkills::unlocked)
    ).apply(i, PlayerSkills::new));

    public boolean has(String id) {
        return unlocked.contains(id);
    }

    public PlayerSkills withPoints(int p) {
        return new PlayerSkills(p, maxLevel, unlocked);
    }

    public PlayerSkills withMaxLevel(int l) {
        return new PlayerSkills(points, l, unlocked);
    }

    public PlayerSkills withUnlocked(String id) {
        List<String> list = new ArrayList<>(unlocked);
        list.add(id);
        return new PlayerSkills(points, maxLevel, List.copyOf(list));
    }
}
