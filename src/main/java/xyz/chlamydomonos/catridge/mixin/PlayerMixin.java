package xyz.chlamydomonos.catridge.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.chlamydomonos.catridge.loaders.DataAttachmentLoader;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @WrapWithCondition(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;stopSleepInBed(ZZ)V"
        )
    )
    boolean wrapTickStopSleepInBed(Player instance, boolean wakeImmediately, boolean updateLevelForSleepingPlayers) {
        return !instance.getData(DataAttachmentLoader.INSTANCE.getON_CATRIDGE_SURGERY_TABLE());
    }
}
