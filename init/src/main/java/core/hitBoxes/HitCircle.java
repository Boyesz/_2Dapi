package core.hitBoxes;

public class HitCircle {

    float x;
    float y;
    float r;

    public HitCircle(float x, float y, float r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public boolean checkOverlapping(HitCircle circle){

        if(Math.pow((x - circle.x),2) + Math.pow((y+circle.y),2) <= Math.pow((r + circle.r),2))
            return true;

        return false;
    }
}
