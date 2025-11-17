package objects;

import pt.iscte.poo.utils.Point2D;

public class Princess extends GameObject {

    public Princess(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Princess";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
