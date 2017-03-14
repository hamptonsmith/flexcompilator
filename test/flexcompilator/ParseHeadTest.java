package flexcompilator;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;
import com.shieldsbetter.flexcompilator.matchers.MCapture;
import com.shieldsbetter.flexcompilator.matchers.MLiteral;
import junit.framework.Assert;
import org.junit.Test;

public class ParseHeadTest {
    @Test
    public void basicCaptureTest()
            throws NoMatchException, WellFormednessException {
        ParseHead h = new ParseHead("something");
        h.require(new MCapture(new MLiteral("something")));
        
        Assert.assertEquals("something", h.popCapture());
        
        try {
            h.popCapture();
            Assert.fail();
        }
        catch (IllegalStateException ise) {
            // Expected;
        }
    }
}
