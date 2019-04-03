package com.project.spender.fns.api;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Receipt {

    @SerializedName("operationType")
    @Expose
    public Long operationType;

    @SerializedName("fiscalSign")
    @Expose
    public Long fiscalSign;

    @SerializedName("dateTime")
    @Expose
    public String dateTime;

    @SerializedName("rawData")
    @Expose
    public String rawData;

    @SerializedName("totalSum")
    @Expose
    public Long totalSum;

    @SerializedName("nds10")
    @Expose
    public Long nds10;

    @SerializedName("userInn")
    @Expose
    public String userInn;

    @SerializedName("taxationType")
    @Expose
    public Long taxationType;

    @SerializedName("operator")
    @Expose
    public String operator;

    @SerializedName("fiscalDocumentNumber")
    @Expose
    public Long fiscalDocumentNumber;

    @SerializedName("properties")
    @Expose
    public List<Property> properties = null;

    @SerializedName("receiptCode")
    @Expose
    public Long receiptCode;

    @SerializedName("requestNumber")
    @Expose
    public Long requestNumber;

    @SerializedName("user")
    @Expose
    public String user;

    @SerializedName("kktRegId")
    @Expose
    public String kktRegId;

    @SerializedName("fiscalDriveNumber")
    @Expose
    public String fiscalDriveNumber;

    @SerializedName("items")
    @Expose
    public List<Item> items = null;

    @SerializedName("ecashTotalSum")
    @Expose
    public Long ecashTotalSum;

    @SerializedName("retailPlaceAddress")
    @Expose
    public String retailPlaceAddress;

    @SerializedName("cashTotalSum")
    @Expose
    public Long cashTotalSum;

    @SerializedName("shiftNumber")
    @Expose
    public Long shiftNumber;

}