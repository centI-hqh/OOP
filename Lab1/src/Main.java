import java.util.*;

public class Main {
    private List<Resource> resources;
    private List<Wall> walls;
    private List<Castle> castles;
    private List<Defender> defenders;
    private List<SiegeEngine> siegeEngines;

    public Main() {
        this.resources = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.castles = new ArrayList<>();
        this.defenders = new ArrayList<>();
        this.siegeEngines = new ArrayList<>();

        initializeGame();
    }

    private void initializeGame() {

        resources.add(new Resource("Дерево", 500, 2000));
        resources.add(new Resource("Камень", 300, 1500));
        resources.add(new Resource("Золото", 100, 500));

        Position wallPos = new Position(3, 3);
        Wall wall = new Wall("Главная стена", wallPos);
        walls.add(wall);

        Position castlePos = new Position(5, 5);
        Castle castle = new Castle("Королевский замок", castlePos);
        castles.add(castle);

        Position defenderPos = new Position(4, 5);
        Defender defender = new Defender("Рыцарь", defenderPos, 100, 15, 10, 2);
        defenders.add(defender);

        Position siegePos = new Position(1, 1);
        SiegeEngine siegeEngine = new SiegeEngine("Катапульта", siegePos, 80, 25, 5, 1);
        siegeEngines.add(siegeEngine);
    }

    public void playTurn() {
        System.out.println("Ход игры");
        displayGameState();

        for (Defender defender : defenders) {
            if (defender.isAlive()) {
                Position currentPos = defender.getPosition();
                Position newPos = new Position(currentPos.getX() + 1, currentPos.getY());
                defender.moveTo(newPos);
            }
        }

        for (SiegeEngine siegeEngine : siegeEngines) {
            if (siegeEngine.isAlive()) {
                Position currentPos = siegeEngine.getPosition();
                Position newPos = new Position(currentPos.getX() + 1, currentPos.getY());
                siegeEngine.moveTo(newPos);
            }
        }
        for (Wall wall : walls) {
            if (!wall.isConstructed()) {
                wall.construct(); // Сначала строим
            } else if (wall.getLevel() < wall.getMaxLevel()) {
                wall.upgrade(); // Затем улучшаем
            }
        }

        for (Castle castle : castles) {
            if (!castle.isConstructed()) {
                castle.construct(); // Сначала строим
            } else if (castle.getLevel() < castle.getMaxLevel()) {
                castle.upgrade(); // Затем улучшаем
            }
        }
    }

    private void displayGameState() {
        System.out.println("Ресурсы:");
        for (Resource resource : resources) {
            System.out.println("  " + resource);
        }

        System.out.println("Стены:");
        for (Wall wall : walls) {
            System.out.println("  " + wall);
        }

        System.out.println("Замки:");
        for (Castle castle : castles) {
            System.out.println("  " + castle);
        }

        System.out.println("Защитники:");
        for (Defender defender : defenders) {
            System.out.println("  " + defender);
        }

        System.out.println("Осадные орудия:");
        for (SiegeEngine siegeEngine : siegeEngines) {
            System.out.println("  " + siegeEngine);
        }
    }

    public static void main(String[] args) {
        Main game = new Main();

        for (int i = 0; i < 3; i++) {
            game.playTurn();
            System.out.println();
        }
    }
}