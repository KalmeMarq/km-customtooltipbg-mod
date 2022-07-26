package me.kalmemarq.customtooltipbg;

public class NinesliceInfo {
    public int u0 = 0;
    public int v0 = 0;
    public int u1 = 0;
    public int v1 = 0;
    public int baseWidth = 256;
    public int baseHeight = 256;

    public NinesliceInfo() {
    }

    public NinesliceInfo setBaseSize(int width, int height) {
        this.baseWidth = width;
        this.baseHeight = height;
        return this;
    }

    public NinesliceInfo setNineslice(int xy) {
        return setNineslice(xy, xy);
    }

    public NinesliceInfo setNineslice(int x, int y) {
        return setNineslice(x, y, x, y);
    }

    public NinesliceInfo setNineslice(int x0, int y0, int x1, int y1) {
        this.u0 = x0;
        this.v0 = y0;
        this.u1 = x1;
        this.v1 = y1;
        return this;
    }
}
