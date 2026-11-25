package com.example.skilltree;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

/** One node in the tree. col/row are grid positions used by the screen. */
public record Skill(String id, String name, String description, String parent, int cost,
                    int col, int row, Item icon,
                    Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation) {

    public ResourceLocation modifierId() {
        return ResourceLocation.fromNamespaceAndPath(SkillTreeMod.MODID, "skill_" + id);
    }
}
