package com.n2n.letsgo.Dictionary;

public class DictionaryModel {
    String word,meaning;

    public DictionaryModel(String word, String meaning) {

        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
