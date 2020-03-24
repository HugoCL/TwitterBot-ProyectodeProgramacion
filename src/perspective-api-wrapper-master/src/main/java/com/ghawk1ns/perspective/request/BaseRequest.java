package com.ghawk1ns.perspective.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.asynchttpclient.Response;
import org.asynchttpclient.extras.guava.ListenableFutureAdapter;

import javax.annotation.Nullable;

abstract class BaseRequest<R> {

    Client client;

    BaseRequest(Client client) {
        this.client = client;
    }

    public ListenableFuture<R> postAsync() {
        String body = null;
        try {
            body = bodyJSON();
        } catch (JsonProcessingException e) {
            // TODO: Handle accordingly
        }

        ListenableFuture<Response> response = ListenableFutureAdapter
            .asGuavaFuture(client.http.preparePost(getPath()).setBody(body).execute());
        return Futures.transform(response, new Function<Response, R>() {
            @Nullable
            @Override
            public R apply(@Nullable Response r) {
                return transform(r);
            }
        }, MoreExecutors.directExecutor());
    }

    /**
     *
     * @return the body of the request as json
     */
    abstract String bodyJSON() throws JsonProcessingException;

    /**
     *
     * @return the full path, including query params, for the given request
     */
    abstract String getPath();

    abstract R transform(Response json);
}
