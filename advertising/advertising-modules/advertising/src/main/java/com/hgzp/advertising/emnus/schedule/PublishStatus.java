package com.hgzp.advertising.emnus.schedule;

/**
 * @author new wei
 * @date 2023/12/14 14:05
 * 发布状态： (0-预约 1-预订 2-待发布 3-安排 4-见报 5-已发布 6-上架 7-下架）
 */
public enum PublishStatus {
    //发布状态： (0-预约 1-预订 2-待发布 3- 4-见报 5-已发布 6-上架 7-下架）
    //2023-12-14 14:05 第一次同用户确认 2-待发布（换成 3 安排） 4-见报（换成 5 已发布） 去掉
    PUBLISH_ORDER(0, "预约"),
    PUBLISH_Reserve(1, "预订"),
    //PUBLISH_Tobereleased(2, "待发布"),
    PUBLISH_Arrange(3, "安排"),
   // PUBLISH_End(4, "见报"),
    PUBLISH_Published(5, "已发布");
//    PUBLISH_ON(6, "上架"),
//    PUBLISH_Off(7, "下架");

    public Integer key;
    public String name;

    PublishStatus(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static String getNameByKey(Integer key) {
        for (PublishStatus value : PublishStatus.values()) {
            if (value.key.equals(key)) {
                return value.name;
            }
        }
        return null;
    }

    public static Integer getKeyByName(String name) {
        for (PublishStatus value : PublishStatus.values()) {
            if (value.name.equals(name)) {
                return value.key;
            }
        }
        return null;
    }

    public static PublishStatus getTypeByKey(Integer key) {
        for (PublishStatus value : PublishStatus.values()) {
            if (value.key.equals(key)) {
                return value;
            }
        }
        return null;
    }
}
