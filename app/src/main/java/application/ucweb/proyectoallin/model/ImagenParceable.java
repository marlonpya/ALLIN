package application.ucweb.proyectoallin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ucweb02 on 30/09/2016.
 */
public class ImagenParceable implements Parcelable {
    private String name, url;

    public ImagenParceable() { }

    protected ImagenParceable(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<ImagenParceable> CREATOR = new Creator<ImagenParceable>() {
        @Override
        public ImagenParceable createFromParcel(Parcel in) {
            return new ImagenParceable(in);
        }

        @Override
        public ImagenParceable[] newArray(int size) {
            return new ImagenParceable[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }
}
