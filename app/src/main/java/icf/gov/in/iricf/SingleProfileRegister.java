package icf.gov.in.iricf;

import com.google.gson.annotations.SerializedName;

public class SingleProfileRegister {

    @SerializedName("data")
    private Profile profile;

    @SerializedName("status")
    private int status;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
