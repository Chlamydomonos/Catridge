package xyz.chlamydomonos.catridge.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.blocks.CatridgeSurgeryTableBlock

class ModBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, Catridge.MODID, exFileHelper) {
    override fun registerStatesAndModels() {
        CatridgeSurgeryTableBlock.genModel(this)
    }
}