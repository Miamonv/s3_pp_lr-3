package droids;

public class HealerDroid extends Droid {

    HealerDroid(String droidName, int teamId, BehaviorStrategy strategy) {
        super(droidName, 200, 30, 20, false, teamId, strategy);
    }

    void heal(Droid target) {
        target.current_health += 50;                            // дроїд лікує союзника на 50 одиниць здоров'я
        if (target.current_health > target.total_health) {
            target.current_health = target.total_health;        // не перевищує максимальне здоров'я
        }
    }

    void superHeal(Droid target) {
        target.current_health += 100;                           // дроїд лікує союзника на 100 одиниць здоров'я
        if (target.current_health > target.total_health) {
            target.current_health = target.total_health;        // не перевищує максимальне здоров'я
        }
    }

    void yellAtEnemy(Droid target) {
        target.takeDamage(this.damage);                         // дроїд наносить шкоду цілі
    }
}
