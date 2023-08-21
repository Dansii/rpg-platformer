package com.example.rpgplatformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

/**
 * Game object factory.
 */
public class RpgPlatformerFactory implements EntityFactory {

    @Spawns("ground")
    public Entity spawnGround(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.GROUND)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("key")
    public Entity spawnKeyItem(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.KEY)
                .zIndex(-30)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new KeyItem(data.get("name")))
                .collidable()
                .build();
    }
    @Spawns("potion")
    public Entity spawnPotionItem(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.POTION)
                .zIndex(-30)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PotionItem(data.get("name"), data.get("unitsOfHealth")))
                .collidable()
                .build();
    }
    @Spawns("shield")
    public Entity spawnShieldItem(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.SHIELD)
                .zIndex(-30)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new ShieldItem(data.get("name")))
                .collidable()
                .build();
    }
    @Spawns("sword")
    public Entity spawnSwordItem(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.SWORD)
                .zIndex(-30)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new SwordItem(data.get("name"), data.get("damage")))
                .collidable()
                .build();
    }


    @Spawns("door")
    public Entity spawnDoor(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.DOOR)
                .zIndex(-30)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .collidable()
                .build();
    }

    @Spawns("enemy")
    public Entity spawnGhost(SpawnData data) {
        return FXGL
                .entityBuilder(data)
                .type(EntityType.ENEMY)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new EnemyComponent(data.get("health"), data.get("patrolEndX"), data.get("movingRight")))
                .collidable()
                .build();
    }

    @Spawns("player")
    public Entity spawnPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(2, 65), BoundingShape.box(66, 5)));
        physics.setFixtureDef(new FixtureDef().friction(0f).density(0.1f));
        BodyDef bd = new BodyDef();
        bd.setFixedRotation(true);
        bd.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bd);

        return FXGL
                .entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(physics)
                .with(new PlayerComponent())
                .with(new IrremovableComponent())
                .collidable()
                .build();
    }


    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .view(new ScrollingBackgroundView(texture("background/background.png").getImage(), getAppWidth(), getAppHeight()))
                .zIndex(-100)
                .with(new IrremovableComponent())
                .build();
    }
}
