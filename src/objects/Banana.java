package objects;

import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Banana extends MovableGameObject implements CanOnlyAttackJumpMan {

    private boolean destroyed;

    public Banana(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Banana";
    }

    @Override
    public int getLayer() {
        return 3;
    }

    @Override
    public void move(Direction d) {
        // Uses the base movement logic
        super.move(d);
    }

    @Override
    public boolean isValidMove(Direction direction,
                               List<GameObject> objectsAtCurrent,
                               List<GameObject> objectsAtNew,
                               List<GameObject> objectsBelow) {
        // Reuse generic rules from MovableGameObject
        if (!super.isValidMove(direction, objectsAtCurrent, objectsAtNew, objectsBelow)) {
            return false;
        }

        // You can add any banana-specific rule here if needed
        return true;
    }

    @Override
    public void attack(MovableGameObject target, List<MovableGameObject> toRemove) {
        if (target instanceof JumpMan) {
            JumpMan jumpman = (JumpMan) target;
            jumpman.takeDamage(5);
            System.out.println("Banana hit JumpMan and dealt 5 damage!");
            toRemove.add(this);
            destroyed = true;
        }
    }

    @Override
    public void takeDamage(int damage) {
        System.out.println("Banana took damage and is now destroyed!");
        destroyed = true;
    }

    @Override
    public int getHealth() {
        return destroyed ? 0 : 1;
    }
}
