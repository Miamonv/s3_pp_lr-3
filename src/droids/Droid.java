package droids;

import java.util.List;
/**
 * Базовий абстрактний клас для ВСІХ дроїдів
 * Об'єднує всі спільні поля та методи
 */
public abstract class Droid {
    String name;
    int total_health;
    int current_health;
    int damage;
    int armor;
    boolean shield;

    int x, y;  //координати дрона на полі бою
    int teamId; // ID команди, до якої належить дрон
    protected AiMode mode;

    Droid(String droidName, int health, int dmg, int armr, boolean shld, int team, AiMode mode) {
        name = droidName;
        total_health = health;
        current_health = health;
        damage = dmg;
        armor = armr;
        shield = shld;
        teamId = team;
        this.mode = mode;
    }

    public void takeDamage(int amount) {
        int actualDamage = amount - this.armor;
        if (this.shield) actualDamage /= 2;     // якщо щит активний, він поглинає 50% шкоди

        if (actualDamage > 0) {
            this.current_health -= actualDamage;
            if (this.current_health < 0) {
                this.current_health = 0;
            }
            System.out.println("  " + this.name + " отримує " + actualDamage + " шкоди (" + this.current_health + "/" + this.total_health + ")");
        } else {
            // якщо броня > шкоди, шкода = 0
            System.out.println("  " + this.name + " блокує атаку!");
        }
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public boolean isAlive() {              // перевірка чи дрон живий
        return this.current_health > 0;
    }

    protected Droid findWeakestEnemy(List<Droid> enemies) {
        Droid weakestEnemy = null;
        int minHealth = Integer.MAX_VALUE;
        for (Droid enemy : enemies) {
            if (enemy.isAlive() && enemy.getCurrentHealth() < minHealth) {
                minHealth = enemy.getCurrentHealth();
                weakestEnemy = enemy;
            }
        }
        return weakestEnemy;
    }

    public double getCurrentHealthPercent() {
        if (this.total_health == 0) return 0.0;
        return (double) this.current_health / (double) this.total_health;
    }

    protected Droid findMostDamagedAlly(List<Droid> allies) {
        Droid mostWounded = null;
        double minHealthPercent = 1.0;
        for (Droid ally : allies) {
            if (ally.isAlive() && ally.getCurrentHealthPercent() < minHealthPercent) {
                minHealthPercent = ally.getCurrentHealthPercent();
                mostWounded = ally;
            }
        }
        // якщо ніхто не поранений (або всі 100%)
        return (minHealthPercent < 1.0) ? mostWounded : null;
    }

    public int getTeamId() {
        return teamId;
    }               // геттер для ID команди

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCurrentHealth() {
        return current_health;
    }

    public int getTotalHealth() {
        return total_health;
    }

    public int getArmor() {
        return armor;
    }


    public abstract Action decideAction(List<Droid> allies, List<Droid> enemies);
}

