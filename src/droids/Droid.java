package droids;

import game_logic.Action;
import java.util.List;

/**
 * Базовий клас для всіх дроїдів.
 * Містить спільні характеристики та методи пошуку цілей.
 */
public abstract class Droid {
    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int damage;
    protected int armor;
    protected int attackRange;      // дальність атаки
    protected boolean shieldActive;

    protected int x, y;     // позиція дроїда на полі бою
    protected int teamId;   // ідентифікатор команди (союзники/вороги)
    protected AiMode mode;  // режим для дроїда

    public Droid(String name, int health, int damage, int armor, int range, int teamId, AiMode mode) {
        this.name = name;
        this.maxHealth = health;
        this.currentHealth = health;
        this.damage = damage;
        this.armor = armor;
        this.attackRange = range;
        this.teamId = teamId;
        this.mode = mode;
        this.shieldActive = false;
    }

    public String takeDamage(int amount) {
        int actualDamage = amount - this.armor;

        if (this.shieldActive) {
            actualDamage /= 2;
            this.shieldActive = false; // Щит збивається після удару
        }

        if (actualDamage < 0) actualDamage = 0;

        this.currentHealth -= actualDamage;
        if (this.currentHealth < 0) this.currentHealth = 0;

        return String.format("%s отримує %d шкоди (HP: %d/%d)",
                this.name, actualDamage, this.currentHealth, this.maxHealth);
    }

    public void heal(int healthAmount) {
        this.currentHealth = healthAmount;
        if (this.currentHealth > this.maxHealth) this.currentHealth = this.maxHealth;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    // 1. Знайти найслабшого ворога в радіусі дії
    protected Droid findTargetInRange(List<Droid> enemies) {
        Droid bestTarget = null;
        int minHp = Integer.MAX_VALUE;

        for (Droid e : enemies) {
            if (e.isAlive() && getDistanceTo(e) <= this.attackRange) {
                if (e.getCurrentHealth() < minHp) {
                    minHp = e.getCurrentHealth();
                    bestTarget = e;
                }
            }
        }
        return bestTarget;
    }

    // 2. Знайти найбільш пораненого союзника (для Хілера)
    protected Droid findMostInjuredAlly(List<Droid> allies) {
        Droid injured = null;
        double minPercent = 1.0;

        for (Droid a : allies) {
            if (a.isAlive() && a != this) {
                double percent = (double) a.getCurrentHealth() / a.getMaxHealth();
                if (percent < minPercent) {
                    minPercent = percent;
                    injured = a;
                }
            }
        }
        return (minPercent < 1.0) ? injured : null; // Повертає null, якщо всі здорові
    }

    public double getDistanceTo(Droid other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public abstract Action decideAction(List<Droid> allies, List<Droid> enemies);


    public String getName() { return name; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public int getArmor() { return armor; }
    public int getAttackRange() { return attackRange; }
    public int getTeamId() { return teamId; }
    public AiMode getMode() { return mode; }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void setShield(boolean active) { this.shieldActive = active; }
}