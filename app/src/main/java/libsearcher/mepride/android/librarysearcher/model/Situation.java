package libsearcher.mepride.android.librarysearcher.model;


/**
 * Situation class
 *
 * 书本状态类
 * @author Pride
 * @date 2018/12/31
 */
public class Situation {
    private String bookNumber;      //书号
    private String barcode;         //条码
    private String year;            //年卷期
    private String collection;      //馆藏地址
    private String state;           //书刊状态

    public Situation(String bookNumber,String barcode,String year,String collection,String state){
        this.bookNumber = bookNumber;
        this.barcode = barcode;
        this.year = year;
        this.collection = collection;
        this.state = state;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public String getCollection() {
        return collection;
    }

    public String getState() {
        return state;
    }

    public String getYear() {
        return year;
    }
}

