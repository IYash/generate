package com.person.generate.dao;

/**
 * @author huangchangling on 2018/1/18 0018
 */
public class UploadImageResponse {
    private String ErrorCode;
    private String ErrorMsg;
    private String CtripImageURL;
    private String CtripImageID;
    public String getErrorCode() {
        return ErrorCode;
    }
    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }
    public String getErrorMsg() {
        return ErrorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }
    public String getCtripImageURL() {
        return CtripImageURL;
    }
    public void setCtripImageURL(String ctripImageURL) {
        CtripImageURL = ctripImageURL;
    }

    public String getCtripImageID() {
        return CtripImageID;
    }

    public void setCtripImageID(String ctripImageID) {
        CtripImageID = ctripImageID;
    }
}
