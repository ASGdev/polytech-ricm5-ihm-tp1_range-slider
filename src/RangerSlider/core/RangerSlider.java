package RangerSlider.core;

import javax.swing.*;
import RangerSlider.ui.*;

public class RangerSlider extends JSlider{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	// =====================================
	// ATTRIBUTES
	// =====================================
	private final int minimum;	// the minimal tolerated and available lower
	private int lower;			// the lower bound of the range
	private int higher;			// the higher bound of the range
	private final int maximum;	// the maximal tolerated and available lower
	
	private RangeSliderUI rsui;	// the associated GUI object
	// =====================================
	// METHODES
	// =====================================
	// sEt lower bound
	public void setlower(int lower) throws RSException {
		if (lower < minimum ||
				lower > higher) {
			throw new RSException("INVALID PARAMETER : LOWER");
		}
		this.lower = lower;
	}
	
	// Set higher bound
	public void sethigher(int higher) throws RSException {
		if (higher < lower ||
				higher > maximum) {
			throw new RSException("INVALID PARAMETER : HIGHER");
		}
		this.higher = higher;
	}
	
	// Get the gap between bounds
	public int getGap() {
		return (higher - lower);
	}
	
	// Drag the bounds
	public void drag(int incr) throws RSException{		
		sethigher(this.higher + incr);
		setlower(this.lower + incr);
	}
	
	// =====================================
	// GETTER AND SETTER
	// =====================================
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
	
	// =====================================
	// CONSTRUCTOR
	// =====================================
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
		
		// Set the ATTRIBUTES from parameters
		this.minimum = minimum;
		this.lower = lower;
		this.higher = higher;
		this.maximum = maximum;
	}
}
