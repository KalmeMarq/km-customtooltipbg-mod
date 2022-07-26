package me.kalmemarq.customtooltipbg;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.Identifier;

public class TooltipInfo {
    public boolean enabled = false;
    @NotNull
    public Identifier texture = new Identifier("textures/gui/widgets.png");
    public int u = 0;
    public int v = 0;
    public int regionWidth = 0;
    public int regionHeight = 0;
    public NinesliceInfo nsInfo = new NinesliceInfo();
    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public float alpha = 1.0f;
    public int paddingX0 = 0;
    public int paddingY0 = 0;
    public int paddingX1 = 0;
    public int paddingY1 = 0;
    public int offsetX = 0;
    public int offsetY = 0;

    public TooltipInfo() {
    }

    public TooltipInfo setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public TooltipInfo setTexture(@NotNull Identifier texture) {
        this.texture = texture;
        return this;
    }

    public TooltipInfo setUV(int u, int v) {
        this.u = u;
        this.v = v;
        return this;
    }

    public TooltipInfo setUVSize(int regionWidth, int regionHeight) {
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
        return this;
    }

    public TooltipInfo setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
        return this;
    }

    public TooltipInfo setColor(float r, float g, float b) {
        return setColor(r, g, b, this.alpha);
    }

    public TooltipInfo setColor(float r, float g, float b, float alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
        return this;
    }

    public TooltipInfo setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public TooltipInfo setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public TooltipInfo setPadding(int padX, int padY) {
        return setPadding(padX, padY, padX, padY);
    }

    public TooltipInfo setPadding(int padX0, int padY0, int padX1, int padY1) {
        this.paddingX0 = padX0;
        this.paddingY0 = padY0;
        this.paddingX1 = padX1;
        this.paddingY1 = padY1;
        return this;
    }

    public TooltipInfo setBaseSize(int width, int height) {
        this.nsInfo.setBaseSize(width, height);
        return this;
    }

    public TooltipInfo setNineslice(int xy) {
        this.nsInfo.setNineslice(xy);
        return this;
    }

    public TooltipInfo setNineslice(int x, int y) {
        this.nsInfo.setNineslice(x, y);
        return this;
    }

    public TooltipInfo setNineslice(int x0, int y0, int x1, int y1) {
        this.nsInfo.setNineslice(x0, y0, x1, y1);
        return this;
    }
}
