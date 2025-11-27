import java.util.*;

public class MovementSystem {
    private final GameBoard gameBoard;
    private final PathFinder pathFinder;

    public MovementSystem(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.pathFinder = new PathFinder(gameBoard);
    }

    public boolean canMoveTo(SiegeUnit unit, Position target) {
        if (!gameBoard.isValidPosition(target)) return false;

        Cell targetCell = gameBoard.getCell(target);
        if (targetCell == null) return false;

        if (!targetCell.getTerrainType().isPassable()) return false;

        if (targetCell.isOccupied()) return false;

        double distance = unit.getPosition().distanceTo(target);
        return distance <= Math.sqrt(2);
    }

    public List<Position> getReachablePositions(SiegeUnit unit) {
        List<Position> reachable = new ArrayList<>();
        Position unitPos = unit.getPosition();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                Position testPos = new Position(unitPos.getX() + dx, unitPos.getY() + dy);
                if (canMoveTo(unit, testPos)) {
                    reachable.add(testPos);
                }
            }
        }

        return reachable;
    }

    public boolean moveUnit(SiegeUnit unit, Position target) {
        if (!canMoveTo(unit, target)) {
            return false;
        }

        Position oldPosition = unit.getPosition();
        gameBoard.removeEntity(oldPosition);
        boolean success = gameBoard.placeEntity(unit, target);

        if (success) {
            unit.setPosition(target);
            System.out.println(unit.getName() + " переместился с " + oldPosition + " на " + target);
            return true;
        } else {
            gameBoard.placeEntity(unit, oldPosition);
            return false;
        }
    }

    public boolean moveTowardTarget(SiegeUnit unit, Position target) {
        Position currentPos = unit.getPosition();
        List<Position> possibleMoves = getReachablePositions(unit);

        if (possibleMoves.isEmpty()) {
            System.out.println("Нет доступных ходов для " + unit.getName());
            return false;
        }

        Position bestMove = null;
        double bestDistance = Double.MAX_VALUE;

        for (Position move : possibleMoves) {
            double distance = move.distanceTo(target);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestMove = move;
            }
        }

        if (bestMove != null) {
            return moveUnit(unit, bestMove);
        }

        return false;
    }

    public boolean moveUnitAlongPath(SiegeUnit unit, Position target) {
        List<Position> path = pathFinder.findPath(unit.getPosition(), target);

        if (path.isEmpty()) {
            System.out.println("Путь к " + target + " не найден для " + unit.getName());
            return false;
        }

        if (path.size() > 1) {
            Position nextStep = path.get(1);
            return moveUnit(unit, nextStep);
        }

        return false;
    }

    public List<Position> getOptimalPath(SiegeUnit unit, Position target) {
        return pathFinder.findPath(unit.getPosition(), target);
    }

    public double getPathCost(List<Position> path) {
        if (path.isEmpty()) return 0;

        double totalCost = 0;
        for (Position pos : path) {
            Cell cell = gameBoard.getCell(pos);
            if (cell != null) {
                totalCost += cell.getMovementCost();
            }
        }

        return totalCost;
    }
}