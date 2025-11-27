import java.util.*;

public class GameManager {
    private List<Combatable> combatEntities;
    private List<Upgradeable> upgradeableEntities;
    private List<Producible> productionEntities;
    private List<Movable> movableEntities;
    private List<Repairable> repairableEntities;

    public GameManager() {
        this.combatEntities = new ArrayList<>();
        this.upgradeableEntities = new ArrayList<>();
        this.productionEntities = new ArrayList<>();
        this.movableEntities = new ArrayList<>();
        this.repairableEntities = new ArrayList<>();
    }

    // Геттеры
    public List<Combatable> getCombatEntities() {
        return new ArrayList<>(combatEntities);
    }

    public List<Upgradeable> getUpgradeableEntities() {
        return new ArrayList<>(upgradeableEntities);
    }

    public List<Producible> getProductionEntities() {

        return new ArrayList<>(productionEntities);
    }

    public List<Movable> getMovableEntities() {

        return new ArrayList<>(movableEntities);
    }

    public List<Repairable> getRepairableEntities() {

        return new ArrayList<>(repairableEntities);
    }

    public void addCombatable(Combatable combatable) {

        combatEntities.add(combatable);
    }

    public void addUpgradeable(Upgradeable upgradeable) {

        upgradeableEntities.add(upgradeable);
    }

    public void addProducible(Producible producible) {

        productionEntities.add(producible);
    }

    public void addMovable(Movable movable) {

        movableEntities.add(movable);
    }

    public void addRepairable(Repairable repairable) {

        repairableEntities.add(repairable);
    }


    public void performCombatRound() {
        System.out.println("\n БОЕВОЙ РАУНД ");
        for (Combatable entity : combatEntities) {
            if (entity.isAlive()) {
                // Находим живую цель
                Combatable target = findAliveTarget(entity);
                if (target != null) {
                    entity.attack(target);
                }
            }
        }
    }

    public void upgradeAll() {
        System.out.println("\n УЛУЧШЕНИЕ ");
        int upgradedCount = 0;
        for (Upgradeable entity : upgradeableEntities) {
            if (entity.canUpgrade()) {
                if (entity.upgrade()) {
                    upgradedCount++;
                }
            }
        }
        System.out.println("Улучшено объектов: " + upgradedCount);
    }

    public void produceAll() {
        System.out.println("\n ПРОИЗВОДСТВО ");
        int totalProduction = 0;
        for (Producible entity : productionEntities) {
            if (entity.canProduce()) {
                totalProduction += entity.produce();
            }
        }
        System.out.println("Всего произведено: " + totalProduction + " единиц");
    }

    public void moveAllTo(int x, int y) {
        System.out.println("\n ПЕРЕМЕЩЕНИЕ ");
        int movedCount = 0;
        for (Movable entity : movableEntities) {
            if (entity.canMoveTo(x, y)) {
                if (entity.moveTo(x, y)) {
                    movedCount++;
                }
            }
        }
        System.out.println("Перемещено объектов: " + movedCount);
    }

    public void repairAll() {
        System.out.println("\n=== РЕМОНТ ===");
        int repairedCount = 0;
        for (Repairable entity : repairableEntities) {
            if (entity.canBeRepaired()) {
                if (entity.repair(25)) {
                    repairedCount++;
                }
            }
        }
        System.out.println("Отремонтировано объектов: " + repairedCount);
    }

    // Сортировка
    public void sortCombatantsByHealth() {
        List<CastleElement> castleElements = new ArrayList<>();
        List<SiegeEquipment> siegeEquipments = new ArrayList<>();
        List<Defender> defenders = new ArrayList<>();

        for (Combatable combat : combatEntities) {
            if (combat instanceof CastleElement) {
                castleElements.add((CastleElement) combat);
            } else if (combat instanceof SiegeEquipment) {
                siegeEquipments.add((SiegeEquipment) combat);
            } else if (combat instanceof Defender) {
                defenders.add((Defender) combat);
            }
        }

        Collections.sort(castleElements);
        Collections.sort(siegeEquipments);
        Collections.sort(defenders);

        System.out.println("\n СОРТИРОВКА ПО ЗДОРОВЬЮ ");
        System.out.println("Элементы замка:");
        for (CastleElement element : castleElements) {
            System.out.println("  " + element.getName() + ": " + element.getHealth() + " HP");
        }
        System.out.println("Осадные орудия:");
        for (SiegeEquipment equipment : siegeEquipments) {
            System.out.println("  " + equipment.getName() + ": " + equipment.getHealth() + " HP");
        }
        System.out.println("Защитники:");
        for (Defender defender : defenders) {
            System.out.println("  " + defender.getName() + ": " + defender.getHealth() + " HP");
        }
    }


    private Combatable findAliveTarget(Combatable attacker) {
        for (Combatable target : combatEntities) {
            if (target != attacker && target.isAlive()) {
                return target;
            }
        }
        return null;
    }

    public void displayStatus() {
        System.out.println("\n СТАТУС ИГРЫ ");
        System.out.println("Боевые единицы: " + combatEntities.size());
        System.out.println("Улучшаемые: " + upgradeableEntities.size());
        System.out.println("Производящие: " + productionEntities.size());
        System.out.println("Перемещаемые: " + movableEntities.size());
        System.out.println("Ремонтируемые: " + repairableEntities.size());

        // Статистика по живым/мертвым
        int aliveCount = 0;
        for (Combatable combat : combatEntities) {
            if (combat.isAlive()) {
                aliveCount++;
            }
        }
        System.out.println("Живых боевых единиц: " + aliveCount + "/" + combatEntities.size());
    }


    public int getAliveCombatCount() {
        int count = 0;
        for (Combatable combat : combatEntities) {
            if (combat.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalUpgradeCost() {
        int totalCost = 0;
        for (Upgradeable entity : upgradeableEntities) {
            if (entity.canUpgrade()) {
                totalCost += entity.getUpgradeCost();
            }
        }
        return totalCost;
    }

    public int getTotalRepairCost() {
        int totalCost = 0;
        for (Repairable entity : repairableEntities) {
            if (entity.canBeRepaired()) {
                totalCost += entity.getRepairCost();
            }
        }
        return totalCost;
    }

    public void displayDetailedInfo() {
        System.out.println("\n ДЕТАЛЬНАЯ ИНФОРМАЦИЯ ");

        System.out.println("Боевые сущности:");
        for (Combatable combat : combatEntities) {
            String status = combat.isAlive() ? "ЦЕЛ" : "УНИЧТОЖЕН";
            System.out.println("  " + status + " " + getEntityName(combat) +
                    " [ATK:" + combat.getAttack() + " DEF:" + combat.getDefense() + "]");
        }

        System.out.println("\nСостояние улучшений:");
        for (Upgradeable upgradeable : upgradeableEntities) {
            System.out.println("  " + getEntityName(upgradeable) +
                    " Ур." + upgradeable.getLevel() +
                    " (" + (upgradeable.canUpgrade() ? "Можно улучшить" : "Макс.уровень") + ")");
        }
    }

    private String getEntityName(Object entity) {
        if (entity instanceof CastleElement) {
            return ((CastleElement) entity).getName();
        } else if (entity instanceof SiegeEquipment) {
            return ((SiegeEquipment) entity).getName();
        } else if (entity instanceof Defender) {
            return ((Defender) entity).getName();
        }
        return "Неизвестный объект";
    }
}