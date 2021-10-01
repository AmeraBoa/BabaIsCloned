package baba.rules;

import baba.block.BlockID;
import baba.block.Text;

import java.util.*;

/**
 * A list of Text elements.
 */
public class Rule {
    private final ArrayList<Text> elements = new ArrayList<>();

    public Rule(List<String> elements) {
        for (String item:elements) {
            var id = BlockID.valueOf(item);
            if(id instanceof Text) {
                this.elements.add((Text) id);
            }
        }
    }

    public Rule(String ... elements) {
        this(Arrays.asList(elements));
    }

    public Rule(ArrayList<Text> rule) {
        this.elements.addAll(rule);
    }

    public Iterator<Text> listIterator() {
        return elements.listIterator();
    }

    public List<Text> getTextElements() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    public void copy(Rule rule) {
        this.elements.clear();
        this.elements.addAll(rule.elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(elements, rule.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    public Rule clone() {
        return new Rule(this.elements);
    }
}
