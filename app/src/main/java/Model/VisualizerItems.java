package Model;

import java.util.ArrayList;

public class VisualizerItems {

    String str;
    ArrayList<Integer> pastData;

    public VisualizerItems(String str, ArrayList<Integer> pastData) {
        this.str = str;
        this.pastData = pastData;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public ArrayList<Integer> getPastData() {
        return pastData;
    }

    public void setPastData(ArrayList<Integer> pastData) {
        this.pastData = pastData;
    }
}
