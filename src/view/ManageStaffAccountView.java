package view;
import controller.AdminController;
import enums. *;
import helper.Helper;

public class ManageStaffAccountView extends MainView{
	public void printActions() {
		Helper.clearScreen();
        printBreadCrumbs("Food Ordering and Management App View > Manage Staff Account View");// change breadcrumbs
        System.out.println("What would you like to do ?");
        System.out.println("(1) Add new staff");
        System.out.println("(2) Remove staff");
        System.out.println("(3) Update staff");
        System.out.println("(4) Exit");
	}
	
	public void viewApp() {// change case 2 and 3
        int opt = -1;
        do {
            printActions();
            opt = Helper.readInt(1, 4);
            switch (opt) {
                case 1:
                    Helper.clearScreen();
                    printBreadCrumbs("Food Ordering and Management App View > Manage Staff Account View > Add staff account");
                    promptAddStaffAccount();
                    break;
                case 2: // Remove Staff
                    Helper.clearScreen();
                    printBreadCrumbs("Hotel App View > Menu View > Remove menu items");
                    promptRemoveStaffAccount();
                    break;
                case 3:// 
                    Helper.clearScreen();
                    printBreadCrumbs("Hotel App View > Menu View > Update menu items");
                    promptUpdateStaff();
                    break;
                case 4:
                	System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            if (opt != 4) {
                Helper.pressAnyKeyToContinue();
            }
        } while (opt != 4);
	}
	
	
	
	private boolean promptAddStaffAccount() {
		System.out.println("Enter staff loginId:");
		String loginId = Helper.readString();
		System.out.println("Enter staff name:");
        String name = Helper.readString();
        String password = "password";
        System.out.println("Enter the branch which the staff is in:");
        String branch = Helper.readString(); 
        EmployeePosition position = promptRole();
        if(position == null) return false;
        
        EmployeeGender gender = promptGender();
        if(gender == null) return false;
        
        System.out.println("Enter the staff's age");
        int age = Helper.readInt();
        
        AdminController.addStaffAccount(name, password, branch, position, gender, age, loginId);
        return true;
	}
	
	private void printGenderMenu() {
        System.out.println("Please enter the staff's gender (1-2)");
        System.out.println("(1) Male");
        System.out.println("(2) Female");
    }
	
	private EmployeeGender promptGender() {
        printGenderMenu();
        int choice = Helper.readInt(1, 2);
        if (choice != 1 && choice != 2) {
            return null;
        } else {
            switch (choice) {
                case 1:
                    return EmployeeGender.MALE;
                case 2:
                    return EmployeeGender.FEMALE;
                default:
                    break;
            }
        }
        return null;
    };
    
    private void printRoleMenu() {
        System.out.println("Please enter the staff's role (1-3)");
        System.out.println("(1) Admin");
        System.out.println("(2) Manager");
        System.out.println("(3) Staff");
    }
    
    private EmployeePosition promptRole() {
        printRoleMenu();
        int choice = Helper.readInt(1, 3);
        if (choice < 1 || choice > 3) {
            return null;
        } else {
            switch (choice) {
                case 1:
                    return EmployeePosition.ADMIN;
                case 2:
                    return EmployeePosition.MANAGER;
                case 3:
                	return EmployeePosition.STAFF;
                default:
                    break;
            }
        }
        return null;
    };
    
    //remove staff account
    private boolean promptRemoveStaffAccount() {
        Helper.clearScreen();
        printBreadCrumbs("Hotel App View > Guest View > Remove a staff");
        System.out.println("Enter the login id of the staff that you want to remove: ");
        String loginId = Helper.readString();
        if (!AdminController.removeStaffAccount(loginId)) {
            System.out.println("Employee not found!");
            return false;
        }
        return true;
    }
    
    
 // Update Guest
    /**
     * Prompt to update staff <p>
     * @return {@code true} if update successfully. Otherwise, {@code false}
     */
    private boolean promptUpdateStaff() {
        Helper.clearScreen();
        printBreadCrumbs("Hotel App View > Guest View > Update a Guest Detail");
        System.out.println("Enter the staff login Id that you want to update: ");
        String loginId = Helper.readString();
        if (AdminController.searchStaffById(loginId).size() == 0) {
            System.out.println("Staff not found!");
            return false;
        }
        printUpdateStaffMenu();
        int opt = -1;
        opt = Helper.readInt(1, 5);
        switch (opt) {
            case 1:
                System.out.println("Please enter the staff's new name:");
                String name = Helper.readString();
                AdminController.updateStaffAccount(loginId, name, 1);
                return true;
            case 2:
            	EmployeeGender gender = promptGender();
                if (gender == null) {
                    return false;
                }
                AdminController.updateStaffAccount(loginId, 2, gender);
                return true;
            case 3:
                System.out.println("Please enter the staff's new age:");
                int age = Helper.readInt();
                AdminController.updateStaffAccount(loginId, 3, age);
                return true;
            default:
                break;
        }
        return false;
    }
    
    private void printUpdateStaffMenu() {
        System.out.println("Please choose the information that you want to update (1-3)");
        System.out.println("(1) Name");
        System.out.println("(2) Gender");
        System.out.println("(3) Age");
    }
	
}
