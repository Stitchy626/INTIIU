package rokuniroku.code.intiiu;

public class LNFItem {

    private String foundDate;
    private String venue;

    public LNFItem() {
    }

    public LNFItem(String foundDate, String venue) {
        this.foundDate = foundDate;
        this.venue = venue;
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
