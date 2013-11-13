package dk.ls.hunter.maps;

/**
 * A dummy item representing a piece of content.
 */
public class AnimalMap {
    public String id;
    public String name;
    public String latinName;
    public String Description;

    public AmmoMap ammo;
    public AnimalCatagoryMap animalCatagory;
    public ShotDistanceMap shotDistance;
    public SeasonMap season;

    public AnimalMap(int id, String name, String latinName, String Description, AmmoMap ammo,
                     ShotDistanceMap shotDistance, SeasonMap season, AnimalCatagoryMap animalCatagory) {
        this.id = String.valueOf(id);
        this.name = name;
        this.latinName = latinName;
        this.Description = Description;
        this.ammo = ammo;
        this.shotDistance = shotDistance;
        this.season = season;
        this.animalCatagory = animalCatagory;
    }

    @Override
    public String toString() {
        return name;
    }
}