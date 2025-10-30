package droids;
import java.util.List;
import javax.swing.*;

public abstract class Droid {
    String name;
    int total_health;
    int current_health;
    int damage;
    int armor;
    boolean shield;

    int x;  //координати дрона на полі бою
    int y;
    int teamId = 0; // ID команди, до якої належить дрон

    Droid(String droidName, int health, int dmg, int armr, boolean shld) {
        name = droidName;
        total_health = health;
        current_health = health;
        damage = dmg;
        armor = armr;
        shield = shld;
    }

    public void takeDamage(int amount) {
        int actualDamage = amount - this.armor;
        if (this.shield) actualDamage /= 2;     // якщо щит активний, він поглинає 50% шкоди

        if (actualDamage > 0) {
            this.current_health -= actualDamage;
            if (this.current_health < 0) {
                this.current_health = 0;
            }
        }
        // якщо броня > шкоди, шкода = 0
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public boolean isAlive() {              // перевірка чи дрон живий
        return this.current_health > 0;
    }

    public int getTeamId() {                 // геттер для ID команди
        return teamId;
    }



    public abstract Action decideAction(List<Droid> allies, List<Droid> enemies);    //!!!!!!!!!!!
}

class SniperDroid extends Droid {
    SniperDroid(String droidName, int teamId) {
        super(droidName, 80, 150, 20, false);
    }

    void snipe(Droid target) {
        target.takeDamage(this.damage);                      // дроїд наносить шкоду цілі
    }

    void headshot(Droid target) {
        target.takeDamage(this.damage * 2);                  // дроїд наносить подвоєну шкоду цілі
    }

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // TODO: Реалізувати справжній AI тут
        System.out.println("Снайпер думає...");
        return null; // Поки що нічого не робимо
    }
}

class TankDroid extends Droid {
    TankDroid(String droidName) {
        super(droidName, 70, 50, 100, false);
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

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // TODO: Реалізувати справжній AI тут
        System.out.println("Танк думає...");
        return null;
    }
}

class HealerDroid extends Droid {

    HealerDroid(String droidName) {
        super(droidName, 200, 30, 20, false);
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

    @Override
    public Action decideAction(List<Droid> allies, List<Droid> enemies) {
        // TODO: Реалізувати справжній AI тут
        System.out.println("Хілер думає...");
        return null;
    }
}