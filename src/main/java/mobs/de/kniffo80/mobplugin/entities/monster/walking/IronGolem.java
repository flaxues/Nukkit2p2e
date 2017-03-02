package mobs.de.kniffo80.mobplugin.entities.monster.walking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import mobs.de.kniffo80.mobplugin.MobPlugin;
import mobs.de.kniffo80.mobplugin.entities.monster.WalkingMonster;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class IronGolem extends WalkingMonster {

    public static final int NETWORK_ID = 20;

    public IronGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setFriendly(true);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 2.7f;
    }

    @Override
    public double getSpeed() {
        return 0.8;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(100);
        super.initEntity();

        this.setDamage(new int[] { 0, 21, 21, 21 });
        this.setMinDamage(new int[] { 0, 7, 7, 7 });
    }

    public void attackEntity(Entity player) {
        if (this.attackDelay > 10 && this.distanceSquared(player) < 4) {
            this.attackDelay = 0;
            HashMap<Integer, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.MODIFIER_BASE, (float) this.getDamage());

            if (player instanceof Player) {
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += MobPlugin.armorValues.getOrDefault(i.getId(), 0f);
                }
                damage.put(EntityDamageEvent.MODIFIER_ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.MODIFIER_ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.MODIFIER_BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.CAUSE_ENTITY_ATTACK, damage));
            player.addMotion(0, 0.3, 0);
        }
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        return !(creature instanceof Player) && creature.isAlive() && distance <= 60;
    }
    
    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int ironIngots = Utils.rand(3, 6); // drops 3-5 iron ingots
            int poppies = Utils.rand(0, 3); // drops 0-2 poppies
            for (int i=0; i < ironIngots; i++) {
                drops.add(Item.get(Item.IRON_INGOT, 0, 1));
            }
            for (int i=0; i < poppies; i++) {
                drops.add(Item.get(Item.POPPY, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }
    
    @Override
    public int getKillExperience () {
        return 0; // gain 0 experience
    }


}
