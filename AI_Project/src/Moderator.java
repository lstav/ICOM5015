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
	private Agent[] agents = new Agent[6];
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
		this.turns = agents.length*turns;
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

		//printResourceList();

		/*System.out.println("Initial Map");
		printMap();*/
		System.out.println("Starting simulation\n");
		// Run for selected turns
		int index = 0;
		for (int i = 0; i < turns; i++) {
			//System.out.println("Index " + i);
			index = i;
			/*if(loneSurvivor()) {
				index = i;
				System.out.println("Winner Agent " + nextAgent(i).getID() + " at index " + i);
				System.out.println("With score of " + nextAgent(i).getCurrentScore());
				break;
			}*/

			Agent currentAgent = nextAgent();

			if (!currentAgent.lost()) {
				// Give map to current agent
				currentAgent.getMap(map);
				currentAgent.receiveAgentList(agents);
				currentAgent.receiveResourceList(resourceList);

				currentAgent.run();

				while (currentAgent.isRunning()) {
					// TODO do something here

				}

				// If there was a trade in the turn, get new agent list
				if (currentAgent.traded()) {
					agents = currentAgent.returnAgentList();
				}
				currentAgent.turnPassed();
				map = currentAgent.returnMap();
				System.out.println("\nTransition Map");
				printMap();
				System.out.println(currentAgent.getEssentialRes().getResourceName() + " " + currentAgent.getEssentialResQty() 
						+ " " + currentAgent.getDesirableRes().getResourceName() + " " + currentAgent.getDesirableResQty() 
						+ " " + currentAgent.getLuxuryRes().getResourceName() + " " + currentAgent.getLuxuryResQty());
				System.out.println("Agent ID " + currentAgent.getID() + " Score " + currentAgent.getCurrentScore());
				System.out.println("");
			}			
			endTurn();
			if(loneSurvivor()) {
				index = i;
				/*System.out.println("Winner Agent " + currentAgent.getID() + " at index " + i);
				System.out.println("With score of " + currentAgent.getCurrentScore());*/
				break;
			}
		}
		System.out.println("Winner Agent " + getWinner().getID() + " at index " + index);
		System.out.println("With score of " + getWinner().getCurrentScore());
		System.out.println(getWinner().getEssentialRes().getResourceName() + " " + getWinner().getEssentialResQty() 
				+ " " + getWinner().getDesirableRes().getResourceName() + " " + getWinner().getDesirableResQty() 
				+ " " + getWinner().getLuxuryRes().getResourceName() + " " + getWinner().getLuxuryResQty());
		System.out.println("");
		System.out.println("Final Scores");
		printAgentScores();
		//printAgentResources();
		/*System.out.println("\nFinal Map");
		printMap();*/

	}

	public Agent getWinner() {
		Agent winnerAgent = new Agent(-1);
		int score1 = 0;
		int score2 = 0;
		for(int i = 0; i < agents.length; i++) {
			score2 = agents[i].getCurrentScore();
			if(!agents[i].lost() && score1 < score2) {
				score1 = score2;
				winnerAgent = agents[i];
			}
		}
		return winnerAgent;
	}

	public void printAgentScores() {
		for(int i = 0; i < agents.length; i++) {
			System.out.print("Agent " + agents[i].getID() + " with score " + agents[i].getCurrentScore());
			if(agents[i].lost()) {
				System.out.print(" Lost");
			}
			System.out.println("");
		}
	}

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

	public ArrayList<ResourceItem> getResourcesOnMap() {		
		return resourceList;
	}

	public void inititalizeAgents() {
		for (int i = 0; i < agents.length; i++) {

			agents[i] = new Agent(i);
		}
	}

	/**
	 * Checks if there is only one agent left
	 * @return
	 */
	public boolean loneSurvivor() {
		int agentsLeft = agents.length;
		for (int i=0; i<agents.length; i++) {
			if(agents[i].lost()) {
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
		for (int i = 0; i < agents.length; i++) {
			agents[i].setResources(resources[randomList.get(i)][0], resources[randomList.get(i)][1],
					resources[randomList.get(i)][2]);
		}
	}

	/**
	 * Initializes agents on a random position on map
	 */
	public void putAgentsInMap() {
		for (int i = 0; i < agents.length; i++) {
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
			agents[i].setCoordinates(randomW, randomH);
			map.setOccupied(randomW, randomH, true);
		}
	}

	public Agent nextAgent() {
		return agents[user];
	}

	public void endTurn() {

		user = ((user + 1) % agents.length);

	}

	public int getAgentID(int x, int y) {
		for(int i = 0; i < agents.length; i++) {
			if(agents[i].getCoordinates()[0] == x && agents[i].getCoordinates()[1] == y) {
				return agents[i].getID();
			}
		}
		return -1;
	}

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

	public void printResourceList() {
		for (int i = 0; i < resourceList.size(); i++) {
			System.out.println("Resource " + resourceList.get(i).getResource().getResourceName() +
					" {" + resourceList.get(i).getxCoor() + ", " + resourceList.get(i).getyCoor() + "}");
		}
	}

	public void printAgentResources() {
		for (int i = 0; i < agents.length; i++) {
			System.out.println("Agent " + agents[i].getID() + " ess " + agents[i].getEssentialRes().getResourceName() +
					" des " + agents[i].getDesirableRes().getResourceName() + " lux " + 
					agents[i].getLuxuryRes().getResourceName());
		}
	}

}
