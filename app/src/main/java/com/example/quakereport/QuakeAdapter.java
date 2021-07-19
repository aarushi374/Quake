package com.example.quakereport;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class QuakeAdapter extends ArrayAdapter<Quake> {
    private static final String LOG_TAG = QuakeAdapter.class.getSimpleName();
    public QuakeAdapter(Context context, ArrayList<Quake> earthquake)
    {
        super(context, 0, earthquake);
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_layout, parent, false);
        }
        Quake currentWord=getItem(position);

        TextView mag=(TextView) listItemView.findViewById(R.id.mag);
        mag.setText(Double.toString(currentWord.getMagnitude()));
        TextView country=(TextView) listItemView.findViewById(R.id.country);
        country.setText(currentWord.getCountry());
        TextView date=(TextView) listItemView.findViewById(R.id.date);
        date.setText(currentWord.getDate());
        TextView time=(TextView) listItemView.findViewById(R.id.time);
        time.setText(currentWord.getTime());
        TextView direction=(TextView) listItemView.findViewById(R.id.direction);
        if(currentWord.getDirection()==null)
        {
            direction.setVisibility(View.GONE);
        }
        else {
            direction.setText(currentWord.getDirection());
            direction.setVisibility(View.VISIBLE);
        }
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentWord.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

}
