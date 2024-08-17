package xyz.chlamydomonos.catridge.gui.menu

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.items.SlotItemHandler
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.catridge.blockentities.CatridgeSurgeryTableBlockEntity
import xyz.chlamydomonos.catridge.loaders.MenuLoader

class CatridgeSurgeryTableMenu(
    containerId: Int,
    playerInventory: Inventory,
    pBlockEntity: BlockEntity,
    val containerData: ContainerData
) : AbstractContainerMenu(MenuLoader.CATRIDGE_SURGERY_TABLE, containerId) {
    constructor(containerId: Int, playerInventory: Inventory, buf: RegistryFriendlyByteBuf) : this(
        containerId,
        playerInventory,
        playerInventory.player.level().getBlockEntity(buf.readBlockPos())!!,
        SimpleContainerData(1)
    )

    val blockEntity = pBlockEntity as CatridgeSurgeryTableBlockEntity

    init {
        addSlot(SlotItemHandler(blockEntity.inputItem, 0, 44, 20))
        addSlot(SlotItemHandler(blockEntity.outputItem, 0, 116, 20))

        for (i in 0..2) {
            for (j in 0..8) {
                addSlot(Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18))
            }
        }

        for (k in 0..8) {
            addSlot(Slot(playerInventory, k, 8 + k * 18, 109))
        }
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        return ItemStack.EMPTY //TODO: quick move
    }

    override fun stillValid(player: Player): Boolean {
        return blockEntity.playerOn != null && player.position().distanceTo(blockEntity.blockPos.toVec3()) <= 10
    }
}