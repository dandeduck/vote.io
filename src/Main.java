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
            Ballot ballot = new Ballot(new Option("Trump",1,2), new Option("Hillari",2,2));
            ballots.add(ballot);
        }

        for(int i = 0; i < 40; ++i) {
            Ballot ballot = new Ballot(new Option("Trump",2,2), new Option("Hillari",1,2));
            ballots.add(ballot);
        }

        station.setBallots(ballots);
        System.out.println(station.calculate(1));
    }
}
