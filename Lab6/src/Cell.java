public class Cell {
    private Position position;
    private TerrainType terrainType;
    private GameEntity entity;
    private boolean isOccupied;
    private int height;

    public Cell(Position position, TerrainType terrainType) {
        this.position = position;
        this.terrainType = terrainType;
        this.entity = null;
        this.isOccupied = false;
        this.height = 0;
    }

    public boolean canMoveTo() {
        return terrainType.isPassable() && !isOccupied;
    }

    public double getMovementCost() {
        return terrainType.getMovementCost() + (height * 0.1);
    }

    public int getDefenseBonus() {
        return terrainType.getDefenseBonus();
    }

    public void setEntity(GameEntity entity) {
        this.entity = entity;
        this.isOccupied = (entity != null);
    }

    public void clearEntity() {
        this.entity = null;
        this.isOccupied = false;
    }

    public boolean isOccupied() { return isOccupied; }
    public Position getPosition() { return position; }
    public TerrainType getTerrainType() { return terrainType; }
    public GameEntity getEntity() { return entity; }
    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }
    public void setTerrainType(TerrainType terrainType) { this.terrainType = terrainType; }

    @Override
    public String toString() {
        if (isOccupied && entity instanceof SiegeUnit) {
            SiegeUnit unit = (SiegeUnit) entity;
            if (unit instanceof Knight) return unit.isAttacker() ? "K" : "k";
            if (unit instanceof Archer) return unit.isAttacker() ? "A" : "a";
            return "U";
        }

        // Упрощенные типы местности
        switch(terrainType) {
            case GRASS: return ".";
            case FOREST: return "F";
            case ROAD: return "R";
            case WATER: return "W";
            case WALL: return "█";
            case GATE: return "G";
            case TOWER: return "T";
            case BATTLEFIELD: return ".";
            default: return "?";
        }
    }
}