package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class SendTest extends TestSupport {
//    @Test
    public void send() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "-1");
        mockHelper.mock("/message/send");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1801, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".type"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "2");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1801, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".type"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1802, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(MessageModel.NAME + ".receiver")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", "receiver id");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1802, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(MessageModel.NAME + ".receiver")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "-1");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1803, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".format"), 0, 7), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "8");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1803, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".format"), 0, 7), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1804, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(MessageModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1805, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-match-regex", message.get(MessageModel.NAME + ".code"), "^[a-zA-Z0-9]{1,64}$"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.getRequest().addParameter("code", "code value");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1805, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-match-regex", message.get(MessageModel.NAME + ".code"), "^[a-zA-Z0-9]{1,64}$"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.getRequest().addParameter("code", "codevalue");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        String receiver = generator.uuid();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", receiver);
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.getRequest().addParameter("code", "codevalue");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1806, object.getIntValue("code"));
        Assert.assertEquals(message.get(MessageModel.NAME + ".friend.not-exists"), object.getString("message"));

        receiver = generator.uuid();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "1");
        mockHelper.getRequest().addParameter("receiver", receiver);
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.getRequest().addParameter("code", "codevalue");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1807, object.getIntValue("code"));
        Assert.assertEquals(message.get(MessageModel.NAME + ".group.not-exists"), object.getString("message"));

        String groupId = generator.uuid();
        for (int i = 0; i < 2; i++) {
            receiver = generator.uuid();
            mockCarousel.register("ranch.friend.get", "{\"code\":0,\"data\":{\"" + receiver + "\":{\"id\":\"" + receiver + "\",\"user\":\"friend user id\"}}}");
            mockHelper.reset();
            mockHelper.getRequest().addParameter("type", "0");
            mockHelper.getRequest().addParameter("receiver", receiver);
            mockHelper.getRequest().addParameter("format", "1");
            mockHelper.getRequest().addParameter("content", "friend content");
            mockHelper.getRequest().addParameter("code", "code0");
            mockHelper.mock("/message/send");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            MessageModel message = find(0);
            Assert.assertEquals(message.getId(), object.getString("data"));
            Assert.assertEquals("sign in id", message.getSender());
            Assert.assertEquals(0, message.getType());
            Assert.assertEquals("friend user id", message.getReceiver());
            Assert.assertEquals(1, message.getFormat());
            Assert.assertEquals("friend content", message.getContent());
            Assert.assertTrue(System.currentTimeMillis() - message.getTime().getTime() < 2000L);
            Assert.assertEquals("code0", message.getCode());

            mockCarousel.register("ranch.group.member.find", "{\"code\":0,\"data\":{\"id\":\"member id\"}}");
            mockHelper.reset();
            mockHelper.getRequest().addParameter("type", "1");
            mockHelper.getRequest().addParameter("receiver", groupId);
            mockHelper.getRequest().addParameter("format", "2");
            mockHelper.getRequest().addParameter("content", "group content");
            mockHelper.getRequest().addParameter("code", "code1");
            mockHelper.mock("/message/send");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            message = find(1);
            Assert.assertEquals(message.getId(), object.getString("data"));
            Assert.assertEquals("member id", message.getSender());
            Assert.assertEquals(1, message.getType());
            Assert.assertEquals(groupId, message.getReceiver());
            Assert.assertEquals(2, message.getFormat());
            Assert.assertEquals("group content", message.getContent());
            Assert.assertTrue(System.currentTimeMillis() - message.getTime().getTime() < 2000L);
            Assert.assertEquals("code1", message.getCode());
        }
    }

    private MessageModel find(int type) {
        PageList<MessageModel> pl = liteOrm.query(new LiteQuery(MessageModel.class).where("c_type=?"), new Object[]{type});
        Assert.assertEquals(1, pl.getList().size());

        return pl.getList().get(0);
    }
}
