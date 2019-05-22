package sample;

import javafx.scene.layout.Pane;

public class Tile {

    private Pane pane;
    private Double state;
    private int x;
    private int y;

    public Tile(Pane pane,Double state,int x, int y){
        this.pane = pane;
        this.state = state;
        this.x = x;
        this.y = y;
    }

    public Pane getPane(){ return pane; }

    public void setState(Double state) { this.state=state; }

    public Double getState(){ return state;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString(){
        //return Integer.toString(state.intValue());
        return "(" + x + "," + y + ") ";
    }
}
