package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CoachStatusRegister {


    @SerializedName("data")
    private Datum datum;

    @SerializedName("status")
    private int status;

    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    class Datum{

        @SerializedName("shell_rec")
        Date shellReceived;

        @SerializedName("intake")
        Date intake;

        @SerializedName("agency")
        String agency;

        @SerializedName("coupler")
        Date ivCoupleLoad;

        @SerializedName("ew_panel")
        Date ewPanelLoad;

        @SerializedName("roof_tray")
        Date roofPassTrayLoad;

        @SerializedName("ht_tray")
        Date htRoomTrayLoad;

        @SerializedName("ht_equip")
        Date htEquipLoad;

        @SerializedName("high_dip")
        Date highDip;

        @SerializedName("uf_tray")
        Date ufTrayLoad;

        @SerializedName("uf_trans")
        Date ufTransLoad;

        @SerializedName("uf_wire")
        String ufWire;

        @SerializedName("off_roof")
        Date offRoofClear;

        @SerializedName("roof_clear")
        Date roofClear;

        @SerializedName("off_ew")
        Date offEwClear;

        @SerializedName("ew_clear")
        Date ewClear;

        @SerializedName("mech_pan")
        String mechPanel;

        @SerializedName("off_tf")
        Date offTfClear;

        @SerializedName("tf_clear")
        Date tfClear;

        @SerializedName("tf_prov")
        Date tfProv;

        @SerializedName("lf_load")
        Date lfLoad;

        @SerializedName("off_pow")
        Date offPowerCont;

        @SerializedName("power_hv")
        Date powerHv;

        @SerializedName("off_dip")
        Date offHiDipClear;

        @SerializedName("dip_clear")
        Date hiDipClear;

        @SerializedName("lower")
        Date lower;

        @SerializedName("off_cont")
        Date offControlCont;

        @SerializedName("cont_hv")
        Date controlHv;

        @SerializedName("load_test")
        Date loadTest;

        @SerializedName("pcp_clear")
        Date pcpClear;

        @SerializedName("bu_form")
        Date buFormation;

        @SerializedName("rake_form")
        Date rakeFormation;

        @SerializedName("remarks")
        String remarks;

        @SerializedName("coach_num")
        String coachNum;

        @SerializedName("rake_num")
        String rakeNum;

        public Date getShellReceived() {
            return shellReceived;
        }

        public void setShellReceived(Date shellReceived) {
            this.shellReceived = shellReceived;
        }

        public Date getIntake() {
            return intake;
        }

        public void setIntake(Date intake) {
            this.intake = intake;
        }

        public String getAgency() {
            return agency;
        }

        public void setAgency(String agency) {
            this.agency = agency;
        }

        public Date getIvCoupleLoad() {
            return ivCoupleLoad;
        }

        public void setIvCoupleLoad(Date ivCoupleLoad) {
            this.ivCoupleLoad = ivCoupleLoad;
        }

        public Date getEwPanelLoad() {
            return ewPanelLoad;
        }

        public void setEwPanelLoad(Date ewPanelLoad) {
            this.ewPanelLoad = ewPanelLoad;
        }

        public Date getRoofPassTrayLoad() {
            return roofPassTrayLoad;
        }

        public void setRoofPassTrayLoad(Date roofPassTrayLoad) {
            this.roofPassTrayLoad = roofPassTrayLoad;
        }

        public Date getHtRoomTrayLoad() {
            return htRoomTrayLoad;
        }

        public void setHtRoomTrayLoad(Date htRoomTrayLoad) {
            this.htRoomTrayLoad = htRoomTrayLoad;
        }

        public Date getHtEquipLoad() {
            return htEquipLoad;
        }

        public void setHtEquipLoad(Date htEquipLoad) {
            this.htEquipLoad = htEquipLoad;
        }

        public Date getHighDip() {
            return highDip;
        }

        public void setHighDip(Date highDip) {
            this.highDip = highDip;
        }

        public Date getUfTrayLoad() {
            return ufTrayLoad;
        }

        public void setUfTrayLoad(Date ufTrayLoad) {
            this.ufTrayLoad = ufTrayLoad;
        }

        public Date getUfTransLoad() {
            return ufTransLoad;
        }

        public void setUfTransLoad(Date ufTransLoad) {
            this.ufTransLoad = ufTransLoad;
        }

        public String getUfWire() {
            return ufWire;
        }

        public void setUfWire(String ufWire) {
            this.ufWire = ufWire;
        }

        public Date getOffRoofClear() {
            return offRoofClear;
        }

        public void setOffRoofClear(Date offRoofClear) {
            this.offRoofClear = offRoofClear;
        }

        public Date getRoofClear() {
            return roofClear;
        }

        public void setRoofClear(Date roofClear) {
            this.roofClear = roofClear;
        }

        public Date getOffEwClear() {
            return offEwClear;
        }

        public void setOffEwClear(Date offEwClear) {
            this.offEwClear = offEwClear;
        }

        public Date getEwClear() {
            return ewClear;
        }

        public void setEwClear(Date ewClear) {
            this.ewClear = ewClear;
        }

        public String getMechPanel() {
            return mechPanel;
        }

        public void setMechPanel(String mechPanel) {
            this.mechPanel = mechPanel;
        }

        public Date getOffTfClear() {
            return offTfClear;
        }

        public void setOffTfClear(Date offTfClear) {
            this.offTfClear = offTfClear;
        }

        public Date getTfClear() {
            return tfClear;
        }

        public void setTfClear(Date tfClear) {
            this.tfClear = tfClear;
        }

        public Date getTfProv() {
            return tfProv;
        }

        public void setTfProv(Date tfProv) {
            this.tfProv = tfProv;
        }

        public Date getLfLoad() {
            return lfLoad;
        }

        public void setLfLoad(Date lfLoad) {
            this.lfLoad = lfLoad;
        }

        public Date getOffPowerCont() {
            return offPowerCont;
        }

        public void setOffPowerCont(Date offPowerCont) {
            this.offPowerCont = offPowerCont;
        }

        public Date getPowerHv() {
            return powerHv;
        }

        public void setPowerHv(Date powerHv) {
            this.powerHv = powerHv;
        }

        public Date getOffHiDipClear() {
            return offHiDipClear;
        }

        public void setOffHiDipClear(Date offHiDipClear) {
            this.offHiDipClear = offHiDipClear;
        }

        public Date getHiDipClear() {
            return hiDipClear;
        }

        public void setHiDipClear(Date hiDipClear) {
            this.hiDipClear = hiDipClear;
        }

        public Date getLower() {
            return lower;
        }

        public void setLower(Date lower) {
            this.lower = lower;
        }

        public Date getOffControlCont() {
            return offControlCont;
        }

        public void setOffControlCont(Date offControlCont) {
            this.offControlCont = offControlCont;
        }

        public Date getControlHv() {
            return controlHv;
        }

        public void setControlHv(Date controlHv) {
            this.controlHv = controlHv;
        }

        public Date getLoadTest() {
            return loadTest;
        }

        public void setLoadTest(Date loadTest) {
            this.loadTest = loadTest;
        }

        public Date getPcpClear() {
            return pcpClear;
        }

        public void setPcpClear(Date pcpClear) {
            this.pcpClear = pcpClear;
        }

        public Date getBuFormation() {
            return buFormation;
        }

        public void setBuFormation(Date buFormation) {
            this.buFormation = buFormation;
        }

        public Date getRakeFormation() {
            return rakeFormation;
        }

        public void setRakeFormation(Date rakeFormation) {
            this.rakeFormation = rakeFormation;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
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





}
