package dashsample.swager.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eric-Local on 8/24/2014.
 */
public final class DateTimeUtils {

    public static String getCurrentDateTimeInServerFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        return simpleDateFormat.format(new Date());

    }
}
