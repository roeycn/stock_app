package com.example.stockapp;

public class ViewDimensionChangeHelper {

    private OnDimensionChangedListener listener;

    public void notifyChange(int newWidth, int newHeight) {
        if (listener != null) {
            listener.dimensionsChanged(newWidth, newHeight);
        }
    }

    public void setOnDimensionChangedListener(OnDimensionChangedListener listener) {
        this.listener = listener;
    }

    public interface OnDimensionChangedListener {
        public void dimensionsChanged(int width, int height);
    }

}
