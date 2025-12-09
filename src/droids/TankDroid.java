package droids;

import java.util.List;

public class TankDroid extends Droid {

    public TankDroid(String name, int teamId, AiMode mode) {
        // HP: 150, DMG: 15, ARMOR: 10, RANGE: 2 (ближній бій)
        super(name, 150, 15, 10, 2, teamId, mode);
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // 1. Захист союзників (Тільки TACTICAL)
        if (mode == AiMode.TACTICAL) {
            // Шукаємо союзника поруч, у якого мало HP і немає щита
            for (Droid ally : allies) {
                if (ally.isAlive() && ally != this && !ally.shieldActive && getDistanceTo(ally) <= this.attackRange) {
                    if (ally.getCurrentHealth() < ally.getMaxHealth() * 0.5) {
                        return new ShieldAllyAction(this, ally);
                    }
                }
            }
        }

        // 2. Захист себе (DEFENSIVE)
        if (mode == AiMode.DEFENSIVE && !this.shieldActive && Math.random() < 0.4) {
            return new ShieldAction(this);
        }

        // 3. Атака
        Droid target = findTargetInRange(enemies);
        if (target != null) {
            return new AttackAction(this, target);
        }

        return () -> this.name + " готовий приймати удар.";
    }
}