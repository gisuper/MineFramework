package com.yx.framework.net.ws;

/**
 * Created by yangxiong on 2018/12/21.
 */
public enum WSEventType {
    /** 匹配变化 */
     PUSH_TYPE_MATCH_CHANGE,
    /** 匹配成功 */
    PUSH_TYPE_PRE_CREATE_ROOM,
    /** 创建游戏成功*/
    PUSH_TYPE_CREATE_ROOM_SUCCESS,
    /** 开始回合 */
     PUSH_TYPE_BEGIN_ROUND,
    /** 选择格子成功 */
    PUSH_TYPE_CHOSE_SUCCESS,
    /** 结束回合 */
     PUSH_TYPE_FINISH_ROUND,
    /** 结束游戏 */
     PUSH_TYPE_FINISH_GAME
}
