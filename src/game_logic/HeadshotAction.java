package game_logic;

import droids.Droid;

public class HeadshotAction implements Action {
    private Droid attacker;
    private Droid target;

    public HeadshotAction(Droid attacker, Droid target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public String execute() {
        if (!attacker.isAlive() || !target.isAlive()) return "";

        int critDmg = attacker.getDamage() * 2;
        String log = target.takeDamage(critDmg);
        return "CRITICAL! " + attacker.getName() + " робить ХЕДШОТ -> " + log;
    }
}