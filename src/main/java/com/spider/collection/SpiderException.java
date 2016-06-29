package com.spider.collection;

/**
 * Created by Administrator on 2015/12/28.
 */
public class SpiderException extends Exception{
    /** serialVersionUID*/
    private static final long serialVersionUID = 1L;

    public SpiderException() {
        super();
    }

    public SpiderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpiderException(String message) {
        super(message);
    }

    public SpiderException(Throwable cause) {
        super(cause);
    }
}
