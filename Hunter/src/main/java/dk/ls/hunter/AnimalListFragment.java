package dk.ls.hunter;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dk.ls.hunter.maps.AnimalShortMap;
import dk.ls.hunter.maps.DatabaseHandler;

/**
 * Created by hest on 12-11-13.
 */


/**
 * A list fragment representing a list of Animals. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link }.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FragmentCallbacks}
 * interface.
 */
public class AnimalListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String FRAGMENT_ANIMAL_LIST_ID = "animal_list";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private FragmentCallbacks mFragmentCallbacks = activityFragmentCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public static Fragment newInstance() {

        AnimalListFragment fragment = new AnimalListFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_ANIMAL_LIST_ID, R.string.drawer_animals);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(FRAGMENT_ANIMAL_LIST_ID));
        mFragmentCallbacks = (FragmentCallbacks) activity;
    }


    /**
     * A dummy implementation of the {@link FragmentCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static FragmentCallbacks activityFragmentCallbacks = new FragmentCallbacks() {
        @Override
        public void onAnimalItemSelected(int id) {
        }
    };

    private List<AnimalShortMap> animalsList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnimalListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("DB", "Opening DB connection");
        DatabaseHandler db = new DatabaseHandler(getActivity());

        Log.d("Insert: ", "Inserting ..");
        db.makeDummyData();

        // Reading all contacts
        Log.d("DB", "Getting list of all animals");
        animalsList = db.getAllAnimalsList();
        Log.d("DB", "Closing DB connection");
        db.close();
        setListAdapter(new AnimalArrayAdapter<AnimalListItem>(
                getActivity(),
                R.layout.animal_list_item,
                0,
                animalsList));


//	        for (AnimalShortMap cn : animalsList) {
//	            String log = "Id: "+cn.id+" ,Name: " + cn.name;
//	            Log.d("DB", log);
//	        }

//		Cursor c = db.getAllAnimalsCursor();
//		String[] colNames = new String[] { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_NAME };
//		int[] listLayout = new int[] { android.R.id.text1, android.R.id.text2 };
//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), layout, c, colNames, listLayout);
//
//		setListAdapter(adapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mFragmentCallbacks = activityFragmentCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
//		mFragmentCallbacks.onAnimalItemSelected(AnimalsContent.ITEMS.get(position).id);
        mFragmentCallbacks.onAnimalItemSelected(animalsList.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public class AnimalArrayAdapter<T> extends ArrayAdapter {
        private final Context themedContext;
        private final int layoutId;
        private final List<AnimalShortMap> animalListItems;

        public AnimalArrayAdapter(Context themedContext, int layoutId, int notInUse, List<AnimalShortMap> animalListItems) {
            super(themedContext, layoutId, notInUse, animalListItems);

            this.themedContext = themedContext;
            this.layoutId = layoutId;
            this.animalListItems = animalListItems;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (animalListItems != null && animalListItems.size() > 0) {
                final AnimalShortMap item = animalListItems.get(i);
                final LayoutInflater inflater = (LayoutInflater) themedContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(layoutId, null);

                final TextView text = (TextView) view.findViewById(R.id.animal_list_item_text);
                final ImageView image = (ImageView) view.findViewById(R.id.animal_list_item_image);
//TODO put billede på lit item
                //image.setImageDrawable(themedContext.getResources().getDrawable(item.drawableId));
                text.setText(item.name);

            }
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }
    }

    private class AnimalListItem {
        public String text;
        public int drawableId;
        public int id;

        private AnimalListItem(String text, int drawableId, int id) {
            this.text = text;
            this.drawableId = drawableId;
            this.id = id;
        }
    }
}