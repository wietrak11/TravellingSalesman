package sample;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    //GUI properties
    private int BOARD_TILE_WIDTH = 20;
    private int BOARD_TILE_HEIGHT = 20;
    private int multiplier = (600/BOARD_TILE_WIDTH)-1;
    private double radius = (1.0/(double)BOARD_TILE_WIDTH) * 200.0;

    private double TILE_WIDTH = 500/BOARD_TILE_WIDTH;
    private double TILE_HEIGHT = 500/BOARD_TILE_HEIGHT;
    private Stage stage;
    private Tile[][] tileArray = new Tile[BOARD_TILE_HEIGHT][BOARD_TILE_WIDTH];


    //FXML properties
    @FXML private GridPane board;

    private List<Tile> cityList = new ArrayList<Tile>();
    private List<Tile> pathList = new ArrayList<Tile>();
    private List<Path> listOfPaths = new ArrayList<>();

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

    public void bruteClick(MouseEvent mouseEvent) {
        pathList.clear();
        cityList.clear();
        listOfPaths.clear();

        final long startTime = System.currentTimeMillis();

        List<List<Tile>> tmp = new ArrayList<>();
        buildCityListForAlg();
        Tile startingPoint = cityList.get(0);
        cityList.remove(0);
        tmp = findAllPermutations(cityList);

        for(int i=0 ; i<tmp.size() ; i++){
            Path path = new Path(tmp.get(i));
            path.getPathList().add(0,startingPoint);
            path.getPathList().add(startingPoint);
            path.calcFullDistance();
            listOfPaths.add(path);
        }

        Path bestPath = listOfPaths.get(0);

        for(int i=1 ; i<listOfPaths.size() ; i++){
            if(listOfPaths.get(i).getFulldistance()<bestPath.getFulldistance()){
                bestPath = listOfPaths.get(i);
            }
        }

        final long endTime = System.currentTimeMillis();

        System.out.println("BRUTE FORCE ALGORITHM");
        System.out.println("Path:");
        System.out.println(bestPath.getPathList());
        System.out.println("Distance: " + bestPath.getFulldistance());
        System.out.println("Time of brute force: " + (endTime-startTime) + " milliseconds");
        System.out.println();

        buildCityListForVis();
        Visualization vis = new Visualization(cityList,bestPath.getPathList(),multiplier,radius);
        vis.start(stage);

        pathList.clear();
        cityList.clear();
        listOfPaths.clear();
    }

    public void normalizeBoard(){
        for(int i = 0 ; i<tileArray.length ; i++){
            for(int j = 0 ; j<tileArray[i].length ; j++){
                String style = tileArray[i][j].getPane().getStyle();
                if(style.contains("red")){
                    tileArray[i][j].setState(1.0);
                }
                else if(style.contains("blue")){
                    tileArray[i][j].setState(2.0);
                }
                else{
                    tileArray[i][j].setState(0.0);
                }
            }
        }
    }

    public void greedyClick(MouseEvent mouseEvent){

        final long startTime = System.currentTimeMillis();

        findRoute();

        final long endTime = System.currentTimeMillis();

        System.out.println("GREEDY ALGORITHM");
        System.out.println("Path:");
        System.out.println(pathList);
        Path p = new Path(pathList);
        p.calcFullDistance();
        System.out.println("Distance: " + p.getFulldistance());

        System.out.println("Time of greedy algorithm: " + (endTime-startTime) + " milliseconds");
        System.out.println();

        buildCityListForVis();
        Visualization vis = new Visualization(cityList,pathList,multiplier, radius);
        vis.start(stage);
        normalizeBoard();
        cityList.clear();
        pathList.clear();
    }

    public void loadClick(MouseEvent mouseEvent){
        pathList.clear();
        cityList.clear();
        listOfPaths.clear();

        try(BufferedReader br = new BufferedReader(new FileReader("city.txt"))){
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            int max=0;
            List<Tile> tmpCityList = new ArrayList<Tile>();

            while(line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
                if(!(line==null)){
                    String[] point = line.split("\\s");
                    int x = Integer.parseInt(point[0]);
                    int y = Integer.parseInt(point[1]);

                    if(x>max){
                        max = x;
                    }
                    if(y>max){
                        max = y;
                    }

                    Tile tile = new Tile(1.0,x,y);
                    cityList.add(tile);
                    tmpCityList.add(tile);
                }
            }
            //BRUTE-----------------------------------------------
            final long startTime1 = System.currentTimeMillis();

            List<List<Tile>> tmp = new ArrayList<>();
            Tile startingPoint = cityList.get(0);
            cityList.remove(0);
            tmp = findAllPermutations(cityList);

            for(int i=0 ; i<tmp.size() ; i++){
                Path path = new Path(tmp.get(i));
                path.getPathList().add(0,startingPoint);
                path.getPathList().add(startingPoint);
                path.calcFullDistance();
                listOfPaths.add(path);
            }

            Path bestPath = listOfPaths.get(0);

            for(int i=1 ; i<listOfPaths.size() ; i++){
                if(listOfPaths.get(i).getFulldistance()<bestPath.getFulldistance()){
                    bestPath = listOfPaths.get(i);
                }
            }

            final long endTime1 = System.currentTimeMillis();

            System.out.println("BRUTE FORCE ALGORITHM");
            System.out.println("Path:");
            System.out.println(bestPath.getPathList());
            System.out.println("Distance: " + bestPath.getFulldistance());
            System.out.println("Time of brute force: " + (endTime1-startTime1) + " milliseconds");
            System.out.println();

            Visualization vis = new Visualization(cityList,bestPath.getPathList(),0.5,0.2);
            vis.start(stage);
            //BRUTE -------------------------------------------------------
            //GREEDY ------------------------------------------------------


            tileArray = new Tile[max+1][max+1];
            for(int i=0 ; i<max+1 ; i++){
                for(int j=0 ; j<max+1 ; j++){
                    tileArray[j][i] = new Tile(0.0,i,j);
                }
            }
            for(int i=0 ; i<tmpCityList.size() ; i++){
                if(i==0){
                    tileArray[tmpCityList.get(i).getX()][tmpCityList.get(i).getY()].setState(2.0);
                }
                else{
                    tileArray[tmpCityList.get(i).getX()][tmpCityList.get(i).getY()].setState(1.0);
                }
            }

            final long startTime2 = System.currentTimeMillis();

            findRoute();

            final long endTime2 = System.currentTimeMillis();

            System.out.println("GREEDY ALGORITHM");
            System.out.println("Path:");
            System.out.println(pathList);
            Path p = new Path(pathList);
            p.calcFullDistance();
            System.out.println("Distance: " + p.getFulldistance());

            System.out.println("Time of greedy algorithm: " + (endTime2-startTime2) + " milliseconds");
            System.out.println();

            Visualization vis2 = new Visualization(cityList,pathList,0.5,0.2);
            vis2.start(stage);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tileArray = new Tile[BOARD_TILE_HEIGHT][BOARD_TILE_WIDTH];
        pathList.clear();
        cityList.clear();
        listOfPaths.clear();
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

    private List<List<Tile>> findAllPermutations(List<Tile> original){
        if(original.size()==0){
            List<List<Tile>> result = new ArrayList<List<Tile>>();
            result.add(new ArrayList<Tile>());
            return result;
        }
        Tile firstElement = original.remove(0);
        List<List<Tile>> returnValue = new ArrayList<List<Tile>>();
        List<List<Tile>> permutations = findAllPermutations(original);
        for(List<Tile> smallerPermutated : permutations) {
            for(int index=0; index <= smallerPermutated.size() ; index++){
                List<Tile> temp = new ArrayList<Tile>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
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
