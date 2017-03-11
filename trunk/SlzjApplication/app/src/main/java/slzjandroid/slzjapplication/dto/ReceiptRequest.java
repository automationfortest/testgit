package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 发票
 * Created by hdb on 2016/3/11.
 */
public class ReceiptRequest implements Serializable {

    private String enterpriseIdx;
    private String title;
    private String ticketAmount;
    private String ticketContent;
    private String contactName;
    private String contactCellphone;
    private String contactAddress;
    private String contactZip;
    private List<String> chargeTransNoList;

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }



    public String getEnterpriseIdx() {
        return enterpriseIdx;
    }

    public void setEnterpriseIdx(String enterpriseIdx) {
        this.enterpriseIdx = enterpriseIdx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(String ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    public String getTicketContent() {
        return ticketContent;
    }

    public void setTicketContent(String ticketContent) {
        this.ticketContent = ticketContent;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactCellphone() {
        return contactCellphone;
    }

    public void setContactCellphone(String contactCellphone) {
        this.contactCellphone = contactCellphone;
    }

    public String getContactZip() {
        return contactZip;
    }

    public void setContactZip(String contactZip) {
        this.contactZip = contactZip;
    }

    public List<String> getChargeTransNoList() {
        return chargeTransNoList;
    }

    public void setChargeTransNoList(List<String> chargeTransNoList) {
        this.chargeTransNoList = chargeTransNoList;
    }


}
