package application.ucweb.proyectoallin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.adapter.NavegadorAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.model.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class NavegadorFragment extends Fragment {

    private static String TAG = NavegadorFragment.class.getSimpleName();
    @BindView(R.id.iv_imagen_usuario_navegador) ImageView imagen_usuario;
    @BindView(R.id.txtNombreUsuario) TextView nombre_usuario;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavegadorAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;

    public NavegadorFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.bind(this, layout);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavegadorAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));
        instanciarSesion();
        return layout;
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public List<ItemSimple> getData() {
        List<ItemSimple> data = new ArrayList<>();
        data.add(new ItemSimple(getActivity().getString(R.string.nav_item_inicio), R.drawable.ic_nav_inicio));
        data.add(new ItemSimple(getActivity().getString(R.string.nav_item_perfil), R.drawable.ic_nav_user));
        data.add(new ItemSimple(getString(R.string.nav_item_recomendar), R.drawable.ic_nav_recomendar));
        data.add(new ItemSimple(getActivity().getString(R.string.nav_item_compras), R.drawable.ic_nav_compras));
        data.add(new ItemSimple(getActivity().getString(R.string.nav_item_puntos), R.drawable.ic_nav_puntos_drawer));
        data.add(new ItemSimple(getActivity().getString(R.string.nav_item_cerrar_sesion), R.drawable.ic_nav_cerrar));
        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    private void instanciarSesion() {
        Usuario usuario = Usuario.getUsuario();
        if (usuario != null ) {
            if (usuario.isSesion()) {
                BaseActivity.setGlideCircular(getActivity(), usuario.getFoto(), imagen_usuario);
                nombre_usuario.setText(usuario.getNombre() + " " + usuario.getApellido_p());
            }
        }
    }
}
