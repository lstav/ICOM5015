
public class Resource {
	private String Name;
	private int quantity;

	public Resource(int i) {
		// TODO Auto-generated constructor stub
		switch (i){
		case 1: Name = "Stone";
		case 2: Name = "Wood";
		case 3: Name = "Iron";
        break;
		default: break;
		}
		quantity = 100;
	}
	
	public int getQuantity(){
		return quantity;
	}

}
