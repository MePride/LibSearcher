package libsearcher.mepride.android.librarysearcher.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Book class
 *
 * BookModel类
 * @author Pride
 * @date 2018/12/31
 */

public class Book extends LitePalSupport implements Serializable {

    private int id;
    private String title;
    private String pages;
    private String isbn;
    private String author;
    private String url;
    private String image;
    private String average;
    private String info;
    private String href;

    public Book(){

    }

    public Book(String title,String info,String href){
        this.title = title;
        this.info = info;
        this.href = href;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


    public String getIsbn() {
        return isbn;
    }

    public String getPages() {
        return pages;
    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getAverage() {
        return average;
    }

    public String getHref() {
        return href;
    }

    public String getInfo() {
        return info;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
