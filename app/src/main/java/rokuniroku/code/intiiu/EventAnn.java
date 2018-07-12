package rokuniroku.code.intiiu;

import java.io.Serializable;

public class EventAnn implements Serializable{

    private String title;
    private String club;
    private String dateUpload;
    private String timeUpload;
    private String content;
    private String banner;
    private String dateStart;
    private String dateEnd;
    private String timeStart;
    private String timeEnd;
    private String status;

    public EventAnn(){

    }

    public EventAnn(String title, String club, String dateUpload, String timeUpload, String content, String banner, String dateStart, String dateEnd, String timeStart, String timeEnd, String status) {
        this.title = title;
        this.club = club;
        this.dateUpload = dateUpload;
        this.timeUpload = timeUpload;
        this.content = content;
        this.banner = banner;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getClub() {
        return club;
    }

    public String getDateUpload() {
        return dateUpload;
    }

    public String getTimeUpload() {
        return timeUpload;
    }

    public String getContent() {
        return content;
    }

    public String getBanner() {
        return banner;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getStatus() {
        return status;
    }
}
