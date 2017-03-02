package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.potion.Effect;

import java.util.Map;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {

    private final Entity damager;

    private float knockBack;

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause, float damage) {
        this(damager, entity, cause, damage, 0.4f);
    }

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause, Map<Integer, Float> damage) {
        this(damager, entity, cause, damage, 0.4f);
    }

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause, float damage, float knockBack) {
        super(entity, cause, damage);
        this.damager = damager;
        this.knockBack = knockBack;
        this.addAttackerModifiers(damager);
    }

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause, Map<Integer, Float> damage, float knockBack) {
        super(entity, cause, damage);
        this.damager = damager;
        this.knockBack = knockBack;
        this.addAttackerModifiers(damager);
    }

    protected void addAttackerModifiers(Entity damager) {
        if (damager.hasEffect(Effect.STRENGTH)) {
            this.setDamage(damager.getEffect(Effect.STRENGTH).getAmplifier(), MODIFIER_STRENGTH);
        }

        if (damager.hasEffect(Effect.WEAKNESS)) {
            this.setDamage(-(float) (damager.getEffect(Effect.WEAKNESS).getAmplifier()), MODIFIER_WEAKNESS);
        }
    }

    public Entity getDamager() {
        return damager;
    }

    public float getKnockBack() {
        return knockBack;
    }

    public void setKnockBack(float knockBack) {
        this.knockBack = knockBack;
    }
}
