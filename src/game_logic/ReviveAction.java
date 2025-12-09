package game_logic;

import droids.HealerDroid;
import droids.Droid;

public class ReviveAction implements Action {
    private HealerDroid healer;
    private Droid deadAlly;

    public ReviveAction(HealerDroid healer, Droid deadAlly) {
        this.healer = healer;
        this.deadAlly = deadAlly;
    }

    @Override
    public String execute() {
        if (!healer.isAlive()) return "";

        int reviveHp = deadAlly.getMaxHealth() / 2;
        deadAlly.heal(reviveHp);

        return "⚡ ДИВО! " + healer.getName() + " реанімує " + deadAlly.getName() + "!";
    }
}