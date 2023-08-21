package com.example.rpgplatformer;

import java.io.Serializable;

public class PotionItem extends ItemComponent implements Serializable {

    private final int unitsOfHealth;

    public PotionItem(String name, int unitsOfHealth) {
        super(name);
        this.unitsOfHealth = unitsOfHealth;
    }

    @Override
    public void use() {
        RpgPlatformerUtils.getPlayer().getComponent(PlayerComponent.class).increaseHealth(unitsOfHealth);
        RpgPlatformerUtils.removeItemFromEquipment(this);
    }
}
