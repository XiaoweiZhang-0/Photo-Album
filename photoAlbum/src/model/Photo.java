package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



/**
 * @author Xiaowei Zhang
 */
public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "data";
	public static final String storeFile = "photo.dat";

    private String caption;
    private Date date;
    private Calendar cal;
    public boolean is_stock = false;
    private ArrayList<Tag> taglist;
    private File pic;
    private String filepath;
    
    public Photo(File pic, String filepath)
    {
        this.filepath = filepath;
        this.pic = pic;
        this.taglist = new ArrayList<Tag>();
        this.cal = new GregorianCalendar();
        cal.set(Calendar.MILLISECOND, 0);
        this.date = cal.getTime();
        
    }

    
    /** 
     * @return Date
     */
    public Date get_date()
    {
        return date;
    }
    
    
    /** 
     * @param caption
     */
    public void set_caption(String caption)
    {
        this.caption = caption;
    }

    
    /** 
     * @return String
     */
    public String get_caption()
    {
        return this.caption;
    }

    
    /** 
     * @param name
     * @param value
     */
    public void add_tag(String name, String value)
    {
        Tag tag1 = new Tag(name, value);
        if(this.exist(tag1) == -1)
        {
            this.taglist.add(tag1);
        }
        
    }

    
    /** 
     * @param name
     * @param value
     */
    public void remove_tag(String name, String value)
    {
        Tag tag1 = new Tag(name, value);
        int indx = this.exist(tag1);
        if(indx != -1)
        {
            this.taglist.remove(indx);
        }
    }
    
    /** 
     * @param tag1
     * @return int
     */
    private int exist(Tag tag1)
    {
        for(int i=0; i<taglist.size(); i++)
        {
            Tag tag = taglist.get(i);
            if(tag.tag_compare(tag1))
            {
                return i;
            }
            
        }
        return -1;
    }

    
    /** 
     * @param pic
     */
    public void setPic(File pic)
    {
        this.pic = pic;
    }


    
    /** 
     * @return File
     */
    public File getPic()
    {
        return this.pic;
    }


    
    /** 
     * @return ArrayList<Tag>
     */
    public ArrayList<Tag> get_taglist()
    {
        return this.taglist;
    }


	
    /** 
     * @param name
     * @param value
     * @return boolean
     */
    public boolean tagExists(String name, String value) {
		for(int i = 0; i < taglist.size(); i++) {
			Tag cur = taglist.get(i);
			if(cur.name().toLowerCase().equals(name.toLowerCase()) && cur.value().toLowerCase().equals(value.toLowerCase())) {
				return true;
			}
		}
		return false;
		
	}
    
    /** 
     * @param fp
     */
    public void setFilePath(String fp) {
		this.filepath = fp;
	}
	
	
    /** 
     * @return String
     */
    public String getFilePath() {
		return filepath;
	}

    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == null || o instanceof Photo)
        {
            return false;
        }
        else
        {
            Photo photo = (Photo) o;
            if(this.getFilePath().equals(photo.getFilePath()))
            {
                return true;
            }
            else
            {
                return false;
            }           
        }

    }

    

    
    /** 
     * @param photo
     * @throws IOException
     */
    public static void save(Photo photo) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(photo);
		oos.close();
	}
	

	
    /** 
     * @return Photo
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Photo load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Photo userList = (Photo) ois.readObject();
		ois.close();
		return userList;
	}



}
