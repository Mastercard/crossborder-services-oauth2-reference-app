package com.mastercard.crossborder.api.rest.vas.bav.api.response;

public class Bic {
    String type;
    String value;
    ACH ach;
    Wire wire;

    public Bic(String type, String value, ACH ach, Wire wire) {
        this.type = type;
        this.value = value;
        this.wire = new Wire();
        this.ach = new ACH();
    }

    public Bic() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ACH getAch() {
        return ach;
    }
    public void setAch(ACH ach) {
        this.ach = ach;
    }

    public Wire getWire() {
        return wire;
    }
    public void setWire(Wire wire) {
        this.wire = wire;
    }
    @Override
    public String toString() {
        return "Bic{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", ACH='" + ach + '\'' +
                ", Wire='" + wire + '\'' +
                '}';
    }
}
