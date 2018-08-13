package rokuniroku.code.newintiiu;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventAnnRequestStatusAdapter extends ArrayAdapter<EventAnn> {
    private Activity context;
    private ArrayList<EventAnn> annList;

    public EventAnnRequestStatusAdapter(Activity context, ArrayList<EventAnn> annList){
        super(context, R.layout.listview_eventann_status, annList);
        this.context = context;
        this.annList = annList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listview_eventann_status, null, true);

        TextView textViewDateUpload = (TextView) listViewItem.findViewById(R.id.textViewDateUpload);
        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewStatus = (TextView) listViewItem.findViewById(R.id.textViewStatus);
        RelativeLayout layoutReason = (RelativeLayout) listViewItem.findViewById(R.id.layoutReason);
        TextView textViewReason = (TextView) listViewItem.findViewById(R.id.textViewReason);

        EventAnn ann = annList.get(position);

        textViewDateUpload.setText(ann.getDateUpload() + "\n" + ann.getTimeUpload());
        textViewTitle.setText(ann.getTitle());

        if(ann.getStatus().equals("pending"))
            textViewStatus.setTextColor(Color.parseColor("#FF9100"));
        else if(ann.getStatus().equals("approved"))
            textViewStatus.setTextColor(Color.GREEN);
        else if(ann.getStatus().equals("rejected")) {
            textViewStatus.setTextColor(Color.RED);
            layoutReason.setVisibility(View.VISIBLE);
        }

        textViewReason.setText(ann.getReason());

        textViewStatus.setText(ann.getStatus());

        return listViewItem;
    }
}
