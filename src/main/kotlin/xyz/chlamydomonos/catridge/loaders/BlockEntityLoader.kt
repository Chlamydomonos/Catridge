package xyz.chlamydomonos.catridge.loaders

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.blockentities.CatridgeSurgeryTableBlockEntity

object BlockEntityLoader {
    private val BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Catridge.MODID)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T : BlockEntity> register(
        name: String,
        blockEntity: (BlockPos, BlockState) -> T,
        block: () -> Block
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
        return BLOCK_ENTITIES.register(name) { ->
            BlockEntityType.Builder.of(blockEntity, block()).build(null)
        }
    }

    fun register(bus: IEventBus) {
        BLOCK_ENTITIES.register(bus)
    }

    val CATRIDGE_SURGERY_TABLE by register("catridge_surgery_table", ::CatridgeSurgeryTableBlockEntity) { BlockLoader.CATRIDGE_SURGERY_TABLE.block }
}