package xyz.chlamydomonos.catridge.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

class ModLootTableProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : LootTableProvider(output, emptySet() , listOf(
    SubProviderEntry({ ModBlockLootSubProvider(it) }, LootContextParamSets.BLOCK)
), registries)