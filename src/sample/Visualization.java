package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

        Group root = new Group();

        for(int i=0 ; i<circleList.size() ; i++){
            root.getChildren().add(circleList.get(i));
        }

        for(int i=0; i<pathList.size()-1 ; i++){
            Text t = new Text((pathList.get(i).getX()*29)+35,(pathList.get(i).getY()*29)+35,Integer.toString(i));
            t.setFont(new Font(15));
            t.setFill(Color.RED);
            Line line = new Line((pathList.get(i).getX()*29)+25,(pathList.get(i).getY()*29)+25,(pathList.get(i+1).getX()*29)+25,(pathList.get(i+1).getY()*29)+25);
            root.getChildren().add(line);
            root.getChildren().add(t);
        }

        Scene scene = new Scene(root, 600, 600);

        stage.setTitle("Route");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(){
        launch();
    }
}      