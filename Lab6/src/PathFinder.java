import java.util.*;

public class PathFinder {
    private final GameBoard gameBoard;

    public PathFinder(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public List<Position> findPath(Position start, Position target) {
        if (!gameBoard.isValidPosition(start) || !gameBoard.isValidPosition(target)) {
            return new ArrayList<>();
        }

        // Очередь для BFS
        Queue<Position> queue = new LinkedList<>();
        // Карта для отслеживания предыдущих позиций
        Map<Position, Position> cameFrom = new HashMap<>();
        // Множество посещенных позиций
        Set<Position> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            // Если достигли цели, восстанавливаем путь
            if (current.equals(target)) {
                return reconstructPath(cameFrom, current);
            }

            // Проверяем всех соседей (4 направления)
            for (Position neighbor : current.getNeighbors()) {
                if (!gameBoard.isValidPosition(neighbor) || visited.contains(neighbor)) {
                    continue;
                }

                Cell cell = gameBoard.getCell(neighbor);
                if (cell == null) continue;

                // Проверяем проходимость
                if (!cell.getTerrainType().isPassable()) continue;

                // Добавляем в очередь и отмечаем как посещенную
                queue.offer(neighbor);
                visited.add(neighbor);
                cameFrom.put(neighbor, current);
            }
        }

        return new ArrayList<>(); // Путь не найден
    }

    private List<Position> reconstructPath(Map<Position, Position> cameFrom, Position current) {
        List<Position> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            if (current != null) {
                path.add(0, current);
            }
        }

        return path;
    }
}