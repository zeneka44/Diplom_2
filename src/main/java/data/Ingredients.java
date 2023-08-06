package data;

import java.util.List;

public class Ingredients {

    private Boolean success;
    private List<Ingredient> data;

    public Ingredients(Boolean success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }

    public Ingredients() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }
}
