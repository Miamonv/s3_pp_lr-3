package droids;

/**
 * Конкретна дія: Атакувати ціль
 */
public class AttackAction implements Action {
    private Droid attacker;
    private Droid target;

    public AttackAction(Droid attacker, Droid target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void execute() {
        if (attacker.isAlive() && target.isAlive()) {
            System.out.println(attacker.getName() + " атакує " + target.getName() + "!");
            target.takeDamage(attacker.getDamage());
        }
    }
}
