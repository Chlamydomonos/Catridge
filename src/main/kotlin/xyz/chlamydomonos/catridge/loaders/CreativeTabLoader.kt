package xyz.chlamydomonos.catridge.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import xyz.chlamydomonos.catridge.Catridge

object CreativeTabLoader {
    private val CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Catridge.MODID)

    @Suppress("unused")
    val HYPHACRAFT_TAB = CREATIVE_TABS.register("hyphacraft_tab") {
        ->
        CreativeModeTab.builder()
            .title(Component.translatable("tab.hyphacraft"))
            .icon { ItemStack.EMPTY }
            .displayItems { _, output -> ItemLoader.ITEMS_QUEUE.forEach{ output.accept(it.item.get()) } }
            .build()
    }

    fun register(bus: IEventBus) {
        CREATIVE_TABS.register(bus)
    }
}