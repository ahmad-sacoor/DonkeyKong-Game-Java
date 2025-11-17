package objects;

import java.util.List;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameObject implements ImageTile {

    private Point2D position;

    public GameObject(Point2D position) {
        this.position = position;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }


    @Override
    public int getLayer() {
        return 1; 
    }

    @Override
    public abstract String getName();
    
    
}
