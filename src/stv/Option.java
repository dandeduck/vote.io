package stv;

public class Option {
	private String mOption;
	private int mPlace;
	private int mLastPlace;
	
	public Option(String option, int place, int lastPlace) {
		mOption = option;
		mLastPlace = lastPlace;

		try {
			setPlace(place, lastPlace);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getPlace() {
		return mPlace;
	}

	public String getOption() {
		return mOption;
	}
	public int getLastPlace() {
		return mLastPlace;
	}

	private void setPlace(int place, int lastPlace) throws Exception {
		if(place <= 0 || place > lastPlace)
			throw new Exception("Illegal place value");
		mPlace = place;
	}
}
