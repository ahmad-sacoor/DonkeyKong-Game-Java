package objects;

import pt.iscte.poo.utils.Point2D;

public class DoorClosed extends GameObject implements NonExplodable {

    public DoorClosed(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "DoorClosed";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
