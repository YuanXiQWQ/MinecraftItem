import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

/**
 * Main class for the MinecraftItem game. Extends JavaFX Application and implements the
 * Game interface.
 *
 * @author Jiarui Xing
 */
public class MinecraftItem extends Application implements Game {

    private GameController gameController;

    // UI components
    private HBox topOptionsBox;
    private GridPane craftingGrid;
    private ImageView itemToCraftImageView;
    private Label scoreLabel;
    private Label remainingLabel;
    private Label feedbackLabel;

    private Stage primaryStage;

    // Currently selected item
    private String selectedItemName = null;
    private String selectedItemImagePath = null;
    private ImageCursor selectedCursor = null;

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
        gameController = GameController.getInstance(this);

        final Image appIcon = new Image("file:images/crafting_table.png");
        primaryStage.getIcons().add(appIcon);

        // Initialize UI components
        initUiComponents();

        // Set up the main layout
        final BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        mainLayout.setStyle("-fx-background-color: #c6c6c6;");

        // Top area: 1x9 candidate items
        final VBox topArea = new VBox(10);
        topArea.setAlignment(Pos.CENTER);
        topArea.getChildren().add(topOptionsBox);
        mainLayout.setTop(topArea);

        // Center area: Pane with background image, crafting grid, and item display
        final Pane centerPane = new Pane();
        centerPane.setPrefSize(472, 266);

        // Set background image
        final ImageView backgroundImageView = createBackgroundImageView();
        centerPane.getChildren().add(backgroundImageView);

        // Create crafting grid
        craftingGrid = createCraftingGrid();
        // Manually set crafting grid position relative to background image
        craftingGrid.setLayoutX(8);
        craftingGrid.setLayoutY(48);
        centerPane.getChildren().add(craftingGrid);

        // Create item display cell
        final StackPane itemDisplayCell = createItemDisplayCell();
        // Manually set item display cell position relative to background image
        itemDisplayCell.setLayoutX(368);
        itemDisplayCell.setLayoutY(104);
        centerPane.getChildren().add(itemDisplayCell);

        // Use StackPane to center the centerPane
        final StackPane centerContainer = new StackPane(centerPane);
        centerContainer.setAlignment(Pos.CENTER);
        mainLayout.setCenter(centerContainer);

        // Add item to craft ImageView to item display cell
        itemToCraftImageView = new ImageView();
        itemToCraftImageView.setFitWidth(64);
        itemToCraftImageView.setFitHeight(64);
        itemToCraftImageView.setVisible(false);
        itemDisplayCell.getChildren().add(itemToCraftImageView);

        mainLayout.setCenter(centerPane);

        // Bottom control area: score, remaining questions, skip button, and feedback
        // label
        final HBox controlArea = new HBox(20);
        controlArea.setAlignment(Pos.CENTER_LEFT);
        scoreLabel = new Label("Score: 0");
        remainingLabel = new Label("Remaining Questions: 10");
        final Button skipButton = new Button("Skip");
        skipButton.setOnAction(e -> gameController.skipQuestion());

        // Initialize feedback label
        feedbackLabel = new Label("");

        // Add components to control area
        controlArea.getChildren()
                .addAll(scoreLabel, remainingLabel, skipButton, feedbackLabel);
        mainLayout.setBottom(controlArea);

        // Set the scene with specified size
        final Scene scene = new Scene(mainLayout, 800, 400);
        primaryStage.setTitle("MinecraftItem");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the game
        gameController.startGame();
        updateUi();

        // Add click event to itemDisplayCell
        itemDisplayCell.setOnMouseClicked(event ->
        {
            if(event.getButton() == MouseButton.PRIMARY)
            {
                gameController.submitRecipe();
            }
        });
    }

    /**
     * Initialize UI components, including top options and crafting grid.
     */
    private void initUiComponents()
    {
        // Initialize the top options box (1x9)
        topOptionsBox = new HBox(5);
        topOptionsBox.setAlignment(Pos.CENTER);
        // Initially empty; will be populated in updateUi()
    }

    /**
     * Create the background ImageView.
     *
     * @return Configured ImageView
     */
    private ImageView createBackgroundImageView()
    {
        final ImageView bgImageView = new ImageView();
        final Image bgImage = loadImage("images/background.png");
        if(bgImage != null)
        {
            bgImageView.setImage(bgImage);
            bgImageView.setFitWidth(472);
            bgImageView.setFitHeight(266);
        } else
        {
            // If background image fails to load, set a default background color
            bgImageView.setStyle("-fx-background-color: #d3d3d3;");
        }
        return bgImageView;
    }

    /**
     * Create a cell for the item to craft display with border.
     *
     * @return StackPane representing the item display cell
     */
    private StackPane createItemDisplayCell()
    {
        final StackPane cell = new StackPane();
        cell.setPrefSize(95, 95);
        cell.setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent;");
        return cell;
    }

    /**
     * Create the crafting grid (3x3 GridPane).
     *
     * @return Configured GridPane
     */
    private GridPane createCraftingGrid()
    {
        final GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                final StackPane cell = createCraftingCell(col, row);
                grid.add(cell, col, row);
            }
        }

        return grid;
    }

    /**
     * Create a cell for the crafting grid.
     *
     * @param col Column index
     * @param row Row index
     * @return StackPane representing the cell
     */
    private StackPane createCraftingCell(final int col, final int row)
    {
        final StackPane cell = new StackPane();
        cell.setPrefSize(64, 64);
        cell.setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent;");
        cell.setOnMouseClicked(event ->
        {
            if(event.getButton() == MouseButton.PRIMARY)
            {
                if(selectedItemName != null && selectedItemImagePath != null)
                {
                    // Place the selected item in the cell
                    final Image image = loadImage(selectedItemImagePath);
                    if(image != null)
                    {
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(60);
                        imageView.setFitHeight(60);
                        cell.getChildren().clear();
                        cell.getChildren().add(imageView);

                        // Update game controller
                        gameController.placeItemOnGrid(col, row, selectedItemName);
                    }
                    // Reset cursor and selection
                    resetCursor();
                } else
                {
                    // If no item is selected, remove the item from the cell
                    cell.getChildren().clear();
                    gameController.placeItemOnGrid(col, row, null);
                }
            } else if(event.getButton() == MouseButton.SECONDARY)
            {
                // Right-click to remove the item from the cell
                cell.getChildren().clear();
                gameController.placeItemOnGrid(col, row, null);
            }
        });
        return cell;
    }

    /**
     * Update the UI components with the current game state.
     */
    public void updateUi()
    {
        // Update the top candidate items (1x9)
        topOptionsBox.getChildren().clear();
        final List<Item> options = gameController.getOptionItems();
        for(final Item item : options)
        {
            final Image itemImage = loadImage(item.getImagePath());
            final Button button = getButton(item, itemImage);
            topOptionsBox.getChildren().add(button);
        }

        // Update the item to craft display
        final String imagePath = gameController.getCurrentItem().getImagePath();
        final Image image = loadImage(imagePath);
        if(image != null)
        {
            itemToCraftImageView.setImage(image);
            itemToCraftImageView.setFitWidth(64);
            itemToCraftImageView.setFitHeight(64);
            itemToCraftImageView.setVisible(true);
        } else
        {
            itemToCraftImageView.setImage(null);
            itemToCraftImageView.setVisible(false);
        }

        // Update score and remaining labels
        scoreLabel.setText("Score: " + gameController.getScore());
        remainingLabel.setText(
                "Remaining Questions: " + gameController.getRemainingQuestions());

        // Clear crafting grid
        for(final javafx.scene.Node node : craftingGrid.getChildren())
        {
            if(node instanceof StackPane cell)
            {
                cell.getChildren().clear();
            }
        }

        // Reset cursor and selection
        resetCursor();

        // Clear feedback label
        feedbackLabel.setText("");
    }

    /**
     * Create a button for the given item with its image.
     *
     * @param item      The item to create a button for
     * @param itemImage The image of the item
     * @return Configured Button
     */
    private Button getButton(final Item item, final Image itemImage)
    {
        final ImageView imageView = new ImageView(itemImage);
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        final Button button = new Button();
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");
        button.setOnAction(e ->
        {
            // Handle item selection
            selectedItemName = item.getName();
            selectedItemImagePath = item.getImagePath();
            final Image cursorImage = loadImage(selectedItemImagePath);
            if(cursorImage != null)
            {
                selectedCursor = new ImageCursor(cursorImage, cursorImage.getWidth() / 2,
                        cursorImage.getHeight() / 2);
                primaryStage.getScene().setCursor(selectedCursor);
            }
        });
        return button;
    }

    /**
     * Reset the cursor and clear the selected item.
     */
    private void resetCursor()
    {
        selectedItemName = null;
        selectedItemImagePath = null;
        selectedCursor = null;
        primaryStage.getScene().setCursor(Cursor.DEFAULT);
    }

    /**
     * Load an image from the given path.
     *
     * @param imagePath Relative path to the image (e.g., "images/wood_planks.png")
     * @return Image object or null if loading fails
     */
    private Image loadImage(final String imagePath)
    {
        try
        {
            final File file = new File(imagePath);
            if(file.exists())
            {
                return new Image(file.toURI().toString());
            } else
            {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
        } catch(Exception e)
        {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start()
    {
        // Launch the JavaFX application
        Application.launch();
    }

    /**
     * Display feedback to the player using the feedback label.
     *
     * @param isSuccess      True if success, False if failure
     * @param isFirstFailure True if it's the first failure, allowing retry
     */
    public void displayFeedback(final boolean isSuccess, final boolean isFirstFailure)
    {
        Platform.runLater(() ->
        {
            if(isSuccess)
            {
                // Success: display feedback in green
                feedbackLabel.setStyle("-fx-text-fill: green;");
                feedbackLabel.setText("Crafting Successful!");
                // Pause briefly before proceeding to next question
                final PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event ->
                {
                    feedbackLabel.setText("");
                    gameController.nextQuestion();
                    updateUi();
                });
                pause.play();
            } else
            {
                // Failure: display feedback in red
                feedbackLabel.setStyle("-fx-text-fill: red;");
                if(isFirstFailure)
                {
                    feedbackLabel.setText("Crafting Failed, please try again!");
                } else
                {
                    // Second failure: display feedback and proceed to next question
                    feedbackLabel.setText("Crafting Failed!");
                    final PauseTransition pause =
                            new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(event ->
                    {
                        feedbackLabel.setText("");
                        gameController.nextQuestion();
                        updateUi();
                    });
                    pause.play();
                }
            }
        });
    }

    /**
     * Ask the player if they want to play again.
     */
    public void askToPlayAgain()
    {
        Platform.runLater(() ->
        {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to play again?");
            final ButtonType yesButton = new ButtonType("Yes");
            final ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type ->
            {
                if(type == yesButton)
                {
                    gameController.startGame();
                    updateUi();
                } else
                {
                    // Exit the game or return to main menu
                    primaryStage.close();
                }
            });
        });
    }

    /**
     * Entry point when running the MinecraftItem game separately.
     *
     * @param args Command-line arguments
     */
    public static void main(final String[] args)
    {
        final MinecraftItem game = new MinecraftItem();
        game.start();
    }
}
