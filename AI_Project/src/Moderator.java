
public class Moderator {
	private int width;
	private int height;
	private Map map;
	private int user;
	private Agent[] agents = new Agent[6];
	private int turns;
	private Resource[][] resources;

	public Moderator() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Sets initial values
	 * @param x
	 * @param y
	 * @param turns
	 */
	public Moderator(int x, int y, int turns) {

		initializeResourcesArray();
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
	
	
	public void setMap(int x, int y){
		user = 0;
		width = x;
		height = y;
		map = new Map(x,y);
		map.randomize();
	}
	
	/**
	 * Put resources on agents
	 */
	public void initializeAgentsResources() {
		for(int i = 0; i<6; i++) {
			agents[i].setResources(resources[i][0], resources[i][1], resources[i][2]);
		}
	}
	
	public Agent nextAgent(int i){
		return agents[user+i];
	}
	
	public void endTurn(){
		
		user = ((user+1)%6);
	}
	
	

}
