import java.util.*;

/**
 * Represents a crafting recipe. Stores the required materials and their positions.
 *
 * @author Jiarui Xing
 */
public class Recipe {
    // Map of positions to item names
    private final Map<Position, Set<String>> recipeMap;

    public Recipe()
    {
        recipeMap = new HashMap<>();
    }

    /**
     * Add acceptable items to the recipe at a specific position.
     *
     * @param x         X-coordinate (-1, 0, 1)
     * @param y         Y-coordinate (-1, 0, 1)
     * @param itemNames Acceptable material item names
     */
    public void addItem(final int x, final int y, final String... itemNames)
    {
        final Set<String> items = new HashSet<>();
        Collections.addAll(items, itemNames);
        recipeMap.put(new Position(x, y), items);
    }

    /**
     * Get the recipe map.
     *
     * @return Map of Position to sets of acceptable item names
     */
    public Map<Position, Set<String>> getRecipeMap()
    {
        return recipeMap;
    }

    /**
     * Inner class representing a position on the crafting grid.
     */
    public static class Position {
        private final int x;
        private final int y;

        public Position(final int x, final int y)
        {
            if(x < -1 || x > 1 || y < -1 || y > 1)
            {
                throw new IllegalArgumentException("Position out of bounds.");
            }
            this.x = x;
            this.y = y;
        }

        /**
         * Get the X-coordinate.
         *
         * @return X-coordinate
         */
        public int getX()
        {
            return x;
        }

        /**
         * Get the Y-coordinate.
         *
         * @return Y-coordinate
         */
        public int getY()
        {
            return y;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
            {
                return true;
            }
            if(!(obj instanceof Position pos))
            {
                return false;
            }
            return this.x == pos.x && this.y == pos.y;
        }

        @Override
        public int hashCode()
        {
            return x * 31 + y;
        }
    }
}
