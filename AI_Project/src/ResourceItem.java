
public class ResourceItem {
	private Resource resource;
	private int xCoor;
	private int yCoor;
	
	/**
	 * Create a resource item with resource and position in the map
	 * @param resource
	 * @param xCoor
	 * @param yCoor
	 */
	public ResourceItem(Resource resource, int xCoor, int yCoor) {
		this.resource = resource;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	/**
	 * Returns the resource
	 * @return
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Returns the x coordinates
	 * @return
	 */
	public int getxCoor() {
		return xCoor;
	}

	/**
	 * Returns the y coordinates
	 * @return
	 */
	public int getyCoor() {
		return yCoor;
	}
	
	

}
