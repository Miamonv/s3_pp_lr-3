package droids;

import java.util.List;

public class SniperDroid extends Droid {

    public SniperDroid(String name, int teamId, AiMode mode) {
        // HP: 70, DMG: 40, ARMOR: 0, RANGE: 7 (далекобійний!)
        super(name, 70, 40, 0, 7, teamId, mode);
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        Droid target = findTargetInRange(enemies);

        if (target == null) {
            return () -> this.name + " видивляється ціль у приціл.";
        }

        // Логіка Снайпера
        switch (this.mode) {
            case AGGRESSIVE:
                // Шанс на хедшот (20%)
                if (Math.random() < 0.2) {
                    return new HeadshotAction(this, target);
                }
                break;

            case TACTICAL:
                // Бронебійний постріл (гарантовано пробиває броню)
                // Це унікальна фішка снайпера в цьому режимі
                return new ArmorPiercingShotAction(this, target);

            case DEFENSIVE:
                // Якщо ворог надто близько (< 3 клітинок), відступати/ховатись
                if (getDistanceTo(target) < 3) {
                    return () -> this.name + " намагається розірвати дистанцію!";
                }
                break;
        }

        return new AttackAction(this, target);
    }
}