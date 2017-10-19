package org.lpw.ranch.weixin;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface WeixinDao {
    PageList<WeixinModel> query();

    WeixinModel findByKey(String key);

    WeixinModel findByAppId(String appId);

    void save(WeixinModel weixin);

    void delete(String id);

    void close();
}
