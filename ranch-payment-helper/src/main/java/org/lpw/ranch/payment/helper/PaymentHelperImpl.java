package org.lpw.ranch.payment.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.payment.helper")
public class PaymentHelperImpl implements PaymentHelper, MinuteJob, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private DateTime dateTime;
    @Inject
    private Request request;
    @Inject
    private Carousel carousel;
    @Value("${ranch.payment.key:ranch.payment}")
    private String key;
    private Map<String, PaymentListener> listeners;

    @Override
    public String create(String type, String appId, String user, int amount, String notice, Map<String, String> map) {
        Map<String, String> parameter = getParameter(map);
        parameter.put("type", type);
        parameter.put("appId", appId);
        parameter.put("user", user);
        parameter.put("amount", converter.toString(amount, "0"));
        parameter.put("notice", notice);
        JSONObject object = carousel.service(key + ".create", null, parameter, false, JSONObject.class);

        return object.getString("orderNo");
    }

    @Override
    public String complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map) {
        Map<String, String> parameter = getParameter(map);
        parameter.put("orderNo", orderNo);
        parameter.put("amount", converter.toString(amount, "0"));
        parameter.put("tradeNo", tradeNo);
        parameter.put("state", converter.toString(state, "0"));
        JSONObject object = carousel.service(key + ".complete", null, parameter, false, JSONObject.class);
        System.out.println("#########################################");
        System.out.println(object.toJSONString());
        System.out.println("#########################################");

        return object.getString("orderNo");
    }

    private Map<String, String> getParameter(Map<String, String> map) {
        Map<String, String> parameter = new HashMap<>();
        if (!validator.isEmpty(request.getMap()))
            parameter.putAll(request.getMap());
        if (!validator.isEmpty(map))
            parameter.putAll(map);

        return parameter;
    }

    @Override
    public void executeMinuteJob() {
        if (validator.isEmpty(listeners))
            return;

        Map<String, String> parameter = new HashMap<>();
        parameter.put("start", dateTime.toString(new Date(System.currentTimeMillis() - 30L * TimeUnit.Day.getTime())));
        parameter.put("state", "0");
        parameter.put("pageSize", "1024");
        parameter.put("pageNum", "1");
        JSONArray array = carousel.service(key + ".query", null, parameter, false, JSONObject.class).getJSONArray("list");
        if (array.isEmpty())
            return;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String type = object.getString("type");
            if (!listeners.containsKey(type))
                continue;

            int state = listeners.get(type).getState(object);
            if (state == 1 || state == 2)
                complete(object.getString("orderNo"), object.getIntValue("amount"), object.getString("tradeNo"), state, null);
        }
    }

    @Override
    public int getContextRefreshedSort() {
        return 25;
    }

    @Override
    public void onContextRefreshed() {
        listeners = new HashMap<>();
        BeanFactory.getBeans(PaymentListener.class).forEach(listener -> listeners.put(listener.getType(), listener));
    }
}
