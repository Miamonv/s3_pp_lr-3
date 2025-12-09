package droids;

import java.util.List;

public class HealerDroid extends Droid {
    private boolean hasRevivedUsed = false; // Чи використав воскресіння

    public HealerDroid(String name, int teamId, AiMode mode) {
        // HP: 100, DMG: 10, ARMOR: 0, RANGE: 3 (лікує зблизька)
        super(name, 100, 10, 0, 3, teamId, mode);
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // 1. Спроба воскресіння (Тільки в режимі TACTICAL або SUPPORT)
        // Шукаємо мертвого союзника
        if (!hasRevivedUsed) {
            for (Droid ally : allies) {
                if (!ally.isAlive() && getDistanceTo(ally) <= this.attackRange) {
                    this.hasRevivedUsed = true;
                    return new ReviveAction(this, ally);
                }
            }
        }

        // 2. Лікування поранених
        Droid injured = findMostInjuredAlly(allies);
        if (injured != null && getDistanceTo(injured) <= this.attackRange) {
            // Якщо TACTICAL або союзник при смерті -> лікуємо
            if (mode == AiMode.TACTICAL || ((double)injured.getCurrentHealth()/injured.getMaxHealth() < 0.5)) {
                return new HealAction(this, injured);
            }
        }

        // 3. Атака, якщо нікого лікувати
        Droid target = findTargetInRange(enemies);
        if (target != null) {
            return new AttackAction(this, target);
        }

        // 4. Якщо ворог далеко - рухатись до нього (повертаємо null або дію руху, тут спрощено)
        return () -> this.name + " шукає кому допомогти.";
    }
}