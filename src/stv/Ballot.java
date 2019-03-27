package stv;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class Ballot {
	private Queue<Option> mOptions;
	
	public Ballot(Queue<Option> options) {
		mOptions = sort(options);
	}
	
	public void move() {
		Queue<Option> tmp;
		
		try {
			mOptions.poll();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		tmp = mOptions;
		
		while(!tmp.isEmpty()) {
			Option option = tmp.poll();
			
			try {
				option.setPlace(option.getPlace()-1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Queue<Option> sort(Queue<Option> unsorted) {
		ArrayList<Option> tmp = new ArrayList<>();
		
		while(!unsorted.isEmpty()) {
			Option option = unsorted.poll();
			tmp.add(option.getPlace(), option);
		} for(Option option : tmp) {
			unsorted.add(option);
		}
		
		return reverseQueue(unsorted);
	}
	
	private Queue<Option> reverseQueue(Queue<Option> queue) {
		Stack<Option> tmp  = new Stack<>();
		
		while(!queue.isEmpty()) {
			tmp.add(queue.poll());
		} for(Option option : tmp) {
			queue.add(option);
		}
		return queue;
	}

	public int getLastPlace() {
		Queue<Option> tmp = mOptions;
		int last = 1;

		while(tmp.isEmpty()) {
			int curr  = tmp.poll().getPlace();

			if(curr > last)
				last = curr;
		}

		return last;
	}
	
	public boolean isFirst(Option option) {
		return mOptions.peek().equals(option);
	}

	public Option getFirst() {
		return mOptions.peek();
	}
}
