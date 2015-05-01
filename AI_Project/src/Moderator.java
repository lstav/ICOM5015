import java.util.Random;

public class Moderator {
	private int width;
	private int height;
	private Map map;
	private int user;
	private Agent[] agents = new Agent[6];
	private int turns;
	private Resource[][] resources;
	private Random rand = new Random();

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
		this.turns = turns;
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

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map.check(x, y)) {
					System.out.print(" "
							+ map.getResource(x, y).getResourceName().charAt(0)
							+ " ");
				} else if (map.isOccupied(x, y)) {
					System.out.print(" A ");
				} else {
					System.out.print(" - ");
				}
			}
			System.out.println("");
		}

		// Run for selected turns
		for (int i = 0; i < turns; i++) {
			System.out.println("Index " + i);
			if(loneSurvivor()) {
				System.out.println("Winner Agent " + nextAgent(i).getID());
				break;
			}
			
			Agent currentAgent = nextAgent(i);

			if (!currentAgent.lost()) {
				// Give map to current agent
				currentAgent.getMap(map);
				currentAgent.receiveAgentList(agents);

				currentAgent.run();

				while (currentAgent.isRunning()) {
					// TODO do something here
				
				}

				// If there was a trade in the turn, get new agent list
				if (currentAgent.traded()) {
					agents = currentAgent.returnAgentList();
				}
				currentAgent.turnPassed();
			}

			endTurn();
		}
		
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
		int agentsLeft = 6;
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

		resources[0][0] = new Resource(0);
		resources[0][1] = new Resource(1);
		resources[0][2] = new Resource(2);

		resources[1][0] = new Resource(0);
		resources[1][1] = new Resource(2);
		resources[1][2] = new Resource(1);

		resources[2][0] = new Resource(2);
		resources[2][1] = new Resource(0);
		resources[2][2] = new Resource(1);

		resources[3][0] = new Resource(2);
		resources[3][1] = new Resource(1);
		resources[3][2] = new Resource(0);

		resources[4][0] = new Resource(1);
		resources[4][1] = new Resource(2);
		resources[4][2] = new Resource(0);

		resources[5][0] = new Resource(1);
		resources[5][1] = new Resource(0);
		resources[5][2] = new Resource(2);
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
		for (int i = 0; i < 6; i++) {
			agents[i].setResources(resources[i][0], resources[i][1],
					resources[i][2]);
		}
	}

	/**
	 * Initializes agents on a random position on map
	 */
	public void putAgentsInMap() {
		for (int i = 0; i < 6; i++) {
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

	public Agent nextAgent(int i) {
		return agents[user];
	}

	public void endTurn() {
		
		user = ((user + 1) % 6);
		
	}

}
