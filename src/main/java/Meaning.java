public class Meaning {
    private String partOfSpeech;
    private String description;

    public Meaning(String partOfSpeech, String description) {
        this.partOfSpeech = partOfSpeech;
        this.description = description;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
