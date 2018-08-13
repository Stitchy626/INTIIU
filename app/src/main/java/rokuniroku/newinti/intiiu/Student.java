package rokuniroku.newinti.intiiu;

public class Student {

    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String logID;
    private String status;
    private String priority;
    private String remark;
    private String venue;

    public Student(){
    }

    public Student(String id, String name, String startDate ,String endDate, String startTime, String endTime, String logID, String status, String priority, String remark, String venue) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.logID = logID;
        this.status = status;
        this.priority = priority;
        this.remark = remark;
        this.venue = venue;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLogID() {
        return logID;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public String getRemark() {
        return remark;
    }

    public String getVenue() {
        return venue;
    }
}
