package game_logic;

import droids.Droid;
import droids.TankDroid;

import java.util.Random;

public class DefendAction implements Action {
    private Droid defender;
    private Random random = new Random();

    public DefendAction(Droid defender) {
        this.defender = defender;
    }

    @Override
    public String execute() {
        if (!defender.isAlive()) return "";

        // 1. ТАНК: Завжди вмикає щит (він не тікає, він танкує)
        if (defender instanceof TankDroid) {
            defender.setShield(true);
            return defender.getName() + " активує ЕНЕРГОЩИТ (Броня +50%)!";
        }

        // 2. ІНШІ (Снайпер, Хілер): Спроба тактичного маневру
        int currentX = defender.getX();
        int currentY = defender.getY();

        int newX = currentX;
        int newY = currentY;

        // Крок 1: Спроба відступити назад (до своєї бази)
        // Команда 1 (Сині, зверху) -> Тікають до Y=0
        // Команда 2 (Червоні, знизу) -> Тікають до Y=9
        if (defender.getTeamId() == 1) {
            if (currentY > 0) newY--;
        } else {
            if (currentY < 9) newY++;
        }

        // Якщо назад йти нікуди (ми на краю), пробуємо піти вбік
        if (newY == currentY) {
            // Випадково обираємо: вліво чи вправо
            boolean tryLeft = random.nextBoolean();

            if (tryLeft && currentX > 0) {
                newX--; // Вліво
            } else if (!tryLeft && currentX < 9) {
                newX++; // Вправо
            } else if (currentX > 0) {
                newX--; // Якщо хотіли вправо, але не можна - йдемо вліво
            } else if (currentX < 9) {
                newX++; // Якщо хотіли вліво, але не можна - йдемо вправо
            }
        }

        // Крок 2: Виконання руху
        if (newX != currentX || newY != currentY) {
            defender.setPosition(newX, newY);
            if (newY != currentY) {
                return defender.getName() + " здійснює тактичний відступ назад!";
            } else {
                return defender.getName() + " ухиляється вбік!";
            }
        }

        // 3. ЯКЩО ПРИТИСНУЛИ (Нікуди йти): Аварійний ремонт
        int current = defender.getCurrentHealth();
        int max = defender.getMaxHealth();
        int heal = 15;

        if (current >= max) {
            return defender.getName() + " займає глуху оборону.";
        }

        int newHealth = current + heal;
        if (newHealth > max) newHealth = max;

        defender.heal(newHealth);

        return defender.getName() + " загнаний в кут, проводить аварійний ремонт (+" + heal + " HP).";
    }
}