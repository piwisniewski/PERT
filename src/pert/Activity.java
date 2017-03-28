package pert;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    
    private double tc, tm, tp, t0;      // optymistyczny, najbardziej prawdopodobny, pesymistyczny, wartość oczekiwana
    private double sigmaSquared;
    private String name;
    private List<Activity> previousList;  // indeksy poprzedników
    private String previousActivities;
    public int id;
    
    public Activity(List<String> list){
        previousList = new ArrayList<>();
        this.id = Integer.parseInt(list.get(0));
        this.name = list.get(1);
        this.tc = Double.parseDouble(list.get(2));
        this.tm = Double.parseDouble(list.get(3));
        this.tp = Double.parseDouble(list.get(4));
        if(list.get(5).equals(""))
            this.previousActivities = "0";
        else
            this.previousActivities = list.get(5);
        doExpectedValue();
        doSigma();
    }
    
    public void doExpectedValue(){
        t0 = (tc+(4*tm)+tp)/6;
    }
    
    public void doSigma(){
        sigmaSquared = Math.pow((tp-tc)/6, 2);
    }
    
    public void addPrevious(Activity a){
        previousList.add(a);
    }

    public double getTc() {
        return tc;
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    public double getTm() {
        return tm;
    }

    public void setTm(double tm) {
        this.tm = tm;
    }

    public double getTp() {
        return tp;
    }

    public void setTp(double tp) {
        this.tp = tp;
    }

    public double getT0() {
        return t0;
    }

    public double getSigma() {
        return sigmaSquared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Activity> getPreviousList() {
        return previousList;
    }

    public String getPreviousActivities() {
        return previousActivities;
    }

    public int getId() {
        return id;
    }
    
}
