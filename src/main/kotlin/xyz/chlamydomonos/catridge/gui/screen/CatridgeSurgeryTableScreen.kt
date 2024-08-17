package xyz.chlamydomonos.catridge.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import xyz.chlamydomonos.catridge.Catridge
import xyz.chlamydomonos.catridge.gui.menu.CatridgeSurgeryTableMenu

class CatridgeSurgeryTableScreen(
    menu: CatridgeSurgeryTableMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<CatridgeSurgeryTableMenu>(menu, playerInventory, title) {
    companion object {
        val BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            Catridge.MODID,
            "textures/gui/catridge_surgery_table.png"
        )
        const val TEXTURE_WIDTH = 176
        const val TEXTURE_HEIGHT = 133
    }

    val button = Button.builder(Component.translatable("gui.catridge.create_catridge")) {
        Catridge.LOGGER.debug("test")
    }.pos(69, 20).size(32, 16).build()
    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
        guiGraphics.blit(BACKGROUND, x, y, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        button.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }
}