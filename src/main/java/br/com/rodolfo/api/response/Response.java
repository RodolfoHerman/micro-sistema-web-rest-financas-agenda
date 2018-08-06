package br.com.rodolfo.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Response
 */
public class Response<T> {

    private T data;
    private List<String> errors;
    
    public Response() {}

    /**
     * @return T return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return List<String> return the errors
     */
    public List<String> getErrors() {
        
        if(errors == null) {

            errors = new ArrayList<String>();
        }
        
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}