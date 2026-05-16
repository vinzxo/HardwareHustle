package model;

public abstract class GameEntity {
    protected String name;

    public GameEntity(String name) { this.name = name; }
    
    // Abstract method to be implemented by children
    public abstract String getStatusEffect();
}