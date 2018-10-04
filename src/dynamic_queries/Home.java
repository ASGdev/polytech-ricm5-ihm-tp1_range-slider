package dynamic_queries;

import parameters.Application_variables;

public class Home {

	// =============================================
	// ===== ATTRIBUTS
	// =============================================
	private int coord_x;
	private int coord_y;
	private int nb_room;
	private int value;

	// =============================================
	// ===== CONSTRUCTOR
	// =============================================
	public Home(double a, double b, double c, double d) throws HomeException {
		int x = (int) a;
		int y = (int) b;
		int rooms = (int) c;
		int price = (int) d;

		if (rooms < 0 || price < 0 || x > Application_variables.max_x || x < Application_variables.min_x
				|| y > Application_variables.max_y || y < Application_variables.min_y)
			throw new HomeException();

		this.coord_x = x;
		this.coord_y = y;
		this.nb_room = rooms;
		this.value = price;
	}

	// =============================================
	// ===== SETTER AND GETTER
	// =============================================
	public int getCoord_x() {
		return coord_x;
	}

	public int getCoord_y() {
		return coord_y;
	}

	public int getNb_room() {
		return nb_room;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) throws HomeException {
		if (value < 0)
			throw new HomeException();
		this.value = value;
	}

	// =============================================
	// ===== METHODES
	// =============================================

}
