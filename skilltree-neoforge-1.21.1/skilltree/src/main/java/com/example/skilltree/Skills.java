package com.example.skilltree;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** The skill tree definition. Edit this file to add or change skills. */
public final class Skills {
    public static final int COLUMNS = 4;
    public static final int ROWS = 3;

    public static final List<Skill> ALL;
    public static final Map<String, Skill> BY_ID;

    static {
        List<Skill> list = new ArrayList<>();
        list.addAll(branch("vitality", "Vitality", 0, Items.GOLDEN_APPLE, Attributes.MAX_HEALTH,
                Operation.ADD_VALUE, "max health", 2, 2, 4));
        list.addAll(branch("might", "Might", 1, Items.IRON_SWORD, Attributes.ATTACK_DAMAGE,
                Operation.ADD_VALUE, "attack damage", 1, 1, 2));
        list.addAll(branch("swiftness", "Swiftness", 2, Items.FEATHER, Attributes.MOVEMENT_SPEED,
                Operation.ADD_MULTIPLIED_BASE, "movement speed", 0.1, 0.1, 0.1));
        list.addAll(branch("prospector", "Prospector", 3, Items.IRON_PICKAXE, Attributes.BLOCK_BREAK_SPEED,
                Operation.ADD_MULTIPLIED_BASE, "mining speed", 0.2, 0.2, 0.3));
        ALL = List.copyOf(list);
        BY_ID = ALL.stream().collect(Collectors.toUnmodifiableMap(Skill::id, Function.identity()));
    }

    private static final String[] ROMAN = {"I", "II", "III"};

    private static List<Skill> branch(String key, String name, int col, Item icon, Holder<Attribute> attr,
                                      Operation op, String unit, double... amounts) {
        List<Skill> out = new ArrayList<>();
        for (int tier = 0; tier < amounts.length; tier++) {
            double a = amounts[tier];
            String value = op == Operation.ADD_VALUE
                    ? "+" + (a == Math.floor(a) ? String.valueOf((int) a) : String.valueOf(a))
                    : "+" + Math.round(a * 100) + "%";
            out.add(new Skill(
                    key + "_" + (tier + 1),
                    name + " " + ROMAN[tier],
                    value + " " + unit,
                    tier > 0 ? key + "_" + tier : null,
                    tier + 1,
                    col, tier, icon, attr, a, op));
        }
        return out;
    }

    private Skills() {}
}
