package dev.maikotui.bugboys.mixin;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import dev.maikotui.bugboys.BugBoysMC;
import dev.maikotui.bugboys.Utils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.spellbinding.SpellBindingScreenHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(SpellBindingScreenHandler.class)
public abstract class SpellBindingScreenHandlerMixin extends ScreenHandler {
    @Shadow(remap = false) @Final
    public int[] spellId;
    @Shadow(remap = false) @Final
    public int[] spellPoweredByLib;

    @Unique
    private PlayerEntity boundPlayer;

    protected SpellBindingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At("TAIL"))
    private void capturePlayer(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        this.boundPlayer = playerInventory.player;
    }

    @Inject(method = "lambda$onContentChanged$0(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE", target = "net/spell_engine/spellbinding/SpellBindingScreenHandler.sendContentUpdates ()V"))
    public void modifySpellOffers(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci) {
        // Safely retrieve the player who opened the UI
        PlayerEntity player = this.boundPlayer;

        if (player == null) {
            return;  // Avoid null pointer exception
        }

        if (player.getAbilities().creativeMode) {
            return; // Don't do anything if the player is in creative mode
        }

        for (int targetSpellIndex = 0; targetSpellIndex < SpellBindingScreenHandler.MAXIMUM_SPELL_COUNT; targetSpellIndex++) {
            int spellRawId = spellId[targetSpellIndex];
            if (spellRawId != 0) {
                Optional<Identifier> spellIdOptional = SpellRegistry.fromRawSpellId(spellRawId);
                Identifier advancementId = new Identifier("bugboys", spellIdOptional
                        .map(Identifier::toString)
                        .map(Utils::GenerateSpellAdvancementPath)
                        .orElse("unknown_spell"));
                BugBoysMC.LOGGER.debug("We checked for {}", advancementId);
                if (!AdvancementHelper.hasAdvancement(player, advancementId)) {
                    BugBoysMC.LOGGER.debug("Locked spell {}", spellIdOptional);
                    spellPoweredByLib[targetSpellIndex] = 0;
                }
            }
        }

        sendContentUpdates();
    }
}
