package stv;

public class Option {
	private String mOption;
	private int mPlace;
	
	public Option(String option, int place) {
		mOption = option;
		try {
			setPlace(place);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Option(String option) {
		mOption = option;
	}
	
	public int getPlace() {
		return mPlace;
	}
	

	public String getOption() {
		return mOption;
	}
	
	protected void setPlace(int place) throws Exception {
		if(place <= 0)
			throw new Exception("Illegal place value");
		mPlace = place;
	}
}
