package RangerSlider.main;

import RangerSlider.core.*;
import RangerSlider.ui.*;


public class main_main {

	public static void main(String[] args) {
		System.out.println("___STARTING APPLICATION");
		
		
		RangerSlider rs;
		try {
			rs = new RangerSlider(0, 1, 2, 3);
			RangeSliderUI rsui = new RangeSliderUI(rs);

		}catch (Exception e) {

		}

		
		System.out.println("___ENDING APPLICATION");
	}
}
