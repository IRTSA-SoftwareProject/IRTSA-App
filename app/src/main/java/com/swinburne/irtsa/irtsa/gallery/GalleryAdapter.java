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

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Adapter to provide Scan data to the RecyclerView in the GalleryFragment.
 * This adapter makes use of the ViewHolder pattern to facilitate smooth scrolling.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
  private List<Scan> scans;
  private ImageView thumbImage;
  private TextView thumbTitle;
  private ScanInterface scanAccessObject;

  // Used to emit Scan objects to currently subscribed Observers when a gallery item is selected.
  private final PublishSubject<Scan> onClickGalleryItem = PublishSubject.create();

  /**
   * Retrieve all scans from the database to initialise scans member variable.
   * @param context Context of the current state of the application.
   */
  public GalleryAdapter(Context context) {
    scanAccessObject = new ScanAccessObject(context);
    scans = scanAccessObject.getAllScans();
    Collections.sort(scans, new SortByCreatedAt());
  }

  /**
   * Update the scan data and notify the adapter that the data has changed.
   * This triggers the gallery to redraw.
   * @param scans New scan data to set.
   */
  public void setScanData(List<Scan> scans) {
    this.scans = scans;
    notifyDataSetChanged();
  }

  /**
   * Simple ViewHolder object that initialises thumbImage and thumbTitle to references
   * of the ViewHolder's ImageView and TextView.
   */
  public class ViewHolder extends RecyclerView.ViewHolder {
    /**
     * View holder constructor.
     * @param view The view
     */
    public ViewHolder(View view) {
      super(view);
      thumbImage = view.findViewById(R.id.imageThumb);
      thumbTitle = view.findViewById(R.id.imageTitle);
    }
  }

  /**
   * Here we specify the layout to use for each gallery item.
   * @param parent The container for this view.
   * @param viewType The type of view.
   * @return The view to hold the gallery item.
   */
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    View view = layoutInflater.inflate(R.layout.gallery_item, parent, false);
    return new ViewHolder(view);
  }

  /**
   * Sets the thumbnail and text of a Gallery Item.
   * The position of the ViewHolder is used to index the scans List and retrieve Scan information.
   * @param holder The ViewHolder being created.
   * @param position The position of the ViewHolder in relation to the others.
   */
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    // Add image and text into each view.
    thumbImage.setImageBitmap(scans.get(position).getImage());
    thumbTitle.setText(scans.get(position).getName());

    // Listen for a click on the gallery item and broadcast to observers when one is selected.
    holder.itemView.setOnClickListener(event -> onClickGalleryItem.onNext(scans.get(position)));
  }

  /**
   * Method that returns the amount of gallery items displayed in the gallery.
   * @return The size (count) of Scan objects in the Scan List.
   */
  @Override
  public int getItemCount() {
    return scans.size();
  }

  /**
   * Exposes gallery item to its observers.
   * @return An Observable that emits Scan objects.
   */
  public Observable<Scan> getGalleryClick() {
    return onClickGalleryItem;
  }

  /**
   * Comparator to sort {@link Scan} objects by their created at date.
   * Used to sort the order in which {@link Scan} objects are displayed in the gallery.
   */
  private class SortByCreatedAt implements Comparator<Scan> {
    @Override
    public int compare(Scan scan, Scan t1) {
      if (scan.getCreatedAt() == null || t1.getCreatedAt() == null) {
        return 0;
      }
      return scan.getCreatedAt().compareTo(t1.getCreatedAt());
    }
  }
}
