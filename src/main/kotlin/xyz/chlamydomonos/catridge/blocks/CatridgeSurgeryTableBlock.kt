package xyz.chlamydomonos.catridge.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.blockentities.CatridgeSurgeryTableBlockEntity
import xyz.chlamydomonos.catridge.datagen.ModBlockStateProvider
import xyz.chlamydomonos.catridge.loaders.BlockLoader
import xyz.chlamydomonos.catridge.loaders.DataAttachmentLoader

class CatridgeSurgeryTableBlock : BaseEntityBlock(
    Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()
) {
    companion object {
        val CODEC = simpleCodec { CatridgeSurgeryTableBlock() }
        val FACING = BlockStateProperties.HORIZONTAL_FACING
        val PART = BlockStateProperties.BED_PART
        val SHAPE = box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)

        fun getNeighbourDirection(part: BedPart, direction: Direction): Direction {
            return if (part == BedPart.FOOT) direction else direction.opposite
        }

        fun genModel(provider: ModBlockStateProvider) {
            val builder = provider.getVariantBuilder(BlockLoader.CATRIDGE_SURGERY_TABLE.block)
            val head = ConfiguredModel(provider.models().getExistingFile(
                ResourceLocation.fromNamespaceAndPath(
                    Catridge.MODID,
                    "${ModelProvider.BLOCK_FOLDER}/catridge_surgery_table_head"
                )
            ))
            builder.partialState().with(PART, BedPart.HEAD).setModels(head)

            var direction = Direction.NORTH
            var yRot = 0
            val model = provider.models().getExistingFile(
                ResourceLocation.fromNamespaceAndPath(
                    Catridge.MODID,
                    "${ModelProvider.BLOCK_FOLDER}/catridge_surgery_table"
                )
            )
            for (i in 1..4) {
                builder.partialState().with(PART, BedPart.FOOT).with(FACING, direction)
                    .modelForState()
                    .modelFile(model)
                    .rotationY(yRot)
                    .addModel()
                direction = direction.clockWise
                yRot += 90
            }
            provider.simpleBlockItem(BlockLoader.CATRIDGE_SURGERY_TABLE.block, model)
        }
    }

    init {
        registerDefaultState(defaultBlockState().setValue(PART, BedPart.FOOT).setValue(FACING, Direction.NORTH))
    }

    override fun codec() = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return if (state.getValue(PART) == BedPart.FOOT) {
            null
        } else {
            CatridgeSurgeryTableBlockEntity(pos, state)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, PART)
    }

    override fun getRenderShape(state: BlockState) = RenderShape.MODEL

    override fun updateShape(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        level: LevelAccessor,
        currentPos: BlockPos,
        facingPos: BlockPos
    ): BlockState {
        return if (facing == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            if (facingState.`is`(this) && facingState.getValue(PART) != state.getValue(PART)) {
                state
            } else {
                Blocks.AIR.defaultBlockState()
            }
        } else {
            super.updateShape(state, facing, facingState, level, currentPos, facingPos)
        }
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        if (!level.isClientSide) {
            val blockPos = pos.relative(state.getValue(FACING))
            level.setBlock(blockPos, state.setValue(BedBlock.PART, BedPart.HEAD), 3)
            level.blockUpdated(pos, Blocks.AIR)
            state.updateNeighbourShapes(level, pos, 3)
        }
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val direction = context.horizontalDirection
        val blockPos = context.clickedPos
        val blockPos1 = blockPos.relative(direction)
        val level = context.level
        return if (
            level.getBlockState(blockPos1).canBeReplaced(context) && level.worldBorder.isWithinBounds(blockPos1)
        ) {
            defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction)
        } else {
            null
        }
    }

    private fun getHeadPos(pos: BlockPos, state: BlockState): BlockPos {
        val part = state.getValue(PART)
        val facing = state.getValue(FACING)
        return if (part == BedPart.HEAD) pos else pos.offset(facing.normal)
    }

    private fun lieOn(player: Player, pos: BlockPos, blockEntity: CatridgeSurgeryTableBlockEntity) {
        player.pose = Pose.SLEEPING
        player.setPos(pos.x + 0.5, pos.y + 0.6875, pos.z + 0.5)
        player.setSleepingPos(pos)
        player.deltaMovement = Vec3.ZERO
        player.hasImpulse = true
        player.setData(DataAttachmentLoader.ON_CATRIDGE_SURGERY_TABLE, true)
        blockEntity.playerOn = player.uuid
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.CONSUME
        }

        val headPos = getHeadPos(pos, state)
        val be = level.getBlockEntity(headPos) as CatridgeSurgeryTableBlockEntity
        if (be.playerOn == null) {
            lieOn(player, headPos, be)
            return InteractionResult.SUCCESS
        }

        player.openMenu(getMenuProvider(state, level, pos)!!, pos)
        return InteractionResult.SUCCESS
    }
}