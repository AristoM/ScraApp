package com.scraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.gms.maps.MapView;

class MapOverlay {
//        extends com.google.android.maps.Overlay
//{
//    public boolean draw(Canvas canvas, MapView mapView,
//                        boolean shadow, long when)
//    {
//        super.draw(canvas, mapView, shadow);
//
//        //---translate the GeoPoint to screen pixels---
//        Point screenPts = new Point();
//        mapView.getProjection().toPixels(p, screenPts);
//
//        //---add the marker---
//        Bitmap bmp = BitmapFactory.decodeResource(
//                getResources(), R.drawable.current_location);
//        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-32, null);
//        return true;
//    }
}
