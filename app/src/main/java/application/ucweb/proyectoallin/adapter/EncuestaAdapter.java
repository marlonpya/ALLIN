package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Encuesta;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 29/09/2016.
 */
public class EncuestaAdapter extends RealmBasedRecyclerViewAdapter<Encuesta, EncuestaAdapter.ViewHolder> {
    public static final String TAG = EncuestaAdapter.class.getSimpleName();

    public EncuestaAdapter(
            Context context,
            RealmResults<Encuesta> realmResults,
            boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_encuesta, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Encuesta item = realmResults.get(i);
        viewHolder.pregunta.setText(item.getPregunta());
        viewHolder.si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "si");
            }
        });
        viewHolder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "no");
            }
        });
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_tv_pregunta_encuesta) TextView pregunta;
        @BindView(R.id.row_db_si) RadioButton si;
        @BindView(R.id.row_db_no) RadioButton no;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
