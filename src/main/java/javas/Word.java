package javas;

import java.util.ArrayList;
import java.util.List;

class Word {
    private String wordTarget;
    private String pronunciation;
    private boolean saved;
    private List<Meaning> meanings = new ArrayList<>();

    public Word(String wordTarget, String pronunciation) {
        this.wordTarget = wordTarget;
        this.pronunciation = pronunciation;
    }

    public String getWordTarget() {
        return this.wordTarget;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setWord_target(String word_target) {
        this.wordTarget = wordTarget;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public boolean getSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getFirstMeaning() {
        String[] descriptionLines = meanings.get(0).getDescription().split("\\s*-\\s*");
        return descriptionLines[1].trim();
    }

    public void addMeaning(Meaning currentMeaning) {
        meanings.add(currentMeaning);
    }

    public String getMeaning() {
        StringBuilder result = new StringBuilder();

        for (Meaning meaning : getMeanings()) {
            result.append("* ").append(meaning.getPartOfSpeech()).append("\n");

            // Split the description into lines based on the dash '-'
            String[] descriptionLines = meaning.getDescription().split("\\s*-\\s*");

            // Concatenate each line of the description without the leading empty line
            boolean isFirstLine = true;
            for (String line : descriptionLines) {
                if (!isFirstLine) {
                    result.append("   - ").append(line.trim()).append("\n");
                } else {
                    isFirstLine = false;
                }
            }
        }
        return result.toString();
    }


    public String show() {
        return getWordTarget() + " " + getPronunciation() + "\n" + getMeaning();
    }

    public String showWordTargetFirstMeaning() {
        return getWordTarget() + " " + getPronunciation() + "\n" + getFirstMeaning();
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