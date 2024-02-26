package com.mastercard.crossborder.api.rest.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "AdditionalField", propOrder = {"data"})
@XmlRootElement(name = "additional_data")
public class AdditionalField implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<AdditionalDataField> data = new ArrayList<>();

    @JsonProperty(value = "data_field")
    @XmlElement(name = "data_field", required = true)
    public List<AdditionalDataField> getData() {
        return data;
    }

    public void setData(List<AdditionalDataField> data) {
        this.data = data;
    }

    public void addData(AdditionalDataField dataField) {

        data.add(dataField);
    }

}
