package rokuniroku.code.intiiu;

import java.io.Serializable;

public class INTIAnn implements Serializable{

    private String title;
    private String category;
    private String dateUpload;
    private String timeUpload;
    private String courtDate;
    private String content;
    private String banner;
    private String dateOccur;
    private String timeOccur;

    public INTIAnn(){

    }

    public INTIAnn(String title, String category, String dateUpload, String timeUpload, String courtDate, String content, String banner, String dateOccur, String timeOccur) {
        this.title = title;
        this.category = category;
        this.dateUpload = dateUpload;
        this.timeUpload = timeUpload;
        this.courtDate = courtDate;
        this.content = content;
        this.banner = banner;
        this.dateOccur = dateOccur;
        this.timeOccur = timeOccur;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDateUpload() {
        return dateUpload;
    }

    public String getTimeUpload() {
        return timeUpload;
    }

    public String getCourtDate() {
        return courtDate;
    }

    public String getContent() {
        return content;
    }

    public String getBanner() {
        return banner;
    }

    public String getDateOccur() {
        return dateOccur;
    }

    public String getTimeOccur() {
        return timeOccur;
    }
}
