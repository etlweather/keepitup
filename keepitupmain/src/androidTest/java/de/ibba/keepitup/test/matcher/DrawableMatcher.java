package de.ibba.keepitup.test.matcher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher extends TypeSafeMatcher<View> {

    private @DrawableRes
    final int expectedId;
    private String resourceName;

    public DrawableMatcher(@DrawableRes int expectedId) {
        super(View.class);
        this.expectedId = expectedId;
    }

    @Override
    public boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)) {
            return false;
        }
        ImageView imageView = (ImageView) target;
        if (expectedId < 0) {
            return imageView.getDrawable() == null;
        }
        Resources resources = target.getContext().getResources();
        Drawable expectedDrawable = resources.getDrawable(expectedId);
        resourceName = resources.getResourceEntryName(expectedId);
        if (expectedDrawable == null) {
            return false;
        }
        Drawable actualDrawable = imageView.getDrawable();
        if (actualDrawable instanceof StateListDrawable) {
            actualDrawable = actualDrawable.getCurrent();
        }
        if (expectedDrawable instanceof VectorDrawable) {
            if (!(actualDrawable instanceof VectorDrawable)) return false;
            return vectorToBitmap((VectorDrawable) expectedDrawable).sameAs(vectorToBitmap((VectorDrawable) actualDrawable));
        }
        if (expectedDrawable instanceof BitmapDrawable) {
            if (!(actualDrawable instanceof BitmapDrawable)) return false;
            return ((BitmapDrawable) expectedDrawable).getBitmap().sameAs(((BitmapDrawable) actualDrawable).getBitmap());
        }
        throw new IllegalArgumentException("Unsupported drawable: " + imageView.getDrawable());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(expectedId);
        if (resourceName != null) {
            description.appendText("[");
            description.appendText(resourceName);
            description.appendText("]");
        }
    }

    private Bitmap vectorToBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}