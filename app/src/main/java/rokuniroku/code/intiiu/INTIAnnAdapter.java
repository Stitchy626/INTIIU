package rokuniroku.code.intiiu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class INTIAnnAdapter extends ArrayAdapter<INTIAnn>{

    private Activity context;
    private ArrayList<INTIAnn> annList;

    public INTIAnnAdapter(Activity context, ArrayList<INTIAnn> annList){
        super(context, R.layout.listview_intiann, annList);
        this.context = context;
        this.annList = annList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listview_intiann, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewDateUpload = (TextView) listViewItem.findViewById(R.id.textViewDateUpload);
        TextView textViewTimeUpload = (TextView) listViewItem.findViewById(R.id.textViewTimeUpload);
        ImageView imageViewIcon = (ImageView) listViewItem.findViewById(R.id.imageViewIcon);

        INTIAnn ann = annList.get(position);

        textViewTitle.setText(ann.getTitle());
        textViewDateUpload.setText(ann.getDateUpload());
        textViewTimeUpload.setText(ann.getTimeUpload());

        if(ann.getCategory().equals("ACO"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_aco);
        else if(ann.getCategory().equals("ADC"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_aro);
        else if(ann.getCategory().equals("AFM"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_afmco);
        else if(ann.getCategory().equals("CAE"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_cae);
        else if(ann.getCategory().equals("COLAL"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_colal);
        else if(ann.getCategory().equals("CC"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_cc);
        else if(ann.getCategory().equals("CS"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_cs);
        else if(ann.getCategory().equals("EC"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_ec);
        else if(ann.getCategory().equals("FEQS"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_feqs);
        else if(ann.getCategory().equals("FHLS"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_fhls);
        else if(ann.getCategory().equals("FITS"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_fits);
        else if(ann.getCategory().equals("FOBCAL"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_fobcal);
        else if(ann.getCategory().equals("Finance & Scholarship"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_fns);
        else if(ann.getCategory().equals("INSO"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_it);
        else if(ann.getCategory().equals("ISS"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_iss);
        else if(ann.getCategory().equals("Library"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_lib);
        else if(ann.getCategory().equals("OAR"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_oar);
        else if(ann.getCategory().equals("ORDC"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_ordc);
        else if(ann.getCategory().equals("RO"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_ro);
        else if(ann.getCategory().equals("Safety & Security"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_sns);
        else if(ann.getCategory().equals("SAO"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_sao);
        else if(ann.getCategory().equals("TL"))
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_tnl);
        else
            imageViewIcon.setImageResource(R.mipmap.ic_intiann_faculty_proxy);

        return listViewItem;
    }
}