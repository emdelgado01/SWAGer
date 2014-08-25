package dashsample.swager;

import android.app.Application;

import dashsample.swager.webservices.SWAGerService;
import retrofit.RestAdapter;

/**
 * Created by Eric-Local on 8/20/2014.
 */
public class App extends Application {
    private RestAdapter mRestAdapter;
    private static SWAGerService mSWAGerService;

    @Override
    public void onCreate() {
        super.onCreate();
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint("http://prolific-interview.herokuapp.com/53ed010d7957e2000785931f/")
                .build();
        mSWAGerService = mRestAdapter.create(SWAGerService.class);

    }

    @Override
    public void onTerminate() {
        mRestAdapter = null;
        super.onTerminate();
    }

    public static SWAGerService getApiService() {
        return mSWAGerService;
    }


}
