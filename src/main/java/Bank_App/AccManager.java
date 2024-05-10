package Bank_App;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccManager {
    private Connection con;
    private Scanner sc;
    AccManager(Connection con, Scanner sc)
    {
        this.con = con;
        this.sc = sc;
    }
    public void credit(long acc_no) throws SQLException
    {
        sc.nextLine();
        System.out.println("ENTER AMOUNT : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("ENTER SECURITY PIN : ");
        String pin = sc.nextLine();

        try
        {
            con.setAutoCommit(false);
            if(acc_no !=0)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE pin = ?");
                ps.setString(1,pin);
                ResultSet rs = ps.executeQuery();

                if(rs.next())
                {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE ac_no = ?";
                    PreparedStatement ps1 = con.prepareStatement(credit_query);
                    ps1.setDouble(1,amount);
                    ps1.setLong(2, acc_no);
                    int rowsAffected = ps1.executeUpdate();
                    if(rowsAffected>0)
                    {
                        System.out.println("Rs."+amount +"CREDITED SUCCESSFULLY !");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("TRANSACTION FAILED !");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("INVALID PIN !");
                }
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
    public void debit(long acc_no) throws SQLException
    {
        sc.nextLine();
        System.out.println("ENTER AMOUNT : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("ENTER PIN : ");
        String pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(acc_no!=0)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE pin = ?");
                ps.setString(1,pin);
                ResultSet rs = ps.executeQuery();

                if(rs.next())
                {
                    double curr_bal = rs.getDouble("balance");
                    if(amount<=curr_bal)
                    {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE ac_no = ?";
                        PreparedStatement ps1 = con.prepareStatement(debit_query);
                        ps1.setDouble(1,amount);
                        ps1.setLong(2,acc_no);
                        int rowsAffected = ps1.executeUpdate();
                        if(rowsAffected>0)
                        {
                            System.out.println("Rs."+amount + "DEBITED SUCCESSFULLY !");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("TRANSACTION FAILED !");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    }else {
                        System.out.println("INSUFFICIENT BALANCE !");
                    }
                }else {
                    System.out.println("INVALID PIN !");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
    public void transfer(long senderAccNo) throws SQLException
    {
        sc.nextLine();
        System.out.println("ENTER RECEIVER ACCOUNT NO. :");
        long recAccNo =sc.nextLong();
        System.out.println("ENTER AMOUNT : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("ENTER PIN : ");
        String pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(senderAccNo!=0 && recAccNo!=0)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE ac_no = ? AND pin = ?");
                ps.setLong(1,senderAccNo);
                ps.setString(2,pin);
                ResultSet rs = ps.executeQuery();

                if(rs.next())
                {
                    double curr_bal = rs.getDouble("balance");
                    if(amount<=curr_bal)
                    {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE ac_no = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE ac_no = ?";
                        PreparedStatement creditps = con.prepareStatement(credit_query);
                        PreparedStatement debitps = con.prepareStatement(debit_query);
                        creditps.setDouble(1,amount);
                        creditps.setLong(2,recAccNo);
                        debitps.setDouble(1,amount);
                        debitps.setLong(2,senderAccNo);
                        int rowsAffected1=debitps.executeUpdate();
                        int rowsAffected2=creditps.executeUpdate();
                        if(rowsAffected1>0 && rowsAffected2>0)
                        {
                            System.out.println("TRANSACTION SUCCESSFULL !");
                            System.out.println("Rs."+amount+"TRANSFERRED SUCCESSFULLY !");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("TRANSACTION FAILED !");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("INSUFFICIENT BALANCE !");
                    }
                }else{
                    System.out.println("INVALID PIN !");
                }
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
    public void getBalance(long acc_no)
    {
        sc.nextLine();
        System.out.println("ENTER PIN : ");
        String pin = sc.nextLine();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM Accounts WHERE ac_no = ? AND pin = ?");
            ps.setLong(1,acc_no);
            ps.setString(2,pin);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                double bal = rs.getDouble("balance");
                System.out.println("BALANCE : "+bal);
            }else {
                System.out.println("INVALID PIN !");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
