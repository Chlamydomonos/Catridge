package xyz.chlamydomonos.catridge.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import xyz.chlamydomonos.catridge.loaders.BlockLoader
import java.util.stream.Collectors

class ModBlockLootSubProvider(
    registries: HolderLookup.Provider
) : BlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags(), registries) {
    override fun generate() {
        add(BlockLoader.CATRIDGE_SURGERY_TABLE.block) { createSinglePropConditionTable(it,  BlockStateProperties.BED_PART, BedPart.FOOT) }
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return BlockLoader.BLOCKS_WITH_LOOT.stream().map { it.get() }.collect(Collectors.toList())
    }
}