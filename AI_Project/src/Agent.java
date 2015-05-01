
public class Agent {
	private Map map;
	private int xCoor;
	private int yCoor;
	
	private int id;

	private Resource essentialRes;
	private int essentialResQty;

	private Resource desirableRed;
	private int desirableResQty;

	private Resource luxuryRes;
	private int luxuryResQty;
	
	// Coordinates to reach resource
	private int goalX;
	private int goalY;
	
	private boolean traded = false;
	
	private boolean lost = false;
	
	private boolean isRunning;
	
	private Agent agentList[];

	public Agent(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public void run() {
		isRunning = true;
		traded = false;
		
		// TODO do algorithm to search in here
		
		
		
		isRunning = false;
	}
	
	public boolean lost() {
		return lost;
	}
	
	
		
	/**
	 * Returns status of agent
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
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
		
		if(essentialResQty < 3 || desirableResQty < 1) {
			lost = true;
		}
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
	 * @param essential Number of essential resources available on a turn
	 * @param desirable Number of desirable resources available on a turn
	 * @param luxury Number of luxury resources available on a turn
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
	 * @return true if agent can move, false if tile is occupied
	 */
	public boolean moveAgent(int x, int y) {
		if (!map.isOccupied(x, y)) {
			map.setOccupied(xCoor, yCoor, false);
			map.setOccupied(x, y, true);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Receives the list of agents for trading with other agents and agent order
	 * @param agents
	 */
	public void receiveAgentList(Agent agents[]) {
		agentList = agents;
	}
	
	/**
	 * Returns the list of agents to the moderator
	 * @return agentList
	 */
	public Agent[] returnAgentList() {
		return agentList;
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
	 * Sets goal coordinates to reach resource
	 * @param x
	 * @param y
	 */
	public void setGoalCoordinates(int x, int y) {
		goalX = x;
		goalY = y;
	}

	/**
	 * Explores the tile at coordinates x and y
	 * @param x
	 * @param y
	 * @return resource on tile, if any
	 */
	public Resource exploreTile(int x, int y) {
		if(map.check(x, y) && !map.isOccupied(x, y)) {
			return map.getResource(x, y);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns true if agent reached the goal tile
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean reachedGoalTile(int x, int y) {
		return (x == goalX && y == goalY);
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
	 * Returns coordinates of next tile to reach goal node
	 * @param x
	 * @param y
	 * @return
	 */
	public int[] getNextCoordinates(int x, int y) {
		int nextX = x;
		int nextY = y;
		
		if(nextX > goalX) {
			nextX = nextX - 1;
		} else if(nextX < goalX) {
			nextX = nextX + 1;
		} else if(nextX == goalX) {
			nextX = nextX;
		}
		
		if(nextY > goalY) {
			nextY = nextY - 1;
		} else if(nextY < goalY) {
			nextY = nextY + 1;
		} else if(nextY == goalY) {
			nextY = nextY;
		}
		
		
		int[] coordinates = {nextX, nextY};
		return coordinates;
	}

	/**
	 * Makes a trade transaction
	 */
	public void trade() {
		essentialResQty = essentialResQty + 10;
		luxuryResQty = luxuryResQty - 10;
		traded = true;
	}

	/**
	 * Makes a trade with external agent
	 * @param agent
	 */
	public Agent trade(Agent agent) {
		agent.trade();
		this.trade();
		traded = true;
		return agent;
	}
	
	/**
	 * Returns if the agent traded with another agent
	 * @return
	 */
	public boolean traded() {
		return traded;
	}

}
