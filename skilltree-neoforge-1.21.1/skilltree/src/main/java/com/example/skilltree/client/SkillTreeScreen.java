package com.example.skilltree.client;

import com.example.skilltree.ClientSkillData;
import com.example.skilltree.Skill;
import com.example.skilltree.Skills;
import com.example.skilltree.UnlockSkillPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeScreen extends Screen {
    private static final int NODE = 24;
    private static final int COL_W = 90;
    private static final int ROW_H = 64;
    private static final int TOP = 70;

    private static final int GREEN = 0xFF55FF55;
    private static final int GOLD = 0xFFFFD700;
    private static final int GRAY = 0xFF555555;

    public SkillTreeScreen() {
        super(Component.literal("Skill Tree"));
    }

    private int nodeX(Skill s) {
        int left = width / 2 - (Skills.COLUMNS * COL_W) / 2 + (COL_W - NODE) / 2;
        return left + s.col() * COL_W;
    }

    private int nodeY(Skill s) {
        return TOP + s.row() * ROW_H;
    }

    private boolean parentUnlocked(Skill s) {
        return s.parent() == null || ClientSkillData.has(s.parent());
    }

    private boolean canUnlock(Skill s) {
        return !ClientSkillData.has(s.id()) && parentUnlocked(s) && ClientSkillData.points() >= s.cost();
    }

    private Skill hovered(double mx, double my) {
        for (Skill s : Skills.ALL) {
            int x = nodeX(s), y = nodeY(s);
            if (mx >= x && mx < x + NODE && my >= y && my < y + NODE) return s;
        }
        return null;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick); // draws the background (once)

        g.drawCenteredString(font, title, width / 2, 14, 0xFFFFFF);
        g.drawCenteredString(font, "Skill Points: " + ClientSkillData.points(), width / 2, 30, GOLD);
        g.drawCenteredString(font, "Earn 1 point per XP level", width / 2, 44, 0xAAAAAA);

        // connector lines
        for (Skill s : Skills.ALL) {
            if (s.parent() == null) continue;
            Skill p = Skills.BY_ID.get(s.parent());
            int cx = nodeX(p) + NODE / 2;
            g.fill(cx - 1, nodeY(p) + NODE + 2, cx + 1, nodeY(s) - 2,
                    ClientSkillData.has(p.id()) ? GREEN : GRAY);
        }

        // nodes
        for (Skill s : Skills.ALL) {
            int x = nodeX(s), y = nodeY(s);
            int border = ClientSkillData.has(s.id()) ? GREEN : canUnlock(s) ? GOLD : GRAY;
            g.fill(x - 2, y - 2, x + NODE + 2, y + NODE + 2, border);
            g.fill(x, y, x + NODE, y + NODE, 0xFF202020);
            g.renderItem(new ItemStack(s.icon()), x + 4, y + 4);
            g.drawCenteredString(font, s.name(), x + NODE / 2, y + NODE + 5, 0xFFFFFF);
        }

        // tooltip
        Skill h = hovered(mouseX, mouseY);
        if (h != null) {
            List<Component> tip = new ArrayList<>();
            tip.add(Component.literal(h.name()).withStyle(ChatFormatting.BOLD));
            tip.add(Component.literal(h.description()).withStyle(ChatFormatting.GREEN));
            tip.add(Component.literal("Cost: " + h.cost() + " point(s)").withStyle(ChatFormatting.GOLD));
            if (ClientSkillData.has(h.id())) {
                tip.add(Component.literal("Unlocked").withStyle(ChatFormatting.GREEN));
            } else if (!parentUnlocked(h)) {
                tip.add(Component.literal("Requires " + Skills.BY_ID.get(h.parent()).name()).withStyle(ChatFormatting.RED));
            } else if (!canUnlock(h)) {
                tip.add(Component.literal("Not enough points").withStyle(ChatFormatting.RED));
            } else {
                tip.add(Component.literal("Click to unlock").withStyle(ChatFormatting.YELLOW));
            }
            g.renderComponentTooltip(font, tip, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            Skill s = hovered(mx, my);
            if (s != null && canUnlock(s)) {
                PacketDistributor.sendToServer(new UnlockSkillPayload(s.id()));
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ClientModEvents.OPEN_KEY.matches(keyCode, scanCode)) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
