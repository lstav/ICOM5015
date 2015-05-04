import java.util.Random;


public class Map {
	private Tile[][] map;
	private int width,height, maxRes;
	private Random rand = new Random();

	public Map(int x, int y) {
		// TODO Auto-generated constructor stub
		width = x;
		height = y;
		maxRes = (int) Math.sqrt(x*y); // Maximum number per resource
		map = new Tile[x][y];
		initializeTiles();
	}	
	
	
	
	public int getWidth() {
		return width;
	}



	public int getHeight() {
		return height;
	}



	public void initializeTiles() {
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				map[i][j] = new Tile();
			}
		}
	}
	
	/**
	 * Creates random resources in the map, distributes equal number of
	 * resources maxRes through all resource types	 
	 */
	public void randomize() {
		for(int i=0; i<3; i++) {
			for(int j=0;j<maxRes;j++) {
				int randomW = rand.nextInt(width);
				int randomH = rand.nextInt(height);
				
				/*
				 * Checks if random position has a resource, if it does
				 * try again, else puts the resource in the random position
				 */
				if(check(randomW, randomH)) {
					j--;
				} else {
					putResource(randomW, randomH, new Resource(i+1));
				}
			}
		}
	}
	
	/**
	 * Puts single resource on a random tile
	 */
	private void randomize(Resource resource) {
		int randomW;
		int randomH;
		/*
		 * Checks for available tile
		 */
		do {
			randomW = rand.nextInt(width);
			randomH = rand.nextInt(height);
		} while(check(randomW, randomH));
		/*
		 * Puts resource on selected tile
		 */
		putResource(randomW, randomH, resource);
	}	
	
	/**
	 * Puts resource on x and y coordinates
	 * @param x
	 * @param y
	 * @param resource
	 */
	public void putResource(int x, int y, Resource resource) {
		map[x][y].putResource(resource);
	}
	
	/**
	 * Checks if coordinates x and y are occupied by a resource
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean check(int x, int y) {
		return map[x][y].check(); 
	}
	
	/**
	 * Gets resource from coordinates x and y
	 * @param x
	 * @param y
	 * @return resource
	 */
	public Resource getResource(int x, int y) {
		return map[x][y].getResource();
	}
	
	/**
	 * Extracts resource from coordinate, if resource is empty,
	 * creates it on a random tile
	 * @param x
	 * @param y
	 * @return quantity left
	 */
	public int extract(int x, int y, int qty2) {
		int qty = map[x][y].extract(qty2); 
		if(qty == 0) {
			randomize(getResource(x, y));
		}
		return qty;
	}
	
	/**
	 * Checks if tile at coordinates x and y has an agent 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isOccupied(int x, int y) {
		return map[x][y].isOccupied();
	}
	
	/**
	 * Puts or removes agent from tile at coordinates x and y
	 * @param x
	 * @param y
	 * @param isOccupied
	 */
	public void setOccupied(int x, int y, boolean isOccupied) {
		map[x][y].setOccupied(isOccupied);
	}
		
}