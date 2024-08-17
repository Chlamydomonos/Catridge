package xyz.chlamydomonos.catridge.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.chlamydomonos.catridge.blocks.CatridgeSurgeryTableBlock;
import xyz.chlamydomonos.catridge.loaders.BlockLoader;
import xyz.chlamydomonos.catridge.loaders.DataAttachmentLoader;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract Optional<BlockPos> getSleepingPos();

    @WrapWithCondition(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;stopSleeping()V"
        )
    )
    boolean wrapTickStopSleeping(LivingEntity instance) {
        return !instance.getData(DataAttachmentLoader.INSTANCE.getON_CATRIDGE_SURGERY_TABLE());
    }

    @Inject(
        method = "getBedOrientation",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    void injectGetBedOrientation(CallbackInfoReturnable<Direction> cir) {
        var sleepingPos = this.getSleepingPos();
        if (sleepingPos.isPresent()) {
            var pos = sleepingPos.get();
            var state = this.level().getBlockState(pos);
            if (state.is(BlockLoader.INSTANCE.getCATRIDGE_SURGERY_TABLE().getBlock())) {
                cir.setReturnValue(state.getValue(CatridgeSurgeryTableBlock.Companion.getFACING()));
            }
        }
    }
}
