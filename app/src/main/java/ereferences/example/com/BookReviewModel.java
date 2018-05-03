package ereferences.example.com;

public class BookReviewModel {

    private String bookPushKey;
    private String reviewAuthor;
    private String reviewComment;

    public BookReviewModel() {
    }

    public BookReviewModel(String bookPushKey, String reviewAuthor, String reviewComment) {
        this.bookPushKey = bookPushKey;
        this.reviewAuthor = reviewAuthor;
        this.reviewComment = reviewComment;
    }

    public String getBookPushKey() {
        return bookPushKey;
    }

    public void setBookPushKey(String bookPushKey) {
        this.bookPushKey = bookPushKey;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}

