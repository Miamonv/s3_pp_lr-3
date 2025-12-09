package game_logic;

import battlefield.BattleArena;
import droids.Droid;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class BattleManager {
    private List<Droid> team1; // Сині (Зверху)
    private List<Droid> team2; // Червоні (Знизу)
    private List<Droid> allDroids;

    private BattleArena arena;
    private TextArea logArea;

    private int currentRound = 1;
    private final int GRID_SIZE = 10; // Розмір поля 10х10

    public BattleManager(BattleArena arena, TextArea logArea) {
        this.arena = arena;
        this.logArea = logArea;
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
        this.allDroids = new ArrayList<>();
    }

    public void setTeams(List<Droid> t1, List<Droid> t2) {
        this.team1 = t1;
        this.team2 = t2;
        this.allDroids.clear();
        this.allDroids.addAll(t1);
        this.allDroids.addAll(t2);

        setupPositions();

        log("Бій розпочато! Сині (Зверху) vs Червоні (Знизу).");
        arena.drawDroids(allDroids);
    }

    // --- ОНОВЛЕНА ЛОГІКА РОЗСТАНОВКИ (10x10) ---
    private void setupPositions() {
        // Команда 1 (Сині) - ставимо на верхні ряди (y=0, y=1)
        for (int i = 0; i < team1.size(); i++) {
            // Розставляємо через одну клітинку: 0, 2, 4, 6, 8
            int x = (i * 2) % GRID_SIZE;
            int y = (i * 2) / GRID_SIZE; // Якщо більше 5 дроїдів, перейде на ряд 1

            // Захист від виходу за межі (хоча у нас макс 5 дроїдів)
            if (y > 1) y = 1;

            team1.get(i).setPosition(x, y);
        }

        // Команда 2 (Червоні) - ставимо на нижні ряди (y=9, y=8)
        for (int i = 0; i < team2.size(); i++) {
            int x = (i * 2) % GRID_SIZE;
            int y = (GRID_SIZE - 1) - ((i * 2) / GRID_SIZE); // 9, потім 8

            if (y < 8) y = 8;

            team2.get(i).setPosition(x, y);
        }
    }

    public boolean nextRound() {
        if (checkWinCondition()) return true;

        log("\n--- РАУНД " + currentRound + " ---");

        for (Droid droid : allDroids) {
            if (!droid.isAlive()) continue;

            List<Droid> allies = (droid.getTeamId() == 1) ? team1 : team2;
            List<Droid> enemies = (droid.getTeamId() == 1) ? team2 : team1;

            // 1. ЛОГІКА РУХУ
            // Знаходимо найближчого ворога
            Droid nearestEnemy = findNearestEnemy(droid, enemies);

            boolean moved = false;
            // Якщо ворог існує, і він далі ніж радіус атаки - рухаємось
            if (nearestEnemy != null && droid.getDistanceTo(nearestEnemy) > droid.getAttackRange()) {
                moveTowards(droid, nearestEnemy);
                moved = true;
            }

            // 2. ЛОГІКА ДІЇ
            // Дрон вирішує, що робити (атакувати, лікувати, захищатись)
            Action action = droid.decideAction(allies, enemies);
            String result = action.execute();

            if (moved) {
                log(droid.getName() + " рухається. " + result);
            } else {
                log(result);
            }
        }

        currentRound++;
        arena.drawDroids(allDroids);
        return checkWinCondition();
    }

    // Пошук найближчого ворога
    private Droid findNearestEnemy(Droid self, List<Droid> enemies) {
        Droid nearest = null;
        double minD = Double.MAX_VALUE;
        for(Droid e : enemies) {
            if(e.isAlive()) {
                double d = self.getDistanceTo(e);
                if(d < minD) { minD = d; nearest = e; }
            }
        }
        return nearest;
    }

    // Рух до цілі на 1 клітинку
    private void moveTowards(Droid droid, Droid target) {
        int oldX = droid.getX();
        int oldY = droid.getY();

        // Визначаємо вектор руху (-1, 0, +1)
        int dirX = Integer.compare(target.getX(), oldX);
        int dirY = Integer.compare(target.getY(), oldY);

        int newX = oldX + dirX;
        int newY = oldY + dirY;

        // Спроба 1: Йти по діагоналі (або прямо до цілі)
        if (isValidMove(newX, newY)) {
            droid.setPosition(newX, newY);
        }
        // Спроба 2: Якщо зайнято, йти тільки по X
        else if (isValidMove(newX, oldY)) {
            droid.setPosition(newX, oldY);
        }
        // Спроба 3: Якщо зайнято, йти тільки по Y
        else if (isValidMove(oldX, newY)) {
            droid.setPosition(oldX, newY);
        }
        // Якщо все зайнято - стоїмо на місці
    }

    // Перевірка, чи можна стати в клітинку (чи в межах поля і чи не зайнята)
    private boolean isValidMove(int x, int y) {
        // Межі поля 0..9
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) return false;

        return !isOccupied(x, y);
    }

    private boolean isOccupied(int x, int y) {
        for(Droid d : allDroids) {
            if(d.isAlive() && d.getX() == x && d.getY() == y) return true;
        }
        return false;
    }

    private boolean checkWinCondition() {
        boolean t1Alive = team1.stream().anyMatch(Droid::isAlive);
        boolean t2Alive = team2.stream().anyMatch(Droid::isAlive);

        if (!t1Alive && !t2Alive) {
            log("\n🏁 НІЧИЯ! Всі загинули.");
            return true;
        }
        if (!t1Alive) {
            log("\n🏆 ПЕРЕМОГА ЧЕРВОНИХ (Знизу)!");
            return true;
        }
        if (!t2Alive) {
            log("\n🏆 ПЕРЕМОГА СИНІХ (Зверху)!");
            return true;
        }
        return false;
    }

    private void log(String msg) {
        logArea.appendText(msg + "\n");
    }

    public int getCurrentRound() { return currentRound; }
}