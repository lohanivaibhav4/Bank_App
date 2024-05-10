package Bank_App;
import java.sql.*;
import java.util.Scanner;

public class Bank_App {
    private static final String url = "jdbc:mysql://localhost:3306/bank_app";
    private static final String username = "root";
    private static final String password = "3007";

    public static void main(String[] args) throws ClassNotFoundException,SQLException
    {
        // LOADING JDBC DRIVERS
        try{
            Class.forName("com.sql.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        // CREATING CONNECTION
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con,sc);
            Accounts ac = new Accounts(con, sc);
            AccManager mgr = new AccManager(con,sc);
            String email;
            long acc_no;

            while(true)
            {
                System.out.println("|| WELCOME TO BANK_APP ||");
                System.out.println();
                System.out.println("1.REGISTER");
                System.out.println("2.LOGIN");
                System.out.println("3.EXIT");
                System.out.println("ENTER YOUR CHOICE : ");
                int choice1 = sc.nextInt();
                switch(choice1)
                {
                    case 1:
                        user.register();
                        System.out.print("\033[H\033[2J"); // CLEAR SCREEN IN JAVA
                        System.out.flush();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null)
                        {
                            System.out.println();
                            System.out.println("USER LOGGED IN !");
                            if(!ac.account_exist(email))
                            {
                                System.out.println();
                                System.out.println("1. OPEN A NEW BANK ACCOUNT");
                                System.out.println("2. EXIT");
                                if(sc.nextInt() == 1)
                                {
                                    acc_no = ac.open_account(email);
                                    System.out.println("ACCOUNT CREATED SUCCESSFULLY !");
                                    System.out.println("YOUR ACCOUNT NUMBER IS :" + acc_no);
                                }else
                                {
                                    break;
                                }
                            }
                            acc_no = ac.getAccountNo(email);
                            int choice2 = 0;
                            while(choice2!=5)
                            {
                                System.out.println();
                                System.out.println("1. DEBIT MONEY");
                                System.out.println("2. CREDIT MONEY");
                                System.out.println("3. TRANSFER MONEY");
                                System.out.println("4. CHECK BALANCE");
                                System.out.println("5. LOGOUT");
                                System.out.println("ENTER YOUR CHOICE : ");
                                choice2 = sc.nextInt();
                                switch (choice2)
                                {
                                    case 1:
                                        mgr.debit(acc_no);
                                        break;
                                    case 2:
                                        mgr.credit(acc_no);
                                        break;
                                    case 3:
                                        mgr.transfer(acc_no);
                                        break;
                                    case 4:
                                        mgr.getBalance(acc_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("ENTER VALID CHOICE ! ");
                                        break;
                                }
                            }
                        }
                        else
                            System.out.println("INCORRECT EMAIL OR PASSWORD !");
                    case 3:
                        System.out.println("THANKS FOR USING OUR BANK_APP !");
                        System.out.println("APP CLOSED ! ");
                        return;
                }
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
