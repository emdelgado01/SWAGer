package dashsample.swager.models.books;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import dashsample.swager.App;
import retrofit.Callback;

/**
 * Created by Eric-Local on 8/20/2014.
 */
public class Book implements Parcelable {

    private String id;
    private final String author;
    private final String categories;
    private String lastCheckedOut;
    private String lastCheckedOutBy;
    private final String publisher;
    private final String title;
    private final String url;
    //private int position = -1;

    public Book(String id, String author, String categories, String lastCheckedOut, String lastCheckedOutBy, String publisher, String title, String url) {
        this.id = id;
        this.author = author;
        this.categories = categories;
        this.lastCheckedOut = lastCheckedOut;
        this.lastCheckedOutBy = lastCheckedOutBy;
        this.publisher = publisher;
        this.title = title;
        this.url = url;
    }

    public Book(String id, String author, String categories, String lastCheckedOut, String lastCheckedOutBy, String publisher, String title, String url, int position) {
        this(id, author, categories, lastCheckedOut, lastCheckedOutBy, publisher, title, url);
        //this.position = position;
    }

    public static void getBooks(Callback<List<Book>> callback) {
        App.getApiService().getBooks(callback);
    }

    public void createBook(Callback<Book> callback) {
        App.getApiService().createBook(this, callback);
    }

    public void editBook(Callback<Book> callback) {
        App.getApiService().editBook(this.id, this, callback);
    }

    public Book editBook() {
        return App.getApiService().editBook(this.id, this);
    }

    public void deleteBook(Callback<Book> callback) {
        App.getApiService().deleteBook(this.id, callback);
    }

    public Book deleteBook() {
        return App.getApiService().deleteBook(this.id);
    }

    public static void deleteAllBooks(Callback callback) {
        App.getApiService().deleteBooks(callback);
    }

    public String getId() {
        return this.id;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCategories() {
        return this.categories;
    }

    public String getLastCheckedOut() {
        return this.lastCheckedOut;
    }

    public String getLastCheckedOutBy() {
        return this.lastCheckedOutBy;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    /*public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
*/
    public void setLastCheckedOutBy(String lastCheckedOutBy) {
        this.lastCheckedOutBy = lastCheckedOutBy;
    }

    public void setLastCheckedOut(String lastCheckedOut) {
        this.lastCheckedOut = lastCheckedOut;
    }

    public Book(Parcel in) {
        String[] data = new String[8];
        in.readStringArray(data);
        this.author = data[0];
        this.categories = data[1];
        this.lastCheckedOut = data[2];//DateTime.parse(data[2]);
        this.lastCheckedOutBy = data[3];
        this.publisher = data[4];
        this.title = data[5];
        this.url = data[6];
        this.id = data[7];
        //this.position = Integer.valueOf(data[8]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.author,
                this.categories,
                this.lastCheckedOut,
                this.lastCheckedOutBy,
                this.publisher,
                this.title,
                this.url,
                this.id
                //String.valueOf(this.position)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
