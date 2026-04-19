package com.labs.game.model;

public class Record {
    private int maxScore;
    private int curScore;

    public Record(){
        maxScore = 0;
        curScore = 0;
    }

    public int getMaxScore(){
        return this.maxScore;
    }
    public int getCurScore(){
        return this.curScore;
    }
    public void update(int price){
        curScore +=price;
        if(curScore > maxScore){
            maxScore = curScore;
        }
    }
    public void resetMaxScore(){
        maxScore = 0;
    }
    public void resetCurScore(){
        curScore = 0;
    }
}
