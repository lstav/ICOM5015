
public class Moderator {
	private int width;
	private int height;
	private Map map;
	private int user;
	private Agent[] agents = new Agent[6];

	public Moderator() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMap(int x, int y){
		user = 0;
		width = x;
		height = y;
		map = new Map(x,y);
		map.randomize();
		
		
		
	}
	
	public Agent nextAgent(int i){
		return Agent[user+i];
	}
	
	public void endTurn(){
		
		user = ((user+1)%6);
	}
	
	

}
