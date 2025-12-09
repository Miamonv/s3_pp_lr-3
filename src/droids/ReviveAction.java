package droids;

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
        // Відновлюємо 50% здоров'я
        int reviveHp = deadAlly.getMaxHealth() / 2;
        deadAlly.resurrect(reviveHp);

        return "РОЗРЯЯЯЯД!!! " + healer.getName() + " використовує дефібрилятор! " + deadAlly.getName() + " повертається в бій!";
    }
}
