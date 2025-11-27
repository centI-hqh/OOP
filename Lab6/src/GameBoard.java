import java.util.*;

public class GameBoard {
    private final int width;
    private final int height;
    private final Cell[][] cells;
    private final Map<Position, GameEntity> entityMap;
    private Position castlePosition;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        this.entityMap = new HashMap<>();
        this.castlePosition = new Position(width/2, height/2);

        initializeSiegeBoard();
    }

    private void initializeSiegeBoard() {
        // Создаем базовое поле
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Position pos = new Position(x, y);
                TerrainType terrain = getSiegeTerrain(pos);
                cells[x][y] = new Cell(pos, terrain);
            }
        }

        // Создаем стены замка
        createCastleWalls();
    }

    private TerrainType getSiegeTerrain(Position pos) {
        double distanceToCastle = pos.distanceTo(castlePosition);
        double rand = Math.random();

        if (pos.getX() == 0 && pos.getY() == 0) {
            return TerrainType.GRASS; // Вместо воды на стартовой позиции
        }
        if (pos.getX() == 5 && pos.getY() == 2) {
            return TerrainType.GRASS; // Убираем воду с (5,2)
        }

        if (distanceToCastle < 3) {
            return TerrainType.GRASS;
        } else if (rand < 0.5) {
            return TerrainType.GRASS;
        } else if (rand < 0.7) {
            return TerrainType.FOREST;
        } else if (rand < 0.85) {
            return TerrainType.ROAD;
        } else {
            return TerrainType.WATER;
        }
    }

    private void createCastleWalls() {
        int castleRadius = 2;

        for (int x = castlePosition.getX() - castleRadius; x <= castlePosition.getX() + castleRadius; x++) {
            for (int y = castlePosition.getY() - castleRadius; y <= castlePosition.getY() + castleRadius; y++) {
                if (isValidPosition(new Position(x, y))) {
                    // Стены по периметру
                    if (x == castlePosition.getX() - castleRadius || x == castlePosition.getX() + castleRadius ||
                            y == castlePosition.getY() - castleRadius || y == castlePosition.getY() + castleRadius) {
                        cells[x][y].setTerrainType(TerrainType.WALL);
                    }
                    // Башни по углам
                    if ((x == castlePosition.getX() - castleRadius && y == castlePosition.getY() - castleRadius) ||
                            (x == castlePosition.getX() + castleRadius && y == castlePosition.getY() - castleRadius) ||
                            (x == castlePosition.getX() - castleRadius && y == castlePosition.getY() + castleRadius) ||
                            (x == castlePosition.getX() + castleRadius && y == castlePosition.getY() + castleRadius)) {
                        cells[x][y].setTerrainType(TerrainType.TOWER);
                    }
                }
            }
        }

        // Ворота
        cells[castlePosition.getX()][castlePosition.getY() - castleRadius].setTerrainType(TerrainType.GATE);
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < width &&
                position.getY() >= 0 && position.getY() < height;
    }

    public Cell getCell(Position position) {
        if (!isValidPosition(position)) return null;
        return cells[position.getX()][position.getY()];
    }

    public boolean placeEntity(GameEntity entity, Position position) {
        if (!isValidPosition(position)) return false;

        Cell cell = getCell(position);
        if (cell == null || !cell.canMoveTo()) return false;

        cell.setEntity(entity);
        entityMap.put(position, entity);
        return true;
    }

    public void removeEntity(Position position) {
        if (!isValidPosition(position)) return;

        Cell cell = getCell(position);
        if (cell != null) {
            cell.clearEntity();
            entityMap.remove(position);
        }
    }

    public boolean moveEntity(GameEntity entity, Position newPosition) {
        Position oldPosition = entity.getPosition();

        if (!isValidPosition(newPosition)) return false;

        Cell newCell = getCell(newPosition);
        if (newCell == null || !newCell.canMoveTo()) return false;

        // Удаляем со старой позиции
        removeEntity(oldPosition);

        // Размещаем на новой позиции
        return placeEntity(entity, newPosition);
    }

    public List<Position> getCastlePositions() {
        List<Position> castlePositions = new ArrayList<>();
        int castleRadius = 2;

        for (int x = castlePosition.getX() - castleRadius; x <= castlePosition.getX() + castleRadius; x++) {
            for (int y = castlePosition.getY() - castleRadius; y <= castlePosition.getY() + castleRadius; y++) {
                Position pos = new Position(x, y);
                if (isValidPosition(pos)) {
                    castlePositions.add(pos);
                }
            }
        }

        return castlePositions;
    }

    public Position getCastlePosition() { return castlePosition; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Map<Position, GameEntity> getEntityMap() { return entityMap; }
}