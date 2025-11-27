import java.util.*;

public class Game {
    private GameManager gameManager;
    private List<CastleElement> castleElements;
    private List<SiegeEquipment> siegeEquipments;
    private List<Defender> defenders;

    public Game() {
        this.gameManager = new GameManager();
        this.castleElements = new ArrayList<>();
        this.siegeEquipments = new ArrayList<>();
        this.defenders = new ArrayList<>();

        initializeGame();
    }

    private void initializeGame() {

        CastleElement wall = new Wall("Северная стена", 500, 0, 20, 10);
        CastleElement tower = new Tower("Сторожевая башня", 300, 25, 10, 30);

        castleElements.add(wall);
        castleElements.add(tower);

        gameManager.addCombatable(wall);
        gameManager.addCombatable(tower);
        gameManager.addUpgradeable(wall);
        gameManager.addUpgradeable(tower);
        gameManager.addRepairable(wall);
        gameManager.addRepairable(tower);

        SiegeEquipment catapult = new Catapult("Катапульта", 200, 80, 5, 10);

        siegeEquipments.add(catapult);


        gameManager.addCombatable(catapult);
        gameManager.addProducible(catapult);
        gameManager.addRepairable(catapult);


        Defender defender1 = new Defender("Рыцарь Артур", 100, 15, 8);
        Defender defender2 = new Defender("Лучник Робин", 80, 20, 5);

        defenders.add(defender1);
        defenders.add(defender2);


        gameManager.addCombatable(defender1);
        gameManager.addCombatable(defender2);
        gameManager.addMovable(defender1);
        gameManager.addMovable(defender2);
    }

    public void playTurn() {
        System.out.println(" Ход игры ");


        gameManager.moveAllTo(3, 3);

        gameManager.produceAll();
        gameManager.upgradeAll();

        gameManager.performCombatRound();

        System.out.println("Общее количество боевых единиц: " + gameManager.getAliveCombatCount());

        displayGameState();
    }

    private void displayGameState() {
        System.out.println("\nСостояние игры:");

        System.out.println("Элементы замка:");
        for (CastleElement element : castleElements) {
            System.out.println("  " + element);
        }

        System.out.println("Осадные орудия:");
        for (SiegeEquipment equipment : siegeEquipments) {
            System.out.println("  " + equipment);
        }

        System.out.println("Защитники:");
        for (Defender defender : defenders) {
            System.out.println("  " + defender);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();

        // Играем несколько ходов
        for (int i = 0; i < 3; i++) {
            game.playTurn();
            System.out.println();
        }

        System.out.println(" ДОПОЛНИТЕЛЬНЫЕ ВОЗМОЖНОСТИ ");
        game.gameManager.repairAll();
        game.gameManager.sortCombatantsByHealth();

        System.out.println("\n ДЕМОНСТРАЦИЯ КЛОНИРОВАНИЯ ");
        try {
            Defender original = game.defenders.get(0);
            Defender cloned = (Defender) original.clone();
            System.out.println("Оригинал: " + original.getName());
            System.out.println("Клон: " + cloned.getName());
        } catch (CloneNotSupportedException e) {
            System.out.println("Клонирование не поддерживается");
        }


        System.out.println("\n ФИНАНСОВАЯ ИНФОРМАЦИЯ ");
        System.out.println("Общая стоимость улучшений: " + game.gameManager.getTotalUpgradeCost() + " золота");
        System.out.println("Общая стоимость ремонта: " + game.gameManager.getTotalRepairCost() + " золота");

        game.gameManager.displayDetailedInfo();
    }
}