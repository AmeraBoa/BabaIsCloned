package baba.rules;

import baba.block.identifiers.Operator;
import baba.block.Text;

import java.util.*;

/**
 * Handle expression-rule parsing.
 */
public class Parser {
    public void parse(List<Rule> rules) {
        var operators = Operator.values();

        simplifyRuleWithAnd(rules);
        for(Operator operator : operators) {
            for(Rule rule:rules) {
                evaluateRule(rule, operator);
            }
        }
    }

    /**
     * Each rule with an AND operator can be simplified into a set of rule containing no AND operator.
     * @param rules list of rule to be simplified.
     */
    public void simplifyRuleWithAnd(List<Rule> rules) {
        var ruleSet = new HashSet<>(splitAndRule(new ArrayList<>(rules)));
        rules.clear();
        rules.addAll(ruleSet);
    }

    /**
     * Split rules with AND operator. For each operator in a given rule, two new rules are created.
     * Keep splitting until no AND operator are present in the specified list.
     * @param rules the list of rule to be splitted.
     * @return the list of rule with no AND operators.
     */
    private ArrayList<Rule> splitAndRule(ArrayList<Rule> rules) {
        var nextRulesIteration = new ArrayList<>(rules);

        for (Rule rule:rules) {
            var textElem = rule.getTextElements();

            var ruleFromLeftElem = new ArrayList<>(textElem);
            var ruleFromRightElem = new ArrayList<>(textElem);

            for (int i = 0; i < textElem.size(); i++) {
                if (textElem.get(i).equals(Operator.AND)) {
                    var subRule = new SubRule(textElem, i);
                    var currentIndex = 0;

                    /* Creating two new rule. */
                    mergeElementAroundOperator(ruleFromLeftElem, subRule.previous, subRule);
                    mergeElementAroundOperator(ruleFromRightElem, subRule.next, subRule);

                    nextRulesIteration.remove(currentIndex);
                    nextRulesIteration.add(new Rule(ruleFromLeftElem));
                    nextRulesIteration.add(new Rule(ruleFromRightElem));
                    break;
                }
            }
        }

        if(nextRulesIteration.stream().anyMatch(r -> r.getTextElements().contains(Operator.AND))) {
            return splitAndRule(nextRulesIteration);
        }
        return nextRulesIteration;
    }

    /**
     * Collapse the tree specified Text elements and replace it with the a new element.
     * @param ruleAsArray the current array of text elements representing a rule.
     * @param newElement the elements to add instead of the previous current and next elements.
     * @param subRule the sub rule to be removed.
     */
    private void mergeElementAroundOperator(ArrayList<Text> ruleAsArray, Text newElement, SubRule subRule) {
        int currentIndex;

        ruleAsArray.remove(subRule.previous);
        ruleAsArray.remove(subRule.next);
        currentIndex = ruleAsArray.indexOf(subRule.current);
        ruleAsArray.remove(subRule.current);
        ruleAsArray.add(currentIndex, newElement);
    }

    /**
     * Collapse the elements around the operator and the operator itself in the specified rule and
     * replace it with the result of the corresponding operation.
     * @param rule the rules to be evaluated.
     * @param operator the operator to be checked.
     */
    public void evaluateRule(Rule rule, Operator operator) {
        var textElem = rule.getTextElements();
        var newRule = new ArrayList<>(textElem);

        for (int i = 0; i < textElem.size(); i++) {
            if(textElem.get(i) == operator) {
                var subRule = new SubRule(textElem, i);

                newRule.remove(subRule.previous);
                newRule.remove(subRule.next);
                var currentIndex = newRule.indexOf(subRule.current);
                newRule.remove(subRule.current);

                if(currentIndex >= 0 && currentIndex < textElem.size())
                    newRule.add(currentIndex, operator.getOperation().eval(subRule.previous, subRule.next));
            }
        }

        rule.copy(new Rule(newRule));
    }

    /**
     * Most basic rule entity, composed of three elements.
     */
    private class SubRule {
        public Text previous;
        public Text next;
        public Text current;

        private SubRule(List<Text> textElem, int i) {
            previous = i > 0 ? textElem.get(i - 1) : null;
            next = i < textElem.size() - 1 ? textElem.get(i + 1) : null;
            current = textElem.get(i);
        }
    }
}
