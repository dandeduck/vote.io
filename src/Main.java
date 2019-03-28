import stv.Ballot;
import stv.Option;
import stv.STVStation;

import java.util.ArrayDeque;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        STVStation station = new STVStation();
        Queue<Ballot> ballots = new ArrayDeque<>();

        for(int i = 0; i < 60; ++i) {
            Ballot ballot = new Ballot(new Option("Trump",2,4), new Option("Hillari",1,4), new Option("Tom",3,4));
            Ballot ballot1 = new Ballot(new Option("Trump",1,4), new Option("Hillari",3,4), new Option("Tom",2,4));
            ballots.add(ballot);
            ballots.add(ballot1);
        }

        for(int i = 0; i < 40; ++i) {
            Ballot ballot = new Ballot(new Option("Trump",3,4), new Option("Hillari",2,4), new Option("Tom",1,4));
            ballots.add(ballot);
        }

        station.setBallots(ballots,"Trump","Hillari","Tom","MaMan");
        System.out.println(station.calculate(3));
    }
}
