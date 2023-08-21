package com.example.rpgplatformer;

import java.io.Serializable;

public class SwordItem extends ItemComponent implements Serializable {

    private final int coolDown = 1;

    private final int damage;

    public SwordItem(String name, int damage) {
        super(name);
        this.damage = damage;
    }

    @Override
    public void use() {
        var enemy = RpgPlatformerUtils.getNearestEnemy(RpgPlatformerUtils.getPlayer());
        if (enemy != null) {
            var enemyComponent = enemy.getComponent(EnemyComponent.class);
            enemyComponent.takeDamage(damage);
        }
    }
}
