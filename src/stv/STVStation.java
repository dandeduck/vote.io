package stv;

import java.util.ArrayDeque;
import java.util.Queue;

public class STVStation {
	private int mWinnersCount;
	private Queue<Ballot> mBallots;
	private Queue<Result> mResults;
	private Queue<Option> mWinners;

	public STVStation(Queue<Option> options, Queue<Ballot> ballots) {
		mWinners = new ArrayDeque<>();
		mBallots = ballots;
		mResults = new ArrayDeque<>();

		while(!options.isEmpty())
			mResults.add(new Result(options.poll()));


		mWinnersCount = mResults.size();
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
		Option winner = findWinners().getOption();

		while(winner != null) {
			redestributeWinnerVotes(winner);
			mResults.remove(winner);
		}
	}
	
	private Result findWinners() {
		Queue<Result> tmp = mResults;
		
		while(!tmp.isEmpty()) {
			Result curr = tmp.poll();
			if(curr.getVotes() >= 1/mWinnersCount * mBallots.size())
				return curr;
		}
		
		return null;
	}
	
	private void removeLooser() {
		Option looser = findLooser();
		mResults.remove(new Result(looser));
		redestributeLooserVotes(looser);
	}
	
	private void redestributeLooserVotes(Option option) {
		Queue<Ballot> tmp = new ArrayDeque<>();
		
		while(!mBallots.isEmpty()) {
			Ballot curr = mBallots.poll();
		
			if(curr.isFirst(option)) {
				curr.move();
				tmp.add(curr);
			}
		}
		mBallots.addAll(tmp);
	}
	
	private void redestributeWinnerVotes(Option option) {
		Queue<Ballot> tmp = new ArrayDeque<Ballot>();
		int count = 1;

		while(!mBallots.isEmpty()) {
			Ballot curr = mBallots.poll();
			
			if(curr.isFirst(option)) {
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
	
	private Option findLooser() {
		Result looser = null;
		Queue<Result> tmp = mResults;
		
		while(!tmp.isEmpty()) {
			Result curr = tmp.poll();
			
			if(looser.getVotes() < curr.getVotes())
				looser = curr;
		}
		
		return looser.getOption();
	}
	
	private void countAndSortVotes() {
		Queue<Result> tmp = mResults;
		
		while(!tmp.isEmpty()) {
			countVotes(tmp.poll());
		}
		sort();
	}
	
	private void sort() {
		Queue<Result> sorted = new ArrayDeque<>();
		Queue<Result> tmp;
		
		while(!mResults.isEmpty()) {
			Result max = null;
			tmp = mResults;
			
			while(!tmp.isEmpty()) {
				 Result curr = tmp.poll();
				
				if(curr.getVotes() > max.getVotes())
					max = curr;
			}
			sorted.add(max);
			mResults.remove(max);
		}
		
		while(!sorted.isEmpty()) {
			mResults.add(sorted.poll());
		}
	}
	
	private void countVotes(Result result) {
		Queue<Ballot> tmp = mBallots;
		
		while(!tmp.isEmpty()) {
			if (tmp.poll().isFirst(result.getOption()))
				result.addVote();
		}
	}
}
