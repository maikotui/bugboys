package dev.maikotui.bugboys.mixin;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.item.trinket.SpellBookTrinketItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Map;

/**
 * Mixin to make SpellBookTrinketItem Revelation-aware
 */
@Mixin(SpellBookTrinketItem.class)
public abstract class SpellBookTrinketItemMixin implements RevelationAware, Trinket {

	@Unique
	private static final Identifier CLOAK_ADVANCEMENT = new Identifier("bugboys", "unlock_spellbooks");

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstruct(Identifier poolId, Item.Settings settings, CallbackInfo ci) {
		System.out.println("WE CONSTRUCTED");
		RevelationAware.register(this);
	}

	/**
	 * Determines the advancement required to reveal this item.
	 */
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return CLOAK_ADVANCEMENT;
	}

	@Override
	@Nullable
	public Pair<Block, MutableText> getCloakedBlockTranslation() {
		return null;
	}

	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Collections.emptyMap();
	}

	/**
	 * Cloaked item mapping: how this item appears when hidden.
	 */
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>((Item)(Object)this, net.minecraft.item.Items.BOOK);
	}

	/**
	 * Optional: trigger behavior when item is hidden (noop).
	 */
	@Override
	public void onCloak() {
		// Optional: Add visual/sound effects
	}

	/**
	 * Optional: trigger behavior when item becomes visible (noop).
	 */
	@Override
	public void onUncloak() {
		// Optional: Add visual/sound effects
		System.out.println("HERES A THING THAT HAPPENED");
	}

	/**
	 * Optional: custom translation shown when the item is cloaked.
	 */
	@Override
	public Pair<Item, MutableText> getCloakedItemTranslation() {
		return new Pair<>((Item)(Object)this, Text.translatable("item.bugboys.cloaked_spellbook"));
	}

	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity player) {
			boolean hasAccess = isVisibleTo(player);
			if (!hasAccess) {
				System.out.println("[Bugboys] Prevented cloaked SpellBook from being equipped.");
			}
			return hasAccess;
		}
		return true;
	}
}
