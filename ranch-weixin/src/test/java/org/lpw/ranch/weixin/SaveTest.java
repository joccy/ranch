package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class SaveTest extends TestSupport {
    @Inject
    private WeixinService weixinService;

    @Test
    public void save() throws Exception {
        schedulerAspect.pause();
        Field auto = WeixinServiceImpl.class.getDeclaredField("auto");
        auto.setAccessible(true);
        auto.set(weixinService, false);

        mockHelper.reset();
        mockHelper.mock("/weixin/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2402, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2403, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2404, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2405, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".appId")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2406, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".appId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2407, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".secret")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2408, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".secret"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("token", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2409, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".token"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("mchId", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2410, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".mchId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("mchKey", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".mchKey"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        httpAspect.reset();
        List<String> urls = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            contents.add("");
        httpAspect.get(urls, new ArrayList<>(), new ArrayList<>(), contents);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(4, data.size());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals("app id", data.getString("appId"));
        Assert.assertEquals("secret", data.getString("secret"));
        WeixinModel weixin1 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key"});
        Assert.assertEquals("key", weixin1.getKey());
        Assert.assertNull(weixin1.getName());
        Assert.assertEquals("app id", weixin1.getAppId());
        Assert.assertEquals("secret", weixin1.getSecret());
        Assert.assertNull(weixin1.getToken());
        Assert.assertNull(weixin1.getMchId());
        Assert.assertNull(weixin1.getMchKey());
        Assert.assertNull(weixin1.getAccessToken());
        Assert.assertNull(weixin1.getJsapiTicket());
        Assert.assertNull(weixin1.getTime());
        Assert.assertEquals(0, urls.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2412, object.getIntValue("code"));
        Assert.assertEquals(message.get(WeixinModel.NAME + ".exists"), object.getString("message"));

        auto.set(weixinService, true);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret 1");
        mockHelper.getRequest().addParameter("secret", "secret 1");
        mockHelper.getRequest().addParameter("token", "token 1");
        mockHelper.getRequest().addParameter("mchId", "mch id 1");
        mockHelper.getRequest().addParameter("mchKey", "mch key 1");
        mockHelper.getRequest().addParameter("accessToken", "access token 1");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 1");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:05");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(7, data.size());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals("app id", data.getString("appId"));
        Assert.assertEquals("secret 1", data.getString("secret"));
        Assert.assertEquals("token 1", data.getString("token"));
        Assert.assertEquals("mch id 1", data.getString("mchId"));
        Assert.assertEquals("mch key 1", data.getString("mchKey"));
        weixin1 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key"});
        Assert.assertEquals("key", weixin1.getKey());
        Assert.assertNull(weixin1.getName());
        Assert.assertEquals("app id", weixin1.getAppId());
        Assert.assertEquals("secret 1", weixin1.getSecret());
        Assert.assertEquals("token 1", weixin1.getToken());
        Assert.assertEquals("mch id 1", weixin1.getMchId());
        Assert.assertEquals("mch key 1", weixin1.getMchKey());
        Assert.assertNull(weixin1.getAccessToken());
        Assert.assertNull(weixin1.getJsapiTicket());
        Assert.assertNull(weixin1.getTime());
        Assert.assertEquals(1, urls.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("appId", "app id 2");
        mockHelper.getRequest().addParameter("secret", "secret 2");
        mockHelper.getRequest().addParameter("token", "token 2");
        mockHelper.getRequest().addParameter("mchId", "mch id 2");
        mockHelper.getRequest().addParameter("mchKey", "mch key 2");
        mockHelper.getRequest().addParameter("accessToken", "access token 2");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 2");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:22");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(8, data.size());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals("name 2", data.getString("name"));
        Assert.assertEquals("app id 2", data.getString("appId"));
        Assert.assertEquals("secret 2", data.getString("secret"));
        Assert.assertEquals("token 2", data.getString("token"));
        Assert.assertEquals("mch id 2", data.getString("mchId"));
        Assert.assertEquals("mch key 2", data.getString("mchKey"));
        WeixinModel weixin2 = liteOrm.findById(WeixinModel.class, weixin1.getId());
        Assert.assertEquals("key", weixin2.getKey());
        Assert.assertEquals("name 2", weixin2.getName());
        Assert.assertEquals("app id 2", weixin2.getAppId());
        Assert.assertEquals("secret 2", weixin2.getSecret());
        Assert.assertEquals("token 2", weixin2.getToken());
        Assert.assertEquals("mch id 2", weixin2.getMchId());
        Assert.assertEquals("mch key 2", weixin2.getMchKey());
        Assert.assertNull(weixin2.getAccessToken());
        Assert.assertNull(weixin2.getJsapiTicket());
        Assert.assertNull(weixin2.getTime());
        Assert.assertEquals(2, urls.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", weixin1.getId());
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("appId", "app id 3");
        mockHelper.getRequest().addParameter("secret", "secret 3");
        mockHelper.getRequest().addParameter("token", "token 3");
        mockHelper.getRequest().addParameter("mchId", "mch id 3");
        mockHelper.getRequest().addParameter("mchKey", "mch key 3");
        mockHelper.getRequest().addParameter("accessToken", "access token 3");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 3");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:33");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(8, data.size());
        Assert.assertEquals("key 3", data.getString("key"));
        Assert.assertEquals("name 3", data.getString("name"));
        Assert.assertEquals("app id 3", data.getString("appId"));
        Assert.assertEquals("secret 3", data.getString("secret"));
        Assert.assertEquals("token 3", data.getString("token"));
        Assert.assertEquals("mch id 3", data.getString("mchId"));
        Assert.assertEquals("mch key 3", data.getString("mchKey"));
        WeixinModel weixin3 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key 3"});
        Assert.assertNotEquals(weixin1.getId(), weixin3.getId());
        Assert.assertEquals("key 3", weixin3.getKey());
        Assert.assertEquals("name 3", weixin3.getName());
        Assert.assertEquals("app id 3", weixin3.getAppId());
        Assert.assertEquals("secret 3", weixin3.getSecret());
        Assert.assertEquals("token 3", weixin3.getToken());
        Assert.assertEquals("mch id 3", weixin3.getMchId());
        Assert.assertEquals("mch key 3", weixin3.getMchKey());
        Assert.assertNull(weixin3.getAccessToken());
        Assert.assertNull(weixin3.getJsapiTicket());
        Assert.assertNull(weixin3.getTime());
        Assert.assertEquals(3, urls.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", weixin1.getId());
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("name", "name 4");
        mockHelper.getRequest().addParameter("appId", "app id 3");
        mockHelper.getRequest().addParameter("secret", "secret 3");
        mockHelper.getRequest().addParameter("token", "token 4");
        mockHelper.getRequest().addParameter("mchId", "mch id 4");
        mockHelper.getRequest().addParameter("mchKey", "mch key 4");
        mockHelper.getRequest().addParameter("accessToken", "access token 4");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 4");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:44");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(8, data.size());
        Assert.assertEquals("key 3", data.getString("key"));
        Assert.assertEquals("name 4", data.getString("name"));
        Assert.assertEquals("app id 3", data.getString("appId"));
        Assert.assertEquals("secret 3", data.getString("secret"));
        Assert.assertEquals("token 4", data.getString("token"));
        Assert.assertEquals("mch id 4", data.getString("mchId"));
        Assert.assertEquals("mch key 4", data.getString("mchKey"));
        WeixinModel weixin4 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key 3"});
        Assert.assertNotEquals(weixin1.getId(), weixin4.getId());
        Assert.assertEquals("key 3", weixin4.getKey());
        Assert.assertEquals("name 4", weixin4.getName());
        Assert.assertEquals("app id 3", weixin4.getAppId());
        Assert.assertEquals("secret 3", weixin4.getSecret());
        Assert.assertEquals("token 4", weixin4.getToken());
        Assert.assertEquals("mch id 4", weixin4.getMchId());
        Assert.assertEquals("mch key 4", weixin4.getMchKey());
        Assert.assertNull(weixin4.getAccessToken());
        Assert.assertNull(weixin4.getJsapiTicket());
        Assert.assertNull(weixin4.getTime());
        Assert.assertEquals(3, urls.size());

        auto.set(weixinService, false);
        schedulerAspect.press();
    }
}
