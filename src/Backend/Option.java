package Backend;

public class Option {
	private String mOption;
	private short mPlace;
	
	public Option(String option, short place) {
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
	
	public short getPlace() {
		return mPlace;
	}
	

	public String getOption() {
		return mOption;
	}
	
	protected void setPlace(short place) throws Exception {
		if(place <= 0)
			throw new Exception("Illegal place value");
		mPlace = place;
	}
}
