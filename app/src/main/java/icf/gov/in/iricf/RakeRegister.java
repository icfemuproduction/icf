package icf.gov.in.iricf;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RakeRegister {

    @SerializedName("data")
    private List<RakeName> rakes = new ArrayList<>();

    @SerializedName("status")
    private Integer status;

    public List<RakeName> getRakes() {
        return rakes;
    }

    public void setRakes(List<RakeName> rakes) {
        this.rakes = rakes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
