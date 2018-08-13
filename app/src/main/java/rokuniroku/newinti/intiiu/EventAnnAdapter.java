package rokuniroku.newinti.intiiu;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventAnnAdapter extends ArrayAdapter<EventAnn> {

    private Activity context;
    private ArrayList<EventAnn> annList;

    private FirebaseStorage dbStorage;
    private StorageReference rootStorage;

    public EventAnnAdapter(Activity context, ArrayList<EventAnn> annList){
        super(context, R.layout.listview_eventann, annList);
        this.context = context;
        this.annList = annList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listview_eventann, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewDateTime = (TextView) listViewItem.findViewById(R.id.textViewDateTime);
        ImageView imageViewBanner = (ImageView) listViewItem.findViewById(R.id.imageViewBanner);

        EventAnn ann = annList.get(position);

        String combineDateTime = ann.getDateStart() + "  " + ann.getTimeStart() + " - "
                + ann.getDateEnd() + "  " + ann.getTimeEnd();

        textViewTitle.setText(ann.getTitle());
        textViewDateTime.setText(combineDateTime);

        if(ann.getBanner().compareTo("default") == 0){
        }else{
            rootStorage = dbStorage.getInstance().getReference().child("Announcement").child("EventAnn").child(ann.getBanner());

            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(rootStorage)
                    .into(imageViewBanner);
        }

        if(ann.getBannerBackground().compareTo("light") == 0) {
            textViewTitle.setTextColor(Color.parseColor("#000000"));
            imageViewBanner.setBackgroundResource(R.drawable.default_banner);
        }else {
            textViewTitle.setTextColor(Color.parseColor("#FFFFFF"));
            imageViewBanner.setBackgroundResource(R.drawable.default_banner_dark);
        }

        return listViewItem;
    }
}
