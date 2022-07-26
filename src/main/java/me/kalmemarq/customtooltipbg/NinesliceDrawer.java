package me.kalmemarq.customtooltipbg;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class NinesliceDrawer {
    public static void renderTexture(MatrixStack matrices, int x, int y, int z, int width, int height, TooltipInfo tooltipInfo) {
        renderTexture(matrices.peek().getPositionMatrix(), x, y, z, width, height, tooltipInfo);
    }

    public static void renderTexture(Matrix4f matrix, int x, int y, int z, int width, int height, TooltipInfo tooltipInfo) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

        NinesliceInfo nsInfo = tooltipInfo.nsInfo;

        // Top Left
        renderTextureRegion(matrix, bufferBuilder, x, y, z, nsInfo.u0, nsInfo.v0, tooltipInfo.u, tooltipInfo.v, nsInfo.u0, nsInfo.v0, nsInfo, tooltipInfo);
        // Top Middle
        renderTextureRegion(matrix, bufferBuilder, x + nsInfo.u0, y, z, width - nsInfo.u0 - nsInfo.u1, nsInfo.v0, tooltipInfo.u + nsInfo.u0, tooltipInfo.v, tooltipInfo.regionWidth - nsInfo.u0 - nsInfo.u1, nsInfo.v0, nsInfo, tooltipInfo);
        // Top Right
        renderTextureRegion(matrix, bufferBuilder, x + width - nsInfo.u1, y, z, nsInfo.u1, nsInfo.v0, tooltipInfo.u + tooltipInfo.regionWidth - nsInfo.u1, tooltipInfo.v, nsInfo.u1, nsInfo.v0, nsInfo, tooltipInfo);
        // Left Middle
        renderTextureRegion(matrix, bufferBuilder, x, y + nsInfo.v0, z, nsInfo.u0, height - nsInfo.v0 - nsInfo.v1, tooltipInfo.u, tooltipInfo.v + nsInfo.v0, nsInfo.u0, tooltipInfo.regionHeight - nsInfo.v0 - nsInfo.v1, nsInfo, tooltipInfo);
        // Center
        renderTextureRegion(matrix, bufferBuilder, x + nsInfo.u0, y + nsInfo.v0, z, width - nsInfo.u0 - nsInfo.u1, height - nsInfo.v0 - nsInfo.v1, tooltipInfo.u + nsInfo.u0, tooltipInfo.v + nsInfo.v0, tooltipInfo.regionWidth - nsInfo.u0 - nsInfo.u1, tooltipInfo.regionHeight - nsInfo.v0 - nsInfo.v1, nsInfo, tooltipInfo);
        // Right Middle
        renderTextureRegion(matrix, bufferBuilder, x + width - nsInfo.u1, y + nsInfo.v0, z, nsInfo.u0,height - nsInfo.v0 - nsInfo.v1, tooltipInfo.u + tooltipInfo.regionWidth - nsInfo.u1, tooltipInfo.v + nsInfo.v0, nsInfo.u0, tooltipInfo.regionHeight - nsInfo.v0 - nsInfo.v1, nsInfo, tooltipInfo);
        // Bottom Left
        renderTextureRegion(matrix, bufferBuilder, x, y + height - nsInfo.v1, z, nsInfo.u0, nsInfo.v1, tooltipInfo.u, tooltipInfo.v + tooltipInfo.regionHeight - nsInfo.v1, nsInfo.u0, nsInfo.v1, nsInfo, tooltipInfo);
        // Bottom Middle
        renderTextureRegion(matrix, bufferBuilder, x + nsInfo.u0, y + height - nsInfo.v1, z,width - nsInfo.u0 - nsInfo.u1, nsInfo.v1, tooltipInfo.u + nsInfo.u0, tooltipInfo.v + tooltipInfo.regionHeight - nsInfo.v1, tooltipInfo.regionWidth - nsInfo.u0 - nsInfo.u1, nsInfo.v1, nsInfo, tooltipInfo);
        // Bottom Right
        renderTextureRegion(matrix, bufferBuilder, x + width - nsInfo.u1, y + height - nsInfo.v1, z, nsInfo.u0, nsInfo.v1, tooltipInfo.u + tooltipInfo.regionWidth - nsInfo.u1, tooltipInfo.v + tooltipInfo.regionHeight - nsInfo.v1, nsInfo.u0, nsInfo.v1, nsInfo, tooltipInfo);

        BufferRenderer.drawWithShader(bufferBuilder.end());
    }

    private static void renderTextureRegion(Matrix4f matrix, BufferBuilder bufferBuilder, int x, int y, int z, int width, int height, int u, int v, int regionWidth, int regionHeight, NinesliceInfo nsInfo, TooltipInfo tooltipInfo) {
        int x0 = x;
        int x1 = x + width;
        int y0 = y;
        int y1 = y + height;

        float u0 = (u + 0.0f) / nsInfo.baseWidth;
        float v0 = (v + 0.0f) / nsInfo.baseHeight;
        float u1 = (u + (float)regionWidth) / nsInfo.baseWidth;
        float v1 = (v + (float)regionHeight) / nsInfo.baseHeight;

        float r = tooltipInfo.r;
        float g = tooltipInfo.g;
        float b = tooltipInfo.b;
        float a = tooltipInfo.alpha;

        bufferBuilder.vertex(matrix, x0, y1, z).color(r, g, b, a).texture(u0, v1).next();
        bufferBuilder.vertex(matrix, x1, y1, z).color(r, g, b, a).texture(u1, v1).next();
        bufferBuilder.vertex(matrix, x1, y0, z).color(r, g, b, a).texture(u1, v0).next();
        bufferBuilder.vertex(matrix, x0, y0, z).color(r, g, b, a).texture(u0, v0).next();
    }
}
