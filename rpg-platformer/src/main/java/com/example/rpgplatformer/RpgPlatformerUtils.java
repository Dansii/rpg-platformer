package com.example.rpgplatformer;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;


public class RpgPlatformerUtils {

    private static final int MAX_LEVEL = 3;

    /**
     * Returns the main player
     * @return player
     */
    public static Entity getPlayer() {
        return getSingleton(EntityType.PLAYER);
    }

    /**
     * The method changes the level of the game
     */
    public static void nextLevel() {
        if (FXGL.geti("level") == MAX_LEVEL) {
            FXGL.showMessage("You finished game");
            return;
        }

        FXGL.inc("level", +1);


        FXGL.setLevelFromMap("level" + FXGL.geti("level")  + ".tmx");
        RpgPlatformerUtils.getPlayer().getComponent(PhysicsComponent.class).overwritePosition(new Point2D(70, 770));
        RpgPlatformerUtils.getPlayer().setZIndex(Integer.MAX_VALUE);
        setUpCamera();
    }

    /**
     * The method returns an entity in collision with a player.
     * Except the enemy entity.
     * @return Entity in collision with a player
     */
    public static Entity getTouchedItem() {
        return getTouchedEntities()
                .stream()
                .filter((entity) -> entity.getType() != EntityType.ENEMY)
                .findFirst()
                .orElse(null);
    }

    /**
     * Return number of item
     * @param item Entity of the item
     * @return number of item
     */
    public static Integer getNumberOfItem(Entity item) {
        return switch ((EntityType) item.getType()) {
            case KEY -> 0;
            case SWORD -> 1;
            case POTION -> 2;
            case SHIELD -> 3;
            default -> null;
        };
    }

    /**
     * Return all inventory items type in a game
     * @return list entity type of items
     */
    public static List<EntityType> getListItemEntityTypes() {
        List<EntityType> itemTypes = new ArrayList<>();
        itemTypes.add(EntityType.KEY);
        itemTypes.add(EntityType.SWORD);
        itemTypes.add(EntityType.POTION);
        itemTypes.add(EntityType.SHIELD);
        return itemTypes;
    }

    /**
     * Return component of the entity item
     * @param entity item
     * @return component item
     */
    public static ItemComponent getComponentOfItem(Entity entity) {
        return switch ((EntityType) entity.getType()) {
            case KEY -> entity.getComponent(KeyItem.class);
            case SWORD -> entity.getComponent(SwordItem.class);
            case POTION -> entity.getComponent(PotionItem.class);
            case SHIELD -> entity.getComponent(ShieldItem.class);
            default -> null;
        };
    }

    /**
     * The method returns an enemy that falls into the player's attack zone
     * @param player entity of player
     * @return entity of attacked enemy
     */
    public static Entity getNearestEnemy(Entity player) {
        var enemy = FXGL.getGameWorld().getClosestEntity(player, (entity) -> {
            if (entity.getType() != EntityType.ENEMY) {
                return false;
            }
            var angularСoefficient = ((entity.getY() - player.getY()) / (entity.getX() - player.getX())); //TODO: угловой коэффициент
            var distance = Math.sqrt(Math.pow(player.getX() - entity.getX(),2) + Math.pow(player.getY() - entity.getY(),2));
//            System.out.println("distance is " + distance);
//            System.out.println("enemy position (x,y) " + entity.getX() + " " + entity.getY());
//            System.out.println("player position (x,y) " + player.getX() + " " + player.getY());
//            System.out.println("angularСoefficient " + angularСoefficient);
//            System.out.println("distance " + distance);
            if (player.getComponent(PlayerComponent.class).isTurnedRight()) {
                return (angularСoefficient >= -1 && angularСoefficient <= 1 && distance < 100) && (entity.getX() > player.getX());
            } else if (player.getComponent(PlayerComponent.class).isTurnedLeft()){
                return (angularСoefficient >= -1 && angularСoefficient <= 1 && distance < 100) && (entity.getX() < player.getX());
            }
            return false;
        });
//        System.out.println(enemy);
        return enemy.orElse(null);
    }

    /**
     * Removes an item from the inventory interface
     * @param item Item to delete
     */
    public static void removeItemFromEquipment(ItemComponent item) {
        ItemComponent[] equipment = FXGL.geto("equipment");
        for (int i = 0; i < equipment.length; i++) {
            if (equipment[i] != null) {
                if (equipment[i] == item) {
                    FXGL.removeUINode(equipment[i].getInventoryRepresentation());
                    equipment[i] = null;
                }
            }
        }
    }

    /**
     * Adjusts the camera fixed behind the player
     */
    private static void setUpCamera() {
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        viewport.bindToEntity(getPlayer(), getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static List<Entity> getTouchedEntities() {
        return FXGL.getGameWorld().getCollidingEntities(getPlayer());
    }

    private static Entity getSingleton(EntityType type) {
        return FXGL.getGameWorld().getSingleton(type);
    }


}
