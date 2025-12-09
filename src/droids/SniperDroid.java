package droids;

import game_logic.*;
import java.util.List;

public class SniperDroid extends Droid {

    public SniperDroid(String name, int teamId, AiMode mode) {
        // HP: 200, DMG: 75, ARMOR: 3, RANGE: 7 (Далекобійний)
        super(name, 200, 75, 3, 7, teamId, mode);
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        Droid target = findTargetInRange(enemies);

        if (target == null) {
            // Якщо ворогів немає в радіусі, просто чекаємо (Defend)
            return new DefendAction(this);
        }

        switch (this.mode) {
            case AGGRESSIVE:
                // 20% шанс на хедшот
                if (Math.random() < 0.3) {
                    return new HeadshotAction(this, target);
                }
                break;

            case TACTICAL:
                // Пробиває броню
                return new ArmorPiercingShotAction(this, target);

            case DEFENSIVE:
                // Якщо ворог занадто близько (< 4 клітинок) -> піти в захист (імітація відступу)
                if (getDistanceTo(target) < 4) {
                    return new DefendAction(this);
                }
                break;
        }

        // Звичайна атака, якщо нічого специфічного не спрацювало
        return new AttackAction(this, target);
    }
}