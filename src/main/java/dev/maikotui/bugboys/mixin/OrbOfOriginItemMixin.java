package dev.maikotui.bugboys.mixin;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import dev.emi.trinkets.api.TrinketsApi;
import dev.maikotui.bugboys.BugBoysMC;
import dev.maikotui.bugboys.Utils;
import io.github.apace100.origins.content.OrbOfOriginItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OrbOfOriginItem.class)
public abstract class OrbOfOriginItemMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient()) {
            // Unequip SpellBooks before revoking achievements
            BugBoysMC.LOGGER.info("Orb of Origins used — clearing cloaked items...");
            TrinketsApi.getTrinketComponent(user).ifPresent(component -> {
                component.getAllEquipped().forEach((pair) -> {
                    ItemStack stack = pair.getRight();
                    if (stack.getItem() instanceof RevelationAware aware && aware.isVisibleTo(user)) {
                        BugBoysMC.LOGGER.info("Unequipping cloaked item: {}", stack.getName().getString());
                        user.giveItemStack(stack.copy());
                        pair.getLeft().inventory().setStack(pair.getLeft().index(), ItemStack.EMPTY);
                    }
                });
            });

            // Revoke all spellbook achievements
            if (user instanceof ServerPlayerEntity serverPlayer) {
                Utils.RevokeAllSpellbookAdvancements(serverPlayer);
            }
        }
    }
}