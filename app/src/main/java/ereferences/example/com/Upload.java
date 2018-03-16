package ereferences.example.com;

/**
 * Created by Belal on 8/25/2017.
 */
public class Upload {

    public String category;
    public String bookName;
    public String bookUrl;
    public String thumbUrl;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String category, String bookName, String bookUrl, String thumbUrl) {
        this.category = category;
        this.bookName = bookName;
        this.bookUrl = bookUrl;
        this.thumbUrl = thumbUrl;
    }

}