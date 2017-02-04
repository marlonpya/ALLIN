package application.ucweb.proyectoallin.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ucweb03 on 30/01/2017.
 */

public class Banner extends RealmObject {
    private static final String TAG = Banner.class.getSimpleName();
    public static final String ID = "id";
    @PrimaryKey
    private long id;

    private String url;

    public static void insertOrUpdate(ArrayList<Banner> banners) {
        deleteBanner();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < banners.size(); i++) {
            Banner banner = realm.where(Banner.class).equalTo(ID, banners.get(i).getId()).findFirst();
            if (!banners.get(i).getUrl().isEmpty() || !banners.get(i).getUrl().equals("null") || banners.get(i).getUrl() == null) {
                if (banner == null) {
                    Banner insert_banner = realm.createObject(Banner.class, banners.get(i).getId());
                    insert_banner.setUrl(banners.get(i).getUrl());
                    realm.copyToRealm(insert_banner);
                    Log.d(TAG, insert_banner.toString());
                } else {
                    banner.setUrl(banners.get(i).getUrl());
                    Log.d(TAG, banner.toString());
                }
            }
        }
        realm.commitTransaction();
        realm.close();
    }

    public static RealmResults<Banner> getBanners() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Banner.class).findAll();
    }

    public static void deleteBanner() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Banner> banners = getBanners();
        banners.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
