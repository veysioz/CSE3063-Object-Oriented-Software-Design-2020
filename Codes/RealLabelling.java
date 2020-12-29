import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RealLabelling {
	
	@SuppressWarnings("resource")
	public RealLabelling(User user, int currentDatasetID) {
		Scanner scanner = new Scanner(System.in);
		Dataset dataset = user.getDataset(currentDatasetID);
		Instance instance = null;
		ArrayList<Label> temp = new ArrayList<Label>(), list = new ArrayList<Label>();
		boolean probability = new Probability(user.getCCP()).isTrue();
		int counter = 0;
		String input;
		boolean validLabs = true;
		
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
						temp.add(new Label(instance.getAssignedLabel(j).getLabelID(), instance.getAssignedLabel(j).getLabelText()));
					instance.clearAssignedLabel();
					break;
				}
			}
		}
		
		System.out.println("\n\tDataset: " + dataset.getDatasetID() + ". " + dataset.getDatasetName());
		System.out.println("\tInstance: " + instance.getInstanceID() + ". " + instance.getInstanceText());
		System.out.print("\t(");
		for(int i = 1; i <= dataset.getLabelSize(); i++)
			System.out.print(dataset.getLabel(i).getLabelID() + ". " + dataset.getLabel(i).getLabelText() + " ");
		System.out.print(")\n\tEnter your label ID(s) with spaces (Max Label: " + dataset.getMaxNumLabsPerIns() + "): ");
		input = scanner.nextLine();
		
		String[] labels = input.split("\\s+");
		while(validLabs) {
			validLabs = false;
			if(labels.length > dataset.getMaxNumLabsPerIns() || isDuplicate(labels)) {
				validLabs = true;
			} else {
				validLabs = false;
			}
			for(String s : labels) {
				if (s.matches("[0-9]+")) {
					if(Integer.parseInt(s) > dataset.getLabelSize() || Integer.parseInt(s) < 1) {
						validLabs = true;
						break;
					}
				} else {
					validLabs = true;
					break;
				}
					
			}
			if(validLabs) {
				System.out.print("\t(Wrong Input!) Enter your label ID(s) with spaces (Max Label: " + dataset.getMaxNumLabsPerIns() + "): ");
				input = scanner.nextLine();
				labels = input.split("\\s+");
			}
		}
		
		for(int i = 0; i < labels.length; i++) {
			instance.addAssignedLabel(new Label(dataset.getLabel(Integer.parseInt(labels[i])).getLabelID(), dataset.getLabel(Integer.parseInt(labels[i])).getLabelText()));
			list.add(new Label(dataset.getLabel(Integer.parseInt(labels[i])).getLabelID(), dataset.getLabel(Integer.parseInt(labels[i])).getLabelText()));
		}
		
		System.out.print("\tAssignment(s): ");
		for(int i = 1; i <= instance.getAssignedLabelSize(); i++)
			System.out.print(instance.getAssignedLabel(i).getLabelID() + ". " + instance.getAssignedLabel(i).getLabelText() + " ");
		System.out.println("\n\tUsername: " + user.getUserName());
		System.out.println("\tDate & Time: " + new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss").format(new Date()));
		if(probability && counter > 0) System.out.println("\tConsistency: " + new Consistency(temp, list).getPercentage());
		
		System.out.print("\t(1. Continue, 2. Logout): ");
		input = scanner.nextLine();
		while(true) {
			if(input.equals("1")) {
				new RealLabelling(user, currentDatasetID);
				break;
			} else if(input.equals("2")) {
				System.out.println();
				break;
			} else {
				System.out.print("\t(Wrong Input!)(1. Continue, 2. Logout): ");
				input = scanner.nextLine();
			}
		}
	}
	
	public boolean isDuplicate(String[] sArr) {
		int countDups = 0;
		for(String s : sArr) {
			countDups = 0;
			for(String b : sArr) {
				if(s.equals(b)) {
					countDups++;
					if(countDups > 1)
						return true;
				}
			}
		}
		return false;
	}
}
