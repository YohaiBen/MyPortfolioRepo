package app.core.entities;

public enum Category {

	Food(1), Electricity(2), Restaurant(3), Vacation(4), Sport(5);

	private int id;

	private Category(int id) {
		this.id = id;
	}

	public static Category getCategoryById(int id) {
		for (Category c : Category.values()) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;

	}

	public int getId() {
		return id;
	}

}