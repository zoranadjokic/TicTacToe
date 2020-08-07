package TicTacToe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private boolean playable = true;
    private boolean turnX = true;
    private Tile[][] board = new Tile[3][3];
    private List<Combo> combos = new ArrayList<>();
    private GridPane gp = new GridPane();
    private Label lbWinner = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox root = new VBox(10);

        root.setAlignment(Pos.CENTER);

        gp.setPadding(new Insets(10,10,10,10));
        gp.setVgap(10);
        gp.setHgap(10);

        for (int i = 0; i < 3 ; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();

                GridPane.setConstraints(tile,j,i);
                gp.getChildren().add(tile);

                board[i][j] = tile;

            }
        }

        // horizontal
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        // vertical
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // diagonals
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        Button btExit = new Button("EXIT");
        btExit.setOnAction(e -> {
            primaryStage.close();
        });

        Button btReload = new Button("REPLAY");

        btReload.setOnAction(e -> {Main app = new Main();
        primaryStage.close();
            try {
                app.start(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Button btInsturctions = new Button("Show instructions");
        btInsturctions.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Instructions");
            alert.setHeaderText(null);
            String s = "Player X plays first. \n" +
                    " Player X plays by clicking left click on the mouse,\n" +
                    " and Player O plays by clicking right click on the mouse";
            alert.setContentText(s);

            alert.showAndWait();
        });

        HBox hbTop = new HBox(10);
        hbTop.setAlignment(Pos.CENTER);

        lbWinner.setTextFill(Color.RED);
        lbWinner.setFont(Font.font(20));

        hbTop.getChildren().addAll(btReload, btExit);

        root.getChildren().addAll(btInsturctions,gp,lbWinner,hbTop);


        primaryStage.setScene(new Scene(root, 500,700));
        primaryStage.show();

    }

    private class Tile extends StackPane {
        private Text text = new Text();

        public Tile() {
            //when created tile is empty
            Rectangle border = new Rectangle(150, 150);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (!playable)
                    return;
                //primery is the left click
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!turnX)
                        return;

                    drawX();
                    turnX = false;
                    checkState();
                }
                //secondary is the right click
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (turnX)
                        return;
                    drawO();
                    turnX = true;
                    checkState();
                }
            });
        }


        public String getValue() {
            return text.getText();
        }

        private void drawX() {
            if(!text.getText().isEmpty())
                return;
            text.setFill(Color.BLUE);
            text.setStroke(Color.BLUE);
            text.setText("X");

        }

        private void drawO() {
            if(!text.getText().isEmpty())
                return;
            text.setFill(Color.YELLOW);
            text.setStroke(Color.YELLOW);
            text.setText("O");
        }
    }


    private class Combo {
        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
    }

    private void playWinAnimation(Combo combo) {
        if(combo.tiles[0].getValue().equals("X"))
            lbWinner.setText("Player X won the game!");
        else
            lbWinner.setText("Player O won the game!");

        combo.tiles[0].text.setFill(Color.RED);
        combo.tiles[0].text.setStroke(Color.RED);
        combo.tiles[1].text.setFill(Color.RED);
        combo.tiles[1].text.setStroke(Color.RED);
        combo.tiles[2].text.setFill(Color.RED);
        combo.tiles[2].text.setStroke(Color.RED);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
