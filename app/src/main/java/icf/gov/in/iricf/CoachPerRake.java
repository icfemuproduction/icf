package icf.gov.in.iricf;

import com.google.gson.annotations.SerializedName;

public class CoachPerRake {

    @SerializedName("coach_num")
    private String coachNum;

    @SerializedName("type")
    private String coachtype;

    public String getCoachNum() {
        return coachNum;
    }

    public void setCoachNum(String coachNum) {
        this.coachNum = coachNum;
    }

    public String getCoachtype() {
        return coachtype;
    }

    public void setCoachtype(String coachtype) {
        this.coachtype = coachtype;
    }
}
