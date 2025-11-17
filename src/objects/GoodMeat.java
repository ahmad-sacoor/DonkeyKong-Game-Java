package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;

public class GoodMeat extends GameObject implements Pickable {

    private int spawnedAtTick = 0;

    public GoodMeat(Point2D position, int spawnedAtTick) {
        super(position);
        this.spawnedAtTick = spawnedAtTick;
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void whenPickedUp(MovableGameObject picker) {
        if (picker instanceof JumpMan) {
            JumpMan jm = (JumpMan) picker;
            jm.restoreHealth();
            ImageGUI.getInstance().setStatusMessage(
                jm.getName() + " re-established his Stamina! Health: " + jm.getHealth()
            );
        }
    }

    public int getSpawnedAtTick() {
        return spawnedAtTick;
    }

    public void setSpawnedAtTick(int tick) {
        this.spawnedAtTick = tick;
    }
}
