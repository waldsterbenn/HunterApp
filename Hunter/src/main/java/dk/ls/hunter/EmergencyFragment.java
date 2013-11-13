package dk.ls.hunter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hest on 12-11-13.
 */
public class EmergencyFragment extends Fragment {
    private static final String FRAGMENT_EMERGENCY_ID = "fragment_emergency";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);

        return rootView;
    }

    public static Fragment newInstance() {

    EmergencyFragment fragment = new EmergencyFragment();
    Bundle args = new Bundle();
    args.putInt(FRAGMENT_EMERGENCY_ID, R.string.drawer_emergency);
    fragment.setArguments(args);
    return fragment;
}

    public EmergencyFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(FRAGMENT_EMERGENCY_ID));
    }
}