package test.Dao.Po;

import java.util.List;

public class ResultModel {
    private String success;
    private List<Student> result;
    private String error_info;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Student> getResult() {
        return result;
    }

    public void setResult(List<Student> result) {
        this.result = result;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }
}
