package ereferences.example.com;

public class RequestBookModel {

    private String requesterName;
    private String requestBookName;

    public RequestBookModel() {
    }

    public RequestBookModel(String requesterName, String requestBookName) {
        this.requesterName = requesterName;
        this.requestBookName = requestBookName;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequestBookName() {
        return requestBookName;
    }

    public void setRequestBookName(String requestBookName) {
        this.requestBookName = requestBookName;
    }
}
