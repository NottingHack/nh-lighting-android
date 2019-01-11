package vitalinstinct.nh_lights;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Reece on 16/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class RVAdaptorLights extends RecyclerView.Adapter<RVAdaptorLights.ViewHolder> {

    private ArrayList<Light> lights;
    ProcessHandler handler;
    String room;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_light_status;
        public TextView text_light_information;
        public ToggleButton tb_light_status;

        public ViewHolder(View itemView)
        {
            super(itemView);
            iv_light_status = (ImageView) itemView.findViewById(R.id.iv_light_status);
            text_light_information = (TextView) itemView.findViewById(R.id.tv_light_name);
            tb_light_status = (ToggleButton) itemView.findViewById(R.id.tb_light_status);
        }
    }

    public RVAdaptorLights(ArrayList<Light> lights, ProcessHandler handler, String room)
    {
        this.lights = lights;
        this.handler = handler;
        this.room = room;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.light_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Light light = lights.get(i);

        viewHolder.text_light_information.setText(String.valueOf(light.getId()));
        viewHolder.iv_light_status.setBackgroundColor(Color.GRAY);

        viewHolder.tb_light_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                     handler.handleChangeCommand("button", i, true, room);
                    viewHolder.iv_light_status.setBackgroundColor(Color.YELLOW);
                }
                else
                {
                     handler.handleChangeCommand("button", i, false, room);
                    viewHolder.iv_light_status.setBackgroundColor(Color.GRAY);
                }
            }
        });

        if (light.getSingleState() == "ON")
        {
            viewHolder.tb_light_status.setChecked(true);
        }
        else
        {
            viewHolder.tb_light_status.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return lights.size();
    }
}
