package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.ItemSimple;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class NavegadorAdapter extends RecyclerView.Adapter<NavegadorAdapter.MyViewHolder> {
    List<ItemSimple> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavegadorAdapter(Context context, List<ItemSimple> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_navegador, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemSimple current = data.get(position);
        holder.titleMenu.setText(current.getTitulo());
        Glide.with(this.context)
                .load(current.getIcono())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(holder.iconMenu);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleMenu;
        ImageView iconMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleMenu = (TextView) itemView.findViewById(R.id.titleMenu);
            iconMenu = (ImageView) itemView.findViewById(R.id.iconMenu);
        }
    }
}
