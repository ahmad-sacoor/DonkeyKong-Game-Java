package objects;

import pt.iscte.poo.utils.Point2D;

public class Trap extends GameObject {

    public Trap(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Trap";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
