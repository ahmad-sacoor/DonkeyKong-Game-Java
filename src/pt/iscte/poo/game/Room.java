package pt.iscte.poo.game;

import objects.JumpMan;
import objects.Floor;
import objects.GameObject;
import objects.Object_Factory;
import objects.DonkeyKong;
import objects.GoodMeat;
import objects.DoorClosed;
import objects.BadMeat;
import objects.Princess;
import objects.Wall;
import objects.Bomb;
import objects.Trap;
import objects.Banana;
import objects.Bat;


import objects.MovableGameObject;
import objects.RandomMovement;
import objects.CanOnlyAttackJumpMan;


import objects.Pickable;
import objects.NonExplodable;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Room {

    private Map<Point2D, List<GameObject>> objectsMap; // Map to store multiple objects per position
    private JumpMan jumpman;
    Point2D jumpmanStartPosition;
    
	//these bottom two lists were used because its nature is such that it depends on time
    private List<Bomb> bombsThatAreTriggered;
    private List<Banana> bananasThrown;

    public Room(String filename,int currentTick) {
    	objectsMap = new HashMap<>();
    	
    	bombsThatAreTriggered = new ArrayList<>();
    	bananasThrown= new ArrayList<>();
        loadFromFile(filename,currentTick);  
    }


    
    public JumpMan getJumpMan() {
        return jumpman;
    }
    

    private void loadFromFile(String filename, int currentTick) {

    	ImageGUI.getInstance().clearImages();
        objectsMap.clear();
        bombsThatAreTriggered.clear();

        File file = new File(filename);
        if (!file.exists()) {
            // Prompt user for a new file name if file does not exist
            System.out.println("File does not exist. Please provide a valid file name:");
            Scanner sc = new Scanner(System.in);
            filename = sc.nextLine(); // Read new file name from user
            loadFromFile(filename, currentTick); // Retry loading with new file name
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            int y = 0; // Row index

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                if (line.startsWith("#")) {
                    continue; 
                }
                
//                if (line.isEmpty()) {
//                    // If a line is completely empty, we treat it as a fatal error and abort the game
//                    System.err.println("Error: An entire line is missing from the file. Aborting the game.");
//                    ImageGUI.getInstance().dispose(); // Dispose of the GUI

//                }
//

                for (int x = 0; x < line.length(); x++) {
                    char tileChar = line.charAt(x); 
                    Point2D position = new Point2D(x, y);

                    if (!objectsMap.containsKey(position)) {
                        objectsMap.put(position, new ArrayList<>());
                    }

                    // Add Floor object first
                    GameObject floor = new Floor(position);
                    objectsMap.get(position).add(floor);
                    ImageGUI.getInstance().addImage(floor); 

                    GameObject obj = Object_Factory.create(tileChar, position, currentTick);

                    if (obj != null) {
                        objectsMap.get(position).add(obj);
                        ImageGUI.getInstance().addImage(obj); 

                        if (obj instanceof JumpMan) {
                            jumpman = (JumpMan) obj;   
                            jumpmanStartPosition = jumpman.getPosition();
                        }



                        if (obj instanceof GoodMeat) {
                            ((GoodMeat) obj).setSpawnedAtTick(currentTick);
                        }
                        
                    } else {
                        GameObject fallbackFloor = new Floor(position);
                        objectsMap.get(position).add(fallbackFloor);
                        ImageGUI.getInstance().addImage(fallbackFloor); 
                        System.err.println("Warning: Unknown character '" + tileChar + "' at position (" + x + "," + y + "). Replaced with floor.");
                    }
                }

                y++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error opening the file.");
        }
    }

    
//    public void display(){
//        ImageGUI.getInstance().setStatusMessage("Lives: " + jumpman.getLives() + ", Health: " + jumpman.getHealth() + ", Strength: " + jumpman.getAttackStrength());
//
//    }
    
    
    public void moveObject(MovableGameObject obj, Direction direction) {
        if (obj == null) return;

        Point2D currentPosition = obj.getPosition();
        Point2D newPosition = currentPosition.plus(direction.asVector());

        List<GameObject> objectsAtNew = objectsMap.get(newPosition);
        List<GameObject> objectsAtCurrent = objectsMap.get(currentPosition);
        List<GameObject> objectsBelow = objectsMap.get(currentPosition.plus(Direction.DOWN.asVector()));

        if(obj instanceof JumpMan){
        	checkForHiddenTraps(objectsBelow);
        }
        checkCombat(obj, newPosition); 

        if (obj.isValidMove(direction, objectsAtCurrent, objectsAtNew, objectsBelow)) {

            if (objectsAtNew != null) {
                for (GameObject gameObj : objectsAtNew) {
                    if (gameObj instanceof Bomb && ((Bomb) gameObj).isTriggered()) {
                        explodeBomb((Bomb) gameObj);
                        return; 
                    }
                }
            }


            // Move the object in the map
            objectsMap.get(currentPosition).remove(obj);
            obj.move(direction);
            objectsMap.get(newPosition).add(obj);
            
            if (obj instanceof JumpMan){
            pickUpItemsAt(newPosition, obj);
            }
             
        }
    }

    public void moveJumpMan(Direction direction) {
        moveObject(jumpman, direction); 
    }


    
    public void moveObjectRandomly(){
        for (List<GameObject> objectsAtPosition : objectsMap.values()) {
            List<GameObject> objectsToMove = new ArrayList<>(objectsAtPosition);
            for (GameObject obj : objectsToMove) {
                if (obj instanceof RandomMovement) {
                    MovableGameObject movableObj = (MovableGameObject) obj; 
                    Direction randomDirection = Direction.random();
                    moveObject(movableObj, randomDirection);
                     

                }
            }
        }
    }
    
    

    public void processDonkeyKongBananas(int currentTick) {
        for (List<GameObject> objectsAtPosition : objectsMap.values()) {
            for (GameObject obj : new ArrayList<>(objectsAtPosition)) { // Iterate over a copy
                if (obj instanceof DonkeyKong) {
                    DonkeyKong kong = (DonkeyKong) obj;

                    if (kong.isReadyToThrowBananas(currentTick)) {

                        Banana newBanana = new Banana(kong.getPosition());
                        newBanana = kong.throwBanana(currentTick, newBanana.getPosition());

                        Point2D bananaPosition = newBanana.getPosition();
                        List<GameObject> objectsAtCurrent = objectsMap.get(bananaPosition);
                        List<GameObject> objectsBelow = objectsMap.get(bananaPosition.plus(Direction.DOWN.asVector()));

                        if (newBanana.isValidMove(Direction.DOWN, objectsAtCurrent, objectsBelow, objectsBelow)) {
                            objectsMap.get(bananaPosition).add(newBanana);
                            bananasThrown.add(newBanana); 
                            ImageGUI.getInstance().addImage(newBanana);
                        }
                    }
                }
            }
        }

        moveBananas();
    }

    

    private void moveBananas() {

        for (Banana banana : bananasThrown) {
            Point2D currentPosition = banana.getPosition();
            Point2D newPosition = currentPosition.plus(Direction.DOWN.asVector());

            List<GameObject> objectsAtCurrent = objectsMap.get(currentPosition);
            List<GameObject> objectsAtNew = objectsMap.get(newPosition);
            

            checkCombat(banana, currentPosition); 


            if (banana.isValidMove(Direction.DOWN, objectsAtCurrent, objectsAtNew, objectsAtNew)) {
                objectsMap.get(currentPosition).remove(banana);
                banana.move(Direction.DOWN);
                objectsMap.get(newPosition).add(banana);
            } else {
                objectsMap.get(currentPosition).remove(banana);
                ImageGUI.getInstance().removeImage(banana);
            }
        }
    }

    
    public void increaseDifficulty(int currentIndex) {
        for (List<GameObject> objectsAtPosition : objectsMap.values()) {
            for (GameObject obj : new ArrayList<>(objectsAtPosition)) { // Iterate over a copy
                if (obj instanceof DonkeyKong) {
                    DonkeyKong kong = (DonkeyKong) obj;

                    for (int i = 0; i < currentIndex; i++) {
                        kong.increaseDifficulty();
                    }
                }
            }
        }
    }
    
    public void checkCombat(MovableGameObject attacker, Point2D targetPosition) {
        List<GameObject> objectsAtTarget = objectsMap.get(targetPosition);

        List<MovableGameObject> toRemove = new ArrayList<>();

        if (objectsAtTarget != null) {
            for (GameObject obj : objectsAtTarget) {
                if (obj instanceof MovableGameObject && obj != attacker) {  // Ensure it's a MovableGameObject and not the attacker itself
                    MovableGameObject defender = (MovableGameObject) obj;

                    if (attacker instanceof CanOnlyAttackJumpMan) {
                       
                    	if (defender instanceof JumpMan) {
                            
                    		if (attacker instanceof Bat) {
                         	   toRemove.add(attacker);
                            }
                            attacker.attack(defender, toRemove);
                            ImageGUI.getInstance().setStatusMessage(attacker.getName() + " attacked " + defender.getName() + " - JumpMan Health: " + defender.getHealth());

                        }
                    } else {
                        attacker.attack(defender, toRemove);
                        ImageGUI.getInstance().setStatusMessage(attacker.getName() + " attacked " + defender.getName() + " - Health: " + defender.getHealth());

                    }
                }
            }
        }

        for (MovableGameObject obj : toRemove) {
          if (obj instanceof CanOnlyAttackJumpMan) {
 
        	objectsAtTarget.remove(obj); 
            objectsMap.get(obj.getPosition()).remove(obj);  
            ImageGUI.getInstance().removeImage(obj);  
          }
          }
    }
    
    
    public boolean processLives() {
        if (jumpman == null) return false;

        if (jumpman.getHealth() <= 0) {
            System.out.println("JumpMan has 0 health. Processing lives...");

            handleJumpManDeath();
        }

        return jumpman != null && jumpman.isGameOver(); 
    }

    private void handleJumpManDeath() {
        if (jumpman == null) return;

        jumpman.loseLife();
        System.out.println("JumpMan lost a life! Remaining lives: " + jumpman.getLives());
        ImageGUI.getInstance().setStatusMessage("JumpMan lost a life! Remaining lives: " + jumpman.getLives());

        if (jumpman.isGameOver()) {
            System.out.println("Game Over!");
            ImageGUI.getInstance().setStatusMessage("Game Over!");

        } else {
            System.out.println("Respawning JumpMan at the start of the level.");

            respawnJumpManAtStart();
        }
    }

    private void respawnJumpManAtStart() {
        Point2D startingPosition = jumpmanStartPosition;
        Point2D currentPosition = jumpman.getPosition();

        objectsMap.get(currentPosition).remove(jumpman);
        jumpman.setPosition(startingPosition);
        objectsMap.get(startingPosition).add(jumpman);

         
    }

    
    public boolean isLevelComplete() {
        if (jumpman == null) return false;

        for (Point2D position : objectsMap.keySet()) {
            List<GameObject> objectsAtPosition = objectsMap.get(position);
            for (GameObject obj : objectsAtPosition) {
                if ((obj instanceof Princess || obj instanceof DoorClosed) && jumpman.getPosition().equals(position)) {
                    return true; 
                }
            }
        }
        return false;
    }
    
    
  
    public void dropBomb(int currentTick) {
        if (jumpman.getHasBomb()) {
            Point2D currentPosition = jumpman.getPosition(); 
            Bomb bomb = new Bomb(currentPosition); 
            bomb.setDroppedAtTick(currentTick); 
            bombsThatAreTriggered.add(bomb);

            // Place the bomb in the room at JumpMan's position
            placeBomb(bomb, currentPosition);
            bomb.trigger();

      
            jumpman.removeBomb();

            // Update the game GUI to show the bomb
            ImageGUI.getInstance().addImage(bomb);
            ImageGUI.getInstance().setStatusMessage("Bomb dropped at " + currentPosition);
             
        } else {
            // If JumpMan has no bomb
            ImageGUI.getInstance().setStatusMessage("No bomb in inventory to drop!");
             
        }
    }

    private void placeBomb(Bomb bomb, Point2D position) {
        List<GameObject> objectsAtPosition = objectsMap.get(position);
//        
        objectsAtPosition.add(bomb);
    }
    
    public void checkBombExplosions(int currentTick) {
        List<Bomb> toExplode = new ArrayList<>();

        for (Bomb bomb : bombsThatAreTriggered) {
            if (currentTick - bomb.getDroppedAtTick() >= 5) {
                toExplode.add(bomb);
            }
        }

        // Explode the bombs that are ready
        for (Bomb bomb : toExplode) {
            explodeBomb(bomb);
        }
    }
    
    private void explodeBomb(Bomb bomb) {
        Point2D position = bomb.getPosition();
        List<Point2D> affectedPoints = position.getWideNeighbourhoodPoints(); // Assuming this returns all affected points

        for (Point2D point : affectedPoints) {
            List<GameObject> objectsAtPoint = objectsMap.get(point);
            if (objectsAtPoint != null) {
                // Create a copy of the list to avoid modification during iteration
                List<GameObject> objectsToRemove = new ArrayList<>();

                for (GameObject obj : objectsAtPoint) {
                    
                	if(obj instanceof JumpMan){
                			jumpman.die();
                	
                	}
                	if( (obj instanceof MovableGameObject) && !(obj instanceof JumpMan) ){
                    	
                    	
                         ImageGUI.getInstance().removeImage(obj);
                         
                    }
                	if (!(obj instanceof NonExplodable)) {
                        objectsToRemove.add(obj);
                    }
                }

                // Remove objects from both the map and GUI
                for (GameObject obj : objectsToRemove) {
                    objectsAtPoint.remove(obj);
                    //objectsMap.remove(point);
                    ImageGUI.getInstance().removeImage(obj);
                }
                
            }
        }

        // Remove bomb from the game
        bombsThatAreTriggered.remove(bomb);
        List<GameObject> objectsAtBombPosition = objectsMap.get(bomb.getPosition());
        if (objectsAtBombPosition != null) {
            objectsAtBombPosition.remove(bomb);
        }
        ImageGUI.getInstance().removeImage(bomb);

    }
    
    
    public void checkIfMeatIsRotten(int currentTick) {
        List<GoodMeat> toReplace = new ArrayList<>();

        for (List<GameObject> objectsAtPosition : objectsMap.values()) {
            for (GameObject obj : new ArrayList<>(objectsAtPosition)) { // Use a copy to avoid modification issues
                if (obj instanceof GoodMeat) {
                    GoodMeat goodMeat = (GoodMeat) obj;

                    if (currentTick - goodMeat.getSpawnedAtTick() >= 15) {
                        toReplace.add(goodMeat);
                    }
                }
            }
        }

        for (GoodMeat goodMeat : toReplace) {
            Point2D position = goodMeat.getPosition();

            // Remove GoodMeat from the map
            objectsMap.get(position).remove(goodMeat);
            ImageGUI.getInstance().removeImage(goodMeat);

            // Add BadMeat at the same position
            BadMeat badMeat = new BadMeat(position);
            objectsMap.get(position).add(badMeat);
            ImageGUI.getInstance().addImage(badMeat);

        }
    }
   
    private void checkForHiddenTraps(List<GameObject> objectsBelow) {

        if (objectsBelow != null) {
            for (GameObject obj : objectsBelow) {
                if (obj instanceof Wall) {
                    Wall wall = (Wall) obj;

                    if (wall.isTrap()) {
                        Trap trap = new Trap(wall.getPosition());
                        objectsBelow.remove(wall);
                        ImageGUI.getInstance().removeImage(wall); 
                        objectsBelow.add(trap);
                        ImageGUI.getInstance().addImage(trap); 

                        jumpman.takeDamage(10); 
                        System.out.println("JumpMan triggered a trap! Health: " + jumpman.getHealth());
                        ImageGUI.getInstance().setStatusMessage("JumpMan triggered a trap! Health: " + jumpman.getHealth());

                    }
                }
            }
        }
    }
    
    
    private void pickUpItemsAt(Point2D position, MovableGameObject picker) {
        List<GameObject> objectsAtPosition = objectsMap.get(position);
        if (objectsAtPosition == null) return;

        for (GameObject obj : new ArrayList<>(objectsAtPosition)) { // copy to avoid concurrent modification
            if (obj instanceof Pickable) {
                Pickable item = (Pickable) obj;

                item.whenPickedUp(picker);

                objectsAtPosition.remove(obj);
                ImageGUI.getInstance().removeImage(obj);

                break; 
            }
        }
    }


    
    
    public void fallIfPossible() {
        if (jumpman == null) return;

        Point2D currentPosition = jumpman.getPosition();
        Point2D belowPosition = currentPosition.plus(Direction.DOWN.asVector());
        
        List<GameObject> current = objectsMap.get(currentPosition);

        List<GameObject> objectsBelow = objectsMap.get(belowPosition);

        // Fall if only a Floor exists below
        if ((objectsBelow.get(1) instanceof Floor || objectsBelow.get(1) instanceof Trap)) {
            objectsMap.get(currentPosition).remove(jumpman);
        	jumpman.move(Direction.DOWN);
            objectsMap.get(belowPosition).add(jumpman);

            

        }
    }
    
    
    public void checkForAdjacentTrapDamage() {
        if (jumpman == null) return;

        Point2D currentPosition = jumpman.getPosition();
        Point2D UPPosition = currentPosition.plus(Direction.UP.asVector());
        Point2D belowPosition = currentPosition.plus(Direction.DOWN.asVector());
        Point2D nextLeftPos = currentPosition.plus(Direction.LEFT.asVector());
        Point2D nextRIGHTPos = currentPosition.plus(Direction.RIGHT.asVector());

        List<GameObject> objectsUP = objectsMap.get(UPPosition);
        List<GameObject> objectsBelow = objectsMap.get(belowPosition);
        List<GameObject> objectsC = objectsMap.get(currentPosition);
        List<GameObject> objectsL = objectsMap.get(nextLeftPos);
        List<GameObject> objectsR = objectsMap.get(nextRIGHTPos);
        

            if ( objectsBelow.get(1) instanceof Trap ) {           
                    jumpman.takeDamage(10); // Apply 10 damage
                    System.out.println("JumpMan stepped on a trap ! Health: " + jumpman.getHealth());
                    ImageGUI.getInstance().setStatusMessage("JumpMan stepped on a trap ! Health: " + jumpman.getHealth());
                    return; // Damage applied; no need to check further
                }
    }

       
}

