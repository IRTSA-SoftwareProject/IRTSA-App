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
 * Created by Lionel on 8/25/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

  private List<Scan> scans;
  private Context context;
  private ImageView thumbImage;
  private TextView thumbTitle;

  public class ViewHolder extends RecyclerView.ViewHolder {
    /**
     * Thumbnail view holder.
     * @param view View with thumbnail and title
     */
    public ViewHolder(View view) {
      super(view);
      thumbImage = view.findViewById(R.id.imageThumb);
      thumbTitle = view.findViewById(R.id.imageTitle);
    }
  }

  /**
   * Gallery adapter constructor.
   * @param context Scan context
   */
  public GalleryAdapter(Context context) {
    this.context = context;

    ScanInterface scanAccessObject = new ScanAccessObject(context);

    scans = scanAccessObject.getAllScans();

  }

  @Override
  public int getItemCount() {
    return scans.size();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    View view = layoutInflater.inflate(R.layout.gallery_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // Code to add image into each view.
    thumbImage.setImageBitmap(scans.get(position).image);
    thumbTitle.setText(scans.get(position).name);
  }
}
