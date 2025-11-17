package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;

public class BadMeat extends GameObject implements Pickable {

    public BadMeat(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "BadMeat";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void whenPickedUp(MovableGameObject picker) {
        if (picker instanceof JumpMan) {
            JumpMan jm = (JumpMan) picker;
            jm.takeDamage(15);
            ImageGUI.getInstance().setStatusMessage(
                jm.getName() + " ate rotten meat, Health decreased by 15 and is now " + jm.getHealth()
            );
        }
    }
}
