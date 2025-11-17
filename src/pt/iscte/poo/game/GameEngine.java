package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import objects.JumpMan;


public class GameEngine implements Observer {
	
	private Room currentRoom;
	private int lastTickProcessed = 0;
    private String[] levelFiles = {"rooms/room0.txt", "rooms/room1.txt", "rooms/room2.txt"};
    private int currentLevelIndex = 0;
    

	public GameEngine() {
        currentRoom = new Room(levelFiles[currentLevelIndex],lastTickProcessed); // Load room from configuration file
		ImageGUI.getInstance().update();
	}

	@Override
	public void update(Observed source) {
		
		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			if (Direction.isDirection(k)) {
			    Direction direction = Direction.directionFor(k); // Get the direction from key
				currentRoom.moveJumpMan(direction); //direction that we want to go
                ImageGUI.getInstance().update();  // Refresh the GUI

			}
			
			if (k == 66) {  // If 'b' is pressed, drop a bomb
                currentRoom.dropBomb(lastTickProcessed);//lastTickProcessed);
            }
			
		}
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

	
	
	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		currentRoom.moveObjectRandomly();
		ImageGUI.getInstance().update();

        currentRoom.processLives();  
        boolean resetGame = currentRoom.processLives();
        if (resetGame) {
            resetGame(); //all lives lost
            return; 
        }        currentRoom.checkForAdjacentTrapDamage();
	    currentRoom.checkBombExplosions(lastTickProcessed);

		currentRoom.checkIfMeatIsRotten(lastTickProcessed);

		currentRoom.fallIfPossible();

	    currentRoom.processDonkeyKongBananas(lastTickProcessed);

	    if (currentRoom.isLevelComplete()) {
            loadNextLevel();
        }
		ImageGUI.getInstance().update();

	    lastTickProcessed++;
	}



	private void loadNextLevel() {
        currentLevelIndex++;

        if (currentLevelIndex >= levelFiles.length  ) {
            System.out.println("Game completed! All levels finished.");
            ImageGUI.getInstance().showMessage("Victory!", "Congratulations! You rescued the princess");
    		ImageGUI.getInstance().dispose(); 

        } else {
            System.out.println("Loading next level: " + levelFiles[currentLevelIndex]);
            ImageGUI.getInstance().setStatusMessage("Loading next level: " + levelFiles[currentLevelIndex]);



            currentRoom = new Room(levelFiles[currentLevelIndex],lastTickProcessed);
            
            currentRoom.increaseDifficulty(currentLevelIndex);

            ImageGUI.getInstance().update();
        }
    }
	
	
	private void resetGame() {
	    currentLevelIndex = 0;       // Restart from the first level
	    lastTickProcessed = 0;       // Reset the tick counter
	    currentRoom = new Room(levelFiles[currentLevelIndex], lastTickProcessed); // Reload the first room
	    
	    // Reset JumpMan's state
	    if (currentRoom.getJumpMan() != null) {
	        JumpMan jumpman = currentRoom.getJumpMan();
	        jumpman.restoreHealth();  // Reset health
	        jumpman.resetLives();     // Restore default number of lives
	        jumpman.clearInventory(); // Clear inventory
	    }

	    ImageGUI.getInstance().update();  // Refresh the GUI
	    System.out.println("Game has been reset to the first level.");
        ImageGUI.getInstance().setStatusMessage("Game has been reset to the first level.");

	}
	


}
