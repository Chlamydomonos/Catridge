package xyz.chlamydomonos.catridge.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.blocks.CatridgeSurgeryTableBlock

object BlockLoader {
    class BlockAndItsItem<T : Block>(
        blockHolder: DeferredHolder<Block, T>,
        itemHolder: DeferredHolder<Item, BlockItem>
    ) {
        val block by blockHolder
        val item by itemHolder
    }

    private val BLOCKS = DeferredRegister.create(Registries.BLOCK, Catridge.MODID)
    val BLOCKS_WITH_LOOT = arrayListOf<DeferredHolder<Block, out Block>>()

    private fun <T : Block> register(name: String, block: () -> T): BlockAndItsItem<T> {
        return register(name, 0, block)
    }

    private fun <T : Block> register(name: String, priority: Int, block: () -> T): BlockAndItsItem<T> {
        val registeredBlock = blockOnly(name, block)
        BLOCKS_WITH_LOOT.add(registeredBlock)
        val registeredItem = ItemLoader.register(name, priority) { BlockItem(registeredBlock.get(), Item.Properties()) }
        return BlockAndItsItem(registeredBlock, registeredItem)
    }

    private fun <T : Block> blockOnly(name: String, block: () -> T): DeferredHolder<Block, T> {
        val registeredBlock = BLOCKS.register(name, block)
        BLOCKS_WITH_LOOT.add(registeredBlock)
        return registeredBlock
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
    }

    val CATRIDGE_SURGERY_TABLE = register("catridge_surgery_table", ::CatridgeSurgeryTableBlock)
}