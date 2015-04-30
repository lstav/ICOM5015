
public class Agent {
	private Map map;
	private int xCoor;
	private int yCoor;
	
	private Resource essentialRes;
	private int essentialResQty;
	
	private Resource desirableRed;
	private int desirableResQty;
	
	private Resource luxuryRes;
	private int luxuryResQty;
	
	public Agent() {
		
	}
	
	/**
	 * Sets the essential, desirable and luxury resources for agent
	 * @param essential
	 * @param desirable
	 * @param luxury
	 */
	public void setResources(Resource essential, Resource desirable, Resource luxury) {
		essentialRes = essential;
		essentialResQty = 20;
		
		desirableRed = desirable;
		desirableResQty = 15;
		
		luxuryRes = luxury;
		luxuryResQty = 15;
	}
	
	/**
	 * Removes resources after turn passed
	 */
	public void turnPassed() {
		essentialResQty = essentialResQty - 3;
		desirableResQty = desirableResQty - 1;
	}
	
	/**
	 * Returns the current score of the agent
	 * @return current score
	 */
	public int getCurrentScore() {
		return (int) (essentialResQty*.72 + desirableResQty*.24 + luxuryResQty*.04);
	}
	
	/**
	 * Get array with resources quantities
	 * @return
	 */
	public int[] getCurrentQty() {
		int[] quantities = {essentialResQty, desirableResQty, luxuryResQty};
		return quantities;
	}
	
	/**
	 * Returns the score the agent would get on a turn
	 * @param essential Number of essential resources taken on a turn
	 * @param desirable Number of desirable resources taken on a turn
	 * @param luxury Number of luxury resources taken on a turn
	 * @param turnNumber Number of turns to reach and extract a resource
	 * @param resource Possible resource to extract from tile
	 * @return score that the agent would get on a given turn
	 */
	public int getTurnScore(int essential, int desirable, int luxury, int turnNumber, Resource resource) {
		
		essential = essential - (turnNumber*3) + (resource.equals(essentialRes)?10:0);
		desirable = desirable - (turnNumber*1) + (resource.equals(desirableRed)?10:0);;
		luxury = luxury - (turnNumber*0) + (resource.equals(luxuryRes)?10:0);;
		
		return (int) (essential*.72 + desirable*.24 + luxury*.04);
	}
	
	/**
	 * Gets the map from moderator
	 * @param map
	 */
	public void getMap(Map map) {
		this.map = map;
	}
	
	/**
	 * Return modified map to moderator
	 * @return
	 */
	public Map returnMap() {
		return map;
	}
	
	/**
	 * Moves agent to new position, sets previous tile to empty and new tile to occupied
	 * @param x
	 * @param y
	 */
	public void moveAgent(int x, int y) {
		map.setOccupied(xCoor, yCoor, false);
		map.setOccupied(x, y, true);
	}
	
	
	public void requestNextAgent() {
		
	}
	
	/**
	 * Sets agent coordinates
	 * @param x
	 * @param y
	 */
	public void setCoordinates(int x, int y) {
		xCoor = x;
		yCoor = y;
	}
	
	
	/**
	 * Return agent coordinates
	 * @return
	 */
	public int[] getCoordinates() {
		int[] coordinates = {xCoor, yCoor};
		return coordinates;
	}
	
	/**
	 * Makes a trade transaction
	 */
	public void trade() {
		essentialResQty = essentialResQty + 10;
		luxuryResQty = luxuryResQty - 10;
	}
	
	/**
	 * Makes a trade with external agent
	 * @param agent
	 */
	public Agent trade(Agent agent) {
		agent.trade();
		this.trade();
		return agent;
	}
	
}
