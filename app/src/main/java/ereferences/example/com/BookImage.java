package ereferences.example.com;

/**
 * Created by solan on 17-03-18.
 */

class BookImage {

    private String name;
    private String url;

    public BookImage() {
    }

    public BookImage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
