package dlapps.dlv.kqandroid.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.contentful.vault.Asset;
import com.contentful.vault.ContentType;
import com.contentful.vault.Field;
import com.contentful.vault.Resource;

import java.io.Serializable;

/**
 * Created by DanielLujanApps on Tuesday14/02/17.
 *
 */

@ContentType("saloon")
public class Saloon extends Resource implements Parcelable {

    @Field public String description;
    @Field public String name;
    @Field public Asset location;
    @Field public Asset image;

    public Saloon(){};

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Asset getLocation() {
        return location;
    }

    public void setLocation(Asset location) {
        this.location = location;
    }

    public String getImageUrl() {
        return image.url();
    }

    public void setImage(Asset image) {
        this.image = image;
    }


    /*************  PARCELABLE  *******************/
    protected Saloon(Parcel in) {
        description = in.readString();
        name = in.readString();
        location = in.readParcelable(Asset.class.getClassLoader());
        image = in.readParcelable(Asset.class.getClassLoader());
    }

    public static final Creator<Saloon> CREATOR = new Creator<Saloon>() {
        @Override
        public Saloon createFromParcel(Parcel in) {
            return new Saloon(in);
        }

        @Override
        public Saloon[] newArray(int size) {
            return new Saloon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeParcelable(location, PARCELABLE_WRITE_RETURN_VALUE);
        parcel.writeParcelable(image, PARCELABLE_WRITE_RETURN_VALUE);
    }
}
