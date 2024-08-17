package xyz.chlamydomonos.catridge.loaders

import net.minecraft.data.DataProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.datagen.ModBlockStateProvider
import xyz.chlamydomonos.catridge.datagen.ModLootTableProvider

@EventBusSubscriber(modid = Catridge.MODID, bus = EventBusSubscriber.Bus.MOD)
object DataGenLoader {
    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val efh = event.existingFileHelper
        val lp = event.lookupProvider

        event.generator.addProvider(
            event.includeClient(),
            DataProvider.Factory { ModBlockStateProvider(it, efh) }
        )

        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { ModLootTableProvider(it, lp) }
        )
    }
}