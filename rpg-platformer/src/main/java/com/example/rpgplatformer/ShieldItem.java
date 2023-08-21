package com.example.rpgplatformer;

import com.almasb.fxgl.dsl.FXGL;
import javafx.util.Duration;

import java.io.Serializable;

public class ShieldItem extends ItemComponent implements Serializable {

    public ShieldItem(String name) {
        super(name);
    }

    @Override
    public void use() {
        FXGL.set("isPlayerAttacked", AttackedStatus.PROTECTED);
        RpgPlatformerUtils.getPlayer().setZIndex(20);
        FXGL.getGameTimer().runOnceAfter(() -> {
            FXGL.set("isPlayerAttacked", AttackedStatus.NONE);
            RpgPlatformerUtils.getPlayer().setZIndex(-20);
        } , Duration.seconds(3));
    }
}
