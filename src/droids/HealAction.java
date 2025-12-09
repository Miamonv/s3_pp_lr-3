package droids;

/**
 * Конкретна дія: Лікувати ціль
 * Ця дія може бути створена ТІЛЬКИ Хілером.
 */
public class HealAction implements Action {
    private HealerDroid healer;
    private Droid target;

    public HealAction(HealerDroid healer, Droid target) {
        this.healer = healer;
        this.target = target;
    }

    @Override
    public void execute() {
        int randomValue = (int) (Math.random() * 10);
        if (healer.isAlive() && target.isAlive()) {
            switch (randomValue) {
                case 0, 1, 2, 3, 4, 5, 6, 7:
                    System.out.println(healer.getName() + " лікує " + target.getName() + " на 50 одиниць здоров'я.");
                    healer.heal(target);
                    break;

                case 8, 9, 10:
                    System.out.println(healer.getName() + " здійснює супер лікування " + target.getName() + " на 100 одиниць здоров'я!");
                    healer.superHeal(target);
                    break;
            }

        }
    }
}