package mobs.milk.pureentities.entity.monster.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import mobs.milk.pureentities.util.Utils;

import java.util.HashMap;

public class CaveSpider extends Spider {
	public static final int NETWORK_ID = 40;

	public CaveSpider(FullChunk chunk, CompoundTag nbt) {
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
		return 0.8f;
	}

	@Override
	public double getSpeed() {
		return 1.3;
	}

	@Override
	public void initEntity() {
		super.initEntity();

		this.setMaxHealth(12);
		this.setDamage(new int[] { 0, 2, 3, 3 });
	}

	@Override
	public void attackEntity(Entity player) {
		if (this.attackDelay > 10 && this.distanceSquared(player) < 1.32) {
			this.attackDelay = 0;
			HashMap<Integer, Float> damage = new HashMap<>();
			damage.put(EntityDamageEvent.MODIFIER_BASE, (float) this.getDamage());

			if (player instanceof Player) {
				float points = 0;
				for (Item i : ((Player) player).getInventory().getArmorContents()) {
					points += armorValues.getOrDefault(i.getId(), 0f);
				}

				damage.put(EntityDamageEvent.MODIFIER_ARMOR,
						(float) (damage.getOrDefault(EntityDamageEvent.MODIFIER_ARMOR, 0f) - Math
								.floor(damage.getOrDefault(EntityDamageEvent.MODIFIER_BASE, 1f) * points * 0.04)));
			}
			player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.CAUSE_ENTITY_ATTACK, damage));
		}
	}

	@Override
	public Item[] getDrops() {
		return this.lastDamageCause instanceof EntityDamageByEntityEvent
				? new Item[] { Item.get(Item.STRING, 0, Utils.rand(0, 2)) } : new Item[0];
	}

}
