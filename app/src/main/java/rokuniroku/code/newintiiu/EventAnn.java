package rokuniroku.code.newintiiu;

import java.io.Serializable;

public class EventAnn implements Serializable{

    private String title;
    private String venue;
    private String club;
    private String clubEmail;
    private String dateUpload;
    private String timeUpload;
    private String deleteDate;
    private String content;
    private String url;//facebook link
    private String banner;//unique entry key to locate the image in the storage
    private String bannerURL;
    private String bannerBackground;
    private String dateStart;
    private String dateEnd;
    private String timeStart;
    private String timeEnd;
    private String reason;
    private String status;

    public EventAnn(){

    }

    public EventAnn(String title, String venue, String club, String clubEmail, String dateUpload, String timeUpload, String deleteDate, String content, String url, String banner, String bannerURL, String bannerBackground, String dateStart, String dateEnd, String timeStart, String timeEnd, String reason, String status) {
        this.title = title;
        this.venue = venue;
        this.club = club;
        this.clubEmail = clubEmail;
        this.dateUpload = dateUpload;
        this.timeUpload = timeUpload;
        this.deleteDate = deleteDate;
        this.content = content;
        this.url = url;
        this.banner = banner;
        this.bannerURL = bannerURL;
        this.bannerBackground = bannerBackground;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.reason = reason;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getVenue() {
        return venue;
    }

    public String getClub() {
        return club;
    }

    public String getClubEmail() {
        return clubEmail;
    }

    public String getDateUpload() {
        return dateUpload;
    }

    public String getTimeUpload() {
        return timeUpload;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getBanner() {
        return banner;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public String getBannerBackground() {
        return bannerBackground;
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

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
