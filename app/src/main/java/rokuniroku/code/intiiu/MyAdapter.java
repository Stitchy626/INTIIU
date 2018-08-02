package rokuniroku.code.intiiu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>(  ); //array list of item
    ArrayList<String> urls = new ArrayList<>(  ); //array list of url

    public void update(String name, String url)
    {
        items.add(name);
        urls.add(url);
        notifyDataSetChanged(); //refreshes the recycler view autotmatically
    }

    public MyAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// to create view for recycler view item
        View view = LayoutInflater.from(context).inflate( R.layout.item,parent,false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // initialise the elements of indiv, items........
        holder.nameOfFile.setText(items.get(position));

    }

    @Override
    public int getItemCount() {
        //return the number of items
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfFile;

        public ViewHolder(View itemView) {
            // Individual list items......
            super( itemView );
            nameOfFile  = itemView.findViewById( R.id.nameOfFile ); //declaration
            //when an item click whwat will happen
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //when click it will open download link
                    int position = recyclerView.getChildLayoutPosition( v );
                    Intent intent = new Intent( );
                    // Intent intent = new Intent( context, OpenPDF.class );
                    intent.setType( Intent.ACTION_VIEW ); //intent action view
                    intent.setData( Uri.parse( urls.get(position) ) );
                    //intent.putExtra("URI", urls);
                    context.startActivity( intent );


                }
            } );
        }
    }
}
