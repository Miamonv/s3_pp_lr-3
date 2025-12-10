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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private Stage primaryStage;
    private BattleArena battleArena;
    private BattleManager battleManager;
    private Timeline gameLoop;

    // Списки для збереження вибору користувача (Тип дрона і Режим)
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

        // КНОПКИ
        Button btnDuel = createMenuButton("⚔ Дуель 1 vs 1");
        Button btnTeam = createMenuButton("Командна Битва");
        Button btnLoad = createMenuButton("Читати запис бою");
        Button btnRules = createMenuButton("Правила / Інформація");
        Button btnExit = createMenuButton("Вихід");

        // Дії кнопок
        btnDuel.setOnAction(e -> showSetupScreen(1, 1)); // Одразу 1 на 1
        btnTeam.setOnAction(e -> showTeamSizeSelector()); // Спочатку вибір кількості!
        btnLoad.setOnAction(e -> loadBattleLog());
        btnRules.setOnAction(e -> showRulesScreen());
        btnExit.setOnAction(e -> primaryStage.close());

        menuLayout.getChildren().addAll(titleLabel, btnDuel, btnTeam, btnLoad, btnRules, btnExit);

        primaryStage.setScene(new Scene(menuLayout, 450, 600));
        primaryStage.centerOnScreen();
    }

    // --- 1.5. ВІКНО ВИБОРУ РОЗМІРУ КОМАНД ---
    private void showTeamSizeSelector() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #ecf0f1;");

        Label lblTitle = new Label("Налаштування розміру команд");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Вибір для Синіх
        HBox boxBlue = new HBox(10);
        boxBlue.setAlignment(Pos.CENTER);
        Label lblBlue = new Label("Сині (Кількість):");
        lblBlue.setTextFill(javafx.scene.paint.Color.DARKBLUE);
        lblBlue.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        // Spinner: мін 2, макс 10, дефолт 5
        Spinner<Integer> spinBlue = new Spinner<>(2, 10, 5);
        boxBlue.getChildren().addAll(lblBlue, spinBlue);

        // Вибір для Червоних
        HBox boxRed = new HBox(10);
        boxRed.setAlignment(Pos.CENTER);
        Label lblRed = new Label("Червоні (Кількість):");
        lblRed.setTextFill(javafx.scene.paint.Color.DARKRED);
        lblRed.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Spinner<Integer> spinRed = new Spinner<>(2, 10, 5);
        boxRed.getChildren().addAll(lblRed, spinRed);

        // Кнопки
        Button btnNext = new Button("Далі >>");
        btnNext.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnBack = new Button("Назад");
        btnBack.setOnAction(e -> showMainMenu());

        // Переходимо до налаштування типів з вибраними кількостями
        btnNext.setOnAction(e -> {
            showSetupScreen(spinBlue.getValue(), spinRed.getValue());
        });

        layout.getChildren().addAll(lblTitle, boxBlue, boxRed, new Separator(), btnNext, btnBack);

        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.centerOnScreen();
    }

    // --- 2. ЕКРАН ПРАВИЛ ---
    private void showRulesScreen() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ecf0f1;");
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("ДОВІДНИК");
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

    // --- 3. ЕКРАН НАЛАШТУВАНЬ (Динамічний) ---
    private void showSetupScreen(int sizeBlue, int sizeRed) {
        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");
        mainLayout.setAlignment(Pos.CENTER);

        // --- Команда 1 (Сині) ---
        VBox team1Box = new VBox(10);
        team1Box.setAlignment(Pos.TOP_CENTER);
        Label lblBlue = new Label("СИНІ (" + sizeBlue + ")");
        lblBlue.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblBlue.setTextFill(javafx.scene.paint.Color.DARKBLUE);
        team1Box.getChildren().add(lblBlue);

        team1Types.clear(); team1Modes.clear();
        for(int i=0; i<sizeBlue; i++) createDroidSelector(team1Box, team1Types, team1Modes);

        // --- Команда 2 (Червоні) ---
        VBox team2Box = new VBox(10);
        team2Box.setAlignment(Pos.TOP_CENTER);
        Label lblRed = new Label("ЧЕРВОНІ (" + sizeRed + ")");
        lblRed.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblRed.setTextFill(javafx.scene.paint.Color.DARKRED);
        team2Box.getChildren().add(lblRed);

        team2Types.clear(); team2Modes.clear();
        for(int i=0; i<sizeRed; i++) createDroidSelector(team2Box, team2Types, team2Modes);

        // --- Кнопки управління ---
        VBox controls = new VBox(20);
        controls.setAlignment(Pos.CENTER);

        Button btnStart = new Button("БІЙ! >>");
        btnStart.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnBack = new Button("<< Назад");
        btnBack.setOnAction(e -> showMainMenu());

        btnStart.setOnAction(e -> startCustomBattle());

        controls.getChildren().addAll(btnStart, btnBack);

        mainLayout.getChildren().addAll(team1Box, controls, team2Box);

        // Адаптуємо висоту вікна під більшу команду
        int maxTeamSize = Math.max(sizeBlue, sizeRed);
        int height = 350 + (maxTeamSize * 45);
        if (height > 850) height = 850;

        primaryStage.setScene(new Scene(mainLayout, 1000, height));
        primaryStage.centerOnScreen();
    }

    private void createDroidSelector(VBox parent, List<ComboBox<String>> types, List<ComboBox<AiMode>> modes) {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER);

        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Tank", "Sniper", "Healer");
        type.getSelectionModel().select(0); // За замовчуванням Tank
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
            t1.add(createDroidFactory(team1Types.get(i).getValue(), "Blue-" + (i+1), 1, team1Modes.get(i).getValue()));
        }
        for (int i = 0; i < team2Types.size(); i++) {
            t2.add(createDroidFactory(team2Types.get(i).getValue(), "Red-" + (i+1), 2, team2Modes.get(i).getValue()));
        }
        showBattleScreen(t1, t2);
    }

    private Droid createDroidFactory(String type, String name, int teamId, AiMode mode) {
        switch (type) {
            case "Sniper": return new SniperDroid(name, teamId, mode);
            case "Healer": return new HealerDroid(name, teamId, mode);
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

        Button btnPlay = new Button("▶ Старт");
        Button btnPause = new Button("⏸ Пауза");
        Button btnSave = new Button("Зберегти Лог");
        btnSave.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Label statusLabel = new Label("Раунд 1");
        statusLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        topPanel.getChildren().addAll(btnMenu, btnPlay, btnPause, btnSave, statusLabel);
        battleRoot.setTop(topPanel);

        // ЦЕНТР (АРЕНА)
        battleArena = new BattleArena(0, 0);
        StackPane arenaWrapper = new StackPane(battleArena);
        arenaWrapper.setStyle("-fx-background-color: #95a5a6;");
        arenaWrapper.setAlignment(Pos.CENTER);
        arenaWrapper.setPadding(new Insets(3, 0, 0, 0)); // Відступ зверху
        battleRoot.setCenter(arenaWrapper);

        // ПРАВО (ЛОГ)
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setFont(Font.font("Monospaced", 12));
        logArea.setStyle("-fx-control-inner-background: #ecf0f1;");

        VBox logBox = new VBox(5);
        logBox.setPadding(new Insets(5));
        logBox.setPrefWidth(300);
        logBox.setStyle("-fx-background-color: #bdc3c7;");
        Label logTitle = new Label("Журнал Бою:");
        logTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox.setVgrow(logArea, Priority.ALWAYS); // Розтягуємо лог

        logBox.getChildren().addAll(logTitle, logArea);
        battleRoot.setRight(logBox);

        // ЛОГІКА
        battleManager = new BattleManager(battleArena, logArea);
        battleManager.setTeams(team1, team2);

        btnSave.setOnAction(e -> saveLogToFile(logArea.getText()));

        // ТАЙМЕР
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.8), event -> {
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

        // Вікно ширше, щоб вмістити лог збоку
        primaryStage.setScene(new Scene(battleRoot, 1100, 750));
        primaryStage.centerOnScreen();
    }

    // --- ФУНКЦІЇ ДЛЯ РОБОТИ З ФАЙЛАМИ ---

    private void saveLogToFile(String content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти лог бою");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("battle_log.txt");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.print(content);
                new Alert(Alert.AlertType.INFORMATION, "Лог успішно збережено!").show();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Помилка при збереженні.").show();
            }
        }
    }

    private void loadBattleLog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Відкрити лог бою");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                showLogViewer(content);
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Помилка при читанні файлу.").show();
            }
        }
    }

    private void showLogViewer(String content) {
        Stage logStage = new Stage();
        logStage.setTitle("Архів Бою (Текст)");

        TextArea area = new TextArea(content);
        area.setEditable(false);
        area.setWrapText(true);
        area.setFont(Font.font("Monospaced", 12));

        VBox layout = new VBox(area);
        VBox.setVgrow(area, Priority.ALWAYS);

        logStage.setScene(new Scene(layout, 600, 800));
        logStage.show();
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