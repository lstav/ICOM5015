import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Agent {
	private Map map;
	private int xCoor;
	private int yCoor;

	private AlphaBeta AB;

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

	private ArrayList<Agent> agentList = new ArrayList<>();

	private ArrayList<ResourceItem> resourceList = new ArrayList<>();

	/**
	 * Creates agent and gives it a unique ID
	 * @param id
	 */
	public Agent(int id) {
		this.id = id;
		AB = new AlphaBeta();
	}

	/**
	 * Returns agent's unique ID
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Makes a copy of the array of agents
	 * @param agents
	 * @return
	 */
	public ArrayList<Agent> copyArray(ArrayList<Agent> agents) {
		ArrayList<Agent> aglist = new ArrayList<>();

		for (int i = 0; i < agents.size(); i++) {
			aglist.add(agents.get(i).clone());
		}

		return aglist;
	}

	/**
	 * Runs the agent
	 */
	public void run() {
		isRunning = true; // Starts running
		traded = false;

		ArrayList<Agent> agentsOfShield = new ArrayList<>();

		agentsOfShield = copyArray(agentList);

		AB.uploadList(agentsOfShield); // Upload the agent list to the ab pruning class

		AB.prunn(getID(), 3); // Starts the pruning with initial agent

		ResourceItem closestResource = getBestResource(); // Finds best resource to get

		/*
		 * Checks if there was a trade
		 */
		if(!traded()) {
			setGoalCoordinates(closestResource.getxCoor(), closestResource.getyCoor()); // Sets coordinates to reach resource

			int[] nextCoor = getNextCoordinates(xCoor, yCoor); // Gets the next coordinates to reach goal

			/*
			 * Extracts resource or moves to other resource
			 */
			if(reachedGoalTile(xCoor, yCoor) && map.check(xCoor, yCoor)) {
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
		}

		isRunning = false; // Stops running
	}

	/**
	 * Adds the extracted resource to agent
	 * @param resource extracted resource
	 * @param quantity 
	 */
	public void getResourceOnExtract(Resource resource, int quantity) {
		if(resource.equals(essentialRes)) {
			essentialResQty = essentialResQty + 10;
		} else if(resource.equals(desirableRes)) {
			desirableResQty = desirableResQty + 10;
		} else if(resource.equals(luxuryRes)) {
			luxuryResQty = luxuryResQty + 10;
		}
	}	

	/**
	 * Finds the best resource to extract
	 * @return resource
	 */
	public ResourceItem getBestResource() {
		int aX = xCoor;
		int aY = yCoor;
		int rX;
		int rY;
		int tileNumber1;
		int turnScore1;

		/*
		 * Saves first result's location
		 */
		rX = resourceList.get(0).getxCoor();
		rY = resourceList.get(0).getyCoor();

		tileNumber1 = getNumberOfTilesToGoal(rX, rY); // Number of tiles to reach first resource
		turnScore1 = getTurnScore(essentialResQty, desirableResQty, luxuryResQty, 
				tileNumber1, resourceList.get(0).getResource()); // Gets the score to reach first resource

		for(int i = 0; i < resourceList.size(); i++) {
			ResourceItem resource = resourceList.get(i);

			/*
			 * Saves next result's location
			 */
			int tempX = resource.getxCoor();
			int tempY = resource.getyCoor();
			int tileNumber2 = getNumberOfTilesToGoal(tempX, tempY); // Number of tiles to reach next resource
			int turnScore2 = getTurnScore(essentialResQty, desirableResQty, luxuryResQty, 
					tileNumber2, resource.getResource()); // Gets the score to reach next resource

			/*
			 * Checks if the next result has a better score and if it can be gathered
			 */
			if(((turnScore1 <= turnScore2)) &&
					(!map.isOccupied(tempX, tempY) || (reachedGoalTile(aX, aY))) 
					&& (map.check(tempX, tempY))) {				
				/*
				 * Sets current location to the highest score location
				 */
				rX = tempX;
				rY = tempY;
				tileNumber1 = getNumberOfTilesToGoal(rX, rY);
				turnScore1 = getTurnScore(essentialResQty, desirableResQty, luxuryResQty, 
						tileNumber1, resource.getResource());
			}
		}

		/*
		 * Checks if the pruning returned a better score than the gathering or moving in the map
		 */
		if(AB.getAgent() != 6) {
			if(turnScore1 < AB.getValue()) {
				// Checks if agents can trade
				if(agentList.get(AB.getAgent()).canTrade() && canTrade()) {
					trade(agentList.get(AB.getAgent()), agentList.get(AB.getAgent()).getLuxuryRes());
					System.out.println("Agent " + getID() + " traded with " + AB.getAgent());
				}
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
	public void setResources(int width, int height, Resource essential, Resource desirable, Resource luxury) {
		int x = width;
		int y = height;

		essentialRes = essential;
		essentialResQty = (int) Math.sqrt(x*y)*3;

		desirableRes = desirable;
		desirableResQty = (int) Math.sqrt(x*y)*2;

		luxuryRes = luxury;
		luxuryResQty = (int) Math.sqrt(x*y)*1;
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

		int ess = 0;
		int des = 0;
		int lux = 0;
		if(resource == null) {
			ess = essential - (turnNumber*3);
			des = desirable - (turnNumber*1);
			lux = luxury - (turnNumber*0);
		} else {
			ess = essential - (turnNumber*3) + (resource.equals(essentialRes)?10:0);
			des = desirable - (turnNumber*1) + (resource.equals(desirableRes)?10:0);
			lux = luxury - (turnNumber*0) + (resource.equals(luxuryRes)?10:0);
		}
		if(ess < 0 || des < 0) {
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
	public void receiveAgentList(ArrayList<Agent> agents) {
		agentList = agents;
	}

	/**
	 * Gets resource list on map from moderator
	 * @param resourceList
	 */
	public void receiveResourceList(ArrayList<ResourceItem> resourceList) {
		this.resourceList = resourceList;	
	}

	/**
	 * Returns the list of agents to the moderator
	 * @return agentList
	 */
	public ArrayList<Agent> returnAgentList() {
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
		} 

		if(nextY > goalY) {
			nextY = nextY - 1;
		} else if(nextY < goalY) {
			nextY = nextY + 1;
		} 

		int[] coordinates = {nextX, nextY};
		return coordinates;
	}

	/**
	 * Returns the number of tiles to reach goal
	 * @param goalX goal x coordinates
	 * @param goalY goal y coordinates
	 * @return
	 */
	public int getNumberOfTilesToGoal(int goalX, int goalY) {
		int tileNumber = 1;
		int nextX = xCoor;
		int nextY = yCoor;

		while(nextX != goalX || nextY != goalY) {
			if(nextX > goalX) {
				nextX = nextX - 1;
			} else if(nextX < goalX) {
				nextX = nextX + 1;
			} 

			if(nextY > goalY) {
				nextY = nextY - 1;
			} else if(nextY < goalY) {
				nextY = nextY + 1;
			} 
			tileNumber++;
		}

		return tileNumber;
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
		trade(otherLuxuryRes);
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

	/**
	 * Distributes the resources for the copy of the agent
	 * @param essential Indicates the Essential Resource
	 * @param essQty Indicates the Essential Resource Quantity
	 * @param desirable Indicates the Desirable Resource
	 * @param desQty Indicates the Desirable Resource Quantity
	 * @param luxury Indicates the Luxury Resource
	 * @param luxQty Indicates the Luxury Resource Quantity
	 */
	private void setResources(Resource essential, int essQty, Resource desirable, int desQty, Resource luxury, int luxQty) {

		essentialRes = essential;
		essentialResQty = essQty;

		desirableRes = desirable;
		desirableResQty = desQty;

		luxuryRes = luxury;
		luxuryResQty = luxQty;
	}

	/**
	 * Makes a shallow copy of an Agent
	 */
	public Agent clone() {
		Agent agentClone = new Agent(getID());
		agentClone.setCoordinates(xCoor, yCoor);
		agentClone.setGoalCoordinates(goalX, goalY);
		agentClone.setResources(getEssentialRes(), getEssentialResQty(), getDesirableRes(), getDesirableResQty(), getLuxuryRes(), getLuxuryResQty());

		return agentClone;

	}

}
