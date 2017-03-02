package mobs.de.kniffo80.mobplugin.entities.animal.swimming;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import mobs.de.kniffo80.mobplugin.entities.animal.WalkingAnimal;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class Squid extends WalkingAnimal {

    public static final int NETWORK_ID = 17;

    public Squid(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.95f;
    }

    @Override
    public float getHeight() {
        return 0.95f;
    }
    
    public float getLength() {
        return 0.95f;
    }

    @Override
    public float getEyeHeight() {
        return 0.7f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(10);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return false;
    }

    @Override
    public Item[] getDrops() {
        return new Item[0];
    }
    
    @Override
    public int getKillExperience () {
        return Utils.rand(1, 4); // gain 1-3 experience
    }
    

}