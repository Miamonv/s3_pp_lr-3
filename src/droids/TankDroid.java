package droids;

public class TankDroid extends Droid {
    TankDroid(String droidName, int teamId, BehaviorStrategy strategy) {
        super(droidName, 70, 50, 100, false, teamId, strategy);
    }

    void activateShield() {
        this.shield = true;                                   // дроїд активує щит
        this.armor += 50;
    }

    void activateTargetShield(Droid target) {
        target.shield = true;                                 // дроїд активує щит для союзника
        target.armor += 30;
    }

    void smash(Droid target) {
        target.takeDamage(this.damage);                      // дроїд наносить шкоду цілі
    }
}
