package io.cucumber.gherkin;

import io.cucumber.messages.Messages.Envelope;
import io.cucumber.messages.Messages.GherkinDocument;
import io.cucumber.messages.Messages.Pickle;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.cucumber.gherkin.Gherkin.makeSourceEnvelope;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class GherkinTest {
    @Test
    public void provides_access_to_the_ast() {
        List<Envelope> envelopes = toList(Gherkin.fromPaths(singletonList("testdata/good/minimal.feature"), false, true, false));

        // Get the AST
        GherkinDocument gherkinDocument = envelopes.get(0).getGherkinDocument();

        // Get the Feature node of the AST
        GherkinDocument.Feature feature = gherkinDocument.getFeature();
        assertEquals("Minimal", feature.getName());

        // Get the first Scenario node of the Feature node
        GherkinDocument.Feature.Scenario scenario = feature.getChildren(0).getScenario();
        assertEquals("minimalistic", scenario.getName());
    }

    @Test
    public void provides_access_to_pickles_which_are_compiled_from_the_ast() {
        List<Envelope> envelopes = toList(Gherkin.fromPaths(singletonList("testdata/good/scenario_outline.feature"), false, false, true));

        // Get the first pickle
        Pickle pickle = envelopes.get(0).getPickle();

        // Get the first step of the pickle
        Pickle.PickleStep step = pickle.getSteps(0);
        assertEquals("the minimalism", step.getText());
    }

    @Test
    public void parses_supplied_source() {
        Envelope envelope = makeSourceEnvelope("Feature: Minimal\n" +
                "\n" +
                "  Scenario: minimalistic\n" +
                "    Given the minimalism\n", "test.feature");
        List<Envelope> envelopes = toList(Gherkin.fromSources(singletonList(envelope), false, true, false));

        GherkinDocument gherkinDocument = envelopes.get(0).getGherkinDocument();
        GherkinDocument.Feature feature = gherkinDocument.getFeature();
        assertEquals("Minimal", feature.getName());
    }

    private static <T> List<T> toList(Iterable<T> iterable) {
        List<T> result = new ArrayList<>();
        for (T item : iterable) {
            result.add(item);
        }
        return result;
    }
}
