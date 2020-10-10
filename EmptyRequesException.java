package http;

public class EmptyRequesException extends RuntimeException{
    public EmptyRequesException(){
        super();
    }

    public EmptyRequesException(String message,Throwable cause,boolean enableSuppression,boolean writableStacjkTrace){
        super(message,cause,enableSuppression,writableStacjkTrace);
    }

    public EmptyRequesException(String message,Throwable cause){
        super(message,cause);
    }

    public EmptyRequesException(String message){
        super(message);
    }

    public EmptyRequesException(Throwable cause){
        super(cause);
    }
}
