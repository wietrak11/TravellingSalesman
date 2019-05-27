package sample;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Tile> pathList = new ArrayList<Tile>();
    private double fulldistance = 0.0;

    public Path(List<Tile> pathList) {
        this.pathList = pathList;
    }

    public void calcFullDistance(){
        for(int i=0 ; i<pathList.size()-1 ; i++){
            fulldistance = fulldistance + calcDistance(pathList.get(i),pathList.get(i+1));
        }
    }

    public List<Tile> getPathList() {
        return pathList;
    }

    public double getFulldistance() {
        return fulldistance;
    }

    private double calcDistance(Tile x1, Tile x2){

        double distance = 0.0;

        double xDis = x1.getX() - x2.getX();
        xDis = Math.pow(xDis,2);
        double yDis = x1.getY() - x2.getY();
        yDis = Math.pow(yDis,2);

        distance = Math.sqrt(xDis + yDis);

        return distance;
    }
}
