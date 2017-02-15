package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.recycle.RecycleService;

import java.util.Map;

/**
 * @author lpw
 */
public interface ClassifyService extends RecycleService {
    /**
     * 检索分类信息集。
     *
     * @param code 编码前缀。
     * @return 分类信息集。
     */
    JSONObject query(String code);

    /**
     * 检索分类信息树。
     *
     * @param code 编码前缀。
     * @return 分类信息树。
     */
    JSONArray tree(String code);

    /**
     * 查找分类信息。
     *
     * @param ids ID集。
     * @return 分类信息，如果不存在则返回空JSON。
     */
    JSONObject get(String[] ids);

    /**
     * 创建新分类。
     *
     * @param map 参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject create(Map<String, String> map);

    /**
     * 修改分类信息。
     *
     * @param id     ID值。
     * @param code   编码。
     * @param pinyin 拼音码。
     * @param name   名称。
     * @param map    参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject modify(String id, String code, String pinyin, String name, Map<String, String> map);

    /**
     * 刷新缓存。
     */
    void refresh();
}
