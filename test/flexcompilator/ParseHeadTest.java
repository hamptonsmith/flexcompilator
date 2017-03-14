package flexcompilator;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;
import com.shieldsbetter.flexcompilator.matchers.MCapture;
import com.shieldsbetter.flexcompilator.matchers.MLiteral;
import java.util.NoSuchElementException;
import junit.framework.Assert;
import org.junit.Test;

public class ParseHeadTest {
    @Test
    public void basicCaptureTest()
            throws NoMatchException, WellFormednessException {
        ParseHead h = new ParseHead("something");
        h.advanceOver(new MCapture(new MLiteral("something")));
        
        Assert.assertEquals("something", h.nextCapture());
        
        try {
            h.nextCapture();
            Assert.fail();
        }
        catch (NoSuchElementException nsee) {
            // Expected;
        }
    }
}
