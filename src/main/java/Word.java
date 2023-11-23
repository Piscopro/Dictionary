import java.util.ArrayList;
import java.util.List;

class Word {
    private String word_target;
    private String pronunciation;
    private List<Meaning> meanings = new ArrayList<Meaning>();

    public Word(String word_target, String pronunciation) {
        this.word_target = word_target;
        this.pronunciation = pronunciation;
    }

    public String getWordTarget() {
        return this.word_target;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public String getFirstMeaning() {
        String[] descriptionLines = meanings.get(0).getDescription().split("\\s*-\\s*");
        return descriptionLines[1].trim();
    }

    public void addMeaning(Meaning currentMeaning) {
        meanings.add(currentMeaning);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}


