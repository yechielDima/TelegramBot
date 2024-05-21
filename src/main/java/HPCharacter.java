public class HPCharacter {
    private String id;
    private String name;
    private String gender;
    private String species;
    private String house;
    private String dateOfBirth;
    private String actor;
    private boolean wizard;
    private boolean hogwartsStudent;
    private boolean hogwartsStaff;
    private boolean alive;
    private boolean hasBeanInitialized = false;

    public HPCharacter(String id , String name){
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public String getHouse() {
        return house;
    }
    public void setHouse(String house) {
        this.house = house;
        if (house.equals("")){
            this.house = "not a student";
        }
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        if (dateOfBirth.equals("ul")){
            this.dateOfBirth = "Unknown";
        }
    }
    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
        if (actor.equals("")){
            this.actor = "Unknown";
        }
    }
    public boolean hasBeanInitialized() {
        return hasBeanInitialized;
    }
    public void setHasBeanInitialized(boolean hasBeanInitialized) {
        this.hasBeanInitialized = hasBeanInitialized;
    }
    public boolean isWizard() {
        return wizard;
    }
    public void setWizard(boolean wizard) {
        this.wizard = wizard;
    }
    public boolean isHogwartsStudent() {
        return hogwartsStudent;
    }
    public void setHogwartsStudent(boolean hogwartsStudent) {
        this.hogwartsStudent = hogwartsStudent;
    }
    public boolean isHogwartsStaff() {
        return hogwartsStaff;
    }
    public void setHogwartsStaff(boolean hogwartsStaff) {
        this.hogwartsStaff = hogwartsStaff;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
