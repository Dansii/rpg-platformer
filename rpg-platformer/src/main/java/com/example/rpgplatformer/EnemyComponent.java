package com.example.rpgplatformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;


public class EnemyComponent extends Component {

    Logger logger = Logger.get(PlayerComponent.class);
    private int health;
    private final int patrolEndX;
    private final Duration duration;
    private double speed;
    private final AnimatedTexture texture;
    private final AnimationChannel animationWalk;
    private boolean movingRight;

    public EnemyComponent(int health, int patrolEndX, boolean movingRight) {
        this.patrolEndX = patrolEndX;
        this.health = health;
        this.duration = Duration.seconds(RpgPlatformerUtils.getRandomNumber(2, 5));
        this.movingRight = movingRight;

        animationWalk = new AnimationChannel(FXGL.image("enemy-walk.png"), Duration.seconds(1), 12);
        texture = new AnimatedTexture(animationWalk);
    }

    @Override
    public void onAdded() {
        double distance = Math.negateExact((int) (patrolEndX - entity.getX()));
        this.speed = distance / duration.toSeconds();

        FXGL.getGameTimer().runAtInterval(() -> movingRight = !movingRight, duration);

        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animationWalk);
    }

    @Override
    public void onUpdate(double tpf) {
        if (movingRight) {
            entity.translateX(speed * tpf);
            getEntity().setScaleX(-1);
        } else {
            entity.translateX(-speed * tpf);
            getEntity().setScaleX(1);
        }
    }

    /**
     * The method is called when an enemy takes damage.
     * @param damage amount of damage
     */
    public void takeDamage(int damage) {
        logger.info("Player inflicted " + damage + " points of damage to the enemy");
        health -= damage;

        if (isDead()) {
            logger.info("Player killed the enemy");
            this.getEntity().removeFromWorld();
        } else {
            this.getEntity().setOpacity(0.5);
            FXGL.getGameTimer().runOnceAfter(() -> {
                if (this.getEntity() != null) {
                    this.getEntity().setOpacity(1);
                }
            }, Duration.seconds(0.24));
        }
    }

    /**
     * The method checks if the enemy is dead.
     * @return true if enemy is dead, false if alive
     */
    public boolean isDead() {
        return health <= 0;
    }
}
