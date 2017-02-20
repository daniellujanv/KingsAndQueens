package dlapps.dlv.kqandroid.objects;

import com.contentful.vault.Asset;
import com.contentful.vault.ContentType;
import com.contentful.vault.Field;
import com.contentful.vault.Resource;

/**
 * Created by DanielLujanApps on Tuesday14/02/17.
 */

@ContentType("playdate")
public class Playdate extends Resource {
    @Field public String description;
    @Field public Asset image;
}
