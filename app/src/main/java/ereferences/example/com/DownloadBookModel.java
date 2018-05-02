package ereferences.example.com;

public class DownloadBookModel {

    private String bookName;
    private String bookThumb;

    public DownloadBookModel(String bookName, String bookThumb) {
        this.bookName = bookName;
        this.bookThumb = bookThumb;
    }

    public DownloadBookModel() {
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookThumb() {
        return bookThumb;
    }

    public void setBookThumb(String bookThumb) {
        this.bookThumb = bookThumb;
    }
}
