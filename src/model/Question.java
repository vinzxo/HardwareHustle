package model;

public class Question {
    private String text;
    private String[] options;
    private String correctAns;

    public Question(String text, String a, String b, String c, String d, String ans) {
        this.text = text;
        this.options = new String[]{a, b, c, d};
        this.correctAns = ans;
    }

    // Getters - Encapsulation in action
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public String getCorrectAns() { return correctAns; }
}