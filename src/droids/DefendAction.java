package droids;
/**
 * Конкретна дія: Захищатися (або тікати)
 * Ця дія може бути створена ТІЛЬКИ Танком.
 */
public class DefendAction implements Action {
    private TankDroid defender;
    private Droid toDefend;

    public DefendAction(TankDroid defender, Droid toDefend) {
        this.defender = defender;
    }

    @Override
    public void execute() {
        if (defender.isAlive()) {
            System.out.println(defender.getName() + " стає в захисну стійку!");
            defender.activateShield();
        }

    }
}