package RangerSlider.core;

public class RangerSlider {
	
	// ATTRIBUTES
	private final int minimum;	// the minimal tolerated and available lower
	private int lower;			// the lower bound of the range
	private int higher;			// the higher bound of the range
	private final int maximum;	// the maximal tolerated and available lower
	
	// SETTER
	public void setlower(int lower) throws RSException {
		if (lower < minimum ||
				lower > higher) {
			throw new RSException("INVALID PARAMETER : LOWER");
		}
		this.lower = lower;
	}
	public void sethigher(int higher) throws RSException {
		if (higher < lower ||
				higher > maximum) {
			throw new RSException("INVALID PARAMETER : HIGHER");
		}
		this.higher = higher;
	}
	
	// GETTER
	public int getMinimum() {
		return minimum;
	}
	public int getlower() {
		return lower;
	}
	public int gethigher() {
		return higher;
	}
	public int getMaximum() {
		return maximum;
	}
	
	// FUNCTIONS
	// get the gap between bounds
	public int getGap() {
		return (higher - lower);
	}
	
	// drag the bounds
	public void drag(int incr) throws RSException{		
		sethigher(this.higher + incr);
		setlower(this.lower + incr);
	}
	
	
	// CONSTRUCTOR
	public RangerSlider(int minimum, int lower, int higher, int maximum) throws RSException {
		// FIXME
		super();
		
		// Verify values
		if (minimum > maximum ||
				lower < minimum ||
				higher > maximum ||
				lower > higher) {
			throw new RSException("INVALID PARAMETER");
		}
		
		// Set the lowers from parameters
		this.minimum = minimum;
		this.lower = lower;
		this.higher = higher;
		this.maximum = maximum;
	}
}
