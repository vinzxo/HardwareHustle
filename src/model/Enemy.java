package model;

public class Enemy extends GameEntity {
    public Enemy(String name) { super(name); }

    @Override
    public String getStatusEffect() {
        return name + " is stunned by your hardware knowledge!";
    }
}