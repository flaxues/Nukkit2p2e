package cn.nukkit.level.generator.biome;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class NormalBiome extends Biome {
    @Override
    public int getColor() {
        return NormalBiome.grassColor;
    }
    
    public static int getGrassColor() {
        return grassColor;
    }
}
