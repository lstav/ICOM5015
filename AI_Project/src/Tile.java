
public class Tile {
	private Resource resource = null;
	private boolean isOccupied = false;
	private int quantity = 0;
	
	public void putResource(Resource resource) {
		this.resource = resource;
		this.quantity = 100;
	}
	
	public Resource check() {
		return resource;
	}
		
	public int extract() {
		quantity = quantity - 10;
		return quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
}
