package droids;

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

        int massiveDmg = attacker.damage * 2;
        String log = target.takeDamage(massiveDmg);
        return "CRITICAL! " + attacker.getName() + " робить ХЕДШОТ по " + log;
    }
}
