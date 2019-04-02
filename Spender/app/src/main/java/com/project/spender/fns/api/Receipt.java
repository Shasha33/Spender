package com.project.spender.fns.api;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Receipt {

    @SerializedName("operationType")
    @Expose
    public Integer operationType;

    @SerializedName("fiscalSign")
    @Expose
    public Integer fiscalSign;

    @SerializedName("dateTime")
    @Expose
    public String dateTime;

    @SerializedName("rawData")
    @Expose
    public String rawData;

    @SerializedName("totalSum")
    @Expose
    public Integer totalSum;

    @SerializedName("nds10")
    @Expose
    public Integer nds10;

    @SerializedName("userInn")
    @Expose
    public String userInn;

    @SerializedName("taxationType")
    @Expose
    public Integer taxationType;

    @SerializedName("operator")
    @Expose
    public String operator;

    @SerializedName("fiscalDocumentNumber")
    @Expose
    public Integer fiscalDocumentNumber;

    @SerializedName("properties")
    @Expose
    public List<Property> properties = null;

    @SerializedName("receiptCode")
    @Expose
    public Integer receiptCode;

    @SerializedName("requestNumber")
    @Expose
    public Integer requestNumber;

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
    public Integer ecashTotalSum;

    @SerializedName("retailPlaceAddress")
    @Expose
    public String retailPlaceAddress;

    @SerializedName("cashTotalSum")
    @Expose
    public Integer cashTotalSum;

    @SerializedName("shiftNumber")
    @Expose
    public Integer shiftNumber;

}