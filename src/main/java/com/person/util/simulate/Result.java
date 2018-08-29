package com.person.util.simulate;

/**
 * 生成EXCEL文件结果信息
 * <p>
 * Created by houjian on 16/11/3.
 */
public class Result {
    //返回代码
    private RESULT_CODE code = RESULT_CODE.SUCCESS;
    //返回信息
    private String message = "";
    //生成文件的路径名
    private String fileName;

    public RESULT_CODE getCode() {
        return code;
    }

    public void setCode(RESULT_CODE code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public enum RESULT_CODE {
        SUCCESS, FAIL
    }

}
