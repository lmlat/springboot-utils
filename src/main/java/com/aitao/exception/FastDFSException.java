package com.aitao.exception;

/**
 * @Author : AiTao
 * @Create : 2021-10-23 16:40
 * @Description :
 */
public class FastDFSException extends RuntimeException {
    public FastDFSException() {
        super();
    }

    public FastDFSException(String msg) {
        super(msg);
    }

    public FastDFSException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FastDFSException(Throwable cause){
        super(cause);
    }
}
