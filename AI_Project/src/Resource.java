
public class Resource {
	private String Name;
	private int quantity;

	/**
	 * Select resource type
	 * @param i type of resource
	 */
	public Resource(int i) {
		// TODO Auto-generated constructor stub
		switch (i){
		case 1: Name = "Stone";
		break;
		case 2: Name = "Wood";
		break;
		case 3: Name = "Iron";
        break;
		default: break;
		}
	}
	
	/**
	 * Returns resource name
	 * @return
	 */
	public String getResourceName(){
		return Name;
	}
	
	/**
	 * Checks if two resources are the same type
	 * @param resource
	 * @return
	 */
	public boolean equals(Resource resource) {
		if(resource == null) {
			return false;
		}
		return this.getResourceName().equals(resource.getResourceName());
	}

}
