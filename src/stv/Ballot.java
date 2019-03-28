package stv;

import java.util.*;

public class Ballot {
	private Queue<Option> mOptions;
	
	public Ballot(Option... options) {
		mOptions = sort(options);
	}
	
	public void move() {
		mOptions.poll();
	}

	private Queue<Option> sort(Option... unsorted) {
		List<Option> tmp = new ArrayList<>();
		int optionsLength = unsorted[0].getLastPlace();

		initList(tmp, optionsLength);

		for(Option option : unsorted){
			int place = option.getPlace()-1;

			tmp.remove(place);
			tmp.add(place, option);
		}
		return new ArrayDeque<>(tmp);
	}

	private void initList(List<Option> options, int size) {
		for(int i = 0; i < size; ++i) {
			options.add(i, null);
		}
	}

	public Option getFirst() {
		return mOptions.peek();
	}

	public String toString() {
		String options = "";

		for(Option option :mOptions) {
			options += option.getOption() + ",";
		}
		options = options.substring(0,options.length()-1);

		return options;
	}
}
