package sample;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    //GUI properties
    private int BOARD_TILE_WIDTH = 20;
    private int BOARD_TILE_HEIGHT = 20;
    private double TILE_WIDTH = 500/BOARD_TILE_WIDTH;
    private double TILE_HEIGHT = 500/BOARD_TILE_HEIGHT;
    private Stage stage;
    private Tile[][] tileArray = new Tile[BOARD_TILE_HEIGHT][BOARD_TILE_WIDTH];


    //FXML properties
    @FXML private GridPane board;

    private List<Tile> cityList = new ArrayList<Tile>();
    private List<Tile> pathList = new ArrayList<Tile>();

    public void setGrid(){
        for(int i = 0; i< BOARD_TILE_HEIGHT; i++){
            for(int j = 0; j< BOARD_TILE_WIDTH; j++){
                Tile tile = new Tile(new Pane(), 0.0,j,i);
                tileArray[i][j] = tile;
                tileArray[i][j].getPane().setStyle("-fx-background-color: lightgrey;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                tileArray[i][j].getPane().setPrefWidth(TILE_WIDTH);
                tileArray[i][j].getPane().setPrefHeight(TILE_HEIGHT);
                GridPane.setConstraints(tileArray[i][j].getPane(),j,i);
                board.getChildren().add(tileArray[i][j].getPane());
            }
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setMouseClickedListener(){
        board.addEventHandler(MouseEvent.MOUSE_CLICKED,
                me -> {
                    if(me.getX()>=0 && me.getX()<500 && me.getY()>=0 && me.getY()<500) {
                        if (me.getButton().equals(MouseButton.PRIMARY)) {
                            if(tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getState()==1.0){
                                tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: blue;" +
                                        "-fx-border-color: black;" +
                                        "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                                tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(2.0);
                            }
                            else{
                                tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: red;" +
                                        "-fx-border-color: black;" +
                                        "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                                tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(1.0);
                            }
                        }
                        if (me.getButton().equals(MouseButton.SECONDARY)) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: lightgrey;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(0.0);
                        }
                    }
                });
    }

    public void setMouseDraggedListener(){
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                me -> {
                    if(me.getX()>=0 && me.getX()<500 && me.getY()>=0 && me.getY()<500){
                        if(me.isPrimaryButtonDown()) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: red;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(1.0);
                        }
                        if(me.isSecondaryButtonDown()) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: lightgrey;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(0.0);
                        }
                    }
                });
    }

    public <T> void showArray(T[][] array){
        System.out.print("{");
        for(int i=0 ; i<array.length ; i++){
            System.out.print("{");
            for(int j=0 ; j<array[0].length ; j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.print("},");
            System.out.println();
        }
        System.out.print("};");
        System.out.println();
        System.out.println();
    }

    public void checkClick(MouseEvent mouseEvent) {
        Visualization vis = new Visualization(cityList,pathList);
        vis.start(stage);
    }

    public void greedyClick(MouseEvent mouseEvent){
        pathList.clear();
        findRoute();
        System.out.println(pathList);
        buildCityListForVis();
        Visualization vis = new Visualization(cityList,pathList);
        vis.start(stage);
    }

    private void findRoute() {

        int numOfCities = countCities();

        for(int i=0 ; i<numOfCities ; i++){
            buildCityListForAlg();
            findNeares();
        }
    }

    public void findNeares(){

        List<Double> distanceList = new ArrayList<Double>();
        double nearest = calcDistance(tileArray[0][0],tileArray[BOARD_TILE_HEIGHT-1][BOARD_TILE_WIDTH-1]);
        nearest = nearest + 1.0;
        int index = 0;

        for(int i=1 ; i<cityList.size() ; i++){
            distanceList.add(calcDistance(cityList.get(0),cityList.get(i)));
        }

        for(int i=0 ; i<distanceList.size() ; i++){
            if(distanceList.get(i)<nearest){
                nearest = distanceList.get(i);
                index = i;
            }
        }
        if(cityList.size()==1){
            pathList.add(cityList.get(0));
            cityList.get(0).setState(3.0);
            pathList.add(pathList.get(0));
        }
        else {
            pathList.add(cityList.get(0));
            cityList.get(index+1).setState(2.0);
            cityList.get(0).setState(3.0);
        }
    }

    private void buildCityListForAlg(){
        cityList.clear();

        for(int i = 0; i< tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                if(tileArray[i][j].getState()==2) {
                    cityList.add(tileArray[i][j]);
                }
            }
        }

        for(int i = 0; i< tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                if(tileArray[i][j].getState()==1) {
                    cityList.add(tileArray[i][j]);
                }
            }
        }
    }

    private void buildCityListForVis(){
        cityList.clear();

        for(int i = 0; i< tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                if(tileArray[i][j].getState()>0) {
                    cityList.add(tileArray[i][j]);
                }
            }
        }
    }

    private int countCities(){
        int counter = 0;

        for(int i = 0; i< tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                if (tileArray[i][j].getState() > 0) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public double calcDistance(Tile x1, Tile x2){

        double distance = 0.0;

        double xDis = x1.getX() - x2.getX();
        xDis = Math.pow(xDis,2);
        double yDis = x1.getY() - x2.getY();
        yDis = Math.pow(yDis,2);

        distance = Math.sqrt(xDis + yDis);

        return distance;
    }

}
