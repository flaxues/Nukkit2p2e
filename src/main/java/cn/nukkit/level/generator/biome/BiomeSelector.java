package cn.nukkit.level.generator.biome;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.math.NukkitRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BiomeSelector {
    private final Biome fallback;
    private final Simplex temperature;
    private final Simplex rainfall;

    private final Map<Integer, Biome> biomes = new HashMap<>();

    private int[] map = new int[64 * 64];

    public BiomeSelector(NukkitRandom random, Biome fallback) {
        this.fallback = fallback;
        this.temperature = new Simplex(random, 2F, 1F / 8F, 1F / 1024F);
        this.rainfall = new Simplex(random, 2F, 1F / 8F, 1F / 1024F);
    }

    public int lookup(double temperature, double rainfall) {
        if (temperature < 0.5)  {
            //cold
            if (temperature < 0.15)  {
                if (rainfall < 0.5) {
                    return Biome.MOUNTAINS;
                } else {
                    return Biome.SMALL_MOUNTAINS;
                }
            }

            if (temperature < 0.4)  {
                if (rainfall < 0.5) {
                    return Biome.TAIGA;
                } else {
                    return Biome.ICE_PLAINS;
                }
            }

            if (rainfall < 0.5) {
                return Biome.PLAINS;
            } else {
                return Biome.RIVER;
            }
        } else {
            //warm
            if (temperature < 0.6) {
                if (rainfall < 0.5) {
                    return Biome.BIRCH_FOREST;
                } else {
                    return Biome.FOREST;
                }
            }

            if (temperature < 0.7)  {
                return Biome.ROOFED_FOREST;
            }

            if (rainfall < 0.25)    {
                if (temperature < 0.85) {
                    return Biome.DESERT;
                } else {
                    return Biome.MESA_PLATEAU;
                }
            }

            if (rainfall < 0.85)    {
                if (temperature < 0.58)  {
                    return Biome.SAVANNA;
                } else {
                    return Biome.OCEAN;
                }
            }

            //temperature is now > 0.7, rainfall > 0.85
            if (rainfall < 0.93)    {
                return Biome.SWAMP;
            } else {
                return Biome.JUNGLE;
            }
        }
    }
    
    public void recalculate() {
        this.map = new int[64 * 64];
        for(int i = 0; i < 64; ++i) {
            for(int j = 0; j < 64; ++j) {
                this.map[i + (j << 6)] = this.lookup(i / 63d, j / 63d);
            }
        }
    }

    public void addBiome(Biome biome) {
        this.biomes.put(Integer.valueOf(biome.getId()), biome);
    }

    public double getTemperature(double x, double z) {
        return (this.temperature.noise2D(x, z, true) + 1) / 2;
    }

    public double getRainfall(double x, double z) {
        return (this.rainfall.noise2D(x, z, true) + 1) / 2;
    }

    public Biome pickBiome(double x, double z) {
        int temperature = (int) (this.getTemperature(x, z) * 63);
        int rainfall = (int) (this.getRainfall(x, z) * 63);

        int biomeId = this.map[temperature + (rainfall << 6)];
        return this.biomes.containsKey(biomeId) ? this.biomes.get(biomeId) : this.fallback;
    }

}
