package Bank_App;
import java.sql.*;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc)
    {
        this.con = con;
        this.sc = sc;
    }

    public void register()
    {
        sc.nextLine();
        System.out.println("ENTER NAME : ");
        String name = sc.nextLine();
        System.out.println("ENTER EMAIL : ");
        String email = sc.nextLine();
        System.out.println("ENTER PASSWORD : ");
        String pass = sc.nextLine();
        if(user_exist(email))
        {
            System.out.println("USER ALREADY EXISTS !");
            return;
        }
        String register_query = "INSERT INTO User (name,email,pass) VALUES(?,?,?)";
        try
        {
            PreparedStatement ps = con.prepareStatement(register_query);
            ps.setString(1,name);
            ps.setString(2,email);
            ps.setString(3,pass);
            int affectedRows = ps.executeUpdate();
            if(affectedRows>0)
                System.out.println("REGISTRATION SUCCESSFULL !");
            else
                System.out.println("REGISTRATION FAILED !");
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public String login()
    {
        sc.nextLine();
        System.out.println("EMAIL : ");
        String email = sc.nextLine();
        System.out.println("PASSWORD : ");
        String pass = sc.nextLine();
        String login_query = "SELECT * FROM User WHERE email = ? AND pass = ?";
        try
        {
            PreparedStatement ps = con.prepareStatement(login_query);
            ps.setString(1,email);
            ps.setString(2,pass);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return email;
            else
                return null;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exist(String email)
    {
        String query = " SELECT * FROM User WHERE email = ?";
        try
        {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }


}
