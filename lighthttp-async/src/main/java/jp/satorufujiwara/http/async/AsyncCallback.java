package jp.satorufujiwara.http.async;


import jp.satorufujiwara.http.Response;

public interface AsyncCallback<T> {

    void onResult(Response<T> response, Throwable e);

}
