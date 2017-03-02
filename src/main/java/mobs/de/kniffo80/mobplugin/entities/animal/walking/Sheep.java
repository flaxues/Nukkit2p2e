package mobs.de.kniffo80.mobplugin.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import mobs.de.kniffo80.mobplugin.entities.animal.WalkingAnimal;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class Sheep extends WalkingAnimal {

    public static final int NETWORK_ID = 13;

    public Sheep(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 1.3f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(8);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.SEEDS && distance <= 49;
        }
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