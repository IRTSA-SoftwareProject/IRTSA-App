package com.swinburne.irtsa.irtsa.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.ToolbarSetter;
import com.swinburne.irtsa.irtsa.model.Scan;
import io.reactivex.functions.Consumer;

/**
 * Fragment that displays the saved scan gallery.
 */
public class GalleryFragment extends Fragment implements ToolbarSetter {
  private RecyclerView recyclerView;
  private GalleryAdapter adapter;

  public GalleryFragment() {
    // Required empty public constructor.
  }

  public void refreshGallery() {
    adapter.refreshScans();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_gallery, container, false);

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
        System.out.println("Scan " + selectedScan.id + " selected.");
        System.out.println("Scan Name: " + selectedScan.name);
        System.out.println("Scan Description: " + selectedScan.description);

        // Scan objects now implement Parcelable, making them easy
        // to pass to fragments as shown below.

        //  Bundle bundle = new Bundle();
        //  bundle.putParcelable("Scan", selectedScan);
        //  Fragment Fragment = new Fragment();
        //  fragment.setArguments(bundle);
      }
    };

    // Register the consumer as a gallery item subscriber
    adapter.getGalleryClick().subscribe(galleryItemSelectedConsumer);
  }

  /**
   * Change the icons that are viewable on the top menu toolbar.
   *
   * @param menu the menu at the top of the application
   */
  @Override
  public void setToolbar(Menu menu) {
    menu.findItem(R.id.settings).setVisible(false);
    menu.findItem(R.id.save).setVisible(false);
    menu.findItem(R.id.select).setVisible(true);
    menu.findItem(R.id.share).setVisible(true);
    menu.findItem(R.id.delete).setVisible(true);
  }
}