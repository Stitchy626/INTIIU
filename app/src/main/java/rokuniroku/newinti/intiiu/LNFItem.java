package rokuniroku.newinti.intiiu;

public class LNFItem {

    private String foundDate;
    private String foundTime;
    private String venue;
    private String Description;

    public LNFItem() {
    }

    public LNFItem(String foundDate, String venue, String Description, String foundTime) {
        this.foundDate = foundDate;
        this.venue = venue;
        this.Description = Description;
        this.foundTime = foundTime;
    }

    public String getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(String foundTime) {
        this.foundTime = foundTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(String foundDate) {
        this.foundDate = foundDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
