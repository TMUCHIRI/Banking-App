import BankingApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.io.*;
import java.util.*;

public class StartClient {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            Bank bankObj = BankHelper.narrow(ncRef.resolve_str("BankService"));

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Banking System");

            while (true) {
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Check Balance");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter amount to deposit: ");
                        int depositAmount = scanner.nextInt();
                        int newBalanceAfterDeposit = bankObj.deposit(depositAmount);
                        System.out.println("New balance: " + newBalanceAfterDeposit);
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        int withdrawAmount = scanner.nextInt();
                        int newBalanceAfterWithdrawal = bankObj.withdraw(withdrawAmount);
                        System.out.println("New balance: " + newBalanceAfterWithdrawal);
                        break;
                    case 3:
                        int currentBalance = bankObj.getBalance();
                        System.out.println("Current balance: " + currentBalance);
                        break;
                    case 4:
                        bankObj.shutdown();
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
