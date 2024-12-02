/**
 * Represents a craftable item with a recipe.
 *
 * @author Jiarui Xing
 */
public class CraftingItem extends Item implements Craftable {
    private final Recipe recipe;

    public CraftingItem(final String name, final String imagePath, final Recipe recipe)
    {
        super(name, imagePath);
        this.recipe = recipe;
    }

    @Override
    public Recipe getRecipe()
    {
        return recipe;
    }
}
