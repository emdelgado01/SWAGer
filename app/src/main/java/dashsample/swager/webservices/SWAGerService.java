package dashsample.swager.webservices;

import java.util.List;

import dashsample.swager.models.books.Book;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Eric-Local on 8/20/2014.
 */
public interface SWAGerService {
    @GET("/books")
    void getBooks(Callback<List<Book>> callback);

    @GET("/books/{id}")
    Book getBook(@Path("id") String id);

    @POST("/books/")
    void createBook(@Body Book book, Callback<Book> callback);

    @PUT("/books/{id}/")
    void editBook(@Path("id") String id, @Body Book book, Callback<Book> callback);

    @PUT("/books/{id}/")
    Book editBook(@Path("id") String id, @Body Book book);

    @DELETE("/books/{id}/")
    void deleteBook(@Path("id") String id, Callback<Book> callback);

    @DELETE("/books/{id}/")
    Book deleteBook(@Path("id") String id);

    @DELETE("/clean/")
    void deleteBooks(Callback<String> callback);
}
