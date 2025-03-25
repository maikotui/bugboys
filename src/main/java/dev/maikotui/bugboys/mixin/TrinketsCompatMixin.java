package dev.maikotui.bugboys.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.compat.TrinketsCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TrinketsCompat.class)
public abstract class TrinketsCompatMixin {
    @Inject(method= "getEquippedSpells(Lnet/spell_engine/api/spell/SpellContainer;Lnet/minecraft/entity/player/PlayerEntity;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void getEquippedSpells(SpellContainer proxyContainer, PlayerEntity player, CallbackInfoReturnable<List<String>> cir) {

    }
}
