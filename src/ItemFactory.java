/**
 * Factory class for creating Item instances, including MaterialItem and CraftingItem.
 *
 * @author Jiarui Xing
 */
public class ItemFactory {

    /**
     * Create a MaterialItem instance.
     *
     * @param name      Item name
     * @param imagePath Image path
     * @return MaterialItem instance
     */
    public static MaterialItem createMaterialItem(final String name,
                                                  final String imagePath)
    {
        return new MaterialItem(name, imagePath);
    }

    /**
     * Create a CraftingItem instance.
     *
     * @param name      Item name
     * @param imagePath Image path
     * @param recipe    Crafting recipe
     * @return CraftingItem instance
     */
    public static CraftingItem createCraftingItem(final String name,
                                                  final String imagePath,
                                                  final Recipe recipe)
    {
        return new CraftingItem(name, imagePath, recipe);
    }
}
