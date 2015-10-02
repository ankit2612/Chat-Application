package com.plusconnect.Asynctask;

/**
 * Created by ambesh on 02-10-2015.
 */
public interface IBackgroundTaskInterface {

        public void doOnSucess(String...params);
        public void doOnError(String errorCode);

}
