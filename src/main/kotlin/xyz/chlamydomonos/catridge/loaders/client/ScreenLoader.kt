package xyz.chlamydomonos.catridge.loaders.client

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.gui.screen.CatridgeSurgeryTableScreen
import xyz.chlamydomonos.catridge.loaders.MenuLoader

@EventBusSubscriber(modid = Catridge.MODID, bus = EventBusSubscriber.Bus.MOD)
object ScreenLoader {
    @SubscribeEvent
    fun registerScreen(event: RegisterMenuScreensEvent) {
        event.register(MenuLoader.CATRIDGE_SURGERY_TABLE, ::CatridgeSurgeryTableScreen)
    }
}