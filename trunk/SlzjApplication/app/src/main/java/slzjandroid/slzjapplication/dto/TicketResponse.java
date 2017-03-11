package slzjandroid.slzjapplication.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuyifei on 16/5/9.
 */
public class TicketResponse implements Serializable {
    private String message;

    private Result result;
    private int status;
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Result implements Serializable {
        private List<TicketList> ticketList;

        public List<TicketList> getTicketList() {
            return ticketList;
        }

        public void setTicketList(List<TicketList> ticketList) {
            this.ticketList = ticketList;
        }


    }

    public class TicketList implements Serializable {

        private String contactAddress;
        private String contactCellphone;
        private String contactName;
        private String logisticsName;
        private String logisticsNo;
        private String ticketAmount;
        private String ticketStatus;
        private String ticketTitle;
        private String ticketTransNo;

        public String getTicketTransNo() {
            return ticketTransNo;
        }

        public void setTicketTransNo(String ticketTransNo) {
            this.ticketTransNo = ticketTransNo;
        }

        public String getContactAddress() {
            return contactAddress;
        }

        public void setContactAddress(String contactAddress) {
            this.contactAddress = contactAddress;
        }

        public String getContactCellphone() {
            return contactCellphone;
        }

        public void setContactCellphone(String contactCellphone) {
            this.contactCellphone = contactCellphone;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getLogisticsName() {
            return logisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            this.logisticsName = logisticsName;
        }

        public String getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }

        public String getTicketAmount() {
            return ticketAmount;
        }

        public void setTicketAmount(String ticketAmount) {
            this.ticketAmount = ticketAmount;
        }

        public String getTicketStatus() {
            return ticketStatus;
        }

        public void setTicketStatus(String ticketStatus) {
            this.ticketStatus = ticketStatus;
        }

        public String getTicketTitle() {
            return ticketTitle;
        }

        public void setTicketTitle(String ticketTitle) {
            this.ticketTitle = ticketTitle;
        }


    }

}
