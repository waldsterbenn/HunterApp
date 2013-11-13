package dk.ls.hunter;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Calendar;

import dk.ls.hunter.maps.AnimalMap;
import dk.ls.hunter.maps.DatabaseHandler;

/**
 * Created by hest on 13-11-13.
 */
public class AnimalDetailFragment extends Fragment {
    private static final String FRAGMENT_ANIMAL_DETAIL_ID = "animal_detail";

    int animalId;
    private AnimalMap animalMap;


    public static Fragment newInstance(int animalId) {
        AnimalDetailFragment fragment = new AnimalDetailFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_ANIMAL_DETAIL_ID, animalId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        animalId = getArguments().getInt(FRAGMENT_ANIMAL_DETAIL_ID);
        new GetAnimalDetailTask().execute(animalId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal_detail, container, false);
    }


    private void presentAnimal() {
        ((TextView) getView().findViewById(R.id.animal_name)).setText(animalMap.name);
        ((TextView) getView().findViewById(R.id.animal_latin_name)).setText(animalMap.latinName);

        Calendar now = Calendar.getInstance();
        boolean isLegal = false;
        String period = "";
        try {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.MONTH, animalMap.season.StartMonth);
            start.set(Calendar.DAY_OF_MONTH, animalMap.season.StartDay);

            boolean before = now.before(start);
            Calendar end = Calendar.getInstance();
            end.set(Calendar.MONTH, animalMap.season.EndMonth);
            end.set(Calendar.DAY_OF_MONTH, animalMap.season.EndDay);
            boolean after = now.after(end);

            isLegal = !(before || after);
            period = String.format("%s - %s", DateFormat.format("dd MMMM", start), DateFormat.format("dd MMMM", end));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ((CheckBox) getView().findViewById(R.id.animal_season_legal)).setChecked(isLegal);
        ((TextView) getView().findViewById(R.id.animal_season_period)).setText(period);

        ((TextView) getView().findViewById(R.id.animal_max_shot_distance)).setText(animalMap.shotDistance!=null?String.valueOf(animalMap.shotDistance):"");
        ((TextView) getView().findViewById(R.id.animal_ammo_req)).setText(animalMap.ammo!=null? animalMap.ammo.text:"");
        ((TextView) getView().findViewById(R.id.animal_description)).setText(animalMap.Description);





    }

    private class GetAnimalDetailTask extends AsyncTask<Integer, Void, AnimalMap> {
        protected AnimalMap doInBackground(Integer... ids) {
            Log.d("DB", "Opening DB connection");
            DatabaseHandler db = new DatabaseHandler(getActivity());
            int count = ids.length;
            AnimalMap a = null;
            for (int i = 0; i < count; i++) {
                a = db.getAnimal(ids[i]);
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            Log.d("DB", "Closing DB connection");
            db.close();
            return a;
        }

        protected void onPostExecute(AnimalMap result) {
            animalMap = result;
            presentAnimal();
        }
    }
}