package cn.nukkit.level.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.level.generator.populator.Populator;
import cn.nukkit.level.generator.populator.PopulatorElytraStone;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class End extends Generator {
	private ChunkManager level;
    /**
     * @var Random
     */
    private NukkitRandom nukkitRandom;
    private Random random;
    private double emptyHeight = 64;
    private double emptyAmplitude = 1;
    private double density = 0.5;
    private List<Populator> generationPopulators = new ArrayList<>();

    private long localSeed1;
    private long localSeed2;

    private Simplex noiseBase;

    public End() {
        this(new HashMap<>());
    }

    public End(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public int getId() {
        return Generator.TYPE_END;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_END;
    }

    @Override
    public String getName() {
        return "end";
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.noiseBase = new Simplex(this.nukkitRandom, 4, 1 / 4f, 1 / 64f);
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();
        
        //this.generationPopulators.add(new PopulatorEndFortress());
        this.generationPopulators.add(new PopulatorElytraStone());
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());
        
        double[][][] noise = Generator.getFastNoise3D(this.noiseBase, 16, 32, 16, 4, 1, 4, chunkX * 16, 0, chunkZ * 16);
        FullChunk chunk = this.level.getChunk(chunkX, chunkZ);

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                Biome biome = Biome.getBiome(Biome.DESERT);
                chunk.setBiomeId(x, z, Biome.DESERT);
                int biomecolor = biome.getColor();
                chunk.setBiomeColor(x, z, (biomecolor >> 16), (biomecolor >> 8) & 0xff, (biomecolor & 0xff));
                
                for (int y = 32; y < 64; ++y) {
                    double noiseValue = (Math.abs(this.emptyHeight - y) / this.emptyHeight) * this.emptyAmplitude - noise[x][z][y - 32];
                    noiseValue -= 1 - this.density;
                    if (noiseValue > 0) {
                        chunk.setBlockId(x, y, z, Block.END_STONE);
                    }
                }
            }
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
    	for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom);
        }
    }

    public Vector3 getSpawn() {
        return new Vector3(0, 128, 0);
    }
}
