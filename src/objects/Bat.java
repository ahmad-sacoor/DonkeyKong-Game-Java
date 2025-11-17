package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

import java.util.List;

public class Bat extends MovableGameObject implements Intransponivel, RandomMovement, CanOnlyAttackJumpMan {

    private int health = 10;
    private int attackStrength = 5;

    public Bat(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Bat";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public boolean isValidMove(Direction direction,
                               List<GameObject> objectsAtCurrent,
                               List<GameObject> objectsAtNew,
                               List<GameObject> objectsBelow) {

        // Bat can go down stairs
        if (direction == Direction.DOWN &&
            objectsAtNew != null &&
            objectsAtNew.size() > 1 &&
            objectsAtNew.get(1) instanceof Stairs) {
            return true;
        }

        if (!super.isValidMove(direction, objectsAtCurrent, objectsAtNew, objectsBelow)) {
            return false;
        }

        // Bats only move horizontally unless using stairs down
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return false;
        }

        return true;
    }

    @Override
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        System.out.println("Bat took " + damage + " damage. Health now: " + this.health);
        if (this.health <= 0) {
            System.out.println("Bat has been defeated!");
        }
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void attack(MovableGameObject defender, List<MovableGameObject> toRemove) {
        int damage = this.attackStrength;
        defender.takeDamage(damage);
        System.out.println(this.getName() + " attacked " + defender.getName() + " for " + damage + " damage!");
        // Bat sacrifices itself after attacking once
        health = 0;

        if (defender.getHealth() <= 0) {
            System.out.println(defender.getName() + " has been defeated!");
            toRemove.add(defender);
        }
    }
}
