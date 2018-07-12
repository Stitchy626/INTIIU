package rokuniroku.code.intiiu;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class INTIAnnAdapter extends ArrayAdapter<INTIAnn>{

    private Activity context;
    private ArrayList<INTIAnn> annList;

    public INTIAnnAdapter(Activity context, ArrayList<INTIAnn> annList){
        super(context, R.layout.listview_inti_ann, annList);
        this.context = context;
        this.annList = annList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listview_inti_ann, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewDateUpload = (TextView) listViewItem.findViewById(R.id.textViewDateUpload);
        TextView textViewTimeUpload = (TextView) listViewItem.findViewById(R.id.textViewTimeUpload);

        INTIAnn ann = annList.get(position);

        textViewTitle.setText(ann.getTitle());
        textViewDateUpload.setText(ann.getDateUpload());
        textViewTimeUpload.setText(ann.getTimeUpload());

        return listViewItem;
    }
}