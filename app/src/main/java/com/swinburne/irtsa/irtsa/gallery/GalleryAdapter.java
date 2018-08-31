package com.swinburne.irtsa.irtsa.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;
import com.swinburne.irtsa.irtsa.model.ScanAccessObject;
import com.swinburne.irtsa.irtsa.model.ScanInterface;

import java.util.List;

/**
 * Adapter to provide Scan data to the RecyclerView in the GalleryFragment.
 * This adapter makes use of the ViewHolder pattern to facilitate smooth scrolling.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<Scan> scans;
    private ImageView thumbImage;
    private TextView thumbTitle;

    /**
     * Retrieve all scans from the database to initialise scans member variable
     *
     * @param context Context of the current state of the application.
     */
    public GalleryAdapter(Context context){
        ScanInterface scanAccessObject = new ScanAccessObject(context);
        scans = scanAccessObject.getAllScans();
    }

    /**
     * Simple ViewHolder object that initialises thumbImage and thumbTitle to references
     * of the ViewHolder's ImageView and TextView
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder (View view){
            super(view);
            thumbImage = view.findViewById(R.id.imageThumb);
            thumbTitle = view.findViewById(R.id.imageTitle);
        }
    }

    /**
     * Method that returns the amount of gallery items displayed in the gallery.
     *
     * @return The size (count) of Scan objects in the Scan List.
     */
    @Override
    public int getItemCount(){
        return scans.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Sets the thumbnail and text of a Gallery Item.
     * The position of the ViewHolder is used to index the scans List and retrieve Scan information.
     * When this method is called, thumbImage and thumbTitle are already set to reference the
     * Text and Image View's contained in the ViewHolder at the position passed in as a parameter.
     *
     * @param holder The ViewHolder being created.
     * @param position The position of the ViewHolder in relation to the others.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        // Add image and text into each view.
        thumbImage.setImageBitmap(scans.get(position).image);
        thumbTitle.setText(scans.get(position).name);
    }
}
