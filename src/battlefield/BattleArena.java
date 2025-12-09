package battlefield;

import droids.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class BattleArena extends Pane {
    private Canvas canvas;
    private final int CELL_SIZE = 50;
    private final int COLS = 10;
    private final int ROWS = 10;

    // Відступи для центрування сітки
    private final int OFFSET_X = 68; // Відступ зліва
    private final int OFFSET_Y = 68; // Відступ зверху

    public BattleArena(double width, double height) {
        // Збільшуємо розмір полотна, щоб вмістити відступи та сітку
        // Ширина: Сітка + відступ зліва + відступ справа (для симетрії)
        // Висота: Сітка + відступ зверху + невеликий запас знизу
        canvas = new Canvas(COLS * CELL_SIZE + OFFSET_X * 2, ROWS * CELL_SIZE + OFFSET_Y * 2);
        getChildren().add(canvas);
        setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
    }

    public void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1.0);

        // Вертикальні лінії (зсуваємо на OFFSET_X)
        for (int i = 0; i <= COLS; i++) {
            double x = i * CELL_SIZE + OFFSET_X;
            // Малюємо лінію від верху (OFFSET_Y) до низу сітки
            gc.strokeLine(x, OFFSET_Y, x, ROWS * CELL_SIZE + OFFSET_Y);
        }

        // Горизонтальні лінії (зсуваємо на OFFSET_Y)
        for (int i = 0; i <= ROWS; i++) {
            double y = i * CELL_SIZE + OFFSET_Y;
            // Малюємо лінію від лівого краю (OFFSET_X) до правого краю сітки
            gc.strokeLine(OFFSET_X, y, COLS * CELL_SIZE + OFFSET_X, y);
        }
    }

    public void drawDroids(List<Droid> droids) {
        drawGrid();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (Droid droid : droids) {
            if (!droid.isAlive()) continue;

            // Додаємо відступи до координат дроїда
            double x = droid.getX() * CELL_SIZE + OFFSET_X;
            double y = droid.getY() * CELL_SIZE + OFFSET_Y;

            if (droid.getTeamId() == 1) {
                gc.setFill(Color.ROYALBLUE);
            } else {
                gc.setFill(Color.CRIMSON);
            }

            gc.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);

            // --- ВИЗНАЧЕННЯ СИМВОЛУ ТИПУ ---
            String symbol = "?";
            if (droid instanceof TankDroid) {
                symbol = "Т";
            } else if (droid instanceof SniperDroid) {
                symbol = "С";
            } else if (droid instanceof HealerDroid) {
                symbol = "Х";
            }

            // Малювання символу
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            // Центрування символу
            gc.fillText(symbol, x + 18, y + 32);

            // HP Bar
            double hpPercent = (double) droid.getCurrentHealth() / droid.getMaxHealth();
            gc.setFill(Color.DARKRED);
            gc.fillRect(x + 5, y - 6, CELL_SIZE - 10, 5);
            gc.setFill(Color.LIMEGREEN);
            gc.fillRect(x + 5, y - 6, (CELL_SIZE - 10) * hpPercent, 5);
        }
    }
}