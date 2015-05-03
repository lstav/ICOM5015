import java.util.ArrayList;
import java.util.Collections;


public class Agent {
	private Map map;
	private int xCoor;
	private int yCoor;

	private int id;

	private Resource essentialRes;
	private int essentialResQty;

	private Resource desirableRes;
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

	private ArrayList<ResourceItem> resourceList = new ArrayList<>();

	public Agent(int id) {
		this.id = id;
	}

	/**
	 * Returns agent's unique ID
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Runs the agent
	 */
	public void run() {
		isRunning = true;
		traded = false;

		// TODO do algorithm to search in here
		ResourceItem closestResource = getClosestResource(); // Finds best resource to get
		setGoalCoordinates(closestResource.getxCoor(), closestResource.getyCoor()); // Sets coordinates to reach resource

		int[] nextCoor = getNextCoordinates(xCoor, yCoor);

		if(reachedGoalTile(xCoor, yCoor) && map.check(xCoor, yCoor)) {
			//if(xCoor == goalX && yCoor == goalY && map.check(goalX, goalY)) {
			System.out.println(essentialRes.getResourceName() + " " + essentialResQty 
					+ " " + desirableRes.getResourceName() + " " + desirableResQty +
					" " + luxuryRes.getResourceName() + " " + luxuryResQty);
			int extractQty = 0;
			if(map.getResource(xCoor, yCoor).equals(essentialRes)) {
				extractQty = 10;
			} else if(map.getResource(xCoor, yCoor).equals(desirableRes)) {
				extractQty = 10;
			} else if(map.getResource(xCoor, yCoor).equals(luxuryRes)) {
				extractQty = 10;
			}

			System.out.println("Agent " + getID() + " getting resource " + map.getResource(xCoor, yCoor).getResourceName() 
					+ " on {" + xCoor + ", " + yCoor + "} " + "Quantity left " + map.extract(xCoor, yCoor, extractQty));
			getResourceOnExtract(map.getResource(xCoor, yCoor), 10);
		} else {
			System.out.println(essentialRes.getResourceName() + " " + essentialResQty 
					+ " " + desirableRes.getResourceName() + " " + desirableResQty +
					" " + luxuryRes.getResourceName() + " " + luxuryResQty);
			System.out.print("Moving agent " + getID() + " from {" + xCoor +", " + yCoor + "} to ");
			moveAgent(nextCoor[0], nextCoor[1]);
			System.out.print("{" + xCoor +", " + yCoor + "} ");
			System.out.print("to reach goal {" + goalX +", " + goalY + "}");
			System.out.println(" and resource " + map.getResource(goalX, goalY).getResourceName());

		}

		//System.out.println("Agent ID " + getID() + " {" + xCoor + "," + yCoor + "}");

		isRunning = false;
	}

	public void getResourceOnExtract(Resource resource, int quantity) {
		if(resource.equals(essentialRes)) {
			essentialResQty = essentialResQty + 10;
		} else if(resource.equals(desirableRes)) {
			desirableResQty = desirableResQty + 10;
		} else if(resource.equals(luxuryRes)) {
			luxuryResQty = luxuryResQty + 10;
		}
	}

	public ResourceItem getClosestResource() {
		int aX = xCoor;
		int aY = yCoor;
		int rX;
		int rY;
		int tileNumber1;
		int turnScore1;
		int currentScore;

		//Collections.shuffle(resourceList);

		rX = resourceList.get(0).getxCoor();
		rY = resourceList.get(0).getyCoor();

		currentScore = getCurrentScore();
		tileNumber1 = getNumberOfTilesToGoal(rX, rY);
		turnScore1 = getTurnScore(essentialResQty, desirableResQty, luxuryResQty, 
				tileNumber1, resourceList.get(0).getResource());


		for(int i = 0; i < resourceList.size(); i++) {
			ResourceItem resource = resourceList.get(i);

			int tempX = resource.getxCoor();
			int tempY = resource.getyCoor();
			int tileNumber2 = getNumberOfTilesToGoal(tempX, tempY);
			int turnScore2 = getTurnScore(essentialResQty, desirableResQty, luxuryResQty, 
					tileNumber2, resource.getResource());

			if(((currentScore <= turnScore2) || (turnScore1 <= turnScore2)) &&
					(!map.isOccupied(tempX, tempY) || (reachedGoalTile(aX, aY))) 
					&& (map.check(tempX, tempY))) {				
				rX = tempX;
				rY = tempY;
				tileNumber1 = getNumberOfTilesToGoal(rX, rY);
				turnScore1 = getTurnScore(essentialResQty, desirableResQty, luxuryResQty, 
						tileNumber1, resource.getResource());
			}
		}
		return new ResourceItem(map.getResource(rX, rY), rX, rY);
	}

	/**
	 * Checks if agent lost
	 * @return
	 */
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

		desirableRes = desirable;
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

		if(essentialResQty < 0 || desirableResQty < 0) {
			lost = true;
			map.setOccupied(xCoor, yCoor, false);
			System.out.println("Lost Agent " + getID() + " Final Score " + getCurrentScore());
			System.out.println("");
		}
	}

	/**
	 * Returns the current score of the agent
	 * @return current score
	 */
	public int getCurrentScore() {

		return (int) (essentialResQty*.70 + desirableResQty*.25 + luxuryResQty*.05);
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

		int ess = essential - (turnNumber*3) + (resource.equals(essentialRes)?10:0);
		int des = desirable - (turnNumber*1) + (resource.equals(desirableRes)?10:0);
		int lux = luxury - (turnNumber*0) + (resource.equals(luxuryRes)?10:0);

		if(ess <= 0 || des <= 0) {
			return 0;
		}

		return (int) (ess*.70 + des*.25 + lux*.05);
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
		boolean prevOccupied = map.isOccupied(x, y);

		map.setOccupied(xCoor, yCoor, prevOccupied);
		map.setOccupied(x, y, true);
		xCoor = x;
		yCoor = y;

		return prevOccupied;

	}

	/**
	 * Receives the list of agents for trading with other agents and agent order
	 * @param agents
	 */
	public void receiveAgentList(Agent agents[]) {
		agentList = agents;
	}

	public void receiveResourceList(ArrayList<ResourceItem> resourceList) {
		this.resourceList = resourceList;	
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
	public int[] getNextCoordinates(int currentX, int currentY) {
		int nextX = currentX;
		int nextY = currentY;

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

	public int getNumberOfTilesToGoal(int currentX, int currentY) {
		int tileNumber = 0;
		int nextX = xCoor;
		int nextY = yCoor;
		if(nextX == currentX && nextY == currentY) {
			return 1;
		}
		do {
			if(nextX > currentX) {
				nextX = nextX - 1;
			} else if(nextX < currentX) {
				nextX = nextX + 1;
			} 

			if(nextY > currentY) {
				nextY = nextY - 1;
			} else if(nextY < currentY) {
				nextY = nextY + 1;
			} 
			tileNumber++;
		} while(nextX != currentX || nextY != currentY);

		return tileNumber+1;
	}

	/**
	 * Makes a trade transaction
	 */
	public void trade(Resource otherLuxuryRes) {
		if(otherLuxuryRes.equals(essentialRes)) {
			essentialResQty = essentialResQty + 10;	
		} else if(otherLuxuryRes.equals(desirableRes)) {
			desirableResQty = desirableResQty + 10;
		} else if(otherLuxuryRes.equals(luxuryRes)) {
			luxuryResQty = luxuryResQty + 10;
		}

		luxuryResQty = luxuryResQty - 10;
		traded = true;
	}

	/**
	 * Makes a trade with external agent
	 * @param agent
	 */
	public Agent trade(Agent agent, Resource otherLuxuryRes) {
		agent.trade(getLuxuryRes());
		this.trade(otherLuxuryRes);
		traded = true;
		return agent;
	}

	/**
	 * Gets agent's essential resource
	 * @return
	 */
	public Resource getEssentialRes() {
		return essentialRes;
	}

	/**
	 * Gets agent's essential resource quantity
	 * @return
	 */
	public int getEssentialResQty() {
		return essentialResQty;
	}

	/**
	 * Gets agent's desirable resource
	 * @return
	 */
	public Resource getDesirableRes() {
		return desirableRes;
	}

	/**
	 * Gets agent's desirable resource quantity
	 * @return
	 */
	public int getDesirableResQty() {
		return desirableResQty;
	}

	/**
	 * Gets agent's luxury resource
	 * @return
	 */
	public Resource getLuxuryRes() {
		return luxuryRes;
	}

	/**
	 * Gets agent's luxury resource quantity
	 * @return
	 */
	public int getLuxuryResQty() {
		return luxuryResQty;
	}

	/**
	 * Returns if the agent traded with another agent
	 * @return
	 */
	public boolean traded() {
		return traded;
	}

	/**
	 * Checks if agent can trade
	 * @return
	 */
	public boolean canTrade() {
		return luxuryResQty >= 10;
	}

}
