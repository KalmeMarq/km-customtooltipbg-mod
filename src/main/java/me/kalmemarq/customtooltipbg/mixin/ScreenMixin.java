package me.kalmemarq.customtooltipbg.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.customtooltipbg.CTBgReloader;
import me.kalmemarq.customtooltipbg.NinesliceDrawer;
import me.kalmemarq.customtooltipbg.TooltipInfo;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin extends DrawableHelper {
    @Shadow public int width;
    @Shadow public int height;
    @Shadow protected TextRenderer textRenderer;
    @Shadow protected ItemRenderer itemRenderer;

    @Inject(at = @At("HEAD"), method = "renderTooltipFromComponents", cancellable = true)
    private void injectRenderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo info) {
        TooltipComponent tooltipComponent2;
        int t;
        int k;
        if (components.isEmpty()) {
            return;
        }
        int i = 0;
        int j = components.size() == 1 ? -2 : 0;
        for (TooltipComponent tooltipComponent : components) {
            k = tooltipComponent.getWidth(this.textRenderer);
            if (k > i) {
                i = k;
            }
            j += tooltipComponent.getHeight();
        }
        int l = x + 12;
        int m = y - 12;
        k = i;
        int n = j;
        if (l + i > this.width) {
            l -= 28 + i;
        }
        if (m + n + 6 > this.height) {
            m = this.height - n - 6;
        }
        matrices.push();
        int o = -267386864;
        int p = 0x505000FF;
        int q = 1344798847;
        int r = 400;
        float f = this.itemRenderer.zOffset;
        this.itemRenderer.zOffset = 400.0f;
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        TooltipInfo ctInfo = CTBgReloader.tooltipInfo;

        if (ctInfo.enabled) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ctInfo.texture);

            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            NinesliceDrawer.renderTexture(matrices, l - 4 - ctInfo.paddingX0 + ctInfo.offsetX, m - 4 - ctInfo.paddingY0 + ctInfo.offsetY, r, k + 8 + ctInfo.paddingX0 + ctInfo.paddingX1, n + 8 + ctInfo.paddingY0 + ctInfo.paddingY1, ctInfo);
            RenderSystem.disableBlend();
        } else {
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + k + 3, m - 3, r, o, o);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 3, m + n + 3, l + k + 3, m + n + 4, r, o, o);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + k + 3, m + n + 3, r, o, o);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + n + 3, r, o, o);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l + k + 3, m - 3, l + k + 4, m + n + 3, r, o, o);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + n + 3 - 1, r, p, q);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l + k + 2, m - 3 + 1, l + k + 3, m + n + 3 - 1, r, p, q);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + k + 3, m - 3 + 1, r, p, p);
            ScreenMixin.fillGradient(matrix4f, bufferBuilder, l - 3, m + n + 2, l + k + 3, m + n + 3, r, q, q);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BufferRenderer.drawWithShader(bufferBuilder.end());
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
        }

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        matrices.translate(0.0, 0.0, 400.0);
        int s = m;
        for (t = 0; t < components.size(); ++t) {
            tooltipComponent2 = components.get(t);
            tooltipComponent2.drawText(this.textRenderer, l, s, matrix4f, immediate);
            s += tooltipComponent2.getHeight() + (t == 0 ? 2 : 0);
        }
        immediate.draw();
        matrices.pop();
        s = m;
        for (t = 0; t < components.size(); ++t) {
            tooltipComponent2 = components.get(t);
            tooltipComponent2.drawItems(this.textRenderer, l, s, matrices, this.itemRenderer, 400);
            s += tooltipComponent2.getHeight() + (t == 0 ? 2 : 0);
        }
        this.itemRenderer.zOffset = f;
        info.cancel();
    }
}
