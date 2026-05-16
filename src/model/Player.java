package model;

public class Player extends GameEntity {
    public Player(String name) {
        super(name);
    }

    public String getStatusEffect() {
        return name + " is feeling motivated!";
    }

    // If you want to keep performAction, remove the @Override if it's not in GameEntity
    public void performAction() {
        System.out.println(name + " is studying hardware!");
    }
}