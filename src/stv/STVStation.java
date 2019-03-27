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

	private Map<Option, Queue<Ballot>> mResults;

	public STVStation(Queue<Ballot> ballots) {
		mResults = new HashMap<>();
		mTotalVotes = ballots.size();

		distributeVotes(ballots);
	}

	private void distributeVotes(Queue<Ballot> ballots) {
		for(Ballot ballot : ballots) {
			Option key = ballot.getFirst();

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
			if(group.contains(ballot))
				return true;
		}
		return false;
	}

	private void shuffleBallots(Option option) {
		List<Queue<Ballot>> ballotGroups = extractGroups(mResults.get(option));
		Queue<Ballot> result = new ArrayDeque<>();

		while(!isEmpty(ballotGroups)) {
			for(Queue<Ballot> group : ballotGroups) {
				while(isTurn(ballotGroups,group.size())) {
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

	private int getVotes(Option option) {
		return mResults.get(option).size();
	}

	private void removeExtraVotes(Option option, int positions) {
		int totalVotes = getVotes(option);
		int extraVotes =  (int) (totalVotes - mTotalVotes * (1.0/positions));

		while(getVotes(option) > extraVotes) {
			mResults.get(option).poll();
		}
	}

	private Queue<Ballot> removeBiggestLoser() {
		AtomicReference<Option> loser = new AtomicReference<>();

		for(Option key : mResults.keySet()) {
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
			Option option = ballot.getFirst();

			if(option != null)
				mResults.get(option).add(ballot);
		}
	}

	private Option getWinner(int positions) {
		Option biggest = getBiggest();
		int winningAmount = (int) (mTotalVotes * (1.0 / (double) positions));

		if(getTotatlVotes() <= winningAmount || mResults.get(biggest).size() >= winningAmount)
			return biggest;
		else
			return null;
	}

	private int getTotatlVotes() {
		AtomicInteger total = new AtomicInteger();

		for(Option option : mResults.keySet()) {
			total.addAndGet(mResults.get(option).size());
		}
		return total.get();
	}

	private Option getBiggest() {
		AtomicReference<Option> max = new AtomicReference<>();

		for(Option option : mResults.keySet()) {
			if (max.get() == null || mResults.get(option).size() > mResults.get(max.get()).size())
				max.set(option);
		}
		return max.get();
	}

	public Queue<Option> calculate(int positions) {
		Set<Option> candidates = mResults.keySet();
		Queue<Option> winners = new ArrayDeque<>();

		if(positions >= candidates.size())
			return new ArrayDeque<>(candidates);

		while(winners.size() < positions) {
			Option winner = getWinner(positions);
			Queue<Ballot> redistributeVotes;

			if(winner != null) {
				winners.add(winner);
				shuffleBallots(winner);
				removeExtraVotes(winner,positions);
				redistributeVotes = mResults.get(winner);

			} else {
				redistributeVotes = removeBiggestLoser();
			}
			redistributeVotes(redistributeVotes);
		}
		return winners;
	}
}
