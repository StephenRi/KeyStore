package utils;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class JSONObjectTest {
    @Test
    public void testNewJSON() {
        JSONObject json = new JSONObject();
        Assert.assertEquals(json.toString(), "{}");
        Assert.assertEquals(json.isEmpty(), true);
    }
}
