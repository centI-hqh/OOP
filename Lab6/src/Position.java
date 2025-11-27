import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public double distanceTo(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int manhattanDistance(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public List<Position> getNeighbors() {
        List<Position> neighbors = new ArrayList<>();
        neighbors.add(new Position(x + 1, y));
        neighbors.add(new Position(x - 1, y));
        neighbors.add(new Position(x, y + 1));
        neighbors.add(new Position(x, y - 1));
        return neighbors;
    }

    public List<Position> getAllNeighbors() {
        List<Position> neighbors = new ArrayList<>();
        // 4 основных направления
        neighbors.add(new Position(x + 1, y));
        neighbors.add(new Position(x - 1, y));
        neighbors.add(new Position(x, y + 1));
        neighbors.add(new Position(x, y - 1));
        // 4 диагональных направления
        neighbors.add(new Position(x + 1, y + 1));
        neighbors.add(new Position(x + 1, y - 1));
        neighbors.add(new Position(x - 1, y + 1));
        neighbors.add(new Position(x - 1, y - 1));
        return neighbors;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}