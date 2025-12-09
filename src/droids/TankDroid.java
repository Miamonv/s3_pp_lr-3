package droids;

import game_logic.*;
import java.util.List;

public class TankDroid extends Droid {

    public TankDroid(String name, int teamId, AiMode mode) {
        // HP: 350, DMG: 30, ARMOR: 12, RANGE: 2
        super(name, 350, 30, 12, 2, teamId, mode);
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // 1. Захист союзника (Тільки TACTICAL)
        if (mode == AiMode.TACTICAL) {
            for (Droid ally : allies) {
                // Якщо союзник поруч, живий, не я сам, і без щита -> захистити
                if (ally.isAlive() && ally != this && getDistanceTo(ally) <= this.attackRange) {
                    // Пріоритет тим, у кого < 50% HP
                    if (ally.getCurrentHealth() < ally.getMaxHealth() * 0.5) {
                        return new ShieldAllyAction(this, ally);
                    }
                }
            }
        }

        // 2. Власний захист (DefendAction для танка вмикає щит)
        if (mode == AiMode.DEFENSIVE && Math.random() < 0.4) {
            return new DefendAction(this);
        }

        // 3. Атака
        Droid target = findTargetInRange(enemies);
        if (target != null) {
            return new AttackAction(this, target);
        }

        return new DefendAction(this);
    }
}