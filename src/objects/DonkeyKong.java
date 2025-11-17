package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.gui.ImageGUI;

import java.util.List;

public class DonkeyKong extends MovableGameObject implements Intransponivel, RandomMovement, CanOnlyAttackJumpMan {

    private int health = 100;
    private int attackStrength = 10;
    private int cooldown = 5;
    private int lastBananaTick = -cooldown;

    public DonkeyKong(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "DonkeyKong";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean isValidMove(Direction direction,
                               List<GameObject> objectsAtCurrent,
                               List<GameObject> objectsAtNew,
                               List<GameObject> objectsBelow) {

        if (!super.isValidMove(direction, objectsAtCurrent, objectsAtNew, objectsBelow)) {
            return false;
        }

        // DonkeyKong moves only horizontally
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return false;
        }

        return true;
    }

    @Override
    public void attack(MovableGameObject defender, List<MovableGameObject> toRemove) {
        int damage = this.attackStrength;
        defender.takeDamage(damage);
        System.out.println(this.getName() + " attacked " + defender.getName() + " for " + damage + " damage!");

        if (defender.getHealth() <= 0) {
            System.out.println(defender.getName() + " has been defeated!");
            toRemove.add(defender);
        }
    }

    public void increaseDifficulty() {
        this.cooldown = Math.max(1, this.cooldown - 1);
    }

    public boolean isReadyToThrowBananas(int currentTick) {
        return (currentTick - lastBananaTick) >= cooldown;
    }

    public Banana throwBanana(int currentTick, Point2D position) {
        lastBananaTick = currentTick;
        return new Banana(position);
    }

    @Override
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        System.out.println("Donkey-Kong took " + damage + " damage. Health now: " + this.health);
        if (this.health <= 0) {
            System.out.println("Donkey-Kong has been defeated!");
        }
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    @Override
    public int getHealth() {
        return health;
    }

    public int getCooldown() {
        return cooldown;
    }
}
