package game_logic;

import droids.Droid;
import droids.TankDroid;

public class DefendAction implements Action {
    private Droid defender;

    public DefendAction(Droid defender) {
        this.defender = defender;
    }

    @Override
    public String execute() {
        if (!defender.isAlive()) return "";

        // Якщо це Танк - він вмикає щит
        if (defender instanceof TankDroid) {
            defender.setShield(true);
            return defender.getName() + " активує ЕНЕРГОЩИТ (Броня +50%)!";
        }

        int current = defender.getCurrentHealth();
        int max = defender.getMaxHealth();
        int heal = 15;

        // Якщо здоров'я і так повне
        if (current >= max) {
            return defender.getName() + " займає оборону.";
        }

        int newHealth = current + heal;
        if (newHealth > max) newHealth = max;

        // Викликаємо heal, який просто сетить значення
        defender.heal(newHealth);

        return defender.getName() + " відновлює сили (+" + heal + " HP).";
    }
}