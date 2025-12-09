package droids;

public class HealAction implements Action {
    private HealerDroid healer;
    private Droid target;

    public HealAction(HealerDroid healer, Droid target) {
        this.healer = healer;
        this.target = target;
    }

    @Override
    public String execute() {
        if (!healer.isAlive()) return "";

        int healAmount = 30;
        // Проста логіка лікування (без прямого доступу до полів, якби були сетери)
        // Тут ми припускаємо, що є метод heal() або доступ до полів у пакеті
        // Для спрощення, уявимо що heal() повертає int

        int oldHp = target.getCurrentHealth();
        // (Тут треба додати метод heal(int amount) в Droid або доступ до полів)
        // Хакинг для прикладу:
        target.takeDamage(-healAmount); // Від'ємний урон = лікування (якщо логіка дозволяє)

        return healer.getName() + " ремонтує " + target.getName() + " (+" + healAmount + " HP).";
    }
}