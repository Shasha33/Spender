package com.project.spender.fns.api.data.Json;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Receipt {

    @SerializedName("operationType")
    @Expose
    public long operationType;

    @SerializedName("fiscalSign")
    @Expose
    public long fiscalSign;

    @SerializedName("dateTime")
    @Expose
    public String dateTime;

    @SerializedName("rawData")
    @Expose
    public String rawData;

    @SerializedName("totalSum")
    @Expose
    public long totalSum;

    @SerializedName("nds10")
    @Expose
    public long nds10;

    @SerializedName("userInn")
    @Expose
    public String userInn;

    @SerializedName("taxationType")
    @Expose
    public long taxationType;

    @SerializedName("operator")
    @Expose
    public String operator;

    @SerializedName("fiscalDocumentNumber")
    @Expose
    public long fiscalDocumentNumber;

    @SerializedName("properties")
    @Expose
    public List<Property> properties = null;

    @SerializedName("receiptCode")
    @Expose
    public long receiptCode;

    @SerializedName("requestNumber")
    @Expose
    public long requestNumber;

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
    public long ecashTotalSum;

    @SerializedName("retailPlaceAddress")
    @Expose
    public String retailPlaceAddress;

    @SerializedName("cashTotalSum")
    @Expose
    public long cashTotalSum;

    @SerializedName("shiftNumber")
    @Expose
    public long shiftNumber;

}