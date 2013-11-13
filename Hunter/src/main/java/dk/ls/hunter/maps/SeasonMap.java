package dk.ls.hunter.maps;

public class SeasonMap {

    public SeasonMap(int id, int startMonth, int startDay, int endMonth, int endDay) {
        this.id = id;
        StartMonth = startMonth;
        StartDay = startDay;
        EndMonth = endMonth;
        EndDay = endDay;
    }

    public int id;
    public int StartMonth;
    public int EndMonth;
    public int StartDay;
    public int EndDay;


}
