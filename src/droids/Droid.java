package droids;

import java.util.List;

public abstract class Droid {
    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int damage;
    protected int armor;
    protected boolean shieldActive;

    protected int attackRange; // Радіус дії

    protected int x;
    protected int y;

    protected int teamId;
    protected AiMode mode;

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
            this.shieldActive = false;
        }
        if (actualDamage < 0) actualDamage = 0;

        this.currentHealth -= actualDamage;
        if (this.currentHealth < 0) this.currentHealth = 0;

        return String.format("%s отримує %d шкоди (HP: %d/%d)",
                this.name, actualDamage, this.currentHealth, this.maxHealth);
    }

    // Метод для воскресіння
    public void resurrect(int healthAmount) {
        this.currentHealth = healthAmount;
        if (this.currentHealth > this.maxHealth) this.currentHealth = this.maxHealth;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    // --- HELPER METHODS ---

    // Розрахунок відстані (Евклідова або Манхеттенська)
    public double getDistanceTo(Droid other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    // Знайти найслабшого ворога ТІЛЬКИ в радіусі дії
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

    // Знайти найближчого ворога (щоб рухатись до нього, якщо нікого немає в радіусі)
    protected Droid findNearestEnemy(List<Droid> enemies) {
        Droid nearest = null;
        double minDist = Double.MAX_VALUE;

        for (Droid e : enemies) {
            if (e.isAlive()) {
                double dist = getDistanceTo(e);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = e;
                }
            }
        }
        return nearest;
    }

    public abstract Action decideAction(List<Droid> allies, List<Droid> enemies);

    // --- GETTERS & SETTERS ---
    public String getName() { return name; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public int getTeamId() { return teamId; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void setShield(boolean active) { this.shieldActive = active; }
    public void setArmor(int armor) { this.armor = armor; }
    public int getArmor() { return armor; }
    public AiMode getMode() { return mode; }
    public int getAttackRange() { return attackRange; }
}