package droids;

import java.util.List;

public class SniperDroid extends Droid {
    public SniperDroid(String droidName, int teamId, AiMode mode) {
        super(droidName, 80, 150, 20, false, teamId, mode);
    }

    void snipe(Droid target) {
        target.takeDamage(this.damage);                      // дроїд наносить шкоду цілі
    }

    void headshot(Droid target) {
        target.takeDamage(this.damage * 2);                  // дроїд наносить подвоєну шкоду цілі
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
//        if (enemies.isEmpty()) {
//            return new WaitAction(this);
//        }

        switch (this.mode) {
            case DEFENSIVE:
                // якщо здоров'я < 30% - тікати
                if (this.getCurrentHealthPercent() < 0.3) {
                    return new DefendAction(this);
                }
                // Якщо ні, то він діє АГРЕСИВНО

            case AGGRESSIVE:
            default:
                // ТВОЯ ІДЕЯ: "атакувати зі слабшим здоров'ям"
                Droid target = findWeakestEnemy(enemies);
                if (target != null) {
                    return new AttackAction(this, target);
                }
                break;

            case SUPPORT:
                // Снайпер не вміє лікувати, тому просто атакує
                Droid targetSupport = findWeakestEnemy(enemies);
                if (targetSupport != null) {
                    return new AttackAction(this, targetSupport);
                }
                break;
        }

        return new WaitAction(this);
    }
}
}