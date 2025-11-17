package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import java.util.List;
import pt.iscte.poo.gui.ImageGUI;

import java.util.ArrayList;

public class JumpMan extends MovableGameObject implements Intransponivel, NonExplodable {

    private int lives = 3;
    private List<Pickable> inventory = new ArrayList<>();

    private int health;
    private int attackStrength;
    private final int MAX_HEALTH = 100;
    private boolean hasBomb;

    public JumpMan(Point2D initialPosition) {
        super(initialPosition);
        this.health = MAX_HEALTH;
        this.attackStrength = 10;
        this.hasBomb = false;
    }

    public void increaseAttackStrength(int value) {
        this.attackStrength += value;
        System.out.println("Jump-Man's attack strength increased to " + this.attackStrength);
    }

    public void restoreHealth() {
        this.health = MAX_HEALTH;
        System.out.println("Health restored to maximum!");
    }

    @Override
    public boolean isValidMove(Direction direction,
                               List<GameObject> objectsAtCurrent,
                               List<GameObject> objectsAtNew,
                               List<GameObject> objectsBelow) {

        if (!super.isValidMove(direction, objectsAtCurrent, objectsAtNew, objectsBelow))
            return false;

        // Block UP movement when standing between two floors
        if (direction == Direction.UP) {
            if (objectsAtCurrent.get(1) instanceof Floor &&
                objectsBelow.get(1) instanceof Floor) {
                return false;
            }
        }

        return true;
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        System.out.println("Jump-Man took " + damage + " damage. Health now: " + this.health);

        if (this.health <= 0) {
            System.out.println("Jump-Man has been defeated!");
        }
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

    public void die() {
        this.health = 0;
    }

    @Override
    public String getName() {
        return "JumpMan";
    }

    @Override
    public int getLayer() {
        return 2;
    }


    public void addBomb() {
        this.hasBomb = true;
    }

    public boolean getHasBomb() {
        return hasBomb;
    }

    public void removeBomb() {
        this.hasBomb = false;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives--;
        restoreHealth();

        // Remove sword effect
        for (Pickable item : inventory) {
            if (item instanceof Sword) {
                increaseAttackStrength(-15);
                System.out.println("Sword effect removed. Attack strength: " + this.getAttackStrength());
            }
        }

        removeBomb();
        inventory.clear();
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public void addToInventory(Pickable item) {
        inventory.add(item);
    }

    public List<Pickable> getInventory() {
        return inventory;
    }

    public boolean hasItem(Pickable item) {
        return inventory.contains(item);
    }

    public void resetLives() {
        lives = 3;
    }

    public void clearInventory() {
        inventory.clear();
        removeBomb();
        System.out.println("JumpMan's inventory has been cleared.");
    }
}
