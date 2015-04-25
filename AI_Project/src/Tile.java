
public class Tile {
	private Resource resource = null;
	private boolean isOccupied = false;
	private int quantity = 0;
	
	public void putResource(Resource resource) {
		this.resource = resource;
		this.quantity = 100;
	}
	
	public boolean check() {
		return quantity > 0;
	}
	
	public Resource getResource() {
		return resource;
	}
		
	public int extract() {
		quantity = quantity - 10;
		return quantity;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
}
