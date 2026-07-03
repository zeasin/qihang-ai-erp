package cn.qihangerp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;




import java.util.Date;
import java.util.List;

public class SysMenu {
    @TableId(type = IdType.AUTO)
    private Long menuId; private String menuName; private Long parentId;
    private Integer orderNum; private String path; private String component;
    private String perms; private String icon; private String menuType;
    private String visible; private String status; private Date createTime;
    private String remark;
    private List<SysMenu> children;

    public Long getMenuId() { return menuId; } public void setMenuId(Long v) { this.menuId = v; }
    public String getMenuName() { return menuName; } public void setMenuName(String v) { this.menuName = v; }
    public Long getParentId() { return parentId; } public void setParentId(Long v) { this.parentId = v; }
    public Integer getOrderNum() { return orderNum; } public void setOrderNum(Integer v) { this.orderNum = v; }
    public String getPath() { return path; } public void setPath(String v) { this.path = v; }
    public String getComponent() { return component; } public void setComponent(String v) { this.component = v; }
    public String getPerms() { return perms; } public void setPerms(String v) { this.perms = v; }
    public String getIcon() { return icon; } public void setIcon(String v) { this.icon = v; }
    public String getMenuType() { return menuType; } public void setMenuType(String v) { this.menuType = v; }
    public String getVisible() { return visible; } public void setVisible(String v) { this.visible = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date v) { this.createTime = v; }
    public String getRemark() { return remark; } public void setRemark(String v) { this.remark = v; }
    public List<SysMenu> getChildren() { return children; } public void setChildren(List<SysMenu> v) { this.children = v; }
}
