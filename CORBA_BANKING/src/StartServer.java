import BankingApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;

class BankImpl extends BankPOA {
    private ORB orb;
    private int balance = 0;
    private static final int OVERDRAFT_LIMIT = 10000;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    // Implement deposit method
    public int deposit(int amount) {
        if (amount > 0) {
            balance += amount;
        }
        return balance;
    }

    // Implement withdraw method
    public int withdraw(int amount) {
        if (amount <= balance + OVERDRAFT_LIMIT) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds including overdraft limit.");
        }
        return balance;
    }

    // Implement getBalance method
    public int getBalance() {
        return balance;
    }

    // Implement shutdown method
    public void shutdown() {
        orb.shutdown(false);
    }
}


public class StartServer {

    public static void main(String args[]) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            BankImpl bankObj = new BankImpl();
            bankObj.setORB(orb);

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(bankObj);
            Bank href = BankHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name("BankService");
            ncRef.rebind(path, href);

            System.out.println("Banking Server ready and waiting ...");
            orb.run();
        }

            catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
        System.out.println("Banking Server Exiting ...");
    }
}
