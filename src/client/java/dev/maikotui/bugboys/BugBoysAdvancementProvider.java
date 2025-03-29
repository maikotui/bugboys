package dev.maikotui.bugboys;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.internals.SpellRegistry;

import java.util.function.Consumer;

import static dev.maikotui.bugboys.Utils.SPELLBOOK_IDS;
import static dev.maikotui.bugboys.Utils.SPELL_IDS;

public class BugBoysAdvancementProvider extends FabricAdvancementProvider {
    protected BugBoysAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        for (String id : SPELLBOOK_IDS) {
            String advancementId = Utils.GenerateSpellbookAdvancementPath(id);
            Advancement advancement = Advancement.Builder.create()
                    .display(
                            Items.ENCHANTED_BOOK, // Icon
                            Text.translatable("advancement.bugboys." + advancementId + ".title"),
                            Text.translatable("advancement.bugboys." + advancementId + ".desc"),
                            null,
                            AdvancementFrame.TASK,
                            false,
                            false,
                            true
                    )
                    .criterion("injected", new ImpossibleCriterion.Conditions())
                    .build(new Identifier("bugboys", advancementId));

            consumer.accept(advancement);
        }

        for (String id : SPELL_IDS) {
            String advancementId = Utils.GenerateSpellAdvancementPath(id);
            Advancement advancement = Advancement.Builder.create()
                    .display(
                            Items.ENCHANTED_BOOK, // Icon
                            Text.translatable("advancement.bugboys." + advancementId + ".title"),
                            Text.translatable("advancement.bugboys." + advancementId + ".desc"),
                            null,
                            AdvancementFrame.TASK,
                            false,
                            false,
                            true
                    )
                    .criterion("injected", new ImpossibleCriterion.Conditions())
                    .build(new Identifier("bugboys", advancementId));

            consumer.accept(advancement);
        }
    }
}
