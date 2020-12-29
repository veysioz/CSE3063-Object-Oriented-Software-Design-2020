import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RandomLabelling {
	
	public RandomLabelling(User user, int currentDatasetID) {
		Dataset dataset = user.getDataset(currentDatasetID);
		Instance instance = null;
		ArrayList<Label> listA = new ArrayList<Label>(), listB = new ArrayList<Label>();
		boolean probability = new Probability(user.getCCP()).isTrue();
		int counter = 0;
		
		for(int i = 1; i <= dataset.getInstanceSize(); i++) {
			if(dataset.getInstance(i).getIsLabeled())
				if(++counter == dataset.getInstanceSize()) probability = true;
			if(!dataset.getInstance(i).getIsLabeled() || counter == dataset.getInstanceSize()) {
				if(!probability || counter == 0) {
					instance = dataset.getInstance(i);
					instance.setIsLabeled(true);
					break;
				} else {
					instance = dataset.getInstance((int)(Math.random() * counter) + 1);
					for(int j = 1; j <= instance.getAssignedLabelSize(); j++)
						listA.add(new Label(instance.getAssignedLabel(j).getLabelID(), instance.getAssignedLabel(j).getLabelText()));
					instance.clearAssignedLabel();
					break;
				}
			}
		}
		
		int random1 = (int)(Math.random() * dataset.getMaxNumLabsPerIns()) + 1, random2;
		for(int i = 0; i < random1; i++) {
			random2 = (int)(Math.random() * dataset.getLabelSize()) + 1;
			Label label = new Label(dataset.getLabel(random2).getLabelID(), dataset.getLabel(random2).getLabelText());
			boolean isDuplicate = false;
			for(Label lab : listB)
				if(label.getLabelID() == lab.getLabelID()) isDuplicate = true;
			if(!isDuplicate) {
				instance.addAssignedLabel(label);
				listB.add(label);
			}
		}
		
		System.out.println("\n\tDataset: " + dataset.getDatasetID() + ". " + dataset.getDatasetName());
		System.out.println("\tInstance: " + instance.getInstanceID() + ". " + instance.getInstanceText());
		System.out.print("\tAssignment(s): ");
		for(int i = 1; i <= instance.getAssignedLabelSize(); i++)
			System.out.print(instance.getAssignedLabel(i).getLabelID() + ". " + instance.getAssignedLabel(i).getLabelText() + " ");
		System.out.println("\n\tUsername: " + user.getUserName());
		System.out.println("\tDate & Time: " + new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss").format(new Date()));
		if(probability && counter > 0) System.out.println("\tConsistency: " + new Consistency(listA, listB).getPercentage());
		System.out.println();
	}
}