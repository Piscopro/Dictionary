package javas;

import java.util.ArrayList;
import java.util.List;

class Word {
    private String wordTarget;
    private String pronunciation;
    private List<Meaning> meanings = new ArrayList<Meaning>();

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

    public String getFirstMeaning() {
        String[] descriptionLines = meanings.get(0).getDescription().split("\\s*-\\s*");
        return descriptionLines[1].trim();
    }

    public void addMeaning(Meaning currentMeaning) {
        meanings.add(currentMeaning);
    }

    public void show() {
        // Print word and pronunciation
        System.out.println(getWordTarget() + " " + getPronunciation());

        // Loop through and print each meaning of the word
        for (Meaning meaning : getMeanings()) {
            System.out.println("*  " + meaning.getPartOfSpeech());

            // Split the description into lines based on the dash '-'
            String[] descriptionLines = meaning.getDescription().split("\\s*-\\s*");

            // Print each line of the description without the leading empty line
            boolean isFirstLine = true;
            for (String line : descriptionLines) {
                if (!isFirstLine) {
                    System.out.println("   - " + line.trim());
                } else {
                    isFirstLine = false;
                }
            }
        }
    }

    public String showWordTargetFirstMeaning() {
        return getWordTarget() + " "
                + getPronunciation() + "\n" + getFirstMeaning();
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