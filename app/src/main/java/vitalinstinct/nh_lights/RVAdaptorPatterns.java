package vitalinstinct.nh_lights;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Reece on 16/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class RVAdaptorPatterns extends RecyclerView.Adapter<RVAdaptorPatterns.ViewHolder> {

    private ArrayList<Pattern> patterns;
    ProcessHandler handler;
    String room;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_light_status;
        public TextView text_light_information;
        public Button tb_light_status;

        public ViewHolder(View itemView)
        {
            super(itemView);
            iv_light_status = (ImageView) itemView.findViewById(R.id.iv_light_status);
            text_light_information = (TextView) itemView.findViewById(R.id.tv_light_name);
            tb_light_status = (Button) itemView.findViewById(R.id.tb_light_status);
        }
    }

    public RVAdaptorPatterns(ArrayList<Pattern> patterns, ProcessHandler handler, String room)
    {
        this.patterns = patterns;
        this.handler = handler;
        this.room = room;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pattern_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Pattern pattern = patterns.get(i);

        viewHolder.text_light_information.setText(String.valueOf(pattern.getName()));
        //viewHolder.iv_light_status.setBackgroundColor(Color.GRAY);
        viewHolder.iv_light_status.setImageResource(R.drawable.ic_light_pattern);
        viewHolder.tb_light_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.handleChangeCommand("pattern", pattern.getPatternId(), true, room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patterns.size();
    }
}