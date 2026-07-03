package cn.qihangerp.common;

import java.util.HashMap;
import java.util.List;

public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String DATA_TAG = "data";

    public AjaxResult() {}

    public AjaxResult(int code, String msg) { super.put(CODE_TAG, code); super.put(MSG_TAG, msg); }

    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code); super.put(MSG_TAG, msg);
        if (data != null) super.put(DATA_TAG, data);
    }

    public static AjaxResult success() { return new AjaxResult(200, "操作成功"); }
    public static AjaxResult success(Object data) { return new AjaxResult(200, "操作成功", data); }
    public static AjaxResult success(String msg, Object data) { return new AjaxResult(200, msg, data); }
    public static AjaxResult error(int code, String msg) { return new AjaxResult(code, msg); }
    public static AjaxResult error(String msg) { return new AjaxResult(500, msg); }

    @Override
    public AjaxResult put(String key, Object value) { super.put(key, value); return this; }

    // 路由 VO（用于前端菜单树）
    public static class RouterVo {
        private String path; private String name; private String component;
        private MetaVo meta; private List<RouterVo> children;

        public String getPath() { return path; } public void setPath(String v) { this.path = v; }
        public String getName() { return name; } public void setName(String v) { this.name = v; }
        public String getComponent() { return component; } public void setComponent(String v) { this.component = v; }
        public MetaVo getMeta() { return meta; } public void setMeta(MetaVo v) { this.meta = v; }
        public List<RouterVo> getChildren() { return children; } public void setChildren(List<RouterVo> v) { this.children = v; }
    }

    public static class MetaVo {
        private String title; private String icon;
        public MetaVo() {}
        public MetaVo(String title, String icon) { this.title = title; this.icon = icon; }
        public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
        public String getIcon() { return icon; } public void setIcon(String v) { this.icon = v; }
    }
}
