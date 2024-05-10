package Bank_App;
import java.sql.*;
import java.util.Scanner;
public class Accounts {
    private Connection con;
    private Scanner sc;

    public Accounts(Connection con, Scanner sc)
    {
        this.con = con;
        this.sc = sc;
    }

    public long open_account(String email)
    {
        if(!account_exist(email))
        {
            String openAccQuery = " INSERT INTO Accounts(ac_no,name,email,balance,pin)VALUES(?,?,?,?,?)";
            sc.nextLine();
            System.out.println("ENTER YOUR NAME : ");
            String name = sc.nextLine();
            System.out.println("ENTER INITIAL AMOUNT : ");
            double balance = sc.nextDouble();
            System.out.println("ENTER SECURITY PIN : ");
            String pin = sc.nextLine();
            try
            {
                long acc_no = generateAccountNumber();
                PreparedStatement ps = con.prepareStatement(openAccQuery);
                ps.setLong(1,acc_no);
                ps.setString(2,name);
                ps.setString(3,email);
                ps.setDouble(4,balance);
                ps.setString(5,pin);
                int affectedRows=ps.executeUpdate();
                if(affectedRows>0)
                   return acc_no;
                else
                    throw new RuntimeException("ACCOUNT CREATION FAILED !");

            }catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("ACCOUNT ALREADY EXISTS !");
    }

    public long getAccountNo(String email)
    {
        String query = "SELECT ac_no FROM Accounts WHERE email = ?";
        try
        {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getLong("ac_no");
        }catch(SQLException e)
        {
           System.out.println(e.getMessage());
        }
        throw new RuntimeException("ACCOUNT NUMBER DOES NOT EXIST !");
    }
    public long generateAccountNumber()
    {
        try
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT ac_no FROM Accounts ORDER BY ac_no DESC LIMIT 1");
            if(rs.next())
            {
                return rs.getLong("ac_no");
            }
            else
                return 10000100;
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return 10000100;
    }
    public boolean account_exist(String email)
    {
        String query = "SELECT ac_no FROM Accounts WHERE email = ?";
        try
        {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
