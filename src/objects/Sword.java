package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Sword extends GameObject implements Pickable {

    private static final int ATTACK_BOOST = 15;

    public Sword(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Sword";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void whenPickedUp(MovableGameObject picker) {
        if (picker instanceof JumpMan) {
            JumpMan jm = (JumpMan) picker;
            jm.increaseAttackStrength(ATTACK_BOOST);
            jm.addToInventory(this);  // <-- use `this`, no extra item parameter

            ImageGUI.getInstance().setStatusMessage(
                jm.getName() + " damage increased by " + ATTACK_BOOST +
                " and is now " + jm.getAttackStrength()
            );
        }
    }
}
