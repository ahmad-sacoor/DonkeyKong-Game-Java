package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;
import java.util.List;

public abstract class MovableGameObject extends GameObject {


    public MovableGameObject(Point2D position) {
        super(position); // Call the constructor of GameObject to initialize position
    }

    public void move(Direction d) {
        setPosition(getPosition().plus(d.asVector()));

    }
    
    
    public abstract void takeDamage(int damage);

    public abstract int getHealth();  // Abstract method to get health

    public abstract void attack(MovableGameObject defender, List<MovableGameObject> toRemove);

    
    public boolean isValidMove(Direction direction, List<GameObject> objectsAtCurrent, List<GameObject> objectsAtNew, List<GameObject> objectsBelow) {
        
    	if (objectsAtNew == null) return false;

        for (GameObject obj : objectsAtNew) {
            if (obj instanceof Intransponivel){
            	return false; 
            }
    	    if (obj instanceof Bomb && ((Bomb) obj).isTriggered()) {       
    	    	return false; // Bombs in a triggered state are impassable
    	    }
    	    
        }
        
        return true;
    }
}