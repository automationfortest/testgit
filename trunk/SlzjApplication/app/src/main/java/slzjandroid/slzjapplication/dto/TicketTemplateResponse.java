package slzjandroid.slzjapplication.dto;

import java.io.Serializable;

/**
 * Created by xuyifei on 16/5/11.
 */
public class TicketTemplateResponse implements Serializable {
    private String message;
    private Result result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result implements Serializable {

        private String contactAddress;
        private String contactCellphone;
        private String contactName;
        private String contactZip;
        private String ticketContent;
        private String title;

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

        public String getContactZip() {
            return contactZip;
        }

        public void setContactZip(String contactZip) {
            this.contactZip = contactZip;
        }

        public String getTicketContent() {
            return ticketContent;
        }

        public void setTicketContent(String ticketContent) {
            this.ticketContent = ticketContent;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


    }

}
