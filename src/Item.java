/**
 * Abstract base class representing a generic item in the game. Provides basic properties
 * like name and image path.
 *
 * @author Jiarui Xing
 */
public abstract class Item {
    protected final String name;
    protected final String imagePath;

    public Item(final String name, final String imagePath)
    {
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * Get the name of the item.
     *
     * @return item name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the image path of the item.
     *
     * @return image path
     */
    public String getImagePath()
    {
        return imagePath;
    }
}
