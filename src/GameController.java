import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.*;

/**
 * Controls the game logic, handles player actions, and updates the game state.
 *
 * @author Jiarui Xing
 */
public class GameController {
    private static GameController instance;

    private int totalQuestions = 10;

    private int score = 0;
    private int currentQuestion = 0;
    private CraftingItem currentItem;
    private List<Item> optionItems;
    private final Map<Recipe.Position, String> playerRecipe;

    private final List<CraftingItem> craftingItems;
    private final List<MaterialItem> materialItems;
    private List<CraftingItem> availableItems;

    // Game statistics
    private int gamesPlayed = 0;
    private int totalScore = 0;

    private final MinecraftItem gameUi;

    // Track attempts left for the current question
    private int attemptsLeft = 2;

    // Private constructor for Singleton pattern
    private GameController(final MinecraftItem gameUi)
    {
        this.gameUi = gameUi;
        craftingItems = new ArrayList<>();
        materialItems = new ArrayList<>();
        playerRecipe = new HashMap<>();

        loadItems();
    }

    /**
     * Get the singleton instance of GameController.
     *
     * @param gameUi The MinecraftItem game UI instance
     * @return The singleton GameController instance
     */
    public static synchronized GameController getInstance(final MinecraftItem gameUi)
    {
        if(instance == null)
        {
            instance = new GameController(gameUi);
        }
        return instance;
    }

    /**
     * Load items and recipes.
     */
    private void loadItems()
    {
        // Load material items using ItemFactory
        materialItems.add(
                ItemFactory.createMaterialItem("Wood Planks", "images/wood_planks.png"));
        materialItems.add(ItemFactory.createMaterialItem("Stick", "images/stick.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Diamond", "images/diamond.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Iron Ingot", "images/iron_ingot.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Gold Ingot", "images/gold_ingot.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Cobblestone", "images/cobblestone.png"));
        materialItems.add(ItemFactory.createMaterialItem("String", "images/string.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Feather", "images/feather.png"));
        materialItems.add(ItemFactory.createMaterialItem("Flint", "images/flint.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Leather", "images/leather.png"));
        materialItems.add(ItemFactory.createMaterialItem("Paper", "images/paper.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Gunpowder", "images/gunpowder.png"));
        materialItems.add(ItemFactory.createMaterialItem("Sand", "images/sand.png"));
        materialItems.add(ItemFactory.createMaterialItem("Glass", "images/glass.png"));
        materialItems.add(ItemFactory.createMaterialItem("Redstone Dust",
                "images/redstone_dust" + ".png"));
        materialItems.add(ItemFactory.createMaterialItem("Lapis Lazuli",
                "images/lapis_lazuli.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Emerald", "images/emerald.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Obsidian", "images/obsidian.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Ender Pearl", "images/ender_pearl.png"));
        materialItems.add(ItemFactory.createMaterialItem("Book", "images/book.png"));
        materialItems.add(ItemFactory.createMaterialItem("Eye of Ender",
                "images/eye_of_ender.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Ghast Tear", "images/ghast_tear.png"));
        materialItems.add(ItemFactory.createMaterialItem("Amethyst Shard",
                "images/amethyst_shard.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Andesite", "images/andesite.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Blaze Rod", "images/blaze_rod.png"));
        materialItems.add(ItemFactory.createMaterialItem("Chiseled Stone Brick",
                "images/chiseled_stone_brick.png"));
        materialItems.add(ItemFactory.createMaterialItem("Copper Ingot",
                "images/copper_ingot.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Diorite", "images/diorite.png"));
        materialItems.add(ItemFactory.createMaterialItem("Egg", "images/egg.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Granite", "images/granite.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Honeycomb", "images/honeycomb.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Magma Cream", "images/magma_cream.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Milk Bucket", "images/milk_bucket.png"));
        materialItems.add(ItemFactory.createMaterialItem("Nether Quartz",
                "images/nether_quartz.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Nether Star", "images/nether_star.gif"));
        materialItems.add(ItemFactory.createMaterialItem("Netherite Ingot",
                "images/netherite_ingot.png"));
        materialItems.add(ItemFactory.createMaterialItem("Netherite Scrap",
                "images/netherite_scrap.png"));
        materialItems.add(ItemFactory.createMaterialItem("Popped Chorus Fruit",
                "images/popped_chorus_fruit.png"));
        materialItems.add(ItemFactory.createMaterialItem("Purpur Block",
                "images/purpur_block.png"));
        materialItems.add(ItemFactory.createMaterialItem("Purpur Pillar",
                "images/purpur_pillar.png"));
        materialItems.add(
                ItemFactory.createMaterialItem("Purpur Slab", "images/purpur_slab.png"));
        materialItems.add(ItemFactory.createMaterialItem("Redstone Torch",
                "images/redstone_torch.png"));
        materialItems.add(ItemFactory.createMaterialItem("Sculk Sensor",
                "images/sculk_sensor.gif"));
        materialItems.add(
                ItemFactory.createMaterialItem("Slimeball", "images/slimeball.png"));
        materialItems.add(ItemFactory.createMaterialItem("Stone", "images/stone.png"));
        materialItems.add(ItemFactory.createMaterialItem("Stone Brick Slab",
                "images/stone_brick_slab.png"));
        materialItems.add(ItemFactory.createMaterialItem("Sugar", "images/sugar.png"));
        materialItems.add(ItemFactory.createMaterialItem("Wheat", "images/wheat.png"));


        // Load crafting items with recipes using ItemFactory
        // Stick
        final Recipe stickRecipe = new Recipe();
        stickRecipe.addItem(0, 0, "Wood Planks");
        stickRecipe.addItem(0, -1, "Wood Planks");
        craftingItems.add(
                ItemFactory.createCraftingItem("Stick", "images/stick.png", stickRecipe));

        // Diamond Axe
        final Recipe diamondAxeRecipe = new Recipe();
        diamondAxeRecipe.addItem(0, 1, "Diamond");
        diamondAxeRecipe.addItem(1, 1, "Diamond");
        diamondAxeRecipe.addItem(1, 0, "Diamond");
        diamondAxeRecipe.addItem(0, 0, "Stick");
        diamondAxeRecipe.addItem(0, -1, "Stick");
        craftingItems.add(
                ItemFactory.createCraftingItem("Diamond Axe", "images/diamond_axe.png",
                        diamondAxeRecipe));

        // Iron Sword
        final Recipe ironSwordRecipe = new Recipe();
        ironSwordRecipe.addItem(0, 1, "Iron Ingot");
        ironSwordRecipe.addItem(0, 0, "Iron Ingot");
        ironSwordRecipe.addItem(0, -1, "Stick");
        craftingItems.add(
                ItemFactory.createCraftingItem("Iron Sword", "images/iron_sword.png",
                        ironSwordRecipe));

        // Bow
        final Recipe bowRecipe = new Recipe();
        bowRecipe.addItem(-1, 0, "Stick");
        bowRecipe.addItem(0, 1, "Stick");
        bowRecipe.addItem(0, -1, "Stick");
        bowRecipe.addItem(1, 1, "String");
        bowRecipe.addItem(1, 0, "String");
        bowRecipe.addItem(1, -1, "String");
        craftingItems.add(
                ItemFactory.createCraftingItem("Bow", "images/bow.png", bowRecipe));

        // Arrow
        final Recipe arrowRecipe = new Recipe();
        arrowRecipe.addItem(0, 1, "Flint");
        arrowRecipe.addItem(0, 0, "Stick");
        arrowRecipe.addItem(0, -1, "Feather");
        craftingItems.add(
                ItemFactory.createCraftingItem("Arrow", "images/arrow.png", arrowRecipe));

        // Enchanting Table
        final Recipe enchantingTableRecipe = new Recipe();
        enchantingTableRecipe.addItem(-1, -1, "Obsidian");
        enchantingTableRecipe.addItem(-1, 0, "Diamond");
        enchantingTableRecipe.addItem(0, -1, "Obsidian");
        enchantingTableRecipe.addItem(0, 0, "Obsidian");
        enchantingTableRecipe.addItem(0, 1, "Book");
        enchantingTableRecipe.addItem(1, -1, "Obsidian");
        enchantingTableRecipe.addItem(1, 0, "Diamond");
        craftingItems.add(ItemFactory.createCraftingItem("Enchanting Table",
                "images/enchanting_table.gif", enchantingTableRecipe));

        // Andesite
        final Recipe andesiteRecipe = new Recipe();
        andesiteRecipe.addItem(-1, 0, "Diorite");
        andesiteRecipe.addItem(0, 0, "Cobblestone");
        craftingItems.add(
                ItemFactory.createCraftingItem("Andesite", "images/andesite.png",
                        andesiteRecipe));

        // Beacon
        final Recipe beaconRecipe = new Recipe();
        beaconRecipe.addItem(-1, -1, "Obsidian");
        beaconRecipe.addItem(-1, 0, "Glass");
        beaconRecipe.addItem(-1, 1, "Glass");
        beaconRecipe.addItem(0, -1, "Obsidian");
        beaconRecipe.addItem(0, 0, "Nether Star");
        beaconRecipe.addItem(0, 1, "Glass");
        beaconRecipe.addItem(1, -1, "Obsidian");
        beaconRecipe.addItem(1, 0, "Glass");
        beaconRecipe.addItem(1, 1, "Glass");
        craftingItems.add(ItemFactory.createCraftingItem("Beacon", "images/beacon.png",
                beaconRecipe));

        // Beehive
        final Recipe beehiveRecipe = new Recipe();
        beehiveRecipe.addItem(-1, -1, "Wood Planks");
        beehiveRecipe.addItem(-1, 0, "Honeycomb");
        beehiveRecipe.addItem(-1, 1, "Wood Planks");
        beehiveRecipe.addItem(0, -1, "Wood Planks");
        beehiveRecipe.addItem(0, 0, "Honeycomb");
        beehiveRecipe.addItem(0, 1, "Wood Planks");
        beehiveRecipe.addItem(1, -1, "Wood Planks");
        beehiveRecipe.addItem(1, 0, "Honeycomb");
        beehiveRecipe.addItem(1, 1, "Wood Planks");
        craftingItems.add(ItemFactory.createCraftingItem("Beehive", "images/beehive.png",
                beehiveRecipe));

        // Cake
        final Recipe cakeRecipe = new Recipe();
        cakeRecipe.addItem(-1, -1, "Wheat");
        cakeRecipe.addItem(-1, 0, "Sugar");
        cakeRecipe.addItem(-1, 1, "Milk Bucket");
        cakeRecipe.addItem(0, -1, "Wheat");
        cakeRecipe.addItem(0, 0, "Egg");
        cakeRecipe.addItem(0, 1, "Milk Bucket");
        cakeRecipe.addItem(1, -1, "Wheat");
        cakeRecipe.addItem(1, 0, "Sugar");
        cakeRecipe.addItem(1, 1, "Milk Bucket");
        craftingItems.add(
                ItemFactory.createCraftingItem("Cake", "images/cake.png", cakeRecipe));

        // Calibrated Sculk Sensor
        final Recipe calibratedSculkSensorRecipe = new Recipe();
        calibratedSculkSensorRecipe.addItem(-1, 0, "Amethyst Shard");
        calibratedSculkSensorRecipe.addItem(0, 0, "Sculk Sensor");
        calibratedSculkSensorRecipe.addItem(0, 1, "Amethyst Shard");
        calibratedSculkSensorRecipe.addItem(1, 0, "Amethyst Shard");
        craftingItems.add(ItemFactory.createCraftingItem("Calibrated Sculk Sensor",
                "images/calibrated_sculk_sensor.gif", calibratedSculkSensorRecipe));

        // Candle
        final Recipe candleRecipe = new Recipe();
        candleRecipe.addItem(0, 0, "Honeycomb");
        candleRecipe.addItem(0, 1, "String");
        craftingItems.add(ItemFactory.createCraftingItem("Candle", "images/candle.png",
                candleRecipe));

        // Chiseled Stone Brick
        final Recipe chiseledStoneBrickRecipe = new Recipe();
        chiseledStoneBrickRecipe.addItem(0, 0, "Stone Brick Slab");
        chiseledStoneBrickRecipe.addItem(0, -1, "Stone Brick Slab");
        craftingItems.add(ItemFactory.createCraftingItem("Chiseled Stone Brick",
                "images/chiseled_stone_brick.png", chiseledStoneBrickRecipe));

        // Diorite
        final Recipe dioriteRecipe = new Recipe();
        dioriteRecipe.addItem(-1, -1, "Nether Quartz");
        dioriteRecipe.addItem(-1, 0, "Cobblestone");
        dioriteRecipe.addItem(0, -1, "Cobblestone");
        dioriteRecipe.addItem(0, 0, "Nether Quartz");
        craftingItems.add(ItemFactory.createCraftingItem("Diorite", "images/diorite.png",
                dioriteRecipe));

        // End Rod
        final Recipe endRodRecipe = new Recipe();
        endRodRecipe.addItem(0, -1, "Popped Chorus Fruit");
        endRodRecipe.addItem(0, 0, "Blaze Rod");
        craftingItems.add(ItemFactory.createCraftingItem("End Rod", "images/end_rod.png",
                endRodRecipe));

        // Granite
        final Recipe graniteRecipe = new Recipe();
        graniteRecipe.addItem(-1, 0, "Diorite");
        graniteRecipe.addItem(0, 0, "Nether Quartz");
        craftingItems.add(ItemFactory.createCraftingItem("Granite", "images/granite.png",
                graniteRecipe));

        // Lead
        final Recipe leadRecipe = new Recipe();
        leadRecipe.addItem(-1, 0, "String");
        leadRecipe.addItem(-1, 1, "String");
        leadRecipe.addItem(0, 0, "Slimeball");
        leadRecipe.addItem(0, 1, "String");
        leadRecipe.addItem(1, -1, "String");
        craftingItems.add(
                ItemFactory.createCraftingItem("Lead", "images/lead.png", leadRecipe));

        // Lodestone
        final Recipe lodestoneRecipe = new Recipe();
        lodestoneRecipe.addItem(-1, -1, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(-1, 0, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(-1, 1, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(0, -1, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(0, 0, "Netherite Ingot");
        lodestoneRecipe.addItem(0, 1, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(1, -1, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(1, 0, "Chiseled Stone Brick");
        lodestoneRecipe.addItem(1, 1, "Chiseled Stone Brick");
        craftingItems.add(
                ItemFactory.createCraftingItem("Lodestone", "images/lodestone.png",
                        lodestoneRecipe));

        // Magma Block
        final Recipe magmaBlockRecipe = new Recipe();
        magmaBlockRecipe.addItem(-1, -1, "Magma Cream");
        magmaBlockRecipe.addItem(-1, 0, "Magma Cream");
        magmaBlockRecipe.addItem(0, -1, "Magma Cream");
        magmaBlockRecipe.addItem(0, 0, "Magma Cream");
        craftingItems.add(
                ItemFactory.createCraftingItem("Magma Block", "images/magma_block.gif",
                        magmaBlockRecipe));

        // Netherite Ingot
        final Recipe netheriteIngotRecipe = new Recipe();
        netheriteIngotRecipe.addItem(-1, -1, "Gold Ingot");
        netheriteIngotRecipe.addItem(-1, 0, "Netherite Scrap");
        netheriteIngotRecipe.addItem(-1, 1, "Netherite Scrap");
        netheriteIngotRecipe.addItem(0, -1, "Gold Ingot");
        netheriteIngotRecipe.addItem(0, 0, "Gold Ingot");
        netheriteIngotRecipe.addItem(0, 1, "Netherite Scrap");
        netheriteIngotRecipe.addItem(1, 0, "Gold Ingot");
        netheriteIngotRecipe.addItem(1, 1, "Netherite Scrap");
        craftingItems.add(ItemFactory.createCraftingItem("Netherite Ingot",
                "images/netherite_ingot.png", netheriteIngotRecipe));

        // Observer
        final Recipe observerRecipe = new Recipe();
        observerRecipe.addItem(-1, -1, "Cobblestone");
        observerRecipe.addItem(-1, 0, "Redstone Dust");
        observerRecipe.addItem(-1, 1, "Cobblestone");
        observerRecipe.addItem(0, -1, "Cobblestone");
        observerRecipe.addItem(0, 0, "Redstone Dust");
        observerRecipe.addItem(0, 1, "Cobblestone");
        observerRecipe.addItem(1, -1, "Cobblestone");
        observerRecipe.addItem(1, 0, "Nether Quartz");
        observerRecipe.addItem(1, 1, "Cobblestone");
        craftingItems.add(
                ItemFactory.createCraftingItem("Observer", "images/observer.png",
                        observerRecipe));

        // Piston
        final Recipe pistonRecipe = new Recipe();
        pistonRecipe.addItem(-1, -1, "Cobblestone");
        pistonRecipe.addItem(-1, 0, "Cobblestone");
        pistonRecipe.addItem(-1, 1, "Wood Planks");
        pistonRecipe.addItem(0, -1, "Redstone Dust");
        pistonRecipe.addItem(0, 0, "Iron Ingot");
        pistonRecipe.addItem(0, 1, "Wood Planks");
        pistonRecipe.addItem(1, -1, "Cobblestone");
        pistonRecipe.addItem(1, 0, "Cobblestone");
        pistonRecipe.addItem(1, 1, "Wood Planks");
        craftingItems.add(ItemFactory.createCraftingItem("Piston", "images/piston.gif",
                pistonRecipe));

        // Purpur Block
        final Recipe purpurBlockRecipe = new Recipe();
        purpurBlockRecipe.addItem(-1, -1, "Popped Chorus Fruit");
        purpurBlockRecipe.addItem(-1, 0, "Popped Chorus Fruit");
        purpurBlockRecipe.addItem(0, -1, "Popped Chorus Fruit");
        purpurBlockRecipe.addItem(0, 0, "Popped Chorus Fruit");
        craftingItems.add(
                ItemFactory.createCraftingItem("Purpur Block", "images/purpur_block.png",
                        purpurBlockRecipe));

        // Purpur Pillar
        final Recipe purpurPillarRecipe = new Recipe();
        purpurPillarRecipe.addItem(0, -1, "Purpur Slab");
        purpurPillarRecipe.addItem(0, 0, "Purpur Slab");
        craftingItems.add(ItemFactory.createCraftingItem("Purpur Pillar",
                "images/purpur_pillar.png", purpurPillarRecipe));

        // Purpur Slab
        final Recipe purpurSlabRecipe = new Recipe();
        purpurSlabRecipe.addItem(-1, -1, "Purpur Block", "Purpur Pillar");
        purpurSlabRecipe.addItem(0, -1, "Purpur Block", "Purpur Pillar");
        purpurSlabRecipe.addItem(1, -1, "Purpur Block", "Purpur Pillar");
        craftingItems.add(
                ItemFactory.createCraftingItem("Purpur Slab", "images/purpur_slab.png",
                        purpurSlabRecipe));

        // Redstone Comparator
        final Recipe redstoneComparatorRecipe = new Recipe();
        redstoneComparatorRecipe.addItem(-1, -1, "Stone");
        redstoneComparatorRecipe.addItem(-1, 0, "Redstone Torch");
        redstoneComparatorRecipe.addItem(0, -1, "Stone");
        redstoneComparatorRecipe.addItem(0, 0, "Nether Quartz");
        redstoneComparatorRecipe.addItem(0, 1, "Redstone Torch");
        redstoneComparatorRecipe.addItem(1, -1, "Stone");
        redstoneComparatorRecipe.addItem(1, 0, "Redstone Torch");
        craftingItems.add(
                ItemFactory.createCraftingItem("Redstone Comparator",
                        "images/redstone_comparator.png", redstoneComparatorRecipe));

        // Redstone Repeater
        final Recipe redstoneRepeaterRecipe = new Recipe();
        redstoneRepeaterRecipe.addItem(-1, -1, "Stone");
        redstoneRepeaterRecipe.addItem(-1, 0, "Redstone Torch");
        redstoneRepeaterRecipe.addItem(0, -1, "Stone");
        redstoneRepeaterRecipe.addItem(0, 0, "Redstone Dust");
        redstoneRepeaterRecipe.addItem(1, -1, "Stone");
        redstoneRepeaterRecipe.addItem(1, 0, "Redstone Torch");
        craftingItems.add(
                ItemFactory.createCraftingItem("Redstone Repeater",
                        "images/redstone_repeater.png", redstoneRepeaterRecipe));

        // Redstone Torch
        final Recipe redstoneTorchRecipe = new Recipe();
        redstoneTorchRecipe.addItem(0, -1, "Stick");
        redstoneTorchRecipe.addItem(0, 0, "Redstone Dust");
        craftingItems.add(
                ItemFactory.createCraftingItem("Redstone Torch",
                        "images/redstone_torch.png", redstoneTorchRecipe));

        // Spyglass
        final Recipe SpyglassRecipe = new Recipe();
        SpyglassRecipe.addItem(0, -1, "Copper Ingot");
        SpyglassRecipe.addItem(0, 0, "Copper Ingot");
        SpyglassRecipe.addItem(0, 1, "Amethyst Shard");
        craftingItems.add(
                ItemFactory.createCraftingItem("Spyglass",
                        "images/spyglass.png", SpyglassRecipe));

        // Tinted Glass
        final Recipe tintedGlassRecipe = new Recipe();
        tintedGlassRecipe.addItem(-1, 0, "Amethyst Shard");
        tintedGlassRecipe.addItem(0, -1, "Amethyst Shard");
        tintedGlassRecipe.addItem(0, 0, "Glass");
        tintedGlassRecipe.addItem(0, 1, "Amethyst Shard");
        tintedGlassRecipe.addItem(1, 0, "Amethyst Shard");
        craftingItems.add(
                ItemFactory.createCraftingItem("Tinted Glass",
                        "images/tinted_glass.png", tintedGlassRecipe));

        // TNT
        final Recipe tntRecipe = new Recipe();
        tntRecipe.addItem(-1, -1, "Gunpowder");
        tntRecipe.addItem(-1, 0, "Sand");
        tntRecipe.addItem(-1, 1, "Gunpowder");
        tntRecipe.addItem(0, -1, "Sand");
        tntRecipe.addItem(0, 0, "Gunpowder");
        tntRecipe.addItem(0, 1, "Sand");
        tntRecipe.addItem(1, -1, "Gunpowder");
        tntRecipe.addItem(1, 0, "Sand");
        tntRecipe.addItem(1, 1, "Gunpowder");
        craftingItems.add(ItemFactory.createCraftingItem("TNT", "images/tnt.png",
                tntRecipe));
    }

    /**
     * Start the game by generating the first question.
     */
    public void startGame()
    {
        gamesPlayed++;
        score = 0;
        currentQuestion = 0;

        // Initialize available items and shuffle
        availableItems = new ArrayList<>(craftingItems);
        Collections.shuffle(availableItems);

        // Set totalQuestions to min(10, available items)
        totalQuestions = Math.min(10, availableItems.size());

        nextQuestion();
    }

    /**
     * Generate the next question.
     */
    public void nextQuestion()
    {
        if(currentQuestion >= totalQuestions || availableItems.isEmpty())
        {
            endGame();
            return;
        }

        currentQuestion++;
        // Reset attempts
        attemptsLeft = 2;

        // Randomly select a crafting item
        currentItem = availableItems.remove(0);

        // Generate options
        generateOptions();

        // Reset player recipe
        playerRecipe.clear();

        // Update the UI accordingly
        gameUi.updateUi();
    }

    /**
     * Generate options including correct materials and distractors.
     */
    private void generateOptions()
    {
        optionItems = new ArrayList<>();
        final Set<String> materialNames = new HashSet<>();

        // Add correct materials
        for(final Set<String> itemNames : currentItem.getRecipe().getRecipeMap().values())
        {
            for(final String itemName : itemNames)
            {
                if(!materialNames.contains(itemName))
                {
                    materialNames.add(itemName);
                    optionItems.add(getMaterialItemByName(itemName));
                }
            }
        }

        // Add distractor materials
        final Random rand = new Random();
        while(optionItems.size() < 9 && optionItems.size() < materialItems.size())
        {
            final MaterialItem material =
                    materialItems.get(rand.nextInt(materialItems.size()));
            if(!materialNames.contains(material.getName()))
            {
                materialNames.add(material.getName());
                optionItems.add(material);
            }
        }

        // Shuffle options
        Collections.shuffle(optionItems);
    }

    /**
     * Get a material item by its name.
     *
     * @param name Material item name
     * @return MaterialItem object
     */
    private MaterialItem getMaterialItemByName(final String name)
    {
        for(final MaterialItem item : materialItems)
        {
            if(item.getName().equals(name))
            {
                return item;
            }
        }
        return null;
    }

    /**
     * Handle the player's action when they place an item on the crafting grid.
     *
     * @param gridX    Grid X-coordinate (0 to 2)
     * @param gridY    Grid Y-coordinate (0 to 2)
     * @param itemName Name of the item placed
     */
    public void placeItemOnGrid(final int gridX, final int gridY, final String itemName)
    {
        // Convert grid coordinates to recipe positions (-1, 0, 1)
        final int x = gridX - 1;
        final int y = 1 - gridY;

        final Recipe.Position position = new Recipe.Position(x, y);
        if(itemName != null)
        {
            playerRecipe.put(position, itemName);
        } else
        {
            playerRecipe.remove(position);
        }
    }

    /**
     * Submit the player's recipe and check if it matches the correct recipe.
     */
    public void submitRecipe()
    {
        final boolean isCorrect = checkRecipe();
        if(isCorrect)
        {
            score++;
            gameUi.displayFeedback(true, false);
        } else
        {
            attemptsLeft--;
            gameUi.displayFeedback(false, attemptsLeft > 0);
            if(attemptsLeft <= 0)
            {
                nextQuestion();
            }
        }
    }

    /**
     * Skip the current question. The question is not counted towards the score.
     */
    public void skipQuestion()
    {
        nextQuestion();
    }

    /**
     * Check if the player's recipe matches the correct recipe, considering allowed
     * transformations.
     *
     * @return True if correct, False otherwise
     */
    private boolean checkRecipe()
    {
        final Map<Recipe.Position, Set<String>> correctRecipe =
                currentItem.getRecipe().getRecipeMap();

        // Generate all allowed transformations of the correct recipe
        final List<Map<Recipe.Position, Set<String>>> transformedRecipes =
                generateTransformedRecipes(correctRecipe);

        // Compare player's recipe with each transformed recipe
        for(final Map<Recipe.Position, Set<String>> transformedRecipe :
                transformedRecipes)
        {
            if(recipesMatch(playerRecipe, transformedRecipe))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate all allowed transformations (translations and horizontal flips) of the
     * recipe.
     *
     * @param recipe Original recipe map
     * @return List of transformed recipes
     */
    List<Map<Recipe.Position, Set<String>>> generateTransformedRecipes(
            final Map<Recipe.Position, Set<String>> recipe)
    {
        final List<Map<Recipe.Position, Set<String>>> transformedRecipes =
                new ArrayList<>();

        // Possible translations
        final int[] dx = {-1, 0, 1};
        final int[] dy = {-1, 0, 1};

        for(final int tx : dx)
        {
            for(final int ty : dy)
            {
                if(canTranslate(recipe, tx, ty))
                {
                    final Map<Recipe.Position, Set<String>> translatedRecipe =
                            translateRecipe(recipe, tx, ty);
                    transformedRecipes.add(translatedRecipe);

                    // Add horizontal flip of the translated recipe
                    final Map<Recipe.Position, Set<String>> flippedRecipe =
                            horizontalFlipRecipe(translatedRecipe);
                    if(isWithinBounds(flippedRecipe))
                    {
                        transformedRecipes.add(flippedRecipe);
                    }
                }
            }
        }

        return transformedRecipes;
    }

    /**
     * Check if translating the recipe by (dx, dy) keeps all positions within bounds.
     *
     * @param recipe Original recipe map
     * @param dx     Translation in X-direction
     * @param dy     Translation in Y-direction
     * @return True if translation keeps all positions within bounds, False otherwise
     */
    private boolean canTranslate(final Map<Recipe.Position, Set<String>> recipe,
                                 final int dx,
                                 final int dy)
    {
        for(final Recipe.Position pos : recipe.keySet())
        {
            final int newX = pos.getX() + dx;
            final int newY = pos.getY() + dy;
            if(newX < -1 || newX > 1 || newY < -1 || newY > 1)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Translate the recipe by given offsets.
     *
     * @param recipe Original recipe
     * @param dx     Offset in X-direction
     * @param dy     Offset in Y-direction
     * @return Translated recipe
     */
    private Map<Recipe.Position, Set<String>> translateRecipe(
            final Map<Recipe.Position, Set<String>> recipe, final int dx, final int dy)
    {
        final Map<Recipe.Position, Set<String>> newRecipe = new HashMap<>();
        for(final Map.Entry<Recipe.Position, Set<String>> entry : recipe.entrySet())
        {
            final int newX = entry.getKey().getX() + dx;
            final int newY = entry.getKey().getY() + dy;
            newRecipe.put(new Recipe.Position(newX, newY), entry.getValue());
        }
        return newRecipe;
    }

    /**
     * Perform a horizontal flip of the recipe.
     *
     * @param recipe Original recipe
     * @return Horizontally flipped recipe
     */
    private Map<Recipe.Position, Set<String>> horizontalFlipRecipe(
            final Map<Recipe.Position, Set<String>> recipe)
    {
        final Map<Recipe.Position, Set<String>> newRecipe = new HashMap<>();
        for(final Map.Entry<Recipe.Position, Set<String>> entry : recipe.entrySet())
        {
            final int newX = -entry.getKey().getX();
            final int newY = entry.getKey().getY();
            newRecipe.put(new Recipe.Position(newX, newY), entry.getValue());
        }
        return newRecipe;
    }

    /**
     * Check if all positions in the recipe are within bounds.
     *
     * @param recipe Recipe map to check
     * @return True if all positions are within bounds, False otherwise
     */
    private boolean isWithinBounds(final Map<Recipe.Position, Set<String>> recipe)
    {
        for(final Recipe.Position pos : recipe.keySet())
        {
            if(pos.getX() < -1 || pos.getX() > 1 || pos.getY() < -1 || pos.getY() > 1)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if two recipes match exactly, considering acceptable materials.
     *
     * @param playerRecipe  Player's recipe
     * @param correctRecipe Correct recipe
     * @return True if recipes match, False otherwise
     */
    private boolean recipesMatch(final Map<Recipe.Position, String> playerRecipe,
                                 final Map<Recipe.Position, Set<String>> correctRecipe)
    {
        if(playerRecipe.size() != correctRecipe.size())
        {
            return false;
        }
        for(final Map.Entry<Recipe.Position, Set<String>> entry :
                correctRecipe.entrySet())
        {
            final String playerItemName = playerRecipe.get(entry.getKey());
            final Set<String> acceptableItems = entry.getValue();
            if(playerItemName == null || !acceptableItems.contains(playerItemName))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * End the game and show statistics.
     */
    private void endGame()
    {
        totalScore += score;
        final double averageScore = (double) totalScore / gamesPlayed;

        // Build statistics string
        final StringBuilder sb = new StringBuilder();
        sb.append("Game Statistics:").append("\nGames Played: ").append(gamesPlayed)
                .append("\nTotal Score: ").append(totalScore)
                .append("\nAverage Score per Game: ")
                .append(String.format("%.2f", averageScore));

        // Show statistics
        Platform.runLater(() ->
        {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Statistics");
            alert.setHeaderText(null);
            alert.setContentText(sb.toString());
            alert.showAndWait();

            // Ask if the player wants to play again
            gameUi.askToPlayAgain();
        });
    }

    /**
     * Get the current item to craft.
     *
     * @return Current CraftingItem
     */
    public CraftingItem getCurrentItem()
    {
        return currentItem;
    }

    /**
     * Get the list of option items.
     *
     * @return List of Items
     */
    public List<Item> getOptionItems()
    {
        return optionItems;
    }

    /**
     * Get the current score.
     *
     * @return Current score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Get the remaining number of questions.
     *
     * @return Remaining questions
     */
    public int getRemainingQuestions()
    {
        return totalQuestions - currentQuestion;
    }
}
