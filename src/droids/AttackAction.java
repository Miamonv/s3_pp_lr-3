package droids;

public class AttackAction implements Action {
    private Droid attacker;
    private Droid target;

    public AttackAction(Droid attacker, Droid target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public String execute() {
        if (!attacker.isAlive()) return attacker.getName() + " мертвий і не може атакувати.";
        if (!target.isAlive()) return attacker.getName() + " б'є по корпусу мертвого ворога.";

        String dmgLog = target.takeDamage(attacker.damage);
        return attacker.getName() + " стріляє в " + dmgLog;
    }
}