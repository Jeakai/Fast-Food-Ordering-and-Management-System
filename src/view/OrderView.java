package view;

import helper.Helper;
import model.*;
import repository.*;
import java.util.*;
import java.util.Map.Entry;
import controller.*;
import enums.DineInOption;

/**
 * OrderView provides the view to take user input on handling their {@link Order} 
 * through {@link OrderController}
 * 
 * @author Jacky, Hong Sheng
 * @version 1.0
 * @since 2024-04-04
 */
public class OrderView extends MainView{
	/**
	 * branch of OrderView
	 */
	private String branch;
	/**
	 * orderId of OrderView
	 */
	private String orderId;
	/**
	 * Constructing required View Classes and initializing variables
	 */
	private PaymentView paymentView;
	/**
	 * Default Constructor of OrderView
	 * @param branch branch name
	 * @param orderId orderId of particular user
	 */
	public OrderView(String branch, String orderId) {
		super();
		this.branch = branch;
		this.orderId = orderId;
	}
	/**
	 * View Actions of OrderView
	 */
	@Override
	protected void printActions() {
		printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order View for Order ID " + orderId);
		System.out.println("(1) Add food item");
		System.out.println("(2) Remove food item");
		System.out.println("(3) Enter remarks");
		System.out.println("(4) Remove remarks");
		System.out.println("(5) Print orders");
		System.out.println("(6) View Shopping Cart");
		System.out.println("(7) Checkout");
		System.out.println("(8) Back");
	}
	/**
	 * View Application of OrderView that calls {@link OrderController}
	 * for further handling of orders
	 */
	@Override
	public void viewApp() {
		String branch = this.branch;
		String orderId = this.orderId;
		int opt = -1;
		do {
			printActions();
			opt = Helper.readInt(1,8);
			switch (opt) {
			case 1: 
				Helper.clearScreen();
				printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Add an item for OrderId" + orderId);
                addOrderItem(orderId, branch);
				Helper.pressAnyKeyToContinue();
				break;
			case 2:
				Helper.clearScreen();
                printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Remove an item for OrderId" + orderId);
                removeOrderItem(orderId, branch);
				Helper.pressAnyKeyToContinue();
				break;
			case 3:
				Helper.clearScreen();
				printBreadCrumbs("Fast Food App View > Customer View > " + branch + " >  Order for Order ID " + orderId + " > Enter remarks for OrderId" + orderId);
				System.out.println("Enter remarks for your order");
				setRemarks(orderId,branch);
				Helper.pressAnyKeyToContinue();
				break;
			case 4:
				Helper.clearScreen();
				printBreadCrumbs("Fast Food App View > Customer View > " + branch + " >  Order for Order ID " + orderId + " > Remove remarks for OrderId" + orderId);
				OrderController.printOrderDetails(orderId, branch);
				if(!removeRemarks(orderId, branch)) {
					System.out.println("Remarks removal unsuccessful");
				} else {
					System.out.println("Successfully removed remarks");
				}
				Helper.pressAnyKeyToContinue();
				break;
			case 5:
				Helper.clearScreen();
				printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Print order View for OrderId" +orderId);
				OrderController.printOrderDetails(orderId, branch);
				Helper.pressAnyKeyToContinue();
				break;
			case 6:
				printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Shopping Cart View for OrderId" +orderId);
				shoppingCart(orderId, branch);
				Helper.pressAnyKeyToContinue();
				break;
			case 7:
				printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Checkout View for OrderId" +orderId);
				boolean checkoutStatus = checkout(orderId, branch);
		 		if (checkoutStatus == true) {
		 			return;
		 		}
				Helper.pressAnyKeyToContinue();
				break;
			case 8:
				break;
			}
		} while (opt != 8);
		
		
	}
	 /**
	  * The function that prompts the addition of {@link MenuItem} to {@link Order} through {@link MenuManager}
	  * @param orderId orderId of the order
	  * @param branch branch name of the branch this customer is currently in
	  */
	 private void addOrderItem(String orderId, String branch) {
	        String itemName;
	        int itemAmount;
	        int opt = -1; 
	        
	        String category = promptSelectCategory(branch);
	        HashMap<String, MenuItem> filteredMenu = MenuController.filterMenuItemsByCategory(Repository.BRANCH.get(branch).getMenuItems(), category);
	        
	        int size = filteredMenu.size(); 
	        do {
		        MenuController.printMenuByFoodCategory(branch, category);
		        System.out.println("Select the food to order:");
		        opt = Helper.readInt();
		        if(opt <= size && opt > 0) {
		        	break;
		        }
		        else {
		        	System.out.println("Invalid option. Please try again.");
		        }
		        
	        }while(opt > size || opt <= 0);
	        
	        System.out.println("Enter number of quantity:");
	        itemAmount = Helper.readInt();
	        itemName = promptFoodOption(filteredMenu, opt);
	        addToOrder(itemName, orderId, itemAmount, branch);
	 }
	 /**
	  * The function that prompts the removal of {@link MenuItem} from {@link Order} 
	  * through {@link OrderController}
	  * @param orderId orderId of the order
	  * @param branch branch name of the branch that the customer is currently in
	  */
	 private void removeOrderItem(String orderId, String branch) {
		 	String itemName;
	        int itemAmount;
	        int opt = -1; 
	        
	        if (Repository.BRANCH.get(branch).getOrders().get(orderId).getCurrentOrders().size() == 0) {
				System.out.println("Order is empty! Please add food item into order");
				return;
			}
	        	        
	        OrderController.printOrderDetails(orderId, branch);
	        
	        int size = Repository.BRANCH.get(branch).getOrders().get(orderId).getCurrentOrders().size();
	        do {
	        	System.out.println("Select which food to remove: ");
		        opt = Helper.readInt();
		        if(opt <= size && opt > 0) {
		        	break;
		        }
		        else {
		        	System.out.println("Invalid option. Please try again.");
		        }
		        
	        }while(opt > size || opt <= 0);
	        
	        System.out.println("Enter number of quantity:");
	        itemAmount = Helper.readInt();
	        
	        itemName = promptRemoveFood(Repository.BRANCH.get(branch).getOrders().get(orderId).getCurrentOrders(), opt);
	        removeFromOrder(itemName, orderId, itemAmount, branch);
	        
	 }
	 /**
	  * The function that handles management of shopping cart
	  * @param orderId orderId of the order
	  * @param branch branch name of the branch that the customer is currently in
	  */
	 private void shoppingCart(String orderId, String branch) {
		 int opt = -1;
		 do {
			 Helper.clearScreen();
			 printShoppingCart(orderId, branch);
			 opt = Helper.readInt(1,6);
			 switch (opt) {
			 	case 1:
			 		printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Add an item for OrderId" + orderId);
					addOrderItem(orderId, branch);
					Helper.pressAnyKeyToContinue();
					break;
			 	case 2:
			 		printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Remove an item for OrderId" + orderId);
	                removeOrderItem(orderId, branch);
					Helper.pressAnyKeyToContinue();
					break;
			 	case 3:
			 		printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Enter remarks for OrderId" + orderId);
					System.out.println("Enter remarks for your order");
					setRemarks(orderId,branch);
					Helper.pressAnyKeyToContinue();
					break;
			 	case 4:
			 		printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Remove remarks for OrderId" + orderId);
					OrderController.printOrderDetails(orderId, branch);
					if(!removeRemarks(orderId, branch)) {
						System.out.println();
						System.out.println("Remarks removal unsuccessful");
					} else {
						System.out.println("Successfully removed remarks");
						System.out.println();
					}
					Helper.pressAnyKeyToContinue();
					break;
			 	case 5:
			 		printBreadCrumbs("Fast Food App View > Customer View > " + branch + " > Order for Order ID " + orderId + " > Checkout View for OrderId" + orderId);
			 		boolean checkoutStatus = checkout(orderId, branch);
			 		if (checkoutStatus == true) {
			 			return;
			 		}
			 		break;
			 	case 6:
			 		return;
			 }
		 } while(opt != 6);
	 }
	 /**
	  * The function that prints out available actions in shopping cart
	  * @param orderId orderId of the order
	  * @param branch branch name of the branch that the customer is currently in
	  */
	 private void printShoppingCart(String orderId, String branch) {
		 System.out.println("Shopping Cart: ");
		 OrderController.printOrderDetails(orderId, branch);
		 System.out.println();
		 System.out.println("What would you like to do?");
		 System.out.println("(1) Add an order");
		 System.out.println("(2) Remove an order");
		 System.out.println("(3) Enter remarks");
		 System.out.println("(4) Remove remarks");
		 System.out.println("(5) Checkout");
		 System.out.println("(6) Back");
	 }
	 /**
	  * The function accesses {@link Branch} to print {@link FoodCategory} available in the branch
	  * and prompts users to select
	  * @param branch branch name of the branch that the customer is currently in
	  * @return the name of the FoodCategory
	  */
	 private String promptSelectCategory(String branch) {
		 int categoryChoice;
		 int size = Repository.BRANCH.get(branch).getFoodCategoryList().size();
	        do {
	        	System.out.println("Please select a food category to view: ");
	        	MenuController.printFoodCategory(branch);
		        categoryChoice = Helper.readInt();
		        if(categoryChoice <= size && categoryChoice > 0) {
		        	break;
		        }
		        else {
		        	System.out.println("Invalid option. Please try again.");
		        }
		        
	        }while(categoryChoice > size || categoryChoice <= 0);
		 return Repository.BRANCH.get(branch).getFoodCategoryList().get(categoryChoice -1);
	 }
	 /**
	  * The function that adds {@link MenuItem} to an {@link Order} through {@link OrderController}
	  * @param name name of the food to be added
	  * @param orderId orderId of the order
	  * @param amount amount of the food to be added
	  * @param branch branch name of the branch that the customer is currently in
	  */
	private void addToOrder(String name, String orderId, int amount, String branch){
        if (OrderController.addOrderItem(name, orderId, amount, branch)){
            System.out.printf("\"%s\" added to order SUCCESSFULLY\n", name);
        }
        else{
            System.out.printf("Addition to order FAILED (\"%s\" NOT FOUND in menu)\n", name);
        };
    }
	/**
	 * The function that removes {@link MenuItem} from {@link Order} through {@link OrderController}
	 * @param name name of the food to be removed
	 * @param orderId orderId of the current order
	 * @param amount amount of the food to be removed
	 * @param branch branch name of the branch that the customer is currently in
	 */
	private void removeFromOrder(String name, String orderId, int amount, String branch) {
		if(OrderController.removeOrderItem(name, orderId, amount, branch)) {
			System.out.printf("\"%s\" removed from order SUCCESSFULLY\n", name);
		}
		else {
			System.out.printf("Removal from order FAILED (\"%s\" NOT FOUND in order\\ removal quantity > current quantity)\n", name);
		}
	}
	/**
	 * The function that iterates through MenuItem HashMap to obtain the MenuItem object
	 * @param menuItems the HashMap of MenuItems for a particular branch
	 * @param opt the option of MenuItems the user chose
	 * @return the MenuItem the user chose to add
	 */
	private String promptFoodOption(HashMap<String, MenuItem> menuItems, int opt) {
		Iterator<Entry<String, MenuItem>> iteratedFood= menuItems.entrySet().iterator();
		int i = 1;
		for(i=1; i<opt; i++) {
			iteratedFood.next();
		}
		Entry<String, MenuItem> SelectedFood = iteratedFood.next();
		MenuItem chosenFood = SelectedFood.getValue();
		String itemName = chosenFood.getName();
		return itemName;
	}
	/**
	 * The function that iterates through the currentOrders HashMap in {@link Order}
	 * to obtain the MenuItem object
	 * @param currentOrders the HashMap of MenuItems for a particular order
	 * @param opt the option of the MenuItems the user chose
	 * @return the MenuItems the user chose to remove
	 */
	private String promptRemoveFood(HashMap<MenuItem, Integer> currentOrders, int opt) {
		Iterator<Entry<MenuItem, Integer>> iteratedRemove = currentOrders.entrySet().iterator();
		int i = 1;
		for(i=1; i<opt; i++) {
			iteratedRemove.next();
		}
		Entry<MenuItem, Integer> toBeRemovedFood = iteratedRemove.next();
		MenuItem chosenFoodToBeRemoved = toBeRemovedFood.getKey();
		String itemName = chosenFoodToBeRemoved.getName();
		return itemName;
	}
	/**
	 * The function that leads customer to {@link PaymentView}
	 * @param orderId orderId of the order
	 * @param branch branch name of the branch that the customer is currently in
	 */
	private boolean checkout(String orderId, String branch) {
		Helper.clearScreen();
		if (Repository.BRANCH.get(branch).getOrders().get(orderId).getCurrentOrders().size() == 0) {
			System.out.println("Order is empty! Please add food item into order");
			return false;
		}
		if(promptDineInOption(orderId, branch)) {;
			System.out.println("Shopping cart: ");
			OrderController.printOrderDetails(orderId, branch);
			paymentView = new PaymentView(orderId, branch);
			paymentView.viewApp();
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * The function that prompts the customers to removeRemarks on their {@link Order}
	 * @param orderId orderId of the order
	 * @param branch branch name of the branch that the customer is currently in
	 * @return {@code true} if removal of remarks is successful and {@code false} otherwise
	 */
	private boolean removeRemarks(String orderId, String branch) {
		int opt = -1;
		int size = Repository.BRANCH.get(branch).getOrders().get(orderId).getRemarks().size();
		
		if(Repository.BRANCH.get(branch).getOrders().get(orderId).getRemarks().get(0) == "No Remarks") {
			System.out.println("No remarks to be removed");
			return false;
		}
		do {
			System.out.println("Select which remarks you would like to remove");
			opt = Helper.readInt();
			if(opt<=0 || opt>size) {
				System.out.println("Invalid option. Please try again");
			}
		}while(opt<=0 || opt>size);
		if(promptRemoveRemarks(orderId, branch, opt)) {
			System.out.println("Successfully removed remarks!");
			System.out.println();
			OrderController.printOrderDetails(orderId, branch);
			return true;
		}
		else {
			System.out.println("Remarks removal unsuccessful");
			System.out.println();
			OrderController.printOrderDetails(orderId, branch);
			return false;
		}
		
	}
	/**
	 * The function that removes the remarks on a {@link Order}
	 * @param orderId orderId of the order
	 * @param branch branch name of the branch that the customer is currently in
	 * @param opt the choice of remark the customer chose to remove
	 * @return  {@code true} if removal successful and {@code false} otherwise
	 */
	private boolean promptRemoveRemarks(String orderId, String branch, int opt) {
		List<String> remarksList = Repository.BRANCH.get(branch).getOrders().get(orderId).getRemarks();
		if(remarksList.get(0).equals("No Remarks")) {
			return false;
		}
		else {
			remarksList.remove(opt -1);
		}
		
		if(remarksList.size() == 0) {
			remarksList.add("No Remarks");
		}
		return true;
	}
	/**
	 * The function to set remarks for a particular {@link Order} through {@link OrderController}
	 * @param remarks the remarks the user intends to add
	 * @param orderId orderId of the order
	 * @param branch branch name of the branch that the customer is currently in
	 */
	private void setRemarks(String orderId, String branch) {
		if (Repository.BRANCH.get(branch).getOrders().get(orderId).getCurrentOrders().size() == 0) {
			System.out.println("Order is empty! Please add food item into order");
			return;
		}
		String remarks = Helper.readString();
		
		if(!OrderController.addRemarks(remarks, orderId, branch)) {
			System.out.println("Remarks entered unsuccessful");
		}
		else {
			System.out.println("Successfully entered remarks");
		}
	}
	/**
	 * The function to prompt customer to select dine in option
	 * @param orderId orderId of the order
	 * @param branch branch name of the branch that the customer is currently in
	 * @return {@code true} if selection of option is valid and {@code false} otherwise.
	 */
	private boolean promptDineInOption(String orderId, String branch) {
		int opt = -1;
		do {
			System.out.println("Would you like to :");
			System.out.println("(1) Dine in");
			System.out.println("(2) Takeaway");
			
			opt = Helper.readInt();
			switch(opt) {
			case 1:
				Repository.BRANCH.get(branch).getOrders().get(orderId).setOption(DineInOption.DINEIN);
				return true;
			case 2:
				Repository.BRANCH.get(branch).getOrders().get(orderId).setOption(DineInOption.TAKEAWAY);
				return true;
			default:
				System.out.println("Invalid option. Please try again.");
				break;
			}
		}while (opt<1 || opt>2);
		return false;
	}
}
