package duc.googlebook.model;

public class Book {
    private int id;

    private String title, tg, content, img;

    public Book() {
    }

    public Book(int id, String title, String tg, String content) {
        this.id = id;
        this.title = title;
        this.tg = tg;
        this.content = content;
    }

    public Book(int id, String title, String tg, String content, String img) {
        this.id = id;
        this.title = title;
        this.tg = tg;
        this.content = content;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
