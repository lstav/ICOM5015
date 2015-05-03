
public class ResourceItem {
	private Resource resource;
	private int xCoor;
	private int yCoor;
	
	public ResourceItem(Resource resource, int xCoor, int yCoor) {
		this.resource = resource;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	public Resource getResource() {
		return resource;
	}

	public int getxCoor() {
		return xCoor;
	}

	public int getyCoor() {
		return yCoor;
	}
	
	

}
