package xyz.chlamydomonos.catridge

import com.mojang.logging.LogUtils
import net.neoforged.fml.common.Mod
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import xyz.chlamydomonos.catridge.loaders.*

@Mod(Catridge.MODID)
object Catridge {
    const val MODID = "catridge"

    val LOGGER = LogUtils.getLogger()
    init {
        BlockLoader.register(MOD_BUS)
        ItemLoader.register(MOD_BUS)
        CreativeTabLoader.register(MOD_BUS)
        BlockEntityLoader.register(MOD_BUS)
        DataAttachmentLoader.register(MOD_BUS)
        MenuLoader.register(MOD_BUS)
    }
}