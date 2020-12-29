import java.util.Scanner;

public class Test{
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Config config = new Config();
		String input;
		boolean isRealUser = false;
		User realUser = null;
		
		System.out.println("Welcome to Date Labelling System!\n");
		
		while(true) {
			System.out.print("Username: ");
			input = scanner.nextLine();
			
			for(int i = 1; i <= config.getRealUserSize(); i++) {
				if(input.equals(config.getRealUser(i).getUserName())) {
					realUser = config.getRealUser(i);
					isRealUser = true;
					break;
				}
			}
			
			if(input.equals(""))
				new RandomLabelling(config.getRandomUser((int)(Math.random() * config.getRandomUserSize()) + 1), config.getCurrentDatasetID());
			else if(isRealUser) {
				System.out.print("Password: ");
				input = scanner.nextLine();
				if(input.equals(realUser.getPassword())) {
					new RealLabelling(realUser, config.getCurrentDatasetID());
					isRealUser = false;
				} else
					System.out.println("Wrong Password!\n");
			}
			else
				System.out.println("Wrong Username!\n");
		}
	}
}