package game_logic;

import droids.Droid;

public class AttackAction implements Action {
    private Droid attacker;
    private Droid target;

    public AttackAction(Droid attacker, Droid target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public String execute() {
        if (!attacker.isAlive() || !target.isAlive()) return "";

        String result = target.takeDamage(attacker.getDamage());
        return attacker.getName() + " атакує -> " + result;
    }
}