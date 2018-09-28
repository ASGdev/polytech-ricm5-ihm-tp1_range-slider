package dynamic_queries;

public class Home {

	/*
	 * ======= ATTRIBUTS
	 */
	private float coord_x;
	private float coord_y;
	private float nb_room;
	private float value;

	/*
	 * ======= CONSTRUCTOR
	 */
	public Home(float x, float y, float rooms, float price) throws HomeException {
		if (rooms < 0 || price < 0 || x > Application.max_x || x < Application.min_x || y > Application.max_y
				|| y < Application.min_y)
			throw new HomeException();

		this.coord_x = x;
		this.coord_y = y;
		this.nb_room = rooms;
		this.value = price;
	}

	/*
	 * ======= METHODES
	 */
	public float getCoord_x() {
		return coord_x;
	}

	public float getCoord_y() {
		return coord_y;
	}

	public float getNb_room() {
		return nb_room;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) throws HomeException {
		if (value < 0)
			throw new HomeException();
		this.value = value;
	}

}
