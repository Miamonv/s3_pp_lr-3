package game_logic;

import droids.HealerDroid;
import droids.Droid;

public class HealAction implements Action {
    private HealerDroid healer;
    private Droid target;
    private int healAmount = 60;

    public HealAction(HealerDroid healer, Droid target) {
        this.healer = healer;
        this.target = target;
    }

    @Override
    public String execute() {
        if (!healer.isAlive()) return "";

        int newHp = target.getCurrentHealth() + healAmount;
        target.heal(newHp);

        return healer.getName() + " ремонтує " + target.getName() + " на +" + healAmount + " HP.";
    }
}