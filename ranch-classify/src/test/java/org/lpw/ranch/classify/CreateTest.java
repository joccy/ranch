package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
    @Test
    public void create() {
        mockHelper.reset();
        mockHelper.mock("/classify/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1204, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1205, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".value")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("value", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1206, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".value"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("value", "value");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1207, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".name")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("value", "value");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1208, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("value", "value");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.getRequest().addParameter("value", "value 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equals(data, 1);
        Assert.assertFalse(data.containsKey("json"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.getRequest().addParameter("value", "value 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1210, object.getIntValue("code"));
        Assert.assertEquals(message.get(ClassifyModel.NAME + ".code-value.exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("value", "value 2");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("json", "json 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equals(data, 2);
        Assert.assertEquals("json 2", data.getString("json"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("value", "value 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("json", "{\"id\":\"id\",\"name\":\"name\"}");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equals(data, 3);
        JSONObject json = data.getJSONObject("json");
        Assert.assertEquals("id", json.getString("id"));
        Assert.assertEquals("name", json.getString("name"));
    }
}
