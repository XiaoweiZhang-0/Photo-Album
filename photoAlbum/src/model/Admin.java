package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Xiaowei Zhang
 */
public class Admin implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "data";
	public static final String storeFile = "admin.dat";
    

    private User current;
    private ArrayList<User> users;


    public Admin()
    {
        users = new ArrayList<User>();
        users.add(new User("admin"));
        this.current = null;
    }
    
    /** 
     * @param current
     */
    public void setCurrent(User current)
    {
        this.current = current;
    }
    
    /** 
     * @return User
     */
    public User getCurrent()
    {
        return this.current;
    }

    
    /** 
     * @param username
     * @return boolean
     */
    public boolean addUser(String username)
    {

        users.add(new User(username));
        return true;
    
        
    }

    
    /** 
     * @param indx
     */
    public void deleteUser(int indx)
    {
        users.remove(indx);
    }

    
    /** 
     * @param username
     * @return int
     */
    public int GetUserIndx(String username)
    {
        int indx = 0;
        for(User user : users)
        {
            if(user.get_user_name().equals(username))
            {
                return indx;
            }
            indx++;
        }
        return -1;
    }

    
    /** 
     * @return ArrayList<User>
     */
    public ArrayList<User> GetUsers()
    {
        return users;
    }

    
    /** 
     * @param username
     * @return User
     */
    public User GetUser(String username)
    {
        User user = users.get(GetUserIndx(username));
        return user;
    }


	
    /** 
     * @param admin
     * @throws IOException
     */
    public static void save(Admin admin) throws IOException {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(admin);
			oos.close();
	}
	

	
    /** 
     * @return Admin
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Admin load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Admin admin = (Admin) ois.readObject();
		ois.close();
		return admin;
		
	}

}