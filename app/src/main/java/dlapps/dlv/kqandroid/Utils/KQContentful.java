package dlapps.dlv.kqandroid.Utils;

import android.content.Context;
import android.util.Log;

import com.contentful.java.cda.CDAClient;
import com.contentful.vault.SyncConfig;
import com.contentful.vault.SyncResult;
import com.contentful.vault.Vault;

import java.util.ArrayList;
import java.util.List;

import dlapps.dlv.kqandroid.objects.KQSpace;
import dlapps.dlv.kqandroid.objects.Playdate;
import dlapps.dlv.kqandroid.objects.Promotion;
import dlapps.dlv.kqandroid.objects.Saloon;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dlapps.dlv.kqandroid.BuildConfig.CDA_TOKEN;
import static dlapps.dlv.kqandroid.BuildConfig.SPACE_ID;

/**
 * Created by DanielLujanApps on Monday20/02/17.
 *
 */
public class KQContentful {

    /*
  * Create the client
  *
  * This client will abstract the communication to Contentful. Use it to make your requests to
  * Contentful.
  *
  * For initialization it needs the {@link #CDA_TOKEN} and {@link #SPACE_ID} from above.
  */
    private final CDAClient client = CDAClient
            .builder()
            .setToken(CDA_TOKEN)
            .setSpace(SPACE_ID)
            .build();

    private String TAG = getClass().getSimpleName();
    private Vault mVault;
    private Context context;

    private static KQContentful kqContentful = null;

    public static KQContentful getInstance(Context context) {
        if(kqContentful == null){
            kqContentful = new KQContentful(context);
        }
        return kqContentful;
    }

    private KQContentful(Context context) {
        this.context = context;
    }

    /************* Contentful *******************/
    public void fetchSaloonsVault(final OnSaloonsFetchedListener listener){
        //TODO move all vault & contenful to singleton
        mVault.observe(Saloon.class)
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<Saloon>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                        listener.onException((Exception) e);
                    }

                    @Override
                    public void onNext(List<Saloon> saloons) {
                        Log.i(TAG, "onNext "+saloons.size());
                        listener.onComplete((ArrayList<Saloon>) saloons);
                    }
                });
    }

    public void fetchPlaydateEntriesVault(final OnPlaydatesFetchedListener listener){
        mVault.observe(Playdate.class)
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<Playdate>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                        listener.onException((Exception) e);
                    }

                    @Override
                    public void onNext(List<Playdate> playdates) {
                        Log.i(TAG, "onNext "+playdates.size());
                        listener.onComplete((ArrayList<Playdate>) playdates);
                    }
                });
    }

    public void fetchPromoEntriesVault(final OnPromotionsFetchedListener listener){
        mVault.observe(Promotion.class)
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<Promotion>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                        listener.onException((Exception) e);
                    }

                    @Override
                    public void onNext(List<Promotion> promotions) {
                        Log.i(TAG, "onNext "+promotions.size());
                        listener.onComplete((ArrayList<Promotion>) promotions);
                    }
                });
    }

    public void initVault(final OnVaulListener listener){

// Get instance of Vault
        mVault = Vault.with(context, KQSpace.class);
        // Trigger sync
        mVault.requestSync(SyncConfig.builder().setClient(client).build());

        Vault.observeSyncResults()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SyncResult>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "oncomplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                        listener.onException((Exception) e);
                    }

                    @Override
                    public void onNext(SyncResult syncResult) {
                        Log.i(TAG, "onNext SyncResult "+syncResult.isSuccessful());
                        listener.onComplete(syncResult.isSuccessful());

                    }
                });
    }

    /*********** LISTENERS ***************/
    private interface OnException{
        void onException(Exception e);
    }

    public interface OnVaulListener extends OnException{
        void onComplete(boolean result);
    }

    public interface OnSaloonsFetchedListener extends OnException{
        void onComplete(ArrayList<Saloon> saloons);
    }

    public interface OnPlaydatesFetchedListener extends OnException{
        void onComplete(ArrayList<Playdate> playdates);
    }

    public interface OnPromotionsFetchedListener extends OnException{
        void onComplete(ArrayList<Promotion> promotions);
    }
}
