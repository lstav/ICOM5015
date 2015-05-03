
public class Tile {
	private Resource resource = null;
	private boolean isOccupied = false;
	private int quantity = 0;
	
	public Tile() {
		resource = null;
		isOccupied = false;
		quantity = 0;
	}
	
	/**
	 * Creates new resource with quantity 100 on tile
	 * @param resource
	 */
	public void putResource(Resource resource) {
		this.resource = resource;
		this.quantity = 10;
	}
	
	/**
	 * Checks if tile has a resource
	 * @return boolean
	 */
	public boolean check() {
		return quantity > 0;
	}
	
	/**
	 * Returns resource in tile
	 * @return resource
	 */
	public Resource getResource() {
		return resource;
	}
		
	/**
	 * Extract 10 resource from tile, returns remaining resources
	 * @return
	 */
	public int extract(int qty) {
		quantity = quantity - qty;
		return quantity;
	}
	
	/**
	 * Returns true if agent is in tile
	 * @return isOccupied
	 */
	public boolean isOccupied() {
		return isOccupied;
	}
	
	/**
	 * Puts or removes agent from tile
	 * @param isOccupied
	 */
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
}
