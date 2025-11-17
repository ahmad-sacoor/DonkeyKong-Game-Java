package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;

public class Bomb extends GameObject implements Pickable {

    private boolean isTriggered = false;
    private int droppedAtTick;

    public Bomb(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    // ✔️ Updated to new Pickable signature
    @Override
    public void whenPickedUp(MovableGameObject picker) {
        if (picker instanceof JumpMan) {
            JumpMan jm = (JumpMan) picker;

            jm.addBomb();
            jm.addToInventory(this);

            ImageGUI.getInstance().setStatusMessage(
                jm.getName() + " picked up a bomb"
            );
        }
    }

    public void trigger() {
        isTriggered = true;
    }

    public void setDroppedAtTick(int tick) {
        this.droppedAtTick = tick;
    }

    public int getDroppedAtTick() {
        return droppedAtTick;
    }

    public boolean isTriggered() {
        return isTriggered;
    }
}
