package droids;

import game_logic.*;
import java.util.List;

public class HealerDroid extends Droid {
    private boolean hasRevivedUsed = false;

    public HealerDroid(String name, int teamId, AiMode mode) {
        // HP: 250, DMG: 15, ARMOR: 5, RANGE: 3
        super(name, 250, 15, 5, 3, teamId, mode);
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // 1. Воскресіння (Тільки якщо не використано і є мертвий союзник поруч)
        if (!hasRevivedUsed && (mode == AiMode.TACTICAL)) {
            for (Droid ally : allies) {
                if (!ally.isAlive() && getDistanceTo(ally) <= this.attackRange) {
                    this.hasRevivedUsed = true;
                    return new ReviveAction(this, ally);
                }
            }
        }

        // 2. Лікування (Використовуємо метод з батьківського класу)
        Droid injured = findMostInjuredAlly(allies);

        if (injured != null && getDistanceTo(injured) <= this.attackRange) {
            // Лікуємо, якщо режим Тактичний АБО здоров'я союзника критичне (<50%)
            if (mode == AiMode.TACTICAL || ((double)injured.getCurrentHealth()/injured.getMaxHealth() < 0.5)) {
                return new HealAction(this, injured);
            }
        }

        // 3. Якщо нікого лікувати - атакуємо або захищаємось
        Droid target = findTargetInRange(enemies);
        if (target != null) {
            return new AttackAction(this, target);
        }

        return new DefendAction(this); // Чекає/Ховається
    }
}