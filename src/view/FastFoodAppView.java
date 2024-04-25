package view;
import helper.Helper;

public class FastFoodAppView extends MainView{

	BranchView branchView = new BranchView();
	LoginView loginView = new LoginView();
	CustomerView customerView = new CustomerView();
	
	public FastFoodAppView() {
		super();
		branchView = new BranchView();
		loginView = new LoginView();
	}
	
	@Override
	protected void printActions() {
		printBreadCrumbs("Fast Food App View");
        System.out.println("Who would you like to continue as?");
        System.out.println("(1) Customer");
        System.out.println("(2) Staff");
        System.out.println("(3) Quit Fast Food App");
	}

	@Override
	public void viewApp() {
		int choice;
		do {
			printActions();
			choice = Helper.readInt();
			switch(choice) {
				case 1:
					customerView.viewApp();
					break;
				case 2:
					loginView.viewApp();
					break;
				case 3:
					System.exit(0);
					break;
				default:
					System.out.println("Invalid input! Please try again.");
					break;
			}
		} while (choice != 3);
		
	}
	
}
