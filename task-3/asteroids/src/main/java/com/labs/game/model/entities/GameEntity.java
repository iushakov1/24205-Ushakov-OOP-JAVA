package com.labs.game.model.entities;

public abstract class GameEntity {
    double x;
    double y;
    double xSpeed;
    double ySpeed;
    double rotationAngle;
    double radius;
    boolean destroyed;
    boolean ghostForm;
    int ghostFormTimer;

    public void update(int width, int height){
        if(destroyed){
            return;
        }
        this.updateGhostForm();
        x += xSpeed;
        xSpeed*=0.99;
        y += ySpeed;
        ySpeed*=0.99;

        if (x < 0){
            x = width;
        }
        if (x > width){
            x = 0;
        }
        if (y < 0){
            y = height;
        }
        if (y > height){
            y = 0;
        }
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public boolean isDestroyed(){
        return this.destroyed;
    }
    abstract void damaged();

    public boolean isColliding(GameEntity other) {
        if(this.isGhost() || other.isGhost()){
            return false;
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distanceSq = dx * dx + dy * dy;
        double radiusSum = this.radius + other.radius;
        return distanceSq < (radiusSum * radiusSum);
    }

    public void setGhost(int time){
        if(time > 0){
            this.ghostForm = true;
            this.ghostFormTimer = time;
        }
    }

    public boolean isGhost(){
        return this.ghostForm;
    }

    protected void updateGhostForm(){
        if(ghostFormTimer > 0){
            --ghostFormTimer;
        }else{
            this.ghostForm = false;
        }
    }

}
