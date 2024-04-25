package view;

import model.Order;
import repository.Repository;
import controller.OrderController;
import helper.Helper;

public class ReceiptView extends MainView{
	String orderId; 
	String branch;

	public ReceiptView (String orderId, String branch) {
		this.orderId = orderId;
		this.branch = branch;
	}
	
	@Override
	protected void printActions() {
		System.out.println("(1) Yes");
		System.out.println("(2) Back");
	}

	@Override
	public void viewApp() {
		printBreadCrumbs("Fast Food App View > Customer View > Checkout > Payment Confirmation");
		Order currentOrder = Repository.BRANCH.get(this.branch).getOrders().get(this.orderId);
		System.out.println("Your total bill is: " + currentOrder.getTotalBill());
		System.out.println("Confirm payment? ");
		printActions();
		int opt = -1;
		do {
			opt = Helper.readInt();
			switch(opt) {
			case 1:
				OrderController.confirmOrder();
				break;
			case 2:
				return;
			default:
				System.out.println("Invalid option. Please Try Again");
			}
			
			
		} while(opt<1 || opt>2);
	}


}
