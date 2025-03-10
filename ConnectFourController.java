import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Controller class for the "Connect Four game" UI.
 */
public class ConnectFourController {

    private static int rowsNum;
    private static int colsNum;

    private static ConnectFourLogic connect4;

    private final Color backColor = Color.WHITE;
    private final Color hoverCol = Color.DARKBLUE;
    private final Color[] playersColors = {null, Color.RED, Color.YELLOW};
    private Circle[][] disks;
    private Circle previewDisk;

    @FXML
    private GridPane grid;

    @FXML
    private Pane previewPane;

    @FXML
    private Label headerLabel;

    /**
     * Initializes the controller and sets up the game.
     */
    public void initialize() {

        connect4 = new ConnectFourLogic();
        rowsNum = ConnectFourLogic.getNumOfRows();
        colsNum = ConnectFourLogic.getNumOfCols();

        displayBoard();
        initPreviewDisk();
    }

    /**
     * Displays the game board grid with empty disks.
     */
    private void displayBoard() {
        double radius = getRadius();
        adjustGridGaps(radius);

        disks = new Circle[rowsNum][colsNum];
        for (int r = 0; r < rowsNum; r++) {
            for (int c = 0; c < colsNum; c++) {
                disks[r][c] = createNewDisk(radius);
                grid.add(disks[r][c], c, r);
            }
        }
    }

    /**
     * Initializes the preview disk for indicating the next move.
     */
    private void initPreviewDisk() {
        previewDisk = new Circle(getRadius(), playersColors[connect4.getCurrPlayer()]);
        previewDisk.setStroke(Color.BLACK);
        previewDisk.setCenterY(previewPane.getPrefHeight() / 2);
        previewDisk.setCenterX(previewPane.getPrefWidth() / 2);
        previewPane.getChildren().add(previewDisk);
    }

    /**
     * Calculates the radius of a disk based on grid size.
     *
     * @return The radius of a disk.
     */
    private double getRadius() {
        // Calculate available height and width for the grid
        double availHeight = grid.getPrefHeight() - ((rowsNum - 1) * grid.getVgap())
                - grid.getPadding().getBottom() - grid.getPadding().getTop();
        double availWidth = grid.getPrefWidth() - ((colsNum - 1) * grid.getHgap())
                - grid.getPadding().getRight() - grid.getPadding().getLeft();

        double cellHeight = availHeight / rowsNum;
        double cellWidth = availWidth / colsNum;

        return 0.5 * Math.min(cellHeight, cellWidth);

    }

    /**
     * Adjusts the gaps between cells in the grid layout.
     * (It's possible to change the number of columns or rows
     * and still get a nicely well-structured board)
     * @param radius The radius of a disk.
     */
    private void adjustGridGaps(double radius) {

        double vGap = (grid.getPrefHeight() - (radius * 2 * rowsNum)
                - grid.getPadding().getBottom() - grid.getPadding().getTop()) / (rowsNum - 1);
        grid.setVgap(Math.max(vGap, grid.getVgap()));

        double hGap = (grid.getPrefWidth() - (radius * 2 * colsNum)
                - grid.getPadding().getRight() - grid.getPadding().getLeft()) / (colsNum - 1);
        grid.setHgap(Math.max(hGap, grid.getHgap()));

    }

    /**
     * Creates a new disk with the specified radius and event handlers.
     *
     * @param radius The radius of the disk.
     * @return The newly created disk.
     */
    private Circle createNewDisk(double radius) {
        Circle newDisk = new Circle(radius);
        newDisk.setFill(backColor);

        newDisk.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMouseEntered(mouseEvent);
            }
        });

        newDisk.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMouseExited(mouseEvent);
            }
        });

        newDisk.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMousePressed(mouseEvent);
            }
        });
        return newDisk;
    }

    /**
     * Handles the mouse entering a grid cell by coloring all the empty cell in the column
     *
     * @param mouseEvent The mouse event.
     */
    private void handleMouseEntered(MouseEvent mouseEvent) {
        int currCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());

        for (int row = 0; row < rowsNum; row++) {
            if (disks[row][currCol].getFill() != backColor) { // rest of the column contains discs
                break;
            }
            disks[row][currCol].setFill(hoverCol);
        }
    }

    /**
     * Handles the mouse exiting a grid cell by coloring all the empty cell in the column
     *
     * @param mouseEvent The mouse event.
     */
    private void handleMouseExited(MouseEvent mouseEvent) {
        int currCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        for (int row = 0; row < rowsNum; row++) {
            if (disks[row][currCol].getFill() != hoverCol) { // rest of the column contains discs
                break;
            }
            disks[row][currCol].setFill(backColor);
        }
    }

    /**
     * Handles the mouse click on a grid cell by placing a disk in the corresponding column if the game is ongoing
     * according to the resuld of the game logic
     *
     * @param mouseEvent The mouse event.
     */
    private void handleMousePressed(MouseEvent mouseEvent) {
        if (connect4.isGameOn()) {
            int currCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
            int lowestRow = connect4.dropDisk(currCol);
            if (lowestRow >= 0) {
                disks[lowestRow][currCol].setFill(playersColors[connect4.getCurrPlayer()]);
                previewDisk.setFill(playersColors[connect4.getNextPlayer()]);
            }
            if (!connect4.isGameOn()) {
                displayWinner();
            }
        }
    }

    /**
     * Displays the winner of the game when it ends.
     */
    private void displayWinner(){
        if (connect4.getCurrPlayer() == 1) {
            headerLabel.setText("Red wins!");
            headerLabel.setTextFill(Color.RED);
        } else {
            headerLabel.setText("Yellow wins!");
            headerLabel.setTextFill(Color.YELLOW.darker());
        }
    }

    /**
     * Moving the preview disk above the board
     */
    @FXML
    void onMouseMovedGrid(MouseEvent event) {
        previewDisk.setCenterX(event.getX());
    }


    /**
     * restarting the game
     */
    @FXML
    void onRestartButtonPressed(ActionEvent event) {
        restart();
    }

    /**
     * Restarts the game by resetting the board and updating the UI.
     */
    private void restart() {
        headerLabel.setText("Connect 4");
        headerLabel.setTextFill(Color.BLACK);

        connect4.restartGame();
        resetBoard();
        previewDisk.setFill(playersColors[connect4.getCurrPlayer()]);

    }

    /**
     * Resets the game board by clearing all disks.
     */
    private void resetBoard() {
        for (int i = 0; i < rowsNum; i++) {
            for (int j = 0; j < colsNum; j++) {
                disks[i][j].setFill(backColor);
            }
        }
    }
}

