package dev.maikotui.bugboys;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.SkillsMod;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static final List<String> SPELLBOOK_IDS = List.of(
            // Wizards
            "arcane",
            "fire",
            "frost",
            // Rogues and Warriors
            "rogue",
            "warrior",
            // Archer
            "archer",
            // Spellblades
            "frost_battlemage",
            "fire_battlemage",
            "arcane_battlemage",
            "runic_echoes",
            "phoenix",
            "deathchill",
            "vengeance",
            "defiance",
            // Paladins and Priests
            "paladin",
            "priest",
            // Death Knights
            "blood",
            "unholy",
            "frost"
    );

    public static String GenerateSpellbookAdvancementPath(String spellbookId) {
        return "unlock_spellbook_" + spellbookId;
    }

    public static void RevokeAllSpellbookAdvancements(ServerPlayerEntity player) {
        for (String spellbookId : SPELLBOOK_IDS) {
            Identifier advancementId = new Identifier("bugboys", GenerateSpellbookAdvancementPath(spellbookId));
            RevokeAdvancement(player, advancementId);
        }
        player.sendMessage(Text.literal("Your magical knowledge fades..."), true);
    }

    public static void RevokeAdvancement(ServerPlayerEntity player, Identifier advancementId) {
        Advancement advancement = Objects.requireNonNull(player.getServer()).getAdvancementLoader().get(advancementId);
        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);
            for (String criterion : progress.getObtainedCriteria()) {
                player.getAdvancementTracker().revokeCriterion(advancement, criterion);
            }
        }
    }

    public static void ResetPufferfishSkillsCategory(ServerPlayerEntity player, Identifier categoryId) {
        SkillsMod skillsMod = SkillsMod.getInstance();
        int spentPoints = skillsMod.getSpentPoints(player, categoryId).orElse(0);
        int refundAmount = spentPoints * 1;
        skillsMod.resetSkills(player, categoryId);
        skillsMod.addPoints(player, categoryId, new Identifier("bugboys", "orb_refund"), refundAmount, false);
        player.sendMessage(Text.literal("Your skill tree has been reset. " + refundAmount + " points refunded."), false);
    }
}
