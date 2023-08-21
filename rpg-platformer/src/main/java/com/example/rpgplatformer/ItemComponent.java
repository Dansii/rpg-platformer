package com.example.rpgplatformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.Node;

/**
 * A common component for all inventory items
 */
public abstract class ItemComponent extends Component {

    private final String name;
    private Node inventoryRepresentation;
    private final Texture texture;

    public ItemComponent(String name) {
        this.name = name;
        texture = FXGL.texture("items/" + name + ".png");
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    /**
     * The method is called when using an item
     */
    public abstract void use();

    public Node getInventoryRepresentation() {
        return inventoryRepresentation;
    }

    public void setInventoryRepresentation(Node inventoryRepresentation) {
        this.inventoryRepresentation = inventoryRepresentation;
    }
}
