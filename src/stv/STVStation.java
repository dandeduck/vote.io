package stv;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class STVStation {
	private int mTotalVotes;

	private Map<String, Queue<Ballot>> mResults;

	public STVStation() {
		mResults = new HashMap<>();
	}

	public void setBallots(Queue<Ballot> ballots) {
		distributeVotes(ballots);
		mTotalVotes = ballots.size();
	}

	private void distributeVotes(Queue<Ballot> ballots) {
		for(Ballot ballot : ballots) {
			String key = ballot.getFirst().getOption();

			if(!mResults.containsKey(key))
				mResults.put(key,new ArrayDeque<>());

			mResults.get(key).add(ballot);
		}
	}

	private List<Queue<Ballot>> extractGroups(Queue<Ballot> ballots) {
		List<Queue<Ballot>> groups = new ArrayList<>();

		for(Ballot ballot : ballots) {
			if(isInGroups(groups,ballot))
				for(Queue<Ballot> group : groups) {
					if (group.contains(ballot))
						group.add(ballot);
				}
			else {
				groups.add(new ArrayDeque<>());
				groups.get(groups.size()-1).add(ballot);
			}
		}

		return groups;
	}

	private boolean isInGroups(List<Queue<Ballot>> groups, Ballot ballot) {
		for(Queue<Ballot> group : groups) {
			for(Ballot item : group) {
				if(item.toString().equals(ballot.toString()))
					return true;
			}
		}
		return false;
	}

	private void shuffleBallots(String option) {
		List<Queue<Ballot>> ballotGroups = extractGroups(mResults.get(option));
		Queue<Ballot> result = new ArrayDeque<>();

		System.out.println(ballotGroups);

		while(!isEmpty(ballotGroups)) {
			for(Queue<Ballot> group : ballotGroups) {
				while(!group.isEmpty() && isTurn(ballotGroups,group.size())) {
					result.add(group.poll());
				}
			}
		}
		mResults.replace(option, reverseQueue(result));
	}

	private Queue<Ballot> reverseQueue(Queue<Ballot> queue) {
		Stack<Ballot> tmp  = new Stack<>();

		while(!queue.isEmpty()) {
			tmp.add(queue.poll());
		} queue.addAll(tmp);
		return queue;
	}

	private boolean isEmpty(List<Queue<Ballot>> set) {
		for(Queue<Ballot> items : set) {
			if(!items.isEmpty())
				return false;
		}
		return true;
	}

	private boolean isTurn(List<Queue<Ballot>> groups, int groupSize) {
		for(Queue<Ballot> group : groups) {
			if (group.size() > groupSize)
				return false;
		}
		return true;
	}

	private int getVotes(String option) {
		return mResults.get(option).size();
	}

	private void removeExtraVotes(String option, int positions) {
		int totalVotes = getVotes(option);
		int extraVotes =  (int) (totalVotes - mTotalVotes * (1.0/positions));

		while(getVotes(option) > extraVotes) {
			mResults.get(option).poll();
		}
	}

	private Queue<Ballot> removeBiggestLoser() {
		AtomicReference<String> loser = new AtomicReference<>();

		for(String key : mResults.keySet()) {
			if(loser.get() == null || getVotes(key) < getVotes(loser.get())) {
				loser.set(key);
			}
		}

		Queue<Ballot> leftoverVotes = mResults.get(loser.get());
		mResults.remove(loser.get());

		return leftoverVotes;
	}

	private void redistributeVotes(Queue<Ballot> ballots) {
		for(Ballot ballot : ballots) {
			String  option = ballot.getFirst().getOption();

			if(option != null)
				mResults.get(option).add(ballot);
		}
	}

	private String getWinner(int positions) {
		String biggest = getBiggest();
		int winningAmount = (int) (mTotalVotes * (1.0 / (double) positions));

		if(getTotalVotes() <= winningAmount || mResults.get(biggest).size() >= winningAmount)
			return biggest;
		else
			return null;
	}

	private int getTotalVotes() {
		AtomicInteger total = new AtomicInteger();

		for(String option : mResults.keySet()) {
			total.addAndGet(mResults.get(option).size());
		}
		return total.get();
	}

	private String getBiggest() {
		AtomicReference<String> max = new AtomicReference<>();

		for(String option : mResults.keySet()) {
			if (max.get() == null || mResults.get(option).size() > mResults.get(max.get()).size())
				max.set(option);
		}
		return max.get();
	}

	private void move(Queue<Ballot> ballots) {
		for(Ballot ballot : ballots)
			ballot.move();
	}

	public Queue<String> calculate(int positions) {
		Set<String> candidates = mResults.keySet();
		Queue<String> winners = new ArrayDeque<>();

		if(mResults.isEmpty())
			return null; //Maybe throw a custom Exception here

		if(positions >= candidates.size())
			return new ArrayDeque<>(candidates);

		while(winners.size() < positions) {
			String winner = getWinner(positions);
			Queue<Ballot> redistributeVotes;

			if(winner != null) {
				winners.add(winner);
				shuffleBallots(winner);
				removeExtraVotes(winner,positions);
				redistributeVotes = mResults.get(winner);

			} else
				redistributeVotes = removeBiggestLoser();

			move(redistributeVotes);
			redistributeVotes(redistributeVotes);
		}
		return winners;
	}
}
