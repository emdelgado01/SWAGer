package dashsample.swager.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import dashsample.swager.R;
import dashsample.swager.models.books.Book;

/**
 * Created by Eric-Local on 8/22/2014.
 */
public final class UI {
    public static void crossFade(final Boolean showView1, final View view1, final View view2, final int invisibleState, Context context) {
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        view1.animate().setDuration(shortAnimTime).alpha(showView1 ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view1.setVisibility(showView1 ? View.VISIBLE : invisibleState);
            }
        });
        view2.animate().setDuration(shortAnimTime).alpha(showView1 ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view2.setVisibility(showView1 ? invisibleState : View.VISIBLE);
            }
        });
    }

    public static Bundle getDetailActivityOptions(Context context) {
        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(context, R.anim.slide_up_in, R.anim.slide_up_out);
        return activityOptions.toBundle();
    }

    public static Intent getDefaultShareIntent(Context context, Book book) {
        SparseArray<Book> books = new SparseArray<Book>(1);
        books.put(0, book);
        return getDefaultShareIntent(context, books);
    }

    public static Intent getDefaultShareIntent(Context context, SparseArray<Book> books) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject));
        StringBuilder stringBuilder = new StringBuilder(books.size());
        for (int x = 0; x < books.size(); x++) {
            if (x > 0)
                stringBuilder.append(" , ");
            stringBuilder.append(books.valueAt(x).getTitle());
        }
        intent.putExtra(Intent.EXTRA_TEXT, String.format(context.getString(R.string.share), stringBuilder.toString()));
        return intent;
    }


}
