package au.com.museumvictoria.fieldguide.bunurong.ui.fragments;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import au.com.museumvictoria.fieldguide.bunurong.db.FieldGuideDatabase;
import au.com.museumvictoria.fieldguide.bunurong.util.ImageResizer;
import au.com.museumvictoria.fieldguide.bunurong.util.Utilities;
import au.com.museumvictoria.fieldguide.bunurong.R;

import com.actionbarsherlock.app.SherlockListFragment;

public class SpeciesGroupListFragment extends SherlockListFragment {
	private static final String TAG = "SpeciesGroupListFragment";

	private ListView mListView;
	private Cursor mCursor;
	private FieldGuideDatabase fgdb;
	private SpeciesCursorAdapter mAdapter; 
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_species_item, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		fgdb = FieldGuideDatabase.getInstance(getActivity().getApplicationContext());

		Log.i(TAG, "Loading grouped items");

		mCursor = fgdb.getSpeciesGroups();
		
		mListView = getListView();
		mListView.setFastScrollEnabled(true);
		mAdapter = new SpeciesCursorAdapter(getActivity().getApplicationContext(), mCursor, 0);
		mListView.setAdapter(mAdapter);

		Log.i(TAG, "Done loading items");
	}
	
	@Override
	public void onDestroy() {
		mCursor.close();
		fgdb.close();
		
		super.onDestroy();
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = l.getItemAtPosition(position);
		Log.i(TAG, "Object: " + o.toString() + " -- " + o.getClass().getCanonicalName());
		Toast.makeText(getActivity().getApplicationContext(), "Item clicked: " + id, Toast.LENGTH_SHORT).show();
	}
	
	
	
	

	class SpeciesCursorAdapter extends CursorAdapter implements SectionIndexer {

		AlphabetIndexer mAlphabetIndexer;

		public SpeciesCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);

			mAlphabetIndexer = new AlphabetIndexer(c,
					c.getColumnIndex(FieldGuideDatabase.SPECIES_GROUP),
					" ABCDEFGHIJKLMNOPQRTSUVWXYZ");
			mAlphabetIndexer.setCursor(c);
		}

		@Override
		public int getPositionForSection(int section) {
			return mAlphabetIndexer.getPositionForSection(section);
		}

		@Override
		public int getSectionForPosition(int position) {
			return mAlphabetIndexer.getSectionForPosition(position);
		}

		@Override
		public Object[] getSections() {
			return mAlphabetIndexer.getSections();
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String groupLabel = cursor.getString(cursor.getColumnIndex(FieldGuideDatabase.SPECIES_GROUP));
			String iconLabel = groupLabel.toLowerCase(Locale.UK).replaceAll(" ", "").replaceAll(",", "");
			String iconPath = Utilities.SPECIES_GROUPS_PATH + iconLabel + "@2x.png"; 
			
			TextView txtView1 = (TextView) view.findViewById(R.id.speciesLabel);
			txtView1.setText(groupLabel);
			
			Log.w(TAG, "Getting AssetsFileDescriptor for species group icon: " + iconPath);
			ImageView imgView = (ImageView) view.findViewById(R.id.speciesIcon);
			imgView.setImageBitmap(ImageResizer.decodeSampledBitmapFromFile(Utilities.getFullExternalDataPath(getActivity(), iconPath), 75, 75));

			
			TextView txtView2 = (TextView) view.findViewById(R.id.speciesSublabel);
			txtView2.setVisibility(View.GONE);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View newView = inflater.inflate(R.layout.species_list, parent, false);
			return newView;
		}

	}
	
}
