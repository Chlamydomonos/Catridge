package xyz.chlamydomonos.catridge.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.gui.menu.CatridgeSurgeryTableMenu
import xyz.chlamydomonos.catridge.loaders.BlockEntityLoader
import xyz.chlamydomonos.catridge.loaders.ItemLoader
import java.util.*

class CatridgeSurgeryTableBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(BlockEntityLoader.CATRIDGE_SURGERY_TABLE, pos, blockState), MenuProvider {
    var playerOn: UUID? = null
        set(value) {
            field = value
            setChanged()
        }

    val containerData = object : ContainerData {
        override fun get(index: Int) = if (playerOn != null) 1 else 0

        override fun set(index: Int, value: Int) {}

        override fun getCount() = 1
    }

    val inputItem = object : ItemStackHandler(1) {
        override fun isItemValid(slot: Int, stack: ItemStack) = stack.`is`(ItemLoader.EMPTY_CATRIDGE)
    }

    val outputItem = object : ItemStackHandler(1) {
        override fun isItemValid(slot: Int, stack: ItemStack) = false
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.putBoolean("has_player", playerOn != null)
        playerOn?.let { tag.putUUID("player_on", it) }
        tag.put("input_item", inputItem.serializeNBT(registries))
        tag.put("output_item", outputItem.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        val hasPlayer = tag.getBoolean("has_player")
        if (hasPlayer) {
            playerOn = tag.getUUID("player_on")
        }
        inputItem.deserializeNBT(registries, tag.getCompound("input_item"))
        outputItem.deserializeNBT(registries, tag.getCompound("output_item"))
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = super.getUpdateTag(registries)
        saveAdditional(tag, registries)
        return tag
    }

    override fun getUpdatePacket() = ClientboundBlockEntityDataPacket.create(this)
    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
        return CatridgeSurgeryTableMenu(containerId, playerInventory, this, containerData)
    }

    override fun getDisplayName() = Component.translatable("gui.${Catridge.MODID}.catridge_surgery_table")
}