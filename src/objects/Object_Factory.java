package objects;

import pt.iscte.poo.utils.Point2D;


public class Object_Factory {

	public static GameObject create(char type, Point2D position, int currentTick) {
        switch (type) {
            case 'W': return new Wall(position,false);
            case 'X': return new Wall(position,true);
            case 'H': return new JumpMan(position);
            case 'S': return new Stairs(position);
            case 's': return new Sword(position);
            case 'm': return new GoodMeat(position,currentTick);
            case 'P': return new Princess(position);
            case '0': return new DoorClosed(position);
            case 't': return new Trap(position);
            case 'G': return new DonkeyKong(position);
            case 'b': return new Bat(position); 
            case 'M': return new BadMeat(position);
            case 'B': return new Bomb(position); 
            case ' ': return new Floor(position); 

            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}