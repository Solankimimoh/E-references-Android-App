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


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}