package dk.ls.hunter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hest on 12-11-13.
 */
public class DrawerArrayAdapter<T> extends ArrayAdapter {
    private final Context themedContext;
    private final int drawer_list_item;
    private final int drawer_list_item_text;
    private final DrawerItem[] drawerItems;

    public DrawerArrayAdapter(Context themedContext, int drawer_list_item, int drawer_list_item_text, DrawerItem[] drawerItems) {
        super(themedContext,drawer_list_item,drawer_list_item_text, drawerItems);

        this.themedContext = themedContext;
        this.drawer_list_item = drawer_list_item;
        this.drawer_list_item_text = drawer_list_item_text;
        this.drawerItems = drawerItems;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (drawerItems!=null && drawerItems.length>0){
            final DrawerItem item = drawerItems[i];
            final LayoutInflater inflater = (LayoutInflater)themedContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(drawer_list_item, null);

        final TextView text = (TextView) view.findViewById(R.id.drawer_list_item_text);
        final ImageView image = (ImageView) view.findViewById(R.id.drawer_list_item_image);

        image.setImageDrawable(themedContext.getResources().getDrawable(item.drawableId));
            text.setText(item.text);

        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
