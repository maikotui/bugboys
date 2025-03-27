package dev.maikotui.bugboys.mixin;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import dev.emi.trinkets.api.TrinketsApi;
import dev.maikotui.bugboys.BugBoysMC;
import io.github.apace100.origins.content.OrbOfOriginItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.puffish.skillsmod.SkillsMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OrbOfOriginItem.class)
public abstract class OrbOfOriginItemMixin {

    @SuppressWarnings("CodeBlock2Expr")
    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient()) {
            // Unequip SpellBooks before revoking achievements
            BugBoysMC.LOGGER.info("Orb of Origins used — clearing cloaked items...");
            TrinketsApi.getTrinketComponent(user).ifPresent(component -> {
                component.getAllEquipped().forEach((pair) -> {
                    ItemStack stack = pair.getRight();
                    if (stack.getItem() instanceof RevelationAware aware && aware.isVisibleTo(user)) {
                        BugBoysMC.LOGGER.info("Unequipping cloaked item '{}' from player '{}'",
                                stack.getName().getString(),
                                user.getName()
                        );
                        user.giveItemStack(stack.copy());
                        pair.getLeft().inventory().setStack(pair.getLeft().index(), ItemStack.EMPTY);
                    }
                });
            });

            if (user instanceof ServerPlayerEntity serverPlayer) {
                // Utils.RevokeAllSpellbookAdvancements(serverPlayer); No longer needed
                SkillsMod skillsMod = SkillsMod.getInstance();
                Identifier skillCategoryId = new Identifier("bugboys", "combat");
                int experienceAmount = skillsMod.getExperience(serverPlayer, skillCategoryId).orElse(0);
                BugBoysMC.LOGGER.debug("User {} had {} experience points in the bugboys:combat category",
                        serverPlayer.getName().toString(),
                        experienceAmount
                );
                skillsMod.resetSkills(serverPlayer, skillCategoryId);
            }
        }
    }
}