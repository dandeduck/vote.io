package stv;

public class Option {
	private String mOption;
	private int mPlace;
	
	public Option(String option, int place, int lastPlace) {
		mOption = option;

		try {
			setPlace(place, lastPlace);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getPlace() {
		return mPlace;
	}
	
	private void setPlace(int place, int lastPlace) throws Exception {
		if(place <= 0 || place > lastPlace)
			throw new Exception("Illegal place value");
		mPlace = place;
	}
}
