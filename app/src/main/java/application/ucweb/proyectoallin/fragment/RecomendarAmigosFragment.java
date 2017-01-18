package application.ucweb.proyectoallin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecomendarAmigosFragment extends Fragment {
    @BindView(R.id.tvDescripcionToolbar) TextView tvRecomendarAF;

    public RecomendarAmigosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recomendar_amigos, container, false);
        iniciarLayout(view);
        return view;
    }

    private void iniciarLayout(View view) {
        ButterKnife.bind(this, view);
        tvRecomendarAF.setText(R.string.recomendar_amigos);
    }

}
