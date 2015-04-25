import java.util.Random;


public class Map {
	private Tile[][] map;
	private int width,height, maxRes;
	private Random rand = new Random();

	public Map(int x, int y) {
		// TODO Auto-generated constructor stub
		width = x;
		height = y;
		maxRes = (int) Math.sqrt(x*y)/2;
		map = new Tile[x][y];
	}
	
	public void randomize() {
		for(int i=0; i<3; i++) {
			for(int j=0;j<maxRes;j++) {
				int randomW = rand.nextInt(width);
				int randomH = rand.nextInt(height);
				
				if(check(randomW, randomH)) {
					j--;
				} else {
					putResource(randomW, randomH, new Resource(i+1));
				}
			}
		}
	}
	
	public void putResource(int x, int y, Resource resource) {
		map[x][y].putResource(resource);
	}
	
	public boolean check(int x, int y) {
		return map[x][y].check(); 
	}
	
	public Resource getResource(int x, int y) {
		return map[x][y].getResource();
	}
		
	public int extract(int x, int y) {
		return map[x][y].extract();
	}
	
	public boolean isOccupied(int x, int y) {
		return map[x][y].isOccupied();
	}
	
	public void setOccupied(int x, int y, boolean isOccupied) {
		map[x][y].setOccupied(isOccupied);
	}
		
	

}