package objects;

import pt.iscte.poo.utils.Point2D;

public class Wall extends GameObject implements Intransponivel, NonExplodable {

    private final boolean isTrap; // hidden trap / fake wall

    public Wall(Point2D position, boolean isTrap) {
        super(position);
        this.isTrap = isTrap;
    }

    @Override
    public String getName() {
        return "Wall";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    public boolean isTrap() {
        return isTrap;
    }
}
