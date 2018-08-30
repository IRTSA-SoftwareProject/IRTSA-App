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
 * Adapter to provide Scan data to the RecyclerView in the GalleryFragment
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder (View view){
            super(view);
            thumbImage = view.findViewById(R.id.imageThumb);
            thumbTitle = view.findViewById(R.id.imageTitle);
        }
    }

    @Override
    public int getItemCount(){
        return scans.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        // Add image and text into each view.
        thumbImage.setImageBitmap(scans.get(position).image);
        thumbTitle.setText(scans.get(position).name);

    }
}
