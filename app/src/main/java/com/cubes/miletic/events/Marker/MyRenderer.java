package com.cubes.miletic.events.Marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyRenderer extends DefaultClusterRenderer<CustomMarker> {

    private Context context;

    public MyRenderer(Context context, GoogleMap map, ClusterManager<CustomMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull CustomMarker item, @NonNull MarkerOptions markerOptions) {
        markerOptions.icon(makeIcon(context, item.getImage()));
    }

    private BitmapDescriptor makeIcon(Context context, int image) {

        Drawable drawable = ContextCompat.getDrawable(context.getApplicationContext(), image);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<CustomMarker> cluster) {
        return false;
    }
}
