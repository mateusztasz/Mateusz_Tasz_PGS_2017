package tasz.mateusz.TextManipulation;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created  on 2017-04-07.
 */
public class TextTest {
    @Test
    public void similarityTest() {
        Assert.assertEquals("Similarity should be", 1, Text.similarity("",""),0);
        Assert.assertEquals("Similarity should be", 1, Text.similarity(" "," "),0);
        Assert.assertEquals("Similarity should be", 1, Text.similarity("Test","test"),0);
    }

}