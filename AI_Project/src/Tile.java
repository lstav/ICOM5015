
public class Tile {
	private Resource resource = null;
	private boolean isOccupied = false;
	private int quantity = 0;
	
	public Resource extract() {
		quantity = quantity - 10;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public boolean isOccupied() {{
		return isOccupied;
	}
	
}
