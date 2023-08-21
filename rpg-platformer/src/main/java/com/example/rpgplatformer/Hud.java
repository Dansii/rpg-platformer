package com.example.rpgplatformer;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders interface elements
 */
public class Hud {
    static final int Y = 990;
    static final int hintWidth = 30;
    static final int hintHeight = 30;

    /**
     * draws a strip of lives
     */
    public static void renderHealthBar() {
        var x = 20;
        var y = 20;

        Rectangle healthBox = new Rectangle();
        healthBox.setX(x);
        healthBox.setY(y);
        healthBox.setWidth(200);
        healthBox.setHeight(8);
        healthBox.setOpacity(0.3);
        healthBox.setFill(Color.RED);

        Rectangle healthBar = new Rectangle();
        healthBar.setX(x);
        healthBar.setY(y);
        healthBar.widthProperty().bind(FXGL.getip("health").multiply(2));
        healthBar.setHeight(8);
        healthBar.setFill(Color.RED);

        FXGL.addUINode(healthBox);
        FXGL.addUINode(healthBar);
    }

    /**
     * draw inventory
     */
    public static void renderInventory() {
        for (int i = 0; i < 5; i++) {
            renderInventoryCell(i);
        }

        renderSelectedInventoryCell();
    }

    /**
     * Draws a border around the selected item in the inventory
     */
    public static void renderSelectedInventoryCell() {
        Rectangle r = new Rectangle();
        r.xProperty().bind(FXGL.getip("currentItem").multiply(50).add(575));
        r.setY(Y);
        r.setWidth(50);
        r.setHeight(50);
        r.setStrokeWidth(4);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.RED);
        FXGL.addUINode(r);
    }

    /**
     * Draws an inventory cell
     * @param number of cell
     */
    private static void renderInventoryCell(int number) {
        var x = 575 + number * 50;
        Rectangle r = new Rectangle();
        r.setX(x);
        r.setY(Y);
        r.setWidth(50);
        r.setHeight(50);
        r.setStrokeWidth(4);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.BLACK);

        Rectangle r2 = new Rectangle();
        r2.setX(x);
        r2.setY(Y);
        r2.setOpacity(0.3);
        r2.setWidth(50);
        r2.setHeight(50);
        r2.setFill(Color.BLACK);

        Label cellNumber = new Label(String.valueOf(number + 1));
        cellNumber.setTextFill(Color.WHITE);
        cellNumber.setFont(Font.font(12));
        FXGL.addUINode(cellNumber, x + 3, Y + 3);

        FXGL.addUINode(r);
        FXGL.addUINode(r2);

    }

    /**
     * Draws the selected item in the inventory cell
     * @param number of item
     * @return UI Node element
     */
    public static Node renderImage(int number) {
        var x = 575 + number * 50;
        var imageName = getImageName(number);
        ImageView image = new ImageView("assets/textures/inventory/" + imageName);
        image.setX(x);
        image.setY(Y);
        image.setFitWidth(50);
        image.setFitHeight(50);
        FXGL.addUINode(image);
        return image;
    }

    /**
     * Draws the hint interface
     * @param width of item
     * @param y the position of the item on the Y axis
     * @param text Hint text
     * @return UI Node elements
     */
    public static List<Node> renderHint(double width, double y, String text) {
        double x = (FXGL.getAppWidth() + (width - hintWidth)) / 2;
        List<Node> nodes = new ArrayList<>();
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y - (width - hintWidth));
        rectangle.setWidth(hintWidth);
        rectangle.setHeight(hintHeight);
        rectangle.setOpacity(0.5);
        rectangle.setStroke(Color.ORANGE);
        rectangle.setStrokeWidth(3);
        rectangle.setFill(Color.BLACK);

        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(15));

        FXGL.addUINode(rectangle);
        FXGL.addUINode(label,  rectangle.getX() + 10 , rectangle.getY() + 5);

        nodes.add(rectangle);
        nodes.add(label);
        return nodes;
    }

    /**
     * Returns the name of the image to be drawn in the inventory
     * @param number of item
     * @return file name
     */
    private static String getImageName(int number) {
        return switch (number) {
            case 0 -> "key.png";
            case 1 -> "sword.png";
            case 2-> "potion.png";
            case 3 -> "shield.png";
            case 4 -> "key2.png";
            default -> null;
        };
    }
}
