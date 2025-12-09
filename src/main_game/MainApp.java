package main_game;

import battlefield.BattleArena;
import droids.*;
import game_logic.BattleManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private Stage primaryStage;
    private BattleArena battleArena;
    private BattleManager battleManager;
    private Timeline gameLoop;

    // Списки для вибору
    private List<ComboBox<String>> team1Types = new ArrayList<>();
    private List<ComboBox<AiMode>> team1Modes = new ArrayList<>();
    private List<ComboBox<String>> team2Types = new ArrayList<>();
    private List<ComboBox<AiMode>> team2Modes = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Drone Battle Arena");
        showMainMenu();
        primaryStage.show();
    }

    // --- 1. ГОЛОВНЕ МЕНЮ ---
    private void showMainMenu() {
        if (gameLoop != null) gameLoop.stop();

        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #2c3e50;");

        Label titleLabel = new Label("[ БИТВИ ДРОЇДІВ ]");
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        Button btnDuel = createMenuButton("Дуель 1 vs 1");
        Button btnTeam = createMenuButton("Битва 5 vs 5");
        Button btnRules = createMenuButton("Правила / Інформація");
        Button btnExit = createMenuButton("Вихід");

        btnDuel.setOnAction(e -> showSetupScreen(1));
        btnTeam.setOnAction(e -> showSetupScreen(5));
        btnRules.setOnAction(e -> showRulesScreen());
        btnExit.setOnAction(e -> primaryStage.close());

        menuLayout.getChildren().addAll(titleLabel, btnDuel, btnTeam, btnRules, btnExit);
        primaryStage.setScene(new Scene(menuLayout, 450, 550));
        primaryStage.centerOnScreen();
    }

    // --- 2. ЕКРАН ПРАВИЛ ---
    private void showRulesScreen() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ecf0f1;");
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("AI MODES INFO");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        TextArea rulesText = new TextArea();
        rulesText.setEditable(false);
        rulesText.setWrapText(true);
        rulesText.setPrefHeight(330);
        rulesText.setStyle("-fx-font-size: 14px;");
        rulesText.setText(
                "РЕЖИМИ ШІ:\n\n" +
                        "1. AGGRESSIVE (Агресивний):\n" +
                        "   - Завжди атакує.\n" +
                        "   - Снайпер має 25% шанс критичного удару.\n\n" +
                        "2. DEFENSIVE (Захисний):\n" +
                        "   - Пріоритет виживання.\n" +
                        "   - Танк частіше вмикає щит.\n" +
                        "   - Дроїди відступають при низькому HP.\n\n" +
                        "3. TACTICAL (Тактичний):\n" +
                        "   - Хілер: Може воскресити 1 союзника.\n" +
                        "   - Танк: Дає щит слабким союзникам.\n" +
                        "   - Снайпер: Пробиває броню."
        );

        Button btnBack = new Button("<< Назад до Меню");
        btnBack.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, rulesText, btnBack);
        primaryStage.setScene(new Scene(layout, 500, 600));
    }

    // --- 3. ЕКРАН НАЛАШТУВАНЬ ---
    private void showSetupScreen(int teamSize) {
        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");
        mainLayout.setAlignment(Pos.CENTER);

        VBox team1Box = new VBox(10);
        team1Box.setAlignment(Pos.TOP_CENTER);
        Label lblBlue = new Label("КОМАНДА 1 (СИНІ - ЗВЕРХУ)");
        lblBlue.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblBlue.setTextFill(javafx.scene.paint.Color.DARKBLUE);
        team1Box.getChildren().add(lblBlue);

        team1Types.clear(); team1Modes.clear();
        for(int i=0; i<teamSize; i++) createDroidSelector(team1Box, team1Types, team1Modes);

        VBox team2Box = new VBox(10);
        team2Box.setAlignment(Pos.TOP_CENTER);
        Label lblRed = new Label("КОМАНДА 2 (ЧЕРВОНІ - ЗНИЗУ)");
        lblRed.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblRed.setTextFill(javafx.scene.paint.Color.DARKRED);
        team2Box.getChildren().add(lblRed);

        team2Types.clear(); team2Modes.clear();
        for(int i=0; i<teamSize; i++) createDroidSelector(team2Box, team2Types, team2Modes);

        VBox controls = new VBox(20);
        controls.setAlignment(Pos.CENTER);

        Button btnStart = new Button("БІЙ! >>");
        btnStart.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;");

        Button btnBack = new Button("<< Назад");
        btnBack.setOnAction(e -> showMainMenu());

        btnStart.setOnAction(e -> startCustomBattle());

        controls.getChildren().addAll(btnStart, btnBack);

        mainLayout.getChildren().addAll(team1Box, controls, team2Box);

        int height = 300 + (teamSize * 50);
        primaryStage.setScene(new Scene(mainLayout, 1000, height)); // Трохи ширше для зручності
        primaryStage.centerOnScreen();
    }

    private void createDroidSelector(VBox parent, List<ComboBox<String>> types, List<ComboBox<AiMode>> modes) {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER);

        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Танк", "Снайпер", "Хіллер");
        type.getSelectionModel().select(0);
        type.setPrefWidth(100);

        ComboBox<AiMode> mode = new ComboBox<>();
        mode.getItems().addAll(AiMode.values());
        mode.getSelectionModel().select(AiMode.AGGRESSIVE);
        mode.setPrefWidth(110);

        types.add(type);
        modes.add(mode);
        row.getChildren().addAll(type, mode);
        parent.getChildren().add(row);
    }

    private void startCustomBattle() {
        List<Droid> t1 = new ArrayList<>();
        List<Droid> t2 = new ArrayList<>();

        for (int i = 0; i < team1Types.size(); i++) {
            String type = team1Types.get(i).getValue();
            AiMode mode = team1Modes.get(i).getValue();
            t1.add(createDroidFactory(type, "Сині-" + (i+1), 1, mode));
        }

        for (int i = 0; i < team2Types.size(); i++) {
            String type = team2Types.get(i).getValue();
            AiMode mode = team2Modes.get(i).getValue();
            t2.add(createDroidFactory(type, "Червоні-" + (i+1), 2, mode));
        }

        showBattleScreen(t1, t2);
    }

    private Droid createDroidFactory(String type, String name, int teamId, AiMode mode) {
        switch (type) {
            case "Снайпер": return new SniperDroid(name, teamId, mode);
            case "Хіллер": return new HealerDroid(name, teamId, mode);
            default:       return new TankDroid(name, teamId, mode);
        }
    }

    // --- 4. ЕКРАН БОЮ ---
    private void showBattleScreen(List<Droid> team1, List<Droid> team2) {
        BorderPane battleRoot = new BorderPane();

        // Верхня панель
        HBox topPanel = new HBox(15);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-background-color: #34495e;");
        topPanel.setAlignment(Pos.CENTER);

        Button btnMenu = new Button("Меню");
        btnMenu.setOnAction(e -> showMainMenu());

        Button btnPlay = new Button("> Старт");
        btnPlay.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnPause = new Button("|| Пауза");
        btnPause.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");

        Label statusLabel = new Label("Раунд 1");
        statusLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        topPanel.getChildren().addAll(btnMenu, btnPlay, btnPause, statusLabel);
        battleRoot.setTop(topPanel);

        // ЦЕНТР (АРЕНА)
        battleArena = new BattleArena(500, 500); // 10 клітинок * 50px
        StackPane arenaWrapper = new StackPane(battleArena);
        arenaWrapper.setStyle("-fx-background-color: #95a5a6;");
        arenaWrapper.setAlignment(Pos.CENTER);

        arenaWrapper.setPadding(new Insets(3, 0, 0, 0));

        battleRoot.setCenter(arenaWrapper);

        // ПРАВА СТОРОНА (ЛОГ)
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true); // Перенесення рядків
        logArea.setPrefWidth(300); // Хороша ширина для логу
        logArea.setPrefHeight(600);
        logArea.setFont(Font.font("Monospaced", 12));
        logArea.setStyle("-fx-control-inner-background: #ecf0f1;");

        // Додаємо заголовок для логу
        VBox logBox = new VBox(10);
        logBox.setPadding(new Insets(10));
        logBox.setStyle("-fx-background-color: #bdc3c7;");
        Label logTitle = new Label("Журнал бою:");
        logTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        logBox.getChildren().addAll(logTitle, logArea);

        // Встановлюємо лог праворуч
        battleRoot.setRight(logBox);

        // Ініціалізація
        battleManager = new BattleManager(battleArena, logArea);
        battleManager.setTeams(team1, team2);

        // Таймер (1с)
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            boolean gameOver = battleManager.nextRound();
            statusLabel.setText("Раунд: " + battleManager.getCurrentRound());
            if (gameOver) {
                gameLoop.stop();
                btnPlay.setDisable(true);
                btnPause.setDisable(true);
                statusLabel.setText("ГРУ ЗАВЕРШЕНО");
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        btnPlay.setOnAction(e -> gameLoop.play());
        btnPause.setOnAction(e -> gameLoop.stop());

        // Ширше вікно, щоб вмістити лог
        primaryStage.setScene(new Scene(battleRoot, 1000, 700));
        primaryStage.centerOnScreen();
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMinWidth(220);
        btn.setMinHeight(45);
        btn.setStyle("-fx-font-size: 16px; -fx-cursor: hand; -fx-background-radius: 10; -fx-background-color: #ecf0f1;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-font-size: 16px; -fx-cursor: hand; -fx-background-radius: 10; -fx-background-color: #bdc3c7;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-font-size: 16px; -fx-cursor: hand; -fx-background-radius: 10; -fx-background-color: #ecf0f1;"));

        return btn;
    }
}