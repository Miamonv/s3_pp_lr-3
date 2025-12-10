package game_logic;

import battlefield.BattleArena;
import droids.Droid;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BattleManager {
    private List<Droid> team1;
    private List<Droid> team2;
    private List<Droid> allDroids;

    private BattleArena arena;
    private TextArea logArea;

    private int currentRound = 1;
    private final int GRID_SIZE = 10;

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

        log("=== БІЙ РОЗПОЧАТО ===");
        arena.drawDroids(allDroids);
    }

    private void setupPositions() {
        // Сині (Зверху)
        int index = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < GRID_SIZE; x += 2) {
                if (index < team1.size()) {
                    int finalX = (y % 2 == 0) ? x : x + 1;
                    if (finalX < GRID_SIZE) team1.get(index++).setPosition(finalX, y);
                }
            }
        }

        // Червоні (Знизу)
        index = 0;
        for (int y = GRID_SIZE - 1; y > GRID_SIZE - 4; y--) {
            for (int x = 0; x < GRID_SIZE; x += 2) {
                if (index < team2.size()) {
                    int finalX = (y % 2 == 0) ? x : x + 1;
                    if (finalX < GRID_SIZE) team2.get(index++).setPosition(finalX, y);
                }
            }
        }
    }

    public boolean nextRound() {
        if (checkWinCondition()) return true;

        log("\n--- РАУНД " + currentRound + " ---");

        for (Droid droid : allDroids) {
            if (!droid.isAlive()) continue;

            List<Droid> allies = (droid.getTeamId() == 1) ? team1 : team2;
            List<Droid> enemies = (droid.getTeamId() == 1) ? team2 : team1;

            // 1. РУХ
            Droid nearestEnemy = findNearestEnemy(droid, enemies);
            boolean moved = false;

            // Якщо ворог далеко, намагаємось підійти
            if (nearestEnemy != null && droid.getDistanceTo(nearestEnemy) > droid.getAttackRange()) {
                // Використовуємо нову логіку обтікання
                moved = moveSmart(droid, nearestEnemy);
            }

            // 2. ДІЯ
            Action action = droid.decideAction(allies, enemies);
            String result = action.execute();

            if (moved) {
                log(droid.getName() + " маневрує... " + result);
            } else {
                log(result);
            }
        }

        currentRound++;
        arena.drawDroids(allDroids);
        return checkWinCondition();
    }

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

    // --- НОВА ЛОГІКА: ОБТІКАННЯ СОЮЗНИКІВ ---
    private boolean moveSmart(Droid droid, Droid target) {
        int currentX = droid.getX();
        int currentY = droid.getY();

        // Всі можливі ходи (сусідні клітинки)
        int[][] directions = {
                {0, -1}, {0, 1}, {-1, 0}, {1, 0},   // Прямі
                {-1, -1}, {1, -1}, {-1, 1}, {1, 1}  // Діагоналі
        };

        // Список усіх ВІЛЬНИХ клітинок навколо
        List<int[]> validMoves = new ArrayList<>();

        for (int[] dir : directions) {
            int newX = currentX + dir[0];
            int newY = currentY + dir[1];

            // Якщо клітинка вільна і в межах поля - додаємо в список кандидатів
            if (isValidMove(newX, newY)) {
                validMoves.add(new int[]{newX, newY});
            }
        }

        // Якщо йти нікуди - стоїмо
        if (validMoves.isEmpty()) return false;

        // СОРТУВАННЯ: Обираємо ту клітинку, яка найближче до цілі
        validMoves.sort(Comparator.comparingDouble(pos ->
                Math.sqrt(Math.pow(target.getX() - pos[0], 2) + Math.pow(target.getY() - pos[1], 2))
        ));

        // Беремо найкращий хід (перший у списку)
        // Навіть якщо він трохи вбік, він кращий ніж стояти на місці!
        int[] bestMove = validMoves.get(0);

        // Перевіряємо, чи цей хід не віддаляє нас надто сильно (анти-тупість)
        double currentDist = droid.getDistanceTo(target);
        double newDist = Math.sqrt(Math.pow(target.getX() - bestMove[0], 2) + Math.pow(target.getY() - bestMove[1], 2));

        // Рухаємось, якщо це наближає АБО якщо ми просто хочемо обійти (dist приблизно така ж)
        if (newDist < currentDist || (newDist - currentDist < 1.0)) {
            droid.setPosition(bestMove[0], bestMove[1]);
            return true;
        }

        return false;
    }

    private boolean isValidMove(int x, int y) {
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
            log("\n🏆 ПЕРЕМОГА ЧЕРВОНИХ!");
            return true;
        }
        if (!t2Alive) {
            log("\n🏆 ПЕРЕМОГА СИНІХ!");
            return true;
        }
        return false;
    }

    private void log(String msg) {
        logArea.appendText(msg + "\n");
    }

    public int getCurrentRound() { return currentRound; }
}