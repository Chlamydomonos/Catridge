package xyz.chlamydomonos.catridge.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.gui.menu.CatridgeSurgeryTableMenu

object MenuLoader {
    private val MENU_TYPES = DeferredRegister.create(Registries.MENU, Catridge.MODID)

    private fun <T : AbstractContainerMenu> register(
        name: String,
        menu: (Int, Inventory, RegistryFriendlyByteBuf) -> T
    ): DeferredHolder<MenuType<*>, MenuType<T>> {
        return MENU_TYPES.register(name) { -> IMenuTypeExtension.create(IContainerFactory(menu)) }
    }

    fun register(bus: IEventBus) {
        MENU_TYPES.register(bus)
    }

    val CATRIDGE_SURGERY_TABLE by register("catridge_surgery_table", ::CatridgeSurgeryTableMenu)
}