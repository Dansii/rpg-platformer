package com.example.rpgplatformer;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.rpgplatformer.RpgPlatformerUtils.*;

public class RpgPlatformerApplication extends GameApplication {
    Logger logger = Logger.get(PlayerComponent.class);

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1400);
        gameSettings.setHeight(1050);
        gameSettings.setTitle("RpgPlatformer");
        gameSettings.setVersion("0.1");
        gameSettings.setDeveloperMenuEnabled(false);
        gameSettings.setEnabledMenuItems(EnumSet.allOf(MenuItem.class));
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new RpgPlatformerFactory());
        spawn("background");
        nextLevel();
        getPlayer().getComponent(PlayerComponent.class).setHealth(247);
    }

    @Override
    protected void onPreInit() {
        getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(DataFile data) {
                // save data to bundle
                var bundle = new Bundle("gameData");

//                int currentItem = geti("currentItem");
//                ItemComponent[] equipment = geto("equipment");
                int health = geti("health");
                int level = geti("level");
//                bundle.put("currentItem", currentItem);
//                bundle.put("equipment", equipment);
                bundle.put("health", health);
                bundle.put("level", level);


                data.putBundle(bundle);
            }

            @Override
            public void onLoad(DataFile data) {
                // load data from bundle
                var bundle = data.getBundle("gameData");
//                set("currentItem", bundle.get("currentItem"));
//                set("equipment", bundle.get("equipment"));

                set("health", bundle.get("health"));
                set("level", bundle.get("level"));
                FXGL.setLevelFromMap("level" + FXGL.geti("level")  + ".tmx");
                RpgPlatformerUtils.getPlayer().getComponent(PhysicsComponent.class).overwritePosition(new Point2D(700, 20));

            }
        });
    }

    // Processing keystrokes
    @Override
    protected void initInput() {
        onKeyDown(KeyCode.G, "Save", () -> getSaveLoadService().saveAndWriteTask("save1.sav").run());

        onKeyDown(KeyCode.L, "Load", () -> getSaveLoadService().readAndLoadTask("save1.sav").run());
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();
                getPlayer().getComponent(PlayerComponent.class).setTurnedLeft(true);
                getPlayer().getComponent(PlayerComponent.class).setTurnedRight(false);
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).right();
                getPlayer().getComponent(PlayerComponent.class).setTurnedLeft(false);
                getPlayer().getComponent(PlayerComponent.class).setTurnedRight(true);
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Top") {
            @Override
            protected void onActionBegin() {
                getPlayer().getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Select key") {
            @Override
            protected void onActionBegin() {
                ItemComponent[] equipment = FXGL.getWorldProperties().getObject("equipment");
                if (equipment[0] != null) {
                    FXGL.getWorldProperties().setValue("currentItem", 0);
                    logger.info("Player has selected a key in the inventory");
                }
            }
        }, KeyCode.DIGIT1);

        getInput().addAction(new UserAction("Select sword") {
            @Override
            protected void onActionBegin() {
                ItemComponent[] equipment = FXGL.getWorldProperties().getObject("equipment");
                if (equipment[1] != null) {
                    FXGL.getWorldProperties().setValue("currentItem", 1);
                    logger.info("Player has selected a sword in the inventory");
                }
            }
        }, KeyCode.DIGIT2);

        getInput().addAction(new UserAction("Select potion") {
            @Override
            protected void onActionBegin() {
                ItemComponent[] equipment = FXGL.getWorldProperties().getObject("equipment");
                if (equipment[2] != null) {
                    FXGL.getWorldProperties().setValue("currentItem", 2);
                    logger.info("Player has selected a potion in the inventory");
                }
            }
        }, KeyCode.DIGIT3);

        getInput().addAction(new UserAction("Select shield") {
            @Override
            protected void onActionBegin() {
                ItemComponent[] equipment = FXGL.getWorldProperties().getObject("equipment");
                if (equipment[3] != null) {
                    FXGL.getWorldProperties().setValue("currentItem", 3);
                    logger.info("Player has selected a shield in the inventory");
                }
            }
        }, KeyCode.DIGIT4);

        getInput().addAction(new UserAction("Select") {
            @Override
            protected void onActionBegin() {
                ItemComponent[] equipment = FXGL.getWorldProperties().getObject("equipment");
                if (equipment[4] != null) {
                    FXGL.getWorldProperties().setValue("currentItem", 4);
                }

            }
        }, KeyCode.DIGIT5);

        getInput().addAction(new UserAction("Take item") {
            @Override
            protected void onActionBegin() {
                var item = getTouchedItem();
                if (item != null) {
                    var position = getNumberOfItem(item);
                    if (position != null) {
                        ItemComponent[] equipment = FXGL.geto("equipment");
                        equipment[position] = getComponentOfItem(item);
                        item.removeFromWorld();
                        RpgPlatformerUtils
                                .getComponentOfItem(item)
                                .setInventoryRepresentation(Hud.renderImage(position));
                        logger.info("Player picked up the " + getComponentOfItem(item).getClass().getSimpleName());
                    }
                }
            }
        }, KeyCode.T);

        getInput().addAction(new UserAction("Use item") {
            @Override
            protected void onActionBegin() {
                ItemComponent[] equipment = FXGL.getWorldProperties().getObject("equipment");
                var currentItem = equipment[FXGL.getWorldProperties().getInt("currentItem")];
                if (currentItem != null) {
                    if (currentItem instanceof SwordItem) {
                        getPlayer().getComponent(PlayerComponent.class).setAttacking(true);
                    }
                    logger.info("Player used the " + currentItem.getClass().getSimpleName());
                    currentItem.use();
                }
            }
        }, KeyCode.U);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("currentItem", 0);
        vars.put("equipment", new ItemComponent[5]);
        vars.put("health", 0);
        vars.put("isPlayerAttacked", AttackedStatus.NONE);
        vars.put("level", 0);
    }

    @Override
    protected void initUI() {
        Hud.renderHealthBar();
        Hud.renderInventory();
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = FXGL.getPhysicsWorld();
        List<EntityType> itemTypes = RpgPlatformerUtils.getListItemEntityTypes();

        for (EntityType itemType: itemTypes) {
            physics.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, itemType) {
                List<Node> nodes;
                @Override
                protected void onCollisionBegin(Entity player, Entity item) {
                    nodes = Hud.renderHint(player.getWidth(), item.getY(), "T");
                }
                @Override
                protected void onCollisionEnd(Entity player, Entity enemy) {
                    for (Node node:nodes) {
                        FXGL.removeUINode(node);
                    }
                }
            });
        }

        physics.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.DOOR) {
            List<Node> nodes;
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                nodes = Hud.renderHint(player.getWidth(), door.getY(), "U");
            }

            @Override
            protected void onCollisionEnd(Entity a, Entity b) {
                for (Node node:nodes) {
                    FXGL.removeUINode(node);
                }
            }
        });

        physics.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                if (FXGL.geto("isPlayerAttacked") != AttackedStatus.PROTECTED) {
                    player.getComponent(PlayerComponent.class).decreaseHealth(10);
                    if (player.getX() >= enemy.getX()) {
                        getWorldProperties().setValue("isPlayerAttacked", AttackedStatus.LEFT);
                    } else {
                        getWorldProperties().setValue("isPlayerAttacked", AttackedStatus.RIGHT);
                    }
                }
            }
        });


    }


    public static void main(String[] args) {
        launch(args);
    }
}