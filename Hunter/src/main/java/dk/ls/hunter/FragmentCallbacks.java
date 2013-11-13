package dk.ls.hunter;

/**
 * Created by hest on 13-11-13.
 */
/**
 * A callback interface that all activities containing this fragment must
 * implement. This mechanism allows activities to be notified of item
 * selections.
 */
public interface FragmentCallbacks {
    /**
     * Callback for when an item has been selected.
     */
    public void onAnimalItemSelected(int id);
}
