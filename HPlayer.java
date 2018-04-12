class HPlayer extends Player {

    HPlayer(String name) { super(name); }

    // ------------------------------------------------------------------

    // a medium for communication to/with player
    private IHDisplay io;

    // register a Display area 
    public void registerDisplay(IDisplay d) {
        this.io = (IHDisplay)d;
    }

    // receive information from server 
    public void inform(String msg) {
        io.msgDisplay(msg);
    }

    // Java can only close over fields, not local variables
    private Turn turn;
    private boolean done = false;

    private boolean rollOrSkip;

    // enable the human player to take a turn 
    public boolean turn(Turn t) {
        this.turn = t;
        rollOrSkip = false;

        // interact with player:

        io.msgDisplay("It's your turn!");

        System.out.println("before" + rollOrSkip);

        io.listen
                (new IListener () {
                    public void done() {
                        turn.skip();
                        done = true;
                        rollOrSkip = true;
                    }
                    public int roll() {
                        int r = turn.roll();
                        sum += r;
                        return r;
                    }
                    public int skip() {
                        turn.skip();
                        numSkip --;
                        rollOrSkip = true;
                        return numSkip;
                    }
                });
        System.out.println("after" + rollOrSkip);

        io.sumDisplay(sum);

        if (!rollOrSkip) {
            io.msgDisplay("Second roll?");
            io.listen
                    (new IListener() {
                        public void done() {
                            turn.skip();
                            done = true;
                        }

                        public int roll() {
                            int r = turn.roll();
                            sum += r;
                            return r;
                        }

                        public int skip() {
                            turn.skip();
                            numSkip--;
                            return numSkip;
                        }
                    });
        }
        // end of interaction

        io.sumDisplay(sum);
        io.skipDisplay(numSkip);

        if (sum <= 21) {
            io.msgDisplay("Turn's up.");
        }
        else {
            done = true;
            io.msgDisplay("Oops, lost this one.");
        };

        return done;
    }

    // ------------------------------------------------------------------
    // Examples: 

    static HPlayer h1;
    static SPlayer s1;
    static Turn t1;
    static Turn t2;

    public static void createExamples() {
        if (h1 == null) {
            h1 = new HPlayer("test1");
            s1 = new SPlayer(h1);
            t1 = new Turn(s1, new Die());
            t2 = new Turn(s1, new Die());
        }
    }

    // ------------------------------------------------------------------
    // Tests: 

    public static void main(String argv[]) {
        createExamples();

        h1.registerDisplay(TestIHDisplay.testIDisplay);

        h1.inform("welcome to human player 1");

        boolean done = h1.turn(t1);
        done = h1.turn(t1);
        Tester.check(s1.done,"human cheat");

        done = h1.turn(t2);

        // cheating wasn't caught
        s1.sum = 22;
        done = h1.turn(t2);
        Tester.check(s1.done,"human cheat 3");
        Tester.check(s1.sum == 0,"human cheat 4");
    }

}
