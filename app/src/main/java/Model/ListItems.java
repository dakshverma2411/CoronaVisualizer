package Model;

public class ListItems {
    private int cases;
    private String countryName;

    public int getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(int activeCases) {
        this.activeCases = activeCases;
    }

    public int getRecoveredCases() {
        return recoveredCases;
    }

    public void setRecoveredCases(int recoveredCases) {
        this.recoveredCases = recoveredCases;
    }

    private int activeCases;
    private int recoveredCases;

    public ListItems(int cases, int activeCases,int recoveredCases,String countryName) {
        this.cases = cases;
        this.recoveredCases = recoveredCases;
        this.activeCases = activeCases;
        this.countryName = countryName;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}
