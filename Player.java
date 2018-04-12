abstract class Player implements IPlayer {

    protected String name; 
    protected int sum = 0;
    protected int numSkip = 5;

    Player(String name) { this.name = name; }
    
    // ------------------------------------------------------------------

    // return name
    public String name() { return this.name; }

    abstract public void inform(String msg); 

    abstract public boolean turn(Turn t);
}

