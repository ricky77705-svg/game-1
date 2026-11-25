package com.example.skilltree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Client-side cache of the local player's skills (plain data, safe to load on any side). */
public final class ClientSkillData {
    private static int points = 0;
    private static Set<String> unlocked = new HashSet<>();

    public static void set(int p, List<String> u) {
        points = p;
        unlocked = new HashSet<>(u);
    }

    public static int points() { return points; }

    public static boolean has(String id) { return unlocked.contains(id); }

    private ClientSkillData() {}
}
