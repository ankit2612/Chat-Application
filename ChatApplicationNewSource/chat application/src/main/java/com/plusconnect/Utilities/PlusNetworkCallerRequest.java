package com.plusconnect.Utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.plusconnect.Beans.BaseRequestBean;

import java.io.UnsupportedEncodingException;

/**
 * Created by ambesh on 09-09-2015.
 */
public class PlusNetworkCallerRequest<T extends BaseRequestBean> extends Request<T[]> {

    private Class<T[]> clazz;
    private Gson gson;
    private Response.Listener<T[]>listener;

    public PlusNetworkCallerRequest(Class<T[]> clazz,String url
            , Response.Listener<T[]> listener,Response.ErrorListener errorListener,Gson gson) {
        super(Method.GET, url, errorListener);
        this.clazz=clazz;
        this.listener=listener;
        this.gson=gson;
    }



    @Override
    protected Response<T[]> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T[] response) {

        listener.onResponse(response);


    }
}
