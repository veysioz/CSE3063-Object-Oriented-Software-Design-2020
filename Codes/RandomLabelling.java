import java.util.ArrayList;
import java.util.Date;

public class RandomLabelling {
	int consistency = 0;
	
	@SuppressWarnings("unchecked")
	public RandomLabelling(User user, Instance ins, Date assignedDate, Dataset dataset, boolean ccp) {
		ArrayList<Label> temp = (ArrayList<Label>)dataset.getArListLab().clone();
		int rand1 = (int)(Math.random() * dataset.getMaxNumLabsPerIns()) + 1, rand2;
		
		ArrayList<Label> temp1 = new ArrayList<Label>();
		for(int i = 0; i < rand1; i++) {
			rand2 = (int)(Math.random() * temp.size());
			temp1.add(temp.get(rand2));
			temp.remove(rand2);
		}
		
		if(ccp)
			consistency = calculateConsistency(ins.getAssignedLabs(), temp1);
		
		ins.setAssignedDate(assignedDate);
		ins.setAssignedLabs(temp1);
		
		if(!ccp)
			user.getAssignedIns().add(ins);
		
		ins.getAssignedLabs().forEach((n) -> System.out.print(n.getLabelID() + " "));
	}
	
	public int getConsistency() {
		return consistency;
	}
	
	public void setConsistency(int consistency) {
		this.consistency = consistency;
	}
	
	@SuppressWarnings("unchecked")
	public int calculateConsistency(ArrayList<Label> before, ArrayList<Label> after) {
		ArrayList<Label> beforeTemp = (ArrayList<Label>)before.clone();
		ArrayList<Label> afterTemp = (ArrayList<Label>)after.clone();
		
		double max = Math.max(beforeTemp.size(), afterTemp.size());
		afterTemp.retainAll(beforeTemp);
		
		return (int)((afterTemp.size() / max) * 100);
	}
}
