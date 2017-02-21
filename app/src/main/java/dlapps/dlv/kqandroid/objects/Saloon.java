package dlapps.dlv.kqandroid.objects;

import com.contentful.vault.Asset;
import com.contentful.vault.ContentType;
import com.contentful.vault.Field;
import com.contentful.vault.Resource;

import java.io.Serializable;

/**
 * Created by DanielLujanApps on Tuesday14/02/17.
 */

@ContentType("saloon")
public class Saloon extends Resource implements Serializable {

    @Field public String description;
    @Field public String name;
    @Field public Asset location;
    @Field public Asset image;
}
