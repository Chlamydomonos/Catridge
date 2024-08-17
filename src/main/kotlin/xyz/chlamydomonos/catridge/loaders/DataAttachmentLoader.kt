package xyz.chlamydomonos.catridge.loaders

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.catridge.Catridge

object DataAttachmentLoader {
    private val ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Catridge.MODID)

    fun register(bus: IEventBus) {
        ATTACHMENT_TYPES.register(bus)
    }

    private fun <T> register(
        name: String,
        builder: () -> AttachmentType<T>
    ): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
        return ATTACHMENT_TYPES.register(name, builder)
    }

    val ON_CATRIDGE_SURGERY_TABLE by register("on_catridge_surgery_table") { AttachmentType.builder { -> false }.build() }
}