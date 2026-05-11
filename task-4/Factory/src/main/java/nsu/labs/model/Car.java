package nsu.labs.model;

public class Car extends Entity{
    private final Body body;
    private final Motor motor;
    private final Accessory accessory;

    public Car(Body body, Motor motor, Accessory accessory){
        super();
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    public Body getBody(){
        return body;
    }

    public Motor getMotor(){
        return motor;
    }

    public Accessory getAccessory(){
        return accessory;
    }

    public String getDescription(){
        return String.format("Auto %d (Body: %d, Motor: %d, Accessory: %d)",
                getId(),
                body.getId(),
                motor.getId(),
                accessory.getId());
    }
}
