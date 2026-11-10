package tick_sightings.model;

public class TickSighting {

    private String id;
    private String date;
    private String location;
    private String species;
    private String latinName;
    public String getId() {
        return id;
    }

    public void setId(String i) {
        id = i;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String dt) {
        date = dt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String loc) {
        location = loc;
    }

    public String getSpecies() {
       return species;
    }

    public void setSpecies(String sp) {
        species = sp;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String ln) {
        latinName = ln;
    }
}