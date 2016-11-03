package Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayoub.mapdemo2.R;

import java.util.List;

public class MarkerAdapter extends ArrayAdapter<Marker>
{

    Context context;
    int layoutResourceId;
    List<Marker> data=null;

    public MarkerAdapter(Context context,int layoutResourceId,List<Marker> data)
    {
        super(context,layoutResourceId,data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       View row=convertView;
       MarkerHolder holder=null;

        if(row==null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MarkerHolder();

            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.coords = (TextView)row.findViewById(R.id.coords);
            holder.txtDesc = (TextView)row.findViewById(R.id.desc);

            row.setTag(holder);
        }
        else
        {
            holder = (MarkerHolder)row.getTag();
        }

        Marker mrk = data.get(position);


        holder.txtTitle.setText(mrk.getEvent());
        holder.imgIcon.setImageResource(mrk.getIcon());
        holder.coords.setText(mrk.getCoordsString());
        holder.txtDesc.setText(mrk.getDesc());


        return row;

    }

    static class MarkerHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDesc;
        TextView coords;

    }
}
