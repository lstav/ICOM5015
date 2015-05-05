import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import javax.swing.event.ListSelectionEvent;

import sun.security.util.Length;

public class Moderator {
	private int width;
	private int height;
	private Map map;
	private int user;
	private ArrayList<Agent> agents = new ArrayList<>();
	private int turns;
	private Resource[][] resources;
	private Random rand = new Random();
	private ArrayList<ResourceItem> resourceList = new ArrayList<>();
	private ArrayList<Integer> randomList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));


	public Moderator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets initial values
	 * 
	 * @param x
	 * @param y
	 * @param turns
	 */
	public Moderator(int x, int y, int turns) {

		width = x;
		height = y;
		this.turns = 6*turns;
		initializeResourcesArray();
		inititalizeAgents();
		startSimulation();
	}

	/**
	 * Starts the simulation
	 */
	public void startSimulation() {
		setMap(width, height);
		initializeAgentsResources();
		putAgentsInMap();
		populateResourceList();

		/*System.out.println("Initial Map");
		printMap();*/
		System.out.println("Starting simulation\n");
		// Run for selected turns
		int index = 0;
		for (int i = 0; i < turns; i++) {
			System.out.println("Index " + i);
			index = i;

			Agent currentAgent = nextAgent();

			if (!currentAgent.lost()) {
				// Give map to current agent
				currentAgent.getMap(map);
				currentAgent.receiveAgentList(agents);
				currentAgent.receiveResourceList(resourceList);

				currentAgent.run();

				while (currentAgent.isRunning()) {
				
				}

				// If there was a trade in the turn, get new agent list
				if (currentAgent.traded()) {
					agents = currentAgent.returnAgentList();
				}
				currentAgent.turnPassed();
				map = currentAgent.returnMap();
				/*System.out.println("\nTransition Map");
				printMap();*/
				
				/*
				 * Prints resources of the running agent 
				 */
				System.out.println(currentAgent.getEssentialRes().getResourceName() + " " + currentAgent.getEssentialResQty() 
						+ " " + currentAgent.getDesirableRes().getResourceName() + " " + currentAgent.getDesirableResQty() 
						+ " " + currentAgent.getLuxuryRes().getResourceName() + " " + currentAgent.getLuxuryResQty());
				System.out.println("Agent ID " + currentAgent.getID() + " Score " + currentAgent.getCurrentScore());
				System.out.println("");
			}			
			endTurn();
			verifyMap();
			if(loneSurvivor()) {
				index = i;
				break;
			}

		}
		/*
		 * Shows the winner of the simulation
		 */
		if(getWinner().getID() == -1) {
			System.out.println("No winner");
		} else {
			System.out.println("Winner Agent " + getWinner().getID() + " at index " + index);
			System.out.println("With score of " + getWinner().getCurrentScore());
			System.out.println(getWinner().getEssentialRes().getResourceName() + " " 
					+ getWinner().getEssentialResQty() 
					+ " " + getWinner().getDesirableRes().getResourceName() + " " 
					+ getWinner().getDesirableResQty() 
					+ " " + getWinner().getLuxuryRes().getResourceName() + " " 
					+ getWinner().getLuxuryResQty());
			System.out.println("");
			
		}
		
		System.out.println("Final Scores");
		printAgentScores();

		System.out.println("");

		/*System.out.println("\nFinal Map");
		printMap();*/

	}

	/**
	 * Selects winner of simulation
	 * @return winnerAgent
	 */
	public Agent getWinner() {
		Agent winnerAgent = new Agent(-1);
		int score1 = 0;
		int score2 = 0;
		for(int i = 0; i < agents.size(); i++) {
			score2 = agents.get(i).getCurrentScore();
			if(!agents.get(i).lost() && score1 < score2) {
				score1 = score2;
				winnerAgent = agents.get(i);
			}
		}
		return winnerAgent;
	}

	/**
	 * Prints the scores of the agents and if they lost
	 */
	public void printAgentScores() {
		for(int i = 0; i < agents.size(); i++) {
			System.out.print("Agent " + agents.get(i).getID() + " with score " + agents.get(i).getCurrentScore());
			if(agents.get(i).lost()) {
				System.out.print(" Lost");
			}
			System.out.println("");
		}
	}

	/**
	 * Populates the resource list from the map resources
	 */
	public void populateResourceList() {
		resourceList.clear();

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(map.check(x, y) && !map.isOccupied(x, y)) {
					resourceList.add(new ResourceItem(map.getResource(x, y), x, y));
				}
			}
		}
	}

	/**
	 * Returns the resource list from the map
	 * @return resourceList
	 */
	public ArrayList<ResourceItem> getResourcesOnMap() {		
		return resourceList;
	}

	/**
	 * Initializes the agents with a unique ID
	 */
	public void inititalizeAgents() {
		for (int i = 0; i < 6; i++) {
			agents.add(new Agent(i));
		}
	}

	/**
	 * Checks if there is only one agent left
	 * @return
	 */
	public boolean loneSurvivor() {
		int agentsLeft = agents.size();
		for (int i=0; i<agents.size(); i++) {
			if(agents.get(i).lost()) {
				agentsLeft = agentsLeft - 1;
			}
		}

		return agentsLeft <= 1;
	}

	/**
	 * Create resources for agents
	 */
	public void initializeResourcesArray() {
		resources = new Resource[6][3];

		resources[0][0] = new Resource(1);
		resources[0][1] = new Resource(2);
		resources[0][2] = new Resource(3);

		resources[1][0] = new Resource(1);
		resources[1][1] = new Resource(3);
		resources[1][2] = new Resource(2);

		resources[2][0] = new Resource(3);
		resources[2][1] = new Resource(1);
		resources[2][2] = new Resource(2);

		resources[3][0] = new Resource(3);
		resources[3][1] = new Resource(2);
		resources[3][2] = new Resource(1);

		resources[4][0] = new Resource(2);
		resources[4][1] = new Resource(3);
		resources[4][2] = new Resource(1);

		resources[5][0] = new Resource(2);
		resources[5][1] = new Resource(1);
		resources[5][2] = new Resource(3);
	}

	/**
	 * Creates map
	 * @param x width
	 * @param y height
	 */
	public void setMap(int x, int y) {
		user = 0;
		width = x;
		height = y;
		map = new Map(x, y);
		map.randomize();
	}

	/**
	 * Put resources on agents
	 */
	public void initializeAgentsResources() {
		Collections.shuffle(randomList);
		for (int i = 0; i < 6; i++) {
			agents.get(i).setResources(width, height, resources[randomList.get(i)][0], resources[randomList.get(i)][1],
					resources[randomList.get(i)][2]);
		}
	}

	/**
	 * Initializes agents on a random position on map
	 */
	public void putAgentsInMap() {
		for (int i = 0; i < agents.size(); i++) {
			int randomW;
			int randomH;
			/*
			 * Checks for available tile
			 */
			do {
				randomW = rand.nextInt(width);
				randomH = rand.nextInt(height);
			} while (map.isOccupied(randomW, randomH)
					|| map.check(randomW, randomH));
			/*
			 * Puts agent on selected tile
			 */
			agents.get(i).setCoordinates(randomW, randomH);
			map.setOccupied(randomW, randomH, true);
		}
	}

	/**
	 * Gets the next agent to run
	 * @return nest Agent
	 */
	public Agent nextAgent() {
		return agents.get(user);
	}

	/**
	 * Gets the next agent index after turn
	 */
	public void endTurn() {

		user = ((user + 1) % agents.size());

	}

	/**
	 * Gets the ID for the agent at position {x, y}
	 * @param x
	 * @param y
	 * @return agent
	 */
	public int getAgentID(int x, int y) {
		for(int i = 0; i < agents.size(); i++) {
			if(agents.get(i).getCoordinates()[0] == x && agents.get(i).getCoordinates()[1] == y) {
				return agents.get(i).getID();
			}
		}
		return -1;
	}

	/**
	 * Prints the map
	 */
	public void printMap() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map.check(x, y) && !map.isOccupied(x, y)) {
					System.out.print(" "
							+ map.getResource(x, y).getResourceName().charAt(0)
							+ " ");
				} else if (map.isOccupied(x, y)) {
					if(getAgentID(x, y) != -1) {
						System.out.print(" " + getAgentID(x, y) + " ");
					} else {
						System.out.print(" - ");
					}
				} else {
					System.out.print(" - ");
				}
			}
			System.out.println("");
		}

	}

	/**
	 * Prints the resource list and their position
	 */
	public void printResourceList() {
		for (int i = 0; i < resourceList.size(); i++) {
			System.out.println("Resource " + resourceList.get(i).getResource().getResourceName() +
					" {" + resourceList.get(i).getxCoor() + ", " + resourceList.get(i).getyCoor() + "}");
		}
	}

	/**
	 * Prints the agents resources
	 */
	public void printAgentResources() {
		for (int i = 0; i < agents.size(); i++) {
			System.out.println("Agent " + agents.get(i).getID() + " ess " + agents.get(i).getEssentialRes().getResourceName() +
					" des " + agents.get(i).getDesirableRes().getResourceName() + " lux " + 
					agents.get(i).getLuxuryRes().getResourceName());
		}
	}

	/**
	 * Verifies position of agents in map
	 */
	public void verifyMap() {
		clearMapOccupancy();
		for (int i = 0; i < agents.size(); i++) {
			map.setOccupied(agents.get(i).getCoordinates()[0], agents.get(i).getCoordinates()[1], true);
		}

	}

	/**
	 * Clear the map's occupancy from agents
	 */
	public void clearMapOccupancy() {
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map.setOccupied(i, j, false);
			}
		}
	}

}
