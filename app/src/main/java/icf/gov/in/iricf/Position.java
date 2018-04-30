package icf.gov.in.iricf;

import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("linename")
    String lineName;

    @SerializedName("lineno")
    Integer lineNo;

    @SerializedName("stage")
    Integer stage;

    @SerializedName("coach_num")
    private String coachNum;

    @SerializedName("rake_num")
    private String rakeNum;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public String getCoachNum() {
        return coachNum;
    }

    public void setCoachNum(String coachNum) {
        this.coachNum = coachNum;
    }

    public String getRakeNum() {
        return rakeNum;
    }

    public void setRakeNum(String rakeNum) {
        this.rakeNum = rakeNum;
    }
}
