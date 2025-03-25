package dev.maikotui.bugboys;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static final List<String> SPELLBOOK_IDS = List.of(
            // Wizards
            "arcane", "fire", "frost"
    );

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

    public static String GenerateSpellbookAdvancementPath(String spellbookId) {
        return "unlock_spellbook_" + spellbookId;
    }
}
