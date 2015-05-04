
public class AlphaBeta {
	int value;
	Agent ag;

	private int getValue() {
		return value;
	}
	private void setValue(int value) {
		this.value = value;
	}
	private Agent getAg() {
		return ag;
	}
	private void setAg(Agent ag) {
		this.ag = ag;
	}
	public AlphaBeta() {
		// TODO Auto-generated constructor stub
	}
	public void prunn(int node,int depth,Agent agList[]){
		int[]v = this.AB_pruning(node, depth, -1000000, 1000000, 0, agList);
		this.setValue(v[0]);
		this.setAg(agList[v[1]]);
	}
	public int[] AB_pruning(int node, int depth, int alpha, int beta,int step, Agent agentList[]){
		int[] v = {-1000000,6};
		Agent agentL[] = agentList.clone();
		if(agentL[(node+step)%6].lost()) {
			return agentL[(node+step+1)%6].AB_pruning(node,depth,alpha,beta,step+1,agentL);
		}
		else if(depth == 0){
			
			int turns = (int)(step / 6)+1;
			v[0] = agentL[(node+step)%6].getTurnScore(agentL[(node+step)%6].getEssentialResQty(),
					agentL[(node+step)%6].getDesirableResQty(),agentL[(node+step)%6].getLuxuryResQty(),turns,null);
			v[0] = ((agentL[(node+step)%6].getDesirableResQty()-turns < 0)||
					(agentL[(node+step)%6].getEssentialResQty()-(3*turns) < 0)||
					(agentL[(node+step)%6].getLuxuryResQty()<0)?-1000000:v[0]);
			v[1] = node;
			return v;
		}
		else if(step%6 == 0){
			v[0] = -1000000;
			v[1] = 6;
			for(int i = 0;i < 7;i++){
				agentL = agentList;
				if(i==6 || ( (agentL[i].getLuxuryRes().equals(agentL[(node+step)%6].getEssentialRes()) 
						|| agentL[i].getLuxuryRes().equals(agentL[(node+step)%6].getDesirableRes())) 
						&& agentL[i].getEssentialRes().equals(agentL[(node+step)%6].getLuxuryRes()))) {
					if(i != 6 && agentL[(node+step)%6].canTrade() && agentL[i].canTrade()) {
						agentL[i] = agentL[(node+step)%6].trade(agentL[i],agentL[i].getLuxuryRes());
					}
					int vTemp[] = agentL[(node+step)%6].AB_pruning(node,depth-1,alpha,beta,step+1,agentL);
					v[0] = Math.max(v[0],vTemp[0]);
					if(alpha<v[0]){
						alpha = v[0];
						v[1] = i;
					}
					if(beta <= alpha)
						break;
				}
			}
			return v;
		}

		else{
			v[0] = 1000000;
			v[1] = 6;
			for(int i = 0;i < 7;i++){
				agentL = agentList;
				if(i==6 || ((agentL[i].getLuxuryRes().equals(agentL[(node+step)%6].getEssentialRes())  
						|| agentL[i].getLuxuryRes().equals(agentL[(node+step)%6].getDesirableRes())) 
						&& agentL[i].getEssentialRes().equals(agentL[(node+step)%6].getLuxuryRes()))) {
					if(i != 6 && agentL[(node+step)%6].canTrade() && agentL[i].canTrade()) {
						agentL[i] = agentL[(node+step)%6].trade(agentL[i],agentL[i].getLuxuryRes());
					}
					int vTemp[] = agentL[(node+step)%6].AB_pruning(node,depth-1,alpha,beta,step+1,agentL);
					vTemp[0] = vTemp[0] - agentL[node].AB_pruning(node,0,alpha,beta,step,agentL)[0];
					v[0] = Math.min(v[0],vTemp[0]);
					if(beta>v[0]){
						beta = v[0];
						v[1] = i;
					}
					if(beta <= alpha)
						break;
				}
			}
			return v;
		}
	}

}


