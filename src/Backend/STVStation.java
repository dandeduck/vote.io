package Backend;

import java.util.ArrayDeque;
import java.util.Queue;

import javafx.util.Pair;

public class STVStation {
	private short mWinnersCount;
	private Queue<Ballot> mBallots;
	private Queue<Pair <Option, Integer>> mOptions;
	private Queue<Option> mWinners;

	public STVStation(Option[] options, Queue<Ballot> ballots) {
		mWinners = new ArrayDeque<Option>();
		mBallots = ballots;
		mOptions = new ArrayDeque<Pair <Option, Integer>>();

		for(Option option : options) 
			mOptions.add(new Pair<Option, Integer>(option,0));
		
		mWinnersCount = (short) mOptions.size();
	}
	
	public STVStation(Queue<Option> options, Queue<Ballot> ballots) {
		mWinners = new ArrayDeque<Option>();
		mBallots = ballots;
		mOptions = new ArrayDeque<Pair <Option, Integer>>();

		while(!options.isEmpty())
			mOptions.add(new Pair<Option, Integer>(options.poll(),0));
		
		mWinnersCount = (short) mOptions.size();
	}
	
	public STVStation(Pair<Option, Integer>[] options,Queue<Ballot> ballots) {
		mWinners = new ArrayDeque<Option>();
		mBallots = ballots;
		mOptions = new ArrayDeque<Pair <Option, Integer>>();

		for(Pair<Option, Integer> option : options)
			mOptions.add(option);
		
		mWinnersCount = (short) mOptions.size();
	}
	
	public Queue<Option> calcResults() {
		while(mWinners.size() < mWinnersCount) {
			countAndSortVotes();
			removeWinners();
			removeLooser();
		}
		
		return mWinners;
	}
	
	private void removeWinners() {
		while(findWinners() != null) {
			redestributeWinnerVotes(findWinners());
			mOptions.remove(findWinners());
		}
	}
	
	private Pair<Option,Integer> findWinners() {
		Queue<Pair<Option,Integer>> tmp = mOptions;
		
		while(!tmp.isEmpty()) {
			Pair<Option,Integer> curr = tmp.poll();
			if(curr.getValue() >= 1/mWinnersCount * mBallots.size())
				return curr;
		}
		
		return null;
	}
	
	private void removeLooser() {
		Pair<Option,Integer> looser = findLooser();
		mOptions.remove(looser);
		redestributeLooserVotes(looser);
	}
	
	private void redestributeLooserVotes(Pair<Option,Integer> option) {
		Queue<Ballot> tmp = new ArrayDeque<Ballot>();
		
		while(!mBallots.isEmpty()) {
			Ballot curr = mBallots.poll();
		
			if(curr.isFirst(option.getKey())) {
				curr.move();
				tmp.add(curr);
			}
		}
		mBallots.addAll(tmp);
	}
	
	private void redestributeWinnerVotes(Pair<Option,Integer> option) {
		Queue<Ballot> tmp = new ArrayDeque<Ballot>();
		int count = 1;
		
		while(!mBallots.isEmpty()) {
			Ballot curr = mBallots.poll();
			
			if(curr.isFirst(option.getKey())) {
				if(count < 1/mWinnersCount * mBallots.size())
					curr = null;
				else
					curr.move();
				tmp.add(curr);
				count++;
			}
		}
		mBallots.addAll(tmp);
	}
	
	private Pair<Option,Integer> findLooser() {
		Pair<Option,Integer> looser = null;
		Queue<Pair<Option,Integer>> tmp = mOptions;
		
		while(!tmp.isEmpty()) {
			Pair<Option,Integer> curr = tmp.poll();
			
			if(looser.getValue() < curr.getValue())
				looser = curr;
		}
		
		return looser;
	}
	
	private void countAndSortVotes() {
		Queue<Pair <Option,Integer>> tmp = mOptions;
		
		while(!tmp.isEmpty()) {
			countVotes(tmp.poll().getKey());
		}
		sort();
	}
	
	private void sort() {
		Queue<Pair<Option,Integer>> sorted = new ArrayDeque<Pair<Option,Integer>>();
		Queue<Pair<Option,Integer>> tmp = new ArrayDeque<Pair<Option,Integer>>();

		Pair<Option,Integer> max;
		
		while(!mOptions.isEmpty()) {
			max = null;
			tmp = mOptions;
			
			while(!tmp.isEmpty()) {
				Pair<Option,Integer> curr = tmp.poll();
				
				if(curr.getValue() > max.getValue()) 
					max = curr;
			}
			sorted.add(max);
			mOptions.remove(max);
		}
		
		while(!sorted.isEmpty()) {
			mOptions.add(sorted.poll());
		}
	}
	
	private int countVotes(Option option) {
		Queue<Ballot> tmp = mBallots;
		int count = 0;
		
		while(!tmp.isEmpty()) {
			if(tmp.poll().isFirst(option))
				count++;
		}
		return count;
	}
}
