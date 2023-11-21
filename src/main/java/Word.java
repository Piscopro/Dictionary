import java.util.Objects;

class Word {
    private String word_target;
    String word_explain;
    private String pronunciation;

    public Word(String word_target, String pronunciation, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.pronunciation = pronunciation;
    }

    public String getWordTarget() {
        return this.word_target;
    }

    public String getWordExplain() {
        return this.word_explain;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word)) return false;
        return Objects.equals(word_target, word.word_target) && Objects.equals(word_explain, word.word_explain) && Objects.equals(getPronunciation(), word.getPronunciation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(word_target, word_explain, getPronunciation());
    }
}
