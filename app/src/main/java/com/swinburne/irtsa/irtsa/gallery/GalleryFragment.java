package com.swinburne.irtsa.irtsa.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;
import com.swinburne.irtsa.irtsa.model.ScanAccessObject;

import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment that displays the saved scan gallery.
 */
public class GalleryFragment extends Fragment {
  private RecyclerView recyclerView;
  private GalleryAdapter adapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_gallery, container, false);
    // Required so the gallery toolbar doesn't display when the app is first launched.
    setHasOptionsMenu(false);

    initialiseUi(v);

    return v;
  }

  private void initialiseUi(View v) {
    adapter = new GalleryAdapter(getActivity().getApplicationContext());

    recyclerView = v.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    recyclerView.setAdapter(adapter);

    // Consumes the event emitted from the adapter when a gallery item is selected.
    Consumer<Scan> galleryItemSelectedConsumer = new Consumer<Scan>() {
      @Override
      public void accept(Scan selectedScan) {
        // Scan objects now implement Parcelable, making them easy
        // to pass to fragments as shown below.
        Bundle bundle = new Bundle();
        bundle.putParcelable("Scan", selectedScan);

        GalleryDetailFragment galleryDetailFragment = new GalleryDetailFragment();
        galleryDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = getParentFragment()
                .getChildFragmentManager().beginTransaction();
        // Store the Fragment in the Fragment back-stack
        transaction.addToBackStack("GalleryFragment");
        transaction.replace(R.id.galleryContainer, galleryDetailFragment,
                "GalleryDetailFragment").commit();
      }
    };
    // Register the consumer as a gallery item subscriber
    adapter.getGalleryClick().subscribe(galleryItemSelectedConsumer);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.gallery_toolbar, menu);
  }

  public void refreshGallery() {
    RefreshGalleryItemsTask galleryRefreshTask = new RefreshGalleryItemsTask();
    galleryRefreshTask.execute();
  }

  /**
   * Task that asynchronously fetches all scans and provides this data to the adapter.
   * Performed asynchronously to prevent lag when swiping the viewpager.
   */
  private class RefreshGalleryItemsTask extends AsyncTask {
    private List<Scan> scans = new ArrayList<>();

    /**
     * Get all scans.
     * Return type is irrelevant here.
     *
     * @param objects Data to work with in the background, irrelevant for this implementation.
     * @return data to return, irrelevant for this implementation.
     */
    @Override
    protected Boolean doInBackground(Object[] objects) {
      ScanAccessObject scanAccessObject = new ScanAccessObject(getContext());
      scans = scanAccessObject.getAllScans();
      Collections.sort(scans, (scan, scanToCompare) -> {
        if (scan.createdAt == null || scanToCompare.createdAt == null) {
          return 0;
        }
        return scan.createdAt.compareTo(scanToCompare.createdAt);
      });
      return true;
    }

    /**
     * Executes on the main thread and updates the adapter's list of scan items.
     *
     * @param o irrelevant in our use case.
     */
    @Override
    protected void onPostExecute(Object o) {
      super.onPostExecute(o);
      adapter.setScanData(scans);
    }
  }
}