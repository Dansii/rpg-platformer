package com.example.rpgplatformer;

import com.almasb.fxgl.logging.Logger;

import java.io.Serializable;

public class KeyItem extends ItemComponent implements Serializable {

    Logger logger = Logger.get(PlayerComponent.class);

    public KeyItem(String name) {
        super(name);
    }

    @Override
    public void use() {
        if (RpgPlatformerUtils.getTouchedItem() != null) {
            if (RpgPlatformerUtils.getTouchedItem().getType() == EntityType.DOOR) {
                RpgPlatformerUtils.nextLevel();
                RpgPlatformerUtils.removeItemFromEquipment(this);
                logger.info("Player has moved to a new level");
            }
        }
    }
}
