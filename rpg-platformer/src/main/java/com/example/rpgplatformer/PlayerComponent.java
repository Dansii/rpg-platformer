package com.example.rpgplatformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerComponent extends Component {

    Logger logger = Logger.get(PlayerComponent.class);
    private PhysicsComponent physicsComponent;
    private final AnimatedTexture texture;
    private final AnimationChannel idle;
    private final AnimationChannel walk;
    private final AnimationChannel attack;
    private boolean turnedRight;
    private boolean turnedLeft;
    private boolean isAttacking;
    private final Integer SPEED = 150;
    private int jumps = 2;
    private double lastY = 0;
    private double currentY = 0;

    public PlayerComponent() {
        idle = new AnimationChannel(FXGL.image("player-idle.png"),Duration.seconds(1), 12);
        attack = new AnimationChannel(FXGL.image("player-attack.png"),Duration.seconds(0.5), 12);
        walk = new AnimationChannel(FXGL.image("player-walk.png"), Duration.seconds(1), 18);

        texture = new AnimatedTexture(idle);
        logger.info("Spawn player");
    }

    public boolean isTurnedRight() {
        return turnedRight;
    }

    public void setTurnedRight(boolean turnedRight) {
        this.turnedRight = turnedRight;
    }

    public boolean isTurnedLeft() {
        return turnedLeft;
    }

    public void setTurnedLeft(boolean turnedLeft) {
        this.turnedLeft = turnedLeft;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }


    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(idle);

        // Sensor for calculating the height of the fall and updating jumps
        physicsComponent.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (old) {
                lastY = this.getEntity().getY();
            }
            if (!old) {
                currentY = this.getEntity().getY();
                fell(currentY - lastY);
            }
            if (isOnGround) {
                jumps = 2;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (this.getEntity().getY() > 1050) {
            decreaseHealth(10);
            RpgPlatformerUtils.getPlayer().getComponent(PhysicsComponent.class).overwritePosition(new Point2D(70, 770));
        }
        double durationOfEnemyAttack = 0.4;
        // The player is thrown away from the enemy depending on the side of the attack
        if (FXGL.geto("isPlayerAttacked") == AttackedStatus.LEFT) {
            physicsComponent.setVelocityX(SPEED);
            physicsComponent.setVelocityY(-30);
            FXGL.getGameTimer().runOnceAfter(() -> {
                FXGL.set("isPlayerAttacked", AttackedStatus.NONE);
                this.stop();
            }, Duration.seconds(durationOfEnemyAttack));
        } else if (FXGL.geto("isPlayerAttacked") == AttackedStatus.RIGHT) {
            physicsComponent.setVelocityX(-SPEED);
            physicsComponent.setVelocityY(-30);
            FXGL.getGameTimer().runOnceAfter(() -> {
                FXGL.set("isPlayerAttacked", AttackedStatus.NONE);
                this.stop();
            }, Duration.seconds(durationOfEnemyAttack));
        } else if (isAttacking) {
            // Attack Animation
            RpgPlatformerUtils.getPlayer().getComponent(PlayerComponent.class).attack();
            FXGL.getGameTimer().runOnceAfter(() -> {
                texture.loopAnimationChannel(idle);
                setAttacking(false);
            }, Duration.seconds(0.5));
        }
    }

    public void left() {
        if (!isAttacking) {
            physicsComponent.setVelocityX(-SPEED);
            getEntity().setScaleX(-1);
            if (texture.getAnimationChannel() != walk) {
                texture.loopAnimationChannel(walk);
            }
        }
    }

    public void right() {
        if (!isAttacking) {
            physicsComponent.setVelocityX(SPEED);
            getEntity().setScaleX(1);
            if (texture.getAnimationChannel() != walk) {
                texture.loopAnimationChannel(walk);
            }
        }
    }
    public void attack() {
        physicsComponent.setVelocityX(0);
        if (texture.getAnimationChannel() != attack) {
            texture.loopAnimationChannel(attack);
        }
    }

    public void jump() {
        if (jumps == 0) {
            return;
        }
        physicsComponent.setVelocityY(-SPEED * 2);
        jumps--;
    }

    public void stop() {
        if (!isAttacking) {
            physicsComponent.setVelocityX(0);
            if (texture.getAnimationChannel() != idle){
                texture.loopAnimationChannel(idle);
            }
        }
    }

    /**
     * The method deals damage to the player when falling high
     * @param height of drop
     */
    public void fell(double height) {
        if (height > 420) {
            int damage = (int) Math.ceil((height - 420) * 0.3);
            decreaseHealth(damage);
            logger.info("Player fell and received " + damage + " points of damage");
        }
    }

    public void setHealth(int health) {
        FXGL.getWorldProperties().setValue("health", health);
    }

    public IntegerProperty getHealth() {
        return FXGL.getip("health");
    }

    public void decreaseHealth(int units) {
        logger.info("Player received " + units + " units of damage");
        FXGL.getWorldProperties().setValue("health", getHealth().getValue() - units);
        logger.info("Player health is " + getHealth().intValue());
        if (getHealth().intValue() < 0) {
            FXGL.showMessage("You died");
            logger.info("Player died");
        }
    }

    public void increaseHealth(int unitOfHealth) {
        logger.info("Player used a potion to restore health");
        int health = Math.min((getHealth().getValue() + unitOfHealth), 100);
        FXGL.getWorldProperties().setValue("health", health);
        logger.info("Player's health is restored to " + getHealth().intValue() + " units");
    }


}
