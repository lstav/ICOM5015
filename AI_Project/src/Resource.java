
public class Resource {
	private String Name;
	private int quantity;

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
		quantity = 100;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public String getResourceName(){
		return Name;
	}
	
	public boolean equals(Resource resource) {
		return this.getResourceName().equals(resource.getResourceName());
	}

}
