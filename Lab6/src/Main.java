import java.util.*;

public class Main {
    private GameBoard gameBoard;
    private MovementSystem movementSystem;
    private List<SiegeUnit> attackers;
    private List<SiegeUnit> defenders;
    private int turnCount;
    private Scanner scanner;
    private Position treasureChest; // Сундук с сокровищами - цель игры

    public Main() {
        this.gameBoard = new GameBoard(10, 10);
        this.movementSystem = new MovementSystem(gameBoard);
        this.attackers = new ArrayList<>();
        this.defenders = new ArrayList<>();
        this.turnCount = 0;
        this.scanner = new Scanner(System.in);
        this.treasureChest = new Position(5, 5); // Сундук в центре замка

        initializeGame();
        showInitialMap();
    }

    private void initializeGame() {
        System.out.println(" СРЕДНЕВЕКОВАЯ ОСАДА ");
        System.out.println("Вы играете за рыцаря! Доберитесь до сундука с сокровищами в замке!");
        System.out.println("Сундук находится в центре замка (позиция 5,5)");

        // Создание главного юнита игрока - рыцаря
        Position knightPos = new Position(0, 0);
        Knight playerKnight = new Knight("Ваш Рыцарь", knightPos, true);
        playerKnight.setMovementSystem(movementSystem);
        attackers.add(playerKnight);

        // Создание защитников (компьютер) - только 2 для упрощения
        Position defenderPos1 = new Position(5, 4);
        Position defenderPos2 = new Position(4, 5);

        Knight defenderKnight = new Knight("Рыцарь-защитник", defenderPos1, false);
        Archer defenderArcher = new Archer("Лучник-защитник", defenderPos2, false);

        defenderKnight.setMovementSystem(movementSystem);
        defenderArcher.setMovementSystem(movementSystem);

        defenders.add(defenderKnight);
        defenders.add(defenderArcher);

        // Размещение юнитов на поле
        for (SiegeUnit unit : attackers) {
            gameBoard.placeEntity(unit, unit.getPosition());
        }
        for (SiegeUnit unit : defenders) {
            gameBoard.placeEntity(unit, unit.getPosition());
        }

        System.out.println("Игра инициализирована!");
        System.out.println("Ваш юнит: 1 рыцарь");
        System.out.println("Защитники: " + defenders.size() + " юнитов");
    }

    private void showInitialMap() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("=".repeat(60));

        displayGameState();

        System.out.println("\n ЦЕЛЬ ИГРЫ:");
        System.out.println("• Доберитесь до сундука с сокровищами ($) в центре замка");
        System.out.println("• Обходите вражеских юнитов (k, a)");
        System.out.println("• Избегайте воды (W) - она непроходима");

        System.out.println("\n УПРАВЛЕНИЕ:");
        System.out.println("• Вводите координаты X и Y для перемещения");
        System.out.println("• Можно двигаться на 1 клетку в любом направлении");
        System.out.println("• Атакуйте врагов, когда они рядом");

        System.out.println("\nНажмите Enter чтобы начать...");
        scanner.nextLine();
    }

    public void playGame() {
        while (true) {
            turnCount++;
            System.out.println("\n" + "=".repeat(50));
            System.out.println(" ХОД " + turnCount);


            System.out.println("\n ВАШ ХОД ");
            playerTurn();

            if (checkVictoryConditions()) {
                break;
            }

            System.out.println("\n ХОД ЗАЩИТНИКОВ ");
            computerTurn();

            if (checkVictoryConditions()) {
                break;
            }

            displayGameState();

            // Ограничение на количество ходов
            if (turnCount >= 30) {
                System.out.println("\n  Достигнут лимит ходов! Игра окончена!");
                break;
            }
        }

        scanner.close();
    }

    private void playerTurn() {
        SiegeUnit playerKnight = attackers.get(0);

        if (!playerKnight.isAlive()) {
            System.out.println(" Ваш рыцарь погиб! Игра окончена.");
            return;
        }

        boolean moveCompleted = false;

        while (!moveCompleted) {
            System.out.println("Ваш рыцарь: " + playerKnight.getName() +
                    " (HP: " + playerKnight.getHealth() + ")");
            System.out.println("Текущая позиция: " + playerKnight.getPosition());
            System.out.println("Цель: сундук с сокровищами на позиции " + treasureChest);

            // Показываем доступные клетки с описанием
            List<Position> reachable = playerKnight.getReachablePositions();
            System.out.println("\nДоступные для перемещения позиции:");
            if (reachable.isEmpty()) {
                System.out.println("  Нет доступных ходов! Все соседние клетки заняты или непроходимы.");
                return;
            } else {
                for (Position pos : reachable) {
                    Cell cell = gameBoard.getCell(pos);
                    String terrainInfo = cell != null ? getTerrainDescription(cell.getTerrainType()) : "Неизвестно";
                    System.out.println("  " + pos + " - " + terrainInfo);
                }
            }

            // Проверяем, можем ли атаковать врагов
            List<SiegeUnit> attackableEnemies = getAttackableEnemies(playerKnight);
            if (!attackableEnemies.isEmpty()) {
                System.out.println("\n Враги в радиусе атаки:");
                for (int i = 0; i < attackableEnemies.size(); i++) {
                    SiegeUnit enemy = attackableEnemies.get(i);
                    double distance = playerKnight.getPosition().distanceTo(enemy.getPosition());
                    System.out.println((i + 1) + ". " + enemy.getName() +
                            " (HP: " + enemy.getHealth() + ") на " +
                            enemy.getPosition() + " (дистанция: " + String.format("%.1f", distance) + ")");
                }
            }

            System.out.println("\nВыберите действие:");
            System.out.println("1. Переместиться");
            if (!attackableEnemies.isEmpty()) {
                System.out.println("2. Атаковать врага");
            }
            System.out.print("Ваш выбор: ");

            try {
                int action = scanner.nextInt();

                if (action == 1) {
                    System.out.print("Введите координату X для перемещения: ");
                    int targetX = scanner.nextInt();
                    System.out.print("Введите координату Y для перемещения: ");
                    int targetY = scanner.nextInt();

                    Position target = new Position(targetX, targetY);

                    // Проверяем, что целевая позиция в списке доступных
                    boolean isReachable = reachable.stream().anyMatch(pos -> pos.equals(target));
                    if (!isReachable) {
                        System.out.println("ОШИБКА: Позиция " + target + " недоступна для перемещения!");
                        System.out.println("Используйте только позиции из списка доступных.");
                        continue; // Возвращаемся к началу цикла
                    }

                    boolean success = playerKnight.moveTo(target);
                    if (success) {
                        System.out.println("Успешно переместились на " + target);
                        moveCompleted = true;

                        // Проверяем, достигли ли сундука
                        if (target.equals(treasureChest)) {
                            System.out.println("ВЫ ДОСТИГЛИ СУНДУКА С СОКРОВИЩАМИ!");
                        }
                    } else {
                        System.out.println("ОШИБКА: Не удалось переместиться на позицию " + target);
                    }

                } else if (action == 2 && !attackableEnemies.isEmpty()) {
                    System.out.print("Выберите цель для атаки (1-" + attackableEnemies.size() + "): ");
                    int enemyChoice = scanner.nextInt() - 1;

                    if (enemyChoice >= 0 && enemyChoice < attackableEnemies.size()) {
                        SiegeUnit targetEnemy = attackableEnemies.get(enemyChoice);
                        playerKnight.attack(targetEnemy);
                        moveCompleted = true;

                        if (!targetEnemy.isAlive()) {
                            System.out.println(targetEnemy.getName() + " повержен!");
                            gameBoard.removeEntity(targetEnemy.getPosition());
                            defenders.remove(targetEnemy);
                        }
                    } else {
                        System.out.println("ОШИБКА: Неверный выбор цели!");
                    }
                } else {
                    System.out.println("ОШИБКА: Неверный выбор действия!");
                }
            } catch (InputMismatchException e) {
                System.out.println("ОШИБКА: Введите корректное число!");
                scanner.next(); // Очищаем некорректный ввод
            }
        }

        // Автоматическое лечение если нужно
        playerKnight.update();
    }

    private String getTerrainDescription(TerrainType terrain) {
        switch(terrain) {
            case GRASS: return "Трава (легкое перемещение)";
            case FOREST: return "Лес (замедляет движение)";
            case ROAD: return "Дорога (быстрое перемещение)";
            case WATER: return "Вода (непроходима)";
            case WALL: return "Стена замка (непроходима)";
            case GATE: return "Ворота замка";
            case TOWER: return "Башня (непроходима)";
            case BATTLEFIELD: return "Поле боя";
            default: return terrain.getName();
        }
    }

    private List<SiegeUnit> getAttackableEnemies(SiegeUnit unit) {
        List<SiegeUnit> attackable = new ArrayList<>();
        for (SiegeUnit enemy : defenders) {
            if (enemy.isAlive()) {
                double distance = unit.getPosition().distanceTo(enemy.getPosition());
                if (distance <= 1.5) {
                    attackable.add(enemy);
                }
            }
        }
        return attackable;
    }
    private void computerTurn() {
        for (SiegeUnit defender : defenders) {
            if (defender.isAlive()) {
                performComputerTurn(defender, attackers);
            }
        }
        updateGameState();
    }

    private void performComputerTurn(SiegeUnit unit, List<SiegeUnit> enemies) {
        SiegeUnit closestEnemy = findClosestEnemy(unit, enemies);

        if (closestEnemy != null) {
            double distance = unit.getPosition().distanceTo(closestEnemy.getPosition());

            if (canAttack(unit, closestEnemy)) {
                System.out.println(unit.getName() + " атакует " + closestEnemy.getName());
                unit.attack(closestEnemy);
            } else {
                System.out.println(unit.getName() + " движется к " + closestEnemy.getName());
                movementSystem.moveTowardTarget(unit, closestEnemy.getPosition());
            }
        }
        unit.update();
    }

    private boolean canAttack(SiegeUnit attacker, SiegeUnit target) {
        double distance = attacker.getPosition().distanceTo(target.getPosition());
        return distance <= 1.5;
    }

    private SiegeUnit findClosestEnemy(SiegeUnit unit, List<SiegeUnit> enemies) {
        SiegeUnit closest = null;
        double minDistance = Double.MAX_VALUE;

        for (SiegeUnit enemy : enemies) {
            if (enemy.isAlive()) {
                double distance = unit.getPosition().distanceTo(enemy.getPosition());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = enemy;
                }
            }
        }
        return closest;
    }

    private void updateGameState() {
        attackers.removeIf(unit -> !unit.isAlive());
        defenders.removeIf(unit -> !unit.isAlive());
    }

    private boolean checkVictoryConditions() {
        SiegeUnit playerKnight = attackers.get(0);

        // Победа - достигли сундука с сокровищами
        if (playerKnight.getPosition().equals(treasureChest)) {
            System.out.println("\n ПОБЕДА! ВЫ НАШЛИ СОКРОВИЩА ЗАМКА!");
            System.out.println("  Ваш рыцарь достиг сундука за " + turnCount + " ходов!");
            System.out.println(" Сокровища ваши! Поздравляем!");
            return true;
        }

        // Поражение - рыцарь мертв
        if (!playerKnight.isAlive()) {
            System.out.println("\n ПОРАЖЕНИЕ! Ваш рыцарь погиб в бою!");
            System.out.println(" Защитники отстояли сокровища замка!");
            return true;
        }

        return false;
    }

    private void displayGameState() {
        System.out.println("\n ТЕКУЩАЯ СИТУАЦИЯ ");

        SiegeUnit playerKnight = attackers.get(0);
        System.out.println("\nВаш рыцарь:");
        System.out.println("  " + playerKnight.getName() + " - HP: " + playerKnight.getHealth() +
                " на позиции " + playerKnight.getPosition());

        System.out.println("\nЗащитники замка:");
        for (SiegeUnit unit : defenders) {
            System.out.println("  " + unit.getName() + " - HP: " + unit.getHealth() +
                    " на позиции " + unit.getPosition());
        }

        System.out.println("\nЦель: сундук с сокровищами на позиции " + treasureChest);

        System.out.println("\nИгровое поле:");
        displayBoard();
    }

    private void displayBoard() {
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9");

        for (int y = 0; y < gameBoard.getHeight(); y++) {
            System.out.print(y + " ");
            for (int x = 0; x < gameBoard.getWidth(); x++) {
                Position currentPos = new Position(x, y);
                Cell cell = gameBoard.getCell(currentPos);

                if (cell != null) {
                    if (cell.isOccupied() && cell.getEntity() instanceof SiegeUnit) {
                        System.out.print(cell.toString() + " ");
                    }
                    else if (currentPos.equals(treasureChest)) {
                        System.out.print("$ ");
                    }
                    else {
                        System.out.print(cell.toString() + " ");
                    }
                } else {
                    System.out.print("? ");
                }
            }
            System.out.println();
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("  ЛЕГЕНДА КАРТЫ");
        System.out.println("=".repeat(50));

        System.out.println("\n ЦЕЛЬ:");
        System.out.println("  $  - Сундук с сокровищами (доберитесь до него!)");

        System.out.println("\n  ЮНИТЫ:");
        System.out.println("  K  - Ваш рыцарь");
        System.out.println("  k  - Вражеский рыцарь");
        System.out.println("  a  - Вражеский лучник");

        System.out.println("\n  МЕСТНОСТЬ:");
        System.out.println("  .  - Трава");
        System.out.println("  F  - Лес ");
        System.out.println("  R  - Дорога ");
        System.out.println("  W  - Вода (непроходима)");

        System.out.println("\n ЗАМОК:");
        System.out.println("  █  - Стена (непроходима)");
        System.out.println("  G  - Ворота");
        System.out.println("  T  - Башня (непроходима)");

        System.out.println("\n СОВЕТ: Двигайтесь к сундуку ($) обходя врагов и воду!");
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.playGame();
    }
}