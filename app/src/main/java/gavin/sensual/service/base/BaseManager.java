package gavin.sensual.service.base;

import com.google.gson.Gson;

import javax.inject.Inject;

import gavin.sensual.inject.component.ApplicationComponent;
import gavin.sensual.net.ClientAPI;
import gavin.sensual.net.DBAPI;
import gavin.sensual.net.GankAPI;

/**
 * BaseManager
 *
 * @author gavin.xiong 2017/4/28
 */
public abstract class BaseManager {
    @Inject
    ClientAPI mApi;
    @Inject
    GankAPI mGankApi;
    @Inject
    DBAPI mDBApi;
    @Inject
    Gson mGson;

    public BaseManager() {
        ApplicationComponent.Instance.get().inject(this);
    }

    public ClientAPI getApi() {
        return mApi;
    }

    public GankAPI getGankApi() {
        return mGankApi;
    }

    public DBAPI getDBApi() {
        return mDBApi;
    }

    public Gson getGson() {
        return mGson;
    }
}
