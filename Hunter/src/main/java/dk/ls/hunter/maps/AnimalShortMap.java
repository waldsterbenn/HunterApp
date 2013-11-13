package dk.ls.hunter.maps;

public class AnimalShortMap {

    public String name;
    public int id;

    public AnimalShortMap(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
