package dev.maikotui.bugboys;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class BugBoysAdvancementProvider extends FabricAdvancementProvider {

    protected BugBoysAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        List<String> spellbookIds = List.of(
            "arcane", "fire", "frost" // Wizards books
        );
        for (String id : spellbookIds) {
            String advancementId = "unlock_spellbook_" + id;

            Advancement advancement = Advancement.Builder.create()
                    .display(
                            Items.ENCHANTED_BOOK, // Icon
                            Text.translatable("advancement.bugboys." + advancementId + ".title"),
                            Text.translatable("advancement.bugboys." + advancementId + ".desc"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("injected", new ImpossibleCriterion.Conditions())
                    .build(new Identifier("bugboys", advancementId));

            consumer.accept(advancement);
        }
    }
}
