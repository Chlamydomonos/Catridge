package xyz.chlamydomonos.catridge.misc

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent
import xyz.chlamydomonos.catridge.blockentities.CatridgeSurgeryTableBlockEntity
import xyz.chlamydomonos.catridge.loaders.DataAttachmentLoader

@EventBusSubscriber
object PlayerEventSubscriber {
    @SubscribeEvent
    fun onPlayerWakeUp(event: PlayerWakeUpEvent) {
        if (event.entity.level().isClientSide) {
            return
        }

        val player = event.entity
        if (player.getData(DataAttachmentLoader.ON_CATRIDGE_SURGERY_TABLE)) {
            val pos = player.sleepingPos.get()
            val be = player.level().getBlockEntity(pos) as CatridgeSurgeryTableBlockEntity
            be.playerOn = null
        }
    }
}