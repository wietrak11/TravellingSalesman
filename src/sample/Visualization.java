package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Visualization extends Application{

    private List<Tile> cityList = new ArrayList<Tile>();
    private List<Tile> pathList = new ArrayList<Tile>();
    private List<Circle> circleList = new ArrayList<Circle>();

    public Visualization(List<Tile> cityList, List<Tile> pathList) {
        this.cityList = cityList;
        this.pathList = pathList;
    }

    @Override
    public void start(Stage stage) {

        for(int i=0 ; i<cityList.size() ; i++){
            Circle circle = new Circle((cityList.get(i).getX()*29)+25,(cityList.get(i).getY()*29)+25,10);
            circleList.add(circle);
        }

        //Line line = new Line();
        //line.setStartX(100.0);
        //line.setStartY(150.0);
        //line.setEndX(300.0);
        //line.setEndY(100.0);

        Group root = new Group();

        for(int i=0 ; i<circleList.size() ; i++){
            root.getChildren().add(circleList.get(i));
        }

        //Line line = new Line((pathList.get(0).getX()*29)+25,(pathList.get(0).getY()*29)+25,(pathList.get(1).getX()*29)+25,(pathList.get(1).getY()*29)+25);
        Line line = new Line(0,0,100,100);
        root.getChildren().add(line);

        Scene scene = new Scene(root, 600, 600);

        stage.setTitle("Animation Route");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(){
        launch();
    }
}      