import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(" ЛАБОРАТОРНАЯ РАБОТА 4: СРЕДНЕВЕКОВАЯ ОСАДА \n");

        // Создание игровых объектов
        CastleElement tower = new Tower("Сторожевая башня", 300, 10, 50);
        CastleElement wall = new Wall("Северная стена", 500, 15, 10);
        CastleElement wall2 = new Wall("Южная стена", 450, 12, 8);
        CastleElement gate = new Wall("Главные ворота", 600, 20, 12);

        SiegeEquipment catapult = new Catapult("Громовержец", 80, 150);
        SiegeEquipment catapult2 = new Catapult("Разрушитель", 70, 130);
        SiegeEquipment trebuchet = new Catapult("Требушет", 90, 170);

        Defender archer = new Defender("Леголас", "Лучник", 80, 25, 8);
        Defender knight = new Defender("Артур", "Рыцарь", 150, 35, 20);
        Defender knight2 = new Defender("Ланселот", "Рыцарь", 140, 32, 18);
        Defender archer2 = new Defender("Робин", "Лучник", 75, 28, 6);


        // 1. GameCollection для всех боевых единиц
        GameCollection<Combatable> combatCollection = new GameCollection<>();
        combatCollection.add(tower);
        combatCollection.add(wall);
        combatCollection.add(wall2);
        combatCollection.add(gate);
        combatCollection.add(archer);
        combatCollection.add(archer2);
        combatCollection.add(knight);
        combatCollection.add(knight2);
        combatCollection.add(catapult);
        combatCollection.add(catapult2);
        combatCollection.add(trebuchet);

        System.out.println("1. GameCollection<Combatable>:");
        System.out.println(combatCollection);

        System.out.println("\nСводка по типам:");
        combatCollection.getCombatSummary().forEach((type, count) ->
                System.out.println("  " + type + ": " + count + " активных"));

        System.out.println("\nСильные юниты (атака > 30):");
        combatCollection.getStrongEntities(30).forEach(unit ->
                System.out.println("  " + unit.getName() + " - атака: " + unit.getAttack()));

        // 2. Inventory для осадных орудий
        Inventory<SiegeEquipment> siegeInventory = new Inventory<>(15);
        siegeInventory.addItem("Громовержец", catapult);
        siegeInventory.addItem("Разрушитель", catapult2);
        siegeInventory.addItem("Требушет", trebuchet);

        System.out.println("\n2. Inventory<SiegeEquipment>:");
        System.out.println(siegeInventory);
        System.out.println("Все осадные орудия:");
        siegeInventory.getAllItems().forEach(equipment ->
                System.out.println("  " + equipment.getName() + " - урон: " + equipment.getAttack() +
                        ", уровень: " + equipment.getLevel()));

        // 3. ResourceManager для улучшаемых объектов
        ResourceManager<Upgradeable> upgradeManager = new ResourceManager<>(2000);
        upgradeManager.addResource("Сторожевая башня", tower);
        upgradeManager.addResource("Северная стена", wall);
        upgradeManager.addResource("Южная стена", wall2);
        upgradeManager.addResource("Главные ворота", gate);
        upgradeManager.addResource("Леголас", archer);
        upgradeManager.addResource("Робин", archer2);
        upgradeManager.addResource("Артур", knight);
        upgradeManager.addResource("Ланселот", knight2);
        upgradeManager.addResource("Громовержец", catapult);
        upgradeManager.addResource("Разрушитель", catapult2);
        upgradeManager.addResource("Требушет", trebuchet);

        System.out.println("\n3. ResourceManager<Upgradeable>:");
        System.out.println(upgradeManager);

        System.out.println("\nДоступные улучшения:");
        upgradeManager.getUpgradeableResources().forEach(resource -> {
            System.out.println("  " + getUpgradeableName(resource) +
                    " [ур. " + resource.getLevel() + "/" + resource.getMaxLevel() +
                    "] - стоимость: " + resource.getUpgradeCost() +
                    ", бонусы: " + resource.getLevelBonuses());
        });

        System.out.println("\nСредние уровни по типам:");
        upgradeManager.getAverageLevelSummary().forEach((type, avgLevel) ->
                System.out.println("  " + type + ": уровень " + String.format("%.1f", avgLevel)));

        // 4. CastleDefenseManager (TreeMap)
        CastleDefenseManager defenseManager = new CastleDefenseManager();
        defenseManager.addDefense(tower);
        defenseManager.addDefense(wall);
        defenseManager.addDefense(wall2);
        defenseManager.addDefense(gate);

        System.out.println("\n4. CastleDefenseManager (TreeMap):");
        System.out.println(defenseManager);
        System.out.println("Распределение по уровням: " + defenseManager.getLevelDistribution());
        System.out.println("Самая сильная защита: " +
                (defenseManager.getStrongestDefense() != null ?
                        defenseManager.getStrongestDefense().getName() : "нет"));
        System.out.println("Самая слабая защита: " +
                (defenseManager.getWeakestDefense() != null ?
                        defenseManager.getWeakestDefense().getName() : "нет"));

        // 5. SiegeEquipmentManager (HashSet)
        SiegeEquipmentManager equipmentManager = new SiegeEquipmentManager();
        equipmentManager.addEquipment(catapult);
        equipmentManager.addEquipment(catapult2);
        equipmentManager.addEquipment(trebuchet);

        System.out.println("\n5. SiegeEquipmentManager (HashSet):");
        System.out.println(equipmentManager);
        System.out.println("Исправные орудия:");
        equipmentManager.getOperationalEquipment().forEach(equipment ->
                System.out.println("  " + equipment.getName()));

        System.out.println("\n=== ОПЕРАЦИИ С КОЛЛЕКЦИЯМИ ===\n");

        // Сортировка
        System.out.println("Сортировка боевых единиц по атаке:");
        combatCollection.sortByAttack();
        combatCollection.getActiveEntities().forEach(unit ->
                System.out.println("  " + unit.getName() + " - атака: " + unit.getAttack()));

        // Улучшение объектов
        System.out.println("\nУлучшение объектов:");
        upgradeManager.upgradeResource("Сторожевая башня");
        upgradeManager.upgradeResource("Артур");
        upgradeManager.upgradeResource("Громовержец");

        // Производство ресурсов
        System.out.println("\nПроизводство ресурсов:");
        ((Catapult)catapult).reload();
        ((Tower)tower).produce();
        catapult.produce();

        // Боевые действия
        System.out.println("\nБоевые действия:");
        archer.attack(catapult);
        tower.attack(catapult);
        ((Catapult)catapult).reload();
        catapult.attack(tower);
        knight.attack(catapult2);

        // Обновление состояния коллекций после боя
        System.out.println("\nСостояние после боя:");
        System.out.println("GameCollection: " + combatCollection);
        System.out.println("Активных сущностей: " + combatCollection.getActiveEntities().size());

        System.out.println("\nСлабые юниты (требуют внимания):");
        combatCollection.getWeakEntities().forEach(unit ->
                System.out.println("  " + unit.getName()));

        // Финальное состояние
        System.out.println("\n=== ФИНАЛЬНОЕ СОСТОЯНИЕ ===");
        System.out.println("GameCollection: " + combatCollection);
        System.out.println("Inventory: " + siegeInventory);
        System.out.println("ResourceManager: " + upgradeManager);
        System.out.println("CastleDefenseManager: " + defenseManager);
        System.out.println("SiegeEquipmentManager: " + equipmentManager);

        System.out.println("\nДетальное состояние ключевых объектов:");
        System.out.println(tower);
        System.out.println(wall);
        System.out.println(catapult);
        System.out.println(archer);
        System.out.println(knight);

        // Демонстрация Stream API
        System.out.println("\n=== ДЕМОНСТРАЦИЯ STREAM API ===");

        int totalAttackPower = combatCollection.getActiveEntities().stream()
                .mapToInt(Combatable::getAttack)
                .sum();
        System.out.println("Общая атака всех активных юнитов: " + totalAttackPower);

        long defenderCount = combatCollection.getActiveEntities().stream()
                .filter(e -> e instanceof Defender)
                .count();
        System.out.println("Количество защитников: " + defenderCount);

        Optional<Combatable> strongestUnit = combatCollection.getActiveEntities().stream()
                .max(Comparator.comparingInt(Combatable::getAttack));
        strongestUnit.ifPresent(unit ->
                System.out.println("Самый сильный юнит: " + unit.getName() + " (атака: " + unit.getAttack() + ")"));
    }

    private static String getUpgradeableName(Upgradeable upgradeable) {
        if (upgradeable instanceof CastleElement) {
            return ((CastleElement) upgradeable).getName();
        } else if (upgradeable instanceof Defender) {
            return ((Defender) upgradeable).getName();
        } else if (upgradeable instanceof SiegeEquipment) {
            return ((SiegeEquipment) upgradeable).getName();
        }
        return upgradeable.getClass().getSimpleName();
    }
}