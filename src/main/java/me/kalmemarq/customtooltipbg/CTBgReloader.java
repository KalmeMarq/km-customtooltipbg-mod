package me.kalmemarq.customtooltipbg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.MathHelper;

import java.io.BufferedReader;
import java.util.Optional;

public class CTBgReloader implements SimpleSynchronousResourceReloadListener {
    public static Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private static final TooltipInfo DEFAULT_TOOLTIP_INFO = new TooltipInfo();
    public static TooltipInfo tooltipInfo = DEFAULT_TOOLTIP_INFO;

    @Override
    public Identifier getFabricId() {
        return new Identifier("kmcustomtooltipbg", "tooltip_background");
    }

    @Override
    public void reload(ResourceManager manager) {
        Optional<Resource> res = manager.getResource(new Identifier("kmcustomtooltipbg", "tooltip_background.json"));

        if (res.isPresent()) {
            try {
                BufferedReader reader = res.get().getReader();
                JsonObject obj = GSON.fromJson(reader, JsonObject.class);

                TooltipInfo tInfo = new TooltipInfo();

                if (JsonHelper.hasString(obj, "texture")) {
                    Identifier txr = Identifier.tryParse(JsonHelper.getString(obj, "texture"));
                    if (txr != null) {
                        tInfo.setTexture(txr);
                    }
                }

                if (JsonHelper.hasArray(obj, "uv")) {
                    JsonArray arr = JsonHelper.getArray(obj, "uv");

                    if (arr.size() == 2) {
                        int u = MathHelper.clamp(arr.get(0).getAsInt(), 0, Integer.MAX_VALUE);
                        int v = MathHelper.clamp(arr.get(1).getAsInt(), 0, Integer.MAX_VALUE);
                    
                        tInfo.setUV(u, v);
                    }
                }

                int baseWidth = 256;
                int baseHeight = 256;

                if (JsonHelper.hasArray(obj, "base_size")) {
                    JsonArray arr = JsonHelper.getArray(obj, "base_size");

                    if (arr.size() == 2) {
                        baseWidth = MathHelper.clamp(arr.get(0).getAsInt(), 0, Integer.MAX_VALUE);
                        baseHeight = MathHelper.clamp(arr.get(1).getAsInt(), 0, Integer.MAX_VALUE);
                    
                        tInfo.setUV(baseWidth, baseHeight);
                    }
                }

                int regionWidth = baseWidth;
                int regionHeight = baseHeight;

                if (JsonHelper.hasArray(obj, "uv_size")) {
                    JsonArray arr = JsonHelper.getArray(obj, "uv_size");

                    if (arr.size() == 2) {
                        regionWidth = MathHelper.clamp(arr.get(0).getAsInt(), 0, Integer.MAX_VALUE);
                        regionHeight = MathHelper.clamp(arr.get(1).getAsInt(), 0, Integer.MAX_VALUE);

                    }
                }

                tInfo.setUVSize(regionWidth, regionHeight);

                if (JsonHelper.hasArray(obj, "nineslice_size")) {
                    JsonArray arr = JsonHelper.getArray(obj, "nineslice_size");

                    if (arr.size() == 2) {
                        int u = MathHelper.clamp(arr.get(0).getAsInt(), 0, Integer.MAX_VALUE);
                        int v = MathHelper.clamp(arr.get(1).getAsInt(), 0, Integer.MAX_VALUE);
                        tInfo.setNineslice(u, v);
                    } else if (arr.size() == 4) {
                        int u0 = MathHelper.clamp(arr.get(0).getAsInt(), 0, Integer.MAX_VALUE);
                        int u1 = MathHelper.clamp(arr.get(2).getAsInt(), 0, Integer.MAX_VALUE);
                        int v0 = MathHelper.clamp(arr.get(1).getAsInt(), 0, Integer.MAX_VALUE);
                        int v1 = MathHelper.clamp(arr.get(3).getAsInt(), 0, Integer.MAX_VALUE);

                        tInfo.setNineslice(u0, v0, u1, v1);

                    }
                } else if (JsonHelper.hasNumber(obj, "nineslice_size")) {
                    tInfo.setNineslice(MathHelper.clamp(JsonHelper.getInt(obj, "nineslice_size"), 0, Integer.MAX_VALUE));
                }

                tInfo.setEnabled(JsonHelper.getBoolean(obj, "enabled", false));

                tInfo.setAlpha((float)MathHelper.clamp(JsonHelper.getFloat(obj, "alpha", 1.0f), 0.0, 1.0f));

                if (JsonHelper.hasArray(obj, "color")) {
                    JsonArray arr = JsonHelper.getArray(obj, "color");

                    if (arr.size() == 3) {
                        float r = MathHelper.clamp(arr.get(0).getAsFloat(), 0.0f, 1.0f);
                        float g = MathHelper.clamp(arr.get(1).getAsFloat(), 0.0f, 1.0f);
                        float b = MathHelper.clamp(arr.get(2).getAsFloat(), 0.0f, 1.0f);
                        tInfo.setColor(r, g, b);
                    }

                    if (arr.size() == 4) {
                        tInfo.setAlpha(MathHelper.clamp(arr.get(3).getAsFloat(), 0.0f, 1.0f));
                    }
                }

                if (JsonHelper.hasArray(obj, "padding")) {
                    JsonArray arr = JsonHelper.getArray(obj, "padding");

                    if (arr.size() == 2) {
                        int padX = arr.get(0).getAsInt();
                        int padY = arr.get(1).getAsInt();
                        tInfo.setPadding(padX, padY);
                    } else if (arr.size() == 4) {
                        int padX0 = arr.get(0).getAsInt();
                        int padX1 = arr.get(2).getAsInt();
                        int padY0 = arr.get(1).getAsInt();
                        int padY1 = arr.get(3).getAsInt();
                        tInfo.setPadding(padX0, padY0, padX1, padY1);
                    }
                } else if (JsonHelper.hasNumber(obj, "padding")) {
                    tInfo.setPadding(JsonHelper.getInt(obj, "padding"));
                }

                if (JsonHelper.hasArray(obj, "offset")) {
                    JsonArray arr = JsonHelper.getArray(obj, "offset");

                    if (arr.size() == 2) {
                        int x = arr.get(0).getAsInt();
                        int y = arr.get(1).getAsInt();
                        tInfo.setOffset(x, y);
                    }
                }

                tooltipInfo = tInfo;

            } catch (Exception e) {
                CustomTooltipBgMod.LOGGER.info("Failed to load tooltip_background.json");
                tooltipInfo = DEFAULT_TOOLTIP_INFO;
            }
        }
    }
}
