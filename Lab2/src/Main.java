import java.util.*;

public class Main {
    private List<CastleElement> castleElements;
    private List<SiegeEquipment> siegeEquipment;
    private List<Defender> defenders;

    public Main() {
        this.castleElements = new ArrayList<>();
        this.siegeEquipment = new ArrayList<>();
        this.defenders = new ArrayList<>();

        initializeGame();
    }

    private void initializeGame() {

        CastleElement northWall = new Wall("Северная стена", 500, 15, 10);
        CastleElement southWall = new Wall("Южная стена", 500, 15, 10);
        CastleElement watchTower = new Tower("Сторожевая башня", 300, 10, 50);
        CastleElement mainGate = new Gate("Главные ворота", 400, 20);

        castleElements.add(northWall);
        castleElements.add(southWall);
        castleElements.add(watchTower);
        castleElements.add(mainGate);

        SiegeEquipment catapult = new Catapult("Громовержец", 80, 150);
        SiegeEquipment ram = new BatteringRam("Крушитель", 60, 5);
        SiegeEquipment trebuchet = new Trebuchet("Дальнобойный", 100, 200);

        siegeEquipment.add(catapult);
        siegeEquipment.add(ram);
        siegeEquipment.add(trebuchet);

        Defender archer = new Defender("Леголас", "Лучник", 80, 25, 8);
        Defender knight = new Defender("Артур", "Рыцарь", 150, 35, 20);
        Defender militia = new Defender("Джон", "Ополченец", 60, 15, 5);
        Defender archer2 = new Defender("Робин", "Лучник", 75, 22, 7);

        defenders.add(archer);
        defenders.add(knight);
        defenders.add(militia);
        defenders.add(archer2);
    }

    public void playSiegeTurn() {
        System.out.println("Ход осады ");

        // Полиморфная защита замка
        System.out.println("\n Защита замка ");
        for (CastleElement element : castleElements) {
            if (!element.isDestroyed()) {
                element.defend();
            }
        }

        // Полиморфная атака осадных орудий
        System.out.println("\n--- Атака осадных орудий ---");
        for (SiegeEquipment equipment : siegeEquipment) {
            if (equipment.isOperational() && equipment.getAmmunition() > 0) {
                // Атакуем случайный элемент замка
                CastleElement target = getRandomCastleElement();
                if (target != null && !target.isDestroyed()) {
                    equipment.attack(target);
                }
            }
        }

        // Действия защитников
        System.out.println("\n Действия защитников ");
        for (Defender defender : defenders) {
            if (defender.isAlive()) {
                defender.update(); // Обновление состояний
                defender.defendCastle();

                // Случайное использование специальной способности
                if (Math.random() < 0.3 && defender.getSpecialAbilityCooldown() == 0) {
                    defender.specialAbility();
                }
            }
        }

        displayGameState();
    }

    private CastleElement getRandomCastleElement() {
        if (castleElements.isEmpty()) return null;
        List<CastleElement> activeElements = new ArrayList<>();
        for (CastleElement element : castleElements) {
            if (!element.isDestroyed()) {
                activeElements.add(element);
            }
        }
        if (activeElements.isEmpty()) return null;
        return activeElements.get((int)(Math.random() * activeElements.size()));
    }

    private void displayGameState() {
        System.out.println("\n Состояние игры ");

        System.out.println("\nЭлементы замка:");
        for (CastleElement element : castleElements) {
            System.out.println("  " + element);
        }

        System.out.println("\nОсадные орудия:");
        for (SiegeEquipment equipment : siegeEquipment) {
            System.out.println("  " + equipment);
        }

        System.out.println("\nЗащитники:");
        for (Defender defender : defenders) {
            if (defender.isAlive()) {
                System.out.println("  " + defender);
            }
        }
    }

    public void demonstratePolymorphism() {
        System.out.println("Демонстрация полиморфизма ");

        System.out.println("Полиморфизм CastleElement:");
        for (CastleElement element : castleElements) {
            System.out.println("   " + element.getName() + ": " + element.getStatus());
            element.takeDamage(30);
        }
    }

    public static void main(String[] args) {
        Main game = new Main();


        game.demonstratePolymorphism();

        System.out.println("\nНАЧАЛО ОСАДЫ ");

        for (int turn = 1; turn <= 5; turn++) {
            System.out.println("ХОД " + turn);
            game.playSiegeTurn();
            System.out.println("\n" + "=".repeat(50) + "\n");

            if (turn == 2) {
                System.out.println(">>> Подкрепление! Стрелы доставлены на башни!\n");

                for (CastleElement element : game.castleElements) {
                    if (element instanceof Tower) {
                        ((Tower) element).resupplyArrows(20);
                    }
                }
            }
        }
    }
}